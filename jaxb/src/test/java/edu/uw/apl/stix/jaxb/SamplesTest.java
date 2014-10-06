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
package edu.uw.apl.stix.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

import org.apache.commons.io.FileUtils;

import org.mitre.stix.stix_1.STIXType;

/**
 * @author Stuart Maclean.
 *
 * Test driver for attempting ingest of sample STIX instance documents
 */
 
public class SamplesTest extends junit.framework.TestCase {

	protected void setUp() {
		docs = new ArrayList<File>();
		File dir = new File( "src/test/resources" );
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
	

	public void testCodecLoad() throws Exception {
		for( File f : docs ) {
			testCodecLoad( f );
		}
	}

	/**
	 * The 'test' is simply that the document can indeed be unmarshaled
	 * into a Java object via the JAXB bindings.
	 *
	 * Some Mitre sample docs are NOT actually valid STIX packages.
	 * We have already weeded out the 'Snippet' files, see above in
	 * setUp(). There remain some sample docs that are still not
	 * correct STIX packages.  For these, we expect
	 * ClassCastExceptions, since the outermost element in the sample
	 * IS some stix-related element, just NOT a STIX package.
	 * 
	 */
	private void testCodecLoad( File f ) throws Exception {
		System.out.println( f );
		try {
			STIXType t = Codec.unmarshal( f );
		} catch( ClassCastException e ) {
			System.err.println( e + " -> " + f );
		} catch( Exception e ) {
			System.err.println( e + " -> " + f );
			fail();
		}
	}

	private List<File> docs;
}

// eof
