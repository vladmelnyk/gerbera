package sd.fomin.gerbera.types;

import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;

import static org.assertj.core.api.Assertions.*;

public class OpSizeTest {

    @Test
    public void testMinValueInt() {
        OpSize value = OpSize.ofInt(1);
        assertThat(value.getSize()).isEqualTo((byte) 0x01);
    }

    @Test
    public void testMaxValueInt() {
        OpSize value = OpSize.ofInt(0x4b);
        assertThat(value.getSize()).isEqualTo((byte) 0x4b);
    }

    @Test
    public void testMidValueInt() {
        OpSize value = OpSize.ofInt(20);
        assertThat(value.getSize()).isEqualTo((byte) 20);
    }

    @Test
    public void testLowerValueInt() {
        assertThatThrownBy(() -> {
            OpSize.ofInt(0);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OP_SIZE_NOT_IMPLEMENTED);
    }

    @Test
    public void testHigherValueInt() {
        assertThatThrownBy(() -> {
            OpSize.ofInt(0x4c);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OP_SIZE_NOT_IMPLEMENTED);
    }

    @Test
    public void testMinValueByte() {
        OpSize value = OpSize.ofByte((byte) 1);
        assertThat(value.getSize()).isEqualTo((byte) 0x01);
    }

    @Test
    public void testMaxValueByte() {
        OpSize value = OpSize.ofByte((byte) 0x4b);
        assertThat(value.getSize()).isEqualTo((byte) 0x4b);
    }

    @Test
    public void testMidValueByte() {
        OpSize value = OpSize.ofByte((byte) 20);
        assertThat(value.getSize()).isEqualTo((byte) 20);
    }

    @Test
    public void testLowerValueByte() {
        assertThatThrownBy(() -> {
            OpSize.ofByte((byte) 0);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OP_SIZE_NOT_IMPLEMENTED);
    }

    @Test
    public void testHigherValueByte() {
        assertThatThrownBy(() -> {
            OpSize.ofByte((byte) 0x4c);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorMessages.OP_SIZE_NOT_IMPLEMENTED);
    }
}
