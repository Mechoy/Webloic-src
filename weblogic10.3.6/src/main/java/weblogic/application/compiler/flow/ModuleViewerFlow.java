package weblogic.application.compiler.flow;

import java.io.IOException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.compiler.WARModule;
import weblogic.application.utils.CustomModuleManager;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.compiler.ToolFailureException;

public class ModuleViewerFlow extends CompilerFlow {
   protected static boolean debug = Boolean.getBoolean("weblogic.application.compiler.viewer");

   public ModuleViewerFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      WebLogicDeployableObjectFactory var1 = this.ctx.getObjectFactory();
      if (var1 != null) {
         EARModule[] var2 = this.ctx.getModules();
         if (var2.length != 1) {
            throw new AssertionError("ModuleViewerFlow can be invoked for standalone modules only");
         }

         try {
            this.ctx.setDeployableApplication(this.createDeployableObject(var1, var2[0]));
         } catch (IOException var4) {
            throw new ToolFailureException("Unable to create deployable object", var4);
         }
      }

   }

   protected void dump(ApplicationDescriptor var1) {
      try {
         DescriptorBean var2 = (DescriptorBean)var1.getApplicationDescriptor();
         var2.getDescriptor().toXML(System.out);
         DescriptorBean var3 = (DescriptorBean)var1.getWeblogicApplicationDescriptor();
         var3.getDescriptor().toXML(System.out);
         DescriptorBean var4 = (DescriptorBean)var1.getWeblogicExtensionDescriptor();
         var4.getDescriptor().toXML(System.out);
      } catch (Exception var5) {
      }

   }

   public void cleanup() {
   }

   protected EditableDeployableObject createDeployableObject(WebLogicDeployableObjectFactory var1, EARModule var2) throws IOException {
      if (debug) {
         this.say("Adding module type " + var2.getModuleType() + " at URI " + var2.getURI());
      }

      EditableDeployableObject var3 = var1.createDeployableObject(var2.getURI(), var2.getAltDD(), var2.getModuleType());
      var3.setVirtualJarFile(var2.getVirtualJarFile());
      if (var2.getRootBean() != null) {
         var3.setRootBean(var2.getRootBean());
      }

      String[] var4 = var2.getDescriptorUris();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         DescriptorBean var6 = var2.getRootBean(var4[var5]);
         if (var6 != null) {
            if (debug) {
               this.say("Adding URI " + var4[var5]);
            }

            var3.addRootBean(var4[var5], var6, var2.getModuleType());
         }
      }

      var3.setClassLoader(var2.getModuleClassLoader());
      if (var2.getResourceFinder() != null) {
         var3.setResourceFinder(var2.getResourceFinder());
      }

      if (var2 instanceof WARModule) {
         String var9 = ((WARModule)var2).getContextRoot();
         var3.setContextRoot(var9);
         CustomModuleManager var10 = ((WARModule)var2).getCustomModuleManager();
         if (var10 != null) {
            var4 = var10.getDescriptorUris();

            for(int var7 = 0; var7 < var4.length; ++var7) {
               DescriptorBean var8 = var10.getRootBean(var4[var7]);
               var3.addRootBean(var4[var7], var8, WebLogicModuleType.CONFIG);
            }
         }
      }

      return var3;
   }
}
