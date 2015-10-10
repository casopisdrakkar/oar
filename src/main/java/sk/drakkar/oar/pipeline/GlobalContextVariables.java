package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;

import java.io.File;

public class GlobalContextVariables {
    public interface Variable<T> {}

    public static class IssueVariable implements Variable<Issue> {}
    public static class IssueAssetVariable implements Variable<File> {}
    public static class ArticleVariable implements Variable<Article> {}

    public static IssueVariable issue = new IssueVariable();

    public static IssueAssetVariable issueAsset = new IssueAssetVariable();

    public static ArticleVariable article = new ArticleVariable();
}
