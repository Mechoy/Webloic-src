package weblogic.xml.security.utils;

import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLStreamException;

public interface Observer {
   boolean observe(XMLEvent var1) throws XMLStreamException;

   boolean consumes();
}
