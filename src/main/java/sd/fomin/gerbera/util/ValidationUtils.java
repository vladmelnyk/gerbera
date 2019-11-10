package sd.fomin.gerbera.util;

import sd.fomin.gerbera.types.Coin;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isTransactionId(String stirng) {
        return stirng.matches("\\p{XDigit}{64}");
    }

    public static boolean isHexString(String string) {
        return string.matches("\\p{XDigit}+");
    }

    public static boolean isBase58(String string) {
        return string.matches("[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]+");
    }

    public static boolean isBech32(String string, Coin coin) {
        List<String> prefixList;
        switch (coin) {

            case BTC:
                prefixList = asList("bc1", "tb1");
                break;
            case BCH:
//                TODO: revisit later
                prefixList = singletonList("bitcoincash:q");
                break;
            case LTC:
                prefixList = singletonList("ltc1");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + coin);
        }
        String prefix = String.join("|", prefixList);
        String regexp = String.format("^(%s)[a-zA-HJ-NP-Z0-9]{25,39}$", prefix);
        return string.matches(regexp);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
