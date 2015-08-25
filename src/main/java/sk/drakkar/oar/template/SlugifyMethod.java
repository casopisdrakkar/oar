package sk.drakkar.oar.template;

import freemarker.ext.beans.StringModel;
import freemarker.template.*;
import sk.drakkar.oar.Slugger;

import java.util.List;

public class SlugifyMethod implements TemplateMethodModelEx {
    private Slugger slugger = new Slugger();

    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments. Expected 1 argument, but found " + args.size());
        }
        String string = ((SimpleScalar) args.get(0)).getAsString();
        return new SimpleScalar(slugger.toSlug(string));
    }
}

