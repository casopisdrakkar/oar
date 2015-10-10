package sk.drakkar.oar.css;

import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.ResourceException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CopyCssPlugin extends ConfigurablePlugin {
    public static final String CSS_FOLDER_NAME = "css";

    public static final String CSS_FOLDER_SOURCE = "sk/drakkar/oar/static/css";

    public static final String[] CSS_FILES = {
            "article.css",
            "bootstrap.min.css",
            "bootstrap-theme.min.css",
            "drakkar.css",
            "issue.css",
            "issue-index.css",
            "author-list.css",
            "tag-list.css"
    };

    public CopyCssPlugin(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void publicationComplete(Context context) {
        File cssOutputFolder = getConfiguration().getOrCreateOutputSubfolder(CSS_FOLDER_NAME);

        for (String cssFileName : CSS_FILES) {
            try {
                String cssFile = "/" + CSS_FOLDER_SOURCE + "/" + cssFileName;
                InputStream cssStream = CopyCssPlugin.class.getResourceAsStream(cssFile);
                Path cssOutputPath = cssOutputFolder.toPath().resolve(cssFileName);
                Files.copy(cssStream, cssOutputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResourceException("Unable to copy resource to target folder", e);
            }
        }

    }
}
