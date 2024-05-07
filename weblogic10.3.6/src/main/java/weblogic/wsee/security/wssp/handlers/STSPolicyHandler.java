package weblogic.wsee.security.wssp.handlers;

import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wst.internal.WSTServerHandler;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsService;

public class STSPolicyHandler extends WSTServerHandler {
   private static final boolean verbose = Verbose.isVerbose(STSPolicyHandler.class);
   public static final String BOOT_STRAP_POLICY = "BootstrapPolicy";
   public static final String SERVICE_POLICY = "ServicePolicy";
   private static final String SCT_CANCEL = "/SCT/Cancel";
   private static final String SCT_RENEW = "/SCT/Renew";
   private NormalizedExpression bootstrapPolicy;
   private NormalizedExpression servicePolicy;
   private boolean isTransportSecurityRequired;

   public void init(HandlerInfo var1) {
      this.bootstrapPolicy = (NormalizedExpression)var1.getHandlerConfig().get("BootstrapPolicy");
      this.isTransportSecurityRequired = SecurityPolicyAssertionInfoFactory.hasTransportSecurityPolicy(this.bootstrapPolicy);
      this.servicePolicy = (NormalizedExpression)var1.getHandlerConfig().get("ServicePolicy");
   }

   public boolean handleTrustRequest(SOAPMessageContext var1, String var2) {
      // $FF: Couldn't be decompiled
   }

   private static boolean isTransportSecure(SOAPMessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      Object var2 = var1.getProperty("weblogic.wsee.transport.servlet.request.secure");
      return var2 instanceof Boolean ? (Boolean)var2 : false;
   }

   private WssPolicyContext getPolicyContext(SOAPMessageContext var1) {
      WssPolicyContext var2 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var2 == null) {
         WlMessageContext var3 = WlMessageContext.narrow(var1);
         WsService var4 = var3.getDispatcher().getWsPort().getEndpoint().getService();
         var2 = var4.getWssPolicyContext();
         var1.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var2);
      }

      return var2;
   }
}
