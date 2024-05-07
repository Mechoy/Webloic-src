package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.internal.MDConnectionManager;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;

public final class MessageDrivenEJBRuntimeMBeanImpl extends EJBRuntimeMBeanImpl implements MessageDrivenEJBRuntimeMBean {
   private EJBPoolRuntimeMBean poolRtMBean;
   private EJBTimerRuntimeMBean timerRtMBean;
   private boolean isJMSConnectionAlive;
   private String connectionStatus;
   private String destination;
   private String jmsClientID;
   private String mdbStatus;
   private AtomicInteger suspendCount;
   private Throwable lastException;
   private MDConnectionManager mdConnManager;
   private String applicationName;

   public MessageDrivenEJBRuntimeMBeanImpl(String var1, String var2, EJBComponentRuntimeMBean var3, String var4, TimerManager var5) throws ManagementException {
      super(var1, var2, var3);
      this.poolRtMBean = new EJBPoolRuntimeMBeanImpl(var1, this);
      if (var5 != null) {
         this.timerRtMBean = new EJBTimerRuntimeMBeanImpl(var1, this, var5);
      }

      this.isJMSConnectionAlive = false;
      this.connectionStatus = "disconnected";
      this.destination = var4;
      this.jmsClientID = "";
      this.mdbStatus = "initializing";
      this.suspendCount = new AtomicInteger(0);
      ApplicationRuntimeMBean var6 = (ApplicationRuntimeMBean)var3.getParent();
      this.applicationName = var6.getApplicationName();
      HealthMonitorService.register("MDB" + this.applicationName, this, false);
   }

   public void unregister() throws ManagementException {
      super.unregister();
      HealthMonitorService.unregister("MDB" + this.applicationName);
   }

   public HealthState getHealthState() {
      byte var1;
      String var2;
      if (this.isJMSConnectionAlive()) {
         var1 = 0;
         var2 = "MDB application " + this.applicationName + " is connected to messaging system.";
      } else {
         var1 = 1;
         var2 = "MDB application " + this.applicationName + " is NOT connected to messaging system.";
      }

      return new HealthState(var1, var2);
   }

   public EJBPoolRuntimeMBean getPoolRuntime() {
      return this.poolRtMBean;
   }

   public EJBTimerRuntimeMBean getTimerRuntime() {
      return this.timerRtMBean;
   }

   public boolean isJMSConnectionAlive() {
      return this.isJMSConnectionAlive;
   }

   public void setJMSConnectionAlive(boolean var1) {
      this.isJMSConnectionAlive = var1;
   }

   public String getConnectionStatus() {
      return this.connectionStatus;
   }

   public void setConnectionStatus(String var1) {
      this.connectionStatus = var1;
   }

   public String getJmsClientID() {
      return this.jmsClientID;
   }

   public void setJmsClientID(String var1) {
      this.jmsClientID = var1;
   }

   public String getDestination() {
      return this.destination;
   }

   public String getMDBStatus() {
      return this.mdbStatus;
   }

   public void setMDBStatus(String var1) {
      this.mdbStatus = var1;
   }

   public long getProcessedMessageCount() {
      return this.getPoolRuntime().getAccessTotalCount();
   }

   public int getSuspendCount() {
      return this.suspendCount.get();
   }

   public void incrementSuspendCount() {
      this.suspendCount.incrementAndGet();
   }

   public Throwable getLastException() {
      return this.lastException;
   }

   public String getLastExceptionAsString() {
      return this.lastException != null ? this.lastException.toString() : "";
   }

   public void setLastException(Throwable var1) {
      this.lastException = var1;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public void setMDConnManager(MDConnectionManager var1) {
      this.mdConnManager = var1;
   }

   public boolean suspend() throws ManagementException {
      if (this.mdConnManager == null) {
         throw new ManagementException("the destination is not available");
      } else {
         return this.mdConnManager.suspend(true);
      }
   }

   public boolean resume() throws ManagementException {
      if (this.mdConnManager == null) {
         throw new ManagementException("the destination is not available");
      } else {
         return this.mdConnManager.resume(true);
      }
   }
}
