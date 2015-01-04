package sk.drakkar.oar;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public abstract class AbstractTemplater {
	protected Configuration freemarkerConfiguration = FreemarkerConfigurationFactory.INSTANCE.getConfiguration();
	
	protected String resolveTemplate(String templateName, Object model) {
		try {
			StringWriter buffer = new StringWriter();
			freemarkerConfiguration.getTemplate(templateName).process(model, buffer);
			return buffer.toString();
		} catch (TemplateException e) {
			throw new TemplatingException("Cannot build template due to error in template resolution " + templateName);
		} catch (IOException e) {
			throw new TemplatingException("Cannot read template or write to the result file " + templateName);
		}
		
	}

}
