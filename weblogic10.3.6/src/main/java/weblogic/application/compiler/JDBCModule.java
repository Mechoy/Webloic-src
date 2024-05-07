package weblogic.application.compiler;

import javax.enterprise.deploy.shared.ModuleType;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.jdbc.module.JDBCDeploymentHelper;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;

public class JDBCModule extends WLSModule {
   private EditableDescriptorManager edm;

   public JDBCModule(String var1, String var2) {
      super(var1, var2);
      if (this.edm == null) {
         this.edm = new EditableDescriptorManager();
      }
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      try {
         JDBCDeploymentHelper var2 = new JDBCDeploymentHelper();
         JDBCDataSourceBean var3 = null;
         if (var1.getEar() != null) {
            var3 = var2.createJDBCDataSourceDescriptor(this.edm, this.getCL(this.getURI()), var1.getConfigDir(), var1.getPlanBean(), var1.getEar().getURI(), this.getURI());
         } else {
            var3 = var2.createJDBCDataSourceDescriptor(this.edm, this.getCL(this.getURI()), var1.getConfigDir(), var1.getPlanBean(), this.getURI(), this.getURI());
         }

         if (var3 != null) {
            this.addRootBean(this.getURI(), (DescriptorBean)var3);
         }
      } catch (Exception var4) {
      }

   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      try {
         JDBCDeploymentHelper var3 = new JDBCDeploymentHelper();
         JDBCDataSourceBean var8 = var3.createJDBCDataSourceDescriptor(this.getOutputFileName());
         this.getModuleValidationInfo().setJDBCDataSourceBean(var8);
         if (var2.isVerbose() && var8 != null) {
            JDBCDeploymentHelper.writeModuleAsXML((DescriptorBean)var8);
         }

      } catch (Exception var7) {
         String var4 = null;
         Throwable var5 = var7.getCause();
         if (var5 != null) {
            var4 = var5.getMessage();
         } else {
            var4 = var7.getMessage();
         }

         Exception var6 = new Exception("Failed to compile JDBC module " + this.getOutputFileName() + ": " + var4);
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.getOutputFileName(), var6.toString()).getMessage(), var6);
      }
   }

   public ModuleType getModuleType() {
      return WebLogicModuleType.JDBC;
   }
}
