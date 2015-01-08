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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.validation.Schema;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

//import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
//import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.mitre.stix.stix_1.ObjectFactory;
import org.mitre.stix.stix_1.STIXType;

/**
 * Encapsulate all the nasty JAXB-related logic for creating a STIX
 * package Java object from an instance document on disk.  Frees the user
 * from having to worry about JAXB context paths, unmarshallers etc.
 *
 * @author Stuart Maclean
 */

public class Codec {

	static public STIXType unmarshal( File stixDocument )
		throws IOException, JAXBException {

		Unmarshaller um = jc.createUnmarshaller();
		um.setEventHandler( new DefaultValidationEventHandler() );
		FileInputStream fis = null;
		try {
			fis = new FileInputStream( stixDocument );
			JAXBElement<STIXType> e = (JAXBElement<STIXType>)um.unmarshal
				( fis );
			return e.getValue();
		} finally {
			if( fis != null )
				fis.close();
		}
	}

	static public void marshal( STIXType package_, File f )
		throws IOException, JAXBException {
		FileOutputStream fos = new FileOutputStream( f );
		marshal( package_, fos );
		fos.close();
	}

	static public void marshal( STIXType package_, OutputStream os )
		throws IOException, JAXBException {
		Marshaller m = jc.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		/*
		  try {
			m.setProperty( "com.sun.xml.internal.bind.namespacePrefixMapper",
						   PREFIXMAPPER );
		} catch( PropertyException pe ) {
			// In case another JAXB implementation is used...
		}
		*/
		ObjectFactory of = new ObjectFactory();
		JAXBElement<STIXType> je = of.createSTIXPackage( package_ );
		m.marshal( je, os );
	}
	
	// derived this via 'find target/classes|grep package-info|sort'...
	static final String STIX111_CONTEXTPATH =
		"org.mitre.data_marking.marking_1:" +
		"org.mitre.stix.campaign_1:" +
		"org.mitre.stix.common_1:" +
		"org.mitre.stix.courseofaction_1:" +
		"org.mitre.stix.default_vocabularies_1:" +
		"org.mitre.stix.exploittarget_1:" +
		"org.mitre.stix.incident_1:" +
		"org.mitre.stix.indicator_2:" +
		"org.mitre.stix.stix_1:"+
		"org.mitre.stix.threatactor_1:"+
		"org.mitre.stix.ttp_1";

	static final String STIX111_EXTENSIONS_CONTEXTPATH =
		"org.mitre.stix.extensions.malware";

	static final String MAEC_CONTEXTPATH =
		"org.mitre.maec.default_vocabularies_1:" +
		"org.mitre.maec.xmlschema.maec_bundle_4:" +
		"org.mitre.maec.xmlschema.maec_package_2";

	static final String CYBOX21_CONTEXTPATH =
		"org.mitre.cybox.cybox_2:" +
		"org.mitre.cybox.common_2:" +
		"org.mitre.cybox.default_vocabularies_2:" +
		"org.mitre.cybox.objects";

	static final String CYBOX21_EXTENSIONS_CONTEXTPATH =
		"org.mitre.cybox.extensions.address:" +
		"org.mitre.cybox.extensions.platform:" +
		"org.mitre.cpe.language._2:" +
		"oasis.names.tc.ciq.xal._3";

	static final String CONTEXTPATH =
		STIX111_CONTEXTPATH + ":" +
		MAEC_CONTEXTPATH + ":" +
		CYBOX21_CONTEXTPATH;// + ":" +
		//		CYBOX21_EXTENSIONS_CONTEXTPATH;
	
	static JAXBContext jc;
	static {
		try {
			jc = JAXBContext.newInstance( CONTEXTPATH );
		} catch( JAXBException je ) {
			throw new ExceptionInInitializerError( je );
		}
	}

	// This is a tar pit of internal classes and general voodoo...
	/*
	static final NamespacePrefixMapper PREFIXMAPPER =
		new NamespacePrefixMapper() {
			@Override
			public String getPreferredPrefix( String namespaceURI,
											  String suggestion,
											  boolean requirePrefix ) {
				namespaceURI = namespaceURI.intern();
				if( false ) {
				} else if( namespaceURI == "http://stix.mitre.org/stix-1" ) {
					return "stix";
				}
				return suggestion;
			}
		};
	*/
}

// eof
