package sk.drakkar.oar.tags;

import com.google.common.collect.Maps;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Tag;

import java.util.Collection;
import java.util.Map;

public class TagDetailPageTemplater extends AbstractTemplater {

    public String convert(Tag tag, Collection<Article> articles) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("tag", tag);
        model.put("articles", articles);

        return super.resolveTemplate("tag-detail.html", model);
    }
}
