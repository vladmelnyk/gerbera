package sd.fomin.gerbera.types;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ULongTest {

    @Test
    public void testMinValue() {
        ULong value = ULong.of(0);
        assertThat(value.toString()).isEqualTo("0000000000000000");
        assertThat(value.asLitEndBytes()).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    public void testMaxValue() {
        ULong value = ULong.of(0xFFFFFFFFFFFFFFFFL);
        assertThat(value.toString()).isEqualTo("ffffffffffffffff");
        assertThat(value.asLitEndBytes()).containsExactly(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
    }

    @Test
    public void testDiffBytes() {
        ULong value = ULong.of(0x1122334455667788L);
        assertThat(value.toString()).isEqualTo("8877665544332211");
        assertThat(value.asLitEndBytes()).containsExactly(0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11);
    }
}
