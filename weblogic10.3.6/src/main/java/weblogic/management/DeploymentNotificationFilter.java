package weblogic.management;

import java.io.Serializable;
import javax.management.Notification;
import javax.management.NotificationFilter;

public class DeploymentNotificationFilter implements NotificationFilter, Serializable {
   private static final long serialVersionUID = 1L;
   private String target = null;
   private String module = null;
   private boolean appsOK = true;
   private boolean modulesOK = true;

   public DeploymentNotificationFilter() {
   }

   public DeploymentNotificationFilter(String var1, String var2, boolean var3, boolean var4) {
      this.target = var1;
      this.module = var2;
      this.appsOK = var3;
      this.modulesOK = var4;
   }

   public boolean isNotificationEnabled(Notification var1) {
      boolean var2 = false;
      if (var1 instanceof DeploymentNotification) {
         var2 = true;
         DeploymentNotification var3 = (DeploymentNotification)var1;
         String var4 = var3.getServerName();
         if (this.target != null && !this.target.equals(var4)) {
            var2 = false;
         }

         String var5 = var3.getModuleName();
         if (var2 && this.module != null && var3.isModuleNotification() && !this.module.equals(var5)) {
            var2 = false;
         }

         var2 = var2 && (this.appsOK && var3.isAppNotification() || this.modulesOK && var3.isModuleNotification());
      }

      return var2;
   }
}
