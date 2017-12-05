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
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA2"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA3"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA4"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA5"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA6"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA7"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA8"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA9"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA10"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA11"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA12"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA13"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA14"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA15"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA16"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA17"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA18"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA19"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA20"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA21"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA22"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA23"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA24"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA25"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA26"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA27"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA28"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA29"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA30"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA31"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA32"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA33"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA34"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA35"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA36"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA37"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA38"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA39"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA40"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA41"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA42"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA43"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA44"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA45"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA46"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA47"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA48"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA49"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA50"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA51"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA52"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA53"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA54"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA55"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA56"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA57"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA58"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA59"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA60"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA61"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA62"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA63"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA64"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA65"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA66"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA67"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA68"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA69"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA70"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA71"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA72"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA73"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA74"),
            @XmlElement(name="uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA75"),
                    })
        public List<String> uniqueFieldsList;
    }
    
}
