/**
 * Copyright © 2014-2016, University of Washington
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
package edu.uw.apl.stix.cli;

import java.util.LinkedList;
import java.util.List;

import org.mitre.stix.stix_1.STIXPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.uw.apl.stix.objects.FileObjectObservable;
import edu.uw.apl.stix.utils.HashExtractors;

/**
 *
 * Usage: FileInfoExtractor stixFile
 *
 * Extract from a STIX file all md5 hashes.  These are likely subelements
 * of FileTypeObjects, themselves subelements of Observable and/or Indicator.
 *
 * Print the resulting hexbinary strings to stdout, one line at a time.
 */
public class FileInfoExtractor extends Extractor {

	public void start() throws Exception {
		List<STIXPackage> stixPackages = getStixPackages(inFile);
		List<FileObjectObservable> fileObjects = new LinkedList<FileObjectObservable>();
		for(STIXPackage stixPackage : stixPackages){
		    fileObjects.addAll(HashExtractors.getFileObservables(stixPackage));
		}

		// Print everything out as json
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(fileObjects));
	}

}