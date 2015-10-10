package sk.drakkar.oar.css;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
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
import java.util.Set;
import java.util.regex.Pattern;

public class CopyCssPlugin extends ConfigurablePlugin {
    public static final String CSS_FOLDER_NAME = "css";

    public static final String CSS_FOLDER_SOURCE = "sk/drakkar/oar/static/css";

    private static final String CSS_RESOURCE_PACKAGE = "sk.drakkar.oar.static.css";

    private Reflections reflections;

    public CopyCssPlugin(Configuration configuration) {
        super(configuration);

        configureReflections();
    }

    @Override
    public void publicationComplete(Context context) {
        File cssOutputFolder = getConfiguration().getOrCreateOutputSubfolder(CSS_FOLDER_NAME);

        for (String cssFileName : getCssFiles()) {
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

    private void configureReflections() {
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(CSS_RESOURCE_PACKAGE))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(CSS_RESOURCE_PACKAGE)))
                .setScanners(new ResourcesScanner()));
    }

    public Set<String> getCssFiles() {
        return reflections.getResources(Pattern.compile(".*\\.css"));
    }
}
