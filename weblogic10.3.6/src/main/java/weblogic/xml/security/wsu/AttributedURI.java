package weblogic.xml.security.wsu;

import javax.xml.soap.SOAPElement;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.stream.XMLOutputStream;

public interface AttributedURI {
   void setId(String var1);

   String getId();

   String getURI();

   void toXML(XMLOutputStream var1);

   void toXML(XMLWriter var1);

   void toXML(SOAPElement var1);
}
