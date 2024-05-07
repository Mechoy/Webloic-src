package weblogic.xml.security.wsse;

import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public interface KeyIdentifier {
   String getIdentifierString();

   byte[] getIdentifier();

   String getValueType();

   void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException;
}
