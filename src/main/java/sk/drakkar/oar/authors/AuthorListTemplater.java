package sk.drakkar.oar.authors;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import freemarker.template.utility.ObjectWrapperWithAPISupport;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.TemplatingException;
import sk.drakkar.oar.template.ColorizeMethod;

public class AuthorListTemplater extends AbstractTemplater {
	private static final ObjectWrapperWithAPISupport NO_WRAPPER = null;

	private MostProductiveAuthorsBuilder mostProductiveAuthorsBuilder = new MostProductiveAuthorsBuilder();

	public String convert(Multimap<Author, Article> authorMap) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("authors", DefaultMapAdapter.adapt(authorMap.asMap(), NO_WRAPPER));
		model.put("authorProductivity", mostProductiveAuthorsBuilder.build(authorMap));

		model.put("colorize", new ColorizeMethod());

		return super.resolveTemplate("authors.html", model);
	}

	private ObjectWrapperWithAPISupport getWrapper() {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23);
		builder.setExposureLevel(BeansWrapper.EXPOSE_ALL);
		return builder.build();
	}

	public void setMostProductiveAuthorsBuilder(MostProductiveAuthorsBuilder mostProductiveAuthorsBuilder) {
		this.mostProductiveAuthorsBuilder = mostProductiveAuthorsBuilder;
	}

	public void setIgnoredMostProductiveAuthorNames(List<String> ignoredMostProductiveAuthorNames) {
		this.mostProductiveAuthorsBuilder.setIgnoredMostProductiveAuthorNames(ignoredMostProductiveAuthorNames);
	}
}
