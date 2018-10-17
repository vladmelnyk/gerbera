package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;

public interface ScriptPubKeyProducer {

    byte[] produceScript(byte[] hash);

    static ScriptPubKeyProducer getInstance(boolean mainNet, byte prefix) {
        if (prefix == (mainNet ? (byte) 0x00 : (byte) 0x6F)) {
            //P2PKH
            return hash -> {
                ByteBuffer lockingScript = new ByteBuffer();
                lockingScript.append(OpCodes.DUP, OpCodes.HASH160);
                lockingScript.append(OpSize.ofInt(hash.length).getSize());
                lockingScript.append(hash);
                lockingScript.append(OpCodes.EQUALVERIFY, OpCodes.CHECKSIG);
                return lockingScript.bytes();
            };
        } else if (prefix == (mainNet ? (byte) 0x05 : (byte) 0xC4)) {
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

