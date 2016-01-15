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

.. _fileObservable:

----------------------------
File Observable Information
----------------------------

You can extract information about all the File Observables from a STIX document.
This will return a JSON list of objects.

.. code-block:: none

    $ stix.extract.fileinfo input-stix-document.xml
    [
      {
        "fileName": "Some File.pdf",
        "fileSize": 0,
        "hashes": {}
      },
      {
        "fileName": "Some Other file.pdf",
        "fileSize": 1234,
        "hashes": {
          "SHA256": "42cff127a81e0d9ad2876ec9e0787d7ac3926c6bbe8c7f2c6d8cd87895431e07",
          "SHA1": "1e487c9378afb0191e487018a211aee6a3251104",
          "MD5": "3e48a8d6d2301f4a3ffd3bcb3f156abe"
        }
      }
    ]

..

Not all file observables have all the information.


*************************
Creating a STIX Document
*************************

The STIX Java tool only supports creating a STIX document with file name and MD5 hash indicators.

To create a document, use the ``stix.author.md5`` command and provide a list of MD5 hashes and file name pairs.

By default, the tool will print the resulting STIX XML to STDOUT. Use the `-o` option to specify a file for the output.

The format for a hash and file name pair is ``"MD5,/path/to/file"`` including quotes. Don't forget the comma between the
hash and file path - that's how the tool separates the two. If you do not have a file name/path, just provide the hash.

Example:

.. code-block:: none

    $ stix.author.md5 "026871ea3d6cbbeb90fea6bf2906cc12,/some/file" "02ac495eb31a2405fce287565b590a1f,/some/other/file" 0678645e45fcd3da84ab27122d6775a9
    <?xml version="1.0" encoding="UTF-8"?>
    <stix:STIX_Package xmlns="http://xml/metadataSharing.xsd"
        xmlns:FileObj="http://cybox.mitre.org/objects#FileObject-2"
        xmlns:cybox="http://cybox.mitre.org/cybox-2"
        xmlns:cyboxCommon="http://cybox.mitre.org/common-2"
        xmlns:cyboxVocabs="http://cybox.mitre.org/default_vocabularies-2" xmlns:stix="http://stix.mitre.org/stix-1">
        <stix:Observables cybox_major_version="2" cybox_minor_version="1">
            <cybox:Observable>
                <cybox:Object>
                    <cybox:Properties
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="FileObj:FileObjectType">
                        <FileObj:File_Name condition="Equals">/some/file</FileObj:File_Name>
                        <FileObj:Hashes>
                            <cyboxCommon:Hash>
                                <cyboxCommon:Type xsi:type="cyboxVocabs:HashNameVocab-1.0">MD5</cyboxCommon:Type>
                                <cyboxCommon:Simple_Hash_Value>026871ea3d6cbbeb90fea6bf2906cc12</cyboxCommon:Simple_Hash_Value>
                            </cyboxCommon:Hash>
                        </FileObj:Hashes>
                    </cybox:Properties>
                </cybox:Object>
            </cybox:Observable>
            <cybox:Observable>
                <cybox:Object>
                    <cybox:Properties
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="FileObj:FileObjectType">
                        <FileObj:File_Name condition="Equals">/some/other/file</FileObj:File_Name>
                        <FileObj:Hashes>
                            <cyboxCommon:Hash>
                                <cyboxCommon:Type xsi:type="cyboxVocabs:HashNameVocab-1.0">MD5</cyboxCommon:Type>
                                <cyboxCommon:Simple_Hash_Value>02ac495eb31a2405fce287565b590a1f</cyboxCommon:Simple_Hash_Value>
                            </cyboxCommon:Hash>
                        </FileObj:Hashes>
                    </cybox:Properties>
                </cybox:Object>
            </cybox:Observable>
            <cybox:Observable>
                <cybox:Object>
                    <cybox:Properties
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="FileObj:FileObjectType">
                        <FileObj:Hashes>
                            <cyboxCommon:Hash>
                                <cyboxCommon:Type xsi:type="cyboxVocabs:HashNameVocab-1.0">MD5</cyboxCommon:Type>
                                <cyboxCommon:Simple_Hash_Value>0678645e45fcd3da84ab27122d6775a9</cyboxCommon:Simple_Hash_Value>
                            </cyboxCommon:Hash>
                        </FileObj:Hashes>
                    </cybox:Properties>
                </cybox:Object>
            </cybox:Observable>
        </stix:Observables>
    </stix:STIX_Package>
    $

..
