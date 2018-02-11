package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.crypto.PrivateKey;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.types.UInt;
import sd.fomin.gerbera.types.ULong;
import sd.fomin.gerbera.types.VarInt;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.util.HashUtils;
import sd.fomin.gerbera.util.HexUtils;

import java.util.List;

public interface SigPreimageProducer {

    byte[] producePreimage(List<Input> inputs, List<Output> outputs, int singingInputIndex);

    static SigPreimageProducer getInstance(boolean segwit) {
        if (segwit) {
            return (inputs, outputs, singingInputIndex) -> {
                ByteBuffer result = new ByteBuffer();
                Input currentInput = inputs.get(singingInputIndex);

                ByteBuffer prevOuts = new ByteBuffer(); //hashPrevOuts
                for (Input input : inputs) {
                    prevOuts.append(input.getTransactionHashBytesLitEnd());
                    prevOuts.append(UInt.of(input.getIndex()).asLitEndBytes());
                }
                result.append(HashUtils.sha256(HashUtils.sha256(prevOuts.bytes())));

                ByteBuffer sequences = new ByteBuffer(); //hashSequences
                for (Input input : inputs) {
                    sequences.append(input.getSequence().asLitEndBytes());
                }
                result.append(HashUtils.sha256(HashUtils.sha256(sequences.bytes())));

                result.append(currentInput.getTransactionHashBytesLitEnd()); //outpoint
                result.append(UInt.of(currentInput.getIndex()).asLitEndBytes());

                PrivateKey privateKey = currentInput.getPrivateKey();
                byte[] pkh = HashUtils.ripemd160(HashUtils.sha256(privateKey.getPublicKey())); //scriptCode
                ByteBuffer scriptCode = new ByteBuffer(OpCodes.DUP, OpCodes.HASH160, (byte) 0x14);
                scriptCode.append(pkh);
                scriptCode.append(OpCodes.EQUALVERIFY, OpCodes.CHECKSIG);
                scriptCode.putFirst(OpSize.ofInt(scriptCode.size()).getSize());
                result.append(scriptCode.bytes());

                result.append(ULong.of(currentInput.getSatoshi()).asLitEndBytes()); //amount in

                result.append(currentInput.getSequence().asLitEndBytes()); //sequence

                ByteBuffer outs = new ByteBuffer(); //hash outs
                outputs.stream().map(Output::serializeForSigHash).forEach(outs::append);
                result.append(HashUtils.sha256(HashUtils.sha256(outs.bytes())));

                return result.bytes();
            };
        } else {
            return (inputs, outputs, singingInputIndex) -> {
                ByteBuffer result = new ByteBuffer();

                result.append(VarInt.of(inputs.size()).asLitEndBytes());
                for (int i = 0; i < inputs.size(); i++) {
                    Input input = inputs.get(i);
                    result.append(input.getTransactionHashBytesLitEnd());
                    result.append(UInt.of(input.getIndex()).asLitEndBytes());

                    if (i == singingInputIndex) {
                        byte[] lockBytes = HexUtils.asBytes(input.getLock());
                        result.append(VarInt.of(lockBytes.length).asLitEndBytes());
                        result.append(lockBytes);
                    } else {
                        result.append(VarInt.of(0).asLitEndBytes());
                    }

                    result.append(input.getSequence().asLitEndBytes());
                }

                result.append(VarInt.of(outputs.size()).asLitEndBytes());
                outputs.stream().map(Output::serializeForSigHash).forEach(result::append);

                return result.bytes();
            };
        }
    }
}
