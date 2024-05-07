package weblogic.application.internal.flow;

import java.util.Map;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.MergedDescriptorModule;
import weblogic.application.Module;
import weblogic.application.internal.Flow;
import weblogic.application.io.MergedDescriptorFinder;
import weblogic.management.DeploymentException;
import weblogic.utils.classloaders.GenericClassLoader;

public class DescriptorFinderFlow extends BaseFlow implements Flow {
   public DescriptorFinderFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      Module[] var1 = this.appCtx.getApplicationModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof MergedDescriptorModule) {
            MergedDescriptorModule var3 = (MergedDescriptorModule)var1[var2];
            Map var4 = var3.getDescriptorMappings();
            if (var4 != null && var4.size() > 0) {
               GenericClassLoader var5 = ApplicationAccess.getApplicationAccess().findModuleLoader(this.appCtx.getApplicationId(), var1[var2].getId());
               MergedDescriptorFinder var6 = new MergedDescriptorFinder(var4);
               var5.addClassFinderFirst(var6);
               var3.handleMergedFinder(var6);
            }
         }
      }

   }
}
