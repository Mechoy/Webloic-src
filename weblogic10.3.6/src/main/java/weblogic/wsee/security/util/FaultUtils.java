package weblogic.wsee.security.util;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.WLSOAPFactory;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.xml.crypto.wss.WSSecurityException;

public class FaultUtils {
   private static final String DEBUG_PROPERTY = "weblogic.wsee.security.debug";
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.wsee.security.debug");
   private static final QName SENDER = new QName("http://www.w3.org/2003/05/soap-envelope", "Sender", "env");
   private static final QName RECEIVER = new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver", "env");
   private static final QName SERVER = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server", "env");

   public static boolean isDebug() {
      return DEBUG;
   }

   public static void setSOAPFault(Throwable var0, SoapMessageContext var1) throws SOAPException {
      var1.setFault(var0);
      MessageFactory var2 = var1.getMessageFactory();
      SOAPMessage var3 = var2.createMessage();
      SOAPFault var4 = var3.getSOAPBody().addFault();
      var1.setMessage(var3);
      QName var5 = getSubcode(var0);
      if (isSOAP12(var1)) {
         setSOAP12Code(var5, var4);
      } else {
         setSOAP11Code(var5, var4);
      }

      var4.setFaultString(getMessage(var0));
      if (DEBUG) {
         Detail var6 = var4.addDetail();
         SOAPFaultUtil.fillDetail(var0, var6, true);
      }

   }

   private static String getMessage(Throwable var0) {
      String var1 = var0.getMessage();
      if (var1 == null) {
         var1 = var0.getCause().getMessage();
      }

      return var1;
   }

   private static void setSOAP11Code(QName var0, SOAPFault var1) throws SOAPException {
      SOAPFactory var2 = WLSOAPFactory.createSOAPFactory("SOAP 1.1 Protocol");
      if (var0 != null) {
         var1.setFaultCode(getName(var0, var2));
      } else {
         var1.setFaultCode(getName(SERVER, var2));
      }

   }

   private static void setSOAP12Code(QName var0, SOAPFault var1) throws SOAPException {
      SOAPFactory var2 = WLSOAPFactory.createSOAPFactory("SOAP 1.2 Protocol");
      if (var0 != null) {
         var1.setFaultCode(getName(SENDER, var2));
         var1.appendFaultSubcode(var0);
      } else {
         var1.setFaultCode(getName(RECEIVER, var2));
      }

   }

   private static Name getName(QName var0, SOAPFactory var1) throws SOAPException {
      return var1.createName(var0.getLocalPart(), var0.getPrefix(), var0.getNamespaceURI());
   }

   private static QName getSubcode(Throwable var0) {
      QName var1 = null;
      if (var0 instanceof WSSecurityException) {
         var1 = ((WSSecurityException)var0).getFaultCode();
      }

      return var1;
   }

   private static boolean isSOAP12(SoapMessageContext var0) throws SOAPException {
      Dispatcher var1 = var0.getDispatcher();
      if (var1 != null) {
         return var1.isSOAP12();
      } else {
         return "http://www.w3.org/2003/05/soap-envelope".equals(var0.getMessage().getSOAPPart().getEnvelope().getNamespaceURI());
      }
   }
}
