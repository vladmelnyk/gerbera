package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;

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
    public void testBech32TestnetDestination() {
        assertNotNull(TransactionBuilder.create(false).to("tb1q3wrc5yq9c300jxlfeg7ae76tk9gsx044ucyg7a", 1));
    }

    @Test
    public void testBech32MainnetDestination() {
        assertNotNull(TransactionBuilder.create().to("bc1qd6h6vp99qwstk3z668md42q0zc44vpwkk824zh", 1));
    }

    @Test
    public void testIncorrectDestinationPrefixMainNet() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().to("2NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, "[1, L, M]", "[3]", "[b, c, l]"));
    }

    @Test
    public void testIncorrectDestinationPrefixTestNet() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create(false).to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(ErrorMessages.OUTPUT_ADDRESS_WRONG_PREFIX, "[m, n]", "[2]", "[t, b]"));
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
