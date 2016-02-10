/**
 * Copyright © 2016, University of Washington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the name of the University of Washington nor the names
 *       of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written
 *       permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL UNIVERSITY OF
 * WASHINGTON BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.uw.apl.stix.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.mitre.cybox.common_2.AnyURIObjectPropertyType;
import org.mitre.cybox.common_2.DateTimeWithPrecisionType;
import org.mitre.cybox.common_2.StringObjectPropertyType;
import org.mitre.cybox.common_2.TimeType;
import org.mitre.cybox.cybox_2.Observable;
import org.mitre.cybox.cybox_2.Observables;
import org.mitre.cybox.objects.Address;
import org.mitre.cybox.objects.DomainName;
import org.mitre.cybox.objects.FileObjectType;
import org.mitre.cybox.objects.URIObjectType;
import org.mitre.data_marking.extensions.markingstructure.TLPColorEnum;
import org.mitre.data_marking.extensions.markingstructure.TLPMarkingStructureType;
import org.mitre.data_marking.marking_1.MarkingSpecificationType;
import org.mitre.data_marking.marking_1.MarkingType;
import org.mitre.stix.common_1.InformationSourceType;
import org.mitre.stix.common_1.StructuredTextType;
import org.mitre.stix.indicator_2.Indicator;
import org.mitre.stix.stix_1.IndicatorsType;
import org.mitre.stix.stix_1.STIXHeaderType;
import org.mitre.stix.stix_1.STIXPackage;

import edu.uw.apl.stix.objects.SimpleSTIXDocument;
import edu.uw.apl.stix.objects.SimpleSTIXHeader;

/**
 * Utility class for constructing STIX documents from {@link SimpleSTIXDocument} classes
 */
public class SimpleSTIXComposer {

    /**
     * Generate a {@link STIXPackage} from a {@link SimpleSTIXDocument}
     * @param document
     * @return
     */
    public static STIXPackage toSTIXPackage(SimpleSTIXDocument document){
        org.mitre.stix.stix_1.ObjectFactory stixObjectFactory = new org.mitre.stix.stix_1.ObjectFactory();
        org.mitre.cybox.cybox_2.ObjectFactory cyboxObjectFactory = new org.mitre.cybox.cybox_2.ObjectFactory();
        // Create the basic STIX info
        STIXPackage result = stixObjectFactory.createSTIXPackage();

        // Get the header
        STIXHeaderType header = getStixHeader(document);
        result.setSTIXHeader(header);
        // Set the ID
        if(document.getHeader().getId() != null){
            QName id = new QName(document.getHeader().getId());
            result.setId(id);
        }

        Observables ot = cyboxObjectFactory.createObservables();
        ot.setCyboxMajorVersion("2");
        ot.setCyboxMinorVersion("1");

        // Prepare the observable list
        List<Observable> observables = ot.getObservables();
        // Add all the file observables
        List<FileObjectType> fileObjectTypes = HashComposers.asFileObjectHashes(document.getObservableFiles());
        for(FileObjectType fileObjectType : fileObjectTypes){
            Observable observable = HashComposers.inObservable(fileObjectType);
            observables.add(observable);
        }
        // Add it to our final object
        result.setObservables(ot);

        // Prepare the IP indicators
        IndicatorsType indicators = new IndicatorsType();
        for(String ip : document.getObservableIpAddresses()){
            Address address = getAddressType(ip);
            Indicator indicator = new Indicator();
            indicator.setObservable(HashComposers.inObservable(address));
            // Add it to the list
            indicators.getIndicators().add(indicator);
        }
        // Hostname indicators
        for(String hostname : document.getObservableHostnames()){
            DomainName domain = getDomainType(hostname);
            Indicator indicator = new Indicator();
            indicator.setObservable(HashComposers.inObservable(domain));
            // Add it to the list
            indicators.getIndicators().add(indicator);
        }
        // URI indicators
        for(String uri : document.getObservableURIs()){
            URIObjectType uriObject = new URIObjectType();
            AnyURIObjectPropertyType value = new AnyURIObjectPropertyType();
            value.setValue(uri);
            uriObject.setValue(value);
            // Wrap it in an indicator
            Indicator indicator = new Indicator();
            indicator.setObservable(HashComposers.inObservable(uriObject));
            // Add it
            indicators.getIndicators().add(indicator);
        }

        result.setIndicators(indicators);

        return result;
    }

    /**
     * Get an {@link Address} object from an IP string
     * @param ip
     * @return
     */
    private static Address getAddressType(String ip){
        Address address = new Address();
        StringObjectPropertyType value = new StringObjectPropertyType();
        value.setValue(ip);
        address.setAddressValue(value);
        return address;
    }

    /**
     * Gte a {@link DomainName} object from a hostname string
     * @param hostname
     * @return
     */
    private static DomainName getDomainType(String hostname){
        DomainName domain = new DomainName();
        StringObjectPropertyType value = new StringObjectPropertyType();
        value.setValue(hostname);
        domain.setValue(value);
        return domain;
    }

    /**
     * Set the header of the STIXPackage from the SimpleSTIXDocument
     * @param document
     * @param dest
     */
    private static STIXHeaderType getStixHeader(SimpleSTIXDocument document){
        STIXHeaderType stixHeader = new STIXHeaderType();
        SimpleSTIXHeader header = document.getHeader();

        // Set the title
        if(header.getTitle() != null){
            stixHeader.setTitle(header.getTitle());
        }
        // Set the produced time
        if(header.getProducedTime() != null){
            InformationSourceType source = new InformationSourceType();
            TimeType time = new TimeType();
            DateTimeWithPrecisionType producedTime = new DateTimeWithPrecisionType();
            producedTime.setValue(dateToXML(header.getProducedTime()));
            time.setProducedTime(producedTime);
            source.setTime(time);
            stixHeader.setInformationSource(source);
        }
        // Set the description
        if(header.getDescription() != null){
            StructuredTextType description = new StructuredTextType();
            description.setValue(header.getDescription());
            stixHeader.getDescriptions().add(description);
        }
        // Set the TLP marking
        if(header.getMarking() != null){
            MarkingType marking = new MarkingType();
            MarkingSpecificationType markingType = new MarkingSpecificationType();
            markingType.setControlledStructure("//node()");
            TLPMarkingStructureType tlpMarkingStructure = new TLPMarkingStructureType();
            tlpMarkingStructure.setColor(TLPColorEnum.fromValue(header.getMarking().toString()));
            markingType.getMarkingStructures().add(tlpMarkingStructure);
            marking.withMarkings(markingType);
            // Set it in the header
            stixHeader.setHandling(marking);
        }

        // Set the header
        return stixHeader;
    }

    /**
     * Convert a Java date to the weird XML date format
     * @param date
     * @return
     */
    private static XMLGregorianCalendar dateToXML(Date date){
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
