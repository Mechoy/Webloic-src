package weblogic.wsee.util;

import java.security.AccessController;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.saaj.SOAPConstants;

/** @deprecated */
@Deprecated
public final class FaultUtil {
   private static final String FAULT_URI = "http://www.bea.com/servers/wls70/webservice/fault/1.0.0";
   private static final String FAULT_PREFIX = "bea_fault";
   private static final SOAPFactory soapFactory = getSoapFactory();
   private static final Name FAULT_NAME = createFaultName();
   private static final List<String> SOAP12_FAULT_CODES = Arrays.asList("VersionMismatch", "MustUnderstand", "DataEncodingUnknown", "Sender", "Receiver");

   private static SOAPFactory getSoapFactory() {
      return WLSOAPFactory.createSOAPFactory();
   }

   private static Name createFaultName() {
      try {
         return soapFactory.createName("stacktrace", "bea_fault", "http://www.bea.com/servers/wls70/webservice/fault/1.0.0");
      } catch (SOAPException var1) {
         throw new AssertionError(var1);
      }
   }

   public static Detail newDetail() {
      try {
         return soapFactory.createDetail();
      } catch (SOAPException var1) {
         throw new AssertionError(var1);
      }
   }

   public static void throwSOAPFaultException(String var0, String var1, Throwable var2) throws SOAPFaultException {
      Detail var3 = null;
      if (var2 != null) {
         var3 = newDetail(var2);
      }

      throw new SOAPFaultException(new QName("env", var0), var1, (String)null, var3);
   }

   public static Detail newDetail(Throwable var0) {
      if (var0 instanceof SOAPFaultException) {
         return ((SOAPFaultException)var0).getDetail();
      } else {
         Detail var1 = newDetail();
         return fillDetail(var0, var1);
      }
   }

   public static Detail fillDetail(Throwable var0, Detail var1, boolean var2) {
      if (!var2) {
         fillStackTrace(var0, var1);
      } else {
         try {
            var1.addDetailEntry(new QName("java.io", "string", "java")).addTextNode(getDettailString(var0));
         } catch (SOAPException var4) {
            throw new AssertionError(var4);
         }
      }

      return var1;
   }

   public static Detail fillDetail(Throwable var0, Detail var1) {
      return fillDetail(var0, var1, isProductionMode());
   }

   private static boolean isProductionMode() {
      if (!KernelStatus.isServer()) {
         return false;
      } else {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         return ManagementService.getRuntimeAccess(var0).getDomain().isProductionModeEnabled();
      }
   }

   private static String getDettailString(Throwable var0) {
      StringBuffer var1 = new StringBuffer();

      do {
         var1.append(var0.toString());
         var1.append("\n");
         var0 = var0.getCause();
      } while(var0 != null);

      return var1.toString();
   }

   private static Detail fillStackTrace(Throwable var0, Detail var1) {
      try {
         DetailEntry var2 = var1.addDetailEntry(FAULT_NAME);
         var2.addNamespaceDeclaration("bea_fault", "http://www.bea.com/servers/wls70/webservice/fault/1.0.0");
         StringBuffer var3 = new StringBuffer();
         Throwable var4 = var0;
         String var5 = "";

         do {
            var3.append(var5);
            var3.append(StackTraceUtils.throwable2StackTrace(var4));
            var4 = var4.getCause();
            var5 = "Caused by: ";
         } while(var4 != null);

         var2.addTextNode(var3.toString());
         return var1;
      } catch (SOAPException var6) {
         throw new AssertionError(var6);
      }
   }

   public static void fillFault(SOAPFault var0, Throwable var1, String var2) throws SOAPException {
      boolean var3 = true;
      if ("http://schemas.xmlsoap.org/soap/envelope/".equals(var0.getNamespaceURI())) {
         var3 = false;
      }

      if (var1 instanceof SOAPFaultException) {
         SOAPFaultException var4 = (SOAPFaultException)var1;
         addFaultCode(var0, var4, var3);
         var0.setFaultString(var4.getFaultString());
         String var5 = var4.getFaultActor();
         if (var5 != null) {
            var0.setFaultActor(var5);
         }

         if (var4.getDetail() != null) {
            if (var0.getDetail() != null) {
               var0.removeChild(var0.getDetail());
            }

            if (useStandardSoap12Fault(var3)) {
               fillSoap12Detail(var4.getDetail(), var0);
            } else {
               var0.addChildElement(var4.getDetail());
            }
         }
      } else if (isAuthError(var1)) {
         if (useStandardSoap12Fault(var3)) {
            setSoap12FaultCode(var0, "Sender.Authentication");
         } else {
            var0.setFaultCode("env:Client.Authentication");
         }

         var0.setFaultString(var2);
         fillDetail(var1, var0.addDetail());
      } else {
         if (useStandardSoap12Fault(var3)) {
            setSoap12FaultCode(var0, "Receiver");
         } else {
            var0.setFaultCode("env:Server");
         }

         var0.setFaultString(var2);
         fillDetail(var1, var0.addDetail());
      }

   }

   private static boolean isAuthError(Throwable var0) {
      String var1 = var0.getClass().getName();
      return var1.endsWith("AccessException") || var1.endsWith("AccessLocalException");
   }

   private static void addFaultCode(SOAPFault var0, SOAPFaultException var1, boolean var2) throws SOAPException {
      QName var3 = var1.getFaultCode();
      if (var3 == null) {
         if (useStandardSoap12Fault(var2)) {
            var0.setFaultCode("env:Receiver");
         } else {
            var0.setFaultCode("env:Server");
         }
      } else {
         String var4 = var3.getLocalPart();
         String var5 = var3.getNamespaceURI();
         if (var4 == null || "".equals(var4)) {
            var4 = "Server";
         }

         if (var5 == null) {
            if (useStandardSoap12Fault(var2)) {
               setSoap12FaultCode(var0, var4);
            } else {
               var0.setFaultCode("env:" + var4);
            }
         } else {
            String var6 = var3.getPrefix();
            if (var6 == null || "".equals(var6)) {
               var6 = "fault";
            }

            var0.addNamespaceDeclaration(var6, var5);
            if (useStandardSoap12Fault(var2)) {
               setSoap12FaultCode(var0, var4, var5, var6);
            } else {
               var0.setFaultCode(var6 + ":" + var4);
            }
         }
      }

   }

   public static SOAPMessage exception2Fault(MessageFactory var0, Throwable var1) {
      try {
         SOAPMessage var2 = var0.createMessage();
         SOAPBody var3 = var2.getSOAPPart().getEnvelope().getBody();
         if (var3 == null) {
            var3 = var2.getSOAPPart().getEnvelope().addBody();
         }

         SOAPFault var4 = var3.addFault();
         StringBuilder var5 = new StringBuilder();
         var1 = ExceptionUtil.unwrapException(var1, var5);
         fillFault(var4, var1, var5.toString());
         return var2;
      } catch (SOAPException var6) {
         throw new AssertionError(var6);
      }
   }

   private static boolean useStandardSoap12Fault(boolean var0) {
      return var0;
   }

   private static void setSoap12FaultSubCode(SOAPFault var0, String var1, String var2, String var3) throws SOAPException {
      QName var4 = null;
      if (var2 != null) {
         var4 = new QName(var2, var1, var3);
      } else {
         var4 = new QName(var0.getNamespaceURI(), var1, "env");
      }

      var0.appendFaultSubcode(var4);
   }

   private static void setSoap12FaultCode(SOAPFault var0, String var1) throws SOAPException {
      setSoap12FaultCode(var0, var1, (String)null, (String)null);
   }

   private static void setSoap12FaultCode(SOAPFault var0, String var1, String var2, String var3) throws SOAPException {
      String[] var4 = var1.trim().split("\\.", 2);
      var4[0] = soap12FaultCodeTransform(var4[0]);
      if (SOAP12_FAULT_CODES.contains(var4[0])) {
         var0.setFaultCode("env:" + var4[0]);
         if (var4.length > 1) {
            setSoap12FaultSubCode(var0, var4[1], var2, var3);
         }
      } else {
         var0.setFaultCode("env:Receiver");
         setSoap12FaultSubCode(var0, var1, var2, var3);
      }

   }

   private static String soap12FaultCodeTransform(String var0) {
      if ("Server".equals(var0)) {
         return "Receiver";
      } else {
         return "Client".equals(var0) ? "Sender" : var0;
      }
   }

   private static Detail fillSoap12Detail(Detail var0, SOAPFault var1) throws SOAPException {
      NodeList var2 = var0.getChildNodes();
      Detail var3 = var1.addDetail();

      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         Node var5 = var1.getOwnerDocument().importNode(var2.item(var4), true);
         var3.appendChild(var5);
      }

      return var3;
   }

   private static void addSupportHeader(SOAPMessage var0) throws SOAPException {
      SOAPHeaderElement var1 = var0.getSOAPHeader().addHeaderElement(SOAPConstants.HEADER12_UPGRADE);
      SOAPElement var2 = var1.addChildElement(SOAPConstants.HEADER12_SUPPORTED_ENVELOPE);
      var2.setAttributeNS((String)null, "qname", "Envelope");
   }

   public static SOAPMessage createVersionMismatchMsg(boolean var0, Throwable var1) {
      try {
         if (var1 instanceof SOAPFaultException) {
            SOAPFaultException var2 = (SOAPFaultException)var1;
            MessageFactory var3 = WLMessageFactory.getInstance().getMessageFactory(false);
            SOAPMessage var4 = var3.createMessage();
            if (var0) {
               addSupportHeader(var4);
            }

            SOAPBody var5 = var4.getSOAPPart().getEnvelope().getBody();
            if (var5 == null) {
               var5 = var4.getSOAPPart().getEnvelope().addBody();
            }

            SOAPFault var6 = var5.addFault();
            fillFault(var6, var2, var2.getMessage());
            return var4;
         } else {
            throw new SOAPException("Illegal SOAPFaultException", var1);
         }
      } catch (SOAPException var7) {
         throw new AssertionError(var7);
      }
   }

   public static boolean isVersionMismatch(Throwable var0) {
      if (var0 instanceof SOAPFaultException) {
         SOAPFaultException var1 = (SOAPFaultException)var0;
         if (var1.getFaultCode() != null && "VersionMismatch".equals(var1.getFaultCode().getLocalPart())) {
            return true;
         }
      }

      return false;
   }
}
