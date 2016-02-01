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

import junit.framework.TestCase;

/**
 * Test verifying TLP colors
 */
public class TLPMarkingTest extends TestCase {
    private static final String[] VALID_COLORS = {"green", "GREEN", "greEN",
            "RED", "red", "AMBER", "amBER", "white", "WHITE" };

    private static final String[] INVALID_COLORS = {"blue", "BLUe", "magenta", null };

    public void testValidTLPColors() throws Exception {
        for(String color : VALID_COLORS){
            System.out.println("Testing valid color "+color);
            assertEquals(TLPMarkingExtractor.isvalidTLPMarking(color), true);
        }
    }

    public void testInvalidTLPColors() throws Exception {
        for(String color : INVALID_COLORS){
            System.out.println("Testing invalid color "+color);
            assertEquals(TLPMarkingExtractor.isvalidTLPMarking(color), false);
        }
    }
}