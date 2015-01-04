package sk.drakkar.oar;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummaryExtractor {
	
	private static final Logger logger = LoggerFactory.getLogger(SummaryExtractor.class);

	private ToHtmlConverter toHtmlConverter = new PegdownConverter();

	public String getSummary(Article article) {
		String source = article.getSource();
		
		@SuppressWarnings("resource") Scanner scanner 
			= new Scanner(source).useDelimiter("\n\n");
		if(scanner.hasNext()) {
			String firstParagraph = scanner.next();
			
			logger.debug(firstParagraph);
			
			return toHtmlConverter.convert(firstParagraph);
		}
		
		return "";
	}
}
