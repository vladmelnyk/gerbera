package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.types.ULong;
import sd.fomin.gerbera.types.VarInt;
import sd.fomin.gerbera.util.Base58CheckUtils;
import sd.fomin.gerbera.util.HexUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sd.fomin.gerbera.util.ValidationUtils.isBase58;
import static sd.fomin.gerbera.util.ValidationUtils.isEmpty;

class Output {

    private final boolean mainNet;
    private final long satoshi;
    private final String destination;
    private final OutputType type;
    private final byte[] decodedAddress;

    Output(boolean mainNet, long satoshi, String destination, OutputType type) {
        validateOutputData(mainNet, satoshi, destination);

        this.mainNet = mainNet;
        this.satoshi = satoshi;
        this.destination = destination;
        this.type = type;
        this.decodedAddress = Base58CheckUtils.decode(destination);
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

    long getSatoshi() {
        return satoshi;
    }

    @Override
    public String toString() {
        return destination + " " + satoshi;
    }

    private byte[] getLockingScript() {
        return ScriptPubKeyProducer.getInstance(mainNet, decodedAddress[0])
                .produceScript(Arrays.copyOfRange(decodedAddress, 1, decodedAddress.length));
    }

    private void validateOutputData(boolean mainNet, long satoshi, String destination) {
        validateDestinationAddress(mainNet, destination);
        validateAmount(satoshi);
    }

    private void validateAmount(long satoshi) {
        if (satoshi <= 0) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_AMOUNT_NOT_POSITIVE);
        }
    }

    private void validateDestinationAddress(boolean mainNet, String destination) {
        if (isEmpty(destination)) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_ADDRESS_EMPTY);
        }

        if (!isBase58(destination)) {
            throw new IllegalArgumentException(ErrorMessages.OUTPUT_ADDRESS_NOT_BASE_58);
        }

        List<Character> prefixP2PKH = mainNet ? singletonList('1') : asList('m', 'n');
        List<Character> prefixP2SH = singletonList(mainNet ? '3' : '2');
        char prefix = destination.charAt(0);

        if (!prefixP2PKH.contains(prefix) && !prefixP2SH.contains(prefix)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, prefixP2PKH, prefixP2SH));
        }
    }
}
