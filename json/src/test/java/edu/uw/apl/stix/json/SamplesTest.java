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
package edu.uw.apl.stix.json;

import java.io.File;
import java.util.*;

import org.mitre.stix.stix_1.ObjectFactory;
import org.mitre.stix.stix_1.STIXType;

import edu.uw.apl.stix.jaxb.Codec;

import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test cases for converting STIX package objects to and from JSON
 * strings.  The STIX package objects are created by loading from
 * sample STIX documents in our 'sample set', as distributed by Mitre.
 *
 * {@link https://stix.mitre.org/language/version1.1.1/samples/stix1.1.1-samples-all.zip}
 *
 * Note: Given the complexities of the various sample documents, there
 * is NO guarantee that these objects (which conform to the STIX
 * schema) can be represented/exchanged in json format.  They can of
 * course be represented/exchanged in xml form.  This 'json-ification'
 * of STIX obects was a purely academic exercise, essentially to
 * convince myself that the json would <em>not</em> work. Laugh!
 */

public class SamplesTest {
	
	@BeforeClass
	static public void locateSamples() {
		docs = new ArrayList<File>();
		File dir = new File( "../jaxb/src/test/resources" );
		if( dir.isDirectory() ) {
			Collection<File> fs = FileUtils.listFiles
				( dir, new String[] { "xml" }, true );
			for( File f : fs ) {
				if( f.getName().contains( "Snippet" ) )
					continue;
				docs.add( f );
			}
		}
		System.out.println( "Sample Documents: " + docs.size() );
	}

	@Test
	public void testSamples() {
		for( File d : docs ) {
			testSample( d );
			// Do as many as we need ??
			break;
		}
	}

	private void testSample( File f ) {
		System.out.println( f );
		try {
			// Load a STIX document into a Java object
			STIXType t = Codec.unmarshal( f );

			// See how that object looks as xml again, on stdout
			Codec.marshal( t, System.out );
			
			// Convert the object to JSON and see how that looks
			boolean prettyPrint = true;
			String s = JSONCodec.toJSON( t, prettyPrint );
			System.out.println( s );

			// Can we rebuild another object from the JSON? Likely not ;)
			STIXType t2 = JSONCodec.fromJSON( s );
		} catch( Exception e ) {
			System.err.println( e );
		}
	}
	
	static private List<File> docs;
}

// eof

