package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.types.ULong;
import sd.fomin.gerbera.types.VarInt;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.util.HexUtils;

public abstract class Output {

    protected final OutputType type;
    protected final long satoshi;

    protected Output(OutputType type, long satoshi) {
        validateData(type, satoshi);

        this.type = type;
        this.satoshi = satoshi;
    }

    byte[] serializeForSigHash() {
        ByteBuffer serialized = new ByteBuffer();

        serialized.append(ULong.of(satoshi).asLitEndBytes());

        byte[] lockingScript = getLockingScript();
        serialized.append(VarInt.of(lockingScript.length).asLitEndBytes());
        serialized.append(lockingScript);

        return serialized.bytes();
    }

    void fillTransaction(Transaction transaction) {
        transaction.addHeader("   Output (" + type.getDesc() + ")");

        transaction.addData("      Satoshi", ULong.of(satoshi).toString());
        byte[] lockingScript = getLockingScript();
        transaction.addData("      Lock length", VarInt.of(lockingScript.length).toString());
        transaction.addData("      Lock", HexUtils.asString(lockingScript));
    }

    protected abstract byte[] getLockingScript();

    long getSatoshi() {
        return satoshi;
    }

    private void validateData(OutputType type, long satoshi) {
        if (type == null) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_TYPE_NULL);
        }
        if (satoshi <= 0) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_AMOUNT_NOT_POSITIVE);
        }
    }

}
