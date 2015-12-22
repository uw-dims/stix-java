=================================
Running
=================================

***********************
Extracting Information
***********************

There are three types of data that the STIX Java tools can process and extract from STIX
documents: MD5 file hashes, IP addresses, and host names.

All the extraction tools take a single parameter, the input file. While they are parsing the file,
they may print some XML syntax warnings to STDERR.
When they have processed the file, they will print the results to STDOUT, one per line.


----------------
MD5 File Hashes
----------------

To extract MD5 file hashes, use the ``stix.extract.md5`` tool.

Example:

.. code-block:: none

    $ stix.extract.md5 input-stix-document.xml

    EventT
        Severity: 2
        Message: cvc-elt.4.2: Cannot resolve 'CISCP:IndicatorTypeVocab-0.0' to a type definition for element 'indicator:Type'.
        Linked Excpetion: org.xml.sax.SAXParseException; lineNumber: 125; columnNumber: 65; cvc-elt.4.2: Cannot resolve 'CISCP:IndicatorTypeVocab-0.0' to a type definition for element 'indicator:Type'.
        Locator
        Line Number: 125
        Column Number: 65
        Offset: -1
        Object: null
        Node: null
        URL: null

    EventT
        Severity: 1
        Message: unrecognized type name: {http://www.us-cert.gov/ciscp}IndicatorTypeVocab-0.0
        Linked Excpetion: null
        Locator
        Line Number: 125
        Column Number: 65
        Offset: -1
        Object: null
        Node: null
        URL: null
    026871ea3d6cbbeb90fea6bf2906cc12
    02ac495eb31a2405fce287565b590a1f
    0678645e45fcd3da84ab27122d6775a9
    0a43013eef1c2ffba36e3c29512c89a2
    1f43738b1f67266fdafd73235acbf338
    $

..

.. note::

    The EventT block in the code examples is a XML syntax warning, and is printed to STDERR.
    You can ignore these warnings.

    All further examples will not contain these warnings.

..

----------------
IP Addresses
----------------

To extract IP addresses, use the ``stix.extract.ip`` tool.

Example:

.. code-block:: none

    $ stix.extract.ip input-stix-document.xml
    125.141.229.78
    204.74.215.58
    98.126.148.114
    219.90.112.203
    $

..


----------------
Host Names
----------------

To extract host names, use the ``stix.extract.hostname`` tool.

Example:

.. code-block:: none

    $ stix.extract.hostname input-stix-document.xml
    www.dhcpserver.ns01.us
    www.dnsserver.ns01.us
    www.hq.dsmtp.com
    www.hq.dynssl.com
    www.msnet.freetcp.com
    $

..


