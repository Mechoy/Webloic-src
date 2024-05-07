package weblogic.wsee.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import weblogic.management.ManagementException;
import weblogic.wsee.mc.mbean.WseeMcRuntimeData;

class WseeBasePortRuntimeData extends WseeBaseRuntimeData {
   private String transport = null;
   private Set<WseeBaseOperationRuntimeData> operations = new HashSet();
   private Set<WseeHandlerRuntimeData> handlers = new HashSet();
   private long startTime = 0L;
   private WseePortPolicyRuntimeData metric = null;
   private WseeClusterRoutingRuntimeData _clusterRouting = null;
   private WseeWsrmRuntimeData _wsrm = null;
   private WseeMcRuntimeData _mc = null;
   private WseeBasePortAggregatedBaseOperationsRuntimeData _aggregatedBaseOperations = null;
   private ReentrantReadWriteLock _childLock = new ReentrantReadWriteLock();

   WseeBasePortRuntimeData(String var1, String var2) throws ManagementException {
      super(var1, (WseeBaseRuntimeData)null);
      this.transport = var2;
   }

   public boolean addOperation(WseeBaseOperationRuntimeData var1) {
      Iterator var2 = this.operations.iterator();

      WseeBaseOperationRuntimeData var3;
      do {
         if (!var2.hasNext()) {
            var1.setParentData(this);
            this.operations.add(var1);
            return true;
         }

         var3 = (WseeBaseOperationRuntimeData)var2.next();
      } while(!var3.getName().equals(var1.getName()));

      return false;
   }

   public boolean addHandler(WseeHandlerRuntimeData var1) {
      Iterator var2 = this.handlers.iterator();

      WseeHandlerRuntimeData var3;
      do {
         if (!var2.hasNext()) {
            var1.setParentData(this);
            this.handlers.add(var1);
            return true;
         }

         var3 = (WseeHandlerRuntimeData)var2.next();
      } while(!var3.getName().equals(var1.getName()));

      return false;
   }

   public void setPortPolicy(WseePortPolicyRuntimeData var1) {
      this.metric = var1;
   }

   public String getPortName() {
      return this.getName();
   }

   public String getTransportProtocolType() {
      return this.transport;
   }

   public WseeOperationRuntimeData[] getOperations() {
      return (WseeOperationRuntimeData[])this.operations.toArray(new WseeOperationRuntimeData[this.operations.size()]);
   }

   public WseeHandlerRuntimeData[] getHandlers() {
      return (WseeHandlerRuntimeData[])this.handlers.toArray(new WseeHandlerRuntimeData[this.handlers.size()]);
   }

   public int getPolicyFaults() {
      return this.metric.getPolicyFaults();
   }

   void setStartTime(long var1) {
      this.startTime = var1;
   }

   public long getStartTime() {
      return this.startTime;
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      return this.metric.getTotalFaults();
   }

   public int getTotalSecurityFaults() {
      return this.metric.getTotalSecurityFaults();
   }

   public WseePortPolicyRuntimeData getPortPolicy() {
      return this.metric;
   }

   public void setClusterRouting(WseeClusterRoutingRuntimeData var1) {
      try {
         this._childLock.writeLock().lock();
         this._clusterRouting = var1;
      } finally {
         this._childLock.writeLock().unlock();
      }

   }

   public WseeClusterRoutingRuntimeData getClusterRouting() {
      WseeClusterRoutingRuntimeData var1;
      try {
         this._childLock.readLock().lock();
         var1 = this._clusterRouting;
      } finally {
         this._childLock.readLock().unlock();
      }

      return var1;
   }

   public void setWsrm(WseeWsrmRuntimeData var1) {
      try {
         this._childLock.writeLock().lock();
         this._wsrm = var1;
      } finally {
         this._childLock.writeLock().unlock();
      }

   }

   public WseeWsrmRuntimeData getWsrm() {
      WseeWsrmRuntimeData var1;
      try {
         this._childLock.readLock().lock();
         var1 = this._wsrm;
      } finally {
         this._childLock.readLock().unlock();
      }

      return var1;
   }

   public void setMcData(WseeMcRuntimeData var1) {
      try {
         this._childLock.writeLock().lock();
         this._mc = var1;
      } finally {
         this._childLock.writeLock().unlock();
      }

   }

   public WseeMcRuntimeData getMcData() {
      WseeMcRuntimeData var1;
      try {
         this._childLock.readLock().lock();
         var1 = this._mc;
      } finally {
         this._childLock.readLock().unlock();
      }

      return var1;
   }

   public void setAggregatedBaseOperationsData(WseeBasePortAggregatedBaseOperationsRuntimeData var1) {
      try {
         this._childLock.writeLock().lock();
         this._aggregatedBaseOperations = var1;
      } finally {
         this._childLock.writeLock().unlock();
      }

   }

   public WseeBasePortAggregatedBaseOperationsRuntimeData getAggregatedBaseOperationsData() {
      WseeBasePortAggregatedBaseOperationsRuntimeData var1;
      try {
         this._childLock.readLock().lock();
         var1 = this._aggregatedBaseOperations;
      } finally {
         this._childLock.readLock().unlock();
      }

      return var1;
   }

   Set<WseeBaseOperationRuntimeData> getOperationsData() {
      return this.operations;
   }
}
