package sk.drakkar.oar.resources;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.ResourceException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurationSupport;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.regex.Pattern;

public class CopyCssPlugin extends ConfigurationSupport implements PortalAssemblyPlugin {
    public static final String CSS_FOLDER_NAME = "css";

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
                String cssResourceFullName = addRootPrefix(cssFileName);
                InputStream cssStream = CopyCssPlugin.class.getResourceAsStream(cssResourceFullName);
                Path cssOutputPath = cssOutputFolder.toPath().resolve(getBaseName(cssFileName));
                Files.copy(cssStream, cssOutputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResourceException("Unable to copy resource to target folder", e);
            }
        }
    }

    private String getBaseName(String fileName) {
        return new File(fileName).getName();
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

    private String addRootPrefix(String resource) {
        if(resource.startsWith("/")) {
            return resource;
        }
        return "/" + resource;
    }
}
