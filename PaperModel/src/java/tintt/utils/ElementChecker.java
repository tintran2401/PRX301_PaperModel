/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.utils;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 *
 * @author TiTi
 */
public class ElementChecker {
      public static boolean isElementWith(StartElement element, String elementName, 
            String attributeName, String attributeValue) {
        String name = element.getName().getLocalPart();
        if (!elementName.equals(name)) {
            return false;
        }
        Attribute attribute = element.getAttributeByName(new QName(attributeName));
        if (attribute == null) {
            return false;
        }
        return attributeValue.equals(attribute.getValue());
    }
    
    public static boolean isElementWith(StartElement element, String elementName) {
        String name = element.getName().getLocalPart();
        return elementName.equals(name);
    }
    
    public static boolean isElementWith(EndElement element, String elementName) {
        String name = element.getName().getLocalPart();
        return elementName.equals(name);
    }
}
