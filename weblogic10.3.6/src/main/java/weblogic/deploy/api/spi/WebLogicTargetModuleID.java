package weblogic.deploy.api.spi;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.shared.WebLogicModuleType;

public abstract class WebLogicTargetModuleID extends WebLogicModuleType implements TargetModuleID {
   private WebLogicTargetModuleID() {
      super(0);

      assert false : "invalid use of constructor";

   }

   protected WebLogicTargetModuleID(int var1) {
      super(var1);
   }

   public abstract String getModuleID();

   public abstract String getApplicationName();

   public abstract String getArchiveVersion();

   public abstract String getPlanVersion();

   public abstract String getVersion();

   public abstract Target[] getServers() throws IllegalStateException;

   public abstract boolean isOnVirtualHost();

   public abstract boolean isOnServer();

   public abstract boolean isOnCluster();

   public abstract boolean isOnJMSServer();

   public abstract boolean isOnSAFAgent();

   public abstract boolean isTargeted();

   public abstract void setTargeted(boolean var1);
}
