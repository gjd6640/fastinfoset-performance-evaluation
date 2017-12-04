import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.SAXException;

import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;
import com.sun.xml.fastinfoset.stax.JaxbStringInternSuppressionStaxManager;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXManager;
import com.sun.xml.fastinfoset.stax.factory.StAXInputFactory;

public class FastinfosetCodec {
    /*
    public XMLStreamReader decode(final byte[] bytes) {
        return decode(new ByteArrayInputStream(bytes));
    }
    */
    public XMLStreamReader decodeWithDefaultInterningSettings(final InputStream byteStream) {
        StAXInputFactory xmlInputFactory = new StAXInputFactory();

        XMLStreamReader p;
        try {
            p = xmlInputFactory.createXMLStreamReader(byteStream);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

        return p;
    }
    
    public XMLStreamReader decodeInterningSuppressedViaReflection(final InputStream byteStream) {
//System.setProperty("com.sun.xml.fastinfoset.parser.string-interning", "false"); // We once thought this but can no longer reproduce it: Jaxb expects strings to be interned and the FI lib doesn't do that by default. Without this you'll get an "AssertionError" if Jaxb and the underlying FI stream encounters unexpected/extra tags.
        StAXManager m = new StAXManager();
        
        Field f;
        try {
            f = m.getClass().getDeclaredField("features");
            f.setAccessible(true);
            HashMap<String, String> featuresMap = (HashMap) f.get(m);
            featuresMap.put("org.codehaus.stax2.internNames", null);
            featuresMap.put("org.codehaus.stax2.internNsUris", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // We do NOT want the Jaxb2 library to spend time interning strings. Setting these two properties to 'true' seems to tell it not to do interning.
        m.setProperty("org.codehaus.stax2.internNames", true); // Why does 'true' here seem to DISABLE interning in the Stax lib? Maybe here the stax lib is asking 'Did you already do interning so that I don't need to?'
        m.setProperty("org.codehaus.stax2.internNsUris", true);
        StAXDocumentParser p = new StAXDocumentParser(byteStream, m);
        p.setParseFragments(true);
        p.setStringInterning(true); // With Jaxb's interning turned off unmarshalling fails (quietly / empty object) unless we turn on interning in the FI library.

        return p;
    }

    public XMLStreamReader decodeWithInterningDisabledThruFactoryProperties(final InputStream byteStream) {
        //XMLInputFactory xmlInputFactory = StAXInputFactory.newFactory("com.sun.xml.fastinfoset.stax.StAXDocumentParser", this.getClass().getClassLoader()); // <-- Doesn't work. What 'name' should I use?
        //XMLInputFactory xmlInputFactory = StAXInputFactory.newInstance();// <- Does not returns a factory that supports FI decoding 
        StAXInputFactory xmlInputFactory = new StAXInputFactory();
        xmlInputFactory.setProperty("org.codehaus.stax2.internNames", true); // Fails at runtime / invalid property name
        xmlInputFactory.setProperty("org.codehaus.stax2.internNsUris", true);

        XMLStreamReader p;
        try {
            p = xmlInputFactory.createXMLStreamReader(byteStream);
            ((StAXDocumentParser)p).setStringInterning(true); // With Jaxb's interning turned off unmarshalling fails (quietly / empty object) unless we turn on interning in the FI library.
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

        return p;
    }

    public XMLStreamReader decodeWithInterningDisabledUsingCustomFactoryExtension(final InputStream byteStream) {
        StAXManager manager = new JaxbStringInternSuppressionStaxManager();
        manager.setProperty("org.codehaus.stax2.internNames", true);
        manager.setProperty("org.codehaus.stax2.internNsUris", true);
        
        StAXDocumentParser p = new StAXDocumentParser(byteStream);
        p.setManager(manager);
        p.setStringInterning(true); // With Jaxb's interning turned off unmarshalling fails (quietly / empty object) unless we turn on interning in the FI library.

        return p;
    }

  public ByteArrayOutputStream encode(final InputStream xmlDocument) {
    // Set up output stream for fast infoset document
    ByteArrayOutputStream fiDocument = new ByteArrayOutputStream();
    encode(xmlDocument, fiDocument);
    return fiDocument;
  }

  public void encode(final InputStream xmlDocument, final OutputStream outputStream) {
    try {
      // Create Fast Infoset SAX serializer
      SAXDocumentSerializer saxDocumentSerializer = new SAXDocumentSerializer();
      // Set the output stream
      saxDocumentSerializer.setOutputStream(outputStream);

      // Instantiate JAXP SAX parser factory
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

      /*
       * Set parser to be namespace aware Very important to do otherwise invalid
       * FI documents will be created by the SAXDocumentSerializer
       */
      saxParserFactory.setNamespaceAware(true);

      try {

        // Instantiate the JAXP SAX parser
        SAXParser saxParser = saxParserFactory.newSAXParser();
        // Set the lexical handler

        saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", saxDocumentSerializer);
        // Parse the XML document and convert to a fast infoset document
        saxParser.parse(xmlDocument, saxDocumentSerializer);

      } catch (IOException | SAXException | ParserConfigurationException e) {
        throw new RuntimeException(e);
      }

    } finally {
      try {
        xmlDocument.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}

