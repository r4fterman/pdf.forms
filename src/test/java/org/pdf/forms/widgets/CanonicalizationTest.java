package org.pdf.forms.widgets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Disabled
class CanonicalizationTest {

    private static final String INPUT = ""
            + "<hello>"
            + "world"
            + "</hello>";

    @Test
    void name() throws Exception {
        final String canonicalXml = transform(INPUT);
        System.out.println(canonicalXml);
    }

    private String transform(final String xml) throws IOException, SAXException, ParserConfigurationException {
        final Document doc = createNewDocument(xml);

        try {
            final Data data = new NodeSetDataImpl(doc, NodeSetDataImpl.getRootNodeFilter());
            final XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
            final CanonicalizationMethod canonicalizationMethod = xmlSignatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null);
            // Doing the actual canonicalization
            final OctetStreamData transformedData = (OctetStreamData) canonicalizationMethod.transform(data, null);
//            final byte[] bytes = IOUtils.toByteArray(transformedData.getOctetStream());
//            return new String(bytes, StandardCharsets.UTF_8);
            return "";
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return xml;
    }

    private Document createNewDocument(final String xml) throws ParserConfigurationException, IOException, SAXException {
        final byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream bin = new ByteArrayInputStream(bytes);

        final DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(true);

        final DocumentBuilder docBuilder = fac.newDocumentBuilder();

        return docBuilder.parse(bin);
    }
}

