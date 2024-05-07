package weblogic.jdbc.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class JDBCDeployment extends SingleModuleDeployment implements Deployment {
   public JDBCDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   public JDBCDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   private static Module createModule(AppDeploymentMBean var0) throws DeploymentException {
      ComponentMBean[] var1 = var0.getAppMBean().getComponents();
      if (var1 != null && var1.length != 0) {
         if (var1.length > 1) {
            throw new DeploymentException("Application" + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " is a DAR file, but it contains > 1 component.");
         } else {
            return new JDBCModule(var1[0].getURI());
         }
      } else {
         throw new DeploymentException("Application " + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " does not have any Components in it.");
      }
   }

   private static Module createModule(SystemResourceMBean var0) throws DeploymentException {
      String var1 = var0.getDescriptorFileName();
      if (var1 == null) {
         throw new DeploymentException("JDBCSystemResource " + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " does not have a descriptor file name");
      } else {
         return new JDBCModule(var1);
      }
   }
}
