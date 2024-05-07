package weblogic.diagnostics.module;

import java.lang.reflect.Method;
import weblogic.application.ApplicationContext;
import weblogic.descriptor.DescriptorDiff;
import weblogic.diagnostics.descriptor.WLDFResourceBean;

public final class SubModuleRegistry {
   private static Method[] allModuleResolvers = null;
   private static int[] appScopedModules = null;
   private static int[] modules = null;
   private static final Boolean NOT_APP_SCOPED = new Boolean(false);
   private static final Boolean IS_APP_SCOPED = new Boolean(true);
   private static final Object[][] moduleSpecs;
   private static SubModuleRegistry self;

   public static String getWLDFSubModuleName(WLDFSubModule var0) {
      return ((ModuleInstance)var0).name;
   }

   public static Class getWLDFSubModuleType(WLDFSubModule var0) {
      return ((ModuleInstance)var0).module.getClass();
   }

   public static WLDFSubModule[] getWLDFSubModules() {
      try {
         if (modules == null) {
            initSubModules();
         }

         return getSubModuleArray(modules);
      } catch (RuntimeException var1) {
         throw var1;
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   static WLDFSubModule[] getAppScopedWLDFSubModules() {
      try {
         if (appScopedModules == null) {
            initSubModules();
         }

         return getSubModuleArray(appScopedModules);
      } catch (RuntimeException var1) {
         throw var1;
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   private static WLDFSubModule[] getSubModuleArray(int[] var0) {
      try {
         WLDFSubModule[] var1 = new WLDFSubModule[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            int var3 = var0[var2];
            String var4 = (String)moduleSpecs[var3][0];
            Method var5 = allModuleResolvers[var3];
            WLDFSubModule var6 = (WLDFSubModule)var5.invoke((Object)null);
            ModuleInstance var9 = self.new ModuleInstance(var4, var6);
            var1[var2] = var9;
         }

         return var1;
      } catch (RuntimeException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }

   private static void initSubModules() throws Exception {
      int[] var0 = new int[moduleSpecs.length];
      int var1 = 0;
      int[] var2 = new int[moduleSpecs.length];
      int var3 = 0;
      allModuleResolvers = new Method[moduleSpecs.length];

      for(int var4 = 0; var4 < moduleSpecs.length; ++var4) {
         Object[] var5 = (Object[])moduleSpecs[var4];
         String var6 = (String)var5[0];
         String var7 = (String)var5[1];
         Boolean var8 = (Boolean)var5[2];
         boolean var9 = var8;
         Class var10 = null;

         try {
            var10 = Class.forName(var7);
            Method var11 = var10.getDeclaredMethod("createInstance");
            allModuleResolvers[var4] = var11;
            var2[var3] = var4;
            ++var3;
            if (var9) {
               var0[var1] = var4;
               ++var1;
            }
         } catch (RuntimeException var12) {
            throw var12;
         } catch (Exception var13) {
            throw new RuntimeException(var13);
         }
      }

      appScopedModules = new int[var1];
      System.arraycopy(var0, 0, appScopedModules, 0, var1);
      modules = new int[var3];
      System.arraycopy(var2, 0, modules, 0, var3);
   }

   static {
      moduleSpecs = new Object[][]{{"Instrumentation System WLDF Submodule", "weblogic.diagnostics.instrumentation.InstrumentationSubmodule", IS_APP_SCOPED}, {"Harvester WLDF Submodule", "weblogic.diagnostics.harvester.internal.HarvesterSubModule", NOT_APP_SCOPED}, {"Watches & Notifications WLDF Submodule", "weblogic.diagnostics.watch.WatchSubModule", NOT_APP_SCOPED}};
      self = new SubModuleRegistry();
   }

   private class ModuleInstance implements WLDFSubModule {
      String name;
      WLDFSubModule module;

      private ModuleInstance(String var2, WLDFSubModule var3) {
         this.name = var2;
         this.module = var3;
      }

      public void init(ApplicationContext var1, WLDFResourceBean var2) throws WLDFModuleException {
         this.module.init(var1, var2);
      }

      public void prepare() throws WLDFModuleException {
         this.module.prepare();
      }

      public void activate() throws WLDFModuleException {
         this.module.activate();
      }

      public void deactivate() throws WLDFModuleException {
         this.module.deactivate();
      }

      public void unprepare() throws WLDFModuleException {
         this.module.unprepare();
      }

      public void destroy() throws WLDFModuleException {
         this.module.destroy();
      }

      public void prepareUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
         this.module.prepareUpdate(var1, var2);
      }

      public void activateUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
         this.module.activateUpdate(var1, var2);
      }

      public void rollbackUpdate(WLDFResourceBean var1, DescriptorDiff var2) {
         this.module.rollbackUpdate(var1, var2);
      }

      // $FF: synthetic method
      ModuleInstance(String var2, WLDFSubModule var3, Object var4) {
         this(var2, var3);
      }
   }
}
