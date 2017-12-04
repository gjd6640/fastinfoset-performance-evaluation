package jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root", namespace = "myNS")
public class RootWithListJaxbObject
{
    @XmlElement(name = "list-item", namespace = "myNS", required = true)
    public List<ListItem> list;
    
    public static class ListItem
    {
        
        public ListItem() { }
                
        @XmlElements({
                @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA1"),
                @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA2")
                // While we could map more of the fields that are present I don't think doing so is necessary to illustrate this concern
                    })
        public List<String> uniqueFieldsList;
    }
    
}
