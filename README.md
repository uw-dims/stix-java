README file for stix-java, Stuart Maclean, Sep 2014
===================================================

A Java binding to Mitre's STIX xsd file set. See http://stix.mitre.org.

The goal is to generate Java bindings to the .xsd files with a minimum
of 'locally added' logic.  We made zero edits to any xsd files.  We
preserve package names, etc.  Where we simply MUST make decisions
locally, essentially to resolve name clashes, we use a .xjb file as
standard for xjc.

To answer the question 'Are these bindings any good?', we try to
assert Yes by consuming into a Java program a series of 33 sample STIX
documents.  See the test cases
[here](./jaxb/src/test/java/edu/uw/apl/stix/jaxb) and the sample
documents [here](./jaxb/src/test/resources). Most samples were
obtained from
[Mitre](https://stix.mitre.org/language/version1.1.1/samples/stix1.1.1-samples-all.zip).

PRE-REQUISITES
==============

This codebase is Java, and so needs a Java compiler system, aka a
'Java Developmment Kit (JDK)'.  A 1.6 or later JDK is required.

The build is via Maven, a build and project management tool for Java
artifacts. So Maven is required too.  All code dependencies are
resolved by Maven. The author uses Maven 2.2.1 on Ubuntu 12.04 LTS.

INSTALLATION
============

Currently this codebase is organised as three Maven 'modules', with a
parent pom at the root level.

* Module 1: the jaxb bindings.  Java classes auto-generated from .xsd
file set via xjc. xjc is bundled with recent JDK releases (1.6+). This
module includes some sample STIX documents (from Mitre and elsewhere).
See under jaxb/src/test/resources.

Getting the xsd file set to build took some work. See
[./jaxb/README.md](./jaxb/README.md) for more details.

* Module 2: utils. Example routines for extracting content from a STIX package.  Eventually, authoring utils would be added too.

* Module 3: cli.  Example command line driven tools for processing STIX documents.

To build:

```
$ cd stix-java

$ mvn install
```
To then try out a bundled command line interface (cli) tool:
```
$ cd stix-java/cli

$ ./stix.md5 ../jaxb/src/test/resources/APT1/Appendix_G_IOCs_Full.xml
``` 

The tool simply loads the supplied file and extracts any md5 hashes
found in Indicators and/or Observables.  It should print a list of
1797 md5 hashes to stdout. The stix.md5 file is a simple bash script
driving the JVM invocation of the appropriate class.


Observation: This whole jaxb lark is way too complicated. Just use grep!

eof
