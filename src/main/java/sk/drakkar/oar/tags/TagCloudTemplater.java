package sk.drakkar.oar.tags;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import sk.drakkar.oar.AbstractTemplater;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Tag;
import sk.drakkar.oar.template.ColorizeMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TagCloudTemplater extends AbstractTemplater {
	public String convert(Multimap<Tag, Article> tagMap) {
		Map<String, Object> model = Maps.newHashMap();
		Map<Tag, Collection<Article>> tagMapppings = tagMap.asMap();

        List<TagEntry> tagEntries = new ArrayList<>();
		tagMapppings.forEach((tag, articles) -> {
            tagEntries.add(new TagEntry(tag, articles));
        });

		model.put("tags", tagEntries);
		model.put("colorize", new ColorizeMethod());
		
		return super.resolveTemplate("tags.html", model);
	}

	public static class TagEntry {
	    private int limit = 5;

	    private final Tag tag;

        private final List<Article> articles;

        public TagEntry(Tag tag, Collection<Article> articles) {
            this.tag = tag;
            this.articles = new ArrayList<>(articles);
        }

        public List<Article> getTopArticles() {
            if (articles.size() <= this.limit) {
                return articles;
            }
            return articles.subList(0, this.limit);
        }

        public boolean hasManyArticles() {
            return articles.size() > this.limit;
        }

        public String getSlug() {
            return tag.getSlug();
        }

        @Override
        public String toString() {
            return this.tag.toString();
        }
    }
}
