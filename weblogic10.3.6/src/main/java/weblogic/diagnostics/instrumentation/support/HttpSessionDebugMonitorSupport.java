package weblogic.diagnostics.instrumentation.support;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpSession;
import weblogic.common.internal.PassivationUtils;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextConstants;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticMonitorControl;
import weblogic.diagnostics.instrumentation.DynamicJoinPointImpl;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationConstants;
import weblogic.diagnostics.instrumentation.InstrumentationDebug;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.LocalHolder;
import weblogic.diagnostics.instrumentation.MonitorLocalHolder;
import weblogic.diagnostics.instrumentation.rtsupport.InstrumentationSupportImpl;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;

public final class HttpSessionDebugMonitorSupport implements DiagnosticContextConstants, InstrumentationConstants {
   public static void preProcess(LocalHolder var0) {
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      preProcess((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.monitor);
   }

   public static void preProcess(JoinPoint var0, DiagnosticMonitor var1) {
      if (var1 instanceof DiagnosticMonitorControl) {
         DiagnosticMonitorControl var2 = (DiagnosticMonitorControl)var1;
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("HttpSessionDebugMonitorSupport.preProcess for " + var2.getType());
         }

         DiagnosticContext var3 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
         if (InstrumentationSupportImpl.dyeMatches(var2, var3)) {
            try {
               if (var0 instanceof DynamicJoinPointImpl) {
                  DynamicJoinPointImpl var4 = (DynamicJoinPointImpl)var0;
                  var4.setMonitorType(var1.getType());
                  Object[] var5 = var4.getArguments();
                  var4.setMonitorType((String)null);
                  Object var6 = var5 != null && var5.length > 0 ? var5[0] : null;
                  if (var6 instanceof HttpSession) {
                     int var7 = -1;

                     try {
                        if (var6 instanceof Serializable) {
                           var7 = PassivationUtils.sizeOf(var6);
                        }
                     } catch (IOException var9) {
                        var7 = -2;
                     }

                     InstrumentationEvent var8 = createInstrumentationEvent(var0, var2, "SessionSize-Before", var7);
                     EventQueue.getInstance().enqueue(var8);
                  }
               }
            } catch (Throwable var10) {
               UnexpectedExceptionHandler.handle("Unexpected exception in HttpSessionDebugMonitorSupport.preProcess", var10);
            }

         }
      }
   }

   public static void postProcess(LocalHolder var0) {
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      postProcess((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.monitor);
   }

   public static void postProcess(JoinPoint var0, DiagnosticMonitor var1) {
      if (var1 instanceof DiagnosticMonitorControl) {
         DiagnosticMonitorControl var2 = (DiagnosticMonitorControl)var1;
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("HttpSessionDebugMonitorSupport.postProcess for " + var2.getType());
         }

         DiagnosticContext var3 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
         if (InstrumentationSupportImpl.dyeMatches(var2, var3)) {
            try {
               if (var0 instanceof DynamicJoinPointImpl) {
                  DynamicJoinPointImpl var4 = (DynamicJoinPointImpl)var0;
                  var4.setMonitorType(var1.getType());
                  Object[] var5 = var4.getArguments();
                  Object var6 = var4.getReturnValue();
                  var4.setMonitorType((String)null);
                  var0 = var4.getDelegate();
                  if (!(var6 instanceof HttpSession)) {
                     if (var5 == null && var0 instanceof DynamicJoinPointImpl) {
                        var4 = (DynamicJoinPointImpl)var0;
                        var4.setMonitorType(var1.getType());
                        var5 = var4.getArguments();
                        var4.setMonitorType((String)null);
                     }

                     var6 = var5 != null && var5.length > 0 ? var5[0] : null;
                  }

                  if (var6 instanceof HttpSession) {
                     int var7 = -1;

                     try {
                        if (var6 instanceof Serializable) {
                           var7 = PassivationUtils.sizeOf(var6);
                        }
                     } catch (IOException var9) {
                        var7 = -2;
                     }

                     InstrumentationEvent var8 = createInstrumentationEvent(var0, var2, "SessionSize-After", var7);
                     EventQueue.getInstance().enqueue(var8);
                  }
               }
            } catch (Throwable var10) {
               UnexpectedExceptionHandler.handle("Unexpected exception in HttpSessionDebugMonitorSupport.postProcess", var10);
            }

         }
      }
   }

   private static InstrumentationEvent createInstrumentationEvent(JoinPoint var0, DiagnosticMonitorControl var1, String var2, int var3) {
      DiagnosticContext var4 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      InstrumentationEvent var5 = new InstrumentationEvent(var1, var0);
      if (var4 != null) {
         var5.setContextId(var4.getContextId());
      }

      var5.setEventType(var2);
      var5.setPayload(new Integer(var3));
      return var5;
   }
}
