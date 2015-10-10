package sk.drakkar.oar.homepage;

import com.google.common.collect.Maps;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Issue;

import java.util.List;
import java.util.Map;

public class HomePageTemplater extends AbstractTemplater {
	public String convert(List<Issue> issues) {
		Map<String, Object> model = Maps.newHashMap();
		model.put("issues", issues);
		
		return super.resolveTemplate("index.html", model);
	}	
}