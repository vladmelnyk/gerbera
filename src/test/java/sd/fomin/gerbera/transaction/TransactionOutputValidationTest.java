package sd.fomin.gerbera.transaction;

import org.junit.Test;

public class TransactionOutputValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSatochi() {
        TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", -1);
    }
}
