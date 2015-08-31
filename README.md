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
