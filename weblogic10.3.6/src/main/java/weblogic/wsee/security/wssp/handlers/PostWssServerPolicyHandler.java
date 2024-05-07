package weblogic.wsee.security.wssp.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.monitoring.WsspStats;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.plan.SecurityPolicyPlan;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class PostWssServerPolicyHandler extends WssHandler {
   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      try {
         WlMessageContext var2 = (WlMessageContext)var1;
         if (var2 != null && var2.getDispatcher() != null && var2.getDispatcher().getOperation() != null) {
            var2.setProperty("WL_OP_NAME", var2.getDispatcher().getOperationName().getLocalPart());
         }

         NormalizedExpression var3 = PolicyContext.getRequestEffectivePolicy(var1);
         if (var3 == null || null == var3.getPolicyAlternatives()) {
            return true;
         }

         this.postValidate(var3, var1, true);
         WSSecurityContext var4 = WSSecurityContext.getSecurityContext(var1);
         AuthenticatedSubject var5 = var4.getSubject();
         if (var5 != null) {
            this.setSubject(var5, var1);
         }

         this.setupMTOMProperty(var1);
      } catch (MarshalException var6) {
         throw new WSSecurityException(var6);
      } catch (SecurityPolicyException var7) {
         throw new WSSecurityException(var7);
      } catch (XMLEncryptionException var8) {
         throw new WSSecurityException(var8);
      }

      this.reportInboundWSSSuccessToWsspStats(this.getWsspStats(var1), var1);
      return true;
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      return true;
   }

   private void setSubject(AuthenticatedSubject var1, SOAPMessageContext var2) {
      var2.setProperty("weblogic.wsee.wss.subject", var1);
   }

   private void setupMTOMProperty(SOAPMessageContext var1) throws PolicyException {
      NormalizedExpression var2 = PolicyContext.getRequestEffectivePolicy(var1);
      if (var2 != null && null != var2.getPolicyAlternatives()) {
         Set var3 = var2.getPolicyAlternatives();
         if (var3 == null) {
            return;
         }

         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            PolicyAlternative var5 = (PolicyAlternative)var4.next();
            SecurityPolicyAssertionInfo var6 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var5);
            if (var6 != null) {
               List var7 = var6.getIntegrityAssertions();
               List var8 = var6.getConfidentialityAssertions();
               if (var7 != null) {
                  var1.setProperty("weblogic.wsee.xop.normal", "sign");
               }

               if (var8 != null && var8.size() > 0) {
                  var1.setProperty("weblogic.wsee.xop.normal", "encrypt");
                  break;
               }
            }
         }
      }

   }

   protected void reportInboundWSSSuccessToWsspStats(WsspStats var1, SOAPMessageContext var2) {
      super.reportInboundWSSSuccessToWsspStats(this.getWsspStats(var2), var2);

      assert var1 != null;

      WSS11Context var3 = (WSS11Context)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");

      assert var3 != null;

      SecurityPolicyPlan var4 = (SecurityPolicyPlan)var3.getPolicyOutline();
      if (var4 != null && var4.getBuildingPlan() > 0 && (var4.getIdentityPolicy() != null && var4.getIdentityPolicy().isAuthenticationRequired() || var3.getIdTokens().size() > 0)) {
         var1.reportAuthenticationSuccess();
      }

   }
}
