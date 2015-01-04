package sk.drakkar.oar;

import java.io.File;

import org.kohsuke.args4j.Argument;

public class CommandLineConfiguration {
	@Argument(required = true, usage = "A project folder with input files")
	private File projectFolder;
	
	public File getProjectFolder() {
		return projectFolder;
	}
	
	public void setProjectFolder(File projectFolder) {
		this.projectFolder = projectFolder;
	}
}
