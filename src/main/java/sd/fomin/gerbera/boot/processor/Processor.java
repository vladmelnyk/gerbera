package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.processor.annotation.BuilderNotRequired;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

public abstract class Processor {

    private String[] aliases;

    public Processor() {
        CommandAliases aliasesAnnotation = getClass().getAnnotation(CommandAliases.class);
        if (aliasesAnnotation == null) {
            throw new RuntimeException("No command aliases given for processor");
        }
        aliases = aliasesAnnotation.value();
        if (aliases == null || aliases.length == 0) {
            throw new RuntimeException("Processor must have at least one alias");
        }
    }

    public TransactionBuilder process(TransactionBuilder builder, List<String> arguments) {
        validate(builder, arguments);
        return doProcess(builder, arguments);
    }

    private void validate(TransactionBuilder builder, List<String> arguments) {
        if (requiresBuilder() && builder == null) {
            throw new RuntimeException("Initialize new builder first");
        }

        if (arguments.size() < getArgumentDescriptions().size()) {
            StringBuilder error = new StringBuilder();
            error.append("Command requires ")
                    .append(getArgumentDescriptions().size()).append(" argument(s):");
            for (String desc : getArgumentDescriptions()) {
                error.append("\n   * " + desc);
            }
            throw new RuntimeException(error.toString());
        }
    }

    public boolean supportsCommand(String command) {
        return Arrays.stream(aliases).filter(alias -> command.equals(alias)).count() > 0;
    }

    protected abstract TransactionBuilder doProcess(TransactionBuilder builder, List<String> arguments);

    protected abstract List<String> getArgumentDescriptions();

    protected boolean requiresBuilder() {
        return getClass().getAnnotation(BuilderNotRequired.class) == null;
    }

}
