package weblogic.jms.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class JMSDeployment extends SingleModuleDeployment implements Deployment {
   public JMSDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   public JMSDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   private static Module createModule(AppDeploymentMBean var0) throws DeploymentException {
      if (var0 == null) {
         throw new DeploymentException("AppDepoymentMBean cannot be null. Please check the server log related error messages");
      } else {
         SubDeploymentMBean[] var1 = var0.getSubDeployments();
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               SubDeploymentMBean[] var3 = var1[var2].getSubDeployments();
               if (var3 != null && var3.length > 0) {
                  throw new DeploymentException("Application " + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " is a not a stand alone JMS deployment, it contains nested sub deployments.");
               }
            }
         }

         return new JMSModule(var0.getSourcePath());
      }
   }

   private static Module createModule(SystemResourceMBean var0) throws DeploymentException {
      if (var0 == null) {
         throw new DeploymentException("SystemResourceMBean cannot be null. Please check the server log related error messages");
      } else {
         String var1 = var0.getDescriptorFileName();
         if (var1 == null) {
            throw new DeploymentException("JMSSystemResource " + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " does not have a descriptor file name");
         } else {
            return new JMSModule(var1);
         }
      }
   }
}
