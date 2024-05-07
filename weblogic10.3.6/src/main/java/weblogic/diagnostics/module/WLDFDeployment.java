package weblogic.diagnostics.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.WLDFSystemResourceMBean;

public class WLDFDeployment extends SingleModuleDeployment implements Deployment {
   WLDFDeployment(WLDFSystemResourceMBean var1, File var2) throws DeploymentException {
      super((SystemResourceMBean)var1, createModule(var1), var2);
   }

   private static WLDFModule createModule(WLDFSystemResourceMBean var0) {
      String var1 = var0.getDescriptorFileName();
      return new WLDFModule(var1);
   }
}
