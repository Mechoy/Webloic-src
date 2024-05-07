package weblogic.wsee.wstx.wsat;

import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

public class WSATFaultFactory {
   static final String INVALID_STATE = "InvalidState";
   static final String INVALID_PROTOCOL = "InvalidProtocol";
   static final String INVALID_PARAMETERS = "InvalidParameters";
   static final String NO_ACTIVITY = "NoActivity";
   static final String CONTEXT_REFUSED = "ContextRefused";
   static final String ALREADY_REGISTERED = "AlreadyRegistered";
   static final String INCONSISTENT_INTERNAL_STATE = "InconsistentInternalState";
   static final String HTTP_SCHEMAS_XMLSOAP_ORG_WS_2004_10_WSAT_FAULT = "http://schemas.xmlsoap.org/ws/2004/10/wsat/fault";
   private static final String CLIENT = "Client";
   static final QName FAULT_CODE_Q_NAME11 = new QName("http://www.w3.org/2003/05/soap-envelope", "Client");
   private static final String SENDER = "Sender";
   static final QName FAULT_CODE_Q_NAME = new QName("http://www.w3.org/2003/05/soap-envelope", "Sender");
   private static boolean m_isSOAP11 = true;

   public static void throwInvalidStateFault() {
      throwSpecifiedWSATFault("The message was invalid for the current state of the activity.", "InvalidState");
   }

   public static void throwInvalidProtocolFault() {
      throwSpecifiedWSATFault("The protocol is invalid or is not supported by the coordinator.", "InvalidProtocol");
   }

   public static void throwInvalidParametersFault() {
      throwSpecifiedWSATFault("The message contained invalid parameters and could not be processed.", "InvalidParameters");
   }

   public static void throwNoActivityFault() {
      throwSpecifiedWSATFault("The participant is not responding and is presumed to have ended.", "NoActivity");
   }

   public static void throwContextRefusedFault() {
      throwSpecifiedWSATFault("The coordination context that was provided could not be accepted.", "ContextRefused");
   }

   public static void throwAlreadyRegisteredFault() {
      throwSpecifiedWSATFault("The participant has already registered for the same protocol.", "AlreadyRegistered");
   }

   public static void throwInconsistentInternalStateFault() {
      throwSpecifiedWSATFault("A global consistency failure has occurred. This is an unrecoverable condition.", "InconsistentInternalState");
   }

   static void setSOAPVersion11(boolean var0) {
      m_isSOAP11 = var0;
   }

   private static void throwSpecifiedWSATFault(String var0, String var1) {
      try {
         SOAPFault var2;
         if (m_isSOAP11) {
            var2 = SOAPFactory.newInstance().createFault(var0, new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat/fault", var1, "wsat"));
         } else {
            var2 = SOAPFactory.newInstance("SOAP 1.2 Protocol").createFault();
            var2.setFaultCode(FAULT_CODE_Q_NAME);
            var2.appendFaultSubcode(new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat/fault", var1, "wsat"));
            var2.addFaultReasonText(var0, Locale.ENGLISH);
         }

         throw new SOAPFaultException(var2);
      } catch (SOAPException var3) {
         throw new WebServiceException(var3);
      }
   }
}
