/**
 * Copyright © 2016, University of Washington
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.mitre.stix.DocumentUtilities;
import org.mitre.stix.stix_1.STIXPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.cli.ParseException;

import edu.uw.apl.stix.objects.SimpleSTIXDocument;
import edu.uw.apl.stix.utils.SimpleSTIXComposer;

/**
 * Usage: SimpleSTIXComposer json
 *
 * Where json is a serialized list of {@link SimpleSTIXDocument} classes
 *
 * We then compose a STIX document with the supplied information.
 * Unless the -o option is specified, the resulting document(s) are written to stdout
 */
public class SimpleSTIXCreator {
    private File outputFile;
    private File inputFile;
    private Gson gson;

	static public void main( String[] args ) {
		SimpleSTIXCreator main = new SimpleSTIXCreator();
		try {
			main.readArgs( args );
			main.start();
		} catch( Exception e ) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	SimpleSTIXCreator() {
	    gson = new GsonBuilder().setPrettyPrinting().create();
	}

	private void readArgs( String[] args ) throws Exception {
		Options options = new Options();
		options.addOption("o", true, "Output file");
		options.addOption("i", true, "Input file");

		final String USAGE = SimpleSTIXCreator.class.getName() + " <JSON list of SimpleSTIXDocument objects>";
		final String HEADER = "If no input file is specified, input will be read from stdin";
		final String FOOTER = "";

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = null;
		try {
			commandLine = commandLineParser.parse( options, args );
		} catch( ParseException pe ) {
			printUsage( options, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
		args = commandLine.getArgs();

		// Check for the output file option
		if(commandLine.hasOption("o")){
		    outputFile = new File(commandLine.getOptionValue("o"));
		}
		// Check for input file
		if(commandLine.hasOption("i")){
		    inputFile = new File(commandLine.getOptionValue("i"));
		}

		if(commandLine.hasOption("h")) {
		    // If there are no pairs specified on the command line or any input file,
		    // print usage and exit
            printUsage( options, USAGE, HEADER, FOOTER );
            System.exit(0);
        }
	}

	static private void printUsage( Options os, String usage,
									String header, String footer ) {
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth( 80 );
		hf.printHelp( usage, header, os, footer );
	}

	private void start() throws Exception {
	    // Read the json
	    StringBuilder jsonBuilder = new StringBuilder();
	    BufferedReader reader = null;
	    if(inputFile == null){
	        //System.out.println("Reading from standard input");
	        // Read from standard in
	        reader = new BufferedReader(new InputStreamReader(System.in));
	    } else {
	        // Read from the input file
	        reader = new BufferedReader(new FileReader(inputFile));
	        //System.out.println("Reading from "+inputFile.getName());
	    }

	    String line = null;
	    while((line = reader.readLine()) != null){
	        jsonBuilder.append(line);
	    }

	    // Parse the document(s)
	    SimpleSTIXDocument[] documents = null;
	    try{
	        // Try and parse a list first
	        documents = gson.fromJson(jsonBuilder.toString(), SimpleSTIXDocument[].class);
	    } catch(JsonSyntaxException e){
	        // Fall  back to a single document
	        SimpleSTIXDocument doc = gson.fromJson(jsonBuilder.toString(), SimpleSTIXDocument.class);
	        documents = new SimpleSTIXDocument[1];
	        documents[0] = doc;
	    }

	    // Prep the list of docs
	    List<STIXPackage> stixDocs = new ArrayList<STIXPackage>(documents.length);
	    for(SimpleSTIXDocument doc : documents){
	        stixDocs.add(SimpleSTIXComposer.toSTIXPackage(doc));
	    }

        // For a single document, just use the toXmlString() method
        if (stixDocs.size() == 1) {
            writeOutput(stixDocs.get(0).toXMLString(true));
        } else {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElementNS("http://www.us-cert.gov/ciscp", "CISCP:STIX_Packages");
            doc.appendChild(root);
            // Write all the stix docs out
            for (STIXPackage stixDoc : stixDocs) {
                Document curStix = stixDoc.toDocument();
                root.appendChild(doc.importNode(curStix.getFirstChild(), true));
            }
            doc.normalize();

            String xmlString = DocumentUtilities.toXMLString(doc, true);
            writeOutput(xmlString);
        }
	}

	private void writeOutput(String output){
	    // Why do the generated STIXPackage XML strings use 'ns139'? weird
	    // TODO - This is a horrible hack
	    output = output.replaceAll("xmlns:ns139=\"http://xml/metadataSharing.xsd\"", "");
	    output = output.replaceAll("ns139", "stix");
        if(outputFile == null){
            System.out.println(output);
        } else {
            try {
                System.out.println("Writing to " + outputFile.getName());
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write(output);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.err.println("Exception writing to output file");
                e.printStackTrace();
            }
        }
	}
}
