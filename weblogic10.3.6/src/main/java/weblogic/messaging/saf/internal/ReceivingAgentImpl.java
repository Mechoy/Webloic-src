package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.util.HashMap;
import java.util.Iterator;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.health.HealthState;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFServiceNotAvailException;
import weblogic.messaging.saf.common.AgentDeliverRequest;
import weblogic.messaging.saf.common.AgentDeliverResponse;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.store.SAFStore;

public final class ReceivingAgentImpl extends AgentImpl implements ReceivingAgent, Externalizable {
   static final long serialVersionUID = -5246833642083492445L;
   private final HashMap conversations = new HashMap();
   private int windowSize;
   private long defaultTimeToLive = Long.MAX_VALUE;
   private long conversationIdleTimeMaximum = Long.MAX_VALUE;
   private boolean started;
   private boolean isPausedForReceiving;
   private long ackInterval;
   private static final SAFAgentFactoryInternal agentFactoryInternal = new SAFAgentFactoryInternal();
   private static final SAFManager manager = SAFManagerImpl.getManager();

   public ReceivingAgentImpl() {
   }

   ReceivingAgentImpl(String var1, SAFAgentAdmin var2, SAFStore var3) throws NamingException, SAFException {
      super(var1, var2, var3, 2);
      if (SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Receiving Agent '" + var1 + "':" + " TimeToLive=" + this.defaultTimeToLive + ", ConversationIdleTimeMaximum=" + this.conversationIdleTimeMaximum + ", WindowSize=" + this.windowSize);
      }

   }

   protected void startInitialize(SAFAgentMBean var1) {
      this.defaultTimeToLive = var1.getDefaultTimeToLive();
      this.conversationIdleTimeMaximum = var1.getConversationIdleTimeMaximum();
      if (this.defaultTimeToLive == 0L) {
         this.defaultTimeToLive = Long.MAX_VALUE;
      }

      if (this.conversationIdleTimeMaximum == 0L) {
         this.conversationIdleTimeMaximum = Long.MAX_VALUE;
      }

      this.windowSize = var1.getWindowSize();
      this.ackInterval = var1.getAcknowledgeInterval();
      this.isPaused = this.isPausedForReceiving = var1.isReceivingPausedAtStartup();
   }

   protected void addToAgentFactory() {
      safManager.addLocalReceivingAgent(this);
      agentFactoryInternal.addAgent(this);
   }

   protected void removeFromAgentFactory() {
      agentFactoryInternal.removeAgent(this);
      safManager.removeLocalReceivingAgent(this);
   }

   static SAFAgentFactoryInternal getAgentFactory() {
      return agentFactoryInternal;
   }

   public void setConversationInfosFromStore(HashMap var1) {
      this.conversationInfosFromStore = var1;
   }

   protected void start() throws SAFException {
      synchronized(this) {
         if (this.started) {
            return;
         }
      }

      Iterator var1;
      synchronized(this.conversationInfosFromStore) {
         var1 = ((HashMap)this.conversationInfosFromStore.clone()).values().iterator();
      }

      if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Receiving anget '" + this.getName() + "' recovered " + this.conversationInfosFromStore.size() + " conversations from the store");
      }

      while(var1.hasNext()) {
         SAFConversationInfo var2 = (SAFConversationInfo)var1.next();

         try {
            this.createConversation(var2);
         } catch (SAFException var7) {
            this.healthState = updateHealthState(this.healthState, 3, var7.getMessage());
            throw var7;
         }
      }

      synchronized(this) {
         this.started = true;
      }
   }

   int getWindowSize() {
      return this.windowSize;
   }

   long getAckInterval() {
      return this.ackInterval;
   }

   public AgentDeliverResponse deliver(AgentDeliverRequest var1) throws SAFException {
      if (this.isPausedForReceiving()) {
         if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug(" [ReceivingAgent.deliver()]: the agent is paused.");
         }

         throw new SAFServiceNotAvailException("ReceivingAgent " + this + " is paused.");
      } else {
         return var1.finishDeliver(this);
      }
   }

   public void suspend(boolean var1) {
      this.close(false);
   }

   public void resume() throws SAFException {
      this.start();
   }

   public synchronized void close(boolean var1) {
      this.closeInternal(var1);
   }

   private void closeInternal(boolean var1) {
      this.unadvertise();
      Iterator var2 = this.conversations.values().iterator();

      while(var2.hasNext()) {
         try {
            ((ConversationReassembler)var2.next()).close(var1);
         } catch (SAFException var4) {
            if (SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
               var4.printStackTrace();
            }
         }
      }

      this.conversations.clear();
      this.store.close();
   }

   public long getDefaultTimeToLive() {
      return this.defaultTimeToLive;
   }

   private void createConversation(SAFConversationInfo var1) throws SAFException {
      this.createConversation(var1, false);
   }

   ConversationReassembler createConversation(SAFConversationInfo var1, boolean var2) throws SAFException {
      if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Receiving Agent '" + this.name + "': about to create conversation " + var1);
      }

      if (var1 == null) {
         return null;
      } else {
         boolean var3 = false;
         SAFConversationInfo var4 = null;
         ConversationReassembler var5;
         synchronized(this) {
            var5 = this.getConversation(var1);
            if (var5 == null) {
               SAFEndpoint var7 = safManager.getEndpointManager(var1.getDestinationType()).getEndpoint(var1.getDestinationURL());
               ((SAFConversationInfoImpl)var1).setTimestamp(System.currentTimeMillis());
               safManager.addConversationInfoOnReceivingSide(var1);
               var5 = new ConversationReassembler(this, var1.getTransportType(), var7, var1, this.store, var2);
               if (var2 && (var4 = var1.getConversationOffer()) != null) {
                  var3 = true;
               }

               this.conversations.put(var1, var5);
               this.storeConversationInfo(var1);
               if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFReceivingAgent.debug("Receiving Agent '" + this.name + "' after created conversation = " + var5);
               }
            }
         }

         if (var3) {
            manager.registerConversationOnSendingSide(var4, (SAFManager.ConversationNameRefinementCallback)null);
         }

         return var5;
      }
   }

   public synchronized ConversationReassembler getConversation(SAFConversationInfo var1) {
      return (ConversationReassembler)this.conversations.get(var1);
   }

   public void removeConversation(SAFConversationInfo var1) throws SAFException {
      ConversationReassembler var2;
      synchronized(this) {
         var2 = this.getConversation(var1);
      }

      if (var2 != null) {
         var2.finishConversation();
      }

      synchronized(this) {
         this.conversations.remove(var1);
         this.conversationInfosFromStore.remove(var1.getConversationName());
      }

      if (var2 == null) {
         throw new SAFException("ConversationReassembler Not Found for conversation: " + var1.getConversationName());
      }
   }

   public synchronized void pauseReceiving() {
      if (!this.isPausedForReceiving) {
         this.isPaused = this.isPausedForReceiving = true;
         this.unadvertise();
      }
   }

   public synchronized void resumeReceiving() {
      if (this.isPausedForReceiving) {
         this.isPaused = this.isPausedForReceiving = false;
         this.advertise();
      }
   }

   public synchronized boolean isPausedForReceiving() {
      return this.isPausedForReceiving;
   }

   void setWindowSize(int var1) {
      this.windowSize = var1;
   }

   synchronized void setDefaultTimeToLive(long var1) {
      if (this.defaultTimeToLive == 0L) {
         this.defaultTimeToLive = Long.MAX_VALUE;
      } else {
         this.defaultTimeToLive = var1;
      }

   }

   synchronized void setAcknowledgementInterval(long var1) {
      this.ackInterval = var1;
   }

   synchronized void setConversationIdleTimeMaximum(long var1) {
      if (this.conversationIdleTimeMaximum == 0L) {
         this.conversationIdleTimeMaximum = Long.MAX_VALUE;
      } else {
         this.conversationIdleTimeMaximum = var1;
      }

   }

   synchronized long getConversationIdleTimeMaximum() {
      return this.conversationIdleTimeMaximum;
   }

   HealthState getHealthState() {
      return this.healthState;
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ReceivingAgent");
      super.dump(var1, var2);
      var2.writeAttribute("windowSize", String.valueOf(this.windowSize));
      var2.writeAttribute("defaultTimeToLive", String.valueOf(this.defaultTimeToLive));
      var2.writeAttribute("conversationIdleTimeMaximum", String.valueOf(this.conversationIdleTimeMaximum));
      var2.writeAttribute("started", String.valueOf(this.started));
      var2.writeAttribute("isPausedForReceiving", String.valueOf(this.isPausedForReceiving));
      var2.writeAttribute("ackInterval", String.valueOf(this.ackInterval));
      var2.writeAttribute("idleTimeMaximum", String.valueOf(this.conversationIdleTimeMaximum));
      SAFDiagnosticImageSource.dumpHealthStateElement(var2, this.getAgentAdmin().getHealthState());
      var2.writeStartElement("ConversationReassemblers");
      Object[] var3 = this.conversations.values().toArray();
      var2.writeAttribute("count", String.valueOf(var3.length));

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ConversationReassembler var5 = (ConversationReassembler)var3[var4];
         var5.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }
}
