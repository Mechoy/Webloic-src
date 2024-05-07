package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.health.HealthState;
import weblogic.jms.saf.SAFService;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFEndpointManager;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.SAFServiceNotAvailException;
import weblogic.messaging.saf.SAFTransport;
import weblogic.messaging.saf.common.AgentDeliverRequest;
import weblogic.messaging.saf.common.AgentDeliverResponse;
import weblogic.messaging.saf.common.SAFConversationHandleImpl;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.common.SAFRemoteContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.transaction.TransactionHelper;

public final class SAFManagerImpl implements SAFManager {
   private static final SAFManagerImpl manager = new SAFManagerImpl();
   private final Map endpointManagers = Collections.synchronizedMap(new HashMap());
   private final Map transports = Collections.synchronizedMap(new HashMap());
   private final HashMap conversationInfosOnSendingSide = new HashMap();
   private final HashMap conversationInfosOnReceivingSide = new HashMap();
   private final List localSendingAgents = Collections.synchronizedList(new ArrayList());
   private final Map localSendingAgentIdsByConversationName = Collections.synchronizedMap(new HashMap());
   private final List localReceivingAgents = Collections.synchronizedList(new ArrayList());
   private List<SAFManager.ConversationLifecycleListener> conversationLifecycleListeners = Collections.synchronizedList(new ArrayList());

   private SAFManagerImpl() {
   }

   public static synchronized SAFManager getManager() {
      return manager;
   }

   public void send(SAFRequest var1) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("send(): conversationName= " + var1.getConversationName() + " MessageId = " + var1.getMessageId() + " transaction = " + TransactionHelper.getTransactionHelper().getTransaction());
      }

      SAFConversationInfo var2 = this.getCachedConversationInfoOnSendingSide(var1.getConversationName());
      if (var2 == null) {
         throw new SAFConversationNotAvailException("Cannot send a message to an unknow conversation");
      } else {
         SendingAgentImpl var3 = this.getSendingAgent(var2, (SAFManager.ConversationNameRefinementCallback)null);
         var3.send(var1);
      }
   }

   public void deliver(SAFConversationInfo var1, SAFRequest var2) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("deliver(): " + var2.getSequenceNumber());
      }

      AgentDeliverRequest var3 = new AgentDeliverRequest(var1, var2, false);
      this.deliverInternal(var3);
   }

   public SAFResult deliverSync(SAFConversationInfo var1, SAFRequest var2) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("deliverSync(): " + var2.getSequenceNumber());
      }

      AgentDeliverRequest var3 = new AgentDeliverRequest(var1, var2, true);
      AgentDeliverResponse var4 = this.deliverInternal(var3);
      return var4.getResult();
   }

   private AgentDeliverResponse deliverInternal(AgentDeliverRequest var1) throws SAFException {
      SAFConversationInfo var2 = var1.getConversationInfo();

      assert var2 != null;

      ReceivingAgentImpl var3 = this.getReceivingAgent(var2);

      assert var3 != null;

      if (SAFDebug.SAFManager.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("deliverIntenal(): conversation =  " + var2.toString() + " receiving agent = " + var3);
      }

      return var3.deliver(var1);
   }

   public void registerTransport(SAFTransport var1) {
      if (this.transports.get(new Integer(var1.getType())) == null) {
         this.transports.put(new Integer(var1.getType()), var1);
      }

   }

   public SAFTransport getTransport(int var1) {
      return (SAFTransport)this.transports.get(new Integer(var1));
   }

   public void registerEndpointManager(int var1, SAFEndpointManager var2) {
      this.endpointManagers.put(new Integer(var1), var2);
   }

   public SAFEndpointManager getEndpointManager(int var1) {
      return (SAFEndpointManager)this.endpointManagers.get(new Integer(var1));
   }

   public String registerConversationOnSendingSide(SAFConversationInfo var1) throws SAFException {
      return this.registerConversationOnSendingSide(var1, (SAFManager.ConversationNameRefinementCallback)null);
   }

   public String registerConversationOnSendingSide(SAFConversationInfo var1, SAFManager.ConversationNameRefinementCallback var2) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("registerConversationOnSendingSide(): name = " + var1.getConversationName());
      }

      if (var1 == null) {
         throw new IllegalArgumentException("Cannot register a conversation without a conversation info");
      } else {
         if (var1.getRemoteContext() == null) {
            if (SAFDebug.SAFManager.isDebugEnabled()) {
               SAFDebug.SAFManager.debug("registerConversationOnSendingSide(): null remote context, create one");
            }

            var1.setRemoteContext(new SAFRemoteContext());
         }

         SAFConversationInfo var3 = this.getCachedConversationInfoOnSendingSide(var1.getConversationName());
         if (var3 != null) {
            return var3.getConversationName();
         } else {
            SendingAgentImpl var4 = this.getSendingAgent(var1, var2);
            var4.registerConversationInfo(var1);
            return var1.getConversationName();
         }
      }
   }

   public void closeConversationOnSendingSide(String var1, boolean var2) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("closeConversationOnSendingSide(): conversationName= " + var1 + " destroy? " + var2);
      }

      SAFConversationInfo var3 = this.getCachedConversationInfoOnSendingSide(var1);
      if (var3 == null) {
         throw new SAFConversationNotAvailException("Cannot close a conversation that does not exist");
      } else {
         SendingAgentImpl var4 = this.getSendingAgent(var3, (SAFManager.ConversationNameRefinementCallback)null);
         var4.closeConversation(var1, var2);
      }
   }

   final void addConversationInfoOnSendingSide(SAFConversationInfo var1) {
      synchronized(this.conversationInfosOnSendingSide) {
         if (this.conversationInfosOnSendingSide.get(var1.getConversationName()) == null) {
            this.conversationInfosOnSendingSide.put(var1.getConversationName(), var1);
            this.notifyAddConverationToCache(true, "ConversationName", var1.getConversationName(), var1);
            if (var1.getCreateConversationMessageID() != null) {
               this.conversationInfosOnSendingSide.put(var1.getCreateConversationMessageID(), var1);
               this.notifyAddConverationToCache(true, "CreateConversationMessageD", var1.getCreateConversationMessageID(), var1);
            }
         }

      }
   }

   private SendingAgentImpl getSendingAgent(String var1) throws SAFException {
      SendingAgentImpl var2 = this.getLocalSendingAgent(var1);
      if (var2 == null) {
         var2 = this.findLocalSendingAgent(var1);
      }

      if (var2 != null) {
         return var2;
      } else {
         throw new SAFServiceNotAvailException("The SAF agent that handles conversation " + var1 + " is not available at the moment.");
      }
   }

   private SendingAgentImpl getCachedSendingAgentId(SAFConversationInfo var1) {
      String var2 = var1.getCreateConversationMessageID();
      SendingAgentImpl var3 = this.getLocalSendingAgentId(var1.getConversationName());
      if (var3 == null && var2 != null) {
         var3 = this.getLocalSendingAgentId(var2);
      }

      return var3;
   }

   private SendingAgentImpl getSendingAgent(SAFConversationInfo var1, SAFManager.ConversationNameRefinementCallback var2) throws SAFException {
      SendingAgentImpl var3 = this.getCachedSendingAgentId(var1);
      if (var3 != null) {
         return var3;
      } else {
         var3 = (SendingAgentImpl)getNextLocalAgent(SendingAgentImpl.getAgentFactory(), (String)null);
         if (SAFDebug.SAFManager.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("getSendingAgent(): candidated agent = " + var3);
         }

         if (var3 != null) {
            if (var2 != null) {
               SAFManager.LocationInfo var4 = new SAFManager.LocationInfo(var3.getStoreName());
               var2.conversationPreStore(var1, var4);
            }

            this.addLocalSendingAgentId(var1, var3);
         }

         if (var3 == null) {
            if (SendingAgentImpl.getAgentFactory().haveSendingAgentAvailable()) {
               throw new SAFServiceNotAvailException("Conversation '" + var1.getConversationName() + "' does not exist, has timed out, or has been administratively destroyed");
            } else {
               throw new SAFServiceNotAvailException("There is no active SAF sending agent available on server '" + ManagementService.getRuntimeAccess((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())).getServerName() + "'.");
            }
         } else {
            return var3;
         }
      }
   }

   private ReceivingAgentImpl getReceivingAgent(SAFConversationInfo var1) throws SAFException {
      ReceivingAgentImpl var2 = this.findLocalReceivingAgent(var1);
      if (var2 == null) {
         if (SAFDebug.SAFManager.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("getReceivingAgent(): The SAF agent that handles conversation " + var1.getConversationName() + " is not available at the moment.");
         }

         throw new SAFException("The SAF receiving agent that handles conversation:" + var1.getConversationName() + " is not available at the moment.");
      } else {
         return var2;
      }
   }

   private static AgentImpl getNextLocalAgent(SAFAgentFactoryInternal var0, String var1) {
      return var0.getAgentImpl(var1);
   }

   void addLocalReceivingAgent(ReceivingAgentImpl var1) {
      this.localReceivingAgents.add(var1);
   }

   private SendingAgentImpl findLocalSendingAgent(String var1) {
      Iterator var2;
      synchronized(this.localSendingAgents) {
         var2 = this.localSendingAgents.iterator();
      }

      SendingAgentImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SendingAgentImpl)var2.next();
      } while(var3.getConversation(var1) == null);

      return var3;
   }

   private ReceivingAgentImpl findLocalReceivingAgent(SAFConversationInfo var1) {
      Iterator var2;
      synchronized(this.localReceivingAgents) {
         var2 = this.localReceivingAgents.iterator();
      }

      ReceivingAgentImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ReceivingAgentImpl)var2.next();
      } while(var3.getConversation(var1) == null);

      return var3;
   }

   void removeLocalReceivingAgent(ReceivingAgentImpl var1) {
      this.localReceivingAgents.remove(var1);
   }

   void addLocalSendingAgent(SendingAgentImpl var1) {
      this.localSendingAgents.add(var1);
   }

   void removeLocalSendingAgent(SendingAgentImpl var1) {
      this.localSendingAgents.remove(var1);
   }

   void recordDynamicName(String var1, String var2) {
      if (!var1.equals(var2)) {
         this.localSendingAgentIdsByConversationName.put(var2, this.getLocalSendingAgentId(var1));

         try {
            this.addConversationInfoOnSendingSide(var2, this.getCachedConversationInfoOnSendingSide(var1));
         } catch (SAFException var4) {
         }

      }
   }

   private void addConversationInfoOnSendingSide(String var1, SAFConversationInfo var2) {
      synchronized(this.conversationInfosOnSendingSide) {
         if (this.conversationInfosOnSendingSide.get(var1) == null) {
            this.conversationInfosOnSendingSide.put(var1, var2);
            this.notifyAddConverationToCache(true, "DynamicName", var1, var2);
         }

      }
   }

   void addLocalSendingAgentId(SAFConversationInfo var1, SendingAgentImpl var2) {
      this.localSendingAgentIdsByConversationName.put(var1.getConversationName(), var2);
      if (var1.getCreateConversationMessageID() != null) {
         this.localSendingAgentIdsByConversationName.put(var1.getCreateConversationMessageID(), var2);
      }

   }

   private SendingAgentImpl getLocalSendingAgentId(String var1) {
      return (SendingAgentImpl)this.localSendingAgentIdsByConversationName.get(var1);
   }

   private SendingAgentImpl getLocalSendingAgent(String var1) {
      return (SendingAgentImpl)this.localSendingAgentIdsByConversationName.get(var1);
   }

   void removeLocalSendingAgentId(SAFConversationInfo var1) {
      this.localSendingAgentIdsByConversationName.remove(var1.getConversationName());
      if (var1.getDynamicConversationName() != null) {
         this.localSendingAgentIdsByConversationName.remove(var1.getDynamicConversationName());
      }

      if (var1.getCreateConversationMessageID() != null) {
         this.localSendingAgentIdsByConversationName.remove(var1.getCreateConversationMessageID());
      }

      this.cleanUpConversationInfoOnSendingSide(var1.getConversationName());
   }

   public SAFConversationHandle registerConversationOnReceivingSide(SAFConversationInfo var1) throws SAFException {
      return this.registerConversationOnReceivingSide(var1, (SAFManager.ConversationNameRefinementCallback)null, (String)null);
   }

   public SAFConversationHandle registerConversationOnReceivingSide(SAFConversationInfo var1, SAFManager.ConversationNameRefinementCallback var2, String var3) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("Registering conversation:" + var1.getConversationName() + " on the receiving side");
      }

      ReceivingAgentImpl var4 = this.findLocalReceivingAgent(var1);
      if (var4 == null) {
         var4 = (ReceivingAgentImpl)getNextLocalAgent(ReceivingAgentImpl.getAgentFactory(), var3);
      }

      if (var4 == null) {
         throw new SAFServiceNotAvailException("There is no active SAF receiving agent available on the server" + (var3 != null ? " that is configured to use store '" + var3 + "'" : ""));
      } else {
         if (var2 != null) {
            SAFManager.LocationInfo var5 = new SAFManager.LocationInfo(var4.getStoreName());
            var2.conversationPreStore(var1, var5);
         }

         this.addConversationInfoOnReceivingSide(var1);
         ConversationReassembler var7 = var4.getConversation(var1);
         if (var7 == null) {
            var7 = var4.createConversation(var1, true);
         }

         if (var7 == null) {
            SAFException var6 = new SAFException("Cannot send messages to a conversation that was never registered, timed out, or destroyed");
            throw var6;
         } else {
            var7.setAgentConnectionEstablished();
            return new SAFConversationHandleImpl(var1.getConversationName(), var1.getConversationTimeout(), var1.getMaximumIdleTime(), var1.getConversationOffer(), var1.getCreateConversationMessageID(), var1.getContext());
         }
      }
   }

   public SAFManager.LocationInfo getLocationInfoForConversationOnSendingSide(String var1) {
      SendingAgentImpl var2 = this.findLocalSendingAgent(var1);
      return var2 == null ? null : new SAFManager.LocationInfo(var2.getStoreName());
   }

   public SAFManager.LocationInfo getLocationInfoForConversationOnReceivingSide(SAFConversationInfo var1) {
      ReceivingAgentImpl var2 = this.findLocalReceivingAgent(var1);
      return var2 == null ? null : new SAFManager.LocationInfo(var2.getStoreName());
   }

   public long getLastAcknowledged(SAFConversationInfo var1) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getLastAcknowledged():" + var1);
      }

      ReceivingAgentImpl var2 = this.findLocalReceivingAgent(var1);
      ConversationReassembler var3 = var2.getConversation(var1);
      return var3.getLastAcked();
   }

   public SAFConversationInfo getCachedConversationInfoOnReceivingSide(String var1) throws SAFException {
      checkShutdown();
      synchronized(this.conversationInfosOnReceivingSide) {
         return (SAFConversationInfo)this.conversationInfosOnReceivingSide.get(var1);
      }
   }

   public SAFConversationInfo getConversationInfoOnReceivingSide(String var1) throws SAFException {
      checkShutdown();
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getConversationInfoOnReceivingSide():" + var1);
      }

      return this.getCachedConversationInfoOnReceivingSide(var1);
   }

   public SAFConversationInfo getCachedConversationInfoOnSendingSide(String var1) throws SAFException {
      checkShutdown();
      synchronized(this.conversationInfosOnSendingSide) {
         return (SAFConversationInfo)this.conversationInfosOnSendingSide.get(var1);
      }
   }

   public SAFConversationInfo getConversationInfoOnSendingSide(String var1) throws SAFException {
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getConversationInfoOnSendingSide():" + var1);
      }

      return this.getCachedConversationInfoOnSendingSide(var1);
   }

   public boolean checkForConversationClosedOnReceivingSide(String var1) throws SAFException {
      return this.getConversationInfoOnReceivingSide(var1) == null;
   }

   public boolean checkForConversationClosedOnSendingSide(String var1) throws SAFException {
      return this.getConversationInfoOnSendingSide(var1) == null;
   }

   private void cleanUpConversationInfoOnSendingSide(String var1) {
      try {
         SAFConversationInfo var2 = this.getConversationInfoOnSendingSide(var1);
         if (var2 != null) {
            synchronized(this.conversationInfosOnSendingSide) {
               this.conversationInfosOnSendingSide.remove(var1);
               this.notifyRemoveConverationFromCache(true, "ConversationName", var1, var2);
               if (var2.isDynamic() && var2.getDynamicConversationName() != null) {
                  this.conversationInfosOnSendingSide.remove(var2.getDynamicConversationName());
                  this.notifyRemoveConverationFromCache(true, "DynamicName", var2.getDynamicConversationName(), var2);
               }

               if (var2.getCreateConversationMessageID() != null) {
                  this.conversationInfosOnSendingSide.remove(var2.getCreateConversationMessageID());
                  this.notifyRemoveConverationFromCache(true, "CreateConversationMessageD", var2.getCreateConversationMessageID(), var2);
               }
            }
         }
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   public void closeConversationOnReceivingSide(SAFConversationInfo var1) throws SAFException {
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("closeConversationOnReceivingSide():" + var1);
      }

      ReceivingAgentImpl var2 = this.findLocalReceivingAgent(var1);
      var2.removeConversation(var1);
   }

   public void closeRAConversation(SAFConversationInfo var1) {
      synchronized(this.conversationInfosOnReceivingSide) {
         this.conversationInfosOnReceivingSide.remove(var1.getConversationName());
         this.notifyRemoveConverationFromCache(false, "ConversationName", var1.getConversationName(), var1);
         if (var1.getCreateConversationMessageID() != null) {
            this.conversationInfosOnReceivingSide.remove(var1.getCreateConversationMessageID());
            this.notifyRemoveConverationFromCache(false, "CreateConversationMessageID", var1.getCreateConversationMessageID(), var1);
         }

      }
   }

   void addConversationInfoOnReceivingSide(SAFConversationInfo var1) {
      synchronized(this.conversationInfosOnReceivingSide) {
         this.conversationInfosOnReceivingSide.put(var1.getConversationName(), var1);
         this.notifyAddConverationToCache(false, "ConversationName", var1.getConversationName(), var1);
         if (var1.getCreateConversationMessageID() != null) {
            this.conversationInfosOnReceivingSide.put(var1.getCreateConversationMessageID(), var1);
            this.notifyAddConverationToCache(false, "CreateConversationMessageID", var1.getCreateConversationMessageID(), var1);
         }

      }
   }

   public void acknowledge(String var1, long var2, long var4) throws SAFException {
      checkShutdown();
      SendingAgentImpl var6 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("acknowledge(): agent =  " + var6 + " conversation = " + var1 + " sequence number low = " + var2 + " sequence number high = " + var4);
      }

      var6.acknowledge(var1, var2, var4);
   }

   public void handleAsyncFault(String var1, String var2, Exception var3) throws SAFException {
      checkShutdown();
      SendingAgentImpl var4 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("handleFault(): agent =  " + var4 + " conversation = " + var1 + " message ID  = " + var2 + " Exception = " + var3.getMessage());
      }

      var4.handleAsyncFault(var1, var2, var3);
   }

   public void createConversationSucceeded(SAFConversationHandle var1) throws SAFException {
      checkShutdown();
      SendingAgentImpl var2;
      if (var1.getCreateConversationMessageID() != null) {
         var2 = this.getSendingAgent(var1.getCreateConversationMessageID());
      } else {
         var2 = this.getSendingAgent(var1.getConversationName());
      }

      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("onCreateSequenceSucceed(): found agent: " + var2 + " for conversation " + var1.getConversationName() + " and createMsgID " + var1.getCreateConversationMessageID());
      }

      var2.onCreateConversationSucceed(var1);
   }

   public void storeConversationContextOnReceivingSide(String var1, Externalizable var2) throws SAFException {
      checkShutdown();
      SAFConversationInfo var3 = this.getCachedConversationInfoOnReceivingSide(var1);
      if (var3 == null) {
         throw new SAFException("Unknown conversation: " + var1);
      } else {
         var3.setContext(var2);
         ReceivingAgentImpl var4 = this.getReceivingAgent(var3);
         if (SAFDebug.SAFManager.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("storeConversationContextOnReceivingSide(): found agent: " + var4 + " for conversation " + var1);
         }

         var4.storeConversationInfo(var3);
      }
   }

   public void storeConversationContextOnSendingSide(String var1, Externalizable var2) throws SAFException {
      checkShutdown();
      SendingAgentImpl var3 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("storeConversationContextOnSendingSide(): found agent: " + var3 + " for conversation " + var1);
      }

      SAFConversationInfo var4 = this.getCachedConversationInfoOnSendingSide(var1);
      if (var4 == null) {
         throw new SAFException("Unknown conversation: " + var1);
      } else {
         var4.setContext(var2);
         var3.storeConversationInfo(var4);
      }
   }

   public List<Long> getAllSequenceNumberRangesOnReceivingSide(String var1) throws SAFException {
      SAFConversationInfo var2 = this.getCachedConversationInfoOnReceivingSide(var1);
      if (var2 == null) {
         throw new SAFException("Unknown conversation: " + var1);
      } else {
         ReceivingAgentImpl var3 = this.getReceivingAgent(var2);
         if (SAFDebug.SAFManager.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("getAllSequenceNumberRangesOnReceivingSide(): found agent: " + var3 + " for conversation " + var1);
         }

         ConversationReassembler var4 = var3.getConversation(var2);
         return var4.getAllSequenceNumberRanges();
      }
   }

   public long getLastAssignedSequenceValueOnSendingSide(String var1) throws SAFException {
      checkShutdown();
      SendingAgentImpl var2 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getLastAssignedSequenceValueOnSendingSide(): found agent: " + var2 + " for conversation " + var1);
      }

      ConversationAssembler var3 = var2.getConversation(var1);
      return var3.getLastAssignedSequenceValue();
   }

   public List<Long> getAllSequenceNumberRangesOnSendingSide(String var1) throws SAFException {
      checkShutdown();
      SendingAgentImpl var2 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getAllSequenceNumberRangesOnSendingSide(): found agent: " + var2 + " for conversation " + var1);
      }

      ConversationAssembler var3 = var2.getConversation(var1);
      return var3.getAllSequenceNumberRanges();
   }

   public boolean hasSentLastMessageOnSendingSide(String var1) throws SAFException {
      checkShutdown();
      SendingAgentImpl var2 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("hasSentLastMessageOnSendingSide(): found agent: " + var2 + " for conversation " + var1);
      }

      ConversationAssembler var3 = var2.getConversation(var1);
      return var3.hasSeenLastMsg();
   }

   public void setSentLastMessageOnSendingSide(String var1, long var2) throws SAFException {
      checkShutdown();
      SendingAgentImpl var4 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("setSentLastMessageOnSendingSide(): found agent: " + var4 + " for conversation " + var1);
      }

      ConversationAssembler var5 = var4.getConversation(var1);
      var5.setSeenLastMsg(true);
      var5.setLastMsgSequenceNumber(var2);
      var5.checkCompleted();
   }

   public boolean hasReceivedLastMessageOnReceivingSide(String var1) throws SAFException {
      SAFConversationInfo var2 = this.getCachedConversationInfoOnReceivingSide(var1);
      if (var2 == null) {
         throw new SAFException("Unknown conversation: " + var1);
      } else {
         ReceivingAgentImpl var3 = this.getReceivingAgent(var2);
         if (SAFDebug.SAFManager.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("hasReceivedLastMessageOnReceivingSide(): found agent: " + var3 + " for conversation " + var1);
         }

         ConversationReassembler var4 = var3.getConversation(var2);
         return var4.hasSeenLastMsg();
      }
   }

   public long getLastMessageSequenceNumberOnReceivingSide(String var1) throws SAFException {
      SAFConversationInfo var2 = this.getCachedConversationInfoOnReceivingSide(var1);
      if (var2 == null) {
         throw new SAFException("Unknown conversation: " + var1);
      } else {
         ReceivingAgentImpl var3 = this.getReceivingAgent(var2);
         if (SAFDebug.SAFManager.isDebugEnabled()) {
            SAFDebug.SAFManager.debug("getLastMessageSequenceNumberOnReceivingSide(): found agent: " + var3 + " for conversation " + var1);
         }

         ConversationReassembler var4 = var3.getConversation(var2);
         return var4.getLastMsgSequenceNumber();
      }
   }

   public long getLastMessageSequenceNumberOnSendingSide(String var1) throws SAFException {
      checkShutdown();
      SendingAgentImpl var2 = this.getSendingAgent(var1);
      if (SAFDebug.SAFManager.isDebugEnabled()) {
         SAFDebug.SAFManager.debug("getLastMessageSequenceNumberOnSendingSide(): found agent: " + var2 + " for conversation " + var1);
      }

      ConversationAssembler var3 = var2.getConversation(var1);
      return var3.getLastMsgSequenceNumber();
   }

   public void addConversationLifecycleListener(SAFManager.ConversationLifecycleListener var1) {
      this.conversationLifecycleListeners.add(var1);
   }

   public void removeConversationLifecycleListener(SAFManager.ConversationLifecycleListener var1) {
      this.conversationLifecycleListeners.remove(var1);
   }

   void notifyAddConverationToCache(boolean var1, String var2, String var3, SAFConversationInfo var4) {
      int var5;
      if (var1) {
         synchronized(this.conversationInfosOnSendingSide) {
            var5 = this.conversationInfosOnSendingSide.size();
         }
      } else {
         synchronized(this.conversationInfosOnReceivingSide) {
            var5 = this.conversationInfosOnReceivingSide.size();
         }
      }

      SAFManager.ConversationLifecycleListener[] var6 = (SAFManager.ConversationLifecycleListener[])((SAFManager.ConversationLifecycleListener[])this.conversationLifecycleListeners.toArray(new SAFManager.ConversationLifecycleListener[0]));

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var6[var7].addToCache(var1, var2, var3, var4, var5);
      }

   }

   void notifyRemoveConverationFromCache(boolean var1, String var2, String var3, SAFConversationInfo var4) {
      int var5;
      if (var1) {
         synchronized(this.conversationInfosOnSendingSide) {
            var5 = this.conversationInfosOnSendingSide.size();
         }
      } else {
         synchronized(this.conversationInfosOnReceivingSide) {
            var5 = this.conversationInfosOnReceivingSide.size();
         }
      }

      SAFManager.ConversationLifecycleListener[] var6 = (SAFManager.ConversationLifecycleListener[])this.conversationLifecycleListeners.toArray(new SAFManager.ConversationLifecycleListener[0]);

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var6[var7].removeFromCache(var1, var2, var3, var4, var5);
      }

   }

   void notifyPreConversationClose(boolean var1, boolean var2, SAFConversationInfo var3) {
      SAFManager.ConversationLifecycleListener[] var4 = (SAFManager.ConversationLifecycleListener[])this.conversationLifecycleListeners.toArray(new SAFManager.ConversationLifecycleListener[0]);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5].preClose(var1, var2, var3);
      }

   }

   void notifyAckConversation(SAFConversationInfo var1, long var2, long var4) {
      SAFManager.ConversationLifecycleListener[] var6 = (SAFManager.ConversationLifecycleListener[])((SAFManager.ConversationLifecycleListener[])this.conversationLifecycleListeners.toArray(new SAFManager.ConversationLifecycleListener[0]));

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var6[var7].ack(var1, var2, var4);
      }

   }

   public Set getConversationNamesOnSendingSide() {
      synchronized(this.conversationInfosOnSendingSide) {
         return new HashSet(this.conversationInfosOnSendingSide.keySet());
      }
   }

   public Set getConversationNamesOnReceivingSide() {
      synchronized(this.conversationInfosOnReceivingSide) {
         return new HashSet(this.conversationInfosOnReceivingSide.keySet());
      }
   }

   private static void checkShutdown() throws SAFException {
      if (SAFServerService.getService() == null) {
         throw new SAFServiceNotAvailException("Reliable Messaging cannot be invoked from a java client");
      } else {
         try {
            SAFServerService.getService().checkShutdown();
         } catch (ServiceFailureException var1) {
            throw new SAFServiceNotAvailException("SAF service is shutting down");
         }
      }
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("SAF");
      HealthState var3 = SAFService.getSAFService().getRuntimeMBean().getHealthState();
      SAFDiagnosticImageSource.dumpHealthStateElement(var2, var3);
      var2.writeStartElement("EndpointManagers");
      Object[] var4 = this.endpointManagers.values().toArray();
      var2.writeAttribute("count", String.valueOf(var4.length));

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var2.writeStartElement("EndpointManager");
         var2.writeCharacters(var4[var5].toString());
         var2.writeEndElement();
      }

      var2.writeEndElement();
      var2.writeStartElement("Transports");
      Object[] var10 = this.transports.values().toArray();
      var2.writeAttribute("count", String.valueOf(var10.length));

      for(int var6 = 0; var6 < var10.length; ++var6) {
         SAFTransport var7 = (SAFTransport)var10[var6];
         var2.writeStartElement("Transport");
         var2.writeAttribute("type", String.valueOf(var7.getType()));
         var2.writeAttribute("isGapsAllowed", String.valueOf(var7.isGapsAllowed()));
         var2.writeEndElement();
      }

      var2.writeEndElement();
      var2.writeStartElement("SendingAgents");
      Object[] var11 = this.localSendingAgents.toArray();
      var2.writeAttribute("count", String.valueOf(var11.length));

      for(int var12 = 0; var12 < var11.length; ++var12) {
         SendingAgentImpl var8 = (SendingAgentImpl)var11[var12];
         var8.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeStartElement("ReceivingAgents");
      Object[] var13 = this.localReceivingAgents.toArray();
      var2.writeAttribute("count", String.valueOf(var13.length));

      for(int var14 = 0; var14 < var13.length; ++var14) {
         ReceivingAgentImpl var9 = (ReceivingAgentImpl)var13[var14];
         var9.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }
}
