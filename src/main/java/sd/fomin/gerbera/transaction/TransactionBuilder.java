package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.constant.SigHashType;
import sd.fomin.gerbera.types.Coin;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.types.UInt;
import sd.fomin.gerbera.types.VarInt;
import sd.fomin.gerbera.util.HashUtils;
import sd.fomin.gerbera.util.HexUtils;

import java.util.LinkedList;
import java.util.List;

public class TransactionBuilder {

    private static final UInt VERSION = UInt.of(1);
    private static final byte SEGWIT_MARKER = (byte) 0x00;
    private static final byte SEGWIT_FLAG = (byte) 0x01;
    private static final UInt LOCK_TIME = UInt.of(0);
    private static final Integer BTC_DUST_AMOUNT = 546;

    private final boolean mainNet;
    private final Coin coin;

    private final List<Input> inputs = new LinkedList<>();
    private final List<Output> outputs = new LinkedList<>();

    private String changeAddress;
    private long fee;
    private long donate;

    private TransactionBuilder(boolean mainNet, Coin coin) {
        this.mainNet = mainNet;
        this.coin = coin;
    }

    public static TransactionBuilder create() {
        return new TransactionBuilder(true, Coin.BTC);
    }

    public static TransactionBuilder create(boolean mainNet, Coin coin) {
        return new TransactionBuilder(mainNet, coin);
    }

    public TransactionBuilder from(String fromTransactionBigEnd, int fromToutNumber, String closingScript, long satoshi, String wif) {
        inputs.add(new Input(mainNet, coin, fromTransactionBigEnd, fromToutNumber, closingScript, satoshi, wif));
        return this;
    }

    public TransactionBuilder from(String fromTransactionBigEnd, int fromToutNumber, String closingScript, long satoshi, String wif, Boolean replaceable) {
        inputs.add(new Input(mainNet, coin, fromTransactionBigEnd, fromToutNumber, closingScript, satoshi, wif, replaceable));
        return this;
    }

    public TransactionBuilder rmInputAt(int i) {
        int index = i - 1;
        if (index < 0 || index >= inputs.size()) {
            throw new RuntimeException("No input found with index " + i);
        }
        inputs.remove(index);
        return this;
    }

    public TransactionBuilder to(String address, long value) {
        outputs.add(new RegularOutput(mainNet, coin, value, address, OutputType.CUSTOM));
        return this;
    }

    public TransactionBuilder put(String data, long value) {
        outputs.add(new UnspendableOutput(data, value));
        return this;
    }

    public TransactionBuilder putNullUnspendableOutput(String data) {
        outputs.add(new UnspendableOutput(data));
        return this;
    }

    public TransactionBuilder rmOutputAt(int i) {
        int index = i - 1;
        if (index < 0 || index >= outputs.size()) {
            throw new RuntimeException("No output found with index " + i);
        }
        outputs.remove(index);
        return this;
    }

    public TransactionBuilder changeTo(String changeAddress) {
        this.changeAddress = changeAddress;
        return this;
    }

    public TransactionBuilder withFee(long fee) {
        if (fee < 0) {
            throw new IllegalArgumentException(ErrorMessages.FEE_NEGATIVE);
        }
        this.fee = fee;
        return this;
    }

    public TransactionBuilder donate(long donate) {
        if (donate < 0) {
            throw new IllegalArgumentException("Donation must not be less than zero");
        }
        this.donate = donate;
        return this;
    }

    public Transaction build() {
        if (inputs.isEmpty()) {
            throw new IllegalStateException("Transaction must contain at least one input");
        }

        List<Output> buildOutputs = new LinkedList<>(outputs);

        long change = getChange();
        if (change >= BTC_DUST_AMOUNT) {
            buildOutputs.add(new RegularOutput(mainNet, coin, change, changeAddress, OutputType.CHANGE));
        }

        if (buildOutputs.isEmpty()) {
            throw new IllegalStateException("Transaction must contain at least one output");
        }

        boolean buildSegWitTransaction = containsSegwitInput();

        Transaction transaction = new Transaction();
        transaction.addData("Version", VERSION.toString());
        if (buildSegWitTransaction) {
            transaction.addData("Marker", HexUtils.asString(SEGWIT_MARKER), false);
            transaction.addData("Flag", HexUtils.asString(SEGWIT_FLAG), false);
        }

        transaction.addData("Input count", VarInt.of(inputs.size()).toString());
        List<byte[]> witnesses = new LinkedList<>();
        for (int i = 0; i < inputs.size(); i++) {
            byte[] sigHash = getSigHash(buildOutputs, i);
            inputs.get(i).fillTransaction(sigHash, transaction);
            if (buildSegWitTransaction) {
                witnesses.add(inputs.get(i).getWitness(sigHash));
            }
        }

        transaction.addData("Output count", VarInt.of(buildOutputs.size()).toString());
        buildOutputs.forEach(output -> output.fillTransaction(transaction));

        if (buildSegWitTransaction) {
            transaction.addHeader("Witnesses");
            witnesses.forEach(w ->
                    transaction.addData("   Witness", HexUtils.asString(w), false)
            );
        }

        transaction.addData("Locktime", LOCK_TIME.toString());

        transaction.setFee(fee);

        return transaction;
    }

    private byte[] getSigHash(List<Output> buildOutputs, int signedInputIndex) {
        ByteBuffer signBase = new ByteBuffer();

        signBase.append(VERSION.asLitEndBytes());
        signBase.append(SigPreimageProducer.getInstance(inputs.get(signedInputIndex).isSegWit())
                .producePreimage(inputs, buildOutputs, signedInputIndex));
        signBase.append(LOCK_TIME.asLitEndBytes());
        signBase.append(SigHashType.ALL.asLitEndBytes());

        return HashUtils.sha256(HashUtils.sha256(signBase.bytes()));
    }

    private long getChange() {
        long income = inputs.stream().mapToLong(Input::getSatoshi).sum();
        long outcome = outputs.stream().mapToLong(Output::getSatoshi).sum();
        long change = income - outcome - fee;

        if (change < 0) {
            throw new IllegalStateException("Not enough satoshi. All inputs: " + income +
                    ". All outputs with fee: " + (outcome + fee));
        }

        if (change > BTC_DUST_AMOUNT && changeAddress == null) {
            throw new IllegalStateException("Transaction contains change (" + change + " satoshi) but no address to send them to");
        }

        return change;
    }

    private boolean containsSegwitInput() {
        return inputs.stream().anyMatch(Input::isSegWit);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Network: ").append(mainNet ? "MainNet" : "TestNet");
        if (inputs.size() > 0) {
            result.append("\nSegwit transaction: ").append(containsSegwitInput() ? true : false);

            result.append("\nInputs: ").append(inputs.stream().mapToLong(Input::getSatoshi).sum());
            for (int i = 0; i < inputs.size(); i++) {
                result.append("\n   ").append(i + 1).append(". ").append(inputs.get(i));
            }
        }
        if (outputs.size() > 0) {
            result.append("\nOutputs: ").append(outputs.stream().mapToLong(Output::getSatoshi).sum());
            for (int i = 0; i < outputs.size(); i++) {
                result.append("\n   ").append(i + 1).append(". ").append(outputs.get(i));
            }
        }
        if (changeAddress != null) {
            result.append("\nChange to: ").append(changeAddress);
        }
        if (fee > 0) {
            result.append("\nFee: ").append(fee);
        }
        if (donate > 0) {
            result.append("\nDonate: ").append(donate);
        }
        result.append("\n");

        return result.toString();
    }
}
