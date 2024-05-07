package weblogic.diagnostics.lifecycle;

import java.lang.reflect.Method;

public final class ComponentRegistry {
   private static DiagnosticComponentLifecycle[] allComponents = null;
   private static int[] foundationComponents = null;
   private static int[] nonFoundationComponents = null;
   private static final Boolean NOT_FOUNDATION = new Boolean(false);
   private static final Boolean IS_FOUNDATION = new Boolean(true);
   private static final Object[][] componentSpecs;

   public static String getWLDFComponentName(DiagnosticComponentLifecycle var0) {
      if (allComponents == null) {
         return null;
      } else {
         for(int var1 = 0; var1 < allComponents.length; ++var1) {
            DiagnosticComponentLifecycle var2 = allComponents[var1];
            if (var0 == var2) {
               return (String)componentSpecs[var1][0];
            }
         }

         return null;
      }
   }

   static DiagnosticComponentLifecycle[] getNonFoundationWLDFComponents() {
      try {
         if (nonFoundationComponents == null) {
            initComponents();
         }

         return getComponentArray(nonFoundationComponents);
      } catch (RuntimeException var1) {
         throw var1;
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   static DiagnosticComponentLifecycle[] getFoundationWLDFComponents() {
      try {
         if (foundationComponents == null) {
            initComponents();
         }

         return getComponentArray(foundationComponents);
      } catch (RuntimeException var1) {
         throw var1;
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   private static DiagnosticComponentLifecycle[] getComponentArray(int[] var0) {
      DiagnosticComponentLifecycle[] var1 = new DiagnosticComponentLifecycle[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = allComponents[var0[var2]];
      }

      return var1;
   }

   private static void initComponents() throws Exception {
      int[] var0 = new int[componentSpecs.length];
      int var1 = 0;
      int[] var2 = new int[componentSpecs.length];
      int var3 = 0;
      allComponents = new DiagnosticComponentLifecycle[componentSpecs.length];

      for(int var4 = 0; var4 < componentSpecs.length; ++var4) {
         Object[] var5 = (Object[])componentSpecs[var4];
         String var6 = (String)var5[0];
         String var7 = (String)var5[1];
         Boolean var8 = (Boolean)var5[2];
         boolean var9 = var8;
         Class var10 = null;

         try {
            var10 = Class.forName(var7);
            Method var11 = var10.getDeclaredMethod("getInstance");
            DiagnosticComponentLifecycle var12 = (DiagnosticComponentLifecycle)var11.invoke((Object)null);
            allComponents[var4] = var12;
            if (var9) {
               var0[var1] = var4;
               ++var1;
            } else {
               var2[var3] = var4;
               ++var3;
            }
         } catch (RuntimeException var13) {
            throw var13;
         } catch (Exception var14) {
            throw new RuntimeException(var14);
         }
      }

      foundationComponents = new int[var1];
      System.arraycopy(var0, 0, foundationComponents, 0, var1);
      nonFoundationComponents = new int[var3];
      System.arraycopy(var2, 0, nonFoundationComponents, 0, var3);
   }

   static {
      componentSpecs = new Object[][]{{"Diagnostic Image", "weblogic.diagnostics.lifecycle.DiagnosticImageLifecycleImpl", IS_FOUNDATION}, {"Server Logging", "weblogic.diagnostics.lifecycle.ServerLoggingLifecycleImpl", IS_FOUNDATION}, {"Debug", "weblogic.diagnostics.lifecycle.DebugLifecycleImpl", IS_FOUNDATION}, {"Diagnostic Context", "weblogic.diagnostics.lifecycle.DiagnosticContextLifecycleImpl", IS_FOUNDATION}, {"Manager", "weblogic.diagnostics.lifecycle.ManagerLifecycleImpl", IS_FOUNDATION}, {"Archive", "weblogic.diagnostics.lifecycle.ArchiveLifecycleImpl", IS_FOUNDATION}, {"Domain Logging", "weblogic.diagnostics.lifecycle.LoggingLifecycleImpl", NOT_FOUNDATION}, {"Harvester", "weblogic.diagnostics.harvester.internal.HarvesterLifecycleImpl", NOT_FOUNDATION}, {"Watches & Notifications", "weblogic.diagnostics.lifecycle.WatchLifecycleImpl", NOT_FOUNDATION}, {"Accessor", "weblogic.diagnostics.lifecycle.AccessorLifecycleImpl", NOT_FOUNDATION}, {"Instrumentation System", "weblogic.diagnostics.lifecycle.InstrumentationLifecycleImpl", NOT_FOUNDATION}, {"WLDF archive data retirement", "weblogic.diagnostics.lifecycle.DataRetirementLifecycleImpl", NOT_FOUNDATION}};
   }
}
