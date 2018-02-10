package sd.fomin.gerbera.transaction;

import org.junit.Assert;
import org.junit.Test;
import sd.fomin.gerbera.util.HexUtils;

public class ScriptPubKeyProducerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedProducerMainNet() {
        ScriptPubKeyProducer.getInstance(true, (byte) 0x02);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedProducerTestNet() {
        ScriptPubKeyProducer.getInstance(false, (byte) 0xA2);
    }

    @Test
    public void testP2PKHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, (byte) 0x00)
                .produceScript(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        Assert.assertArrayEquals(HexUtils.asBytes(expectedScript), script);
    }

    @Test
    public void testP2PKHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, (byte) 0x6F)
                .produceScript(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "76" + "A9" + "05" + "0001020304" + "88" + "AC";
        Assert.assertArrayEquals(HexUtils.asBytes(expectedScript), script);
    }

    @Test
    public void testP2SHMainNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(true, (byte) 0x05)
                .produceScript(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        Assert.assertArrayEquals(HexUtils.asBytes(expectedScript), script);
    }

    @Test
    public void testP2SHTestNet() {
        byte[] script = ScriptPubKeyProducer.getInstance(false, (byte) 0xC4)
                .produceScript(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04});
        String expectedScript = "A9" + "05" + "0001020304" + "87";
        Assert.assertArrayEquals(HexUtils.asBytes(expectedScript), script);
    }
}
