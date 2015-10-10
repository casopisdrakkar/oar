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
There are three independent pipelines for the data in the publication process.

*   **issue asset pipeline** is independently executed for each asset
(article text file, metadata, PDF etc) that is present in the issue folder
*   **issue pipeline** is independently executed for each issue, after the issue
asset pipeline completes its execution
*   **portal assembly pipeline** is executed once, whenever the last
issue pipeline completes its execution.

A plugin can become a part of any of the three pipelines.

Context
-------
Data flow in the pipeline is represented by the notion of *context*, which is
basically a hashmap from `Variable`s to arbitrary objects. The context
is passed between plugins in the pipeline and can be freely modified
during the pipeline execution.

`Oar` class
-----------
The `Oar` class is responsible for managing and executing the pipelines and for
registering the plugins into the pipelines.

Most importantly, it iterates over issue folders, executes
issue pipeline. Within this process, an issue asset pipeline
is executed on each asset in the issue folder. Finally,
the portal assembly pipeline is executed.

Plugins
-------
A plugin is executed in the pipeline whenever it overrides the following methods:

*   `articleProcessed()` for issue asset pipeline
*   `issueArticlesProcessed()` for issue pipeline
*   `publicationComplete()` for portal assembly pipeline

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

    public class MostProductiveAuthorsCollector extends DefaultPlugin {
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

