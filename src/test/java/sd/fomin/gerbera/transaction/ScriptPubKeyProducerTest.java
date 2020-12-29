package sd.fomin.gerbera.transaction;

import org.junit.Test;
import sd.fomin.gerbera.types.Coin;
import sd.fomin.gerbera.util.HexUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptPubKeyProducerTest {

    @Test
    public void testP2PKHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, Coin.BTC, (byte) 0x00, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2PKHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, Coin.BTC, (byte) 0x6F, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, Coin.BTC, (byte) 0x05, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, Coin.BTC, (byte) 0xC4, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2PKHMainNetLtc() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, Coin.LTC, (byte) 0x30, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2PKHTestNetLtc() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, Coin.LTC, (byte) 0x6F, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHMainNetLtc() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, Coin.LTC, (byte) 0x05, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHMainNetLtcNewFormat() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, Coin.LTC, (byte) 0x32, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }

    @Test
    public void testP2SHTestNetLtc() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, Coin.LTC, (byte) 0xC4, false)
                .produceScript(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        assertThat(script).isEqualTo(HexUtils.asBytes(expectedScript));
    }
}
