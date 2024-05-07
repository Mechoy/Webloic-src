package weblogic.application.compiler.flow;

import java.util.ArrayList;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.compiler.ToolsFactoryManager;
import weblogic.application.compiler.WLModuleFactory;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.utils.compiler.ToolFailureException;

public final class InitModulesFlow extends CompilerFlow {
   public InitModulesFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.ctx.setModules(this.initModules());
   }

   private EARModule[] initModules() {
      EARModuleList var1 = new EARModuleList();
      ModuleBean[] var2 = this.ctx.getApplicationDD().getModules();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.addEARModule(ToolsFactoryManager.createModule(var2[var3]));
         }
      }

      WeblogicApplicationBean var6 = this.ctx.getWLApplicationDD();
      if (var6 != null) {
         WeblogicModuleBean[] var4 = var6.getModules();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               var1.addEARModule(this.initModule(var4[var5]));
            }
         }
      }

      if (var1.size() == 0) {
         J2EELogger.logAppcNoModulesFoundLoggable(this.ctx.getSourceName()).log();
         return new EARModule[0];
      } else {
         return (EARModule[])((EARModule[])var1.toArray(new EARModule[var1.size()]));
      }
   }

   private EARModule initModule(WeblogicModuleBean var1) {
      WLModuleFactory[] var2 = ToolsFactoryManager.wlmoduleFactories;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         EARModule var4 = var2[var3].createModule(var1);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   public void cleanup() {
   }

   private class EARModuleList extends ArrayList {
      private static final long serialVersionUID = 2883469812856650286L;

      private EARModuleList() {
      }

      public void addEARModule(EARModule var1) {
         if (var1 != null) {
            super.add(var1);
         }
      }

      // $FF: synthetic method
      EARModuleList(Object var2) {
         this();
      }
   }
}
