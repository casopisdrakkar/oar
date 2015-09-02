package sk.drakkar.oar;

import java.util.Comparator;

public class ArticleByIssueComparator implements Comparator<Article> {
    public static final Comparator<Article> INSTANCE = new ArticleByIssueComparator();

    @Override
    public int compare(Article article, Article otherArticle) {
        int issue = article.getIssue().getNumber();
        int otherIssue = otherArticle.getIssue().getNumber();
        return Integer.compare(issue, otherIssue);
    }
}
