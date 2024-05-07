package weblogic.jms.saf;

import java.util.Arrays;
import java.util.HashSet;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SAFAgentRuntimeMBean;
import weblogic.management.runtime.SAFConversationRuntimeMBean;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.messaging.saf.SAFException;

public class SAFAgentRuntimeMBeanAggregator extends RuntimeMBeanDelegate implements SAFAgentRuntimeMBean {
   private final SAFAgentRuntimeMBean delegate1;
   private SAFAgentRuntimeMBean delegate2;

   public SAFAgentRuntimeMBeanAggregator(String var1, RuntimeMBean var2, SAFAgentRuntimeMBean var3) throws ManagementException {
      super(var1, var2, true);
      this.delegate1 = var3;
   }

   SAFAgentRuntimeMBeanImpl getJMSSAFAgentRuntime() {
      return (SAFAgentRuntimeMBeanImpl)this.delegate1;
   }

   public void setDelegate2(SAFAgentRuntimeMBean var1) {
      this.delegate2 = var1;
   }

   public HealthState getHealthState() {
      return this.delegate1.getHealthState();
   }

   public SAFRemoteEndpointRuntimeMBean[] getRemoteEndpoints() {
      HashSet var1 = new HashSet();
      var1.addAll(Arrays.asList(this.delegate1.getRemoteEndpoints()));
      SAFAgentRuntimeMBean var2 = this.delegate2;
      if (var2 != null) {
         var1.addAll(Arrays.asList(var2.getRemoteEndpoints()));
      }

      return (SAFRemoteEndpointRuntimeMBean[])((SAFRemoteEndpointRuntimeMBean[])var1.toArray(new SAFRemoteEndpointRuntimeMBean[var1.size()]));
   }

   public long getRemoteEndpointsCurrentCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getRemoteEndpointsCurrentCount() + (var1 == null ? 0L : var1.getRemoteEndpointsCurrentCount());
   }

   public long getRemoteEndpointsHighCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getRemoteEndpointsHighCount() + (var1 == null ? 0L : var1.getRemoteEndpointsHighCount());
   }

   public long getRemoteEndpointsTotalCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getRemoteEndpointsTotalCount() + (var1 == null ? 0L : var1.getRemoteEndpointsTotalCount());
   }

   public void pauseIncoming() throws SAFException {
      try {
         this.delegate1.pauseIncoming();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.pauseIncoming();
      }

   }

   public void resumeIncoming() throws SAFException {
      try {
         this.delegate1.resumeIncoming();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.resumeIncoming();
      }

   }

   public boolean isPausedForIncoming() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return var1 != null ? var1.isPausedForIncoming() : this.delegate1.isPausedForIncoming();
   }

   public void pauseForwarding() throws SAFException {
      try {
         this.delegate1.pauseForwarding();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.pauseForwarding();
      }

   }

   public void resumeForwarding() throws SAFException {
      try {
         this.delegate1.resumeForwarding();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.resumeForwarding();
      }

   }

   public boolean isPausedForForwarding() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return var1 != null ? var1.isPausedForForwarding() : this.delegate1.isPausedForForwarding();
   }

   public void pauseReceiving() throws SAFException {
      try {
         this.delegate1.pauseReceiving();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.pauseReceiving();
      }

   }

   public void resumeReceiving() throws SAFException {
      try {
         this.delegate1.resumeReceiving();
      } finally {
         SAFAgentRuntimeMBean var3 = this.delegate2;
         if (var3 == null) {
            return;
         }

         var3.resumeReceiving();
      }

   }

   public boolean isPausedForReceiving() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return var1 != null ? var1.isPausedForReceiving() : this.delegate1.isPausedForReceiving();
   }

   public SAFConversationRuntimeMBean[] getConversations() {
      HashSet var1 = new HashSet();
      SAFAgentRuntimeMBean var2 = this.delegate2;
      if (var2 != null) {
         var1.addAll(Arrays.asList(var2.getConversations()));
      }

      return (SAFConversationRuntimeMBean[])((SAFConversationRuntimeMBean[])var1.toArray(new SAFConversationRuntimeMBean[var1.size()]));
   }

   public long getConversationsCurrentCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getConversationsCurrentCount() + (var1 == null ? 0L : var1.getConversationsCurrentCount());
   }

   public long getConversationsHighCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getConversationsHighCount() + (var1 == null ? 0L : var1.getConversationsHighCount());
   }

   public long getConversationsTotalCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getConversationsTotalCount() + (var1 == null ? 0L : var1.getConversationsTotalCount());
   }

   public long getMessagesCurrentCount() {
      return this.delegate1.getMessagesCurrentCount();
   }

   public long getMessagesPendingCount() {
      return this.delegate1.getMessagesPendingCount();
   }

   public long getMessagesHighCount() {
      return this.delegate1.getMessagesHighCount();
   }

   public long getMessagesReceivedCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getMessagesReceivedCount() + (var1 == null ? 0L : var1.getMessagesReceivedCount());
   }

   public long getMessagesThresholdTime() {
      return this.delegate1.getMessagesThresholdTime();
   }

   public long getBytesPendingCount() {
      return this.delegate1.getBytesPendingCount();
   }

   public long getBytesCurrentCount() {
      return this.delegate1.getBytesCurrentCount();
   }

   public long getBytesHighCount() {
      return this.delegate1.getBytesHighCount();
   }

   public long getBytesReceivedCount() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getBytesReceivedCount() + (var1 == null ? 0L : var1.getBytesReceivedCount());
   }

   public long getBytesThresholdTime() {
      return this.delegate1.getBytesThresholdTime();
   }

   public long getFailedMessagesTotal() {
      SAFAgentRuntimeMBean var1 = this.delegate2;
      return this.delegate1.getFailedMessagesTotal() + (var1 == null ? 0L : var1.getFailedMessagesTotal());
   }
}
