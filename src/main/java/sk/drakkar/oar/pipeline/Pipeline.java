package sk.drakkar.oar.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.plugin.Plugin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Pipeline {
    public static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    private List<Plugin> plugins = new LinkedList<>();

    public Pipeline() {
        // empty constructor
    }

    public Pipeline(Collection<Plugin> plugins) {
        this.plugins.addAll(plugins);
    }

    public void add(Plugin plugin) {
        this.plugins.add(plugin);
    }

    public void execute(Context context) {
        for (Plugin plugin : plugins) {
            try {
                logger.debug("Executing plugin " + plugin + " in pipeline " + this.getClass());
                doWithPlugin(plugin, context);
            } catch (AbortPipelineException e) {
                logger.debug("Pipeline aborted due to " + e.getMessage());
                break;
            } catch (Exception e) {
                String message = "General exception in the pipeline plugin " + plugin;
                if(logger.isTraceEnabled()) {
                    message += " " + context;
                }
                logger.error(message, e);
            }
        }
    }

    protected abstract void doWithPlugin(Plugin plugin, Context context) throws PluginExecutionException;
}
