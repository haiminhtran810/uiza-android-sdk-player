package vn.uiza.utils.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodeUtilsTest {

    @Test
    public void urlEncode_urlDecode() {
        String urlEncodeString = "%E5%93%88%E5%93%88%E5%93%88";
        assertEquals(urlEncodeString, EncodeUtils.urlEncode("哈哈哈"));
        assertEquals(urlEncodeString, EncodeUtils.urlEncode("哈哈哈", "UTF-8"));

        assertEquals("哈哈哈", EncodeUtils.urlDecode(urlEncodeString));
        assertEquals("哈哈哈", EncodeUtils.urlDecode(urlEncodeString, "UTF-8"));
    }

    @Test
    public void htmlEncode_htmlDecode() {
        String html = "<html>" +
                "<head>" +
                "<title>Title</title>" +
                "</head>" +
                "<body>" +
                "<p>body Body</p>" +
                "<p>title Body</p>" +
                "</body>" +
                "</html>";
        String encodeHtml = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Title&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;p&gt;body Body&lt;/p&gt;&lt;p&gt;title Body&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";

        assertEquals(encodeHtml, EncodeUtils.htmlEncode(html));
    }
}
