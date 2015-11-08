package sk.drakkar.oar.authors;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.ArticleByIssueComparator;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.ArticleAssemblyPlugin;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.util.Collection;

public class AuthorArticlesCollector extends ArticleAssemblyPlugin implements PortalAssemblyPlugin {
    public static class ContextVariables {
        public static class AuthorArticlesVariable implements GlobalContextVariables.Variable<Multimap<Author, Article>> {}

        public static AuthorArticlesVariable authorArticles = new AuthorArticlesVariable();
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorArticlesCollector.class);

    private Multimap<Author, Article> authorArticles;

    public AuthorArticlesCollector() {
        authorArticles = MultimapBuilder.ListMultimapBuilder
                .treeKeys(AuthorByNameComparator.INSTANCE)
                .arrayListValues()
                .build();
    }

    @Override
    public void articleProcessed(Article article, Context context) {
        for(Author author : article.getMetadata().getAuthors()) {
            authorArticles.put(author, article);
        }
    }

    @Override
    public void publicationComplete(Context context) {
        sortAuthorArticlesByIssueNumber();
        context.put(ContextVariables.authorArticles, this.authorArticles);
        logger.info("Collected author productivity list.");
    }

    private void sortAuthorArticlesByIssueNumber() {
        for(Author author : this.authorArticles.keySet()) {
            Collection<Article> articles = this.authorArticles.get(author);
            ArticleByIssueComparator.sortByIssue(articles);
        }
    }

}
