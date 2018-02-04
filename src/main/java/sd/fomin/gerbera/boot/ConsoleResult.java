package sd.fomin.gerbera.boot;

import sd.fomin.gerbera.transaction.Transaction;
import sd.fomin.gerbera.transaction.TransactionBuilder;

public class ConsoleResult {

    private TransactionBuilder builder;
    private Transaction transaction;

    public ConsoleResult() { }

    public ConsoleResult(TransactionBuilder builder, Transaction transaction) {
        this.builder = builder;
        this.transaction = transaction;
    }

    public TransactionBuilder getBuilder() {
        return builder;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
