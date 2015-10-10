package sk.drakkar.oar.authors;

import com.google.common.collect.Multimap;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultMapAdapter;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.utility.ObjectWrapperWithAPISupport;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.template.ColorizeMethod;

import java.util.Map;

public class AuthorListTemplater extends AbstractTemplater {
	private static final ObjectWrapperWithAPISupport NO_WRAPPER = null;

	public String convert(Multimap<Author, Article> authorMap, Context context) {
		Map<String, Object> model = newModelFromhContext(context);
		model.put("authors", DefaultMapAdapter.adapt(authorMap.asMap(), NO_WRAPPER));
		model.put("colorize", new ColorizeMethod());


		return super.resolveTemplate("authors.html", model);
	}


	private ObjectWrapperWithAPISupport getWrapper() {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23);
		builder.setExposureLevel(BeansWrapper.EXPOSE_ALL);
		return builder.build();
	}

}
