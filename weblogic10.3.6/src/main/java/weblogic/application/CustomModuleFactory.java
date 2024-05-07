package weblogic.application;

import weblogic.application.compiler.EARModule;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.utils.compiler.ToolFailureException;

public abstract class CustomModuleFactory {
   public abstract void init(CustomModuleContext var1);

   public abstract Module createModule(CustomModuleBean var1) throws ModuleException;

   public EARModule createToolsModule(CustomModuleBean var1) throws ToolFailureException {
      return null;
   }
}
