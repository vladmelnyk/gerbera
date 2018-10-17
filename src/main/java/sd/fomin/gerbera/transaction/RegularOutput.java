package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.util.Base58CheckUtils;
import sd.fomin.gerbera.util.Bech32CheckUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sd.fomin.gerbera.util.ValidationUtils.isBase58;
import static sd.fomin.gerbera.util.ValidationUtils.isBech32;
import static sd.fomin.gerbera.util.ValidationUtils.isEmpty;

class RegularOutput extends Output {

    private final boolean mainNet;
    private final String destination;
    private final byte[] decodedAddress;
    private final boolean isBech32;

    RegularOutput(boolean mainNet, long satoshi, String destination, OutputType type) {
        super(type, satoshi);

        validateOutputData(mainNet, destination);

        this.mainNet = mainNet;
        this.destination = destination;
        this.isBech32 = !isBase58(destination) && isBech32(destination);
        this.decodedAddress = isBech32 ? decodeBech32Address(destination) : Base58CheckUtils.decode(destination);
    }

    @Override
    protected byte[] getLockingScript() {
        return isBech32
                ? ScriptPubKeyProducer.getInstance(mainNet, decodedAddress[0]).produceScript(decodedAddress)
                : ScriptPubKeyProducer.getInstance(mainNet, decodedAddress[0])
                .produceScript(Arrays.copyOfRange(decodedAddress, 1, decodedAddress.length));
    }

    @Override
    public String toString() {
        return destination + " " + satoshi;
    }

    private void validateOutputData(boolean mainNet, String destination) {
        validateDestinationAddress(mainNet, destination);
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
        List<Character> prefixBech32 = mainNet ? asList('b', 'c') : asList('t', 'b');

        char prefix = destination.charAt(0);
        if (!prefixP2PKH.contains(prefix) && !prefixP2SH.contains(prefix) && !prefixBech32.contains(prefix)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, prefixP2PKH, prefixP2SH, prefixBech32));
        }
    }

    private byte[] decodeBech32Address(String bech32) {
        byte[] decoded = Bech32CheckUtils.decode(bech32);
        byte[] dataWithNoVersion = new byte[decoded.length - 1];
        System.arraycopy(decoded, 1, dataWithNoVersion, 0, dataWithNoVersion.length);

        return Bech32CheckUtils.convertBits(dataWithNoVersion, 5, 8);
    }
}
