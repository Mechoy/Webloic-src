package weblogic.wsee.monitoring;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.wsee.util.Verbose;

public class WseeBaseOperationRuntimeData extends WseeAggregatableBaseOperationRuntimeData {
   public static final String WS_PROTOCOL_OP_NAME = "Ws-Protocol";
   private static final long nanoPerMilli = 1000000L;
   private static final boolean verbose = Verbose.isVerbose(WseeBaseOperationRuntimeData.class);
   private int dispatchCount = 0;
   private long dispatchTimeTotal = 0L;
   private long dispatchTimeHigh = 0L;
   private long dispatchTimeLow = 0L;
   private int executionCount = 0;
   private long executionTimeTotal = 0L;
   private long executionTimeHigh = 0L;
   private long executionTimeLow = 0L;
   private int responseCount = 0;
   private long responseTimeTotal = 0L;
   private long responseTimeHigh = 0L;
   private long responseTimeLow = 0L;
   private int responseErrorCount = 0;
   private Throwable lastResponseError = null;
   private long lastInvocationTime;
   private int errorCount;
   private Throwable lastError = null;
   private long lastErrorTime;
   private long lastResponseTime;
   private long lastResponseErrorTime;
   private long lastResetTime;

   public static WseeBaseOperationRuntimeData createWsProtocolOp(WseeBaseRuntimeData var0) throws ManagementException {
      WseeBaseOperationRuntimeData var1 = new WseeBaseOperationRuntimeData("Ws-Protocol");
      var1.setParentData(var0);
      return var1;
   }

   WseeBaseOperationRuntimeData(String var1) {
      super(var1, (WseeBaseRuntimeData)null);
   }

   public String getOperationName() {
      return this.getName();
   }

   public int getInvocationCount() {
      return this.dispatchCount;
   }

   public long getDispatchTimeTotal() {
      return this.dispatchTimeTotal / 1000000L;
   }

   public long getDispatchTimeHigh() {
      return this.dispatchTimeHigh / 1000000L;
   }

   public long getDispatchTimeLow() {
      return this.dispatchTimeLow / 1000000L;
   }

   public long getDispatchTimeAverage() {
      return this.dispatchCount == 0 ? 0L : this.dispatchTimeTotal / (long)this.dispatchCount / 1000000L;
   }

   public long getExecutionTimeTotal() {
      return this.executionTimeTotal / 1000000L;
   }

   public long getExecutionTimeHigh() {
      return this.executionTimeHigh / 1000000L;
   }

   public long getExecutionTimeLow() {
      return this.executionTimeLow / 1000000L;
   }

   public long getExecutionTimeAverage() {
      return this.executionCount == 0 ? 0L : this.executionTimeTotal / (long)this.executionCount / 1000000L;
   }

   public int getResponseCount() {
      return this.responseCount;
   }

   public long getResponseTimeTotal() {
      return this.responseTimeTotal / 1000000L;
   }

   public long getResponseTimeHigh() {
      return this.responseTimeHigh / 1000000L;
   }

   public long getResponseTimeLow() {
      return this.responseTimeLow / 1000000L;
   }

   public long getResponseTimeAverage() {
      return this.responseCount == 0 ? 0L : this.responseTimeTotal / (long)this.responseCount / 1000000L;
   }

   public int getResponseErrorCount() {
      return this.responseErrorCount;
   }

   public long getLastInvocationTime() {
      return this.lastInvocationTime;
   }

   public int getErrorCount() {
      return this.errorCount;
   }

   public String getLastError() {
      return this.lastError != null ? this.lastError.toString() : null;
   }

   public long getLastErrorTime() {
      return this.lastErrorTime;
   }

   public long getLastResponseTime() {
      return this.lastResponseTime;
   }

   public long getLastResponseErrorTime() {
      return this.lastResponseErrorTime;
   }

   public String getLastResponseError() {
      return this.lastResponseError != null ? this.lastResponseError.toString() : null;
   }

   public void reportInvocation(long var1, long var3, long var5) {
      if (verbose) {
         Verbose.log((Object)("WSEE[MONITORING[Invocation[DispatchTime=" + var1 + "][ExecutionTime=" + var3 + "][ResponseTime=" + var5 + "]]]"));
      }

      synchronized(this) {
         this.unsync_reportDispatch(var1);
         this.unsync_reportExecution(var3);
         this.unsync_reportResponse(var5);
      }
   }

   public void reportOnewayInvocation(long var1, long var3) {
      if (verbose) {
         Verbose.log((Object)("WSEE[MONITORING[Invocation[DispatchTime=" + var1 + "][ExecutionTime=" + var3 + "]]]"));
      }

      synchronized(this) {
         this.unsync_reportDispatch(var1);
         this.unsync_reportExecution(var3);
      }
   }

   public void reportDispatch(long var1) {
      synchronized(this) {
         this.unsync_reportDispatch(var1);
      }
   }

   public void reportExecution(long var1) {
      synchronized(this) {
         this.unsync_reportExecution(var1);
      }
   }

   public void reportResponse(long var1) {
      synchronized(this) {
         this.unsync_reportResponse(var1);
      }
   }

   public void reportError(Throwable var1) {
      synchronized(this) {
         ++this.errorCount;
         this.lastError = var1;
         this.lastErrorTime = System.currentTimeMillis();
      }
   }

   public void reportResponseError(Throwable var1) {
      synchronized(this) {
         ++this.responseErrorCount;
         this.lastResponseError = var1;
         this.lastResponseErrorTime = System.currentTimeMillis();
      }
   }

   private void unsync_reportResponse(long var1) {
      if (var1 >= 0L) {
         this.lastResponseTime = System.currentTimeMillis();
         if (this.responseCount == 0) {
            this.responseTimeTotal = var1;
            this.responseTimeHigh = var1;
            this.responseTimeLow = var1;
            this.responseCount = 1;
         } else {
            ++this.responseCount;
            this.responseTimeTotal += var1;
            if (var1 > this.responseTimeHigh) {
               this.responseTimeHigh = var1;
            }

            if (var1 < this.responseTimeLow) {
               this.responseTimeLow = var1;
            }
         }
      }

   }

   private void unsync_reportExecution(long var1) {
      if (var1 > 0L) {
         if (this.executionCount == 0) {
            this.executionTimeTotal = var1;
            this.executionTimeHigh = var1;
            this.executionTimeLow = var1;
            this.executionCount = 1;
         } else {
            ++this.executionCount;
            this.executionTimeTotal += var1;
            if (var1 > this.executionTimeHigh) {
               this.executionTimeHigh = var1;
            }

            if (var1 < this.executionTimeLow) {
               this.executionTimeLow = var1;
            }
         }
      }

   }

   private void unsync_reportDispatch(long var1) {
      if (var1 >= 0L) {
         this.lastInvocationTime = System.currentTimeMillis();
         if (this.dispatchCount == 0) {
            this.dispatchTimeTotal = var1;
            this.dispatchTimeHigh = var1;
            this.dispatchTimeLow = var1;
            this.dispatchCount = 1;
         } else {
            ++this.dispatchCount;
            this.dispatchTimeTotal += var1;
            if (var1 > this.dispatchTimeHigh) {
               this.dispatchTimeHigh = var1;
            }

            if (var1 < this.dispatchTimeLow) {
               this.dispatchTimeLow = var1;
            }
         }
      }

   }

   public void reset() {
      synchronized(this) {
         this.lastResetTime = System.currentTimeMillis();
         this.executionCount = 0;
         this.dispatchTimeTotal = 0L;
         this.dispatchTimeHigh = 0L;
         this.dispatchTimeLow = 0L;
         this.executionTimeTotal = 0L;
         this.executionTimeHigh = 0L;
         this.executionTimeLow = 0L;
         this.lastResponseErrorTime = 0L;
         this.lastResponseError = null;
         this.lastInvocationTime = 0L;
         this.errorCount = 0;
         this.lastError = null;
         this.lastErrorTime = 0L;
         this.lastResponseTime = 0L;
         this.lastResponseErrorTime = 0L;
      }
   }

   public Date getLastResetTime() {
      return new Date(this.lastResetTime);
   }
}
