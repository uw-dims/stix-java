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

import java.util.LinkedList;
import java.util.List;

import org.mitre.cybox.common_2.ObjectPropertiesType;
import org.mitre.cybox.cybox_2.ObjectType;
import org.mitre.cybox.cybox_2.Observable;
import org.mitre.cybox.cybox_2.Observables;
import org.mitre.cybox.objects.DomainName;
import org.mitre.cybox.objects.URIObjectType;
import org.mitre.stix.common_1.IndicatorBaseType;
import org.mitre.stix.indicator_2.Indicator;
import org.mitre.stix.stix_1.IndicatorsType;
import org.mitre.stix.stix_1.STIXPackage;

/**
 * All things concerning host name extraction from STIX packages.
 *
 * Q: Are these any better than grep?  Probably.  Better than xpath, maybe.
 *
 */
public class HostnameExtractors {

    /**
     * @param stixPackage - a STIX package to scan/parse
     *
     * @return a list containing all the URIs
     * extracted from Observable/FileObjectType in the supplied package
     */
    static public List<String> extractURIs( STIXPackage stixPackage ) {
        List<String> result = new LinkedList<String>();

        // All top-level indicators, if any
        IndicatorsType indicators = stixPackage.getIndicators();
        if (indicators != null) {
            for (IndicatorBaseType indicator : indicators.getIndicators()) {
                try {
                    Indicator ind = (Indicator) indicator;
                    Observable observable = ind.getObservable();
                    if (observable != null) {
                        String hostname = getUriFromObservable(observable);
                        if (hostname != null) {
                            result.add(hostname);
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
            return result;
        }

        List<Observable> ots = ot.getObservables();
        for( Observable el : ots ) {
            String hostname = getUriFromObservable(el);
            if(hostname != null){
                result.add(hostname);
            }
        }
        return result;
    }

	/**
	 * @param stixPackage - a STIX package to scan/parse
	 *
	 * @return a list containing all the host names
	 * extracted from Observable/FileObjectType in the supplied package
	 */
	static public List<String> extractHostnames( STIXPackage stixPackage ) {
	    List<String> result = new LinkedList<String>();

	    // All top-level indicators, if any
	    IndicatorsType indicators = stixPackage.getIndicators();
        if (indicators != null) {
            for (IndicatorBaseType indicator : indicators.getIndicators()) {
                try {
                    Indicator ind = (Indicator) indicator;
                    Observable observable = ind.getObservable();
                    if (observable != null) {
                        String hostname = getHostnameFromObservable(observable);
                        if (hostname != null) {
                            result.add(hostname);
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
			return result;
		}
		
		List<Observable> ots = ot.getObservables();
		for( Observable el : ots ) {
		    String hostname = getHostnameFromObservable(el);
		    if(hostname != null){
		        result.add(hostname);
		    }
		}
		return result;
	}

	/**
	 * Get a URI from an observable, if it exists
	 * @param observable
	 * @return
	 */
    private static String getUriFromObservable(Observable observable){
        ObjectType obj = observable.getObject();
        ObjectPropertiesType opt = obj.getProperties();
        // LOOK: any better way than instanceof, yuk!
        if( opt instanceof URIObjectType){
            URIObjectType uriType = (URIObjectType)opt;
            return uriType.getValue().getValue().toString();
        }
        return null;
    }

    /**
     * Get a host name from an observable, if it exists
     * @param observable
     * @return
     */
	private static String getHostnameFromObservable(Observable observable){
        ObjectType obj = observable.getObject();
        ObjectPropertiesType opt = obj.getProperties();
        // LOOK: any better way than instanceof, yuk!
        if( opt instanceof DomainName ) {
            DomainName fot = (DomainName)opt;
            return fot.getValue().getValue().toString();
        }
        return null;
	}
}
