package sd.fomin.gerbera.transaction;

import org.junit.Test;

public class TransactionOutputValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullDestination() {
        TransactionBuilder.create().to(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDestination() {
        TransactionBuilder.create().to("   ", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBase58Destination() {
        TransactionBuilder.create().to("1NZUP3JAc9JkmbvlmoTv7nVgZGtyJjirKV1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectDestinationPrefixMainNet() {
        TransactionBuilder.create().to("2NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectDestinationPrefixTestNet() {
        TransactionBuilder.create(false).to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroSatochi() {
        TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSatochi() {
        TransactionBuilder.create().to("1NZUP3JAc9JkmbvmoTv7nVgZGtyJjirKV1", -1);
    }
}
