package weblogic.deploy.api.spi.deploy;

import java.io.Serializable;
import javax.enterprise.deploy.spi.DeploymentManager;
import weblogic.deploy.api.shared.WebLogicTargetType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTarget;

public class TargetImpl extends WebLogicTarget implements Serializable {
   private static final long serialVersionUID = 1L;
   private String name;
   private transient WebLogicDeploymentManager manager;

   public TargetImpl(String var1, int var2, DeploymentManager var3) {
      super(var2);
      this.name = var1;
      this.manager = (WebLogicDeploymentManager)var3;
   }

   public TargetImpl(String var1, WebLogicTargetType var2, DeploymentManager var3) {
      this(var1, var2.getValue(), var3);
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return super.toString();
   }

   public String toString() {
      return this.getName().concat("/").concat(this.getDescription());
   }

   public WebLogicDeploymentManager getManager() {
      return this.manager;
   }

   public boolean isServer() {
      return this.getValue() == WebLogicTargetType.SERVER.getValue();
   }

   public boolean isCluster() {
      return this.getValue() == WebLogicTargetType.CLUSTER.getValue();
   }

   public boolean isVirtualHost() {
      return this.getValue() == WebLogicTargetType.VIRTUALHOST.getValue();
   }

   public boolean isJMSServer() {
      return this.getValue() == WebLogicTargetType.JMSSERVER.getValue();
   }

   public boolean isSAFAgent() {
      return this.getValue() == WebLogicTargetType.SAFAGENT.getValue();
   }
}
