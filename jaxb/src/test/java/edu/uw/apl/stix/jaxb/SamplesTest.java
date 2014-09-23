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
		instances = new HashMap<Class,Integer>();

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
			System.out.println( f );
			try {
				STIXType t = Codec.unmarshal( f );
			} catch( Exception e ) {
				System.err.println( e + " -> " + f );
			}
		}
	}

	List<File> docs;
	Map<Class,Integer> instances;
}

// eof
