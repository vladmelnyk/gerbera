package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.constant.SigHashType;
import sd.fomin.gerbera.crypto.PrivateKey;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.util.HashUtils;

public interface ScriptSigProducer {

    byte[] produceScriptSig(byte[] sigHash, PrivateKey key);

    static ScriptSigProducer getInstance(boolean segwit) {
        if (segwit) {
            return (sigHash, key) -> {
                ByteBuffer result = new ByteBuffer();

                result.append(OpCodes.FALSE);
                result.append((byte) 0x14); //ripemd160 size
                result.append(HashUtils.ripemd160(HashUtils.sha256(key.getPublicKey())));
                result.putFirst(OpSize.ofInt(result.size()).getSize()); //PUSH DATA

                return result.bytes();
            };
        } else {
            return (sigHash, key) -> {
                ByteBuffer result = new ByteBuffer();

                result.append(key.sign(sigHash));
                result.append(SigHashType.ALL.asByte());

                result.putFirst(OpSize.ofInt(result.size()).getSize());

                byte[] publicKey = key.getPublicKey();
                result.append(OpSize.ofInt(publicKey.length).getSize());
                result.append(publicKey);

                return result.bytes();
            };
        }

    }
}
