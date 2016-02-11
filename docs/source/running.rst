=================================
Running
=================================

***********************
Extracting Information
***********************

There are multiple types of data that the STIX Java tools can process and extract from STIX
documents: MD5 file hashes, file observable information, IP addresses, and host names.

All the extraction tools require a single parameter, the input file.
You can specify the minimum and/or maximum TLP TLP level for documents with the ``-minTlp`` and ``-maxTlp`` options.
For information on the TLP protocol, see `US-CERT <https://www.us-cert.gov/tlp>`_.

While they are parsing the file,they may print some XML syntax warnings to STDERR. These can be ignored.
When they have processed the file, they will print the results to STDOUT or a file specified by the ``-o`` option.

.. _jsonSummary:

----------------
JSON Summary
----------------

To extract a JSON summary of a STIX document or bundle, use the ``stix.extract.json`` tool.
This extract information fromt he header, including the ID, title, summary, production date, and TLP marking.
Additionally, it extracts and IPs, hostnames, URIs, and file information from any observable indicators.

It will always output a list of the JSON summary objects. The structure is as follows:

.. code-block:: json

  [
    {
      "header": {
        "id": "CISCP:IB-01-23456",
        "title": "Phishing Emails",
        "description": "Emails were sent to try and steal credentials.",
        "producedTime": "Jan 6, 2015 9:45:00 AM",
        "marking": {
          "level": "AMBER"
        }
      },
      "observableIpAddresses": [],
      "observableHostnames": [],
      "observableURIs": [
        "http://www.attachment.com/mail/u/0",
        "http://www.attachment.com/mail/u/0",
        "some-domain.com"
      ],
      "observableFiles": [
        {
          "fileName": "Not_Suspicious.pdf",
          "fileSize": 0,
          "hashes": {}
        }
      ]
    },
    {
      "header": {
        "id": "CISCP:IB-02-23456",
        "title": "Phishing Emails Leads to Fraudulent Site",
        "description": "This attachment fraudulent Outlook Webmail Site intended to steal credentials.",
        "producedTime": "Jan 6, 2015 7:33:00 PM",
        "marking": {
          "level": "GREEN"
        }
      },
      "observableIpAddresses": [],
      "observableHostnames": [],
      "observableURIs": [],
      "observableFiles": [
        {
          "fileName": "Schedule.pdf",
          "fileSize": 121686,
          "hashes": {
            "SHA256": "f82fdae7d2bcbad9888c700a2703e681e74e57a59797d19e3604e4de71cf32c9",
            "SSDEEP": "SSDEEP-HASH-HERE",
            "SHA1": "b42802b3f664feb7e8d4a96fa65dc277d6490a4b",
            "MD5": "ca30122d21cae9239d9358b7ce0bf1c1"
          }
        }
      ]
    },
    {
      "header": {
        "id": "CISCP:IB-03-23456",
        "title": "Phishing Email, again",
        "description": "More phising attempts",
        "producedTime": "Jan 6, 2015 5:32:00 PM",
        "marking": {
          "level": "AMBER"
        }
      },
      "observableIpAddresses": [],
      "observableHostnames": [],
      "observableURIs": [
        "www.baddomain.com",
        "baddomain2.com"
      ],
      "observableFiles": [
        {
          "fileName": "OpenMe.rar",
          "fileSize": 1494376,
          "hashes": {
            "MD5": "2bdc8396eab8fdc6caa5b8e1747e55b4"
          }
        },
        {
          "fileName": "OpenMeToo.pdf",
          "fileSize": 1265816,
          "hashes": {
            "MD5": "457c64d693e9f24bad3cd5b5a3a91e6c"
          }
        },
        {
          "fileName": "RunMe.exe",
          "fileSize": 1215265,
          "hashes": {
            "MD5": "e10f870b62f7e17b8450ecbdd4e758b3"
          }
        },
        {
          "fileName": "expl0rer.File",
          "fileSize": 280336,
          "hashes": {
            "MD5": "191dcd9adea47bd4361c7ba8db21a806"
          }
        },
        {
          "fileName": "1.bat",
          "fileSize": 280336,
          "hashes": {
            "MD5": "6d84a2b7bc1b4836be81c20ad5f1a024"
          }
        }
      ]
    }
  ]

..



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

The STIX Java tool supports creating a STIX document from just file name and MD5 hash indicators, or from the :ref:`jsonSummary` section.

---------------------
From Only MD5 Hashes
---------------------

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

---------------------
From a JSON Summary
---------------------

To create a document, use the ``stix.author.json`` command.
You can specify an input file with the ``-i`` option, and an output file with the ``-o`` option.

The input file can either be a single JSON summary object, or a list of them.
The output will contain all the information from the input, formatted into a STIX document.

For example, if the input was the first object from the :ref:`jsonSummary` section, the output will be:

.. code-block:: xml

  <?xml version="1.0" encoding="UTF-8"?>
  <stix:STIX_Package id="CISCP:IB-01-23456"
      xmlns:FileObj="http://cybox.mitre.org/objects#FileObject-2"
      xmlns:URIObj="http://cybox.mitre.org/objects#URIObject-2"
      xmlns:cybox="http://cybox.mitre.org/cybox-2"
      xmlns:cyboxCommon="http://cybox.mitre.org/common-2"
      xmlns:indicator="http://stix.mitre.org/Indicator-2"
      xmlns:marking="http://data-marking.mitre.org/Marking-1"

      xmlns:stix="http://stix.mitre.org/stix-1"
      xmlns:stixCommon="http://stix.mitre.org/common-1" xmlns:tlpMarking="http://data-marking.mitre.org/extensions/MarkingStructure#TLP-1">
      <stix:STIX_Header>
          <stix:Title>Phishing Emails</stix:Title>
          <stix:Description>Emails were sent to try and steal credentials.</stix:Description>
          <stix:Handling>
              <marking:Marking>
                  <marking:Controlled_Structure>//node()</marking:Controlled_Structure>
                  <marking:Marking_Structure color="AMBER"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="tlpMarking:TLPMarkingStructureType"/>
              </marking:Marking>
          </stix:Handling>
          <stix:Information_Source>
              <stixCommon:Time>
                  <cyboxCommon:Produced_Time>2015-01-06T09:45:00.000-05:00</cyboxCommon:Produced_Time>
              </stixCommon:Time>
          </stix:Information_Source>
      </stix:STIX_Header>
      <stix:Observables cybox_major_version="2" cybox_minor_version="1">
          <cybox:Observable>
              <cybox:Object>
                  <cybox:Properties
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="FileObj:FileObjectType">
                      <FileObj:File_Name condition="Equals">Not_Suspicious.pdf</FileObj:File_Name>
                      <FileObj:Hashes/>
                  </cybox:Properties>
              </cybox:Object>
          </cybox:Observable>
      </stix:Observables>
      <stix:Indicators>
          <stix:Indicator
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="indicator:IndicatorType">
              <indicator:Observable>
                  <cybox:Object>
                      <cybox:Properties xsi:type="URIObj:URIObjectType">
                          <URIObj:Value>http://www.attachment.com/mail/u/0</URIObj:Value>
                      </cybox:Properties>
                  </cybox:Object>
              </indicator:Observable>
          </stix:Indicator>
          <stix:Indicator
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="indicator:IndicatorType">
              <indicator:Observable>
                  <cybox:Object>
                      <cybox:Properties xsi:type="URIObj:URIObjectType">
                          <URIObj:Value>http://www.attachment.com/mail/u/0</URIObj:Value>
                      </cybox:Properties>
                  </cybox:Object>
              </indicator:Observable>
          </stix:Indicator>
          <stix:Indicator
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="indicator:IndicatorType">
              <indicator:Observable>
                  <cybox:Object>
                      <cybox:Properties xsi:type="URIObj:URIObjectType">
                          <URIObj:Value>some-domain.com</URIObj:Value>
                      </cybox:Properties>
                  </cybox:Object>
              </indicator:Observable>
          </stix:Indicator>
      </stix:Indicators>
  </stix:STIX_Package>

..
