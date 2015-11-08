package sk.drakkar.oar.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Pipeline<P extends Plugin> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private List<P> plugins = new LinkedList<>();

    public Pipeline() {
        // empty constructor
    }

    public Pipeline(Collection<P> plugins) {
        this.plugins.addAll(plugins);
    }

    public void add(P plugin) {
        this.plugins.add(plugin);
    }

    public PipelineResult execute(Context context) {
        beforeExecute(context);
        for (P plugin : plugins) {
            try {
                logger.debug("Executing plugin " + plugin + " in pipeline " + this.getClass().getName());
                doWithPlugin(plugin, context);
            } catch (PipelineStoppedException e) {
                logger.debug("Stopping current pipeline: " + e.getMessage());
                return PipelineResult.STOPPED;
            } catch (Exception e) {
                String message = "General exception in the pipeline plugin " + plugin;
                if(logger.isTraceEnabled()) {
                    message += " " + context;
                }
                logger.error(message, e);
                return PipelineResult.THROWN_EXCEPTION;
            }
        }
        return PipelineResult.COMPLETED;
    }

    protected void beforeExecute(Context context) {
        // do nothing
    }

    protected abstract void doWithPlugin(P plugin, Context context) throws PluginExecutionException;

    /**
     * Return a copy of list of plugins in the pipeline
     */
    protected Collection<P> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("\n");
        for (P plugin : this.plugins) {
            sb.append("  ").append(plugin.getClass().getSimpleName()).append("\n");
            sb.append("          |\n");
            sb.append("          V\n");
        }
        sb.append("         ---\n");
        return sb.toString();
    }

}
