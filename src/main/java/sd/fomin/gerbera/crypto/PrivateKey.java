package sd.fomin.gerbera.crypto;

import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.util.Base58CheckUtils;
import sd.fomin.gerbera.util.HexUtils;
import sd.fomin.gerbera.util.ApplicationRandom;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sd.fomin.gerbera.crypto.Numbers.*;

public class PrivateKey {

    private final BigInteger key;
    private final boolean compressed;

    private PrivateKey(BigInteger key, boolean compressed) {
        this.key = key;
        this.compressed = compressed;
    }

    public static PrivateKey ofWif(boolean mainNet, String wif) {
        validateWifFormat(mainNet, wif);

        boolean compressed = false;

        byte prefix = mainNet ? (byte) 0x80 : (byte) 0xEF;
        byte[] decoded = Base58CheckUtils.decode(wif);
        if (decoded[0] != prefix) {
            throw new IllegalArgumentException("Decoded WIF must start with 0x" + HexUtils.asString(prefix) + " byte");
        }

        byte[] pkBytes;
        if (decoded.length == 33) {
            pkBytes = Arrays.copyOfRange(decoded, 1, decoded.length);
        } else if (decoded.length == 34) {
            if (decoded[decoded.length - 1] != 0x01) {
                throw new IllegalArgumentException("The last byte of decoded compressed WIF is expected to be 0x01");
            }
            compressed = true;
            pkBytes = Arrays.copyOfRange(decoded, 1, decoded.length - 1);
        } else {
            throw new IllegalArgumentException("Incorrect WIF length: " + decoded.length);
        }

        return new PrivateKey(new BigInteger(HexUtils.asString(pkBytes), 16), compressed);
    }

    private static void validateWifFormat(boolean mainNet, String wif) {
        List<Character> prefixWif = singletonList(mainNet ? '5' : '9');
        List<Character> prefixWifComp = mainNet ? asList('K', 'L', 'T') : singletonList('c');
        char prefix = wif.charAt(0);

        if (!prefixWif.contains(prefix) && !prefixWifComp.contains(prefix)) {
            throw new IllegalArgumentException("WIF must start with " + prefixWif + " (for uncompressed) " +
                    "or " + prefixWifComp + " (for compressed)");
        }
    }

    public byte[] sign(byte[] bytesToSign) {
        BigInteger numToSign = new BigInteger(HexUtils.asString(bytesToSign), 16);

        BigInteger r;
        BigInteger s;
        do {
            BigInteger bigRnd = getBigRandom();
            r = CurveDot.ofGTimes(bigRnd).x().mod(N);
            s = bigRnd.modInverse(N).multiply(numToSign.add(key.multiply(r))).mod(N);
        } while (!isRValid(r) || !isSValid(s));

        ByteBuffer sequence = new ByteBuffer();

        byte[] rPart = getSigPart(r);
        sequence.append((byte) 0x02);
        sequence.append((byte) rPart.length);
        sequence.append(rPart);

        byte[] sPart = getSigPart(s);
        sequence.append((byte) 0x02);
        sequence.append((byte) sPart.length);
        sequence.append(sPart);

        sequence.putFirst((byte) sequence.size());
        sequence.putFirst((byte) 0x30);

        return sequence.bytes();
    }

    private BigInteger getBigRandom() {
        return new BigInteger(N.bitLength(), ApplicationRandom.get())
                .mod(N.subtract(BigInteger.ONE))
                .add(BigInteger.ONE);
    }

    public byte[] getPublicKey() {
        CurveDot publicKeyDot = CurveDot.ofGTimes(key);
        ByteBuffer bytes = new ByteBuffer();
        if (compressed) {
            byte signPrefix = (byte) (publicKeyDot.y().mod(new BigInteger("2")).intValue() + 2);
            bytes.append(signPrefix);

            byte[] xHex = HexUtils.asBytesAligned(publicKeyDot.x().toString(16), 32);
            bytes.append(xHex);
        } else {
            bytes.append((byte) 0x04);

            byte[] xHex = HexUtils.asBytesAligned(publicKeyDot.x().toString(16), 32);
            bytes.append(xHex);

            byte[] yHex = HexUtils.asBytesAligned(publicKeyDot.y().toString(16), 32);
            bytes.append(yHex);
        }

        return bytes.bytes();
    }

    private byte[] getSigPart(BigInteger number) {
        ByteBuffer result = new ByteBuffer();
        result.append(HexUtils.asBytes(number.toString(16)));
        if (result.bytes()[0] < 0) {
            result.putFirst((byte) 0x00);
        }
        return result.bytes();
    }

    private boolean isRValid(BigInteger r) {
        return r.compareTo(BigInteger.ZERO) > 0;
    }

    private boolean isSValid(BigInteger s) {
        return s.compareTo(BigInteger.ZERO) > 0 && s.compareTo(S_MAX) <= 0;
    }


}
