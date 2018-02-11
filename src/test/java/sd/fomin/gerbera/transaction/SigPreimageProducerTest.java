package sd.fomin.gerbera.transaction;

import org.junit.Assert;
import org.junit.Test;
import sd.fomin.gerbera.util.HexUtils;

import java.util.Arrays;
import java.util.List;

public class SigPreimageProducerTest {

    @Test
    public void testRegularPreimage() {
        List<Input> inputs = Arrays.asList(
                new Input(false,
                        "5a0c09d298e45dceafcf3865dfd2d6fe51ee04c7a43fb135faa53d9e5038fe1d",
                        0,
                        "76a9141f594f74c37771ef1d3c73317411d84e52ed743188ac",
                        5,
                        "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7"),
                new Input(false,
                        "cba852f9bd2e955dfa74b87b6329de443f75f856a22bc5fa8a112267b26ecb82",
                        0,
                        "a914587852b3fe1a872ebc3eea917df93d08caad19af87",
                        10,
                        "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7")
        );
        List<Output> outputs = Arrays.asList(
                new Output(false,5, "mmcgtfjqG1sMvZjzEupfufSqehgzngcqFo", OutputType.CUSTOM),
                new Output(false,10, "2MxofCZSNFE9Xo5kmtGYpMH4d4JZsThzfhN", OutputType.CUSTOM)
        );
        byte[] preimage = SigPreimageProducer.getInstance(false).producePreimage(inputs, outputs, 0);
        String expected =
                "02" +
                "1dfe38509e3da5fa35b13fa4c704ee51fed6d2df6538cfafce5de498d2090c5a" +
                "00000000" +
                "19" +
                "76a9141f594f74c37771ef1d3c73317411d84e52ed743188ac" +
                "ffffffff" +
                "82cb6eb26722118afac52ba256f8753f44de29637bb874fa5d952ebdf952a8cb" +
                "00000000" +
                "00" +
                "ffffffff" +
                "02" +
                "0500000000000000" +
                "19" +
                "76a91442e6647d19d3c404662bf6f9d578bb5ae42b008888ac" +
                "0a00000000000000" +
                "17" +
                "a9143cfafc3e72d2e7f4295cafed29117b3db00a302987";
        Assert.assertArrayEquals(HexUtils.asBytes(expected), preimage);
    }

    @Test
    public void testSegwitPreimage() {
        List<Input> inputs = Arrays.asList(
                new Input(false,
                        "5a0c09d298e45dceafcf3865dfd2d6fe51ee04c7a43fb135faa53d9e5038fe1d",
                        0,
                        "76a9141f594f74c37771ef1d3c73317411d84e52ed743188ac",
                        5,
                        "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7"),
                new Input(false,
                        "cba852f9bd2e955dfa74b87b6329de443f75f856a22bc5fa8a112267b26ecb82",
                        0,
                        "a914587852b3fe1a872ebc3eea917df93d08caad19af87",
                        10,
                        "cViLa9BePFvF3wp3rGEc8v4z3zNEepyChKiUKCLEbPd7NqqtDoA7")
        );
        List<Output> outputs = Arrays.asList(
                new Output(false,5, "mmcgtfjqG1sMvZjzEupfufSqehgzngcqFo", OutputType.CUSTOM),
                new Output(false,10, "2MxofCZSNFE9Xo5kmtGYpMH4d4JZsThzfhN", OutputType.CUSTOM)
        );
        byte[] preimage = SigPreimageProducer.getInstance(true).producePreimage(inputs, outputs, 1);
        String expected =
                "3c061f83a2b8759ebd7763117ed08533dfbfeba00e128055acc4fc8af2457381" +
                "752adad0a7b9ceca853768aebb6965eca126a62965f698a0c1bc43d83db632ad" +
                "82cb6eb26722118afac52ba256f8753f44de29637bb874fa5d952ebdf952a8cb" +
                "00000000" +
                "1976a9141f594f74c37771ef1d3c73317411d84e52ed743188ac" +
                "0a00000000000000" +
                "ffffffff" +
                "dd181cd7d6adaa934923e3f7e5a3c92ade79d06957c9323e59570872eb414c89";
        Assert.assertArrayEquals(HexUtils.asBytes(expected), preimage);
    }

}
