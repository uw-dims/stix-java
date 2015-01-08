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
package edu.uw.apl.stix.json;

import org.mitre.stix.stix_1.STIXType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Simple coder/coder for converting STIX package objects to and from
 * JSON string representations. Uses Google's nice gson library for
 * the heavy lifting.
 *
 * {@link https://code.google.com/p/google-gson/}
 */
public class JSONCodec {

	/**
	 * Convenience routine for 2-arg version.
	 *
	 * @see #toJSON( STIXType, boolean )
	 */
	static public String toJSON( STIXType t ) {
		return toJSON( t, false );
	}

	/**
	 * Convert the supplied stix package object to a JSON string, optionally
	 * pretty printing the result
	 */
	static public String toJSON( STIXType package_, boolean prettyPrint ) {
		Gson gs = createGson( prettyPrint );
		String result = gs.toJson( package_ );
		return result;
	}

	static public STIXType fromJSON( String s ) {
		Gson gs = createGson( false );
		STIXType result = gs.fromJson( s, STIXType.class );
		return result;
	}


	/**
	 * Helper function, generates a Gson object, with optional prettyPrinting
	 * This is where to edit should you want/need the json strings to
	 * look different.
	 */
	static private Gson createGson( boolean withPrettyPrinting ) {
		GsonBuilder gb = new GsonBuilder();
		if( withPrettyPrinting )
			gb.setPrettyPrinting();
		gb.serializeNulls();
		gb.disableHtmlEscaping();
		return gb.create();
	}
}

// eof
