package edu.uw.apl.stix.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.stix.stix_1.STIXPackage;



public class HashComposersTest extends junit.framework.TestCase {

	public void testNull() {
	}

	public void _testEmpty() throws Exception {
		List<String> empty = Collections.emptyList();
		STIXPackage s = HashComposers.composeMD5HashObservables( empty );
		// Codec.marshal( s, System.out );
	}

	public void test1() throws Exception {
		List<String> hashes = new ArrayList<String>();
		String hash1 = "12345678901234567890123456789012";
		hashes.add( hash1 );
		STIXPackage s = HashComposers.composeMD5HashObservables( hashes );
		// Codec.marshal( s, System.out );
	}

}

// eof
