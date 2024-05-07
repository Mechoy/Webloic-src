package weblogic.application.internal.flow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weblogic.application.Module;
import weblogic.application.ParentModule;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.IOUtils;
import weblogic.management.DeploymentException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class SingleModuleContainerFlow extends ModuleFilterFlow implements Flow {
   public SingleModuleContainerFlow(FlowContext var1, Module var2) throws DeploymentException {
      super(var1);
      ArrayList var3 = new ArrayList(1);

      try {
         if (var2 instanceof ParentModule) {
            VirtualJarFile var4 = null;

            try {
               var4 = VirtualJarFactory.createVirtualJar(new File(var1.getStagingPath()));
               var3.add(new ScopedModuleDriver(var2, var1, this.getModuleUri(), var4, ((ParentModule)var2).getDescriptorURI()));
            } finally {
               IOUtils.forceClose(var4);
            }
         } else {
            var3.add(var2);
         }

         var1.setApplicationModules(this.createWrappedModules(var3));
      } catch (IOException var10) {
         throw new DeploymentException(var10);
      }
   }

   private String getModuleUri() {
      return this.appCtx.getApplicationMBean() != null ? this.appCtx.getApplicationMBean().getComponents()[0].getURI() : this.appCtx.getSystemResourceMBean().getDescriptorFileName();
   }
}
