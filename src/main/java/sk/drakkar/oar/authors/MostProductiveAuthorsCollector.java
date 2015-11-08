package sk.drakkar.oar.authors;

import com.google.common.collect.Multimap;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.util.*;

public class MostProductiveAuthorsCollector implements PortalAssemblyPlugin {
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
