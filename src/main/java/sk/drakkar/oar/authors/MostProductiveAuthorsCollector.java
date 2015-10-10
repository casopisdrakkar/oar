package sk.drakkar.oar.authors;

import com.google.common.collect.Multimap;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MostProductiveAuthorsCollector extends DefaultPlugin {
    public static class ContextVariables {
        public static class AuthorProductivityListVariable implements GlobalContextVariables.Variable<List<AuthorProductivity>> {}

        public static AuthorProductivityListVariable authorProductivityListVariable = new AuthorProductivityListVariable();
    }

    private List<String> ignoredMostProductiveAuthorNames = Collections.emptyList();

    @Override
    public void publicationComplete(Context context) {
        Multimap<Author, Article> authorMap = context.get(AuthorArticlesCollector.ContextVariables.authorArticles);
        context.put(ContextVariables.authorProductivityListVariable, getProductivities(authorMap));
    }

    public List<AuthorProductivity> getProductivities(Multimap<Author, Article> authorMap) {
        Map<Author, Integer> authorProductivity = new HashMap<>();
        for (Author author : authorMap.keySet()) {
            authorProductivity.put(author, authorMap.get(author).size());
        }

        List<AuthorProductivity> sortedAuthorProductivity = new LinkedList<>();
        authorProductivity
                .entrySet()
                .stream()
                .filter(entry -> !ignoredMostProductiveAuthorNames.contains(entry.getKey().getFullName()))
                .sorted(
                        Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(entry -> sortedAuthorProductivity.add(new AuthorProductivity(entry.getKey(), entry.getValue())));
        return sortedAuthorProductivity;
    }

    public void setIgnoredMostProductiveAuthorNames(List<String> ignoredMostProductiveAuthorNames) {
        this.ignoredMostProductiveAuthorNames = ignoredMostProductiveAuthorNames;
    }


}
