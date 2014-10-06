README file for stix-java, jaxb sub-module. Stuart Maclean, Sep 2014
====================================================================


At time of writing, the STIX schema file set used for Java class
generation was version 1.1.1.  See
https://stix.mitre.org/language/version1.1.1/stix_v1.1.1.zip.

The xsd file set included in this codebase was generated thus:

```
$ cd stix-java/jaxb/src/main/resources/1.1.1

$ wget https://stix.mitre.org/language/version1.1.1/stix_v1.1.1.zip

$ jar xvf stix_v1.1.1.zip

$ wget https://cybox.mitre.org/language/version2.1/cybox_v2.1.zip

$ mkdir cybox_v2.1

$ cd cybox_v2.1

$ jar xvf ../cybox_v2.1.zip

$ rm -r extensions (since stix/extensions has these)

$ cd ..

$ wget http://maec.mitre.org/language/version4.1/maec_4.1.zip

$ jar xvf maec_4.1.zip
```

We made no edits to any surviving xsd file.

Running xjc on the resulting file set leads to a myriad of xjc
complaints/errors.  To combat these, the author wrote a preprocessing
tool which first scans the xsd file graph (all supplied files plus all
those reachable via schema imports, and so on recursively).  This tool
(https://github.com/UW-APL-EIS/xsdwalker) then outputs an 'uber' xsd
consisting solely of imports of all the xsd files known to cause xjc
NO errors.

Some released .xsd files were problemmatic and thus 'excluded' from
the 'uber' xsd file.  This means that there may be valid STIX
documents which cannot be correctly ingested by these jaxb bindings.
Similarly, there will be STIX constructs which cannot be authored
using these Java bindings.  Specifically, we excluded from our uber
set:

1 extensions/vulnerability/cvrf_1.1_vulnerability.xsd

2 extensions/test_mechanism/oval_5.10_test_mechanism.xsd

The exact derivation of our uber xsd file was then:

```
$ cd stix-java/jaxb/src/main/resources/1.1.1/

$ xsdwalker *.xsd cybox_v2.1 maec_4.1 extensions -e extensions/test_mechanism/oval_5.10_test_mechanism.xsd -e extensions/vulnerability -u 1.1.1
```

The -e options exclude the specified files/directories from the xsd
graph to be supplied to xjc. The -u option names the uber file.

Since the STIX xsd schemas do NOT directly reference other efforts,
e.g. CyBox, MAEC, but STIX documents often (?) include elements from
these schemas, we copied in by hand the cybox_v2.1 and maec_4.1 xsd
file sets into our stix xsd file set, rather than trying to work with
'episodic jaxb generation'.


xsdwalker
---------

For some documentation on xsdwalker, see

https://github.com/UW-APL-EIS/xsdwalker/

including

https://github.com/UW-APL-EIS/xsdwalker/blob/master/doc/xsdwalker-STIX-SeattleJavaUserGroup.pdf

