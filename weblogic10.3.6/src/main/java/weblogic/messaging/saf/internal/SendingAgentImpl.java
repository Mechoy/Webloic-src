package weblogic.messaging.saf.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Externalizable;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.health.HealthState;
import weblogic.jms.saf.SAFService;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.SAFConversationRuntimeMBean;
import weblogic.messaging.common.SQLFilter;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.QuotaException;
import weblogic.messaging.kernel.SendOptions;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.kernel.internal.KernelImpl;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFServiceNotAvailException;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.common.SAFRequestImpl;
import weblogic.messaging.saf.store.SAFStore;
import weblogic.messaging.saf.store.SAFStoreException;
import weblogic.messaging.saf.utils.Util;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;

public final class SendingAgentImpl extends AgentImpl implements SendingAgent, Externalizable {
   static final long serialVersionUID = -2776562935951109789L;
   private HashMap conversations;
   private long conversationsCurrentCount;
   private long conversationsHighCount;
   private long conversationsTotalCount;
   private HashMap dynamicNameToName;
   private HashMap conversationsByCreateConvMsgID;
   private long failedMessagesTotal;
   private HashMap kernelTopics;
   private double retryDelayMultiplier;
   private long retryDelayBase;
   private long retryDelayMaximum;
   private long timeToLiveDefault;
   private long conversationIdleTimeMaximum;
   private boolean isLoggingEnabled;
   private int windowSize;
   private boolean isPausedForIncoming;
   private boolean isPausedForForwarding;
   private Kernel kernel;
   private Quota kernelQuota;
   private static final SAFAgentFactoryInternal agentFactoryInternal = new SAFAgentFactoryInternal();
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RuntimeAccess runtimeAccess;
   private ServerStateChangeListener stateChangeListener;

   public SendingAgentImpl() {
   }

   SendingAgentImpl(String var1, SAFAgentAdmin var2, SAFStore var3) throws NamingException, SAFException {
      super(var1, var2, var3, 1);
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + var1 + "': RetryDelayBase=" + this.retryDelayBase + ", RetryDelayMaximum=" + this.retryDelayMaximum + ", RetryDelayMultiplier=" + this.retryDelayMultiplier + ", ConversationIdleTimeMaximum=" + this.conversationIdleTimeMaximum + ", TimeToLive=" + this.timeToLiveDefault + ", WindowSize=" + this.windowSize);
      }

   }

   public String toString() {
      return "<SendingAgentImpl> :  SAFAgentInternalName = " + this.name + " " + this.store;
   }

   static SAFAgentFactoryInternal getAgentFactory() {
      return agentFactoryInternal;
   }

   protected synchronized void addToAgentFactory() {
      agentFactoryInternal.addAgent(this);
      safManager.addLocalSendingAgent(this);
   }

   protected void removeFromAgentFactory() {
      agentFactoryInternal.removeAgent(this);
      safManager.removeLocalSendingAgent(this);
   }

   public void setConversationInfosFromStore(HashMap var1) {
      this.conversationInfosFromStore = var1;
   }

   public void startInitialize(SAFAgentMBean var1) throws SAFException {
      this.state = 1;
      this.retryDelayBase = var1.getDefaultRetryDelayBase();
      this.retryDelayMultiplier = var1.getDefaultRetryDelayMultiplier();
      this.retryDelayMaximum = var1.getDefaultRetryDelayMaximum();
      this.timeToLiveDefault = var1.getDefaultTimeToLive();
      this.conversationIdleTimeMaximum = var1.getConversationIdleTimeMaximum();
      this.windowSize = var1.getWindowSize();
      this.isPaused = this.isPausedForIncoming = var1.isIncomingPausedAtStartup();
      this.isPausedForForwarding = var1.isForwardingPausedAtStartup();
      this.isLoggingEnabled = var1.isLoggingEnabled();
      this.kernel = SAFService.getSAFService().getDeployer().getAgent(var1.getName()).getBackEnd().getKernel();
      this.kernelQuota = SAFService.getSAFService().getDeployer().getAgent(var1.getName()).getBackEnd().getQuota();
      this.initializeMaps();
   }

   private void initializeMaps() {
      this.conversations = new HashMap();
      this.dynamicNameToName = new HashMap();
      this.kernelTopics = new HashMap();
      this.conversationsByCreateConvMsgID = new HashMap();
      if (this.conversationInfosFromStore == null) {
         this.conversationInfosFromStore = new HashMap();
      }

   }

   protected void start() throws SAFException {
      synchronized(this) {
         if ((this.state & 6) != 0) {
            return;
         }

         this.waitForState(1);
         this.state = 2;
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' has recovered " + this.conversationInfosFromStore.size() + " conversation infos");
      }

      Collection var1 = this.kernel.getTopics();
      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' is starting: found " + var1.size() + " topics");
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Topic var3 = (Topic)var2.next();
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Topic: " + var3.getName());
         }

         if (!isNotWSRM(var3.getName())) {
            try {
               var3.setProperty("Quota", this.kernelQuota);
               var3.setFilter(new SQLFilter(this.kernel, SAFVariableBinder.THE_ONE));
               this.kernelTopics.put(var3.getName(), var3);
            } catch (KernelException var11) {
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  var11.printStackTrace();
               }

               throw new SAFException(var11.getMessage(), var11);
            }

            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': recovered topic = " + var3.getName());
            }
         }
      }

      int var14 = this.convertQueuesToTopics();

      try {
         Iterator var4 = this.conversationInfosFromStore.values().iterator();

         while(var4.hasNext()) {
            SAFConversationInfo var5 = (SAFConversationInfo)var4.next();
            this.findOrCreateConversation(var5);
            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': recovered conversation = " + var5);
            }
         }
      } catch (SAFException var12) {
         this.healthState = updateHealthState(this.healthState, 3, var12.getMessage());
         this.close();
         synchronized(this) {
            this.state = 1;
         }

         throw var12;
      }

      try {
         this.activateAllKernelTopics();
      } catch (KernelException var10) {
         throw new SAFException(var10.getMessage());
      }

      if (var14 > 0) {
         this.moveQueueMessagesToTopic();
      }

      synchronized(this) {
         this.state = 4;
         if (this.waitersCount > 0) {
            this.notifyAll();
         }

      }
   }

   private int convertQueuesToTopics() throws SAFException {
      Collection var1 = this.kernel.getQueues();
      int var2 = 0;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Queue var4 = (Queue)var3.next();
         if (!isNotWSRM(var4.getName())) {
            ++var2;
            this.kernelTopics.put(var4.getName(), this.createKernelTopic(var4.getName(), var4.getProperties()));
            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': converted queue = " + var4.getName() + " to a topic");
            }
         }
      }

      return var2;
   }

   private void moveQueueMessagesToTopic() throws SAFException {
      Collection var1 = this.kernel.getQueues();
      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' is starting: move messages " + "from pre9.0.1 saf reposity to post 9.0 one ");
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Queue var3 = (Queue)var2.next();
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Queue: " + var3.getName());
         }

         if (!isNotWSRM(var3.getName())) {
            try {
               Cursor var4 = var3.createCursor(true, (Expression)null, -1);
               KernelRequest var6 = new KernelRequest();

               while(true) {
                  MessageElement var5;
                  if ((var5 = var4.next()) == null) {
                     var4.close();
                     var6.getResult();
                     var6.reset();
                     var3.delete(var6);
                     var6.getResult();
                     break;
                  }

                  SAFRequest var7 = (SAFRequest)var5.getMessage();
                  String var8 = var7.getConversationName();
                  Queue var9 = this.kernel.findQueue(var8);
                  Sequence var10 = var9.findSequence(var8);
                  var10.setPassthru(true);
                  SendOptions var11 = new SendOptions();
                  var11.setPersistent(var7.getDeliveryMode() == 2);
                  var11.setTimeout(0L);
                  var11.setSequence(var10);
                  var11.setSequenceNum(var5.getSequenceNum());
                  var6 = var9.send((SAFRequestImpl)var7, var11);
               }
            } catch (KernelException var12) {
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  var12.printStackTrace();
               }

               throw new SAFException(var12.getMessage(), var12);
            }

            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': recovered messages from queue = " + var3.getName());
            }
         }
      }

   }

   private void activateAllKernelTopics() throws KernelException {
      Iterator var1;
      Iterator var2;
      synchronized(this) {
         var1 = this.kernelTopics.values().iterator();
         var2 = this.conversations.values().iterator();
      }

      while(var2.hasNext()) {
         String var3 = ((ConversationAssembler)var2.next()).getConversationName();
         Queue var4 = this.kernel.findQueue(var3);
         if (var4 != null) {
            Sequence var5 = var4.findSequence(var3);
            if (var5 != null && var5.isPassthru()) {
               var5.setPassthru(false);
            }
         }
      }

      while(var1.hasNext()) {
         Topic var7 = (Topic)var1.next();
         var7.resume(16384);
      }

   }

   void send(SAFRequest var1) throws SAFException {
      if (this.isPausedForIncoming()) {
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' == send(): agent is paused for incoming");
         }

         throw new SAFServiceNotAvailException("The agent is paused for incoming messages");
      } else {
         this.sendInternal(var1);
      }
   }

   private ConversationAssembler prepareSAFRequest(SAFRequest var1) throws SAFException {
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' prepareSAFRequest(): timestamp =" + var1.getTimestamp() + " conversationName = " + var1.getConversationName());
      }

      if (var1.getTimestamp() == -1L || var1.getTimestamp() == 0L) {
         var1.setTimestamp(System.currentTimeMillis());
      }

      if (var1.getMessageId() == null) {
         var1.setMessageId(Util.generateID().toString());
      }

      ConversationAssembler var2 = this.getConversation(var1.getConversationName());
      if (var2 == null) {
         throw new SAFConversationNotAvailException("Failed to send a message: conversation " + var1.getConversationName() + " has never been registered.");
      } else if (var2.isNotAvailAndClosed()) {
         throw new SAFConversationNotAvailException("Failed to send a message: conversation " + var1.getConversationName() + " has expired or terminated," + " or has been destroyed administratively");
      } else if (this.getRemoteEndpoint(var2.getInfo().getDestinationURL()).isPausedForIncoming()) {
         throw new SAFException("The endpoint " + var2.getInfo().getDestinationURL() + " is paused for incoming messages");
      } else {
         if (var1.getTimeToLive() == -1L) {
            var1.setTimeToLive(this.timeToLiveDefault);
         }

         return var2;
      }
   }

   private void sendInternal(SAFRequest var1) throws SAFException {
      synchronized(this) {
         this.waitForState(4);
      }

      ConversationAssembler var2 = this.prepareSAFRequest(var1);
      Topic var3 = this.findKernelTopic(getKernelTopicName(var2.getInfo()));
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.getName() + "': message writer about to put message " + var1.getMessageId() + " to the SAF respository, persistent = " + (var1.getDeliveryMode() == 2));
      }

      try {
         SendOptions var4 = new SendOptions();
         var4.setPersistent(var1.getDeliveryMode() == 2);
         var4.setTimeout(0L);
         KernelRequest var5 = var3.send((SAFRequestImpl)var1, var4);
         if (var5 != null) {
            var5.getResult();
         }
      } catch (QuotaException var7) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            var7.printStackTrace();
         }

         throw new SAFException("Failed to save a message to SAF repository because  quota has been exceeded", var7);
      } catch (KernelException var8) {
         throw new SAFException("Failed to store the request to messaging kernel", var8);
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' == send(): Message stored: " + var1.getMessageId());
      }

   }

   private void destroyConversation(String var1) {
      ConversationAssembler var2 = this.getConversation(var1);
      var2.destroy();
   }

   private void closeConversation(String var1) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': closing conversation " + var1);
      }

      ConversationAssembler var2 = this.getConversation(var1);
      if (var2 != null && !var2.isNotAvailAndClosed()) {
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Anget " + this.name + " closeConversation(): " + " send a fake request with endOfConversation flag");
         }

         SAFRequestImpl var3 = new SAFRequestImpl();
         var3.setConversationName(var1);
         var3.setSequenceNumber(-1L);
         var3.setEndOfConversation(true);
         var3.setPayload((Externalizable)null);
         long var4 = var2.getTimeLeft();
         if (var4 == 0L) {
            var3.setTimeToLive(100L);
         } else {
            var3.setTimeToLive(var4);
         }

         var3.setTimestamp(System.currentTimeMillis());
         int var6 = var1.indexOf("NonPersistent");
         if (var6 != -1 && var6 + 13 == var1.length()) {
            var3.setDeliveryMode(1);
         } else {
            var3.setDeliveryMode(2);
         }

         try {
            this.sendInternal(var3);
         } catch (SAFException var8) {
            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               var8.printStackTrace();
            }
         }

         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' == closeConversation(): " + " after sending a fake request with endOfConversation flag");
         }

      }
   }

   void closeConversation(String var1, boolean var2) {
      if (var2) {
         this.destroyConversation(var1);
      } else {
         this.closeConversation(var1);
      }
   }

   RemoteEndpointRuntimeDelegate getRemoteEndpoint(String var1) {
      return this.agentAdmin.getRemoteEndpoint(var1);
   }

   synchronized Topic getKernelTopic(SAFConversationInfo var1) {
      return this.kernel.findTopic(getKernelTopicName(var1));
   }

   private void findOrCreateKernelTopic(SAFConversationInfo var1) throws SAFException {
      String var2 = getKernelTopicName(var1);
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' == findOrCreateKernelTopic(): " + var2);
      }

      boolean var4 = false;
      Topic var3;
      synchronized(this.kernel) {
         var3 = this.findKernelTopic(var2);
         if (var3 == null) {
            var4 = true;
            var3 = this.createKernelTopic(var2, (Map)null);
         }
      }

      if (var4) {
         try {
            var3.resume(16384);
         } catch (KernelException var7) {
            throw new SAFException("Cannot create reader on the topic", var7);
         }
      }

   }

   private Topic findKernelTopic(String var1) {
      return this.kernel.findTopic(var1);
   }

   private Topic createKernelTopic(String var1, Map var2) throws SAFException {
      if (var2 == null) {
         var2 = new HashMap();
         ((Map)var2).put("Durable", new Boolean(true));
         ((Map)var2).put("Quota", this.kernelQuota);
      }

      try {
         Topic var3 = this.kernel.createTopic(var1, (Map)var2);
         var3.setFilter(new SQLFilter(this.kernel, SAFVariableBinder.THE_ONE));
         return var3;
      } catch (KernelException var4) {
         throw new SAFException("Cannot create kernel topic", var4);
      }
   }

   public void suspend(boolean var1) {
      try {
         this.close();
      } catch (SAFException var3) {
      }

   }

   public void resume() throws SAFException {
      this.start();
   }

   private void close() throws SAFException {
      this.close(false);
   }

   void close(boolean var1) throws SAFException {
      synchronized(this) {
         if ((this.state & 9) != 0) {
            return;
         }

         this.waitForState(6);
         this.state = 8;
         if (this.stateChangeListener != null) {
            runtimeAccess.getServerRuntime().removePropertyChangeListener(this.stateChangeListener);
         }
      }

      try {
         this.unadvertise();
         Iterator var2;
         synchronized(this) {
            this.conversationsCurrentCount = 0L;
            HashMap var4 = (HashMap)this.conversations.clone();
            var2 = var4.keySet().iterator();
            this.dynamicNameToName.clear();
         }

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            ConversationAssembler var19 = (ConversationAssembler)this.conversations.get(var3);
            if (var19 != null) {
               if (var1) {
                  var19.delete();
               } else {
                  this.removeConversation(var3, false);
                  var19.close();
               }
            }
         }

         this.store.close();
      } finally {
         synchronized(this) {
            this.conversations.clear();
            this.conversationsByCreateConvMsgID.clear();
            this.state = 1;
            if (this.waitersCount > 0) {
               this.notifyAll();
            }

         }
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending agent '" + this.name + "' has been closed");
      }

   }

   private SAFConversationInfo normalizeSAFConversationInfo(SAFConversationInfo var1) {
      synchronized(this.conversationInfosFromStore) {
         return (SAFConversationInfo)this.conversationInfosFromStore.get(var1.getConversationName());
      }
   }

   private boolean findOrCreateConversation(SAFConversationInfo var1) throws SAFException {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot create a conversation with null conversation info");
      } else {
         boolean var2 = false;
         this.findOrCreateKernelTopic(var1);
         ConversationAssembler var3;
         SAFConversationInfo var4;
         synchronized(this) {
            var3 = this.getConversation(var1.getConversationName());
            SAFConversationInfo var6;
            if (var3 != null) {
               if (var3.isNotAvailAndClosed()) {
                  throw new SAFConversationNotAvailException("Conversation " + var1.getConversationName() + " has expired" + " or has been terminated or destroyed");
               }

               var6 = var3.getInfo().getConversationOffer();
               if (var6 != var1.getConversationOffer()) {
                  throw new SAFException(" Illegal usage of SAFConversation. Offer = " + var6 + " in SAFConversation already registered with the Sending Agent" + " does not match the current offer = " + var1.getConversationOffer());
               }

               return false;
            }

            var6 = this.normalizeSAFConversationInfo(var1);
            if (var6 == null) {
               if (((SAFConversationInfoImpl)var1).getTimestamp() == 0L) {
                  ((SAFConversationInfoImpl)var1).setTimestamp(System.currentTimeMillis());
               }

               var2 = true;
            }

            var4 = var6 != null ? var6 : var1;
            String var7 = var4.getConversationName();

            try {
               var3 = new ConversationAssembler(this, var4, this.store, this.isLoggingEnabled, this.windowSize);
               this.addConversation(var3);
            } catch (ManagementException var12) {
               if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
                  var12.printStackTrace();
                  SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': failed to create a new conversation runtime MBean " + var7);
               }

               throw new SAFException("Sending Agent '" + this.name + " Failed to create runtime MBEan for conversation " + var7);
            }
         }

         var3.setupSubscriptionQueue();
         safManager.addLocalSendingAgentId(var1, this);
         safManager.addConversationInfoOnSendingSide(var1);
         if (var4.getDynamicConversationName() != null && !var4.getDynamicConversationName().equals(var4.getConversationName())) {
            var3.createAndRecordDynamicConversation(var4.getConversationName(), var4.getDynamicConversationName());
         }

         if (T3Srvr.getT3Srvr().getRunState() == 2) {
            var3.startFromADiffThread();
         } else {
            synchronized(this) {
               if (this.stateChangeListener == null) {
                  this.stateChangeListener = new ServerStateChangeListener();
                  runtimeAccess.getServerRuntime().addPropertyChangeListener(this.stateChangeListener);
               }
            }
         }

         return var2;
      }
   }

   RemoteEndpointRuntimeDelegate findOrCreateRemoteEndpointRuntime(String var1, int var2, Topic var3) throws ManagementException {
      return this.agentAdmin.findOrCreateRemoteEndpointRuntime(var1, var2, var3);
   }

   synchronized void addDynamicName(String var1, String var2) {
      this.dynamicNameToName.put(var2, var1);
      this.addConversation(var2, this.getConversation(var1));
   }

   private synchronized String getOrginalName(String var1) {
      return (String)this.dynamicNameToName.get(var1);
   }

   private void addConversation(ConversationAssembler var1) {
      if (this.conversations.get(var1.getName()) == null) {
         ++this.conversationsCurrentCount;
         ++this.conversationsTotalCount;
         if (this.conversationsCurrentCount > this.conversationsHighCount) {
            this.conversationsHighCount = this.conversationsCurrentCount;
         }

         this.conversations.put(var1.getName(), var1);
         if (var1.getInfo().isDynamic()) {
            this.conversationsByCreateConvMsgID.put(var1.getInfo().getCreateConversationMessageID(), var1);
         }
      }

   }

   private void addConversation(String var1, ConversationAssembler var2) {
      this.conversations.put(var1, var2);
   }

   public synchronized ConversationAssembler getConversation(String var1) {
      return (ConversationAssembler)this.conversations.get(var1);
   }

   ConversationAssembler removeConversation(String var1, boolean var2) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "' : removing conversation " + var1 + " destroy = " + var2);
      }

      SAFConversationInfo var3 = this.getConversationInfo(var1);
      if (var3 == null) {
         return null;
      } else {
         if (var2 && this.store != null) {
            try {
               this.store.removeConversationInfo(var3);
            } catch (SAFStoreException var9) {
            }
         }

         RemoteEndpointRuntimeDelegate var4 = this.getRemoteEndpoint(var3.getDestinationURL());
         if (var4 != null) {
            var4.removeConversation(var1);
         }

         safManager.removeLocalSendingAgentId(var3);
         synchronized(this) {
            if (this.conversations.get(var1) != null) {
               --this.conversationsCurrentCount;
               ConversationAssembler var6 = (ConversationAssembler)this.conversations.remove(var1);
               if (var3.getDynamicConversationName() != null) {
                  this.conversations.remove(var3.getDynamicConversationName());
                  this.dynamicNameToName.remove(var3.getDynamicConversationName());
               }

               if (var3.getCreateConversationMessageID() != null) {
                  this.conversations.remove(var3.getCreateConversationMessageID());
                  this.removeConversationByCreateConvMsgID(var3.getCreateConversationMessageID());
               }

               this.conversationInfosFromStore.remove(var1);
               return var6;
            } else {
               return null;
            }
         }
      }
   }

   private synchronized ConversationAssembler removeConversationByCreateConvMsgID(String var1) {
      return (ConversationAssembler)this.conversationsByCreateConvMsgID.remove(var1);
   }

   void registerConversationInfo(SAFConversationInfo var1) throws SAFException {
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': registering conversation info: " + var1);
      }

      if (this.findOrCreateConversation(var1)) {
         this.storeConversationInfo(var1);
      }

   }

   private synchronized SAFConversationInfo getConversationInfo(String var1) {
      ConversationAssembler var2 = (ConversationAssembler)this.conversations.get(var1);
      if (var2 == null) {
         var2 = (ConversationAssembler)this.conversationsByCreateConvMsgID.get(var1);
      }

      return var2 == null ? null : var2.getInfo();
   }

   synchronized double getDefaultRetryDelayMultiplier() {
      return this.retryDelayMultiplier;
   }

   synchronized long getDefaultRetryDelayBase() {
      return this.retryDelayBase;
   }

   synchronized long getDefaultRetryDelayMaximum() {
      return this.retryDelayMaximum;
   }

   synchronized long getDefaultTimeToLive() {
      return this.timeToLiveDefault;
   }

   synchronized long getDefaultMaximumIdleTime() {
      return this.conversationIdleTimeMaximum;
   }

   void acknowledge(String var1, long var2, long var4) {
      ConversationAssembler var6 = this.getConversation(var1);
      if (var6 == null) {
         String var7 = this.getOrginalName(var1);
         if (var7 != null) {
            var6 = this.getConversation(var7);
         }
      }

      if (var6 != null) {
         try {
            var6.acknowledge(var2, var4);
         } catch (SAFException var8) {
            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': failed to acknowledge messages " + var2 + ":" + var4);
               var8.printStackTrace();
            }
         }

      }
   }

   void handleAsyncFault(String var1, String var2, Exception var3) {
      ConversationAssembler var4 = this.getConversation(var1);
      if (var4 == null) {
         String var5 = this.getOrginalName(var1);
         if (var5 != null) {
            var4 = this.getConversation(var5);
         }
      }

      if (var4 != null) {
         try {
            var4.handleAsyncFault(var2, var3);
         } catch (SAFException var6) {
            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': failed to handle Fault related to " + var2);
               var6.printStackTrace();
            }
         }

      }
   }

   void onCreateConversationSucceed(SAFConversationHandle var1) {
      ConversationAssembler var2;
      if (var1.getCreateConversationMessageID() != null) {
         var2 = this.removeConversationByCreateConvMsgID(var1.getCreateConversationMessageID());
      } else {
         var2 = this.getConversation(var1.getConversationName());
      }

      if (var2 != null) {
         synchronized(this) {
            var2.onCreateConversationSucceed(var1);
         }
      }

   }

   synchronized void increaseFailedMessagesCount() {
      ++this.failedMessagesTotal;
   }

   SAFConversationRuntimeMBean[] getConversationRuntimeDelegates() {
      Iterator var1;
      int var2;
      synchronized(this) {
         var2 = this.conversations.size();
         var1 = ((HashMap)this.conversations.clone()).values().iterator();
      }

      int var3 = 0;
      SAFConversationRuntimeMBean[] var4 = new SAFConversationRuntimeMBean[var2];

      while(var1.hasNext()) {
         ConversationAssembler var5 = (ConversationAssembler)var1.next();
         if (this.getConversation(var5.getName()) != null) {
            var4[var3++] = var5.getRuntimeDelegate();
         }
      }

      return var4;
   }

   public synchronized long getConversationsCurrentCount() {
      return this.conversationsCurrentCount;
   }

   public synchronized long getConversationsHighCount() {
      return this.conversationsHighCount;
   }

   public synchronized long getConversationsTotalCount() {
      return this.conversationsTotalCount;
   }

   public synchronized long getFailedMessagesTotal() {
      return this.failedMessagesTotal;
   }

   public synchronized void pauseIncoming() {
      if (!this.isPausedForIncoming) {
         this.isPaused = this.isPausedForIncoming = true;
         this.unadvertise();
      }
   }

   public synchronized void resumeIncoming() {
      if (this.isPausedForIncoming) {
         this.isPaused = this.isPausedForIncoming = false;
         this.advertise();
      }
   }

   public synchronized boolean isPausedForIncoming() {
      return this.isPausedForIncoming;
   }

   public void pauseForwarding() throws SAFException {
      Iterator var1;
      synchronized(this) {
         if (this.isPausedForForwarding) {
            return;
         }

         this.isPausedForForwarding = true;
         var1 = ((HashMap)this.conversations.clone()).values().iterator();
      }

      while(var1.hasNext()) {
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': about to pause one conversation");
         }

         try {
            ((ConversationAssembler)var1.next()).pauseReader();
         } catch (KernelException var4) {
            throw new SAFException("SAF Agent '" + this.name + "': failed to resume forwarding", var4);
         }
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("SAF Agent " + this.getName() + " is paused for forwarding");
      }

   }

   public void resumeForwarding() throws SAFException {
      Iterator var1;
      synchronized(this) {
         if (!this.isPausedForForwarding) {
            return;
         }

         var1 = ((HashMap)this.conversations.clone()).values().iterator();
         this.isPausedForForwarding = false;
      }

      while(var1.hasNext()) {
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.name + "': about to resume one reader");
         }

         try {
            ((ConversationAssembler)var1.next()).resumeReader();
         } catch (KernelException var4) {
            throw new SAFException("SAF Agent '" + this.name + "': Failed to resume forwarding", var4);
         }
      }

   }

   public synchronized boolean isPausedForForwarding() {
      return this.isPausedForForwarding;
   }

   synchronized void setDefaultRetryDelayBase(long var1) {
      this.retryDelayBase = var1;
   }

   synchronized void setDefaultRetryDelayMaximum(long var1) {
      this.retryDelayMaximum = var1;
   }

   synchronized void setDefaultRetryDelayMultiplier(double var1) {
      this.retryDelayMultiplier = var1;
   }

   synchronized void setDefaultTimeToLive(long var1) {
      this.timeToLiveDefault = var1;
   }

   synchronized void setConversationIdleTimeMaximum(long var1) {
      this.conversationIdleTimeMaximum = var1;
   }

   synchronized void setLoggingEnabled(boolean var1) {
      this.isLoggingEnabled = var1;
   }

   void setWindowSize(int var1) {
      this.windowSize = var1;
   }

   HealthState getHealthState() {
      return this.healthState;
   }

   private static boolean isNotWSRM(String var0) {
      return !var0.substring(0, 3).equals("WS:");
   }

   private static String getKernelTopicName(SAFConversationInfo var0) {
      int var1 = var0.getDestinationType();
      String var2 = var0.getDestinationURL();
      if (var1 == 2) {
         return "WS:" + var2;
      } else {
         return var1 == 3 ? "WS_JAXWS:" + var2 : var2;
      }
   }

   Kernel getKernel() {
      return this.kernel;
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("SendingAgent");
      super.dump(var1, var2);
      var2.writeAttribute("failedMessagesTotal", String.valueOf(this.failedMessagesTotal));
      var2.writeAttribute("retryDelayMultiplier", String.valueOf(this.retryDelayMultiplier));
      var2.writeAttribute("retryDelayBase", String.valueOf(this.retryDelayBase));
      var2.writeAttribute("retryDelayMaximum", String.valueOf(this.retryDelayMaximum));
      var2.writeAttribute("timeToLiveDefault", String.valueOf(this.timeToLiveDefault));
      var2.writeAttribute("conversationIdleTimeMaximum", String.valueOf(this.conversationIdleTimeMaximum));
      var2.writeAttribute("isLoggingEnabled", String.valueOf(this.isLoggingEnabled));
      var2.writeAttribute("windowSize", String.valueOf(this.windowSize));
      var2.writeAttribute("isPausedForIncoming", String.valueOf(this.isPausedForIncoming));
      var2.writeAttribute("isPausedForForwarding", String.valueOf(this.isPausedForForwarding));
      SAFDiagnosticImageSource.dumpHealthStateElement(var2, this.getAgentAdmin().getHealthState());
      var2.writeStartElement("DynamicNames");
      HashMap var3 = (HashMap)this.dynamicNameToName.clone();
      var2.writeAttribute("count", String.valueOf(var3.size()));
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2.writeStartElement("DynamicNameToName");
         var2.writeAttribute("dynamicName", var5);
         var2.writeAttribute("name", (String)var3.get(var5));
         var2.writeEndElement();
      }

      var2.writeEndElement();
      var2.writeStartElement("ConversationAssemblers");
      Object[] var8 = this.conversations.values().toArray();
      var2.writeAttribute("count", String.valueOf(var8.length));

      for(int var6 = 0; var6 < var8.length; ++var6) {
         ConversationAssembler var7 = (ConversationAssembler)var8[var6];
         var7.dump(var1, var2);
      }

      var2.writeEndElement();
      ((KernelImpl)this.kernel).dump(var1, var2);
      var2.writeEndElement();
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(KERNEL_ID);
   }

   class ServerStateChangeListener implements PropertyChangeListener {
      public void propertyChange(PropertyChangeEvent var1) {
         if ("State".equals(var1.getPropertyName()) && "RUNNING".equals((String)var1.getNewValue())) {
            synchronized(SendingAgentImpl.this) {
               Iterator var3 = SendingAgentImpl.this.conversations.values().iterator();

               while(var3.hasNext()) {
                  ConversationAssembler var4 = (ConversationAssembler)var3.next();
                  var4.startFromADiffThread();
               }
            }
         }

      }
   }
}
