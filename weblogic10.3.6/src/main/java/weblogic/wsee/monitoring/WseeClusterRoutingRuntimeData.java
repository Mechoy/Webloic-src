package weblogic.wsee.monitoring;

import com.sun.istack.Nullable;
import weblogic.management.ManagementException;

public final class WseeClusterRoutingRuntimeData extends WseeBaseRuntimeData {
   private int _requestCount = 0;
   private int _routedRequestCount = 0;
   private int _responseCount = 0;
   private int _routedResponseCount = 0;
   private int _routingFailureCount = 0;
   private String _lastRoutingFailure = null;
   private long _lastRoutingFailureTime = 0L;

   public WseeClusterRoutingRuntimeData(String var1, @Nullable WseeBaseRuntimeData var2) throws ManagementException {
      super(var1, var2);
   }

   public int getRequestCount() {
      return this._requestCount;
   }

   public int getRoutedRequestCount() {
      return this._routedRequestCount;
   }

   public int getResponseCount() {
      return this._responseCount;
   }

   public int getRoutedResponseCount() {
      return this._routedResponseCount;
   }

   public int getRoutingFailureCount() {
      return this._routingFailureCount;
   }

   public String getLastRoutingFailure() {
      return this._lastRoutingFailure;
   }

   public long getLastRoutingFailureTime() {
      return this._lastRoutingFailureTime;
   }

   public void incrementRequestCount() {
      ++this._requestCount;
   }

   public void incrementRoutedRequestCount() {
      ++this._routedRequestCount;
   }

   public void incrementResponseCount() {
      ++this._responseCount;
   }

   public void incrementRoutedResponseCount() {
      ++this._routedResponseCount;
   }

   public void incrementRoutingFailureCount() {
      ++this._routingFailureCount;
   }

   public void setLastRoutingFailure(String var1) {
      this._lastRoutingFailure = var1;
   }

   public void setLastRoutingFailureTime(long var1) {
      this._lastRoutingFailureTime = var1;
   }
}
