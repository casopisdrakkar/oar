package sk.drakkar.oar.plugin;

import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.PluginExecutionException;

public interface PortalAssemblyPlugin extends Plugin  {
    /**
     * Issued whenever all issues have been processed and the site building process is about to be
     * completed.
     * @param context Portal Assembly Pipeline context containing shared data between all
     *                plugins that support this method.
     * @throws PluginExecutionException
     */
    public void publicationComplete(Context context) throws PluginExecutionException;
}
