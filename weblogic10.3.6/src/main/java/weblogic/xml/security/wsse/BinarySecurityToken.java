package weblogic.xml.security.wsse;

import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public interface BinarySecurityToken extends Token {
   void setId(String var1);

   void toXML(XMLOutputStream var1) throws XMLStreamException;
}
