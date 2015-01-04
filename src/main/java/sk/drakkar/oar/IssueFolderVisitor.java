package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueFolderVisitor extends SimpleFileVisitor<Path> {
	
	private static final Logger logger = LoggerFactory.getLogger(IssueFolderVisitor.class);
	
	private List<File> issueFolders = new ArrayList<File>();
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		if(isIssueFolder(dir, attrs)) {
			issueFolders.add(dir.toFile());
		}

		return super.preVisitDirectory(dir, attrs);
	}	
	
	public List<File> getIssueFolders() {
		return issueFolders;
	}
	

	protected boolean isIssueFolder(Path issueFolder, @SuppressWarnings("unused") BasicFileAttributes attrs) {
		boolean isIssueFolder = isNumeric(issueFolder.toFile().getName());
		
		if(logger.isDebugEnabled()) {
			logger.debug("Is {} skipped? {}", issueFolder, isIssueFolder);
		}
		
		return isIssueFolder;
	}
	
	private boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
