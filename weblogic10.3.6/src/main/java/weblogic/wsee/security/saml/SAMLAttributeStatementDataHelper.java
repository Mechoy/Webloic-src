package weblogic.wsee.security.saml;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.jws.JwsContext;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class SAMLAttributeStatementDataHelper {
   public static SAMLAttributeStatementData getSAMLAttributeStatementData(WebServiceContext var0) {
      Packet var1 = ((WSWebServiceContext)var0).getRequestPacket();
      WSSecurityContext var2 = (WSSecurityContext)var1.invocationProperties.get("weblogic.xml.crypto.wss.WSSecurityContext");
      SAMLAttributeStatementData var3 = null;
      if (var2 != null && var2.getMessageContext() != null) {
         var3 = (SAMLAttributeStatementData)var2.getMessageContext().getProperty("weblogic.wsee.security.saml.attributies");
      }

      return var3;
   }

   public static SAMLAttributeStatementData getSAMLAttributeStatementData(JwsContext var0) {
      MessageContext var1 = var0.getMessageContext();
      SAMLAttributeStatementData var2 = (SAMLAttributeStatementData)var1.getProperty("weblogic.wsee.security.saml.attributies");
      return var2;
   }
}
