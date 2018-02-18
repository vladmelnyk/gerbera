package sd.fomin.gerbera.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class HexUtilsTest {

    @Test
    public void testStringEmpty() {
        String string = HexUtils.asString();
        assertThat(string).isEmpty();
    }

    @Test
    public void testString() {
        String string = HexUtils.asString(
                (byte) 0x00, (byte) 0x11, (byte) 0x22, (byte) 0x33,
                (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77,
                (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB,
                (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF);
        assertThat(string).isEqualTo("00112233445566778899aabbccddeeff");
    }

    @Test
    public void testStringLeadingZeros() {
        String string = HexUtils.asString((byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x11);
        assertThat(string).isEqualTo("00000011");
    }

    @Test
    public void testBytesEmpty() {
        byte[] bytes = HexUtils.asBytes("");
        assertThat(bytes).isEmpty();
    }

    @Test
    public void testBytesOddStringLength() {
        byte[] bytes = HexUtils.asBytes("10203");
        assertThat(bytes).containsExactly(0x01, 0x02, 0x03);
    }

    @Test
    public void testBytes() {
        byte[] bytes = HexUtils.asBytes("00112233445566778899aabbccddeeff");
        assertThat(bytes).containsExactly(
                0x00, 0x11, 0x22, 0x33,
                0x44, 0x55, 0x66, 0x77,
                0x88, 0x99, 0xAA, 0xBB,
                0xCC, 0xDD, 0xEE, 0xFF
        );
    }

    @Test
    public void testBytesAlignedExceed() {
        byte[] bytes = HexUtils.asBytesAligned("112233", 2);
        assertThat(bytes).containsExactly(0x11, 0x22, 0x33);
    }

    @Test
    public void testBytesAlignedAlign() {
        byte[] bytes = HexUtils.asBytesAligned("112233", 5);
        assertThat(bytes).containsExactly(0x00, 0x00, 0x11, 0x22, 0x33);
    }

    @Test
    public void testBytesAlignedAlignOddStringLength() {
        byte[] bytes = HexUtils.asBytesAligned("10203", 4);
        assertThat(bytes).containsExactly(0x00, 0x01, 0x02, 0x03);
    }
}
