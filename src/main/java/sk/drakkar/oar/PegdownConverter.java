package sk.drakkar.oar;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class PegdownConverter implements ToHtmlConverter{
	private PegDownProcessor markdownProcessor;

	public PegdownConverter() {
		markdownProcessor = new PegDownProcessor(Extensions.ALL - Extensions.HARDWRAPS);
	}
	
	@Override
	public String convert(String markdownSource) {
		return markdownProcessor.markdownToHtml(markdownSource);
	}

}
