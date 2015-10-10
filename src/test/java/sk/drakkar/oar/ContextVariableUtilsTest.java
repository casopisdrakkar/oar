package sk.drakkar.oar;

import org.junit.Assert;
import org.junit.Test;
import sk.drakkar.oar.authors.AuthorArticlesCollector;

import java.util.Map;

public class ContextVariableUtilsTest {
    @Test
    public void testGetContxtVariableExpression() throws Exception {
        String contextVariableExpression = ContextVariableUtils.getContextVariableExpression(AuthorArticlesCollector.ContextVariables.authorArticles);
        Assert.assertEquals("authorArticlesCollector.authorArticles", contextVariableExpression);
    }

    @Test
    public void testConstructModel() throws Exception {
        Map<String, Object> model = ContextVariableUtils.constructModel(AuthorArticlesCollector.ContextVariables.authorArticles, "Test");
        Assert.assertEquals("{authorArticlesCollector={authorArticles=Test}}", model.toString());
    }

}