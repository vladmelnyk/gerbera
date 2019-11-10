package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.types.Coin;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public interface ScriptPubKeyProducer {

    byte[] produceScript(byte[] hash);

    static ScriptPubKeyProducer getInstance(boolean mainNet, Coin coin, byte prefix) {
        List<Byte> prefixesP2PKHMainnet;
        List<Byte> prefixesP2PKHTestnet;
        List<Byte> prefixesP2SHMainnet;
        List<Byte> prefixesP2SHTestnet;

        switch (coin) {
            case BTC:
            case BCH:
                prefixesP2PKHMainnet = singletonList((byte) 0x00);
                prefixesP2PKHTestnet = singletonList((byte) 0x6F);
                prefixesP2SHMainnet = singletonList((byte) 0x05);
                prefixesP2SHTestnet = singletonList((byte) 0xC4);
                break;
            case LTC:
                prefixesP2PKHMainnet = singletonList((byte) 0x30);
                prefixesP2PKHTestnet = singletonList((byte) 0x6F);
                prefixesP2SHMainnet = asList((byte) 0x05, (byte) 0x32);
                prefixesP2SHTestnet = singletonList((byte) 0xC4);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + coin);
        }
        if ((mainNet ? prefixesP2PKHMainnet : prefixesP2PKHTestnet).contains(prefix)) {
            //P2PKH
            return hash -> {
                ByteBuffer lockingScript = new ByteBuffer();
                lockingScript.append(OpCodes.DUP, OpCodes.HASH160);
                lockingScript.append(OpSize.ofInt(hash.length).getSize());
                lockingScript.append(hash);
                lockingScript.append(OpCodes.EQUALVERIFY, OpCodes.CHECKSIG);
                return lockingScript.bytes();
            };
        } else if ((mainNet ? prefixesP2SHMainnet : prefixesP2SHTestnet).contains(prefix)) {
            //P2SH
            return hash -> {
                ByteBuffer lockingScript = new ByteBuffer();
                lockingScript.append(OpCodes.HASH160);
                lockingScript.append(OpSize.ofInt(hash.length).getSize());
                lockingScript.append(hash);
                lockingScript.append(OpCodes.EQUAL);
                return lockingScript.bytes();
            };
            //TODO Provide appropriate condition to verify P2WSH
        } else {
            //P2WSH & P2WKH
            return hash -> {
                ByteBuffer lockingScript = new ByteBuffer();
                lockingScript.append(OpCodes.FALSE);
                lockingScript.append(OpSize.ofInt(hash.length).getSize());
                lockingScript.append(hash);

                return lockingScript.bytes();
            };
        }

//        throw new IllegalArgumentException(String.format(ErrorMessages.SPK_UNSUPPORTED_PRODUCER, mainNet, prefix));
    }

}

