package sd.fomin.gerbera.types;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class VarIntTest {

    @Test
    public void testOneByteVarInt() {
        VarInt varInt0 = VarInt.of(0L);
        assertThat(varInt0.toString()).isEqualTo("00");
        assertThat(varInt0.asLitEndBytes()).containsExactly(0);

        VarInt varInt1 = VarInt.of(1L);
        assertThat(varInt1.toString()).isEqualTo("01");
        assertThat(varInt1.asLitEndBytes()).containsExactly(1);

        VarInt varInt100 = VarInt.of(0x64L);
        assertThat(varInt100.toString()).isEqualTo("64");
        assertThat(varInt100.asLitEndBytes()).containsExactly(0x64);

        VarInt varIntMax = VarInt.of(0xFCL);
        assertThat(varIntMax.toString()).isEqualTo("fc");
        assertThat(varIntMax.asLitEndBytes()).containsExactly(0xFC);
    }

    @Test
    public void testThreeByteVarInt() {
        VarInt varIntMin = VarInt.of(0xFDL);
        assertThat(varIntMin.toString()).isEqualTo("fdfd00");
        assertThat(varIntMin.asLitEndBytes()).containsExactly(0xFD, 0xFD, 0x00);

        VarInt varInt1000 = VarInt.of(0x3E8L);
        assertThat(varInt1000.toString()).isEqualTo("fde803");
        assertThat(varInt1000.asLitEndBytes()).containsExactly(0xFD, 0xE8, 0x03);

        VarInt varIntMax = VarInt.of(0xFFFFL);
        assertThat(varIntMax.toString()).isEqualTo("fdffff");
        assertThat(varIntMax.asLitEndBytes()).containsExactly(0xFD, 0xFF, 0xFF);
    }

    @Test
    public void testFiveByteVarInt() {
        VarInt varIntMin = VarInt.of(0x10000L);
        assertThat(varIntMin.toString()).isEqualTo("fe00000100");
        assertThat(varIntMin.asLitEndBytes()).containsExactly(0xFE, 0x00, 0x00, 0x01, 0x00);

        VarInt varIntAllBytes = VarInt.of(0xAABBCCDDL);
        assertThat(varIntAllBytes.toString()).isEqualTo("feddccbbaa");
        assertThat(varIntAllBytes.asLitEndBytes()).containsExactly(0xFE, 0xDD, 0xCC, 0xBB, 0xAA);

        VarInt varIntMax = VarInt.of(0xFFFFFFFFL);
        assertThat(varIntMax.toString()).isEqualTo("feffffffff");
        assertThat(varIntMax.asLitEndBytes()).containsExactly(0xFE, 0xFF, 0xFF, 0xFF, 0xFF);
    }

    @Test
    public void testNineByteVarInt() {
        VarInt varIntMin = VarInt.of(0x100000000L);
        assertThat(varIntMin.toString()).isEqualTo("ff0000000001000000");
        assertThat(varIntMin.asLitEndBytes()).containsExactly(
                0xFF,
                0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x00, 0x00
        );

        VarInt varIntAllBytes = VarInt.of(0x1122334455667788L);
        assertThat(varIntAllBytes.toString()).isEqualTo("ff8877665544332211");
        assertThat(varIntAllBytes.asLitEndBytes()).containsExactly(
                0xFF,
                0x88, 0x77, 0x66, 0x55,
                0x44, 0x33, 0x22, 0x11
        );

        VarInt varIntMax = VarInt.of(0xFFFFFFFFFFFFFFFFL);
        assertThat(varIntMax.toString()).isEqualTo("ffffffffffffffffff");
        assertThat(varIntMax.asLitEndBytes()).containsExactly(
                0xFF,
                0xFF, 0xFF, 0xFF, 0xFF,
                0xFF, 0xFF, 0xFF, 0xFF
        );
    }
}
