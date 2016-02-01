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

package edu.uw.apl.stix.objects;

/**
 * Representation of a TLP marking level. <br>
 * See <a href="https://www.us-cert.gov/tlp">US-CERT</a> for full details TLP markings
 */
public class TLPMarking implements Comparable<TLPMarking> {
    private TLPMarkingEnum level;

    /**
     * Create a new TlpMarking class from a string TLP level. <br>
     * If the level is not a valid TLP string, it will throw an IllegalArgumentException
     * @param level
     */
    public TLPMarking(String level) throws IllegalArgumentException {
        if(isValidLevel(level)){
            this.level = TLPMarkingEnum.valueOf(level);
        } else {
            throw new IllegalArgumentException("Invlid TLP level "+level);
        }
    }

    /**
     * Check if a string is a valid TLP marking level
     * @param level
     * @return
     */
    public static boolean isValidLevel(String level){
        try{
            level = level.trim().toUpperCase();
            TLPMarkingEnum value = TLPMarkingEnum.fromValue(level);
            return value != null;
        } catch(Exception e){
            // Ignore any exceptions here. Makes it easy to handle null pointers
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TLPMarking o) {
        // Check if they are the same level
        if(this.level == o.level){
            return 0;
        }
        switch(this.level){
        case RED:
            // Red is always the higher level.
            return 1;
        case AMBER:
            // Amber is only lower than red
            if(o.level == TLPMarkingEnum.RED){
                return -1;
            } else {
                return 1;
            }
        case GREEN:
            // Green is only higher than white
            if(o.level == TLPMarkingEnum.WHITE){
                return 1;
            } else {
                return -1;
            }
        case WHITE:
            // White is always the lower level
            return -1;
        default:
            // This can never happen, but it makes Java happy
            return -1;
        }
    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o != null || !(o instanceof TLPMarking)){
            return false;
        }
        TLPMarking other = (TLPMarking) o;
        return level.equals(other.level);
    }

    @Override
    public String toString(){
        return level.value();
    }

    /**
     * Internal enum representation
     */
    private enum TLPMarkingEnum {
        RED, AMBER, GREEN, WHITE;

        public String value() {
            return name();
        }

        public static TLPMarkingEnum fromValue(String v) {
            return valueOf(v);
        }
    }
}
