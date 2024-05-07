package weblogic.xml.security.wsu;

import java.util.Calendar;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.stream.XMLOutputStream;

public interface AttributedDateTime {
   void setId(String var1);

   String getId();

   String getTimeString();

   Calendar getTime();

   QName getValueType();

   void toXML(XMLOutputStream var1);

   void toXML(XMLWriter var1);

   void toXML(SOAPElement var1);
}
