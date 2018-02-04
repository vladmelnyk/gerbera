package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

@BuildingProcessor
@CommandAliases({"to", "output", "out"})
public class ToProcessor extends Processor {

    @Override
    protected TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {
        String destination = arguments.get(0);
        try {
            return builder.to(destination, Long.parseLong(arguments.get(1)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Amount must be integer");
        }
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList(
                "destination address",
                "amount (satoshi)"
        );
    }

}
