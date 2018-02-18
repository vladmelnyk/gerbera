package sd.fomin.gerbera.util;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class Base58CheckUtilsTest {

    @Test
    public void testDecodeEmpty() {
        byte[] decoded = Base58CheckUtils.decode("3QJmnh");
        assertThat(decoded).isEmpty();
    }

    @Test
    public void testOneByteZero() {
        byte[] decoded = Base58CheckUtils.decode("1Wh4bh");
        assertThat(decoded).containsExactly(0);
    }

    @Test
    public void testOneByteNonZero() {
        byte[] decoded = Base58CheckUtils.decode("BXvDbH");
        assertThat(decoded).containsExactly(1);
    }

    @Test
    public void testLeadingZero() {
        byte[] decoded = Base58CheckUtils.decode("15sPzhL1ouhNBTAtdT");
        assertThat(decoded).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testMultipleLeadingZeros() {
        byte[] decoded = Base58CheckUtils.decode("111kdY9bfye13qL4rbC4");
        assertThat(decoded).containsExactly(0, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    }

    @Test
    public void testWrongChecksum() {
        assertThatThrownBy(() -> {
            Base58CheckUtils.decode("111kdY9bfye13qL4rbC3");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.BASE58_WRONG_CS);
    }
}
