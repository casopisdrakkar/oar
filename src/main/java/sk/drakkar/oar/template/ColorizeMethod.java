package sk.drakkar.oar.template;

import freemarker.template.*;
import sk.drakkar.oar.ColorGenerator;
import sk.drakkar.oar.Slugger;

import java.util.List;

public class ColorizeMethod implements TemplateMethodModelEx {
    private ColorGenerator colorGenerator = new ColorGenerator();

    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments. Expected 1 argument, but found " + args.size());
        }
        String string = ((TemplateScalarModel) args.get(0)).getAsString();
        return new SimpleScalar(colorGenerator.getColor(string));
    }
}
