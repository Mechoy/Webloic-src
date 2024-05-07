package weblogic.wsee.security;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.SecurityPolicyValidator;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;

public class WssServerPolicyHandler extends WssServerHandler {
   public boolean processRequest(SOAPMessageContext var1) throws WSSecurityException, SOAPException, SecurityPolicyException, PolicyException {
      PolicyAlternative var2 = getRequestPolicyAlternative(var1);
      SecurityPolicyValidator var3 = this.getSecurityPolicyValidator(var1);

      try {
         this.processInbound(var2, var3, var1);
         return true;
      } catch (XMLEncryptionException var5) {
         throw new WSSecurityException(var5);
      } catch (MarshalException var6) {
         throw new WSSecurityException(var6);
      }
   }

   public boolean processResponse(SOAPMessageContext var1) throws WSSecurityException, SecurityPolicyException, SOAPException, PolicyException {
      return true;
   }

   protected void processInbound(PolicyAlternative var1, SecurityPolicyValidator var2, SOAPMessageContext var3) throws WSSecurityException, SOAPException, SecurityPolicyException, XMLEncryptionException, MarshalException, PolicyException {
      if (var1 != null) {
         var2.processInbound(var1, var3);
      }

      WSSecurityContext var4 = WSSecurityContext.getSecurityContext(var3);
      AuthenticatedSubject var5 = var4.getSubject();
      if (var5 != null) {
         this.setSubject(var5, var3);
      }

   }

   private void setSubject(AuthenticatedSubject var1, SOAPMessageContext var2) {
      var2.setProperty("weblogic.wsee.wss.subject", var1);
   }
}
