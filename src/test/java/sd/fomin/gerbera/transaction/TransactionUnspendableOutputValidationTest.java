package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.util.HexUtils;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TransactionUnspendableOutputValidationTest {

    @Test
    public void testNullData() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().put(null, 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_DATA_IS_EMPTY);
    }

    @Test
    public void testEmptyData() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().put("   ", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_DATA_IS_EMPTY);
    }

    @Test
    public void testDataNotHex() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().put("qwerty", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_DATA_IS_NOT_HEX);

        System.out.println(HexUtils.asString("And it works".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testDataIsHuge() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().put("1234567890123456789012345678901234567890123456789012345678901234567890123456789012", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.OUTPUT_DATA_BYTES_HUGE, 41, 40));
    }


    @Test
    public void testNegativeSatochi() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().put("1234567890", -1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_AMOUNT_NOT_POSITIVE);
    }

    @Test
    public void testCorrectOutput() {
        assertThatCode(() -> {
            TransactionBuilder.create().put("1234567890", 10);
        }).doesNotThrowAnyException();
    }

    @Test
    public void testCorrectNullOutput() {
        assertThatCode(() -> {
            TransactionBuilder.create().putNullUnspendableOutput("1234567890");
        }).doesNotThrowAnyException();
    }
}