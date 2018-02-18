package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class TransactionInputValidationTest {

    @Test
    public void testNullTransaction() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            null,
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_TRANSACTION_EMPTY);

    }

    @Test
    public void testEmptyTransaction() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "   ",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_TRANSACTION_EMPTY);
    }

    @Test
    public void testWrongLengthTransaction() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "00000000000000000000000000000000000000000000000",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_TRANSACTION_NOT_64_HEX);
    }

    @Test
    public void testNotHexTransaction() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "000000000000000000000000000000Q000000000000000000000000000000000",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_TRANSACTION_NOT_64_HEX);
    }

    @Test
    public void testInvalidIndex() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            -1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_INDEX_NEGATIVE);
    }

    @Test
    public void testNullScript() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            null,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_LOCK_EMPTY);
    }

    @Test
    public void testEmptyScript() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "   ",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_LOCK_EMPTY);
    }

    @Test
    public void testNotHexScript() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "76a9140000000000000000Q0000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_LOCK_NOT_HEX);
    }

    @Test
    public void testZeroSatochi() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            0,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_AMOUNT_NOT_POSITIVE);
    }

    @Test
    public void testNegativeSatochi() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            -1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_AMOUNT_NOT_POSITIVE);
    }

    @Test
    public void testIncorrectPKHScriptFormatOpDup() {
        String script = "75a914000000000000000000000000000000000000000088ac";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectPKHScriptFormatOpHash() {
        String script = "76a814000000000000000000000000000000000000000088ac";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectPKHScriptFormatOpEqual() {
        String script = "76a914000000000000000000000000000000000000000087ac";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectPKHScriptFormatOpChecksig() {
        String script = "76a914000000000000000000000000000000000000000088ad";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectPKHScriptSize() {
        String script = "76a913000000000000000000000000000000000000000088ac";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_WRONG_PKH_SIZE, script));
    }

    @Test
    public void testIncorrectP2SHScriptFormatOpDup() {
        String script = "a814000000000000000000000000000000000000000087";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectP2SHScriptFormatOpHash160() {
        String script = "a914000000000000000000000000000000000000000088";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_LOCK_WRONG_FORMAT, script));
    }

    @Test
    public void testIncorrectP2SHScriptSize() {
        String script = "a913000000000000000000000000000000000000000087";
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            script,
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.INPUT_WRONG_RS_SIZE, script));
    }

    @Test
    public void testNullWif() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "a914000000000000000000000000000000000000000087",
                            1,
                            null);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_WIF_EMPTY);
    }

    @Test
    public void testEmptyWif() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "a914000000000000000000000000000000000000000087",
                            1,
                            "   ");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_WIF_EMPTY);
    }

    @Test
    public void testNotBase58Wif() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create()
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "a914000000000000000000000000000000000000000087",
                            1,
                            "OcV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.INPUT_WIF_NOT_BASE_58);
    }

    @Test
    public void testWrongChecksumWif() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create(false)
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "a914000000000000000000000000000000000000000087",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwB");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.BASE58_WRONG_CS);
    }

    @Test
    public void testCorrectWithPKHScript() {
        assertThatCode(() -> {
            TransactionBuilder.create(false)
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "a914000000000000000000000000000000000000000087",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).doesNotThrowAnyException();
    }

    @Test
    public void testCorrectWithP2SHScript() {
        assertThatCode(() -> {
            TransactionBuilder.create(false)
                    .from(
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            1,
                            "76a914000000000000000000000000000000000000000088ac",
                            1,
                            "cV1Qu5Jf9KVaK6AhhJbXptmzjZuCPuWr5o19o9A6WrxhNKhdfCwA");
        }).doesNotThrowAnyException();
    }

}
