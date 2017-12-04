package jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.up.www.services.example.basic.get_current_time._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName QNAME = new QName("myNS", "root");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.up.www.services.example.basic.get_current_time._1
     * 
     */
    public ObjectFactory() {
    }


    @XmlElementDecl(namespace = "myNS", name = "root")
    public JAXBElement<Object> createRootWithListJaxbObject(Object value) {
        return new JAXBElement<Object>(QNAME, Object.class, null, value);
    }

}
