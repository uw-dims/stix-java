README file for stix-jaxb, Stuart Maclean, Sep 2014
===================================================

PRE-REQUISITES
==============

This codebase is Java, and so needs a Java compiler system, aka a
'Java Developmment Kit (JDK)'.  A 1.6 or later JDK is required.

The build is via Maven, a build and project management tool for Java
artifacts. So Maven is required too.  All code dependencies are
resolved by Maven. The author uses Maven 2.2.1 on Ubuntu 12.04 LTS.

INSTALLATION
============

Currently this is a 3-module codebase:

* Module 1: the jaxb bindings.  Java classes auto-generated from .xsd
file set via xjc.  Includes some sample STIX documents (from Mitre and
elsewhere) in src/test/resources.

* Module 2: utils. Example routines for extracting content from a STIX package.  Eventually, authoring utils would be added too.

* Module 3: cli.  Example command line driven tools for processing STIX documents.

To build:

$ cd stix-java

$ mvn install

To then try out a bundled command line interface (cli) tool:

$ cd stix-java/cli

$ ./stix.md5 ../jaxb/src/test/resources/APT1/Appendix_G_IOCs_Full.xml

which should print a list of 1797 md5 hashes to stdout.

JAXB Issues
===========

At time of writing, the STIX schema file set used for Java class
generation was version 1.1.1.  See
https://stix.mitre.org/language/version1.1.1/stix_v1.1.1.zip.

Downloading this package and running xjc on the file set leads to a
myriad of xjc complaints/errors.  To combat these, the author wrote a
preprocessing tool which first scans the xsd file graph (all supplied
files plus all those reachable via schema imports, and so on
recursively).  This tool (https://github.com/UW-APL-EIS/xsdwalker)
then outputs an 'uber' xsd consisting solely of imports of all the xsd
files known to cause xjc NO errors.  

Some released .xsd files were problemmatic and thus 'excluded' from
the 'uber' xsd file.  This means that there may be valid STIX
documents which cannot be correctly ingested by these jaxb bindings.
Similarly, there will be STIX constructs which cannot be authored
using these Java bindings.  Specifically, we excluded from our uber
set the './extensions/vulnerability' directory and the
'./extensions/test_mechanism/oval.xsd'. file.

For some documentation on xsdwalker, see

https://github.com/UW-APL-EIS/xsdwalker/blob/master/doc/xsdwalker-STIX-SeattleJavaUserGroup.pdf

# eof
