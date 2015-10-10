package sk.drakkar.oar.search;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.ResourceException;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class TipueSearchPlugin extends DefaultPlugin {
    public static final Logger logger = LoggerFactory.getLogger(TipueSearchPlugin.class);

    public static final String TIPUE_OUTPUT_FOLDER = "tipuesearch";

    public static final String TIPUE_RESOURCE_PACKAGE = "sk.drakkar.oar.static.tipuesearch";

    public static final String TIPUESEARCH_CONTENT_JS = "tipuesearch_content.js";

    public static final int JSON_INDENT_FACTOR = 4;

    private Configuration configuration;

    private Reflections reflections;

    private SearchTemplater searchTemplater =  new SearchTemplater();

    private List<Document> documents = new LinkedList<>();

    public TipueSearchPlugin(Configuration configuration) {
        this.configuration = configuration;

        configureReflections();
    }

    @Override
    public void articleProcessed(Article article, Context context) {
        Document document = toDocument(article);
        if(document != null) {
            documents.add(document);
        }
    }

    private Document toDocument(Article article) {
        try {
            Document document = new Document();
            document.setTitle(article.getMetadata().getTitle());
            document.setTags(Joiner.on(" ").join(article.getMetadata().getTags()));
            document.setText(getPlainText(article.getHtmlSource()));
            document.setUrl(new URL("http://drakkar.sk/" + article.getIssue().getNumber() + "/" + article.getSlug() + ".html"));
            return document;
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for article " + article);
            return null;
        }
    }

    /**
     * Returns a plain unformatted deHTMLized text from HTML source
     */
    private String getPlainText(String htmlSource) {
        return Jsoup.clean(htmlSource, Whitelist.none());
    }

    @Override
    public void publicationComplete(Context context) {
        copySearchPage();
        copyTipueSearchJavaScriptAndCss();
        saveDocumentsToJson();
    }

    private void copySearchPage() {
        try {
            String html = searchTemplater.getSearchPage();
            File outputFile = new File(this.configuration.getOutputFolder(), "search.html");
            com.google.common.io.Files.write(html, outputFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new SearchException("Unable to write author list", e);
        }
    }

    private void saveDocumentsToJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages", documents);

        File outputFolder = getTipueOutputFolder();
        File tipueContentJs = new File(outputFolder, TIPUESEARCH_CONTENT_JS);

        try(PrintWriter writer = new PrintWriter(tipueContentJs)) {
            String javaScript =
                    "var tipuesearch = " + jsonObject.toString(JSON_INDENT_FACTOR);
            writer.append(javaScript);
        } catch (FileNotFoundException e) {
            throw new SearchIndexBuildException("Unable to create JavaScript for search index: " + tipueContentJs);
        }
    }

    private void configureReflections() {
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(TIPUE_RESOURCE_PACKAGE))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(TIPUE_RESOURCE_PACKAGE)))
                .setScanners(new ResourcesScanner()));
    }

    private void copyTipueSearchJavaScriptAndCss() {
        File outputFolder = getTipueOutputFolder();

        Set<String> resources = getTipueJavaScriptAndCssResources();
        for (String resource : resources) {
            try {
                InputStream in = TipueSearchPlugin.class.getResourceAsStream(addRootPrefix(resource));
                Path outputPath = outputFolder.toPath().resolve(getFileName(resource));
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResourceException("Unable to copy resource to target folder", e);
            }
        }
    }

    private File getTipueOutputFolder() {
        File outputFolder = new File(this.configuration.getOutputFolder(), TIPUE_OUTPUT_FOLDER);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        return outputFolder;
    }

    private String addRootPrefix(String resource) {
        if(resource.startsWith("/")) {
            return resource;
        }
        return "/" + resource;
    }

    /**
     * Return a filename from the slash delimited resource.
     * <p>
     *     <b>Example:</b> <code>sk/drakkar/oar/static/tipuesearch/tipuesearch_set.js</code>
     *     becomes <code>tipuesearch_set.js</code>.
     * </p>
     * @param resource
     * @return
     */
    private String getFileName(String resource) {
        return resource.substring(resource.lastIndexOf("/") + 1);
    }

    private Set<String> getTipueJavaScriptAndCssResources() {
        return reflections.getResources(Pattern.compile(".*"));
    }

}
