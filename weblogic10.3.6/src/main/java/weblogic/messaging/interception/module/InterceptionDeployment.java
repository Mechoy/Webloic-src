package weblogic.messaging.interception.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ComponentMBean;

public final class InterceptionDeployment extends SingleModuleDeployment implements Deployment {
   public InterceptionDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   private static Module createModule(AppDeploymentMBean var0) throws DeploymentException {
      ComponentMBean[] var1 = var0.getAppMBean().getComponents();
      StringBuffer var2 = new StringBuffer();
      var2.append("Components: ");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(var1[var3].getName());
         if (var3 < var1.length - 1) {
            var2.append(",");
         }
      }

      if (var1 != null && var1.length != 0) {
         if (var1.length > 1) {
            throw new DeploymentException("Application" + var0.getName() + " is a MAR file, but it contains > 1 component.");
         } else {
            return new InterceptionModule(var1[0].getURI());
         }
      } else {
         throw new DeploymentException("Application " + var0.getName() + " does not have any Components in it.");
      }
   }
}
