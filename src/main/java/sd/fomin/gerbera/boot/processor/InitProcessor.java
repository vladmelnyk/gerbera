package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuilderNotRequired;
import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;
import sd.fomin.gerbera.types.Coin;

import java.util.Arrays;
import java.util.List;

@BuildingProcessor
@BuilderNotRequired
@CommandAliases({"init", "create"})
public class InitProcessor extends Processor {

    @Override
    public TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {

        String network = arguments.get(0);
        Coin coin = Coin.valueOf(arguments.get(1));
        if ("mainnet".equalsIgnoreCase(network)) {
            return TransactionBuilder.create(true, coin);
        } else if ("testnet".equalsIgnoreCase(network)) {
            return TransactionBuilder.create(false, coin);
        } else {
            throw new RuntimeException("First argument must be 'mainnet' or 'testnet'");
        }
    }

    @Override
    protected List<String> getArgumentDescriptions() {
        return Arrays.asList(
                "network (mainnet or testnet)"
        );
    }

}
