package sk.drakkar.oar.authors;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;

public class AuthorListTemplater extends AbstractTemplater {
	public String convert(Multimap<String, Article> authorMap) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("authors", authorMap.asMap());
		
		return super.resolveTemplate("authors.html", model);
	}	
}
