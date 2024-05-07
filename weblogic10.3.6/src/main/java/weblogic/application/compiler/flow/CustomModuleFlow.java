package weblogic.application.compiler.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import weblogic.application.CustomModuleFactory;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.utils.compiler.ToolFailureException;

public class CustomModuleFlow extends CompilerFlow {
   public CustomModuleFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.ctx.setCustomModules(this.initCustomModules());
   }

   public void cleanup() {
      EARModule[] var1 = this.ctx.getCustomModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].cleanup();
      }

   }

   private EARModule[] initCustomModules() throws ToolFailureException {
      ArrayList var1 = new ArrayList();
      WeblogicExtensionBean var2 = this.ctx.getWLExtensionDD();
      if (var2 != null) {
         this.initCustomModules(var2.getCustomModules(), var1, this.ctx.getCustomModuleFactories());
      }

      return (EARModule[])((EARModule[])var1.toArray(new EARModule[0]));
   }

   private void initCustomModules(CustomModuleBean[] var1, List var2, Map var3) throws ToolFailureException {
      if (var1 != null && var1.length != 0) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            CustomModuleFactory var5 = (CustomModuleFactory)var3.get(var1[var4].getProviderName());
            if (var5 == null) {
               throw new ToolFailureException("The custom module with the uri " + var1[var4].getUri() + " specified a provider-name of " + var1[var4].getProviderName() + ". However, there was no module-provider " + "with this name in your weblogic-extension.xml.");
            }

            EARModule var6 = var5.createToolsModule(var1[var4]);
            if (var6 != null) {
               var6.setOutputDir(this.ctx.getOutputDir());
               var6.initModuleClassLoader(this.ctx, this.ctx.getApplicationContext().getAppClassLoader());
               var6.merge(this.ctx);
               var2.add(var6);
            }
         }

      }
   }
}
