package weblogic.wsee.jaxrpc.soapfault;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
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
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.util.WLSOAPFactory;
import weblogic.xml.saaj.SOAPConstants;

public class SOAPFaultUtil {
   private static final boolean verbose = Verbose.isVerbose(SOAPFaultUtil.class);
   private static final String ENV_PREFIX = "env";
   private static final String FAULT_URI = "http://www.bea.com/servers/wls70/webservice/fault/1.0.0";
   private static final String FAULT_PREFIX = "bea_fault";
   private static final String FAULT_NAME = "stacktrace";
   public static final String SOAP12_FC_SERVER = "Receiver";
   public static final String SOAP12_FC_CLIENT = "Sender";
   public static final String SOAP11_FC_SERVER = "Server";
   public static final String SOAP11_FC_CLIENT = "Client";
   public static final QName SOAP11_FC_CLIENT_QNAME = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client");
   public static final QName SOAP11_FC_SERVER_QNAME = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server");
   public static final QName SOAP12_FC_CLIENT_QNAME = new QName("http://www.w3.org/2003/05/soap-envelope", "Sender");
   public static final QName SOAP12_FC_SERVER_QNAME = new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver");
   public static final String SOAP_FC_VERSION_MISMATCH = "VersionMismatch";
   public static final String SOAP_FC_MUST_UNDERSTAND = "MustUnderstand";
   private static final List<String> SOAP12_FAULT_CODES = Arrays.asList("VersionMismatch", "MustUnderstand", "DataEncodingUnknown", "Sender", "Receiver");

   private static WLSOAPFaultException setSOAP12SubFaultCodes(WLSOAPFaultException var0, QName... var1) {
      if (var0 == null) {
         return null;
      } else if (!var0.isSOAP12() && verbose) {
         Verbose.say("Current wlSoapFaultExceptin is not a SOAP 1.2 version");
         Verbose.say("the sub faultcode(s) can not be set onto it!");
         return var0;
      } else {
         SOAPFault var2 = var0.getFault();
         if (var1 != null) {
            QName[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               QName var6 = var3[var5];

               try {
                  var2.appendFaultSubcode(var6);
               } catch (SOAPException var8) {
                  throw new AssertionError(var8);
               }
            }
         }

         return var0;
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

   private static SOAPFault fillFaultCode(SOAPFault var0, QName var1) throws SOAPException {
      if (var0 == null) {
         return var0;
      } else {
         boolean var2 = getSOAPVersion(var0);
         validateFaultCode(var2, var1);
         var0.setFaultCode(var1);
         return var0;
      }
   }

   private static SOAPFault genPartialWLSOAPFault(boolean var0, String var1, String var2, Throwable var3) throws SOAPException {
      SOAPFactory var4 = getSoapFactory(var0);
      SOAPFault var5 = var4.createFault();
      if (var1 != null) {
         var5.setFaultString(var1);
      }

      if (var2 != null) {
         var5.setFaultActor(var2);
      }

      if (var3 != null) {
         Detail var6 = newDetail(var3, var4);
         Detail var7 = var5.addDetail();
         Iterator var8 = var6.getDetailEntries();

         while(var8.hasNext()) {
            var7.addChildElement((SOAPElement)var8.next());
         }
      }

      return var5;
   }

   private static QName chgFaultCode(boolean var0, String var1) {
      return var1 != null ? createQName(var0, var1) : null;
   }

   static boolean getSOAPVersion(SOAPFault var0) throws SOAPException {
      if (var0 == null) {
         throw new SOAPException("inputted param fault is null!");
      } else if ("http://www.w3.org/2003/05/soap-envelope".equals(var0.getNamespaceURI())) {
         return true;
      } else if ("http://schemas.xmlsoap.org/soap/envelope/".equals(var0.getNamespaceURI())) {
         return false;
      } else {
         throw new SOAPException("Unknow the namespace of a SOAPFault!");
      }
   }

   public static void fillSoapDetail(Detail var0, SOAPFault var1) throws SOAPException {
      if (var0 != null && var1 != null) {
         if (var1.getDetail() != null) {
            var1.removeChild(var1.getDetail());
         }

         NodeList var2 = var0.getChildNodes();
         Detail var3 = var1.addDetail();

         for(int var4 = 0; var4 < var2.getLength(); ++var4) {
            Node var5 = var1.getOwnerDocument().importNode(var2.item(var4), true);
            var3.appendChild(var5);
         }
      }

   }

   private static void copySOAPFault(SOAPFault var0, SOAPFault var1) throws SOAPException {
      var1.setFaultCode(var0.getFaultCodeAsQName());
      var1.setFaultString(var0.getFaultString());

      try {
         if (var0.getFaultRole() != null) {
            var1.setFaultRole(var0.getFaultRole());
         }

         if (var0.getFaultActor() != null) {
            var1.setFaultActor(var0.getFaultActor());
         }

         Iterator var2 = var0.getFaultSubcodes();

         while(var2 != null && var2.hasNext()) {
            var1.appendFaultSubcode((QName)var2.next());
         }
      } catch (UnsupportedOperationException var3) {
      }

      fillSoapDetail(var0.getDetail(), var1);
   }

   private static String soap12FaultCodeTransform(String var0) {
      if ("Server".equals(var0)) {
         return "Receiver";
      } else {
         return "Client".equals(var0) ? "Sender" : var0;
      }
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

   private static void setSoap12FaultCode(SOAPFault var0, String var1, String var2, String var3) throws SOAPException {
      String[] var4 = var1.trim().split("\\.", 2);
      var4[0] = soap12FaultCodeTransform(var4[0]);
      if (SOAP12_FAULT_CODES.contains(var4[0])) {
         var0.setFaultCode("env:" + var4[0]);
         if (var4.length > 1) {
            setSoap12FaultSubCode(var0, var4[1], var2, var3);
         }
      } else {
         var0.setFaultCode("env:Sender");
         setSoap12FaultSubCode(var0, var1, var2, var3);
      }

   }

   private static void autoTransformFaultCode(boolean var0, QName var1, SOAPFault var2) throws SOAPException {
      if (var1 == null) {
         if (var0) {
            var2.setFaultCode("env:Receiver");
         } else {
            var2.setFaultCode("env:Server");
         }
      } else {
         String var3 = var1.getLocalPart();
         String var4 = var1.getNamespaceURI();
         if (var3 == null || "".equals(var3)) {
            var3 = "Server";
         }

         if (var4 != null && !var4.trim().equals("")) {
            String var5 = var1.getPrefix();
            if (var5 == null || "".equals(var5)) {
               var5 = "fault";
            }

            if (!"env".equals(var5)) {
               var2.addNamespaceDeclaration(var5, var4);
            }

            if (var0) {
               setSoap12FaultCode(var2, var3, var4, var5);
            } else {
               var2.setFaultCode(var5 + ":" + var3);
            }
         } else if (var0) {
            setSoap12FaultCode(var2, var3, (String)null, (String)null);
         } else {
            var2.setFaultCode("env:" + var3);
         }
      }

   }

   public static void fillFault(SOAPFault var0, Throwable var1) throws SOAPException {
      fillFault(var0, var1, (String)null);
   }

   private static void fillFault(SOAPFault var0, Throwable var1, String var2) throws SOAPException {
      boolean var3 = getSOAPVersion(var0);
      if (var1 instanceof WLSOAPFaultException) {
         WLSOAPFaultException var4 = (WLSOAPFaultException)var1;
         SOAPFault var5 = var4.getFault();
         copySOAPFault(var5, var0);
      } else if (var1 instanceof SOAPFaultException) {
         SOAPFaultException var7 = (SOAPFaultException)var1;
         autoTransformFaultCode(var3, var7.getFaultCode(), var0);
         var0.setFaultString(var7.getFaultString());
         String var6 = var7.getFaultActor();
         if (var6 != null) {
            var0.setFaultActor(var6);
         }

         fillSoapDetail(var7.getDetail(), var0);
      } else if (isAuthError(var1)) {
         autoTransformFaultCode(var3, new QName("Client.Authentication"), var0);
         var0.setFaultString(var2);
         fillDetail(var1, var0.addDetail(), var3);
      } else {
         if (var3) {
            fillFaultCode(var0, chgFaultCode(var3, "Receiver"));
         } else {
            fillFaultCode(var0, chgFaultCode(var3, "Server"));
         }

         var0.setFaultString(var2);
         fillDetail(var1, var0.addDetail(), var3);
      }

   }

   private static boolean isAuthError(Throwable var0) {
      String var1 = var0.getClass().getName();
      return var1.endsWith("AccessException") || var1.endsWith("AccessLocalException");
   }

   static void validate(boolean var0, String var1) throws SOAPException {
      if (var0 && !SOAP12_FAULT_CODES.contains(var1)) {
         throw new SOAPException("Faultcode [" + var1 + "] is not a legal SOAP1.2 faultcode!");
      }
   }

   static void validateFaultCode(boolean var0, QName var1) throws SOAPException {
      if (var1 == null) {
         throw new SOAPException("Faultcode is null!");
      } else {
         validate(var0, var1.getLocalPart());
         String var2 = var1.getNamespaceURI();
         if (var0 && var2 != null && !"http://www.w3.org/2003/05/soap-envelope".equals(var2)) {
            throw new SOAPException("Faultcode's namespace is not a valid SOAP 1.2 desired!");
         }
      }
   }

   private static Name createFaultName(SOAPFactory var0) {
      try {
         return var0.createName("stacktrace", "bea_fault", "http://www.bea.com/servers/wls70/webservice/fault/1.0.0");
      } catch (SOAPException var2) {
         throw new AssertionError(var2);
      }
   }

   static QName createQName(boolean var0, String var1) {
      String var2 = "http://schemas.xmlsoap.org/soap/envelope/";
      if (var0) {
         var2 = "http://www.w3.org/2003/05/soap-envelope";
      }

      return new QName(var2, var1, "env");
   }

   private static SOAPFactory getSoapFactory(boolean var0) {
      String var1 = "SOAP 1.1 Protocol";
      if (var0) {
         var1 = "SOAP 1.2 Protocol";
      }

      return WLSOAPFactory.createSOAPFactory(var1);
   }

   private static Detail newDetail(Throwable var0, SOAPFactory var1) {
      if (var0 instanceof SOAPFaultException) {
         return ((SOAPFaultException)var0).getDetail();
      } else {
         Detail var2;
         try {
            var2 = var1.createDetail();
         } catch (SOAPException var4) {
            throw new AssertionError(var4);
         }

         return fillDetail(var0, var2, var1);
      }
   }

   public static Detail newDetail(Throwable var0, boolean var1) {
      SOAPFactory var2 = getSoapFactory(var1);
      return newDetail(var0, var2);
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

   public static Detail fillDetail(Throwable var0, Detail var1, boolean var2) {
      return var1 == null ? null : fillDetail(var0, var1, getSoapFactory(var2));
   }

   public static Detail createDetail(QName var0, String var1, boolean var2) {
      SOAPFactory var3 = getSoapFactory(var2);
      Detail var4 = null;

      try {
         var4 = var3.createDetail();
         SOAPElement var5 = var4.addDetailEntry(var0).addTextNode(var1);
         return var4;
      } catch (SOAPException var6) {
         throw new AssertionError(var6);
      }
   }

   private static Detail fillDetail(Throwable var0, Detail var1, SOAPFactory var2) {
      if (!isProductionMode()) {
         fillStackTrace(var0, var1, var2);
      } else {
         try {
            var1.addDetailEntry(new QName("java.io", "string", "java")).addTextNode(getDettailString(var0));
         } catch (SOAPException var4) {
            throw new AssertionError(var4);
         }
      }

      return var1;
   }

   private static boolean isProductionMode() {
      if (!KernelStatus.isServer()) {
         return false;
      } else {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         return ManagementService.getRuntimeAccess(var0).getDomain().isProductionModeEnabled();
      }
   }

   private static Detail fillStackTrace(Throwable var0, Detail var1, SOAPFactory var2) {
      try {
         DetailEntry var3 = var1.addDetailEntry(createFaultName(var2));
         var3.addNamespaceDeclaration("bea_fault", "http://www.bea.com/servers/wls70/webservice/fault/1.0.0");
         StringBuffer var4 = new StringBuffer();
         Throwable var5 = var0;
         String var6 = "";

         do {
            var4.append(var6);
            var4.append(StackTraceUtils.throwable2StackTrace(var5));
            var5 = var5.getCause();
            var6 = "Caused by: ";
         } while(var5 != null);

         var3.addTextNode(var4.toString());
         return var1;
      } catch (SOAPException var7) {
         throw new AssertionError(var7);
      }
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
         if ("VersionMismatch".equals(var1.getFaultCode().getLocalPart())) {
            return true;
         }
      }

      return false;
   }

   public static void throwVersionMismatchException(String var0) {
      throw newWLSOAP11FaultException((String)"VersionMismatch", var0, (String)null, (Throwable)null);
   }

   public static WLSOAPFaultException newWLSOAP11FaultException(String var0, String var1, String var2, Throwable var3) {
      QName var4 = createQName(false, var0);
      return newWLSOAP11FaultException(var4, var1, var2, var3);
   }

   public static WLSOAPFaultException newWLSOAP11FaultException(QName var0, String var1, String var2, Throwable var3) {
      boolean var4 = false;

      try {
         validateFaultCode(var4, var0);
         SOAPFault var5 = genPartialWLSOAPFault(var4, var1, var2, var3);
         fillFaultCode(var5, var0);
         return new WLSOAPFaultException(var4, var5);
      } catch (SOAPException var6) {
         throw new AssertionError(var6);
      }
   }

   public static WLSOAPFaultException newWLSOAP12FaultException(String var0, String var1, String var2, Throwable var3, QName... var4) {
      boolean var5 = true;

      try {
         validate(var5, var0);
         SOAPFault var6 = genPartialWLSOAPFault(var5, var1, var2, var3);
         QName var7 = createQName(var5, var0);
         fillFaultCode(var6, var7);
         WLSOAPFaultException var8 = new WLSOAPFaultException(var5, var6);
         setSOAP12SubFaultCodes(var8, var4);
         return var8;
      } catch (SOAPException var9) {
         throw new AssertionError(var9);
      }
   }

   public static WLSOAPFaultException newWLSOAPFaultException(MessageContext var0, String var1, String var2, String var3, String var4, Throwable var5, QName... var6) {
      return AsyncUtil.isSoap12(var0) ? newWLSOAP12FaultException(var2, var3, var4, var5, var6) : newWLSOAP11FaultException(var1, var3, var4, var5);
   }

   public static WLSOAPFaultException newWLSOAPFaultException(MessageContext var0, QName var1, String var2, String var3, String var4, Throwable var5, QName... var6) {
      return AsyncUtil.isSoap12(var0) ? newWLSOAP12FaultException(var2, var3, var4, var5, var6) : newWLSOAP11FaultException(var1, var3, var4, var5);
   }

   public static QName createFaultCodeQName(String var0) {
      return new QName("env", var0);
   }
}
