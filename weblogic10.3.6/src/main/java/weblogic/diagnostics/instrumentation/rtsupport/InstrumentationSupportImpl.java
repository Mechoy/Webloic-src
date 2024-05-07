package weblogic.diagnostics.instrumentation.rtsupport;

import java.util.Map;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DelegatingMonitorControl;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticMonitorControl;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.DynamicJoinPointImpl;
import weblogic.diagnostics.instrumentation.InstrumentationDebug;
import weblogic.diagnostics.instrumentation.InstrumentationManager;
import weblogic.diagnostics.instrumentation.InstrumentationScope;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.JoinPointImpl;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfoImpl;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfoImpl;
import weblogic.diagnostics.instrumentation.support.DyeInjectionMonitorSupport;
import weblogic.kernel.Kernel;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;

public final class InstrumentationSupportImpl implements InstrumentationSupport.InstrumentationSupportInterface {
   private static final boolean noSupport1 = Boolean.getBoolean("weblogic.diagnostics.internal.noSupport1");
   private static final boolean noSupport2 = Boolean.getBoolean("weblogic.diagnostics.internal.noSupport2");

   public InstrumentationSupportImpl() throws Exception {
      if (!Kernel.isServer()) {
         throw new Exception("Not running in server, this will cause InstrumentationSupport to be used instead");
      }
   }

   public DiagnosticMonitor getMonitor(Class var1, String var2) {
      if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_ACTIONS.debug("InstrumentationSupport.getMonitor: " + var2 + " for class " + var1.getName());
         StringBuffer var3 = new StringBuffer();
         DisplayClassLoaderAnnotations(var1.getClassLoader(), var3);
         InstrumentationDebug.DEBUG_ACTIONS.debug("InstrumentationSupport.getMonitor: classloaders: " + var3.toString());
      }

      InstrumentationManager var9 = InstrumentationManager.getInstrumentationManager();
      Object var4 = var9.getServerManagedMonitorControl(var2);
      if (var4 != null) {
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Returning monitor of type " + var2 + " monitor=" + var4);
         }

         return (DiagnosticMonitor)var4;
      } else {
         ClassLoader var5 = var1.getClassLoader();
         InstrumentationScope var6 = var9.getAssociatedScope(var5);
         if (var6 == null) {
            String var8 = null;

            for(ClassLoader var7 = var5; var7 != null && var8 == null; var7 = var7.getParent()) {
               var8 = getInstrumentationScopeName(var7);
            }

            if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
               InstrumentationDebug.DEBUG_ACTIONS.debug("InstrumentationSupport.getMonitor: appName=" + var8);
            }

            if (var8 == null) {
               var8 = "_WL_INTERNAL_SERVER_SCOPE";
            }

            if (var8 != null) {
               var6 = var9.findInstrumentationScope(var8);
               if (var6 != null) {
                  var9.associateClassloaderWithScope(var5, var6);
               }
            }
         }

         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("InstrumentationSupport.getMonitor: scope=" + (var6 != null ? var6.getName() : "null"));
         }

         if (var6 != null) {
            var4 = var6.findDiagnosticMonitorControl(var2);
         }

         if (var4 == null) {
            var4 = var9.getServerMonitor(var2);
         }

         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("InstrumentationSupport.getMonitor: monitor=" + var4);
         }

         if (var4 == null) {
            if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
               InstrumentationDebug.DEBUG_ACTIONS.debug("Returning orphan monitor of type " + var2);
            }

            var4 = new DelegatingMonitorControl(var2, var2);
         }

         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Returning monitor of type " + var2 + " monitor=" + var4);
         }

         return (DiagnosticMonitor)var4;
      }
   }

   public static void DisplayClassLoaderAnnotations(ClassLoader var0, StringBuffer var1) {
      for(int var2 = 0; var0 != null; var0 = var0.getParent()) {
         var1.append("\n    " + var2 + ": " + var0.getClass().getName() + "@" + var0.hashCode());
         if (var0 instanceof GenericClassLoader) {
            GenericClassLoader var3 = (GenericClassLoader)var0;
            Annotation var4 = var3.getAnnotation();
            var1.append(" annotation=");
            var1.append(var4 != null ? var4.toString() : "null");
         }

         ++var2;
      }

   }

   public static String getInstrumentationScopeName(ClassLoader var0) {
      String var1;
      for(var1 = null; var0 != null && var1 == null; var0 = var0.getParent()) {
         if (var0 instanceof GenericClassLoader) {
            GenericClassLoader var2 = (GenericClassLoader)var0;
            Annotation var3 = var2.getAnnotation();
            if (var3 != null) {
               var1 = var3.getApplicationName();
            }
         }
      }

      return var1;
   }

   public static boolean dyeMatches(DiagnosticMonitor var0) {
      DiagnosticContext var1 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      return dyeMatches(var0, var1);
   }

   public static boolean dyeMatches(DiagnosticMonitor var0, DiagnosticContext var1) {
      if (var1 == null) {
         return true;
      } else {
         if (var0.isDyeFilteringEnabled()) {
            long var2 = var0.getDyeMask();
            if (var1 != null && (var2 & var1.getDyeVector()) != var2) {
               return false;
            }
         } else if (DyeInjectionMonitorSupport.isThrottlingEnabled()) {
            return (var1.getDyeVector() & 4294967296L) != 0L;
         }

         return true;
      }
   }

   public void process(JoinPoint var1, DiagnosticMonitor var2, DiagnosticAction[] var3) {
      if (var2 instanceof DiagnosticMonitorControl) {
         DiagnosticMonitorControl var4 = (DiagnosticMonitorControl)var2;
         int var5 = var3 != null ? var3.length : 0;
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Executing process() for " + var4.getType() + " with " + var5 + " actions");
         }

         DynamicJoinPointImpl var6 = null;
         if (var1 instanceof DynamicJoinPointImpl) {
            var6 = (DynamicJoinPointImpl)var1;
            var6.setMonitorType(var2.getType());
         }

         for(int var7 = 0; var7 < var5; ++var7) {
            try {
               StatelessDiagnosticAction var8 = (StatelessDiagnosticAction)var3[var7];
               var8.process(var1);
            } catch (Throwable var9) {
               if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
                  InstrumentationDebug.DEBUG_ACTIONS.debug("Unexpected exception in process,  executing action " + var7, var9);
               }
            }
         }

         if (var6 != null) {
            var6.setMonitorType((String)null);
         }

      }
   }

   public void preProcess(JoinPoint var1, DiagnosticMonitor var2, DiagnosticAction[] var3, DiagnosticActionState[] var4) {
      if (var2 instanceof DiagnosticMonitorControl) {
         DiagnosticMonitorControl var5 = (DiagnosticMonitorControl)var2;
         int var6 = var3 != null ? var3.length : 0;
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Executing preProcess() for " + var5.getType() + " with " + var6 + " actions");
         }

         DynamicJoinPointImpl var7 = null;
         if (var1 instanceof DynamicJoinPointImpl) {
            var7 = (DynamicJoinPointImpl)var1;
            var7.setMonitorType(var2.getType());
         }

         for(int var8 = 0; var8 < var6; ++var8) {
            try {
               AroundDiagnosticAction var9 = (AroundDiagnosticAction)var3[var8];
               var9.preProcess(var1, var4[var8]);
            } catch (Throwable var10) {
               if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
                  InstrumentationDebug.DEBUG_ACTIONS.debug("Unexpected exception in preProcess,  executing action " + var8, var10);
               }
            }
         }

         if (var7 != null) {
            var7.setMonitorType((String)null);
         }

      }
   }

   public void postProcess(JoinPoint var1, DiagnosticMonitor var2, DiagnosticAction[] var3, DiagnosticActionState[] var4) {
      if (var2 instanceof DiagnosticMonitorControl) {
         DiagnosticMonitorControl var5 = (DiagnosticMonitorControl)var2;
         int var6 = var3 != null ? var3.length : 0;
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Executing postProcess() for " + var5.getType() + " with " + var6 + " actions");
         }

         DynamicJoinPointImpl var7 = null;
         if (var1 instanceof DynamicJoinPointImpl) {
            var7 = (DynamicJoinPointImpl)var1;
            var7.setMonitorType(var2.getType());
         }

         for(int var8 = 0; var8 < var6; ++var8) {
            try {
               AroundDiagnosticAction var9 = (AroundDiagnosticAction)var3[var8];
               var9.postProcess(var1, var4[var8]);
            } catch (Throwable var10) {
               if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
                  InstrumentationDebug.DEBUG_ACTIONS.debug("Unexpected exception in postProcess,  executing action " + var8, var10);
               }
            }
         }

         if (var7 != null) {
            var7.setMonitorType((String)null);
         }

      }
   }

   public DynamicJoinPoint createDynamicJoinPoint(JoinPoint var1, Object[] var2, Object var3) {
      if (var1 instanceof DynamicJoinPointImpl) {
         DynamicJoinPointImpl var4 = (DynamicJoinPointImpl)var1;
         if (var2 != null) {
            var4.setArguments(var2);
         }

         var4.setReturnValue(var3);
         return var4;
      } else {
         return new DynamicJoinPointImpl(var1, var2, var3);
      }
   }

   public JoinPoint createJoinPoint(Class var1, String var2, String var3, String var4, String var5, int var6, Map<String, PointcutHandlingInfo> var7, boolean var8) {
      return new JoinPointImpl(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public PointcutHandlingInfo createPointcutHandlingInfo(ValueHandlingInfo var1, ValueHandlingInfo var2, ValueHandlingInfo[] var3) {
      return new PointcutHandlingInfoImpl(var1, var2, var3);
   }

   public ValueHandlingInfo createValueHandlingInfo(String var1, String var2, boolean var3, boolean var4) {
      return new ValueHandlingInfoImpl(var1, var2, var3, var4);
   }

   public Map<String, PointcutHandlingInfo> makeMap(String[] var1, PointcutHandlingInfo[] var2) {
      return PointcutHandlingInfoImpl.makeMap(var1, var2);
   }
}
