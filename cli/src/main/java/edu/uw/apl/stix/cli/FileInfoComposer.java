/**
 * Copyright © 2014, University of Washington
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

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.mitre.stix.stix_1.STIXPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.cli.ParseException;

import edu.uw.apl.stix.objects.FileObjectObservable;
import edu.uw.apl.stix.utils.HashComposers;

/**
 * Usage: FileInfoComposer json
 *
 * Where json is a serialized list of {@link FileObjectObservable} classes
 *
 * We then compose a STIX document with the supplied file observables being
 * entered as subelement of file-based Observables.  The document is composed
 * on stdout, or to a file if the -o option supplied.
 */
public class FileInfoComposer {
    private File outputFile;
    private File inputFile;
    private Gson gson;
	
	static public void main( String[] args ) {
		FileInfoComposer main = new FileInfoComposer();
		try {
			main.readArgs( args );
			main.start();
		} catch( Exception e ) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	FileInfoComposer() {
	    gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	private void readArgs( String[] args ) throws Exception {
		Options options = new Options();
		options.addOption("o", true, "Output file");
		options.addOption("i", true, "Input file");

		final String USAGE = FileInfoComposer.class.getName() + " <JSON list of FileObjectObservable objects>";
		final String HEADER = "\nTODO. Data fread from STDIN unless the -i option is used.";
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
	        System.out.println("Reading from standard input");
	        // Read from standard in
	        reader = new BufferedReader(new InputStreamReader(System.in));
	    } else {
	        // Read from the input file
	        reader = new BufferedReader(new FileReader(inputFile));
	        System.out.println("Reading from "+inputFile.getName());
	    }
	    
	    String line = null;
	    while((line = reader.readLine()) != null){
	        jsonBuilder.append(line);
	    }
	    
	    FileObjectObservable[] observables = gson.fromJson(jsonBuilder.toString(), FileObjectObservable[].class);
	    List<FileObjectObservable> observableList = new ArrayList<FileObjectObservable>(observables.length);
	    for(FileObjectObservable fileObj : observables){
	        observableList.add(fileObj);
	    }
	    
		STIXPackage s = HashComposers.composeFileObjectObservables(observableList);
		String xmlString = s.toXMLString(true);
		if(outputFile == null){
		    System.out.println(xmlString);
		} else {
            try {
                System.out.println("Writing to " + outputFile.getName());
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write(xmlString);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.err.println("Exception writing to output file");
                e.printStackTrace();
            }
		}
	}
}
