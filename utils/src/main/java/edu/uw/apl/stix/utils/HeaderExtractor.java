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
import java.util.List;

import org.mitre.data_marking.extensions.markingstructure.TLPMarkingStructureType;
import org.mitre.data_marking.marking_1.MarkingSpecificationType;
import org.mitre.data_marking.marking_1.MarkingStructureType;
import org.mitre.stix.common_1.StructuredTextType;
import org.mitre.stix.stix_1.STIXHeaderType;
import org.mitre.stix.stix_1.STIXPackage;

import edu.uw.apl.stix.objects.SimpleSTIXHeader;
import edu.uw.apl.stix.objects.TLPMarking;

/**
 * Get details from the headers of a STIX document
 */
public class HeaderExtractor {

    /**
     * Get a {@link SimpleSTIXHeader} object with as much information populated as possible
     * @param stixPackage
     * @return
     */
    public static SimpleSTIXHeader getHeader(STIXPackage stixPackage){
        SimpleSTIXHeader simpleHeader = new SimpleSTIXHeader();
        try{
            STIXHeaderType header = stixPackage.getSTIXHeader();
            // Get the title
            simpleHeader.setTitle(header.getTitle());

            // Get the description
            List<StructuredTextType> descriptions = header.getDescriptions();
            String description = "";
            for(StructuredTextType currentDescription : descriptions){
                description += currentDescription.getValue();
            }
            simpleHeader.setDescription(description);

            // Get the ID
            try{
                String id = "";
                String prefix = stixPackage.getId().getPrefix();
                if(prefix != null){
                    id = prefix + ":";
                }
                id += stixPackage.getId().getLocalPart();

                simpleHeader.setId(id);
            } catch(Exception e){
                // Ignore. Probably NPE
            }

            // Get the TLP Marking
            String tlpMarking = getDocumentTLPMarking(stixPackage);
            if(tlpMarking != null){
                simpleHeader.setMarking(new TLPMarking(tlpMarking));
            }

            // Get the produced time
            try{
                Date producedTime = null;
                // Massive call
                producedTime = header.getInformationSource().getTime().getProducedTime()
                        .getValue().toGregorianCalendar().getTime();

                simpleHeader.setProducedTime(producedTime);
            } catch(Exception e){
                // Ignore. There are just too many calls to check everything
            }
        } catch(Exception e){
            // Ignore. Probably a null pointer exception
            System.err.println("Error getting document header information, continuing.");
        }

        return simpleHeader;
    }

    /**
     * Get the TLP marking for the overall document
     * @param stixPackage
     * @return
     */
    public static String getDocumentTLPMarking(STIXPackage stixPackage) {
        try {
            List<MarkingSpecificationType> markings = stixPackage.getSTIXHeader().getHandling().getMarkings();

            for (MarkingSpecificationType marking : markings) {
                List<MarkingStructureType> structures = marking.getMarkingStructures();
                for (MarkingStructureType structure : structures) {
                    if (structure instanceof TLPMarkingStructureType) {
                        TLPMarkingStructureType tlpMarking = (TLPMarkingStructureType) structure;
                        return tlpMarking.getColor().toString();
                    }
                }
            }
        } catch (Exception e) {
            // Ignore. Probably a null pointer exception getting the
            // header/markings
            System.err.println("Error getting document TLP marking, returning null");
        }

        return null;
    }

}
