package sk.drakkar.oar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Scanner;

public class SummaryExtractor {
	
	private static final String METADATA_KEY_SUMMARY = "Summary";

	private static final Logger logger = LoggerFactory.getLogger(SummaryExtractor.class);

	private ToHtmlConverter toHtmlConverter = new PegdownConverter();

	public String getSummary(Article article, Map<String, Object> rawMetadata) {
		if(rawMetadata.containsKey(METADATA_KEY_SUMMARY)) {
			logger.debug("Extracted summary from article metadata");
			return (String) rawMetadata.get(METADATA_KEY_SUMMARY);
		}
		
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
