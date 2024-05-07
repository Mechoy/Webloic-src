package weblogic.jms.backend;

import javax.jms.Destination;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.extensions.DestinationInfo;
import weblogic.jms.extensions.WLDestination;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;
import weblogic.management.runtime.JMSDurableSubscriberRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

class BEDestinationRuntimeMBeanImpl extends BEMessageManagementRuntimeDelegate implements JMSDestinationRuntimeMBean {
   private final BEDestinationImpl delegate;

   BEDestinationRuntimeMBeanImpl(String var1, RuntimeMBean var2, boolean var3, BEDestinationImpl var4) throws ManagementException {
      super(var1, var2, var3);
      this.delegate = var4;
   }

   public Destination getDestination() {
      return this.delegate.getDestination();
   }

   public CompositeData getDestinationInfo() {
      try {
         WLDestination var1 = (WLDestination)this.delegate.getDestination();
         DestinationInfo var2 = new DestinationInfo(var1);
         return var2.toCompositeData();
      } catch (OpenDataException var3) {
         return null;
      }
   }

   public void createDurableSubscriber(String var1, String var2, String var3, boolean var4) throws InvalidSelectorException, JMSException {
      if (this.delegate instanceof BEQueueImpl) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logBadDurableSubscriptionLoggable(var1, var2, var3, this.name));
      } else {
         ((BETopicImpl)this.delegate).createDurableSubscriber(var1, var2, var3, var4);
      }
   }

   public void destroyJMSDurableSubscriberRuntime(JMSDurableSubscriberRuntimeMBean var1) throws InvalidSelectorException, JMSException {
      if (this.delegate instanceof BEQueueImpl) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logBadDurableSubscriptionLoggable(var1.getClientID(), var1.getName(), var1.getSelector(), this.name));
      } else if (var1 != null) {
         var1.destroy();
      }
   }

   public JMSDurableSubscriberRuntimeMBean[] getJMSDurableSubscriberRuntimes() {
      return this.getDurableSubscribers();
   }

   public JMSDurableSubscriberRuntimeMBean[] getDurableSubscribers() {
      return this.delegate instanceof BEQueueImpl ? null : ((BETopicImpl)this.delegate).getDurableSubscribers();
   }

   public long getConsumersCurrentCount() {
      return this.delegate.getConsumersCurrentCount();
   }

   public long getConsumersHighCount() {
      return this.delegate.getConsumersHighCount();
   }

   public long getConsumersTotalCount() {
      return this.delegate.getConsumersTotalCount();
   }

   public long getMessagesCurrentCount() {
      return this.delegate.getMessagesCurrentCount();
   }

   public long getMessagesPendingCount() {
      return this.delegate.getMessagesPendingCount();
   }

   public long getMessagesHighCount() {
      return this.delegate.getMessagesHighCount();
   }

   public long getMessagesReceivedCount() {
      return this.delegate.getMessagesReceivedCount();
   }

   public long getMessagesThresholdTime() {
      return this.delegate.getMessagesThresholdTime();
   }

   public long getBytesCurrentCount() {
      return this.delegate.getBytesCurrentCount();
   }

   public long getBytesPendingCount() {
      return this.delegate.getBytesPendingCount();
   }

   public long getBytesHighCount() {
      return this.delegate.getBytesHighCount();
   }

   public long getBytesReceivedCount() {
      return this.delegate.getBytesReceivedCount();
   }

   public long getBytesThresholdTime() {
      return this.delegate.getBytesThresholdTime();
   }

   public String getDestinationType() {
      return this.delegate.getDestinationType();
   }

   /** @deprecated */
   public void pause() {
      this.delegate.pause();
   }

   /** @deprecated */
   public void resume() {
      this.delegate.resume();
   }

   public String getState() {
      return this.delegate.getState();
   }

   public boolean isPaused() throws JMSException {
      return this.delegate.isPaused();
   }

   public void pauseProduction() throws JMSException {
      this.delegate.pauseProduction();
   }

   public boolean isProductionPaused() {
      return this.delegate.isProductionPaused();
   }

   public String getProductionPausedState() {
      return this.delegate.getProductionPausedState();
   }

   public void resumeProduction() throws JMSException {
      this.delegate.resumeProduction();
   }

   public void pauseInsertion() throws JMSException {
      this.delegate.pauseInsertion();
   }

   public boolean isInsertionPaused() {
      return this.delegate.isInsertionPaused();
   }

   public String getInsertionPausedState() {
      return this.delegate.getInsertionPausedState();
   }

   public void resumeInsertion() throws JMSException {
      this.delegate.resumeInsertion();
   }

   public void pauseConsumption() throws JMSException {
      this.delegate.pauseConsumption();
   }

   public boolean isConsumptionPaused() {
      return this.delegate.isConsumptionPaused();
   }

   public String getConsumptionPausedState() {
      return this.delegate.getConsumptionPausedState();
   }

   public void resumeConsumption() throws JMSException {
      this.delegate.resumeConsumption();
   }

   public void lowMemory() throws JMSException {
      this.delegate.lowMemory();
   }

   public void normalMemory() throws JMSException {
      this.delegate.normalMemory();
   }

   public void suspendMessageLogging() throws JMSException {
      this.delegate.suspendMessageLogging();
   }

   public void resumeMessageLogging() throws JMSException {
      this.delegate.resumeMessageLogging();
   }

   public boolean isMessageLogging() throws JMSException {
      return this.delegate.isMessageLogging();
   }

   public void mydelete() throws JMSException {
      this.delegate.adminDeletion();
   }

   public String toString() {
      return "BEDestinationRuntimeMBeanImpl(" + System.identityHashCode(this) + "," + this.getName() + ")";
   }
}
