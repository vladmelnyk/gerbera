package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.Transaction;

import java.util.Collections;
import java.util.List;

@CommandAliases({"split"})
public class SplitProcessor extends Processor {

    @Override
    protected String stringifyTransaction(Transaction transaction, List<String> arguments) {
        return transaction.getSplitTransaction();
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Collections.emptyList();
    }

}
