package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.Transaction;

import java.util.Collections;
import java.util.List;

@CommandAliases({"raw", "hex"})
public class RawProcessor extends Processor {


    @Override
    protected String stringifyTransaction(Transaction transaction) {
        return transaction.getRawTransaction();
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Collections.emptyList();
    }

}
