package sk.drakkar.oar.conversion;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ArticleSummaryPartitioner {
	private static final int SUMMARY_FIRST_LINE_INDEX = 9;

	private static final int TAGS_LINE_INDEX = 7;

	private static final int COLOR_LINE_INDEX = 5;

	private static final int AUTHORS_LINE_INDEX = 3;

	private static final int TITLE_LINE_INDEX = 1;

	private List<Summary> summaries = new LinkedList<Summary>();
	
	private Summary currentSummary;
	
	private int articleLineNumber;

	public List<Summary> convert(String summary) {
		Scanner scanner = new Scanner(summary);
		while(scanner.hasNextLine()) {
			processLine(scanner.nextLine());
		}
		// handle last line in file
		summaries.add(currentSummary);
		return this.summaries;
	}


	private void processLine(String line) {
		if(line.startsWith("---")) {
			if(currentSummary != null) {
				summaries.add(currentSummary);
			}
			currentSummary = new Summary();
			articleLineNumber = 0;
		}
		if(articleLineNumber == TITLE_LINE_INDEX) {
			currentSummary.setTitle(line);
		} else if(articleLineNumber == AUTHORS_LINE_INDEX) {
			currentSummary.setAuthors(line);
		} else if(articleLineNumber == COLOR_LINE_INDEX) {
			currentSummary.setColor(line);
		} else if(articleLineNumber == TAGS_LINE_INDEX) {
			currentSummary.setTags(line);
		} else if(articleLineNumber >= SUMMARY_FIRST_LINE_INDEX) {
			currentSummary.appendSummaryLine(line);
		}
		
		articleLineNumber++;
	}
	
}
