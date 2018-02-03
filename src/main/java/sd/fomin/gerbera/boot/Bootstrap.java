package sd.fomin.gerbera.boot;

import sd.fomin.gerbera.transaction.TransactionBuilder;

import java.util.Arrays;
import java.util.Scanner;

public class Bootstrap {

    private static TransactionBuilder builder = null;

    public static void main(String[] args) {
        BuildWrapper buildWrapper = new BuildWrapper();
        Scanner in = new Scanner(System.in);
        String line;
        printState(buildWrapper.state());
        while (!"exit".equals(line = readLine(in))) {
            try {
                buildWrapper.processLine(line);
            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
            }

            printState(buildWrapper.state());
        }
        in.close();
    }

    private static void printState(String state) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nCurrent state:");
        Arrays.stream(state.split("\n")).forEach(l ->
                builder.append("\n| ").append(l)
        );
        System.out.println(builder);
    }

    private static String readLine(Scanner in) {
        System.out.print("\n> ");
        return in.nextLine();
    }

}
