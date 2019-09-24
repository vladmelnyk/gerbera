package sd.fomin.gerbera.transaction;

import org.junit.Ignore;
import org.junit.Test;
import sd.fomin.gerbera.constant.ErrorMessages;
import sd.fomin.gerbera.util.HexUtils;

import static org.assertj.core.api.Assertions.*;

public class ScriptPubKeyProducerTest {

    @Test
    @Ignore
    public void testUnsupportedProducerMainNet() {
        assertThatThrownBy(() -> {
            ScriptPubKeyProducer.getInstance(true, (byte) 0x02);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.SPK_UNSUPPORTED_PRODUCER, true, (byte) 0x02));
    }

    @Test
    @Ignore
    public void testUnsupportedProducerTestNet() {
        assertThatThrownBy(() -> {
            ScriptPubKeyProducer.getInstance(false, (byte) 0xA2);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(String.format(ErrorMessages.SPK_UNSUPPORTED_PRODUCER, false, (byte) 0xA2));
    }

    @Test
    public void testP2PKHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, (byte) 0x00)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2PKHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, (byte) 0x6F)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, (byte) 0x05)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, (byte) 0xC4)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }
}
