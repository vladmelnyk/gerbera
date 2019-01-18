package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class TransactionRegularOutputValidationTest {

    @Test
    public void testNullDestination() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to(null, 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_ADDRESS_EMPTY);
    }

    @Test
    public void testEmptyDestination() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("   ", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_ADDRESS_EMPTY);
    }

    @Test
    public void testNotBase58Destination() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("1NZUP3JAc9JkmbvlmoTv7nVgZGtyJjirKV1", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_ADDRESS_NOT_BASE_58);
    }

    @Test
    public void testIncorrectDestinationPrefixMainNet() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("2NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, "[1]", "[3]"));
    }

    @Test
    public void testIncorrectDestinationPrefixTestNet() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create(false).to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, "[m, n]", "[2]"));
    }

    @Test
    public void testWrongChecksumDestination() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV2", 1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.BASE58_WRONG_CS);

    }

    @Test
    public void testNegativeSatochi() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", -1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OUTPUT_AMOUNT_NOT_POSITIVE);
    }

    @Test
    public void testCorrectOutput() {
        assertThatCode(() -> {
            TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
        }).doesNotThrowAnyException();
    }
}
