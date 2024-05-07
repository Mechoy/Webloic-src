package weblogic.wsee.security.policy;

import java.io.Serializable;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfiguration;

public class WssPolicyContext implements Serializable {
   private static final long serialVersionUID = -1405648915117924659L;
   public static final String WSS_POLICY_CTX_PROP = "weblogic.weblogic.wsee.security.policy.WssPolicyCtx";
   private String wssConfigMBeanName;
   private boolean isServer;
   private transient PolicyServer ps;
   private transient WssConfiguration wssConfig;

   public WssPolicyContext() {
      this.init(this.wssConfigMBeanName, true, true);
   }

   public WssPolicyContext(String var1) {
      this.init(var1, true, true);
   }

   public WssPolicyContext(boolean var1) {
      this.init((String)null, var1, true);
   }

   public WssPolicyContext(String var1, boolean var2) {
      this.init(var1, var2, true);
   }

   public WssPolicyContext(String var1, boolean var2, boolean var3) {
      this.init(var1, var2, var3);
   }

   private void init(String var1, boolean var2, boolean var3) {
      this.wssConfigMBeanName = var1;
      this.isServer = var2;
      this.ps = new PolicyServer();
      this.wssConfig = new WssConfiguration(this.wssConfigMBeanName, var2);
      if (var3) {
         SecurityPolicyCustomizer var4 = new SecurityPolicyCustomizer(this.wssConfig);
         this.ps.addPolicyCustomizer(var4);
      }

   }

   public PolicyServer getPolicyServer() {
      if (this.ps == null) {
         this.init(this.wssConfigMBeanName, this.isServer, true);
      }

      return this.ps;
   }

   public WssConfiguration getWssConfiguration() {
      if (this.wssConfig == null) {
         this.init(this.wssConfigMBeanName, this.isServer, true);
      }

      return this.wssConfig;
   }
}
