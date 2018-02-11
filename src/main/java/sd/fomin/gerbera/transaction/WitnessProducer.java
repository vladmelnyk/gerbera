package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.SigHashType;
import sd.fomin.gerbera.crypto.PrivateKey;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;

public interface WitnessProducer {

    byte[] produceWitness(byte[] sigHash, PrivateKey key);

    static WitnessProducer getInstance(boolean segwit) {
        if (segwit) {
            return (sigHash, key) -> {
                ByteBuffer result = new ByteBuffer();

                result.append((byte) 0x02);

                ByteBuffer sign = new ByteBuffer(key.sign(sigHash));
                sign.append(SigHashType.ALL.asByte());
                result.append(OpSize.ofInt(sign.size()).getSize());
                result.append(sign.bytes());

                byte[] pubKey = key.getPublicKey();
                result.append(OpSize.ofInt(pubKey.length).getSize());
                result.append(pubKey);

                return result.bytes();
            };
        } else {
            return (sigHash, key) -> new byte[] {(byte) 0x00};
        }
    }
}
