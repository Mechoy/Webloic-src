package weblogic.jms.saf;

import java.util.Date;
import javax.jms.JMSException;
import weblogic.jms.JMSLogger;
import weblogic.jms.backend.BEMessageManagementRuntimeDelegate;
import weblogic.jms.backend.BEQueueImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.forwarder.RuntimeHandler;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSRemoteEndpointRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.RedirectionListener;
import weblogic.messaging.saf.OperationState;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFLogger;
import weblogic.messaging.saf.internal.RemoteEndpointRuntimeCommonAddition;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManager;

public class SAFRemoteEndpointRuntimeMBeanImpl extends BEMessageManagementRuntimeDelegate implements JMSRemoteEndpointRuntimeMBean, RuntimeHandler, Runnable {
   private final String url;
   private final BEQueueImpl delegate;
   private Queue kernelQueue;
   private ErrorHandler errorHandler;
   private long failedMessagesCount;
   private SAFAgentRuntimeMBeanImpl agentRuntime;
   private RemoteEndpointRuntimeCommonAddition addition;
   private long expireAllStartedTime;
   private WorkManager workManager;

   SAFRemoteEndpointRuntimeMBeanImpl(String var1, String var2, SAFQueueImpl var3, SAFAgentRuntimeMBeanImpl var4, ErrorHandler var5) throws ManagementException {
      super(var1, (RuntimeMBean)null, false, var3);
      this.url = var2;
      this.kernelQueue = var3.getKernelQueue();
      this.errorHandler = var5;
      this.agentRuntime = var4;
      this.delegate = var3;
      this.addition = new RemoteEndpointRuntimeCommonAddition();
      this.workManager = JMSSAFManager.manager.getWorkManager();
   }

   public String getURL() {
      return this.url;
   }

   public String getEndpointType() {
      return "JMS";
   }

   public synchronized void pauseIncoming() throws SAFException {
      try {
         this.delegate.pauseProduction(false);
      } catch (JMSException var2) {
         throw new SAFException(var2);
      }

      SAFLogger.logIncomingPauseOfRemoteEndpoint(this.name);
   }

   public synchronized void resumeIncoming() throws SAFException {
      try {
         this.delegate.resumeProduction(false);
      } catch (JMSException var2) {
         throw new SAFException(var2);
      }

      SAFLogger.logIncomingResumeOfRemoteEndpoint(this.name);
   }

   public synchronized boolean isPausedForIncoming() {
      return this.delegate.isProductionPaused();
   }

   public synchronized void pauseForwarding() throws SAFException {
      try {
         this.delegate.pauseConsumption(false);
      } catch (JMSException var2) {
         throw new SAFException(var2);
      }

      SAFLogger.logForwardingPauseOfRemoteEndpoint(this.name);
   }

   public synchronized void resumeForwarding() throws SAFException {
      try {
         this.delegate.resumeConsumption(false);
      } catch (JMSException var2) {
         throw new SAFException(var2);
      }

      SAFLogger.logForwardingResumeOfRemoteEndpoint(this.name);
   }

   public synchronized boolean isPausedForForwarding() {
      return this.delegate.isConsumptionPaused();
   }

   public void purge() throws SAFException {
      try {
         this.deleteMessages("");
      } catch (ManagementException var2) {
         throw new SAFException(var2.getMessage(), var2);
      }
   }

   public void expireAll() {
      synchronized(this) {
         if (this.addition.getOperationState() == OperationState.RUNNING) {
            return;
         }

         this.addition.setOperationState(OperationState.RUNNING);
      }

      this.expireAllStartedTime = System.currentTimeMillis();
      this.workManager.schedule(this);
   }

   public void run() {
      Cursor var1 = null;
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("ExpireAll(): kernelQueue=" + this.kernelQueue.getName() + " messagesCurrentCount = " + this.kernelQueue.getStatistics().getMessagesCurrent());
      }

      try {
         var1 = this.kernelQueue.createCursor(true, (Expression)null, -1);
      } catch (KernelException var8) {
         var8.printStackTrace();
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("expireAll(): cursor.size() = " + (var1 == null ? 0 : var1.size()));
      }

      int var2 = 0;
      Object var3 = null;
      if (var1 != null) {
         MessageElement var4;
         try {
            while((var4 = var1.next()) != null && ((MessageImpl)var4.getMessage()).getJMSTimestamp() <= this.expireAllStartedTime) {
               if (this.errorHandler != null) {
                  this.errorHandler.handleFailure((RedirectionListener.Info)null, this.delegate.getBackEnd().getName(), (MessageImpl)var4.getMessage());
               }

               ++var2;
               KernelRequest var5 = this.kernelQueue.delete(var4);
               if (var5 != null) {
                  var5.getResult();
               }
            }
         } catch (KernelException var9) {
            var3 = var9;
         } catch (JMSException var10) {
            var3 = var10;
         }
      }

      this.updateFailedMessagesCount((long)var2);
      synchronized(this) {
         if (var3 != null) {
            this.addition.setOperationState(OperationState.STOPPED);
         } else {
            this.addition.setOperationState(OperationState.COMPLETED);
         }

      }
   }

   public synchronized long getMessagesCurrentCount() {
      return this.delegate.getMessagesCurrentCount() + this.delegate.getMessagesPendingCount();
   }

   public synchronized long getMessagesPendingCount() {
      return this.delegate.getMessagesPendingCount();
   }

   public synchronized long getMessagesHighCount() {
      return this.delegate.getMessagesHighCount();
   }

   public synchronized long getMessagesReceivedCount() {
      return this.delegate.getMessagesReceivedCount();
   }

   public synchronized long getMessagesThresholdTime() {
      return this.delegate.getMessagesThresholdTime();
   }

   public synchronized long getBytesPendingCount() {
      return this.delegate.getBytesPendingCount();
   }

   public synchronized long getBytesCurrentCount() {
      return this.delegate.getBytesCurrentCount();
   }

   public synchronized long getBytesHighCount() {
      return this.delegate.getBytesHighCount();
   }

   public synchronized long getBytesReceivedCount() {
      return this.delegate.getBytesReceivedCount();
   }

   public synchronized long getBytesThresholdTime() {
      return this.delegate.getBytesThresholdTime();
   }

   public long getFailedMessagesTotal() {
      return this.failedMessagesCount;
   }

   void updateFailedMessagesCount(long var1) {
      synchronized(this) {
         this.failedMessagesCount += var1;
      }

      this.agentRuntime.updateFailedMessagesCount(var1);
   }

   public boolean isConnected() {
      return this.addition.isConnected();
   }

   private void updateLastTimeConnected(long var1) {
      this.addition.updateLastTimeConnected(var1);
   }

   private void updateLastTimeDisconnected(long var1, Exception var3) {
      this.addition.updateLastTimeDisconnected(var1, var3);
   }

   public void connected() {
      if (!this.isConnected()) {
         JMSLogger.logSAFForwarderConnected(this.url);
      }

      this.addition.connected();
   }

   public void disconnected(Exception var1) {
      if (this.isConnected()) {
         JMSLogger.logSAFForwarderDisconnected(this.url, StackTraceUtils.throwable2StackTrace(var1));
      }

      this.addition.disconnected(var1);
   }

   public long getDowntimeHigh() {
      return this.addition.getDowntimeHigh();
   }

   public long getDowntimeTotal() {
      return this.addition.getDowntimeTotal();
   }

   public long getUptimeHigh() {
      return this.addition.getUptimeHigh();
   }

   public long getUptimeTotal() {
      return this.addition.getUptimeTotal();
   }

   public synchronized Date getLastTimeConnected() {
      return this.addition.getLastTimeConnected();
   }

   public synchronized Date getLastTimeFailedToConnect() {
      return this.addition.getLastTimeFailedToConnect();
   }

   public Exception getLastException() {
      return this.addition.getLastException();
   }

   public String getOperationState() {
      return this.addition.getOperationState().toString();
   }

   public void setOperationState(OperationState var1) {
      this.addition.setOperationState(var1);
   }
}
