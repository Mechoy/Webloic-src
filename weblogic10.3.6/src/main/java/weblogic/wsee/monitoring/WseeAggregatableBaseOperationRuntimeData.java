package weblogic.wsee.monitoring;

import com.sun.istack.Nullable;

public abstract class WseeAggregatableBaseOperationRuntimeData extends WseeBaseRuntimeData {
   public WseeAggregatableBaseOperationRuntimeData(String var1, @Nullable WseeBaseRuntimeData var2) {
      super(var1, var2);
   }

   public int getErrorCount() {
      throw this.unsupported("getErrorCount");
   }

   public int getInvocationCount() {
      throw this.unsupported("getInvocationCount");
   }

   public int getResponseCount() {
      throw this.unsupported("getResponseCount");
   }

   public int getResponseErrorCount() {
      throw this.unsupported("getResponseErrorCount");
   }

   public long getDispatchTimeTotal() {
      throw this.unsupported("getDispatchTimeTotal");
   }

   public long getExecutionTimeTotal() {
      throw this.unsupported("getExecutionTimeTotal");
   }

   public long getResponseTimeTotal() {
      throw this.unsupported("getResponseTimeTotal");
   }

   public long getDispatchTimeHigh() {
      throw this.unsupported("getDispatchTimeHigh");
   }

   public long getExecutionTimeHigh() {
      throw this.unsupported("getExecutionTimeHigh");
   }

   public long getResponseTimeHigh() {
      throw this.unsupported("getResponseTimeHigh");
   }

   public long getLastErrorTime() {
      throw this.unsupported("getLastErrorTime");
   }

   public long getLastInvocationTime() {
      throw this.unsupported("getLastInvocationTime");
   }

   public long getLastResponseTime() {
      throw this.unsupported("getLastResponseTime");
   }

   public long getLastResponseErrorTime() {
      throw this.unsupported("getLastResponseErrorTime");
   }

   public long getDispatchTimeLow() {
      throw this.unsupported("getDispatchTimeLow");
   }

   public long getExecutionTimeLow() {
      throw this.unsupported("getResponseTimeLow");
   }

   public long getResponseTimeLow() {
      throw this.unsupported("getResponseTimeLow");
   }

   public String getLastError() {
      throw this.unsupported("getLastError");
   }

   public String getLastResponseError() {
      throw this.unsupported("getLastResponseError");
   }

   public long getDispatchTimeAverage() {
      throw this.unsupported("getDispatchTimeAverage");
   }

   public long getExecutionTimeAverage() {
      throw this.unsupported("getExecutionTimeAverage");
   }

   public long getResponseTimeAverage() {
      throw this.unsupported("getResponseTimeAverage ");
   }

   private UnsupportedOperationException unsupported(String var1) {
      return new UnsupportedOperationException("WseeAggregatableBaseOperationRuntimeData." + var1);
   }
}
