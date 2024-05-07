package weblogic.wsee.security;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.wss.SecurityPolicyDriver;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.WSSecurityFactory;
import weblogic.xml.dom.marshal.MarshalException;

public class WssServerHandler extends WssHandler implements WLHandler {
   protected boolean processRequest(SOAPMessageContext var1) throws WSSecurityException, SOAPException, SecurityPolicyException, PolicyException {
      try {
         if (!hasSecurityHeader(var1)) {
            return true;
         } else {
            this.setupSecurityContext(var1, (PolicyAlternative)null);
            WSSecurityFactory.getInstance();
            WSSecurityFactory.unmarshalAndProcessSecurity(var1);
            return true;
         }
      } catch (MarshalException var4) {
         Throwable var3 = var4.getCause();
         if (var3 instanceof WSSecurityException) {
            throw (WSSecurityException)var3;
         } else if (var3 instanceof SOAPException) {
            throw (SOAPException)var3;
         } else {
            throw new WSSecurityException(var4, WSSConstants.FAILURE_INVALID);
         }
      }
   }

   protected boolean processResponse(SOAPMessageContext var1) throws WSSecurityException, SecurityPolicyException, SOAPException, PolicyException {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (var2.hasFault() || (var2.getProperty("weblogic.wsee.queued.invoke") != null || var2.getProperty("weblogic.wsee.enable.rm") != null && var2.getProperty("weblogic.wsee.async.res") == null) && var2.getProperty("weblogic.wsee.reliable.oneway.reply") == null) {
         return true;
      } else {
         PolicyAlternative var3 = getResponsePolicyAlternative(var1);
         SecurityPolicyDriver var4 = this.getSecurityPolicyDriver(var1, (PolicyAlternative)null);

         try {
            processOutbound(var3, var4, var1);
            return true;
         } catch (weblogic.xml.crypto.api.MarshalException var6) {
            throw new WSSecurityException(var6);
         } catch (XMLEncryptionException var7) {
            throw new WSSecurityException(var7);
         }
      }
   }

   protected static void processOutbound(PolicyAlternative var0, SecurityPolicyDriver var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, weblogic.xml.crypto.api.MarshalException, XMLEncryptionException {
      if (var0 != null) {
         WSSecurityContext.getSecurityContext(var2).reset();
         var1.processOutbound(var0, var2);
      }
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2, PolicyAlternative var3) throws WSSecurityException {
      super.fillCredentialProviders(var1, var2, var3);
   }

   public boolean handleClosure(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      return var2.getProperty("weblogic.wsee.reliable.oneway.reply") != null ? this.handleResponse(var1) : true;
   }
}
