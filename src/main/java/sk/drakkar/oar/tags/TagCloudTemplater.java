package sk.drakkar.oar.tags;

import java.util.Map;

import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class TagCloudTemplater extends AbstractTemplater {
	public String convert(Multimap<String, Article> tagMap) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("tags", tagMap.asMap());
		
		return super.resolveTemplate("tags.html", model);
	}
}
