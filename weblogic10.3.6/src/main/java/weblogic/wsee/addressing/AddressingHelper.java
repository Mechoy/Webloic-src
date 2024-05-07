package weblogic.wsee.addressing;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public final class AddressingHelper {
   public static WSAVersion getWSAVersion(MessageContext var0) {
      if (var0 == null) {
         throw new AssertionError("MessageContext should not be null!");
      } else {
         if (var0.containsProperty("weblogic.wsee.addressing.version")) {
            Object var1 = var0.getProperty("weblogic.wsee.addressing.version");
            if (var1 instanceof WSAVersion) {
               return (WSAVersion)var1;
            }
         }

         return WSAVersion.WSA10;
      }
   }

   public static WSAVersion getWSAVersion(Map var0) {
      if (var0 == null) {
         throw new AssertionError("Map parameter should not be null!");
      } else {
         if (var0.containsKey("weblogic.wsee.addressing.version")) {
            Object var1 = var0.get("weblogic.wsee.addressing.version");
            if (var1 instanceof WSAVersion) {
               return (WSAVersion)var1;
            }
         }

         return WSAVersion.WSA10;
      }
   }

   public static WSAVersion getWSAVersion(String var0) {
      if ("http://www.w3.org/2005/08/addressing".equals(var0)) {
         return WSAVersion.WSA10;
      } else if ("http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var0)) {
         return WSAVersion.MemberSubmission;
      } else {
         throw new IllegalArgumentException(var0 + " is a wrong WS-Addressing namespace!");
      }
   }

   public static boolean isAnonymousEndpointReference(MessageContext var0, EndpointReference var1) {
      if (var0 == null) {
         throw new AssertionError("MessageContext should not be null!");
      } else if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getAddress();
         return getWSAVersion(var0).equals(WSAVersion.MemberSubmission) ? "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous".equals(var2) : "http://www.w3.org/2005/08/addressing/anonymous".equals(var2);
      }
   }

   public static boolean isAnonymousReferenceURI(String var0) {
      return "http://www.w3.org/2005/08/addressing/anonymous".equals(var0) || "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous".equals(var0);
   }

   public static boolean isWSA10NamespaceURI(String var0) {
      return "http://www.w3.org/2005/08/addressing".equals(var0);
   }

   public static EndpointReference createWSA10AnonymousEndpointReference() {
      return AddressingProvider10Impl.getProviderInstance().createAnonymousEndpointReference();
   }

   public static EndpointReference createMemberSubmissionAnonymousEndpointReference() {
      return AddressingProviderSubmissionImpl.getProviderInstance().createAnonymousEndpointReference();
   }

   public static AddressingProvider getAddressingProvider(MessageContext var0) {
      return AddressingProviderFactory.getInstance().getAddressingProvider(var0);
   }

   public static void validateAction(WlMessageContext var0, boolean var1) {
      String var2 = getAddressingWSDLActionURI(var0, var1);
      if (var2 != null) {
         MsgHeaders var3 = var0.getHeaders();
         ActionHeader var4 = (ActionHeader)var3.getHeader(ActionHeader.TYPE);
         if (var4 != null) {
            String var5 = var4.getActionURI();
            if (var5 != null && var5.length() != 0) {
               if (!var2.equals(var5)) {
                  var0.setProperty("weblogic.wsee.addressing.server.hasexception", "true");
                  String var6 = " Action Header in SOAP message is \"" + var5 + "\", but Action defined in WSDL is \"" + var2 + "\".";
                  throw new AddressingHeaderException("The action is not supported in this endpoint, or it is mismatched with the action defined in WSDL." + var6, "Action", new QName[]{getAddressingProvider(var0).getActionNotSupportFaultQName()});
               }
            }
         }
      }
   }

   public static void checkFaultAction(WlMessageContext var0) {
      ActionHeader var1 = (ActionHeader)var0.getHeaders().getHeader(ActionHeader.TYPE);
      if (var1 != null) {
         if (getAddressingProvider(var0).getFaultActionUri().equals(var1.getActionURI()) || "http://www.w3.org/2005/08/addressing/soap/fault".equals(var1.getActionURI())) {
            throw SOAPFaultUtil.newWLSOAPFaultException(var0, (String)"Client", "Sender", "Client has sent a WS-Addressing fault message, it couldn't been processed by server side! (Fault action: " + var1.getActionURI() + ")", (String)null, (Throwable)null);
         }
      }
   }

   public static String getAddressingWSDLActionURI(WlMessageContext var0, boolean var1) {
      if (var0.getDispatcher().getOperation() == null) {
         return null;
      } else {
         String var2 = null;
         if (var1) {
            var2 = var0.getDispatcher().getOperation().getInputAction();
         } else {
            var2 = var0.getDispatcher().getOperation().getOutputAction();
         }

         return var2;
      }
   }
}
