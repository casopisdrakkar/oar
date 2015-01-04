package sk.drakkar.oar;

import java.util.HashMap;
import java.util.Map;

public class ArticleTemplater extends AbstractTemplater {

	public String convert(Article article)  {
		Map<String, Object> model = new HashMap<>();
		model.put("article", article);

		return resolveTemplate("article.html", model);
	}
	
}
