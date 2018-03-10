package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

@BuildingProcessor
@CommandAliases({"put", "putdata"})
public class PutDataProcessor extends Processor {

    @Override
    protected TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {
        String data = arguments.get(0);
        try {
            return builder.put(data, Long.parseLong(arguments.get(1)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Amount must be integer");
        }
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList(
                "data bytes (hex string) to put",
                "amount (satoshi)"
        );
    }
}
