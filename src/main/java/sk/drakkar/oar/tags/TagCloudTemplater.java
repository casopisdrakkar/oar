package sk.drakkar.oar.tags;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.template.ColorizeMethod;
import sk.drakkar.oar.template.SlugifyMethod;

import java.util.Map;

public class TagCloudTemplater extends AbstractTemplater {
	public String convert(Multimap<String, Article> tagMap) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("tags", tagMap.asMap());

		model.put("slugify", new SlugifyMethod());
		model.put("colorize", new ColorizeMethod());
		
		return super.resolveTemplate("tags.html", model);
	}
}
