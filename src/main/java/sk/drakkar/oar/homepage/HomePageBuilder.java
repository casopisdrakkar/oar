package sk.drakkar.oar.homepage;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.IssueByReversedNumberComparator;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HomePageBuilder extends ConfigurablePlugin {

    private static final Logger logger = LoggerFactory.getLogger(HomePageBuilder.class);

    private List<Issue> issues = new LinkedList<Issue>();

    private HomePageTemplater homePageTemplater = new HomePageTemplater();

    public HomePageBuilder(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void publicationComplete(Context context) {
        logger.info("Building home page");
        Collections.sort(issues, IssueByReversedNumberComparator.INSTANCE);

        String html = homePageTemplater.convert(this.issues);
        write(html);
    }

    private void write(String html) {
        try {
            File outputFile = new File(getConfiguration().getOutputFolder(), "index.html");
            Files.write(html, outputFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new HomePageBuildingException("Unable to write home page", e);
        }
    }

    @Override
    public void issueArticlesProcessed(Issue issue) {
        issues.add(issue);
    }
}
