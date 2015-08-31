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
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.mitre.stix.stix_1.STIXPackage;

/**
 * Abstract class all the Extractors will follow
 */
public abstract class Extractor {
	protected File inFile;
	
	/**
	 * Read and parse the command line arguments
	 * @param args
	 * @throws Exception
	 */
	public void readArgs( String[] args ) throws Exception {
		Options os = new Options();

		final String USAGE = "inFile";
		final String HEADER = "";
		final String FOOTER = "";
		
		CommandLineParser clp = new PosixParser();
		CommandLine cl = null;
		try {
			cl = clp.parse( os, args );
		} catch( ParseException pe ) {
			printUsage( os, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
		args = cl.getArgs();
		if( args.length >= 2 ) {
			inFile = new File( args[1] );
			if( !inFile.isFile() ) {
				// like bash would do, write to stderr...
				System.err.println( inFile + ": No such file or directory" );
				System.exit(-1);
			}
		} else {
			printUsage( os, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
	}
	
	/**
	 * Get the STIXPackage from parsing the XML input file
	 * @return
	 * @throws IOException
	 */
	protected STIXPackage getStixPackage() throws IOException{
		String text = FileUtils.readFileToString(inFile);
		return STIXPackage.fromXMLString(text);
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
