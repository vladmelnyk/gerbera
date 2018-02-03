package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

public class RmFromProcessor extends Processor {

    @Override
    protected TransactionBuilder doProcess(TransactionBuilder builder, List<String> arguments) {
        try {
            return builder.rmInputAt(Integer.parseInt(arguments.get(0)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Index must be positive integer");
        }
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList("index (index of input to remove)");
    }
}
