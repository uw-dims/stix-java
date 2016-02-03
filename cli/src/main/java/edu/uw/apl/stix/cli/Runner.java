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

/**
 * Class which decides which Extractor class to use
 */
public class Runner {

	/**
	 * Program entry point
	 * @param args
	 */
	public static void main(String[] args){
		if(args.length == 0){
			System.err.println("Missing type and fiel to extract");
			System.exit(-1);
		}
		
		// Get the correct extractor
		Extractor extractor = null;
		switch(args[0].toLowerCase()){
		case "md5":
			extractor = new MD5Extractor();
			break;
		case "ip":
			extractor = new IPExtractor();
			break;
		case "hostname":
			extractor = new HostnameExtractor();
			break;
		case "fileinfo":
		    extractor = new FileInfoExtractor();
			break;
		case "tlp":
		    extractor = new TLPExtractor();
		    break;
		case "json":
		    extractor = new JsonExtractor();
		    break;
		}
		
		// Make sure that the extractor is set
		if(extractor == null){
			System.err.println("Unknown type: "+args[0]);
			System.exit(-1);
		}
		
		// Run the extractor
		try{
			extractor.readArgs(args);
			extractor.start();
			extractor.close();
		} catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}
