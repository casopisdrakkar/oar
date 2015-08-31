package sk.drakkar.oar.pages;

import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;

import java.util.HashMap;
import java.util.Map;

public class PageTemplater extends AbstractTemplater {
    public String convert(Article article)  {
        Map<String, Object> model = new HashMap<>();
        model.put("article", article);

        return resolveTemplate("page.html", model);
    }
}
