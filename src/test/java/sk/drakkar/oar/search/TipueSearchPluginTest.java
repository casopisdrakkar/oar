package sk.drakkar.oar.search;

import org.junit.Test;
import sk.drakkar.oar.Configuration;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TipueSearchPluginTest {
    @Test
    public void test() throws Exception {
        Configuration configuration = new Configuration(new File("."));

        File targetFolder = new File("./target");

        configuration.setOutputFolder(targetFolder);
        TipueSearchPlugin tipueSearchPlugin = new TipueSearchPlugin(configuration);
        tipueSearchPlugin.publicationComplete();

        assertTrue(Arrays
                .stream(targetFolder.list())
                .anyMatch(file -> TipueSearchPlugin.TIPUE_OUTPUT_FOLDER.equals(file)));
    }
}