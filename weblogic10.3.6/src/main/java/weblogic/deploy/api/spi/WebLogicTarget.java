package weblogic.deploy.api.spi;

import javax.enterprise.deploy.spi.Target;
import weblogic.deploy.api.shared.WebLogicTargetType;

public abstract class WebLogicTarget extends WebLogicTargetType implements Target {
   protected WebLogicTarget(int var1) {
      super(var1);
   }

   public abstract String getName();

   public abstract String getDescription();

   public abstract boolean isServer();

   public abstract boolean isCluster();

   public abstract boolean isVirtualHost();

   public abstract boolean isJMSServer();

   public abstract boolean isSAFAgent();

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else {
         if (var1 instanceof WebLogicTarget) {
            WebLogicTarget var2 = (WebLogicTarget)var1;
            if (this.hashCode() == var2.hashCode()) {
               return true;
            }
         }

         return false;
      }
   }

   public int hashCode() {
      return this.getName().concat(this.getDescription()).hashCode();
   }
}
