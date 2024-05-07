package weblogic.xml.security.utils;

import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public interface Preprocessor {
   void begin(StartElement var1, XMLOutputStream var2) throws XMLStreamException;

   void end(EndElement var1, XMLOutputStream var2) throws XMLStreamException;
}
