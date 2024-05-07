package weblogic.diagnostics.instrumentation.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;

public final class TraceMemoryAllocationAction extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   private static final long serialVersionUID = 1L;
   private static Method getAllocatedBytesMethod = getAllocatedBytesMethod();

   public TraceMemoryAllocationAction() {
      this.setType("TraceMemoryAllocationAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return getAllocatedBytesMethod == null ? null : new TraceMemoryAllocationActionState();
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         TraceMemoryAllocationActionState var3 = (TraceMemoryAllocationActionState)var2;
         long var4 = 0L;
         if (getAllocatedBytesMethod != null) {
            try {
               var4 = (Long)getAllocatedBytesMethod.invoke((Object)null, (Object[])null);
            } catch (IllegalArgumentException var8) {
            } catch (IllegalAccessException var9) {
            } catch (InvocationTargetException var10) {
            }
         }

         var3.setValue(var4);
         DiagnosticMonitor var6 = this.getDiagnosticMonitor();
         if (var6 != null) {
            InstrumentationEvent var7 = this.createInstrumentationEvent(var1, false);
            if (var7 == null) {
               return;
            }

            var7.setEventType(this.getType() + "-Before-" + var3.getId());
            EventQueue.getInstance().enqueue(var7);
         }

      }
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         InstrumentationEvent var3 = this.createInstrumentationEvent(var1, false);
         if (var3 != null) {
            TraceMemoryAllocationActionState var4 = (TraceMemoryAllocationActionState)var2;
            long var5 = 0L;
            if (getAllocatedBytesMethod != null) {
               try {
                  var5 = (Long)getAllocatedBytesMethod.invoke((Object)null, (Object[])null);
               } catch (IllegalArgumentException var8) {
               } catch (IllegalAccessException var9) {
               } catch (InvocationTargetException var10) {
               }
            }

            var3.setPayload(new Long(var5 - var4.getValue()));
            var3.setEventType(this.getType() + "-After-" + var4.getId());
            EventQueue.getInstance().enqueue(var3);
         }
      }
   }

   private static Method getAllocatedBytesMethod() {
      try {
         Class var0 = Class.forName("jrockit.ext.ThreadInfo");
         Method var1 = var0.getMethod("getAllocatedBytes", (Class[])null);
         return var1;
      } catch (ClassNotFoundException var2) {
      } catch (SecurityException var3) {
      } catch (NoSuchMethodException var4) {
      }

      return null;
   }

   private static class TraceMemoryAllocationActionState implements DiagnosticActionState {
      private static int seqNum;
      private int id = genId();
      private long value;

      TraceMemoryAllocationActionState() {
      }

      int getId() {
         return this.id;
      }

      long getValue() {
         return this.value;
      }

      void setValue(long var1) {
         this.value = var1;
      }

      private static synchronized int genId() {
         ++seqNum;
         return seqNum;
      }
   }
}
