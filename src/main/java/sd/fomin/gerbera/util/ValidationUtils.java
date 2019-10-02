package sd.fomin.gerbera.util;

public class ValidationUtils {

    private ValidationUtils() { }

    public static boolean isTransactionId(String stirng) {
        return stirng.matches("\\p{XDigit}{64}");
    }

    public static boolean isHexString(String string) {
        return string.matches("\\p{XDigit}+");
    }

    public static boolean isBase58(String string) {
        return string.matches("[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]+");
    }

    public static boolean isBech32(String string) {
        return string.matches("^(bc1|tb1|ltc1)[a-zA-HJ-NP-Z0-9]{25,39}$");
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
