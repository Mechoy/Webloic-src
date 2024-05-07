package weblogic.jms.saf;

import javax.jms.JMSException;
import weblogic.health.HealthState;
import weblogic.jms.backend.BackEnd;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SAFAgentRuntimeMBean;
import weblogic.management.runtime.SAFConversationRuntimeMBean;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFLogger;

class SAFAgentRuntimeMBeanImpl extends RuntimeMBeanDelegate implements SAFAgentRuntimeMBean {
   private final SAFAgentAdmin safAgent;
   private final BackEnd backEnd;
   private long failedMessagesCount;

   SAFAgentRuntimeMBeanImpl(String var1, SAFAgentAdmin var2, RuntimeMBean var3) throws ManagementException {
      super(var1, (RuntimeMBean)null, false);
      this.safAgent = var2;
      this.backEnd = var2.getBackEnd();
   }

   public HealthState getHealthState() {
      return this.backEnd != null ? this.backEnd.getHealthState() : new HealthState(0);
   }

   public SAFRemoteEndpointRuntimeMBean[] getRemoteEndpoints() {
      return this.safAgent.getRemoteEndpoints();
   }

   public long getRemoteEndpointsCurrentCount() {
      return this.safAgent.getRemoteEndpointsCurrentCount();
   }

   public long getRemoteEndpointsHighCount() {
      return this.safAgent.getRemoteEndpointsHighCount();
   }

   public long getRemoteEndpointsTotalCount() {
      return this.safAgent.getRemoteEndpointsTotalCount();
   }

   public void pauseIncoming() throws SAFException {
      if (this.backEnd != null) {
         try {
            this.backEnd.pauseProduction(false);
         } catch (JMSException var2) {
            throw new SAFException(var2.getMessage());
         }

         SAFLogger.logIncomingPauseOfSAFAgent(this.getName());
      }
   }

   public void resumeIncoming() throws SAFException {
      if (this.backEnd != null) {
         try {
            this.backEnd.resumeProduction(false);
         } catch (JMSException var2) {
            throw new SAFException(var2.getMessage());
         }

         SAFLogger.logIncomingResumeOfSAFAgent(this.getName());
      }
   }

   public boolean isPausedForIncoming() {
      return this.backEnd == null ? false : this.backEnd.isProductionPaused();
   }

   public void pauseForwarding() throws SAFException {
      if (this.backEnd != null) {
         try {
            this.backEnd.pauseConsumption(false);
         } catch (JMSException var2) {
            throw new SAFException(var2.getMessage());
         }

         SAFLogger.logForwardingPauseOfSAFAgent(this.getName());
      }
   }

   public void resumeForwarding() throws SAFException {
      if (this.backEnd != null) {
         try {
            this.backEnd.resumeConsumption(false);
         } catch (JMSException var2) {
            throw new SAFException(var2.getMessage());
         }

         SAFLogger.logForwardingResumeOfSAFAgent(this.getName());
      }
   }

   public boolean isPausedForForwarding() {
      return this.backEnd == null ? false : this.backEnd.isConsumptionPaused();
   }

   public void pauseReceiving() throws SAFException {
   }

   public void resumeReceiving() throws SAFException {
   }

   public boolean isPausedForReceiving() {
      return false;
   }

   public SAFConversationRuntimeMBean[] getConversations() {
      return new SAFConversationRuntimeMBean[0];
   }

   public long getConversationsCurrentCount() {
      return 0L;
   }

   public long getConversationsHighCount() {
      return 0L;
   }

   public long getConversationsTotalCount() {
      return 0L;
   }

   public long getMessagesCurrentCount() {
      return this.backEnd.getMessagesCurrentCount() + this.backEnd.getMessagesPendingCount();
   }

   public long getMessagesPendingCount() {
      return this.backEnd.getMessagesPendingCount();
   }

   public long getMessagesHighCount() {
      return this.backEnd.getMessagesHighCount();
   }

   public long getMessagesReceivedCount() {
      return this.backEnd.getMessagesReceivedCount();
   }

   public long getMessagesThresholdTime() {
      return this.backEnd.getMessagesThresholdTime();
   }

   public long getBytesPendingCount() {
      return this.backEnd.getBytesPendingCount();
   }

   public long getBytesCurrentCount() {
      return this.backEnd.getBytesCurrentCount();
   }

   public long getBytesHighCount() {
      return this.backEnd.getBytesHighCount();
   }

   public long getBytesReceivedCount() {
      return this.backEnd.getBytesReceivedCount();
   }

   public long getBytesThresholdTime() {
      return this.backEnd.getBytesThresholdTime();
   }

   public long getFailedMessagesTotal() {
      return this.failedMessagesCount;
   }

   synchronized void updateFailedMessagesCount(long var1) {
      this.failedMessagesCount += var1;
   }
}
