package edu.uw.apl.stix.utils;

import java.io.File;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathTest extends junit.framework.TestCase {

	protected void setUp() throws Exception {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		// wah, without this 'namespace awareness setting' it all fails!
		fac.setNamespaceAware( true );
		bob = fac.newDocumentBuilder();
		XPathFactory xpf = XPathFactory.newInstance();
		xp = xpf.newXPath();

		NamespaceContext nc = new NamespaceContext() {
				public String getNamespaceURI( String prefix ) {
					if( prefix == null ) {
						throw new IllegalArgumentException( "No prefix" );
					} else if( prefix.equals( "stix" ) ) {
						return "http://stix.mitre.org/stix-1";
					} else if( prefix.equals( "cybox" ) ) {
						return "http://cybox.mitre.org/cybox-2";
					} else if( prefix.equals( "cyboxCommon" ) ) {
						return  "http://cybox.mitre.org/common-2";
					} else if( prefix.equals( "FileObj" ) ) {
						return  "http://cybox.mitre.org/objects#FileObject-2";
					} else {
						return XMLConstants.NULL_NS_URI;
					}
				}
				public String getPrefix(String namespaceURI) {
					// Not needed in this context.
					return null;
				}
				public Iterator getPrefixes(String namespaceURI) {
					// Not needed in this context.
					return null;
				}
			};
		xp.setNamespaceContext( nc );
	}
	
	public void testAPT1_1() throws Exception {
		File f = new File
			( "../jaxb/src/test/resources/APT1/Appendix_G_IOCs_Full.xml" );
		if( !f.exists() )
			return;
		Document d = bob.parse( f );

		String expr1 = "/stix:STIX_Package/stix:Observables/cybox:Observable/"+
			"cybox:Object/cybox:Properties/FileObj:Hashes/cyboxCommon:Hash/cyboxCommon:Simple_Hash_Value";

		NodeList nl = (NodeList)xp.evaluate( expr1, d, XPathConstants.NODESET);
		System.out.println( nl.getLength() );
		for( int i = 0; i < nl.getLength(); i++ ) {
			Node n = nl.item(i);
			System.out.println( n.getTextContent() );
		}
	}
	
	private DocumentBuilder bob;
	private XPath xp;
}

// eof
