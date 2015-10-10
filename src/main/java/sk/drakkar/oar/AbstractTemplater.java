package sk.drakkar.oar;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplater {
	protected Configuration freemarkerConfiguration = FreemarkerConfigurationFactory.INSTANCE.getConfiguration();

	/**
	 * Returns a new model with predefined properties exposed
	 * by context
	 * @param context a context that should be exposed in model
	 * @return brand new model with properties from context.
	 *
	 * @see ContextVariableUtils#constructModel(GlobalContextVariables.Variable, Object)
	 */
	protected Map<String, Object> newModelFromhContext(Context context) {
		Map<String, Object> model = Maps.newHashMap();
		exposeContextInModel(model, context);

		return model;
	}


	protected Map<String, Object> exposeContextInModel(Map<String, Object> model, Context context) {
		Map<String, Object> contextModel = new HashMap<>();
		for(Map.Entry<GlobalContextVariables.Variable<? extends Object>, Object> entry : context.entrySet()) {
			Map<String, Object> map = ContextVariableUtils.constructModel(entry.getKey(), entry.getValue());
			contextModel.putAll(map);
		}
		model.putAll(contextModel);

		return model;
	}

	protected String resolveTemplate(String templateName, Object model) {
		try {
			StringWriter buffer = new StringWriter();
			freemarkerConfiguration.getTemplate(templateName).process(model, buffer);
			return buffer.toString();
		} catch (TemplateException e) {
			throw new TemplatingException("Cannot build template due to error in template resolution " + templateName, e);
		} catch (IOException e) {
			throw new TemplatingException("Cannot read template or write to the result file " + templateName, e);
		}
		
	}

}
