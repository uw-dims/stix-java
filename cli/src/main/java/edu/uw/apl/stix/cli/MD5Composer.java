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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.ParseException;

import org.mitre.stix.stix_1.STIXType;

import edu.uw.apl.stix.jaxb.Codec;
import edu.uw.apl.stix.utils.HashComposers;

/**
 * @author Stuart Maclean
 *
 * Usage: MD5Composer hexHash1 hexHash2 ...
 *
 * where each hexHash cmd line arg is first checked to see if it name
 * a file, and if does not, is used literally.
 *
 * We then compose a STIX document with the supplied hashes being
 * entered as subelement of file-based Observables.  The document is composed
 * on stdout, or to a file if the -o option supplied.
 */
public class MD5Composer {
	
	static public void main( String[] args ) {
		MD5Composer main = new MD5Composer();
		try {
			main.readArgs( args );
			main.start();
		} catch( Exception e ) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	MD5Composer() {
		hashes = new ArrayList<String>();
	}
	
	private void readArgs( String[] args ) throws Exception {
		Options os = new Options();

		final String USAGE = MD5Composer.class.getName() + " hexHash+";
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
		if( args.length > 0 ) {
			for( String arg : args ) {
				hashes.add( arg );
			}
		} else {
			printUsage( os, USAGE, HEADER, FOOTER );
			System.exit(1);
		}
	}

	static private void printUsage( Options os, String usage,
									String header, String footer ) {
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth( 80 );
		hf.printHelp( usage, header, os, footer );
	}

	private void start() throws Exception {
		STIXType s = HashComposers.composeMD5HashObservables( hashes );
		Codec.marshal( s, System.out );
	}

	private List<String> hashes;
}

// eof
