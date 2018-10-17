package sd.fomin.gerbera.util;

import java.io.ByteArrayOutputStream;

public class Bech32CheckUtils {

    private static final String ALPHABET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    private Bech32CheckUtils() {

    }

    public static byte[] decode(final String bech32) {
        var lowerCaseBech = bech32.toLowerCase();
        int prefixPos = lowerCaseBech.lastIndexOf("1");

        byte[] humanReadablePrefix = lowerCaseBech.substring(0, prefixPos).getBytes();

        byte[] data = new byte[lowerCaseBech.length() - prefixPos - 1];
        for (int j = 0, i = prefixPos + 1; i < lowerCaseBech.length(); i++, j++) {
            data[j] = (byte) ALPHABET.indexOf(lowerCaseBech.charAt(i));
        }

        if (!verifyChecksum(humanReadablePrefix, data)) {
            throw new IllegalArgumentException("invalid bech32 checksum");
        }

        byte[] ret = new byte[data.length - 6];
        System.arraycopy(data, 0, ret, 0, ret.length);

        return ret;
    }

    public static byte[] convertBits(byte[] data, int frombits, int tobits) {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int maxv = (1 << tobits) - 1;
        for (byte aData : data) {
            int value = aData & 0xff;
            acc = (acc << frombits) | value;
            bits += frombits;
            while (bits >= tobits) {
                bits -= tobits;
                baos.write((acc >>> bits) & maxv);
            }
        }

        return baos.toByteArray();
    }

    private static boolean verifyChecksum(byte[] hrp, byte[] data) {
        byte[] exp = hrpExpand(hrp);

        byte[] values = new byte[exp.length + data.length];
        System.arraycopy(exp, 0, values, 0, exp.length);
        System.arraycopy(data, 0, values, exp.length, data.length);

        return (1 == polymod(values));
    }

    private static byte[] hrpExpand(byte[] hrp) {
        byte[] buf1 = new byte[hrp.length];
        byte[] buf2 = new byte[hrp.length];
        byte[] mid = new byte[1];

        for (int i = 0; i < hrp.length; i++) {
            buf1[i] = (byte) (hrp[i] >> 5);
        }

        for (int i = 0; i < hrp.length; i++) {
            buf2[i] = (byte) (hrp[i] & 0x1f);
        }

        byte[] ret = new byte[(hrp.length * 2) + 1];
        System.arraycopy(buf1, 0, ret, 0, buf1.length);
        System.arraycopy(mid, 0, ret, buf1.length, mid.length);
        System.arraycopy(buf2, 0, ret, buf1.length + mid.length, buf2.length);

        return ret;
    }

    private static int polymod(byte[] values) {
        final int[] GENERATORS = {0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3};

        int chk = 1;

        for (byte b : values) {
            byte top = (byte) (chk >> 0x19);
            chk = b ^ ((chk & 0x1ffffff) << 5);
            for (int i = 0; i < 5; i++) {
                chk ^= ((top >> i) & 1) == 1 ? GENERATORS[i] : 0;
            }
        }

        return chk;
    }
}
