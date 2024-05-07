package weblogic.wsee.ws.init;

import java.util.Collections;
import java.util.Map;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;

public class WsDeploymentListenerConfig {
   private final Class deploymentListenerClass;
   private final Map<String, String> initParams;

   public WsDeploymentListenerConfig(Class var1) {
      this(var1, Collections.emptyMap());
   }

   public WsDeploymentListenerConfig(Class var1, Map<String, String> var2) {
      assert var1 != null : "no deployment listener class specified";

      assert var2 != null : "No initialization parameter.  Use other constructor";

      if (!var2.isEmpty() && !Configurable.class.isAssignableFrom(var1)) {
         throw new IllegalArgumentException(var1.getName() + " cannot accept initialization parameters");
      } else {
         this.deploymentListenerClass = var1;
         this.initParams = var2;
      }
   }

   WsDeploymentListener newInstance() {
      try {
         WsDeploymentListener var1 = (WsDeploymentListener)this.deploymentListenerClass.newInstance();
         if (var1 instanceof Configurable) {
            ((Configurable)var1).init(this.initParams);
         }

         return var1;
      } catch (InstantiationException var2) {
         throw new IllegalArgumentException("Unable to construct instance of " + this.deploymentListenerClass.getName(), var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalArgumentException("Unable to construct instance of " + this.deploymentListenerClass.getName(), var3);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof WsDeploymentListenerConfig)) {
         return false;
      } else {
         WsDeploymentListenerConfig var2 = (WsDeploymentListenerConfig)var1;
         boolean var3 = this.deploymentListenerClass == var2.deploymentListenerClass;
         var3 = var3 && ObjectUtil.equals(this.initParams, var2.initParams);
         return var3;
      }
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.deploymentListenerClass);
      var1.add(this.initParams);
      return var1.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.deploymentListenerClass.getName());
      if (this.initParams != null && !this.initParams.isEmpty()) {
         var1.append(this.initParams);
      }

      return var1.toString();
   }
}
