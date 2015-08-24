package sk.drakkar.oar.authors;

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

public class AuthorListTemplater extends AbstractTemplater {
	private static final ObjectWrapperWithAPISupport NO_WRAPPER = null;

	public String convert(Multimap<Author, Article> authorMap) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("authors", DefaultMapAdapter.adapt(authorMap.asMap(), NO_WRAPPER));

		return super.resolveTemplate("authors.html", model);
	}

	private ObjectWrapperWithAPISupport getWrapper() {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23);
		builder.setExposureLevel(BeansWrapper.EXPOSE_ALL);
		return builder.build();
	}
}
