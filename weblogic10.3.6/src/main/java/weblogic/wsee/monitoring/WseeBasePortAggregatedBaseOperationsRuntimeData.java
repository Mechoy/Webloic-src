package weblogic.wsee.monitoring;

import com.sun.istack.Nullable;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.ManagementException;

public final class WseeBasePortAggregatedBaseOperationsRuntimeData extends WseeAggregatableBaseOperationRuntimeData {
   public WseeBasePortAggregatedBaseOperationsRuntimeData(String var1, @Nullable WseeBaseRuntimeData var2) throws ManagementException {
      super(var1, var2);
   }

   public int getErrorCount() {
      int var1 = 0;

      WseeBaseOperationRuntimeData var3;
      for(Iterator var2 = this.getOperationsData().iterator(); var2.hasNext(); var1 += var3.getErrorCount()) {
         var3 = (WseeBaseOperationRuntimeData)var2.next();
      }

      return var1;
   }

   public int getInvocationCount() {
      int var1 = 0;

      WseeBaseOperationRuntimeData var3;
      for(Iterator var2 = this.getOperationsData().iterator(); var2.hasNext(); var1 += var3.getInvocationCount()) {
         var3 = (WseeBaseOperationRuntimeData)var2.next();
      }

      return var1;
   }

   public int getResponseCount() {
      int var1 = 0;

      WseeBaseOperationRuntimeData var3;
      for(Iterator var2 = this.getOperationsData().iterator(); var2.hasNext(); var1 += var3.getResponseCount()) {
         var3 = (WseeBaseOperationRuntimeData)var2.next();
      }

      return var1;
   }

   public int getResponseErrorCount() {
      int var1 = 0;

      WseeBaseOperationRuntimeData var3;
      for(Iterator var2 = this.getOperationsData().iterator(); var2.hasNext(); var1 += var3.getResponseErrorCount()) {
         var3 = (WseeBaseOperationRuntimeData)var2.next();
      }

      return var1;
   }

   public long getDispatchTimeTotal() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 += var4.getDispatchTimeTotal();
         }
      }

      return var1;
   }

   public long getExecutionTimeTotal() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 += var4.getExecutionTimeTotal();
         }
      }

      return var1;
   }

   public long getResponseTimeTotal() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getResponseCount() > 0) {
            var1 += var4.getResponseTimeTotal();
         }
      }

      return var1;
   }

   public long getDispatchTimeHigh() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 = Math.max(var1, var4.getDispatchTimeHigh());
         }
      }

      return var1;
   }

   public long getExecutionTimeHigh() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 = Math.max(var1, var4.getExecutionTimeHigh());
         }
      }

      return var1;
   }

   public long getResponseTimeHigh() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0 && var4.getResponseCount() > 0) {
            var1 = Math.max(var1, var4.getResponseTimeHigh());
         }
      }

      return var1;
   }

   public long getLastErrorTime() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 = Math.max(var1, var4.getLastErrorTime());
         }
      }

      return var1;
   }

   public long getLastInvocationTime() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 = Math.max(var1, var4.getLastInvocationTime());
         }
      }

      return var1;
   }

   public long getLastResponseTime() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0 && var4.getResponseCount() > 0) {
            var1 = Math.max(var1, var4.getLastResponseTime());
         }
      }

      return var1;
   }

   public long getLastResponseErrorTime() {
      long var1 = 0L;
      Iterator var3 = this.getOperationsData().iterator();

      while(var3.hasNext()) {
         WseeBaseOperationRuntimeData var4 = (WseeBaseOperationRuntimeData)var3.next();
         if (var4.getInvocationCount() > 0) {
            var1 = Math.max(var1, var4.getLastResponseErrorTime());
         }
      }

      return var1;
   }

   public long getDispatchTimeLow() {
      long var1 = 0L;
      boolean var3 = true;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0) {
            if (var3) {
               var1 = var5.getDispatchTimeLow();
               var3 = false;
            } else {
               var1 = Math.min(var1, var5.getDispatchTimeLow());
            }
         }
      }

      return var1;
   }

   public long getExecutionTimeLow() {
      long var1 = 0L;
      boolean var3 = true;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0) {
            if (var3) {
               var1 = var5.getExecutionTimeLow();
               var3 = false;
            } else {
               var1 = Math.min(var1, var5.getExecutionTimeLow());
            }
         }
      }

      return var1;
   }

   public long getResponseTimeLow() {
      long var1 = 0L;
      boolean var3 = true;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0 && var5.getResponseCount() > 0) {
            if (var3) {
               var1 = var5.getResponseTimeLow();
               var3 = false;
            } else {
               var1 = Math.min(var1, var5.getResponseTimeLow());
            }
         }
      }

      return var1;
   }

   public String getLastError() {
      long var1 = 0L;
      String var3 = null;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0) {
            long var6 = var5.getLastErrorTime();
            if (var6 > var1) {
               var1 = var6;
               var3 = var5.getLastError();
            }
         }
      }

      return var3;
   }

   public String getLastResponseError() {
      long var1 = 0L;
      String var3 = null;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0) {
            long var6 = var5.getLastResponseErrorTime();
            if (var6 > var1) {
               var1 = var6;
               var3 = var5.getLastResponseError();
            }
         }
      }

      return var3;
   }

   public long getDispatchTimeAverage() {
      int var1 = 0;
      long var2 = 0L;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         int var6 = var5.getInvocationCount();
         if (var6 > 0) {
            var1 += var6;
            var2 += var5.getDispatchTimeTotal();
         }
      }

      return average(var2, var1);
   }

   public long getExecutionTimeAverage() {
      int var1 = 0;
      long var2 = 0L;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         int var6 = var5.getInvocationCount();
         if (var6 > 0) {
            var1 += var6;
            var2 += var5.getExecutionTimeTotal();
         }
      }

      return average(var2, var1);
   }

   public long getResponseTimeAverage() {
      int var1 = 0;
      long var2 = 0L;
      Iterator var4 = this.getOperationsData().iterator();

      while(var4.hasNext()) {
         WseeBaseOperationRuntimeData var5 = (WseeBaseOperationRuntimeData)var4.next();
         if (var5.getInvocationCount() > 0) {
            int var6 = var5.getResponseCount();
            if (var6 > 0) {
               var1 += var6;
               var2 += var5.getResponseTimeTotal();
            }
         }
      }

      return average(var2, var1);
   }

   private static long average(long var0, int var2) {
      return Math.round((double)var0 / (double)var2);
   }

   private Set<WseeBaseOperationRuntimeData> getOperationsData() {
      return ((WseeBasePortRuntimeData)this.getParentData()).getOperationsData();
   }
}
