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

import java.util.HashMap;
import java.util.Map;

/**
 * A class for representing a cybox FileObjectType Observable within
 * a STIX document.
 * This will be used for extracting and writing STIX documents.
 */
public class FileObjectObservable {
    private String fileName;
    private long fileSize;
    private Map<String, String> hashes;

    /**
     * Create an empty FileObjectObservable
     */
    public FileObjectObservable(){
        hashes = new HashMap<String, String>();
    }

    /**
     * Create a FileObjectObservable with the provided file name
     * @param fileName
     */
    public FileObjectObservable(String fileName){
        this();
        this.fileName = fileName;
    }

    /**
     * Create a FileObjectObservable with the provided file name and size
     * @param fileName
     * @param fileSize
     */
    public FileObjectObservable(String fileName, long fileSize){
        this(fileName);
        this.fileSize = fileSize;
    }

    /**
     * Checks if this FileObjectObservable has any useful information.
     * Useful information consists of a file name or hashes.
     * <br />
     * If the file name is null and there are no hashes, this will return false
     * @return
     */
    public boolean hasInformation(){
        return !(fileName == null && hashes.isEmpty());
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the hashes
     */
    public Map<String, String> getHashes() {
        return hashes;
    }

    /**
     * @param hashes the hashes to set
     */
    public void setHashes(Map<String, String> hashes) {
        this.hashes = hashes;
    }

    /**
     * Add a hash
     * @param type the hash type
     * @param hash the hash, in Hexadecimal
     */
    public void addHash(String type, String hash){
        this.hashes.put(type, hash);
    }

    /**
     * Get a hash value.
     * Returns null if not found.
     * @param type
     */
    public String getHash(String type){
        return hashes.get(type);
    }
}
