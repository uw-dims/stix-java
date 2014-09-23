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
package edu.uw.apl.stix.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.cybox.cybox_2.ObjectType;
import org.mitre.cybox.common_2.ControlledVocabularyStringType;
import org.mitre.cybox.common_2.ObjectPropertiesType;
import org.mitre.cybox.common_2.HashListType;
import org.mitre.cybox.common_2.HashType;
import org.mitre.cybox.common_2.SimpleHashValueType;
import org.mitre.cybox.common_2.StringObjectPropertyType;
import org.mitre.cybox.cybox_2.ObservableType;
import org.mitre.cybox.cybox_2.ObservablesType;
import org.mitre.cybox.objects.FileObjectType;
import org.mitre.stix.stix_1.STIXType;

/**
 * @author Stuart Maclean
 *
 * All things concerning hash string extraction from STIX packages.
 *
 * Q: Are these any better than grep?  Probably.  Better than xpath, maybe.
 *
 */
public class HashExtractors {

	/**
	 * @param package_ - a STIX package to scan/parse
	 *
	 * @return a list of hexbinary strings containing all the md5 hashes
	 * extracted from Observable/FileObjectType in the supplied package
	 */
	static public List<String> extractMD5HexBinary( STIXType package_ ) {

		// All top-level observables, if any
		ObservablesType ot = package_.getObservables();
		if( ot == null )
			return Collections.EMPTY_LIST;
		List<String> result = new ArrayList<String>();
		List<ObservableType> ots = ot.getObservable();
		for( ObservableType el : ots ) {
			ObjectType obj = el.getObject();
			ObjectPropertiesType opt = obj.getProperties();
			// LOOK: any better way than instanceof, yuk!
			if( opt instanceof FileObjectType ) {
				FileObjectType fot = (FileObjectType)opt;
				result.addAll( extractMD5HexBinary( fot ) );
			}
		}
		return result;
	}

	static List<String> extractMD5HexBinary( FileObjectType fot ) {
		HashListType hlt = fot.getHashes();
		if( hlt == null )
			return Collections.EMPTY_LIST;
		List<String> result = new ArrayList<String>();
		List<HashType> hs = hlt.getHash();
		for( HashType h : hs ) {
			ControlledVocabularyStringType cvst = h.getType();
			Object cvstv = cvst.getValue();
			String cvstvS = (String)cvstv;
			if( cvstvS.equalsIgnoreCase( "MD5" ) ) {
				SimpleHashValueType shvt = h.getSimpleHashValue();
				String hash = (String)shvt.getValue();
				result.add( hash );
			}
		}
		return result;
	}
}

// eof
