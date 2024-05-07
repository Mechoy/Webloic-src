package weblogic.xml.security.wsu;

import java.util.Calendar;
import javax.xml.soap.SOAPElement;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.stream.XMLOutputStream;

public interface Timestamp {
   void setId(String var1);

   void setExpires(long var1);

   void setExpires(Calendar var1);

   String getId();

   Created getCreated();

   Expires getExpires();

   Received[] getReceived();

   void toXML(XMLOutputStream var1);

   void toXML(XMLWriter var1);

   void toXML(SOAPElement var1);

   void setMustUnderstand(boolean var1);

   boolean getMustUnderstand();
}
