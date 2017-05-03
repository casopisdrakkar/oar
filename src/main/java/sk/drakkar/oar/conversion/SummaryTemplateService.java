package sk.drakkar.oar.conversion;

import java.util.ArrayList;
import java.util.List;

public enum SummaryTemplateService {
    INSTANCE;

    private List<Summary> templates = new ArrayList<>();

    private SummaryTemplateService() {
        putArticle();
        putEditorial();
        putHaiku();
    }

    private void putArticle() {
        Summary article = new Summary();
        article.setTitle("Článek");
        article.setColor("gray");

        this.templates.add(article);
    }

    private void putHaiku() {
        Summary haiku = new Summary();
        haiku.setAuthors("redakce");
        haiku.setColor("gray");
        haiku.setTitle("Úvodní haiku");
        haiku.setTags("úvodní haiku, úvodník");
        haiku.setShortSummary("Úvodní haiku");

        this.templates.add(haiku);
    }

    private void putEditorial() {
        Summary editorial = new Summary();
        editorial.setAuthors("redakce");
        editorial.setColor("gray");
        editorial.setTitle("Úvodník");
        editorial.setTags("úvodník");
        editorial.setShortSummary("Úvodní slovo");

        this.templates.add(editorial);
    }

    public List<Summary> getTemplates() {
        return templates;
    }
}
