package sk.drakkar.oar.authors;

import com.google.common.base.Functions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import sk.drakkar.oar.Article;

import java.util.*;
import java.util.stream.Stream;

public class MostProductiveAuthorsBuilder {
    private List<String> ignoredMostProductiveAuthorNames = Collections.emptyList();

    public List<AuthorProductivity> build(Multimap<Author, Article> authorMap) {
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
