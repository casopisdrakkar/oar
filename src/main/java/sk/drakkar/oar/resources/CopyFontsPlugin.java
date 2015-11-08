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

public class CopyFontsPlugin extends ConfigurationSupport implements PortalAssemblyPlugin {
    public static final String FONTS_FOLDER_NAME = "fonts";

    private static final String FONTS_RESOURCE_PACKAGE = "sk.drakkar.oar.static.fonts";

    private Reflections reflections;

    public CopyFontsPlugin(Configuration configuration) {
        super(configuration);

        configureReflections();
    }

    @Override
    public void publicationComplete(Context context) {
        File fontOutputFolder = getConfiguration().getOrCreateOutputSubfolder(FONTS_FOLDER_NAME);

        for (String fontFileName : getFontFiles()) {
            try {
                String fontResourceFullName = addRootPrefix(fontFileName);
                InputStream fontStream = CopyCssPlugin.class.getResourceAsStream(fontResourceFullName);
                Path fontOutputPath = fontOutputFolder.toPath().resolve(getBaseName(fontFileName));
                Files.copy(fontStream, fontOutputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResourceException("Unable to copy font resource to target folder", e);
            }
        }
    }

    private String getBaseName(String fileName) {
        return new File(fileName).getName();
    }

    private void configureReflections() {
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(FONTS_RESOURCE_PACKAGE))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(FONTS_RESOURCE_PACKAGE)))
                .setScanners(new ResourcesScanner()));
    }

    public Set<String> getFontFiles() {
        return reflections.getResources(Pattern.compile(".*\\.woff2"));
    }

    private String addRootPrefix(String resource) {
        if(resource.startsWith("/")) {
            return resource;
        }
        return "/" + resource;
    }
}
