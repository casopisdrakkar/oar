package sk.drakkar.oar.css;

import ch.qos.logback.core.util.FileUtil;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.PublicationCompleteListener;
import sk.drakkar.oar.ResourceException;
import sun.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;

public class CopyCssPlugin implements PublicationCompleteListener {
    public static final String CSS_FOLDER_NAME = "css";

    public static final String CSS_FOLDER_SOURCE = "sk/drakkar/oar/static/css";

    public static final String[] CSS_FILES = {
            "article.css",
            "bootstrap.min.css",
            "bootstrap-theme.min.css",
            "drakkar.css",
            "issue.css",
            "issue-index.css"
    };

    private Configuration configuration;

    public CopyCssPlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void publicationComplete() {
        File cssOutputFolder = new File(this.configuration.getOutputFolder(), CSS_FOLDER_NAME);
        if (!cssOutputFolder.exists()) {
            cssOutputFolder.mkdirs();
        }

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
