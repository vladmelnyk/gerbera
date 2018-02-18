package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class TransactionFeeValidationTest {

    @Test
    public void testNegativeFee() {
        assertThatThrownBy(() -> {
            TransactionBuilder.create().withFee(-1);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.FEE_NEGATIVE);

    }
}
