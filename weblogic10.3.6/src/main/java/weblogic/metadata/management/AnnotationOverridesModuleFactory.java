package weblogic.metadata.management;

import weblogic.application.CustomModuleContext;
import weblogic.application.CustomModuleFactory;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.compiler.EARModule;
import weblogic.application.config.DefaultEARModule;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.ModuleProviderBean;
import weblogic.utils.compiler.ToolFailureException;

public class AnnotationOverridesModuleFactory extends CustomModuleFactory {
   private ModuleProviderBean config;
   private CustomModuleContext ctx;

   public void init(CustomModuleContext var1) {
      this.config = var1.getModuleProviderBean();
      this.ctx = var1;
   }

   public Module createModule(CustomModuleBean var1) throws ModuleException {
      return new AnnotationOverridesModule(this.config, var1);
   }

   public EARModule createToolsModule(CustomModuleBean var1) throws ToolFailureException {
      return new DefaultEARModule(this.ctx, var1, true);
   }
}
