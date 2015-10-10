package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public abstract class FileUtils {
	public static void copyAndOverwrite(File file, File targetDirectory) throws IOException {
		Path src = file.toPath();
		Path target = targetDirectory.toPath().resolve(file.getName());
		Files.copy(src, target, REPLACE_EXISTING);
	}
}
