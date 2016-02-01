/**
 * Copyright © 2014-2015, University of Washington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Washington nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNIVERSITY OF WASHINGTON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.uw.apl.stix.cli;

import java.io.File;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.mitre.stix.stix_1.STIXPackage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.uw.apl.stix.objects.TLPMarking;

/**
 * Abstract class all the Extractors will follow
 */
public abstract class Extractor {
    /**
     * The maximum TLP level command line option
     */
    public static final String MAX_TLP_OPTION = "maxTlp";
    /**
     * The minumum TLP level command line option
     */
    public static final String MIN_TLP_OPTION = "minTlp";

    protected static final String USAGE = "(Options) inputFile";
    protected static final String HEADER = "";
    protected static final String FOOTER = "";

	protected File inFile;
	protected Options options = new Options();
	protected TLPMarking maxTlpLevel;
	protected TLPMarking minTlpLevel;
	
	/**
	 * Read and parse the command line arguments
	 * @param args
	 * @throws Exception
	 */
	public void readArgs( String[] args ) throws Exception {
	    options.addOption(MAX_TLP_OPTION, true, "Maximum TLP marking color");
	    options.addOption(MIN_TLP_OPTION, true, "Minimum TLP marking color");
		
		CommandLineParser clp = new PosixParser();
		CommandLine cl = null;
		try {
			cl = clp.parse( options, args );
		} catch( ParseException pe ) {
			printUsage( options, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
		if(cl.hasOption(MAX_TLP_OPTION)){
		    String tlpLevel = cl.getOptionValue(MAX_TLP_OPTION);
		    checkTlpLevel(tlpLevel);
		    maxTlpLevel = new TLPMarking(tlpLevel);
		}
        if(cl.hasOption(MIN_TLP_OPTION)){
            String tlpLevel = cl.getOptionValue(MIN_TLP_OPTION);
            checkTlpLevel(tlpLevel);
            minTlpLevel = new TLPMarking(tlpLevel);
        }
		args = cl.getArgs();
		if( args.length >= 2 ) {
			inFile = new File( args[1] );
			if( !inFile.isFile() ) {
				// like bash would do, write to stderr...
				System.err.println("Error: " + inFile + ": No such file or directory" );
				System.exit(-1);
			}
		} else {
		    System.err.println("Error: No input file specified");
			printUsage( options, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
	}

	/**
	 * Check if the provided value is a valid TLP marking level.
	 * If it is not, exit with an error code
	 * @param level
	 */
	protected void checkTlpLevel(String level){
        if(!TLPMarking.isValidLevel(level)){
            System.err.println("Error: Invalid TLP marking: "+level);
            System.exit(-1);
        }
	}
	
	/**
	 * Get the STIXPackage from parsing the XML input file
	 * @return
	 * @throws Exception
	 */
	public static List<STIXPackage> getStixPackages(File inFile) throws Exception {
	    List<STIXPackage> packages = new ArrayList<STIXPackage>();

	    // Parse the XML
	    // http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = builderFactory.newDocumentBuilder();
	    Document doc = builder.parse(inFile);

	    // Maybe this isn't needed?
	    // See http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    doc.getDocumentElement().normalize();

	    /*
	     *  Redirect stdout to stderr for this method
	     *  The STIX java library prints lots of warnings to stdout,
	     *  which pollute our output.
	     *  Redirect everything to stderr during processing
	     */
	    final PrintStream originalOut = System.out;
	    System.setOut(System.err);

	    // Check if the input file is a collection of STIX documents, or just a single one
	    if("CISCP:STIX_Packages".equalsIgnoreCase(doc.getDocumentElement().getNodeName())){
	        /*
	         *  Pull the attributes out.
	         *  We will add the attributes from the root to the beginning of the STIX
	         *  sub-documents, so that they are valid XML on their own
	         */
	        NamedNodeMap attributes = doc.getDocumentElement().getAttributes();

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();

	        NodeList children = doc.getDocumentElement().getChildNodes();
	        for(int i=0; i < children.getLength(); i++){
	            Node node = children.item(i);
	            // Make sure we only deal with full elements under the root
	            if(node.getNodeType() != Node.ELEMENT_NODE){
	                continue;
	            }

	            Element element = (Element) node;

	            // Add the root's attributes to this element
	            // This makes sure that namespaces and such are always defined
	            for(int j =0; j < attributes.getLength(); j++){
	                Attr attribute = (Attr) attributes.item(j);
	                element.setAttribute(attribute.getName(), attribute.getValue());
	            }

	            // Prepare to output the element as a full XML string
	            StringWriter stringWriter = new StringWriter();
	            Transformer transformer = transformerFactory.newTransformer();
	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.transform(new DOMSource(element), new StreamResult(stringWriter));

	            // Take the XML string, and let the STIX library parse it
	            String xmlString = stringWriter.toString();
	            STIXPackage stixPackage = STIXPackage.fromXMLString(xmlString);
	            packages.add(stixPackage);
	        }
	    } else {
	        // We should be dealing with a single STIX document as the root
	        String text = FileUtils.readFileToString(inFile);
	        packages.add(STIXPackage.fromXMLString(text));
	    }

	    // Set stdout back to its original value
	    System.setOut(originalOut);
	    return packages;
	}
	
	/**
	 * Start extracting the data
	 * @throws Exception
	 */
	public abstract void start() throws Exception;
	
	/**
	 * Print the usage of the class to the console
	 * @param os
	 * @param usage
	 * @param header
	 * @param footer
	 */
	static protected void printUsage(Options os, String usage, String header, String footer) {
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth(80);
		hf.printHelp(usage, header, os, footer);
	}
}
