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

import java.util.List;

/**
 * Class for serializing the basic information to/from a STIX document
 */
public class SimpleSTIXDocument {
    private SimpleSTIXHeader header;
    private List<String> observableIpAddresses;
    private List<String> observableHostnames;
    private List<String> observableURIs;
    private List<FileObjectObservable> observableFiles;

    /**
     * @return the header
     */
    public SimpleSTIXHeader getHeader() {
        return header;
    }
    /**
     * @param header the header to set
     */
    public void setHeader(SimpleSTIXHeader header) {
        this.header = header;
    }
    /**
     * @return the observableIpAddresses
     */
    public List<String> getObservableIpAddresses() {
        return observableIpAddresses;
    }
    /**
     * @param observableIpAddresses the observableIpAddresses to set
     */
    public void setObservableIpAddresses(List<String> observableIpAddresses) {
        this.observableIpAddresses = observableIpAddresses;
    }
    /**
     * @return the observableHostnames
     */
    public List<String> getObservableHostnames() {
        return observableHostnames;
    }
    /**
     * @param observableHostnames the observableHostnames to set
     */
    public void setObservableHostnames(List<String> observableHostnames) {
        this.observableHostnames = observableHostnames;
    }
    /**
     * @return the observableFiles
     */
    public List<FileObjectObservable> getObservableFiles() {
        return observableFiles;
    }
    /**
     * @param observableFiles the observableFiles to set
     */
    public void setObservableFiles(List<FileObjectObservable> observableFiles) {
        this.observableFiles = observableFiles;
    }
    /**
     * @return the observableURIs
     */
    public List<String> getObservableURIs() {
        return observableURIs;
    }
    /**
     * @param observableURIs the observableURIs to set
     */
    public void setObservableURIs(List<String> observableURIs) {
        this.observableURIs = observableURIs;
    }
}
