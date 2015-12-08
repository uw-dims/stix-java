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
import java.util.List;

import org.mitre.cybox.cybox_2.ObjectType;
import org.mitre.cybox.cybox_2.Observable;
import org.mitre.cybox.cybox_2.Observables;
import org.mitre.cybox.default_vocabularies_2.HashNameVocab10;
import org.mitre.cybox.common_2.ObjectPropertiesType;
import org.mitre.cybox.common_2.ConditionTypeEnum;
import org.mitre.cybox.common_2.HashListType;
import org.mitre.cybox.common_2.HashType;
import org.mitre.cybox.common_2.SimpleHashValueType;
import org.mitre.cybox.common_2.StringObjectPropertyType;
import org.mitre.cybox.objects.FileObjectType;
import org.mitre.stix.stix_1.STIXPackage;

/**
 * @author Stuart Maclean
 *
 * All things concerning hash string authoring in STIX packages, used
 * to e.g. build Observables/Indicators representing md5,shaX digests
 * of files of interest.
 */
public class HashComposers {

	/**
	 *
	 * @param hashes a list of md5 hashes, in hexBinary, to be inserted into
	 * a STIX package. Looking at the insertion as an XPath problem, we will
	 * be creating SimpleHashValueTypes according to this path
	 *
	 * "/stix:STIX_Package/stix:Observables/cybox:Observable/cybox:Object/cybox:Properties/FileObj:Hashes/cyboxCommon:Hash/cyboxCommon:Simple_Hash_Value"
	 *
	 * @return - a new STIX package containing the supplied hashes as
	 * file Observables.  The instance document will look something like
	 *
	 * <pre>
	 * {@code
	    <ns114:Observables cybox_major_version="2" cybox_minor_version="1">
        <ns7:Observable>
            <ns7:Object>
                <ns7:Properties xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns31:FileObjectType">
                    <ns31:Hashes>
                        <ns3:Hash>
                            <ns3:Type xmlns:ns124="http://cybox.mitre.org/default_vocabularies-2" xsi:type="ns124:HashNameVocab-1.0">md5</ns3:Type>
                            <ns3:Simple_Hash_Value>12345678901234567890123456789012</ns3:Simple_Hash_Value>
                        </ns3:Hash>
                    </ns31:Hashes>
                </ns7:Properties>
            </ns7:Object>
        </ns7:Observable>
    </ns114:Observables>

	 * }
	 * </pre>
	 */
	static public STIXPackage composeMD5HashObservables(List<String> fileNames, List<String> hashes) {

		org.mitre.stix.stix_1.ObjectFactory of =
			new org.mitre.stix.stix_1.ObjectFactory();
		STIXPackage result = of.createSTIXPackage();
		
		org.mitre.cybox.cybox_2.ObjectFactory of2 =
			new org.mitre.cybox.cybox_2.ObjectFactory();
		Observables ot = of2.createObservables();
		ot.setCyboxMajorVersion( "2" );
		ot.setCyboxMinorVersion( "1" );
		
		result.setObservables( ot );
		
		List<Observable> observables = ot.getObservables();
		addMD5HashObservables(fileNames, hashes, observables );
		return result;
	}

	static public void addMD5HashObservables( List<String> fileNames, List<String> hashes,
											  List<Observable> observables) {
		List<FileObjectType> fos = asFileObjectHashes(fileNames, hashes);
		for( FileObjectType fo : fos ) {
			Observable obs = inObservable( fo );
			observables.add( obs );
		}		
	}

	static Observable inObservable( ObjectPropertiesType op ) {
		org.mitre.cybox.cybox_2.ObjectFactory of =
			new org.mitre.cybox.cybox_2.ObjectFactory();
		ObjectType obj = of.createObjectType();
		obj.setProperties( op );
		Observable obs = of.createObservable();
		obs.setObject( obj );
		return obs;
		
	}	
		
		
	static public List<FileObjectType> asFileObjectHashes(List<String> fileNames, List<String> hashes ) {
		List<FileObjectType> result = new ArrayList<FileObjectType>();
		for(int i =0; i < hashes.size(); i++){
		    String hash = hashes.get(i);
		    String fileName = fileNames.get(i);
			FileObjectType fo = asFileObjectHash(fileName, hash, "MD5");
			result.add( fo );
		}
		return result;
	}

	static public FileObjectType asFileObjectHash(String fileName, String hash,
												   String algorithm ) {
		org.mitre.cybox.common_2.ObjectFactory of =
			new org.mitre.cybox.common_2.ObjectFactory();
		SimpleHashValueType shvt = of.createSimpleHashValueType();
		shvt.setValue( hash );
		HashType ht = of.createHashType();
		ht.setSimpleHashValue( shvt );

		org.mitre.cybox.default_vocabularies_2.ObjectFactory of2 =
			new org.mitre.cybox.default_vocabularies_2.ObjectFactory();
		HashNameVocab10 hnv = of2.createHashNameVocab10();
		hnv.setValue( algorithm );
		ht.setType( hnv );

		HashListType hlt = of.createHashListType();
		List<HashType> hts = hlt.getHashes();
		hts.add( ht );
		
		org.mitre.cybox.objects.ObjectFactory of3 =
			new org.mitre.cybox.objects.ObjectFactory();
		FileObjectType result = of3.createFileObjectType();

		// Only add the file name if it exists
        if (fileName != null && !"".equals(fileName.trim())) {
            StringObjectPropertyType fileNameProperty = new StringObjectPropertyType();
            fileNameProperty.setValue(fileName.trim());
            fileNameProperty.setCondition(ConditionTypeEnum.EQUALS);
            result.setFileName(fileNameProperty);
        }

		result.setHashes( hlt );
		return result;
	}
}


// eof
