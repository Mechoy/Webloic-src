package weblogic.wsee.security.wssp.handlers;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.security.wss.SecurityPolicyArchitect;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.crypto.wss11.internal.WSS11Factory;
import weblogic.xml.dom.marshal.MarshalException;

public class WssServerHandler extends WssHandler implements WLHandler {
   private static final boolean verbose = Verbose.isVerbose(WssServerHandler.class);

   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      try {
         if (!hasSecurityHeader(var1)) {
            return true;
         }

         this.setupSecurityContext(var1);

         try {
            var1.setProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration", "true");
            WSS11Factory.getInstance();
            WSS11Factory.unmarshalAndProcessSecurity(var1);
         } finally {
            var1.setProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration", "false");
         }
      } catch (MarshalException var7) {
         Throwable var3 = var7.getCause();
         if (var3 instanceof WSSecurityException) {
            throw (WSSecurityException)var3;
         }

         if (var3 instanceof SOAPException) {
            throw (SOAPException)var3;
         }

         throw new WSSecurityException(var7, WSSConstants.FAILURE_INVALID);
      }

      WSS11Context var2 = (WSS11Context)WSSecurityContext.getSecurityContext(var1);
      String[] var8 = var2.getSignatureValues();
      var2.addPreviousMessageSignatureValues(var8);
      return true;
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException, SecurityPolicyException {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (!var2.hasFault() && (var2.getProperty("weblogic.wsee.queued.invoke") == null && (var2.getProperty("weblogic.wsee.enable.rm") == null || var2.getProperty("weblogic.wsee.async.res") != null) || var2.getProperty("weblogic.wsee.reliable.oneway.reply") != null)) {
         NormalizedExpression var3 = PolicyContext.getResponseEffectivePolicy(var1);
         if (var3 != null && var3.getPolicyAlternatives() != null) {
            SecurityPolicyArchitect var4 = this.getSecurityPolicyDriver(var1);

            try {
               processOutbound(var3, var4, var1);
            } catch (weblogic.xml.crypto.api.MarshalException var6) {
               throw new WSSecurityException(var6);
            } catch (XMLEncryptionException var7) {
               throw new WSSecurityException(var7);
            }

            this.reportOutboundWSSSuccessToWsspStats(this.getWsspStats(var1), var1);
            return true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   protected static void processOutbound(NormalizedExpression var0, SecurityPolicyArchitect var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, weblogic.xml.crypto.api.MarshalException, XMLEncryptionException {
      WSS11Context var3 = (WSS11Context)WSSecurityContext.getSecurityContext(var2);
      int var4 = var3.getRequestPolicyIdx();
      var3.reset();
      var3.setRequestPolicyIdx(var4);
      var1.processResponseOutbound(var0, var2);
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2) throws WSSecurityException {
      super.fillCredentialProviders(var1, var2);
   }

   public boolean handleClosure(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      return var2.getProperty("weblogic.wsee.reliable.oneway.reply") != null ? this.handleResponse(var1) : true;
   }
}
