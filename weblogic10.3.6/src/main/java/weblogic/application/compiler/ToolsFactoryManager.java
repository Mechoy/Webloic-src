package weblogic.application.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weblogic.j2ee.descriptor.ModuleBean;

public class ToolsFactoryManager {
   public static CompilerFactory[] compilerFactories = new CompilerFactory[]{new LightWeightCompilerFactory(), new SCACompilerFactory(), new EARCompilerFactory(), new WARCompilerFactory(), new RARCompilerFactory(), new JMSCompilerFactory(), new JDBCCompilerFactory(), new EJBCompilerFactory(), new CARCompilerFactory()};
   public static MergerFactory[] mergerFactories = new MergerFactory[]{new LightWeightCompilerFactory(), new SCACompilerFactory(), new EARCompilerFactory(), new WARCompilerFactory(), new RARCompilerFactory(), new JMSCompilerFactory(), new JDBCCompilerFactory(), new EJBCompilerFactory(), new CARCompilerFactory()};
   private static ModuleFactory[] defaultModuleFactories = new ModuleFactory[]{new WARCompilerFactory(), new RARCompilerFactory(), new EJBCompilerFactory()};
   private static final List<ModuleFactory> overridingModuleFactories = new ArrayList();
   private static ModuleFactory[] moduleFactories;
   public static WLModuleFactory[] wlmoduleFactories = new WLModuleFactory[]{new JMSCompilerFactory(), new JDBCCompilerFactory()};
   private static StandaloneModuleFactory[] defaultStandAloneModuleFactories = new StandaloneModuleFactory[]{new WARCompilerFactory(), new RARCompilerFactory(), new JMSCompilerFactory(), new JDBCCompilerFactory(), new EJBCompilerFactory(), new CARCompilerFactory()};
   private static final List<StandaloneModuleFactory> overridingStandAloneModuleFactories = new ArrayList();
   private static StandaloneModuleFactory[] standAloneModuleFactories;

   public static void addModuleFactory(ModuleFactory var0) {
      overridingModuleFactories.add(var0);
   }

   public static EARModule createModule(ModuleBean var0) {
      if (moduleFactories == null) {
         ArrayList var1 = new ArrayList();
         var1.addAll(overridingModuleFactories);
         var1.addAll(Arrays.asList(defaultModuleFactories));
         moduleFactories = (ModuleFactory[])var1.toArray(new ModuleFactory[0]);
      }

      for(int var3 = 0; var3 < moduleFactories.length; ++var3) {
         EARModule var2 = moduleFactories[var3].createModule(var0);
         if (var2 != null) {
            return var2;
         }
      }

      return null;
   }

   public static void addStandaloneModuleFactory(StandaloneModuleFactory var0) {
      overridingStandAloneModuleFactories.add(var0);
   }

   public static EARModule createStandaloneModule(CompilerCtx var0, File var1) {
      if (standAloneModuleFactories == null) {
         ArrayList var2 = new ArrayList();
         var2.addAll(overridingStandAloneModuleFactories);
         var2.addAll(Arrays.asList(defaultStandAloneModuleFactories));
         standAloneModuleFactories = (StandaloneModuleFactory[])var2.toArray(new StandaloneModuleFactory[0]);
      }

      for(int var4 = 0; var4 < standAloneModuleFactories.length; ++var4) {
         EARModule var3 = standAloneModuleFactories[var4].createModule(var0, var1);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }
}
