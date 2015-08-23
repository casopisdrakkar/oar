package sk.drakkar.oar;

import junit.framework.TestCase;
import org.junit.Assert;

public class SluggerTest extends TestCase {

    public void testToSlug() throws Exception {
        Slugger slugger = new Slugger();
        Assert.assertEquals("jan-jerson-prucha", slugger.toSlug("Jan „Jerson“ Průcha"));
        Assert.assertEquals("ecthelion", slugger.toSlug("Ecthelion²"));
        Assert.assertEquals("alef0", slugger.toSlug("„Alef0“"));
        Assert.assertEquals("potulni-druid", slugger.toSlug("Potulní Druid"));
    }
}