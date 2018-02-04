package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

@BuildingProcessor
@CommandAliases({"changeto", "change"})
public class ChangeToProcessor extends Processor {

    @Override
    protected TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {
        return builder.changeTo(arguments.get(0));
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList(
                "destination address"
        );
    }
}
