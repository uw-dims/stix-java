.. _building:

=================================
Building
=================================


**************
Prerequisites
**************

STIX Java is Java code, organized to build using Maven.  It is a
'multi-module' Maven codebase.  Being Java and using Maven, there are
two very obvious tool prerequisites:

#. A 1.7+ version of the Java Development Kit (JDK).

   For installation on Ubuntu, install the OpenJDK toolset.

   .. code-block:: none

       $ sudo apt-get install openjdk-7-jdk

   ..


   Alternatively, you may prefer the Sun/Oracle toolset, but that takes more
   work to install. See `Oracle's website`_.

#. Apache Maven 3.0.5 or greater.

   See install instructions on the `Maven website`_.  For quick install on Ubuntu:

   .. code-block:: none

       $ sudo apt-get install maven

   ..

After installation of both tools, run ``mvn -v`` which shows Maven's
version and which JDK it has located.  You are hoping to see something
very close to this:

.. code-block:: none

    $ mvn -v

    Apache Maven 3.0.5
    Maven home: /usr/share/maven
    Java version: 1.7.0_79, vendor: Oracle Corporation
    Java home: /usr/lib/jvm/java-7-openjdk-amd64/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "3.19.0-30-generic", arch: "amd64", family: "unix"

..


*************
Dependencies
*************

STIX Java dependencies are all available via Maven central and will be fetched automatically.

*********************
Building & Installing
*********************

You can compile and package up all the Java code into what Maven calls
'artifacts', which are just carefully named Java jar files, using:

.. code-block:: none

    $ cd /path/to/stix-java-git-repo
    $ mvn package

..

Alternatively, you can which uses the local ``Makefile`` to invoke Maven, then
take the jars and copy them to ``/opt/dims/jars``, and copy driver shell
scripts from ``./bin`` to ``/opt/dims/bin``, as follows:

.. code-block:: none

    $ make package
    $ make install

..

.. _native-code:

**********
Unit tests
**********

The above compile/package/install process also runs the unit tests.
If you want to manually run the tests, run `mvn test`


.. _Oracle's website: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
.. _Maven website: http://maven.apache.org/download.cgi
