package sd.fomin.gerbera.transaction;

import org.junit.Test;

public class TransactionInputValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullTransaction() {
        TransactionBuilder.create()
                .from(
                        null,
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTransaction() {
        TransactionBuilder.create()
                .from(
                        "   ",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongLengthTransaction() {
        TransactionBuilder.create()
                .from(
                        "00000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotHexTransaction() {
        TransactionBuilder.create()
                .from(
                        "000000000000000000000000000000Q000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndex() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        -1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullScript() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        null,
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyScript() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "   ",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotHexScript() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a9140000000000000000Q0000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroSatochi() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        0,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSatochi() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        -1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectPKHScriptFormatOpDup() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "75a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectPKHScriptFormatOpHash() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a814000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectPKHScriptFormatOpEqual() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000087ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectPKHScriptFormatOpChecksig() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ad",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectPKHScriptSize() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a913000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectP2SHScriptFormatOpDup() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a814000000000000000000000000000000000000000087",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectP2SHScriptFormatOpHash160() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000088",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectP2SHScriptSize() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a913000000000000000000000000000000000000000087",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWif() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000087",
                        1,
                        null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWif() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000087",
                        1,
                        "   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBase58Wif() {
        TransactionBuilder.create()
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000087",
                        1,
                        "OcV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongChecksumWif() {
        TransactionBuilder.create(false)
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000087",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwB");
    }

    @Test
    public void testCorrectWithPKHScript() {
        TransactionBuilder.create(false)
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "a914000000000000000000000000000000000000000087",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

    @Test
    public void testCorrectWithP2SHScript() {
        TransactionBuilder.create(false)
                .from(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        1,
                        "76a914000000000000000000000000000000000000000088ac",
                        1,
                        "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
    }

}
