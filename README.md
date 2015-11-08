# Oar

Oar is a static site generator for RPG web-zine Drakkar.sk

## Usage

### Data

Oar is supposed to use use data from casopisdrakkar/clanky repository. Magazine issues are stored in directories and include YAML file that includes issue metadata. Every article is in its own file, with YAML metadata in header and Markdown text after it, separated by dashes. See the repository for more info.

### Building

Build a jar file.

    mvn clean assembly:assembly -DdescriptorId=jar-with-dependencies

### Generating the web page

Run the jarfile, it expects directory with issue sources.

# Architecture

Oar is composed from the following fundamental components:

*   pipelines, representing a data flow in the publication process
*   plugins, each delivering a specific feature or functionality of the portal
*   basic `Oar` class serving as a starting point for the portal publishing

Pipelines and Contexts
=======================
There are three interconnected pipelines for the data in the publication process.

*   **asset assembly pipeline** is independently executed for each asset
(article text file, metadata, PDF etc) that is present in the issue folder
*   **issue pipeline** is independently executed for each issue, after the issue
asset pipeline completes its execution
*   **portal assembly pipeline** is executed once, whenever the last
issue pipeline completes its execution.

A plugin can become a part of any of the three pipelines.

Plugins
-------
There are three types of plugins which can be connected to any of the three pipelines

*   `AssetAssemblyPlugin` with `onAsset()` method that is called for any asset (resource, file, image, PDF)
in the issue folder.
    *   `ArticleAssemblyPlugin` is often used special subclass that offers `articleProcessed()`
    to easily listen for article processing events
*   `IssueAssemblyPlugin` with `issueArticlesProcessed()` method that is called in the Issue Assembly Pipeline
*   `PortalAssemblyPlugin` with `publicationComplete()` method that is called in the Portal Assembly Pipeline

Context
-------
Data flow in the pipeline is represented by the notion of *context*, which is
basically a hashmap from `Variable`s to arbitrary objects. The context
is passed between plugins in the pipeline and can be freely modified
during the pipeline execution.


Data flow and the `Oar` class
------------------------------
The `Oar` class is responsible for managing and executing the pipelines and for
registering the plugins into the pipelines.

Most importantly, it iterates over issue folders and executes
Issue Assembly Pipeline. Within this process, an Asset Assembly Pipeline
is executed on each asset in the issue folder. Finally,
the Portal Assembly Pipeline is executed.

Imagine the following structure:

    1
    |- about-sea-dracula.md
    |-  metadata.yaml
    2
    |- playing-modern-rpg.md
    |- playing-old-school-rpg.md
    |-  metadata.yaml

### First directory: asset assembly
At first, a treewalker over directory structure is created, processing
directories in the depth-first manner. This means, that
at first the directory `1` is processed. An `Issue` object
is created and the issue assets will be processed. More specifically,
there are two assets in the `1` folder: a Markdown file and the YAML metadata.

For each file, a new Asset Assembly pipeline with new Context is created
and all plugins in this pipeline are executed. Plugins in this pipeline
usually parse the input files, put the information into the context,
copy the assets into the output folder etc.

### First directory: issue assembly

Whenever all assets in the directory are processed, a new
Issue Assembly Pipeline is created, along with independently
created new context and all `IssueAssemblyPlugin`s are executed.

### Second directory: asset assembly and issue assembly

The similar process is executed on the `2` directory. All three
assets (two Markdowns and a single YAML) are processed in
three independent Asset Assembly Pipelines, with three independent
contexts. When all three assets in this are processed, the
Issue Assembly Pipeline will be called.

### Portal Assembly Pipeline
Finally, the Portal Assembly Pipeline will be executed, along
with a new context,
where all registered `PortalAssemblyPlugin`s will be executed.

Contexts and data flow
----------------------
Note that pipelines have independent lifecycles that
correspond to independent data flow:

*   *Asset Assembly Pipeline* is quite short-lived.
The context exists only during the handling of the asset file.
Each file spawns a new Asset Assembly Pipeline with a new
contexts.
*   *Issue Assembly Pipeline* runs just after all assets in the
issue are processed. Each processed issue executes an independent
IAP with independent context
*   *Portal Assembly Pipeline* runs just after all issues are processed.
A single PAP with independent context will be created.

Implementing plugins
====================
Suppose that you want to implement a new plugin that copies
fonts used in the CSS into the output folder.

At first, choose one of the three pipelines, which
will be connected with this plugin. Remember, there
are three pipelines: one that handles assets, one
that handles issues and the last one that handles portal
assembly.

In our example, the font copy may be executed once,
after the portal is assembled. Therefore, we create
a class that implements `PortalAssemblyPlugin`
and implement the method:

    public void publicationComplete(Context context)

Access to global configuration
-----------------------------------
Your plugin may access the system-wide `Configuration`
object that holds the information about output folder
and other settings.

If you subclass the `ConfigurationSupport`, you
may retrieve access to this global configuration
and use it in your plugins.

Our plugin will need to know where to put the fonts
in the output, so subclassing `ConfigurationSupport`
is very convenient.

Registering plugin to Oar
-------------------------
Currently, the configuration of plugins is done
statically in the `main()` method of `Oar` class.

    CopyFontsPlugin copyFontsPlugin = new CopyFontsPlugin(configuration);
    oar.addPlugin(copyFontsPlugin);

Note that since your plugin extends
`ConfigurationSupport`, you are provided
a constructor with pre-existing `Configuration` object.

Implementing article-handling plugins
-------------------------------------
Many plugins will process the actual `Article` objects.
In this case, subclass `ArticleAssemblyPlugin`
with method

    void articleProcessed(Article article, Context context) throws PluginExecutionException;

If you would like to gain access to the global configuration
object, subclass `ConfigurableArticleAssemblyPlugin`.


Typed contexts
--------------
A mapping in the context are typed. Each key is of type
`GlobalContextVariables.Variable<T>`,
where `T` represents the type of the value in the context.

Each plugin should, by convention, define an internal static
class `ContextVariables` that defines a typesafe information
about properties that will be put into the context by this plugin.

For example, the `MostProductiveAuthorsCollector` exposes
an `authorProductivityListVariable` variable in the context:

    public class MostProductiveAuthorsCollector ... {
        public static class ContextVariables {
            public static class AuthorProductivityListVariable implements GlobalContextVariables.Variable<List<AuthorProductivity>> {}

            public static AuthorProductivityListVariable authorProductivityListVariable = new AuthorProductivityListVariable();
        }
        ...

Accessing context variables in Freemarker templates
---------------------------------------------------

You may access the context variables in the Freemarker templates
by using the following convention. The variable
from the previous example in the `MostProductiveAuthorsCollector`
can be accessed with the following expression:

    mostProductiveAuthorsCollector.authorProductivityList

