package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.server.SequenceData;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFEndpointManager;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFTransport;
import weblogic.messaging.saf.SAFTransportType;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.store.SAFStore;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkAdapter;

public final class ConversationReassembler extends Conversation implements NakedTimerListener {
   private SAFEndpoint destination;
   private int windowSize;
   private final ReceivingAgentImpl receivingAgent;
   private final SAFTransport transport;
   private final QOSHandler qosHandler;
   private long ackInterval;
   private long lastAckTime;
   private final int origWindowSize;
   private int qos;
   private QOSWorkRequest workRequest;
   private final SAFEndpointManager safEndpointManager;
   private boolean endpointIsDown = true;
   private long idleTimeMaximum;
   private final TimerManager timerManager;
   private Timer timeOutTimer;
   private long timeoutCurrent = Long.MAX_VALUE;
   private boolean expired = false;
   private boolean closed;
   private long absTTL;
   private boolean establishSAConnection;
   private long lastMsgSequenceNumber = Long.MAX_VALUE;
   private Destination kernelDestination;
   private Sequence sequence;
   private Object operationLock = new Object();
   private boolean firstRun = true;

   ConversationReassembler(ReceivingAgentImpl var1, int var2, SAFEndpoint var3, SAFConversationInfo var4, SAFStore var5, boolean var6) throws SAFException {
      super(var4, var5, AgentImpl.getSAFManager());
      this.safEndpointManager = this.safManager.getEndpointManager(var4.getDestinationType());
      this.windowSize = this.origWindowSize = var1.getWindowSize();
      this.transport = this.safManager.getTransport(var2);
      this.receivingAgent = var1;
      this.destination = var3;
      this.qosHandler = QOSHandler.getQOSHandler(var4, this.transport, (long)this.windowSize);
      this.initQOS(var4);
      this.initAckInterval();
      TimerManagerFactory var7 = TimerManagerFactory.getTimerManagerFactory();
      this.timerManager = var7.getTimerManager("SAFRECEIVER_" + var1.getName(), var1.getWorkManager());
      this.initTimeToLive();
      this.absTTL = ((SAFConversationInfoImpl)var4).getTimestamp() + this.timeToLive;
      if (this.absTTL < 0L) {
         this.absTTL = Long.MAX_VALUE;
      }

      this.initIdleTimeMaximum();
      this.initTimeoutTimer();
      if (SAFTransportType.isConnectionless(var4.getTransportType())) {
         this.establishSAConnection = true;
      } else {
         this.establishSAConnection = var6;
      }

   }

   private synchronized void ensureStarted() throws SAFException {
      if (this.kernelDestination == null) {
         BEDestinationImpl var1 = JMSServerUtilities.findBEDestinationByJNDIName(this.destination.getTargetQueue());
         if (var1 == null) {
            throw new SAFException("SAF conversation " + this.getName() + " is not ready. BEDestination not found for JNDI name: " + this.destination.getTargetQueue());
         } else {
            this.kernelDestination = var1.getKernelDestination();
            if (this.kernelDestination == null) {
               throw new SAFException("SAF conversation " + this.getName() + " is not ready. BEDestination at JNDI name " + this.destination.getTargetQueue() + " didn't have a kernel destination");
            } else {
               try {
                  synchronized(this.kernelDestination) {
                     this.sequence = this.kernelDestination.findSequence(this.getName());
                     if (this.sequence == null) {
                        this.sequence = this.kernelDestination.createSequence(this.getName(), 4);
                        this.sequence.setPassthru(true);
                     }
                  }

                  SequenceData var2 = (SequenceData)this.sequence.getUserData();
                  if (var2 != null) {
                     this.lastMsgSequenceNumber = ((SAFSequenceData)var2).getLastMsgSequenceNumber();
                  } else {
                     this.sequence.setUserData(new SAFSequenceData(this.info));
                  }

                  this.qosHandler.setSequence(this.sequence);
               } catch (KernelException var5) {
                  throw new SAFException("Failed to setup conversation " + this.getName(), var5);
               }
            }
         }
      }
   }

   private void initTimeoutTimer() {
      long var1 = System.currentTimeMillis();
      if (this.timeToLive != 0L) {
         if (this.timeToLive != Long.MAX_VALUE) {
            this.timeOutTimer = this.timerManager.schedule(this, this.timeToLive);
            this.timeoutCurrent = this.timeOutTimer.getTimeout();
         }

         this.info.setConversationTimeout(this.timeToLive);
         if (this.idleTimeMaximum != Long.MAX_VALUE) {
            this.rescheduleTimeoutTimer(var1, var1 + this.idleTimeMaximum, false);
         }

      } else {
         this.expired = true;
         this.timeoutCurrent = var1;
         this.info.setConversationTimeout(0L);
      }
   }

   private void initIdleTimeMaximum() {
      long var1 = this.info.getMaximumIdleTime();
      if (var1 == 0L) {
         this.idleTimeMaximum = this.receivingAgent.getConversationIdleTimeMaximum();
      } else {
         this.idleTimeMaximum = var1;
      }

      this.info.setMaximumIdleTime(this.idleTimeMaximum);
   }

   private void initTimeToLive() {
      long var1 = this.info.getTimeToLive();
      if (var1 == 0L) {
         this.timeToLive = Long.MAX_VALUE;
      } else {
         this.timeToLive = var1;
      }

      long var3 = this.receivingAgent.getDefaultTimeToLive();
      boolean var5 = (this.info.getTimeoutPolicy() & 1) != 0;
      boolean var6 = (this.info.getTimeoutPolicy() & 2) != 0;
      long var7 = this.info.getConversationTimeout();
      if (var6) {
         var7 = var3;
      }

      if (var5) {
         this.timeToLive = var7;
      } else if (this.timeToLive > var7) {
         this.timeToLive = var7;
      }

      this.info.setTimeToLive(this.timeToLive);
   }

   private void initQOS(SAFConversationInfo var1) {
      switch (this.qos = var1.getQOS()) {
         case 1:
            this.workRequest = new ExactlyOnceQOSWorkRequest();
            break;
         case 2:
            this.workRequest = new AtLeastOnceQOSWorkRequest();
            break;
         case 3:
            this.workRequest = new AtmostOnceQOSWorkRequest();
            break;
         default:
            throw new Error(" Unknown QOS. Contact BEA Support");
      }

   }

   private void initAckInterval() {
      this.ackInterval = this.receivingAgent.getAckInterval();
      if (this.qos == 1 && this.ackInterval == -1L) {
         this.ackInterval = Long.MAX_VALUE;
      }

   }

   private void rescheduleTimeoutTimer(long var1, long var3, boolean var5) {
      if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Conversation '" + this.info.getConversationName() + "' == rescheduleTimeoutTimer(): old  = " + this.timeoutCurrent + " new = " + var3 + " absTTL = " + this.absTTL);
      }

      synchronized(this) {
         if (var3 < 0L && var1 < this.absTTL) {
            var3 = this.absTTL;
         }

         if ((var5 || var3 < this.timeoutCurrent) && var3 != Long.MAX_VALUE && var3 >= 0L) {
            if (this.absTTL < var3) {
               var3 = this.absTTL;
            }

            cancelTimer(this.timeOutTimer);
            this.timeOutTimer = this.timerManager.schedule(this, new Date(var3));
            this.timeoutCurrent = var3;
         }
      }
   }

   private static void cancelTimer(Timer var0) {
      if (var0 != null) {
         var0.cancel();
      }

   }

   public void timerExpired(Timer var1) {
      if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Conversation '" + this.info.getConversationName() + "': timed out");
      }

      try {
         synchronized(this) {
            this.expired = true;
         }

         this.receivingAgent.removeConversation(this.info);
      } catch (SAFException var5) {
         if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug("Conversation '" + this.info.getConversationName() + "': failed to expire " + var5.getStackTrace());
         }
      }

   }

   public void addMessage(SAFRequest var1) throws SAFException {
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Conversation '" + this.info.getConversationName() + "' == addMessage(): " + var1.getSequenceNumber());
      }

      this.ensureStarted();
      boolean var2 = false;
      long var3 = -1L;
      synchronized(this) {
         if (var1.getSequenceNumber() > this.lastMsgSequenceNumber) {
            throw new SAFException("Cannot send more messages after a message that is marked as last message in the conversation/sequence", 19);
         }

         if (var1.isEndOfConversation() && !this.hasSeenLastMsg()) {
            this.lastMsgSequenceNumber = var1.getSequenceNumber();
            var2 = true;
            var3 = this.lastMsgSequenceNumber;
         }
      }

      if (var2) {
         SAFSequenceData var5 = (SAFSequenceData)this.sequence.getUserData();
         var5.setLastMsgSequenceNumber(var3);

         try {
            this.sequence.setUserData(var5);
         } catch (KernelException var9) {
            throw new SAFException(var9.getMessage(), var9);
         }

         this.setSeenLastMsg(true);
      }

      MessageReference var11 = new MessageReference(var1);
      synchronized(this) {
         this.addMessageToListInorder(var11);
      }

      if (this.idleTimeMaximum != Long.MAX_VALUE) {
         long var6 = System.currentTimeMillis();
         this.rescheduleTimeoutTimer(var6, var6 + this.idleTimeMaximum, true);
      }

      this.scheduleWorkRequestRun();
   }

   long getLastMsgSequenceNumber() {
      return this.lastMsgSequenceNumber;
   }

   public int getWindowSize() {
      return this.windowSize;
   }

   public final void close() throws SAFException {
      this.close(false);
   }

   public final void close(boolean var1) throws SAFException {
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
         SAFDebug.SAFReceivingAgent.debug("Conversation '" + this.info.getConversationName() + "' == close(): destroy = " + var1);
      }

      synchronized(this.operationLock) {
         Sequence var3;
         label52: {
            cancelTimer(this.timeOutTimer);
            this.safManager.notifyPreConversationClose(false, var1, this.info);
            this.safManager.closeRAConversation(this.info);
            var3 = null;
            synchronized(this) {
               if (!this.closed) {
                  this.closed = true;
                  var3 = this.sequence;
                  if (this.running) {
                     this.running = false;
                  }

                  this.firstMessage = this.lastMessage = null;
                  this.establishSAConnection = false;
                  this.kernelDestination = null;
                  this.sequence = null;
                  this.timeOutTimer = null;
                  this.workRequest = null;
                  break label52;
               }
            }

            return;
         }

         if (var1) {
            this.removeConversationInfo();

            try {
               if (var3 != null) {
                  var3.delete(false);
               }
            } catch (Exception var7) {
               throw new SAFException(var7.toString(), var7);
            }
         }

      }
   }

   public void finishConversation() throws SAFException {
      this.close(true);
   }

   void removeConversationInfo() throws SAFException {
      this.store.removeConversationInfo(this.info);
   }

   public final long getLastAcked() throws SAFException {
      this.ensureStarted();
      return this.sequence == null ? 0L : this.sequence.getLastValue();
   }

   public final synchronized void setAgentConnectionEstablished() {
      this.establishSAConnection = true;
   }

   private boolean refreshDestination() {
      SAFEndpoint var1 = this.safEndpointManager.getEndpoint(this.info.getDestinationURL());
      if (var1 == null) {
         return this.endpointIsDown = true;
      } else {
         this.endpointIsDown = !var1.isAvailable();
         if (this.endpointIsDown) {
            this.safEndpointManager.removeEndpoint(this.info.getDestinationURL());
            return this.endpointIsDown;
         } else {
            this.safEndpointManager.addEndpoint(this.info.getDestinationURL(), var1);
            this.destination = var1;
            return this.endpointIsDown;
         }
      }
   }

   private void scheduleWorkRequestRun() {
      if (!this.isClosed() && !this.workRequest.isScheduled()) {
         boolean var1;
         synchronized(this) {
            var1 = this.endpointIsDown && !this.firstRun;
            this.firstRun = false;
            if (this.isConversationNotRunnable()) {
               return;
            }

            this.running = true;
         }

         if (var1) {
            synchronized(this.workRequest.scheduledMonitor) {
               if (this.workRequest.setScheduled()) {
                  this.timerManager.schedule(new DelayedScheduleTimerListener(), 10000L);
               }
            }
         } else {
            synchronized(this.workRequest.scheduledMonitor) {
               if (this.workRequest.setScheduled()) {
                  this.receivingAgent.getWorkManager().schedule(this.workRequest);
               }
            }
         }

      }
   }

   private boolean isConversationNotRunnable() {
      boolean var1 = !this.establishSAConnection || this.endpointIsDown && (this.endpointIsDown = this.refreshDestination()) || this.running || this.expired;
      return var1;
   }

   public synchronized boolean isClosed() {
      return this.closed;
   }

   public List<Long> getAllSequenceNumberRanges() {
      if (this.sequence == null) {
         return new ArrayList();
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.sequence.getAllSequenceNumberRanges().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            var1.add((Long)var3);
         }

         return var1;
      }
   }

   public String toString() {
      return this.info.toString();
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ConversationReassembler");
      super.dumpAttributes(var1, var2);
      var2.writeAttribute("destination", this.destination.toString());
      var2.writeAttribute("windowSize", String.valueOf(this.windowSize));
      var2.writeAttribute("ackInterval", String.valueOf(this.ackInterval));
      var2.writeAttribute("lastAckTime", String.valueOf(this.lastAckTime));
      var2.writeAttribute("endpointIsDown", String.valueOf(this.endpointIsDown));
      ((SAFConversationInfoImpl)this.getInfo()).dump(var1, var2);
      var2.writeEndElement();
   }

   private final class AtmostOnceQOSWorkRequest extends QOSWorkRequest {
      private AtmostOnceQOSWorkRequest() {
         super(null);
      }

      protected final boolean processRequestBeforeAck(MessageReference var1) {
         this.updateQOSHandler(var1, 0);
         return false;
      }

      protected final boolean processRequestsAfterAck(MessageReference var1, MessageReference var2) {
         MessageReference var3 = var1;
         boolean var4 = false;

         do {
            if (var4 = this.processRequest(var3, true)) {
               return var4;
            }
         } while(var3 != var2 && (var3 = var3.getNext()) != null);

         return false;
      }

      // $FF: synthetic method
      AtmostOnceQOSWorkRequest(Object var2) {
         this();
      }
   }

   private final class AtLeastOnceQOSWorkRequest extends QOSWorkRequest {
      private AtLeastOnceQOSWorkRequest() {
         super(null);
      }

      // $FF: synthetic method
      AtLeastOnceQOSWorkRequest(Object var2) {
         this();
      }
   }

   private final class ExactlyOnceQOSWorkRequest extends QOSWorkRequest {
      private ExactlyOnceQOSWorkRequest() {
         super(null);
      }

      // $FF: synthetic method
      ExactlyOnceQOSWorkRequest(Object var2) {
         this();
      }
   }

   private class QOSWorkRequest extends WorkAdapter {
      private final Object scheduledMonitor;
      private boolean scheduled;

      private QOSWorkRequest() {
         this.scheduledMonitor = "ScheduledMonitor";
      }

      protected boolean setScheduled() {
         synchronized(this.scheduledMonitor) {
            if (this.scheduled) {
               return false;
            } else {
               this.scheduled = true;
               return true;
            }
         }
      }

      protected boolean isScheduled() {
         synchronized(this.scheduledMonitor) {
            return this.scheduled;
         }
      }

      public final void run() {
         MessageReference var1 = null;
         MessageReference var2 = null;
         MessageReference var3 = null;
         boolean var4 = false;

         try {
            while(true) {
               synchronized(ConversationReassembler.this) {
                  if (!ConversationReassembler.this.running) {
                     return;
                  }

                  ConversationReassembler.this.endpointIsDown = var4;
                  if (ConversationReassembler.this.firstMessage == null || ConversationReassembler.this.endpointIsDown || ConversationReassembler.this.windowSize < 1 || ConversationReassembler.this.poisoned) {
                     this.stopRunning();
                     break;
                  }

                  var1 = this.getOrderedListWithNoGap(ConversationReassembler.this.firstMessage);
               }

               boolean var5 = false;
               this.preProcessSAFRequests(var1);
               var2 = var1;
               int var7 = 0;

               do {
                  ConversationReassembler.this.qosHandler.setCurrentSAFRequest(var1.getMessage());
                  if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
                     this.debugProcessingSplitList(var1);
                  }

                  boolean var6 = this.checkIfSequenceNumberBiggerThanLastMsgOfConv(var1);
                  if (var6) {
                     this.handleInvalidMessage(new SAFException("Cannot send more messages after a message that is marked as last message in the conversation/sequence", 19), var1);
                     synchronized(ConversationReassembler.this) {
                        var1 = var1.getNext();
                        ConversationReassembler.this.removeMessageFromList(var1);
                        ConversationReassembler.this.windowSize--;
                     }

                     ++var7;
                     break;
                  }

                  if (var5 = this.processRequestBeforeAck(var1)) {
                     synchronized(ConversationReassembler.this) {
                        ConversationReassembler.this.endpointIsDown = true;
                        break;
                     }
                  }

                  synchronized(ConversationReassembler.this) {
                     ConversationReassembler.this.windowSize--;
                  }

                  ++var7;
                  var3 = var1;
                  var1 = var1.getNext();
               } while(!this.mustAck() && var1 != null);

               if (var3 == null) {
                  this.postProcessSAFRequests(var7, var1, var5);
               } else {
                  this.postProcessSAFRequests(var7, var3.getNext(), var5);
               }

               var4 = this.processRequestsAfterAck(var2, var3);
            }
         } catch (SAFException var41) {
            if (var3 == null) {
               this.handleSAFException(var41, var1);
            } else {
               this.handleSAFException(var41, var3);
            }
         } catch (Throwable var42) {
            var42.printStackTrace();
         } finally {
            boolean var14 = false;
            synchronized(ConversationReassembler.this) {
               ConversationReassembler.this.running = false;
               if (ConversationReassembler.this.firstMessage != null) {
                  var14 = true;
               }
            }

            synchronized(this.scheduledMonitor) {
               this.scheduled = false;
               if (var14) {
                  ConversationReassembler.this.scheduleWorkRequestRun();
               }

            }
         }

      }

      private void stopRunning() throws SAFException {
         ConversationReassembler.this.running = false;
         if (ConversationReassembler.this.windowSize < 1) {
            throw new SAFException(" Sending Side not honoring the windowSize");
         } else if (ConversationReassembler.this.poisoned) {
            throw new SAFException("Conversation poisoned for Conversation = " + ConversationReassembler.this.info);
         }
      }

      private void handleInvalidMessage(SAFException var1, MessageReference var2) {
         int var3 = var1.getResultCode();
         ConversationReassembler.this.qosHandler.setSAFException(var1);
         ConversationReassembler.this.qosHandler.update(var2, var3);
         ConversationReassembler.this.qosHandler.sendNack();
      }

      private void handleSAFException(SAFException var1, MessageReference var2) {
         int var3 = var1.getResultCode();
         ConversationReassembler.this.qosHandler.setSAFException(var1);
         ConversationReassembler.this.qosHandler.update(var2, var3);
         ConversationReassembler.this.qosHandler.sendNack();
         boolean var4 = Conversation.isPoisoned(var3);
         synchronized(ConversationReassembler.this) {
            ConversationReassembler.this.poisoned = var4;
            if (!ConversationReassembler.this.poisoned && var2 != null) {
               ConversationReassembler.this.restoreMessages(var2);
            }

            ConversationReassembler.this.running = false;
         }
      }

      private void sendAck(int var1, MessageReference var2) {
         synchronized(ConversationReassembler.this) {
            ConversationReassembler.this.windowSize = var1;
            if (var2 != null) {
               ConversationReassembler.this.restoreMessages(var2);
            }
         }

         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == sendAck(): windowSize = " + ConversationReassembler.this.windowSize);
         }

         ConversationReassembler.this.qosHandler.sendAck();
      }

      private void debugProcessingSplitList(MessageReference var1) {
         SAFDebug.SAFReceivingAgent.debug("###################################################");
         SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == execute(): current sequence= " + var1.getSequenceNumber() + " first message = " + (ConversationReassembler.this.firstMessage == null ? -1L : ConversationReassembler.this.firstMessage.getSequenceNumber()));
         SAFDebug.SAFReceivingAgent.debug("###################################################");
      }

      private MessageReference getOrderedListWithNoGap(MessageReference var1) {
         MessageReference var2 = var1;
         long var5 = 0L;

         long var3;
         do {
            var3 = var2.getSequenceNumber();

            do {
               var2 = var2.getNext();
               if (var2 == null) {
                  break;
               }

               var5 = var2.getSequenceNumber();
            } while(var3 == var5);
         } while(var2 != null && var5 == var3 + 1L);

         ConversationReassembler.this.firstMessage = var2;
         if (var2 != null) {
            var2.getPrev().setNext((MessageReference)null);
            var2.setPrev((MessageReference)null);
         } else {
            ConversationReassembler.this.lastMessage = var2;
         }

         return var1;
      }

      private boolean mustAck() {
         long var1 = System.currentTimeMillis();
         if (var1 - ConversationReassembler.this.lastAckTime < ConversationReassembler.this.ackInterval && ConversationReassembler.this.windowSize != ConversationReassembler.this.origWindowSize / 2) {
            return false;
         } else {
            ConversationReassembler.this.lastAckTime = var1;
            return true;
         }
      }

      protected void preProcessSAFRequests(MessageReference var1) {
         ConversationReassembler.this.qosHandler.preProcess(var1);
      }

      protected boolean postProcessSAFRequests(int var1, MessageReference var2, boolean var3) throws SAFException {
         if (!var3) {
            this.sendAck(var1, var2);
            return true;
         } else {
            SAFException var4 = new SAFException(" Failed to deliver message to the Endpoint for Conversation = " + ConversationReassembler.this.info, ConversationReassembler.this.qosHandler.getResult().getSAFException(), ConversationReassembler.this.qosHandler.getResult().getResultCode());
            throw var4;
         }
      }

      private boolean checkIfSequenceNumberBiggerThanLastMsgOfConv(MessageReference var1) {
         synchronized(ConversationReassembler.this) {
            if (var1 != null && var1.getMessage() != null) {
               return var1.getMessage().getSequenceNumber() > ConversationReassembler.this.lastMsgSequenceNumber;
            } else {
               return false;
            }
         }
      }

      protected boolean processRequestBeforeAck(MessageReference var1) {
         return this.processRequest(var1, false);
      }

      protected boolean processRequestsAfterAck(MessageReference var1, MessageReference var2) {
         synchronized(ConversationReassembler.this) {
            return ConversationReassembler.this.endpointIsDown;
         }
      }

      protected final boolean processRequest(MessageReference var1, boolean var2) {
         int var3 = 0;
         int var4 = 0;
         SAFRequest var5 = var1.getMessage();
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == processRequest(): conversation: " + var5.getConversationName() + " request: " + var5.getSequenceNumber());
         }

         Externalizable var6 = var5.getPayload();
         if (var6 == null) {
            if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
               SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == processRequest(): request: " + var5.getSequenceNumber() + " is the close conversation request" + " or gap message");
            }

            ++var4;
         } else {
            try {
               this.deliverToEndpoint(var5);
               ++var4;
            } catch (SAFException var8) {
               var3 = ConversationReassembler.this.qosHandler.handleEndpointDeliveryFailure(var8, var5);
            }
         }

         var3 = var3 == 0 ? this.checkNumMessagesAcked(var4, 1, var5) : var3;
         if (!var2) {
            this.updateQOSHandler(var1, var3);
         }

         return var3 != 0;
      }

      private void deliverToEndpoint(SAFRequest var1) throws SAFException {
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == processRequest(): request: " + var1.getSequenceNumber());
         }

         synchronized(ConversationReassembler.this.operationLock) {
            if (ConversationReassembler.this.isClosed()) {
               throw new SAFConversationNotAvailException("Failed to send a message: conversation " + var1.getConversationName() + " has completed, expired or terminated," + " or has been destroyed administratively");
            }

            ConversationReassembler.this.destination.deliver(ConversationReassembler.this.info, var1);
         }

         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
            SAFDebug.SAFReceivingAgent.debug("Conversation '" + ConversationReassembler.this.info.getConversationName() + "' == processRequest(): request: " + var1.getSequenceNumber() + " has been delivered to the endpoint");
         }

      }

      protected final boolean updateQOSHandler(MessageReference var1, int var2) {
         ConversationReassembler.this.qosHandler.update(var1, var2);
         return var2 == 0;
      }

      private int checkNumMessagesAcked(int var1, int var2, SAFRequest var3) {
         if (var1 != 0 && var1 == var2) {
            return 0;
         } else {
            String var4 = " Number of Messages acknowledged is not equal to number of messages processed acknowlegdedCount = " + var1 + " numRequests = " + var2;
            SAFException var5 = new SAFException(var4);
            return ConversationReassembler.this.qosHandler.handleEndpointDeliveryFailure(var5, var3);
         }
      }

      // $FF: synthetic method
      QOSWorkRequest(Object var2) {
         this();
      }
   }

   public class DelayedScheduleTimerListener implements NakedTimerListener {
      public void timerExpired(Timer var1) {
         if (!ConversationReassembler.this.isClosed()) {
            ConversationReassembler.this.receivingAgent.getWorkManager().schedule(ConversationReassembler.this.workRequest);
         }
      }
   }
}
