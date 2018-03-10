package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.util.HexUtils;
import sd.fomin.gerbera.util.ValidationUtils;

public class UnspendableOutput extends Output {

    private static final int MAX_DATA_SIZE = 40;

    private final String data;
    private final long satoshi;
    private final byte[] dataBytes;

    public UnspendableOutput(String data, long satoshi) {
        super(OutputType.UNSPENDABLE, satoshi);

        validateData(data);

        this.data = data;
        this.satoshi = satoshi;
        this.dataBytes = HexUtils.asBytes(data);
    }

    long getSatoshi() {
        return satoshi;
    }

    @Override
    public String toString() {
        return "[U] " + data + " " + satoshi;
    }

    @Override
    protected byte[] getLockingScript() {
        ByteBuffer result = new ByteBuffer();
        result.append(OpCodes.RETURN);
        result.append(OpSize.ofInt(dataBytes.length).getSize());
        result.append(dataBytes);
        return result.bytes();
    }

    private void validateData(String data) {
        if (ValidationUtils.isEmpty(data)) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_DATA_IS_EMPTY);
        }

        if (!ValidationUtils.isHexString(data)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.OUTPUT_DATA_IS_NOT_HEX));
        }

        byte[] bytes = HexUtils.asBytes(data);
        if (bytes.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException(String.format(ErrorMessages.OUTPUT_DATA_BYTES_HUGE, bytes.length, MAX_DATA_SIZE));
        }
    }

}
