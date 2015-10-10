package sk.drakkar.oar.template;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import sk.drakkar.oar.ColorGenerator;

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
