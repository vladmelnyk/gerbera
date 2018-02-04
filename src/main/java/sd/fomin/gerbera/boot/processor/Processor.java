package sd.fomin.gerbera.boot.processor;

import sd.fomin.gerbera.boot.ConsoleResult;
import sd.fomin.gerbera.boot.processor.annotation.BuilderNotRequired;
import sd.fomin.gerbera.boot.processor.annotation.BuildingProcessor;
import sd.fomin.gerbera.boot.processor.annotation.CommandAliases;
import sd.fomin.gerbera.transaction.Transaction;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.List;

public abstract class Processor {

    private String[] aliases;
    private boolean buildingProcessor;

    public Processor() {
        CommandAliases aliasesAnnotation = getClass().getAnnotation(CommandAliases.class);
        if (aliasesAnnotation == null) {
            throw new RuntimeException("No command aliases given for processor");
        }
        aliases = aliasesAnnotation.value();
        if (aliases == null || aliases.length == 0) {
            throw new RuntimeException("Processor must have at least one alias");
        }
        buildingProcessor = getClass().getAnnotation(BuildingProcessor.class) != null;
    }

    public ConsoleResult process(ConsoleResult wrapper, List<String> arguments) {
        validate(wrapper, arguments);
        if (buildingProcessor) {
            TransactionBuilder builder = processBuilder(wrapper.getBuilder(), arguments);
            return new ConsoleResult(builder, null);
        } else {
            Transaction transaction = wrapper.getTransaction();
            if (transaction == null) {
                transaction = wrapper.getBuilder().build();
            }
            System.out.println(stringifyTransaction(transaction, arguments));
            return new ConsoleResult(wrapper.getBuilder(), transaction);
        }
    }

    private void validate(ConsoleResult wrapper, List<String> arguments) {
        if (requiresBuilder() && wrapper.getBuilder() == null) {
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

    protected TransactionBuilder processBuilder(TransactionBuilder builder, List<String> arguments) {
        throw new UnsupportedOperationException("Processor " + getClass().getSimpleName() + " must implement the method");
    }

    protected String stringifyTransaction(Transaction transaction, List<String> arguments) {
        throw new UnsupportedOperationException("Processor " + getClass().getSimpleName() + " must implement the method");
    }

    protected abstract List<String> getArgumentDescriptions();

    protected boolean requiresBuilder() {
        return getClass().getAnnotation(BuilderNotRequired.class) == null;
    }

}
