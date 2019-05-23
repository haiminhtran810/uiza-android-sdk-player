package vn.uiza.utils.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static vn.uiza.utils.util.ConvertUtils.hexString2Bytes;
import static vn.uiza.utils.util.EncryptUtilsTest.TestConfig.PATH_FILE;

@RunWith(RobolectricTestRunner.class)

public class EncryptUtilsTest {
    static class TestConfig {

        static final String FILE_SEP = File.separator;
        static final String TEST_PATH;

        static {
            String projectPath = System.getProperty("user.dir");
            TEST_PATH = projectPath + FILE_SEP + "src" + FILE_SEP + "test" + FILE_SEP + "res" + FILE_SEP;
        }

        static final String PATH_FILE = TEST_PATH + "file" + FILE_SEP;
    }

    @Test
    public void encryptMD2() {
        String expected = "67C7A694B8F99396BB1946EB7950E2C4";
        assertEquals(
                expected,
                EncryptUtils.encryptMD2ToString("ABC")
        );
        assertEquals(
                expected,
                EncryptUtils.encryptMD2ToString("ABC".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expected),
                        EncryptUtils.encryptMD2("ABC".getBytes())
                )
        );
    }

    @Test
    public void encryptMD5() {
        String expected = "902FBDD2B1DF0C4F70B4A5D23525E932";
        assertEquals(
                expected,
                EncryptUtils.encryptMD5ToString("ABC")
        );
        assertEquals(
                expected,
                EncryptUtils.encryptMD5ToString("ABC".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expected),
                        EncryptUtils.encryptMD5("ABC".getBytes())
                )
        );
    }

    @Test
    public void encryptMD5File() {
        String fileMd5 = "FDB8A37C0B454BD4EB3A43EFC93F36F5";
        assertEquals(
                fileMd5.toUpperCase(),
                EncryptUtils.encryptMD5File2String(new File(PATH_FILE + "UTF8.txt"))
        );
    }

    @Test
    public void encryptSHA1() {
        String expectedSHA1 = "3C01BDBB26F358BAB27F267924AA2C9A03FCFDB8";
        assertEquals(
                expectedSHA1,
                EncryptUtils.encryptSHA1ToString("ABC")
        );
        assertEquals(
                expectedSHA1,
                EncryptUtils.encryptSHA1ToString("ABC".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedSHA1),
                        EncryptUtils.encryptSHA1("ABC".getBytes())
                )
        );
    }

    @Test
    public void encryptSHA224() {
        String expectedSHA224 = "94B0253B75B7EB4D2F6693113D20684F3CF045DF508A70BEDF5FFEB2";
        assertEquals(
                expectedSHA224,
                EncryptUtils.encryptSHA224ToString("HMT")
        );
        assertEquals(
                expectedSHA224,
                EncryptUtils.encryptSHA224ToString("HMT".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedSHA224),
                        EncryptUtils.encryptSHA224("HMT".getBytes())
                )
        );
    }


    @Test
    public void encryptSHA256() throws Exception {
        String blankjSHA256 = "8BD80AE90DFBA112786367BEBDDEE60A638EF5B82682EDF8F3D3CA8E6BFEF648";
        assertEquals(
                blankjSHA256,
                EncryptUtils.encryptSHA256ToString("blankj")
        );
        assertEquals(
                blankjSHA256,
                EncryptUtils.encryptSHA256ToString("blankj".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(blankjSHA256),
                        EncryptUtils.encryptSHA256("blankj".getBytes())
                )
        );
    }

    @Test
    public void encryptSHA384() {
        String blankjSHA384 = "BF831E5221FC108D6A72ACB888BA3EB0C030A5F01BA2F739856BE70681D86F992B85E0D461101C74BAEDA895BD422557";
        assertEquals(
                blankjSHA384,
                EncryptUtils.encryptSHA384ToString("blankj")
        );
        assertEquals(
                blankjSHA384,
                EncryptUtils.encryptSHA384ToString("blankj".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(blankjSHA384),
                        EncryptUtils.encryptSHA384("blankj".getBytes())
                )
        );
    }

    @Test
    public void encryptSHA512() {
        String blankjSHA512 = "D59D31067F614ED3586F85A31FEFDB7F33096316DA26EBE0FF440B241C8560D96650F100D78C512560C976949EFA89CB5D5589DCF68C7FAADE98F03BCFEC2B45";
        assertEquals(
                blankjSHA512,
                EncryptUtils.encryptSHA512ToString("blankj")
        );
        assertEquals(
                blankjSHA512,
                EncryptUtils.encryptSHA512ToString("blankj".getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(blankjSHA512),
                        EncryptUtils.encryptSHA512("blankj".getBytes())
                )
        );
    }


    private String blankjHmacSHA512 =
            "9BA1F63365A6CAF66E46348F43CDEF956015BEA997ADEB06E69007EE3FF517DF10FC5EB860DA3D43B82C2A040C931119D2DFC6D08E253742293A868CC2D82015";
    private String blankjHmackey = "test";

    @Test
    public void encryptHmacMD5() {
        String expectedHmacMD5 = "3C68EEFE461A0C74F41F5A2D4FBD9D30";
        assertEquals(
                expectedHmacMD5,
                EncryptUtils.encryptHmacMD5ToString("ABC", blankjHmackey)
        );
        assertEquals(
                expectedHmacMD5,
                EncryptUtils.encryptHmacMD5ToString("ABC".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedHmacMD5),
                        EncryptUtils.encryptHmacMD5("ABC".getBytes(), blankjHmackey.getBytes())
                )
        );
    }

    @Test
    public void encryptHmacSHA1() {
        String expectedHmacSHA1 = "289BA80D42C7EB94703AED61625555F8211F4905";
        assertEquals(
                expectedHmacSHA1,
                EncryptUtils.encryptHmacSHA1ToString("VTPD", blankjHmackey)
        );
        assertEquals(
                expectedHmacSHA1,
                EncryptUtils.encryptHmacSHA1ToString("VTPD".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedHmacSHA1),
                        EncryptUtils.encryptHmacSHA1("VTPD".getBytes(), blankjHmackey.getBytes())
                )
        );
    }

    @Test
    public void encryptHmacSHA224() {
        String expectedHmacSHA224 = "EDB6197EBC4F6B96DB243748FA3BEF2A3E7592ABA4BB181D5DB0A39A";
        assertEquals(
                expectedHmacSHA224,
                EncryptUtils.encryptHmacSHA224ToString("HMTVTPD", blankjHmackey)
        );
        assertEquals(
                expectedHmacSHA224,
                EncryptUtils.encryptHmacSHA224ToString("HMTVTPD".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedHmacSHA224),
                        EncryptUtils.encryptHmacSHA224("HMTVTPD".getBytes(), blankjHmackey.getBytes())
                )
        );
    }


    @Test
    public void encryptHmacSHA256() {
        String expectedHmacSHA256 = "88CD2108B5347D973CF39CDF9053D7DD42704876D8C9A9BD8E2D168259D3DDF7";
        assertEquals(
                expectedHmacSHA256,
                EncryptUtils.encryptHmacSHA256ToString("test", blankjHmackey)
        );
        assertEquals(
                expectedHmacSHA256,
                EncryptUtils.encryptHmacSHA256ToString("test".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedHmacSHA256),
                        EncryptUtils.encryptHmacSHA256("test".getBytes(), blankjHmackey.getBytes())
                )
        );
    }

    @Test
    public void encryptHmacSHA384() {
        String expectedHmacSHA384 = "E87C86331F55FADEEB670D69319ACCE0E943E051702D43FF9D3B05A95152388BE4D2601631109567A502AB8DA066F869";
        assertEquals(
                expectedHmacSHA384,
                EncryptUtils.encryptHmacSHA384ToString("test", blankjHmackey)
        );
        assertEquals(
                expectedHmacSHA384,
                EncryptUtils.encryptHmacSHA384ToString("test".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(expectedHmacSHA384),
                        EncryptUtils.encryptHmacSHA384("test".getBytes(), blankjHmackey.getBytes())
                )
        );
    }

    @Test
    public void encryptHmacSHA512() {
        assertEquals(
                blankjHmacSHA512,
                EncryptUtils.encryptHmacSHA512ToString("test", blankjHmackey)
        );
        assertEquals(
                blankjHmacSHA512,
                EncryptUtils.encryptHmacSHA512ToString("test".getBytes(), blankjHmackey.getBytes())
        );
        assertTrue(
                Arrays.equals(
                        hexString2Bytes(blankjHmacSHA512),
                        EncryptUtils.encryptHmacSHA512("test".getBytes(), blankjHmackey.getBytes())
                )
        );
    }

    private String dataDES = "0008DB3345AB0223";
    private String keyDES = "6801020304050607";
    private String resDES = "1F7962581118F360";
    private byte[] bytesDataDES = hexString2Bytes(dataDES);
    private byte[] bytesKeyDES = hexString2Bytes(keyDES);
    private byte[] bytesResDES = hexString2Bytes(resDES);

    @Test
    public void encryptDES() {
        assertTrue(
                Arrays.equals(
                        bytesResDES,
                        EncryptUtils.encryptDES(
                                bytesDataDES,
                                bytesKeyDES
                        )
                )
        );
        assertEquals(
                resDES,
                EncryptUtils.encryptDES2HexString(
                        bytesDataDES,
                        bytesKeyDES
                )
        );
    }

    @Test
    public void decryptDES() {
        assertTrue(
                Arrays.equals(
                        bytesDataDES,
                        EncryptUtils.decryptDES(
                                bytesResDES,
                                bytesKeyDES
                        )
                )
        );
        assertTrue(
                Arrays.equals(
                        bytesDataDES,
                        EncryptUtils.decryptHexStringDES(
                                resDES,
                                bytesKeyDES
                        )
                )
        );
    }

    private String data3DES = "1111111111111111";
    private String key3DES = "111111111111111111111111111111111111111111111111";
    private String res3DES = "F40379AB9E0EC533";
    private byte[] bytesDataDES3 = hexString2Bytes(data3DES);
    private byte[] bytesKeyDES3 = hexString2Bytes(key3DES);
    private byte[] bytesResDES3 = hexString2Bytes(res3DES);

    @Test
    public void encrypt3DES() {
        assertTrue(
                Arrays.equals(
                        bytesResDES3,
                        EncryptUtils.encrypt3DES(
                                bytesDataDES3,
                                bytesKeyDES3
                        )
                )
        );
        assertEquals(
                res3DES,
                EncryptUtils.encrypt3DES2HexString(
                        bytesDataDES3,
                        bytesKeyDES3
                )
        );
    }

    @Test
    public void decrypt3DES() {
        assertTrue(
                Arrays.equals(
                        bytesDataDES3,
                        EncryptUtils.decrypt3DES(
                                bytesResDES3,
                                bytesKeyDES3
                        )
                )
        );
        assertTrue(
                Arrays.equals(
                        bytesDataDES3,
                        EncryptUtils.decryptHexString3DES(
                                res3DES,
                                bytesKeyDES3
                        )
                )
        );
    }
}
