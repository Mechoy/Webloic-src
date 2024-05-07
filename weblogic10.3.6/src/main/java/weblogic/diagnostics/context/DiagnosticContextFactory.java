package weblogic.diagnostics.context;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.KernelStatus;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;

public final class DiagnosticContextFactory {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugDiagnosticContext");
   private static final Factory DEFAULT_FACTORY = new DefaultFactoryImpl();
   private static Factory SINGLETON;
   private static boolean contextEnabled;
   private static int propagationMode;

   static synchronized void setFactory(Factory var0) {
      if (KernelStatus.isServer() && SINGLETON == DEFAULT_FACTORY && var0 != null) {
         SINGLETON = var0;
      }
   }

   static boolean isEnabled() {
      return contextEnabled;
   }

   static void setEnabled(boolean var0) {
      contextEnabled = var0;
   }

   static int getPropagationMode() {
      return propagationMode;
   }

   static void setPropagationMode(int var0) {
      propagationMode = var0;
   }

   public static DiagnosticContext getDiagnosticContext() {
      WorkContextMap var0 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
      return (DiagnosticContext)var0.get("weblogic.diagnostics.DiagnosticContext");
   }

   public static DiagnosticContext findOrCreateDiagnosticContext() {
      return findOrCreateDiagnosticContext(contextEnabled);
   }

   public static DiagnosticContext findOrCreateDiagnosticContext(boolean var0) {
      if (!KernelStatus.isServer()) {
         var0 = true;
      }

      return SINGLETON.findOrCreateDiagnosticContext(var0);
   }

   public static void invalidateCache() {
      SINGLETON.invalidateCache();
   }

   public static void setJFRThrottled(DiagnosticContext var0) {
      SINGLETON.setJFRThrottled(var0);
   }

   static void setDiagnosticContext(DiagnosticContext var0) {
      SINGLETON.setDiagnosticContext(var0);
   }

   static {
      SINGLETON = DEFAULT_FACTORY;
      propagationMode = 383;
   }

   private static class DefaultFactoryImpl implements Factory {
      private DefaultFactoryImpl() {
      }

      public DiagnosticContext findOrCreateDiagnosticContext(boolean var1) {
         Object var2 = null;
         WorkContextMap var3 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
         var2 = (DiagnosticContext)var3.get("weblogic.diagnostics.DiagnosticContext");
         if (var2 == null && var1) {
            var2 = new DiagnosticContextImpl();

            try {
               var3.put("weblogic.diagnostics.DiagnosticContext", (WorkContext)var2, DiagnosticContextFactory.propagationMode);
            } catch (Throwable var5) {
               if (DiagnosticContextFactory.DEBUG_LOGGER.isDebugEnabled()) {
                  var5.printStackTrace();
               }
            }
         }

         return (DiagnosticContext)var2;
      }

      public void invalidateCache() {
      }

      public void setJFRThrottled(DiagnosticContext var1) {
      }

      public void setDiagnosticContext(DiagnosticContext var1) {
         WorkContextMap var2 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();

         try {
            if (var1 != null) {
               var2.put("weblogic.diagnostics.DiagnosticContext", var1, DiagnosticContextFactory.propagationMode);
            } else if (var2.get("weblogic.diagnostics.DiagnosticContext") != null) {
               var2.remove("weblogic.diagnostics.DiagnosticContext");
               this.invalidateCache();
            }
         } catch (Throwable var4) {
            if (DiagnosticContextFactory.DEBUG_LOGGER.isDebugEnabled()) {
               var4.printStackTrace();
            }
         }

      }

      // $FF: synthetic method
      DefaultFactoryImpl(Object var1) {
         this();
      }
   }

   public interface Factory {
      DiagnosticContext findOrCreateDiagnosticContext(boolean var1);

      void invalidateCache();

      void setJFRThrottled(DiagnosticContext var1);

      void setDiagnosticContext(DiagnosticContext var1);
   }
}
