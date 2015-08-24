package sk.drakkar.oar.authors;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;

import java.util.Collection;
import java.util.Map;

public class AuthorProfilePageTemplater extends AbstractTemplater {
    public String convert(Author author, Collection<Article> articles) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("author", author.getFullName());
        model.put("articles", articles);

        return super.resolveTemplate("author-profile.html", model);
    }
}
