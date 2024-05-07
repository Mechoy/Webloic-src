package weblogic.application.compiler;

import java.io.File;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.connector.configuration.ConnectorDescriptor;
import weblogic.connector.external.RAComplianceException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EELogger;
import weblogic.kernel.Kernel;
import weblogic.utils.BadOptionException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.compiler.ToolFailureException;

public class RARModule extends EARModule {
   MultiClassFinder moduleClassFinder = new MultiClassFinder();

   public RARModule(String var1, String var2) {
      super(var1, var2);
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      ClasspathClassFinder2 var3 = null;
      GenericClassLoader var4 = null;

      try {
         var3 = new ClasspathClassFinder2(this.getOutputDir().getPath());
         var4 = new GenericClassLoader(var3, var1);

         try {
            var2.getOpts().setOption("classpath", var4.getClassPath());
         } catch (BadOptionException var12) {
            throw new AssertionError(var12);
         }

         if (var2.isVerbose() && !Kernel.isServer()) {
            System.setProperty("weblogic.debug.DebugRACompliance", "true");
         }

         try {
            AppcUtils.compileRAR(var4, this.getVirtualJarFile(), this.getAltDDFile(), var2.getConfigDir(), var2.getPlanBean(), this.getModuleValidationInfo(), var2);
         } catch (RAComplianceException var11) {
            throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.getURI(), var11.toString()).getMessage(), var11);
         }
      } finally {
         if (var3 != null) {
            var3.close();
         }

         if (var4 != null) {
            var4.close();
         }

      }

   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      String var2 = this.getURI();
      ConnectorDescriptor var3 = new ConnectorDescriptor(this.getAltDDFile(), this.getVirtualJarFile(), var1.getConfigDir(), var1.getPlanBean(), var2);

      try {
         if (var3.getConnectorBean() != null) {
            this.addRootBean("META-INF/ra.xml", (DescriptorBean)var3.getConnectorBean());
         }

         if (var3.getWeblogicConnectorBean() != null) {
            this.addRootBean("META-INF/weblogic-ra.xml", (DescriptorBean)var3.getWeblogicConnectorBean());
         }

         this.addDiagnosticDDRootBean(var1);
      } catch (Exception var5) {
      }

   }

   public ClassFinder getClassFinder() {
      return this.moduleClassFinder;
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      this.moduleClassFinder.addFinder(new ClasspathClassFinder2(this.getOutputDir().getPath()));
      if (this.isSplitDir(var1)) {
         File[] var3 = var1.getEar().getModuleRoots(this.getURI());

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.moduleClassFinder.addFinder(new ClasspathClassFinder2(var3[var4].getAbsolutePath()));
         }
      }

      super.initModuleClassLoader(var1, var2);
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
   }

   public DescriptorBean getRootBean() {
      return this.getRootBean("META-INF/ra.xml");
   }

   public ModuleType getModuleType() {
      return ModuleType.RAR;
   }
}
