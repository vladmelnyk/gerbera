package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

@BuildingProcessor
@CommandAliases({"donate"})
public class DonateProcessor extends Processor {

    @Override
    protected TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {
        try {
            return builder.donate(Long.parseLong(arguments.get(0)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Donation must be integer");
        }
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList(
                "amount to donate (satoshi)"
        );
    }
}
