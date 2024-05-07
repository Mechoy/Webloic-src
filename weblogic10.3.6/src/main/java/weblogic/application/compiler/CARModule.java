package weblogic.application.compiler;

import java.io.File;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2eeclient.ApplicationClientDescriptor;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.compiler.ToolFailureException;

public class CARModule extends EARModule {
   MultiClassFinder moduleClassFinder = new MultiClassFinder();

   public CARModule(String var1, String var2) {
      super(var1, var2);
   }

   public String getDescriptorURI() {
      return "META-INF/application-client.xml";
   }

   public ClassFinder getClassFinder() {
      return this.moduleClassFinder;
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      ApplicationClientDescriptor var2 = new ApplicationClientDescriptor(this.getVirtualJarFile(), (File)null, (DeploymentPlanBean)null, (String)null);

      try {
         if (var2.getApplicationClientBean() != null) {
            this.addRootBean("META-INF/application-client.xml", (DescriptorBean)var2.getApplicationClientBean());
         }

         if (var2.getWeblogicApplicationClientBean() != null) {
            this.addRootBean("META-INF/weblogic-application.xml", (DescriptorBean)var2.getWeblogicApplicationClientBean());
         }
      } catch (Exception var4) {
      }

   }

   public DescriptorBean getRootBean() {
      return this.getRootBean("META-INF/application-client.xml");
   }

   public ModuleType getModuleType() {
      return ModuleType.CAR;
   }
}
