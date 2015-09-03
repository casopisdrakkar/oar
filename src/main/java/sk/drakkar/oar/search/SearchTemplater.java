package sk.drakkar.oar.search;

import sk.drakkar.oar.AbstractTemplater;

public class SearchTemplater extends AbstractTemplater {
    public String getSearchPage() {
        return super.resolveTemplate("search.html", new Object());
    }
}
