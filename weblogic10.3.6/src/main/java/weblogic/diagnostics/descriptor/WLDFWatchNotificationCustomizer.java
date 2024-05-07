package weblogic.diagnostics.descriptor;

import java.util.ArrayList;
import weblogic.utils.ArrayUtils;

public class WLDFWatchNotificationCustomizer {
   private WLDFWatchNotificationBean watchNotificationBean;

   public WLDFWatchNotificationCustomizer(WLDFWatchNotificationBean var1) {
      this.watchNotificationBean = var1;
   }

   public WLDFNotificationBean[] getNotifications() {
      ArrayList var1 = new ArrayList();
      addAll(var1, this.watchNotificationBean.getImageNotifications());
      addAll(var1, this.watchNotificationBean.getJMSNotifications());
      addAll(var1, this.watchNotificationBean.getJMXNotifications());
      addAll(var1, this.watchNotificationBean.getSMTPNotifications());
      addAll(var1, this.watchNotificationBean.getSNMPNotifications());
      WLDFNotificationBean[] var2 = new WLDFNotificationBean[var1.size()];
      var1.toArray(var2);
      return var2;
   }

   public WLDFNotificationBean lookupNotification(String var1) {
      WLDFNotificationBean[] var2 = this.getNotifications();
      if (var2 != null && var2.length != 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static void addAll(ArrayList var0, WLDFNotificationBean[] var1) {
      if (var1 != null && var1.length > 0) {
         ArrayUtils.addAll(var0, var1);
      }

   }
}
