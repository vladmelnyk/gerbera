package sd.fomin.gerbera.transaction;

import org.junit.Test;

public class TransactionFeeValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFee() {
        TransactionBuilder.create().withFee(-1);
    }
}
