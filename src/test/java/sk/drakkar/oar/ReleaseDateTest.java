package sk.drakkar.oar;

import junit.framework.Assert;
import org.junit.Test;


public class ReleaseDateTest {
    @Test
    public void testParseFromRawCzechMonthName() throws Exception {
        ReleaseDate rijen2014 = ReleaseDate.parseFromRawCzechMonthName(2014, "rijen");
        Assert.assertEquals(2014, rijen2014.getYear());
        Assert.assertEquals("říjen", rijen2014.getDescriptiveMonth());
        Assert.assertEquals(10, rijen2014.getMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromRawCzechMonthNameWithIllegalMonth() throws Exception {
        ReleaseDate fucen2014 = ReleaseDate.parseFromRawCzechMonthName(2014, "fucen");
    }

    @Test
    public void testToString() throws Exception {
        ReleaseDate rijen2014 = ReleaseDate.parseFromRawCzechMonthName(2014, "rijen");
        Assert.assertEquals("říjen 2014", rijen2014.toString());
    }
}