package com.sun.xml.fastinfoset.stax;

public class JaxbStringInternSuppressionStaxManager extends StAXManager {
    
    public JaxbStringInternSuppressionStaxManager() {
        // Add to the allowable list of feature names so that the use may set these "StAXInputFactory" properties
        super.features.put("org.codehaus.stax2.internNames", null);
        super.features.put("org.codehaus.stax2.internNsUris", null);
    }

}
