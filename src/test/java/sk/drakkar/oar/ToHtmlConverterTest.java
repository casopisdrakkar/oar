package sk.drakkar.oar;

import junit.framework.Assert;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class ToHtmlConverterTest {

    /**
     *  Tests the bug in https://github.com/sirthias/pegdown/issues/114.
     *  This bug is present as of PegDown 1.5.0
     *  Images without description fail to render.
     */
    @Test
    public void testPegdownIssue114() throws Exception {
        PegDownProcessor pegDownProcessor = new PegDownProcessor();
        String html = pegDownProcessor.markdownToHtml("![](dracak.jpg)");
        Assert.assertEquals("<p>![](dracak.jpg)</p>", html);
    }

    /**
     * Pegdown does not support Markdown in HTML blocks.
     */
    @Test
    public void testMarkdownInHtml() throws Exception {
        PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL);
        String html = pegDownProcessor.markdownToHtml("<div markdown='1'>**Test**</div>");
        Assert.assertEquals("<div markdown='1'>**Test**</div>", html);
    }

}
