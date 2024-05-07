package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInException;
import weblogic.upgrade.PluginActionDelegate;
import weblogic.upgrade.UpgradeHelper;

abstract class BaseFileRestructurePlugin extends PluginActionDelegate {
   public final boolean DEBUG = false;

   public BaseFileRestructurePlugin() {
   }

   void updateStatus(String var1) {
      this.log(UpgradeHelper.i18n(var1));
   }

   void updateStatus(String var1, Object var2) {
      this.log(UpgradeHelper.i18n(var1, var2));
   }

   void updateStatus(String var1, Object[] var2) {
      this.log(UpgradeHelper.i18n(var1, var2));
   }

   PlugInException createException(String var1) {
      String var2 = UpgradeHelper.i18n(var1);
      this.log(var2);
      return new PlugInException(this.getName(), var2);
   }

   PlugInException createException(String var1, Object var2) {
      String var3 = UpgradeHelper.i18n(var1, var2);
      this.log(var3);
      return new PlugInException(this.getName(), var3);
   }

   PlugInException createException(String var1, Throwable var2) {
      StringBuffer var3 = new StringBuffer();

      for(Throwable var4 = var2; var4 != null; var4 = var4.getCause()) {
         var3.append(": ");
         var3.append(var4.toString());
      }

      String var5 = UpgradeHelper.i18n(var1, (Object)var3.toString());
      this.log(var5);
      return new PlugInException(this.getName(), var5);
   }

   public void log(Object var1) {
      if (var1 != null) {
         super.log(var1);
      }

   }
}
