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

import java.util.List;

import org.mitre.data_marking.extensions.markingstructure.TLPMarkingStructureType;
import org.mitre.data_marking.marking_1.MarkingSpecificationType;
import org.mitre.data_marking.marking_1.MarkingStructureType;
import org.mitre.stix.stix_1.STIXPackage;

/**
 * Get details about any TLP marking within a STIX Document
 */
public class TLPMarkingExtractor {

    public static String getHighestTLPMarking(STIXPackage stixPackage){
        List<MarkingSpecificationType> markings = stixPackage.getSTIXHeader().getHandling().getMarkings();

        for(MarkingSpecificationType marking : markings){
            List<MarkingStructureType> structures = marking.getMarkingStructures();
            for(MarkingStructureType structure : structures){
                if(structure instanceof TLPMarkingStructureType){
                    TLPMarkingStructureType tlpMarking = (TLPMarkingStructureType) structure;
                    return tlpMarking.getColor().toString();
                }
            }
        }

        return null;
    }

}
