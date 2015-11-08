package sk.drakkar.oar.authors;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurationSupport;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.io.File;
import java.io.IOException;

public class AuthorListBuilder extends ConfigurationSupport implements PortalAssemblyPlugin {
    private static final Logger logger = LoggerFactory.getLogger(AuthorListBuilder.class);

    private AuthorListTemplater authorListTemplater = new AuthorListTemplater();

    public AuthorListBuilder(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void publicationComplete(Context context) {
        Multimap<Author, Article> authorMap = context.get(AuthorArticlesCollector.ContextVariables.authorArticles);

        String html = authorListTemplater.convert(authorMap, context);
        write(html);

        logger.info("Written author list.");
    }

    private void write(String html) {
        try {
            File outputFile = new File(getConfiguration().getOutputFolder(), "authors.html");
            Files.write(html, outputFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new AuthorListBuildingException("Unable to write author list", e);
        }
    }

}
