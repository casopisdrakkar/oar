package sk.drakkar.oar;

import java.io.File;

public class Configuration {
	private static final String OUTPUT_FOLDER = "target/site";

	private static final String CONTENT_FOLDER_NAME = "content";

	private File projectFolder;
	
	private String outputFolder = OUTPUT_FOLDER;
	
	private String contentFolderName = CONTENT_FOLDER_NAME;

	public Configuration(File projectFolder) {
		this.projectFolder = projectFolder;
	}
	
	public File getOutputFolder(Issue issue) {		
		File outputFolder = new File(this.outputFolder, Integer.toString(issue.getNumber()));
		outputFolder.mkdirs();
		return outputFolder;
	}
	
	public File getContentFolder() {
		return new File(projectFolder, this.contentFolderName);
	}
	
	public File getProjectFolder() {
		return projectFolder;
	}
	
	public void setProjectFolder(File projectFolder) {
		this.projectFolder = projectFolder;
	}
	
	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getContentFolderName() {
		return contentFolderName;
	}

	public void setContentFolderName(String contentFolderName) {
		this.contentFolderName = contentFolderName;
	}
	
	
}
