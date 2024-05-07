package weblogic.application.internal.flow;

import java.util.ArrayList;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.internal.Flow;

public final class HeadModuleRedeployFlow extends BaseFlow implements Flow {
   public HeadModuleRedeployFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void stop(String[] var1) {
      Module[] var2 = this.appCtx.getStoppingModules();
      this.appCtx.setStoppingModules(new Module[0]);
      Module[] var3 = this.appCtx.getApplicationModules();
      ArrayList var4 = new ArrayList(var3.length - var2.length);
      int var5 = 0;

      for(int var6 = 0; var6 < var3.length; ++var6) {
         if (var5 < var2.length && var2[var5].getId().equals(var3[var6].getId())) {
            ++var5;
         } else {
            var4.add(var3[var6]);
         }
      }

      this.appCtx.setApplicationModules((Module[])((Module[])var4.toArray(new Module[var4.size()])));
   }
}
