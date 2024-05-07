package weblogic.ejb.container.internal;

import java.util.Date;
import java.util.Random;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.transaction.SystemException;
import weblogic.connector.external.EndpointActivationException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.deployer.MDBService;
import weblogic.ejb.container.deployer.MDBServiceActivator;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.container.monitoring.MessageDrivenEJBRuntimeMBeanImpl;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public abstract class MDConnectionManager implements TimerListener {
   protected static final DebugLogger debugLogger;
   protected MessageDrivenBeanInfo info;
   protected MessageDrivenEJBRuntimeMBeanImpl runtimeMBean;
   protected static final int STATE_DISCONNECTED = 1;
   protected static final int STATE_CONNECTED = 2;
   protected static final int STATE_UNDEPLOYING = 3;
   protected static final int STATE_UNDEPLOYED = 4;
   protected static final int STATE_SUSPENDED = 5;
   protected static final int STATE_CONNECTION_ERROR = 6;
   protected static final int STATE_SUSPENDED_CONNECTION_ERROR = 7;
   private static Random initConnectDelayRandomGenerator;
   protected String stateLock;
   protected int state = 1;
   private int reconnectionErrorInterval = 600000;
   protected int reconnectionCount = 0;
   private long lastReconnectionFailureTime = 0L;
   private Exception lastReconnectionFailureException = new Exception("init exception");
   private int jmsPollingIntervalMS;
   protected boolean scheduleResume;
   private int errorRepeatCount;
   private int deliveryFailureCount;
   private Throwable lastDeliveryFailureException = new Throwable("init exception");
   private int suspendInterval;
   protected String scheduleResumeLock;
   protected Timer timer;
   protected TimerManager timerManager;
   protected MessageDrivenManagerIntf mdManager;

   public MDConnectionManager(MessageDrivenBeanInfo var1, Context var2, MessageDrivenEJBRuntimeMBean var3) throws WLDeploymentException {
      this.info = var1;
      this.mdManager = (MessageDrivenManagerIntf)this.info.getBeanManager();
      this.runtimeMBean = (MessageDrivenEJBRuntimeMBeanImpl)var3;
      this.runtimeMBean.setMDConnManager(this);
      this.initDeliveryFailureParams();
      this.jmsPollingIntervalMS = this.info.getJMSPollingIntervalSeconds() * 1000;
      this.stateLock = new String("StateLock" + this.info.getName());
      this.scheduleResumeLock = new String("scheduleResumeLock" + this.info.getName());
      TimerManagerFactory var4 = TimerManagerFactory.getTimerManagerFactory();
      this.timerManager = var4.getDefaultTimerManager();
   }

   protected abstract void connect() throws WLDeploymentException, JMSException, SystemException, EndpointActivationException;

   protected abstract void disconnect(boolean var1) throws JMSException, EndpointActivationException;

   protected abstract void logException(Exception var1);

   public void timerExpired(Timer var1) {
      synchronized(this.scheduleResumeLock) {
         if (this.scheduleResume) {
            if (this.timer != null) {
               this.timer.cancel();
               this.timer = null;
            }

            this.resume(false);
            this.scheduleResume = false;
            this.errorRepeatCount = 0;
            return;
         }
      }

      String var2;
      if (this.info.getIsWeblogicJMS()) {
         var2 = this.mdManager.getDestinationName();
      } else {
         var2 = this.info.getResourceAdapterJndiName();
      }

      if (debugLogger.isDebugEnabled()) {
         debug("** Trying to reconnect to: " + var2);
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         if (this.getState() == 6) {
            try {
               this.disconnect(false);
            } catch (Exception var8) {
            }
         }

         if (this.getState() == 1) {
            this.connect();
         }

         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         MDBService var3 = MDBServiceActivator.INSTANCE.getMDBService();
         if (var3 != null) {
            var3.addDeployedMDB(this);
         }
      } catch (Exception var11) {
         this.runtimeMBean.setLastException(var11);
         long var4 = System.currentTimeMillis();
         if (!var11.toString().equals(this.lastReconnectionFailureException.toString()) || this.lastReconnectionFailureTime + (long)this.reconnectionErrorInterval <= var4) {
            this.lastReconnectionFailureTime = var4;
            this.lastReconnectionFailureException = var11;
            EJBLogger.logMDBReconnectInfo(this.info.getEJBName(), var2, this.reconnectionCount, this.jmsPollingIntervalMS / 1000, (long)(this.reconnectionErrorInterval / 1000));
            this.logException(var11);
         }
      }

      synchronized(this) {
         synchronized(this.stateLock) {
            if (this.state != 1 && this.state != 6 && this.timer != null) {
               this.timer.cancel();
               this.timer = null;
            }
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debug("** Connect attempt for: " + var2 + " was: " + (this.getState() == 2 ? "Successful" : "unsuccessful"));
      }

   }

   public synchronized void startConnectionPolling() throws WLDeploymentException {
      if (this.timer == null) {
         if (this.state == 4) {
            this.setState(1);
         }

         if (debugLogger.isDebugEnabled()) {
            Debug.assertion(this.getState() == 1);
         }

         int var1 = this.getRandomInitialConnectDelay();
         if (var1 >= 5) {
            this.scheduleInitialConnection((long)var1);
         } else {
            try {
               if (debugLogger.isDebugEnabled()) {
                  this.debugState();
               }

               this.connect();
               if (debugLogger.isDebugEnabled()) {
                  this.debugState();
               }

               MDBService var2 = MDBServiceActivator.INSTANCE.getMDBService();
               if (var2 != null) {
                  var2.addDeployedMDB(this);
               }

               assert this.getState() == 2;
            } catch (Exception var3) {
               if (var3 instanceof EndpointActivationException && !((EndpointActivationException)var3).isChangeable()) {
                  throw new WLDeploymentException(var3.getMessage() + StackTraceUtils.throwable2StackTrace(var3));
               }

               this.runtimeMBean.setLastException(var3);
               this.logException(var3);
            }

            if (debugLogger.isDebugEnabled()) {
               this.debugState();
            }

            if (this.getState() != 2) {
               this.scheduleReconnection();
            }

         }
      }
   }

   public void cancelConnectionPolling() {
      MDBService var1 = MDBServiceActivator.INSTANCE.getMDBService();
      if (var1 != null) {
         var1.removeDeployedMDB(this);
      }

      synchronized(this) {
         this.setState(3);
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         try {
            this.disconnect(false);
         } catch (Exception var5) {
            this.runtimeMBean.setLastException(var5);
            if (debugLogger.isDebugEnabled()) {
               debug("exception on disconnect: " + var5);
            }
         }

         if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
         }

      }
   }

   public void deleteDurableSubscriber() {
   }

   private int getRandomInitialConnectDelay() {
      boolean var1 = this.info.getMinimizeAQSessions();
      if (!var1) {
         return -1;
      } else {
         int var2 = NewJMSMessagePoller.DESTINATION_POLL_INTERVAL_MILLIS;
         if (var2 <= 5000) {
            return -1;
         } else {
            if (initConnectDelayRandomGenerator == null) {
               initConnectDelayRandomGenerator = new Random();
            }

            int var3 = initConnectDelayRandomGenerator.nextInt(var2);
            if (debugLogger.isDebugEnabled()) {
               debug("Initial connect delay is " + var3);
            }

            return var3;
         }
      }
   }

   private synchronized void scheduleInitialConnection(long var1) {
      if (!this.info.getIsInactive()) {
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         synchronized(this.stateLock) {
            if (this.state != 1 && this.state != 6) {
               return;
            }
         }

         this.timer = this.timerManager.scheduleAtFixedRate(this, var1, (long)this.jmsPollingIntervalMS);
         if (debugLogger.isDebugEnabled()) {
            debug("Initial connection is scheduled at delay of " + var1);
         }

      }
   }

   protected synchronized void scheduleReconnection() {
      if (!this.info.getIsInactive()) {
         if (this.scheduleResume) {
            this.initDeliveryFailureParams();
            if (this.timer != null) {
               this.timer.cancel();
               this.timer = null;
            }
         }

         if (this.timer == null) {
            if (debugLogger.isDebugEnabled()) {
               this.debugState();
            }

            synchronized(this.stateLock) {
               if (this.state != 1 && this.state != 6) {
                  return;
               }
            }

            this.timer = this.timerManager.scheduleAtFixedRate(this, (long)this.jmsPollingIntervalMS, (long)this.jmsPollingIntervalMS);
            if (debugLogger.isDebugEnabled()) {
               debug("Reconnection is scheduled at interval of every " + this.jmsPollingIntervalMS);
            }

            this.reconnectionCount = 1;
            this.lastReconnectionFailureTime = System.currentTimeMillis();
            this.lastReconnectionFailureException = new Exception("init exception");
         }
      }
   }

   protected synchronized void scheduleResume() {
      if (this.timer == null) {
         this.scheduleResume = true;
         if (this.info.getMaxSuspendSeconds() == 0) {
            this.suspendInterval = 0;
         }

         this.timer = this.timerManager.schedule(this, (long)this.suspendInterval);
      }
   }

   protected void initDeliveryFailureParams() {
      this.scheduleResume = false;
      this.errorRepeatCount = 0;
      this.suspendInterval = this.info.getInitSuspendSeconds() * 1000;
      if (this.info.getInitSuspendSeconds() == 0) {
         this.suspendInterval = 5000;
      }

      if (this.info.getMaxSuspendSeconds() == 0) {
         this.suspendInterval = 0;
      }

      this.deliveryFailureCount = 0;
      this.lastDeliveryFailureException = new Throwable("init exception");
   }

   protected boolean isPrintErrorMessage(Throwable var1) {
      boolean var2 = false;
      ++this.deliveryFailureCount;
      this.runtimeMBean.setLastException(var1);
      if (var1.toString().equals(this.lastDeliveryFailureException.toString()) && this.suspendInterval != 0) {
         ++this.errorRepeatCount;
         if (this.errorRepeatCount >= 10) {
            SuspendThread var3 = new SuspendThread(this);
            WorkManagerFactory.getInstance().getSystem().schedule(var3);
            var2 = true;
         }
      } else {
         var2 = true;
         this.errorRepeatCount = 1;
         this.lastDeliveryFailureException = var1;
      }

      return var2;
   }

   protected void callSuspend() {
      synchronized(this.scheduleResumeLock) {
         if (this.timer == null) {
            this.suspend(false);
            this.scheduleResume();
            EJBLogger.logMDBRedeliveryInfo(this.info.getEJBName(), this.deliveryFailureCount, (long)(this.suspendInterval / 1000));
            long var2 = System.currentTimeMillis();
            this.runtimeMBean.setMDBStatus("Suspended at " + new Date(var2) + " by the EJB container, resume is scheduled at " + new Date(var2 + (long)this.suspendInterval));
            this.suspendInterval *= 2;
            if (this.suspendInterval > this.info.getMaxSuspendSeconds() * 1000) {
               this.suspendInterval = this.info.getMaxSuspendSeconds() * 1000;
            }

         }
      }
   }

   protected int getState() {
      synchronized(this.stateLock) {
         return this.state;
      }
   }

   protected int setState(int var1) {
      synchronized(this.stateLock) {
         int var3 = this.state;
         this.state = var1;
         return var3;
      }
   }

   protected void debugState() {
      StringBuffer var1 = new StringBuffer();
      var1.append("MDB ");
      var1.append(this.info.getName());
      var1.append(": State = ");
      synchronized(this.stateLock) {
         switch (this.state) {
            case 1:
               var1.append("DISCONNECTED");
               break;
            case 2:
               var1.append("CONNECTED");
               break;
            case 3:
               var1.append("UNDEPLOYING");
               break;
            case 4:
               var1.append("UNDEPLOYED");
               break;
            case 5:
               var1.append("SUSPENDED");
               break;
            case 6:
               var1.append("CONNECTION_ERROR");
               break;
            case 7:
               var1.append("SUSPENDED_CONNECTION_ERROR");
               break;
            default:
               var1.append("<unknown>");
         }
      }

      debug(var1.toString());
   }

   public void updateJMSPollingIntervalSeconds(int var1) {
      this.jmsPollingIntervalMS = var1 * 1000;
   }

   public abstract void signalBackgroundThreads();

   public abstract boolean suspend(boolean var1);

   public abstract boolean resume(boolean var1);

   public abstract void shutdown();

   private static void debug(String var0) {
      debugLogger.debug("[MDConnectionManager] " + var0);
   }

   static {
      debugLogger = EJBDebugService.mdbConnectionLogger;
      initConnectDelayRandomGenerator = null;
   }

   class SuspendThread implements Runnable {
      private MDConnectionManager mdConnectionManager;

      SuspendThread(MDConnectionManager var2) {
         this.mdConnectionManager = var2;
      }

      public void run() {
         this.mdConnectionManager.callSuspend();
      }
   }
}
