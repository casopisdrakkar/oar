package sk.drakkar.oar.authors;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.ArticleByIssueComparator;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.util.Collection;

public class AuthorArticlesCollector extends DefaultPlugin {
    public static class ContextVariables {
        public static class AuthorArticlesVariable implements GlobalContextVariables.Variable<Multimap<Author, Article>> {}

        public static AuthorArticlesVariable authorArticles = new AuthorArticlesVariable();
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorArticlesCollector.class);

    private final Configuration configuration;

    private Multimap<Author, Article> authorArticles;

    public AuthorArticlesCollector(Configuration configuration) {
        this.configuration = configuration;

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
