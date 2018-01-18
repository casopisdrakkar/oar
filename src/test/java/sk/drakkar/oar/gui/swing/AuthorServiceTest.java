package sk.drakkar.oar.gui.swing;

import org.junit.Assert;
import org.junit.Test;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.authors.Author;

import java.io.File;
import java.util.List;

public class AuthorServiceTest {
    @Test
    public void test() {
        Configuration configuration = new Configuration(new File("src/test/resources"));
        AuthorService authorService = new AuthorService(configuration);
        authorService.initialize();
        List<Author> authors = authorService.listAuthors();

        Assert.assertEquals(1, authors.size());
        Assert.assertEquals("Michal „Architrav“ Horváth", authors.get(0));

    }
}