package weblogic.deploy.service.internal.targetserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentRequest;

public class DeploymentContextImpl implements DeploymentContext {
   private final TargetRequestImpl deploymentRequest;
   private HashMap contextComponents = new HashMap();
   private boolean requiresRestart;

   public DeploymentContextImpl(TargetRequestImpl var1) {
      this.deploymentRequest = var1;
   }

   public DeploymentRequest getDeploymentRequest() {
      return this.deploymentRequest;
   }

   public InputStream getDataAsStream(String var1, String var2, Deployment var3, String var4) throws IOException {
      return null;
   }

   public final boolean isRestartRequired() {
      return this.requiresRestart;
   }

   public final void setRestartRequired(boolean var1) {
      this.requiresRestart = var1;
   }

   public void addContextComponent(String var1, Object var2) {
      if (this.isDebugEnabled()) {
         this.debug(" +++ adding contextId entry ('" + var1 + ",'" + var2 + "')");
      }

      this.contextComponents.put(var1, var2);
   }

   public Object getContextComponent(String var1) {
      Object var2 = this.contextComponents.get(var1);
      if (var2 == null) {
         if (this.isDebugEnabled()) {
            this.debug(" +++ contextObject is NULL");
         }
      } else if (this.isDebugEnabled()) {
         this.debug(" +++ contextObject of type:" + var2.getClass().getName() + " = " + var2);
      }

      return var2;
   }

   public Map getContextComponents() {
      return (Map)this.contextComponents.clone();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(");
      var1.append("request='").append(this.deploymentRequest.toString()).append("',");
      var1.append("contextComponents='").append(this.contextComponents).append("',");
      var1.append("requiresRestart='").append(this.requiresRestart).append("')");
      return var1.toString();
   }

   private final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }
}
