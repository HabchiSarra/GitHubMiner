# GitHubMiner
## Description

Given the link of a GitHub repository, GitHubMiner fetches all its compenents:

* Commits;
* Developers;
* Issues;
* Pull requests;
* Labels.

These compenents are saved in a graph database with Neo4J, which represents the repository and its internal relationships.

This project is part of the [Sniffer Toolkit](https://github.com/HabchiSarra/Sniffer). <br /> 
For more details about the tool and its associated research work, you can refer to the following research papers:

* [The rise of Android code smells: Who is to blame?](https://ieeexplore.ieee.org/document/8816779)
* [On the survival of Android code smells in the wild.](https://ieeexplore.ieee.org/abstract/document/8816910)

## Build

To build the jar, use the command `mvn package`
