package weblogic.xml.crypto.wss.api;

import java.util.Calendar;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.xml.crypto.wss.TimestampHandler;

public interface NonceValidator {
   QName NONCE_FAULTCODE = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "NonceFault");

   void init(String var1, TimestampHandler var2);

   void checkNonceAndTime(String var1, Calendar var2) throws SOAPFaultException;

   void setExpirationTime(int var1);

   void checkDuplicateNonce(String var1) throws SOAPFaultException;
}
