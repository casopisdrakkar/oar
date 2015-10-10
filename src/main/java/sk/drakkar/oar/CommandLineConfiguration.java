package sk.drakkar.oar;

import org.kohsuke.args4j.Argument;

import java.io.File;

public class CommandLineConfiguration {
	@Argument(required = true, index = 0, usage = "A project folder with input files")
	private File projectFolder;


	@Argument(index = 1, usage = "A target folder for generated HTML files")
	private File targetFolder;


	public File getProjectFolder() {
		return projectFolder;
	}
	
	public void setProjectFolder(File projectFolder) {
		this.projectFolder = projectFolder;
	}

	public File getTargetFolder() {
		return targetFolder;
	}

	public void setTargetFolder(File targetFolder) {
		this.targetFolder = targetFolder;
	}
}
