package weblogic.diagnostics.instrumentation.action;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticMonitorControl;
import weblogic.diagnostics.instrumentation.InstrumentationScope;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class MethodMemoryAllocationStatisticsAction extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticInstrumentationActions");
   static final long serialVersionUID = -4957607339728632810L;
   private static final String COUNT = "count";
   private static final String MIN = "min";
   private static final String MAX = "max";
   private static final String AVG = "avg";
   private static final String SUM = "sum";
   private static final String SUM_OF_SQUARES = "sum_of_squares";
   private static final String STD_DEVIATION = "std_deviation";
   private static Method getAllocatedBytesMethod = getAllocatedBytesMethod();
   private Map parsedMethodDescriptorCache = new ConcurrentHashMap();

   public MethodMemoryAllocationStatisticsAction() {
      this.setType("MethodMemoryAllocationStatisticsAction");
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

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return getAllocatedBytesMethod == null ? null : new MethodMemoryAllocationStatisticsActionState();
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         MethodMemoryAllocationStatisticsActionState var3 = (MethodMemoryAllocationStatisticsActionState)var2;
         var3.initBeginTimestamp();
      }
   }

   public void postProcess(final JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         DiagnosticMonitor var3 = this.getDiagnosticMonitor();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Monitor class = " + var3.getClass().getName());
         }

         if (var3 instanceof DiagnosticMonitorControl) {
            DiagnosticMonitorControl var4 = (DiagnosticMonitorControl)var3;
            InstrumentationScope var5 = var4.getInstrumentationScope();
            final Map var6 = var5.getInstrumentationStatistics().getMethodMemoryAllocationStatistics();
            MethodMemoryAllocationStatisticsActionState var7 = (MethodMemoryAllocationStatisticsActionState)var2;
            var7.initEndTimestamp();
            final long var8 = var7.computeMemoryAllocated();
            MethodMemoryAllocationStatisticsAction.WORKMANAGER_GETTER.workManager.schedule(new Runnable() {
               public void run() {
                  MethodMemoryAllocationStatisticsAction.this.updateMethodMemoryAllocationStatistics(var1, var6, var8);
               }
            });
         }

      }
   }

   private void updateMethodMemoryAllocationStatistics(JoinPoint var1, Map var2, long var3) {
      long var5 = System.nanoTime();
      String var7 = var1.getClassName();
      String var8 = var1.getMethodName();
      String var9 = var1.getMethodDescriptor();
      if (DEBUG.isDebugEnabled()) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Class=");
         var10.append(var7);
         var10.append(";");
         var10.append("Method=");
         var10.append(var8);
         var10.append(";");
         var10.append("MethodDescriptor=");
         var10.append(var9);
         var10.append(";");
         DEBUG.debug(var10.toString());
      }

      String var45 = (String)this.parsedMethodDescriptorCache.get(var9);
      if (var45 == null) {
         var45 = this.parseMethodDescriptor(var9);
         this.parsedMethodDescriptorCache.put(var9, var45);
      } else if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Got md from cache " + var9 + "=" + var45);
      }

      if (var45 != null) {
         ConcurrentHashMap var11 = (ConcurrentHashMap)var2;
         var11.putIfAbsent(var7, new ConcurrentHashMap());
         ConcurrentHashMap var12 = (ConcurrentHashMap)var11.get(var7);
         var12.putIfAbsent(var8, new ConcurrentHashMap());
         ConcurrentHashMap var13 = (ConcurrentHashMap)var12.get(var8);
         var13.putIfAbsent(var45, new ConcurrentHashMap());
         ConcurrentHashMap var14 = (ConcurrentHashMap)var13.get(var45);
         synchronized(var14) {
            Number var16 = null;
            long var17 = 0L;
            long var19 = -1L;
            long var21 = -1L;
            double var23 = 0.0;
            double var25 = 0.0;
            double var27 = 0.0;
            double var29 = 0.0;
            var16 = (Number)var14.get("count");
            if (var16 != null) {
               var17 = var16.longValue();
            }

            var16 = (Number)var14.get("min");
            if (var16 != null) {
               var19 = var16.longValue();
            }

            var16 = (Number)var14.get("max");
            if (var16 != null) {
               var21 = var16.longValue();
            }

            var16 = (Number)var14.get("sum");
            if (var16 != null) {
               var23 = var16.doubleValue();
            }

            var16 = (Number)var14.get("avg");
            if (var16 != null) {
               var25 = var16.doubleValue();
            }

            var16 = (Number)var14.get("sum_of_squares");
            if (var16 != null) {
               var27 = var16.doubleValue();
            }

            var16 = (Number)var14.get("std_deviation");
            if (var16 != null) {
               var29 = var16.doubleValue();
            }

            ++var17;
            if (var19 == -1L) {
               var19 = var3;
            } else {
               var19 = var3 < var19 ? var3 : var19;
            }

            if (var21 == -1L) {
               var21 = var3;
            } else {
               var21 = var3 > var21 ? var3 : var21;
            }

            var23 += (double)var3;
            long var31 = System.nanoTime();
            var25 = var23 / (double)var17;
            long var33 = System.nanoTime();
            var27 += (double)(var3 * var3);
            long var35 = System.nanoTime();
            double var37 = var27 / (double)var17 - var25 * var25;
            var29 = Math.sqrt(var37);
            long var39 = System.nanoTime();
            if (DEBUG.isDebugEnabled()) {
               StringBuilder var41 = new StringBuilder();
               var41.append("Count=");
               var41.append(var17);
               var41.append(";");
               var41.append("Min=");
               var41.append(var19);
               var41.append(";");
               var41.append("Max=");
               var41.append(var21);
               var41.append(";");
               var41.append("Avg=");
               var41.append(var25);
               var41.append(";");
               var41.append("Sum=");
               var41.append(var23);
               var41.append(";");
               var41.append("Sum of squares=");
               var41.append(var27);
               var41.append(";");
               var41.append("Std Deviation=");
               var41.append(var29);
               var41.append(";");
               DEBUG.debug(var41.toString());
            }

            var14.put("count", new Long(var17));
            var14.put("min", new Long(var19));
            var14.put("max", new Long(var21));
            var14.put("avg", new Double(var25));
            var14.put("sum", new Double(var23));
            var14.put("sum_of_squares", new Double(var27));
            var14.put("std_deviation", new Double(var29));
            long var46 = System.nanoTime();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Stats overhead:" + var3 + "," + (var33 - var31) + "," + (var35 - var33) + "," + (var39 - var35) + "," + (var46 - var5));
            }
         }
      }

   }

   private String parseMethodDescriptor(String var1) {
      StringReader var2 = new StringReader(var1);
      MethodDescriptorLexer var3 = new MethodDescriptorLexer(var2);
      MethodDescriptorParser var4 = new MethodDescriptorParser(var3);

      try {
         var4.methodDescriptor();
         List var5 = var4.getInputParameters();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Input params = " + var5);
         }

         StringBuilder var6 = new StringBuilder();
         int var7 = var5.size();
         if (var7 > 0) {
            for(int var8 = 0; var8 < var7 - 1; ++var8) {
               var6.append(var5.get(var8));
               var6.append(',');
            }

            var6.append(var5.get(var7 - 1));
         }

         return var6.toString();
      } catch (Exception var9) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception parsing", var9);
         }

         return null;
      }
   }

   private static class MethodMemoryAllocationStatisticsActionState implements DiagnosticActionState {
      private long beginMemoryAllocation;
      private long endMemoryAllocation;

      private MethodMemoryAllocationStatisticsActionState() {
      }

      void initBeginTimestamp() {
         this.beginMemoryAllocation = this.getAllocatedBytes();
      }

      void initEndTimestamp() {
         this.endMemoryAllocation = this.getAllocatedBytes();
      }

      long computeMemoryAllocated() {
         return this.endMemoryAllocation - this.beginMemoryAllocation;
      }

      private long getAllocatedBytes() {
         long var1 = 0L;
         if (MethodMemoryAllocationStatisticsAction.getAllocatedBytesMethod != null) {
            try {
               var1 = (Long)MethodMemoryAllocationStatisticsAction.getAllocatedBytesMethod.invoke((Object)null, (Object[])null);
            } catch (IllegalArgumentException var4) {
            } catch (IllegalAccessException var5) {
            } catch (InvocationTargetException var6) {
            }
         }

         return var1;
      }

      // $FF: synthetic method
      MethodMemoryAllocationStatisticsActionState(Object var1) {
         this();
      }
   }

   static class WORKMANAGER_GETTER {
      static WorkManager workManager;

      static {
         workManager = workManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.diagnostics.instrumentation.MethodMemoryAllocationStatisticsActionWorkManager", 1, -1);
      }
   }
}
