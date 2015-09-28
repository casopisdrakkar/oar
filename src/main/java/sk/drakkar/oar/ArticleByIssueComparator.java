package sk.drakkar.oar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArticleByIssueComparator implements Comparator<Article> {
    public static final Comparator<Article> INSTANCE = new ArticleByIssueComparator();

    @Override
    public int compare(Article article, Article otherArticle) {
        int issue = article.getIssue().getNumber();
        int otherIssue = otherArticle.getIssue().getNumber();
        return Integer.compare(issue, otherIssue);
    }

    public static Collection<Article> sortByIssue(Collection<Article> articleCollection) {
        List<Article> articleList = null;
        if(articleCollection instanceof List) {
            articleList = (List<Article>) articleCollection;
        } else {
            articleList = new ArrayList<Article>(articleCollection);
        }
        Collections.sort(articleList, ArticleByIssueComparator.INSTANCE);
        return articleList;
    }
}
