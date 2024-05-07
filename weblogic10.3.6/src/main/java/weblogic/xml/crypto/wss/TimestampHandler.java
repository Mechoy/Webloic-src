package weblogic.xml.crypto.wss;

import java.util.Calendar;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.xml.crypto.wss.api.Timestamp;

public interface TimestampHandler {
   QName EXPIRED_FAULTCODE = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "MessageExpired");

   void validate(Timestamp var1, short var2);

   void validate(Calendar var1) throws SOAPFaultException;

   int getMessageAge();

   void setMessageAge(int var1);
}
