package sk.drakkar.oar.conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import com.github.slugify.Slugify;

public class SummaryExporter {
	private static final String ENCODING_UTF_8 = "utf-8";

	public void writeIssueMetadata(Collection<Summary> summaries, File targetFolder) {
		File metadataFile = new File(targetFolder, "metadata.yaml");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(metadataFile, ENCODING_UTF_8);
			writeIssueMetadata(summaries, writer);
			writer.flush();
		} catch (FileNotFoundException e) {
			throw new ConversionException("Cannot save to file " + metadataFile, e);
		} catch (UnsupportedEncodingException e) {
			throw new ConversionException("Cannot save to file as UTF-8" + metadataFile, e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public void writeIssueMetadata(Collection<Summary> summaries, PrintWriter writer) {
		writer.println("---");
		writer.println("pdf: " + "???");
		writer.println("articles: ");
		for (Summary summary : summaries) {
			writer.println("- " + getFileName(summary) + ".md");
		}
	}

	public void writeSummaries(Collection<Summary> summaries, File targetFolder) {
		for (Summary summary : summaries) {
			File file = new File(targetFolder, getFileName(summary) + ".md");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file, ENCODING_UTF_8);
				writeSummary(summary, writer);
				
				writer.flush();
			} catch (FileNotFoundException e) {
				throw new ConversionException("Cannot save to file " + file, e);
			} catch (UnsupportedEncodingException e) {
				throw new ConversionException("Cannot save to file as UTF-8" + file, e);
			} finally {
				if(writer != null) {
					writer.close();
				}
			}
			
		}		
	}

	private void writeSummary(Summary summary, PrintWriter writer) {
		writer.println("---");
		writer.println("Title: \"" + summary.getTitle() + "\"");
		writer.println("Authors: " + summary.getAuthors());
		writer.println("Tags: " + summary.getTags());
		writer.println("Color: " + summary.getColor());
		if(summary.hasShortSummary()) {
			writer.println("Summary: " + summary.getShortSummary());
		}

		writer.println("---");
		
		writer.println(summary.getSummary());
	}
	
	private String getFileName(Summary summary) {
		try {
			Slugify slugify = new Slugify(true);
			return slugify.slugify(summary.getTitle());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
