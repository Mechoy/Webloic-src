package weblogic.wsee.security.wst.internal;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.connection.transport.servlet.HttpTransportUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.policy.WssPolicyUtils;

public class STSPolicyHandler extends WSTServerHandler {
   private static final String DEFAULT_BOOTSTRAP_POLICY = "SecurityTokenService.xml";
   private static final String INCLUDE_SCT_POLICY = "AddSctToken.xml";
   private static final String SCT_CANCEL = "/SCT/Cancel";
   private static final String SCT_RENEW = "/SCT/Renew";

   public boolean handleTrustRequest(SOAPMessageContext var1, String var2) {
      NormalizedExpression var4;
      NormalizedExpression var3 = var4 = NormalizedExpression.createUnitializedExpression();
      if (!isTransportSecure(var1)) {
         NormalizedExpression var5 = getPolicy(var1, "SecurityTokenService.xml");
         var3 = PolicyMath.merge(var3, var5);
         var4 = PolicyMath.merge(var4, var5);
      }

      var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var3);
      var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var4);
      return true;
   }

   private static boolean isTransportSecure(SOAPMessageContext var0) {
      HttpServletRequest var1 = HttpTransportUtils.getHttpServletRequest(var0);
      return var1 != null ? var1.isSecure() : false;
   }

   private static NormalizedExpression getPolicy(SOAPMessageContext var0, String var1) {
      try {
         WssPolicyContext var2 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
         return WssPolicyUtils.getPolicy(var2, var1);
      } catch (PolicyException var3) {
         return NormalizedExpression.createUnitializedExpression();
      }
   }
}
