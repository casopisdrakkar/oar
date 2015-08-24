package sk.drakkar.oar;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public enum FreemarkerConfigurationFactory {
	INSTANCE;
	
	private Configuration freemarkerConfiguration;

	public Configuration getConfiguration() {
		if(this.freemarkerConfiguration == null) {		
			freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_23);

			freemarkerConfiguration.setClassForTemplateLoading(Oar.class, "");
			freemarkerConfiguration.setDefaultEncoding("UTF-8");

			freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			freemarkerConfiguration.setAPIBuiltinEnabled(true);
		} 
		return freemarkerConfiguration;
	}
}
