package sd.fomin.gerbera.transaction;

import org.junit.Before;
import org.junit.Test;
import sd.fomin.gerbera.crypto.PrivateKey;
import sd.fomin.gerbera.util.ApplicationRandom;
import sd.fomin.gerbera.util.HexUtils;

import static org.assertj.core.api.Assertions.*;

public class ScriptSigProducerTest {

    @Before
    public void resetRandom() {
        ApplicationRandom.reset();
    }

    @Test
    public void testSegwit() {
        byte[] sigHash = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04};
        PrivateKey key = PrivateKey.ofWif(false, "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7");
        byte[] script = ScriptSigProducer.getInstance(true).produceScriptSig(sigHash, key);
        String expected =
                "16" +
                "00" +
                "14" +
                "1f594f74c37771ef1d3c73317411d84e52ed7431";
        assertThat(script).isEqualTo(HexUtils.asBytes(expected));
    }

    @Test
    public void testRegular() {
        byte[] sigHash = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04};
        PrivateKey key = PrivateKey.ofWif(false, "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7");
        byte[] script = ScriptSigProducer.getInstance(false).produceScriptSig(sigHash, key);
        String expected =
                "48" +
                "30" +
                "45" +
                "02" +
                "21" +
                "00c3bd4651cb467dc8901445e611e17f7c73038507430bb07aea386a80c93cfdc9" +
                "02" +
                "20" +
                "2ec903f4c2908ba90ee6f71882636dad61a6052a18ab90514bf531d359beadb9" +
                "01" +
                "21" +
                "03c1029aa08c5e72d09228d9bb90ae48888a6955f79ec052753a81dfd049f39bb7";
        assertThat(script).isEqualTo(HexUtils.asBytes(expected));
    }
}
