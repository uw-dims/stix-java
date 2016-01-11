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
package edu.uw.apl.stix.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mitre.cybox.common_2.ControlledVocabularyStringType;
import org.mitre.cybox.common_2.HashListType;
import org.mitre.cybox.common_2.HashType;
import org.mitre.cybox.common_2.ObjectPropertiesType;
import org.mitre.cybox.common_2.SimpleHashValueType;
import org.mitre.cybox.cybox_2.ObjectType;
import org.mitre.cybox.cybox_2.Observable;
import org.mitre.cybox.cybox_2.Observables;
import org.mitre.cybox.objects.FileObjectType;
import org.mitre.stix.common_1.IndicatorBaseType;
import org.mitre.stix.indicator_2.Indicator;
import org.mitre.stix.stix_1.IndicatorsType;
import org.mitre.stix.stix_1.STIXPackage;

import edu.uw.apl.stix.objects.FileObjectObservable;

/**
 * @author Stuart Maclean
 *
 * All things concerning hash string extraction from STIX packages.
 *
 * Q: Are these any better than grep?  Probably.  Better than xpath, maybe.
 *
 */
public class HashExtractors {

    public static List<FileObjectObservable> getFileObservables(STIXPackage stixPackage){
        List<FileObjectObservable> observables = new LinkedList<FileObjectObservable>();

        // All top-level indicators, if any
        IndicatorsType indicators = stixPackage.getIndicators();
        if(indicators != null){
            for (IndicatorBaseType indicator : indicators.getIndicators()) {
                try {
                    Indicator ind = (Indicator) indicator;
                    Observable observable = ind.getObservable();

                    if (observable != null) {
                        FileObjectObservable fileObject = getFileObservable(observable);
                        if(fileObject != null){
                            observables.add(fileObject);
                        }
                    }
                } catch (Exception e) {
                    // Ignore. Probably a null pointer exception
                }
            }
        }

        // All top-level observables, if any
        Observables ot = stixPackage.getObservables();
        if( ot == null ){
            return observables;
        }

        List<Observable> observableList = ot.getObservables();
        for( Observable observable : observableList ) {
            FileObjectObservable fileObject = getFileObservable(observable);
            if(fileObject != null){
                observables.add(fileObject);
            }
        }

        return observables;
    }

	/**
	 * @param stixPackage - a STIX package to scan/parse
	 *
	 * @return a list of hexbinary strings containing all the md5 hashes
	 * extracted from Observable/FileObjectType in the supplied package
	 */
	static public List<String> extractMD5HexBinary( STIXPackage stixPackage ) {
	    List<String> result = new LinkedList<String>();

	    // All top-level indicators, if any
	    IndicatorsType indicators = stixPackage.getIndicators();
	    if(indicators != null){
            for (IndicatorBaseType indicator : indicators.getIndicators()) {
                try {
                    Indicator ind = (Indicator) indicator;
                    Observable observable = ind.getObservable();

                    if (observable != null) {
                        result.addAll(getHashesFromObservable(observable));
                    }
                } catch (Exception e) {
                    // Ignore. Probably a null pointer exception
                }
            }
	    }

        // All top-level observables, if any
		Observables ot = stixPackage.getObservables();
		if( ot == null ){
			return result;
		}

		List<Observable> observableList = ot.getObservables();
		for( Observable observable : observableList ) {
			result.addAll(getHashesFromObservable(observable));
		}
		return result;
	}

    private static FileObjectObservable getFileObservable(Observable observable){
        ObjectType obj = observable.getObject();
        ObjectPropertiesType opt = obj.getProperties();
        // LOOK: any better way than instanceof, yuk!
        if( opt instanceof FileObjectType ) {
            FileObjectType fileObject = (FileObjectType)opt;

            // Get the file name/size
            String fileName = null;
            long fileSize = 0;
            try{
                fileName = (String) fileObject.getFileName().getValue();
            } catch(Exception e){
                // Ignore
            }
            try{
                fileSize = Long.parseLong((String) fileObject.getSizeInBytes().getValue());
            } catch(Exception e){
                // Ignore
            }

            FileObjectObservable fileObservable = new FileObjectObservable(fileName, fileSize);
            // Get the hash information
            getHashHex(fileObject, fileObservable);

            return fileObservable;
        }

        return null;
    }

	private static List<String> getHashesFromObservable(Observable observable){
	    ObjectType obj = observable.getObject();
        ObjectPropertiesType opt = obj.getProperties();
        // LOOK: any better way than instanceof, yuk!
        if( opt instanceof FileObjectType ) {
            FileObjectType fot = (FileObjectType)opt;
            return extractMD5HexBinary( fot );
        }

        return Collections.emptyList();
	}

	private static List<String> extractMD5HexBinary( FileObjectType fot ) {
		HashListType hlt = fot.getHashes();
		if( hlt == null )
			return Collections.emptyList();
		List<HashType> hs = hlt.getHashes();
		List<String> result = new ArrayList<String>(hs.size());
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

	private static void getHashHex(FileObjectType fileObject, FileObjectObservable fileObservable){
	   HashListType hashListType = fileObject.getHashes();
	   if(hashListType == null){
	       return;
	   }
	   List<HashType> hashes = hashListType.getHashes();
	   for(HashType hash : hashes){
           ControlledVocabularyStringType cvst = hash.getType();
           String hashAlgorithm = (String) cvst.getValue();
           String hashValue = null;
           try{
               hashValue = (String) hash.getSimpleHashValue().getValue();
           } catch(Exception e){
               // Ignore
           }
           if(hashValue == null){
               try{
                   hashValue = (String) hash.getFuzzyHashValue().getValue();
               } catch(Exception e){
                   // Ignore
               }
           }

           if(hashAlgorithm != null && hashValue != null){
               fileObservable.addHash(hashAlgorithm, hashValue);
           }
	   }
	}
}
