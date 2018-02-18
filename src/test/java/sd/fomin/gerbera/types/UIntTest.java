package sd.fomin.gerbera.types;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class UIntTest {

    @Test
    public void testMinValue() {
        UInt value = UInt.of(0);
        assertThat(value.toString()).isEqualTo("00000000");
        assertThat(value.asLitEndBytes()).containsExactly(0, 0, 0, 0);
    }

    @Test
    public void testMaxValue() {
        UInt value = UInt.of(0xFFFFFFFF);
        assertThat(value.toString()).isEqualTo("ffffffff");
        assertThat(value.asLitEndBytes()).containsExactly(0xFF, 0xFF, 0xFF, 0xFF);
    }

    @Test
    public void testDiffBytes() {
        UInt value = UInt.of(0x11335577);
        assertThat(value.toString()).isEqualTo("77553311");
        assertThat(value.asLitEndBytes()).containsExactly(0x77, 0x55, 0x33, 0x11);
    }

    @Test
    public void testAsByteValid() {
        assertThatThrownBy(() -> {
            UInt value = UInt.of(0x0101);
            value.asByte();
        }).isInstanceOf(IllegalStateException.class).hasMessage(ErrorMessages.UINT_NOT_REPRESENTABLE);
    }

    @Test
    public void testAsByteInvalid() {
        UInt value = UInt.of(0x01);
        assertThat(value.asByte()).isEqualTo((byte) 1);
    }
}
