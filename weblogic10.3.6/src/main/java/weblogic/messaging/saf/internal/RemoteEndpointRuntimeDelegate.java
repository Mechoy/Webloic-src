package weblogic.messaging.saf.internal;

import java.security.AccessController;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.management.openmbean.CompositeData;
import weblogic.jms.saf.SAFRemoteEndpointCustomizer;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.SAFConversationRuntimeMBean;
import weblogic.management.runtime.WSRMRemoteEndpointRuntimeMBean;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.common.SQLExpression;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.saf.OperationState;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFLogger;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public final class RemoteEndpointRuntimeDelegate extends SAFStatisticsCommonMBeanImpl implements WSRMRemoteEndpointRuntimeMBean, Runnable {
   static final long serialVersionUID = 8592927712725625160L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String ENDPOINT_NAME_PREFIX = "SAFEndpoint";
   private String url;
   private int endpointType;
   private boolean isPausedForIncoming;
   private boolean isPausedForForwarding;
   private HashMap conversations;
   private long conversationsCurrentCount;
   private long conversationsHighCount;
   private long conversationsTotalCount;
   private long failedMessagesTotal;
   private Topic kernelTopic;
   private SAFMessageOpenDataConverter openDataConverter;
   private final SAFRemoteEndpointCustomizer delegate;
   private RemoteEndpointRuntimeCommonAddition addition;

   public RemoteEndpointRuntimeDelegate(SAFAgentAdmin var1, ID var2, String var3, int var4, Topic var5) throws ManagementException {
      super("SAFEndpoint" + var2.toString(), (RuntimeMBean)null, true);
      this.delegate = new SAFRemoteEndpointCustomizer("SAFEndpoint" + var2.toString(), var1, this);
      this.url = var3;
      this.endpointType = var4;
      this.kernelTopic = var5;
      this.conversations = new HashMap();
      this.openDataConverter = new SAFMessageOpenDataConverter(this);
      this.addition = new RemoteEndpointRuntimeCommonAddition();
   }

   synchronized void addConversation(ConversationRuntimeDelegate var1) {
      if (this.conversations.get(var1.getConversationName()) == null) {
         ++this.conversationsCurrentCount;
         ++this.conversationsTotalCount;
         if (this.conversationsCurrentCount > this.conversationsHighCount) {
            this.conversationsHighCount = this.conversationsCurrentCount;
         }

         this.conversations.put(var1.getConversationName(), var1);
      }

   }

   synchronized void removeConversation(String var1) {
      if (this.conversations.remove(var1) != null) {
         --this.conversationsCurrentCount;
      }

   }

   public String getURL() {
      return this.url;
   }

   synchronized void increaseFailedMessagesCount() {
      ++this.failedMessagesTotal;
   }

   public CompositeData getMessage(String var1) throws ManagementException {
      try {
         Cursor var2 = this.createCursor("SAFMessageID = '" + var1 + "'");
         if (var2.size() == 0) {
            return null;
         } else if (var2.size() > 1) {
            throw new ManagementException("Multiple messages exist for messageID " + var1);
         } else {
            return this.openDataConverter.createCompositeData(var2.next());
         }
      } catch (Exception var3) {
         throw new ManagementException("Error creating message cursor.", var3);
      }
   }

   public String getEndpointType() {
      switch (this.endpointType) {
         case 2:
            return "WebServices";
         case 3:
            return "JaxwsWebServices";
         default:
            return new String("Unknown type");
      }
   }

   public void pauseIncoming() throws SAFException {
      if (SAFDebug.SAFAdmin.isDebugEnabled()) {
         SAFDebug.SAFAdmin.debug("Pause Incoming for endpoint " + this.url);
      }

      synchronized(this) {
         if (!this.isPausedForIncoming) {
            this.isPausedForIncoming = true;
            this.suspendKernelQueues(1);
         }
      }
   }

   public void resumeIncoming() throws SAFException {
      if (SAFDebug.SAFAdmin.isDebugEnabled()) {
         SAFDebug.SAFAdmin.debug("Resume Incoming for endpoint " + this.url);
      }

      synchronized(this) {
         if (this.isPausedForIncoming) {
            this.isPausedForIncoming = false;
            this.resumeKernelQueues(1);
         }
      }
   }

   public synchronized boolean isPausedForIncoming() {
      return this.isPausedForIncoming;
   }

   public void pauseForwarding() throws SAFException {
      if (SAFDebug.SAFAdmin.isDebugEnabled()) {
         SAFDebug.SAFAdmin.debug("Pause Forwarding for endpoint " + this.url);
      }

      synchronized(this) {
         if (!this.isPausedForForwarding) {
            this.isPausedForForwarding = true;
            this.suspendKernelQueues(2);
         }
      }
   }

   public void resumeForwarding() throws SAFException {
      if (SAFDebug.SAFAdmin.isDebugEnabled()) {
         SAFDebug.SAFAdmin.debug("Resume Forwarding for endpoint " + this.url);
      }

      synchronized(this) {
         if (this.isPausedForForwarding) {
            this.isPausedForForwarding = false;
            this.resumeKernelQueues(2);
         }
      }
   }

   public synchronized boolean isPausedForForwarding() {
      return this.isPausedForForwarding;
   }

   public void closeConversations(String var1) throws SAFException {
      Iterator var2 = null;
      synchronized(this) {
         var2 = ((HashMap)this.conversations.clone()).values().iterator();
      }

      while(var2.hasNext()) {
         SAFConversationRuntimeMBean var3 = (SAFConversationRuntimeMBean)var2.next();
         if (var3.getName().indexOf(var1) != -1) {
            this.removeConversation(var3.getConversationName());
            var3.destroy();
         }
      }

   }

   public void expireAll() {
      synchronized(this) {
         if (this.addition.getOperationState() == OperationState.RUNNING) {
            return;
         }
      }

      WorkManagerFactory.getInstance().getSystem().schedule(this);
   }

   public void run() {
      Iterator var1 = null;
      synchronized(this) {
         if (this.addition.getOperationState() == OperationState.RUNNING) {
            return;
         }

         this.addition.setOperationState(OperationState.RUNNING);
         var1 = ((HashMap)this.conversations.clone()).values().iterator();
         this.conversations.clear();
      }

      while(var1.hasNext()) {
         try {
            ((ConversationRuntimeDelegate)var1.next()).getConversation().expireAllMessages(9, (Throwable)null);
         } catch (KernelException var6) {
            this.addition.setOperationState(OperationState.STOPPED);
            return;
         }
      }

      synchronized(this) {
         this.addition.setOperationState(OperationState.COMPLETED);
      }
   }

   private Cursor createCursor(String var1) throws KernelException {
      Iterator var2 = null;
      synchronized(this) {
         var2 = this.conversations.values().iterator();
      }

      HashSet var3 = new HashSet();

      while(var2.hasNext()) {
         Queue var4 = ((ConversationRuntimeDelegate)var2.next()).getConversation().getSubscriptionQueue();
         var3.add(var4);
      }

      return this.kernelTopic.getKernel().createCursor(var3, this.kernelTopic.getFilter().createExpression(new SQLExpression(var1)), Integer.MAX_VALUE);
   }

   public void purge() throws SAFException {
      Iterator var1 = null;
      synchronized(this) {
         var1 = ((HashMap)this.conversations.clone()).values().iterator();
         this.conversations.clear();
      }

      while(var1.hasNext()) {
         ((SAFConversationRuntimeMBean)var1.next()).destroy();
      }

   }

   public synchronized SAFConversationRuntimeMBean[] getConversations() {
      return (SAFConversationRuntimeMBean[])((SAFConversationRuntimeMBean[])this.conversations.values().toArray(new SAFConversationRuntimeMBean[this.conversations.size()]));
   }

   public long getConversationsCurrentCount() {
      return this.conversationsCurrentCount;
   }

   public long getConversationsHighCount() {
      return this.conversationsHighCount;
   }

   public long getConversationsTotalCount() {
      return this.conversationsTotalCount;
   }

   public synchronized long getMessagesCurrentCount() {
      return (long)(this.kernelTopic.getStatistics().getMessagesCurrent() - this.kernelTopic.getStatistics().getMessagesPending());
   }

   public long getMessagesPendingCount() {
      return (long)this.kernelTopic.getStatistics().getMessagesPending();
   }

   public long getMessagesHighCount() {
      return (long)this.kernelTopic.getStatistics().getMessagesHigh();
   }

   public long getMessagesReceivedCount() {
      return (long)this.kernelTopic.getStatistics().getMessagesReceived();
   }

   public synchronized long getBytesCurrentCount() {
      return this.kernelTopic.getStatistics().getBytesCurrent() - this.kernelTopic.getStatistics().getBytesPending();
   }

   public long getBytesPendingCount() {
      return this.kernelTopic.getStatistics().getBytesPending();
   }

   public long getBytesHighCount() {
      return this.kernelTopic.getStatistics().getBytesHigh();
   }

   public long getBytesReceivedCount() {
      return this.kernelTopic.getStatistics().getBytesReceived();
   }

   public synchronized long getFailedMessagesTotal() {
      return this.failedMessagesTotal;
   }

   public String getMessages(String var1, Integer var2) throws ManagementException {
      if (SAFDebug.SAFAdmin.isDebugEnabled()) {
         SAFDebug.SAFAdmin.debug("getMessages() is called on " + this.getName());
      }

      Cursor var3 = null;

      try {
         var3 = this.createCursor(var1);
         if (SAFDebug.SAFAdmin.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("getMessages(): found " + var3.size() + " messages");
         }
      } catch (KernelException var5) {
         var5.printStackTrace();
         throw new ManagementException(var5.getMessage());
      } catch (Exception var6) {
         var6.printStackTrace();
         throw new ManagementException(var6.getMessage());
      }

      SAFMessageCursorDelegate var4 = new SAFMessageCursorDelegate(this, this.openDataConverter, var3, this.openDataConverter, var2);
      this.addCursorDelegate(var4);
      return var4.getHandle();
   }

   public boolean isConnected() {
      return this.addition.isConnected();
   }

   public void connected() {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && !this.isConnected()) {
         SAFLogger.logSAFConnected(this.url);
      }

      this.addition.connected();
   }

   public void disconnected(Exception var1) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && this.isConnected()) {
         SAFLogger.logSAFDisconnected(this.url, StackTraceUtils.throwable2StackTrace(var1));
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

   public synchronized Exception getLastException() {
      return this.addition.getLastException();
   }

   public synchronized String getOperationState() {
      return this.addition.getOperationState().toString();
   }

   void close() {
      try {
         PrivilegedActionUtilities.unregister(this.delegate, kernelId);
      } catch (ManagementException var2) {
      }

   }

   private void suspendKernelQueues(int var1) throws SAFException {
      Iterator var2 = null;
      KernelException var3 = null;
      synchronized(this) {
         var2 = ((HashMap)this.conversations.clone()).values().iterator();
      }

      while(var2.hasNext()) {
         ConversationRuntimeDelegate var4 = (ConversationRuntimeDelegate)var2.next();
         ConversationAssembler var5 = var4.getConversation();

         try {
            var5.getSubscriptionQueue().suspend(var1);
         } catch (KernelException var8) {
            if (var3 == null) {
               var3 = var8;
            }
         }
      }

      if (var3 != null) {
         throw new SAFException(var3);
      }
   }

   private void resumeKernelQueues(int var1) throws SAFException {
      Iterator var2 = null;
      KernelException var3 = null;
      synchronized(this) {
         var2 = ((HashMap)this.conversations.clone()).values().iterator();
      }

      while(var2.hasNext()) {
         ConversationRuntimeDelegate var4 = (ConversationRuntimeDelegate)var2.next();
         ConversationAssembler var5 = var4.getConversation();

         try {
            var5.getSubscriptionQueue().resume(var1);
         } catch (KernelException var8) {
            if (var3 == null) {
               var3 = var8;
            }
         }
      }

      if (var3 != null) {
         throw new SAFException(var3);
      }
   }
}
