package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.management.ManagementException;
import weblogic.messaging.common.SQLExpression;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFEndpointManager;
import weblogic.messaging.saf.SAFErrorAwareEndpointManager;
import weblogic.messaging.saf.SAFErrorAwareTransport;
import weblogic.messaging.saf.SAFErrorHandler;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFInvalidAcknowledgementsException;
import weblogic.messaging.saf.SAFLogger;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFTransport;
import weblogic.messaging.saf.SAFTransportException;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.common.SAFRemoteContext;
import weblogic.messaging.saf.common.SAFRequestImpl;
import weblogic.messaging.saf.store.SAFStore;
import weblogic.messaging.saf.store.SAFStoreException;
import weblogic.messaging.util.DeliveryList;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class ConversationAssembler extends Conversation implements NakedTimerListener, Runnable {
   private static final int STATE_INITIAL = 1;
   private static final int STATE_STARTING = 2;
   private static final int STATE_DESTROYED = 4;
   private static final int STATE_EXPIRED = 8;
   private static final int STATE_CREATING = 16;
   private static final int STATE_CREATED = 32;
   private static final int STATE_STARTED = 64;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private long lastAckedSequenceNumber;
   private TimerManager timerManager;
   private Timer messageRetryTimer;
   private Timer transportRetryTimer;
   private RetryController retryController;
   private double retryDelayMultiplier;
   private long retryDelayBase;
   private long retryDelayMaximum;
   private boolean loggingEnabled;
   private SendingAgentImpl sendingAgent;
   private int windowSize;
   private final SAFTransport transport;
   private Timer timeOutTimer;
   private long timeToLive;
   private long idleTimeMaximum;
   private long absTTL;
   private long timeoutCurrent = Long.MAX_VALUE;
   private ConversationRuntimeDelegate runtimeDelegate;
   private int state = 1;
   private boolean needNotify;
   private RemoteEndpointRuntimeDelegate remoteEndpoint;
   private Queue subQueue;
   private MessageReader reader;
   private TransportRetryTimerListener transportRetryListener;
   private MessageRetryTimerListener messageRetryListener;
   private long lastMsgSequenceNumber = Long.MAX_VALUE;
   private Throwable lastSendError;

   ConversationAssembler(SendingAgentImpl var1, SAFConversationInfo var2, SAFStore var3, boolean var4, int var5) throws ManagementException, SAFException {
      super(var2, var3, SendingAgentImpl.getSAFManager());
      this.transport = this.safManager.getTransport(var2.getTransportType());
      if (this.transport == null) {
         throw new SAFException("Internal Error: invalid transaport type " + var2.getTransportType());
      } else {
         this.timeToLive = var2.getTimeToLive();
         if (this.timeToLive == -1L) {
            this.timeToLive = var1.getDefaultTimeToLive();
         }

         if (this.timeToLive == 0L) {
            this.timeToLive = Long.MAX_VALUE;
         }

         var2.setTimeToLive(this.timeToLive);
         this.idleTimeMaximum = var2.getMaximumIdleTime();
         if (this.idleTimeMaximum == -1L) {
            this.idleTimeMaximum = var1.getDefaultMaximumIdleTime();
         }

         if (this.idleTimeMaximum == 0L) {
            this.idleTimeMaximum = Long.MAX_VALUE;
         }

         var2.setMaximumIdleTime(this.idleTimeMaximum);
         this.loggingEnabled = var4;
         this.windowSize = var5;
         this.ordered = var2.isInorder();
         this.sendingAgent = var1;
         TimerManagerFactory var6 = TimerManagerFactory.getTimerManagerFactory();
         this.timerManager = var6.getTimerManager("SAFSENDER_" + this.sendingAgent.getName(), this.sendingAgent.getWorkManager());

         try {
            final SAFAgentAdmin var8 = this.sendingAgent.getAgentAdmin();
            this.runtimeDelegate = (ConversationRuntimeDelegate)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws ManagementException {
                  return new ConversationRuntimeDelegate(var8, ConversationAssembler.this);
               }
            });
            this.runtimeDelegate.registerMe();
         } catch (PrivilegedActionException var10) {
            throw (ManagementException)var10.getException();
         }

         this.absTTL = ((SAFConversationInfoImpl)var2).getTimestamp() + this.timeToLive;
         if (this.absTTL < 0L) {
            this.absTTL = Long.MAX_VALUE;
         }

         if (this.timeToLive != Long.MAX_VALUE) {
            long var7 = this.getTimeLeft();
            if (var7 <= 0L) {
               this.changeState(8);
            }

            this.timeOutTimer = this.timerManager.schedule(this, var7 == 0L ? 100L : var7);
            this.timeoutCurrent = this.timeOutTimer.getTimeout();
         }

         SAFRemoteContext var11 = var2.getRemoteContext();
         if (var11 != null && var11.getRetryDelayBase() != -1L) {
            this.retryDelayBase = var11.getRetryDelayBase();
         } else {
            this.retryDelayBase = this.sendingAgent.getDefaultRetryDelayBase();
         }

         if (var11 != null && var11.getRetryDelayMaximum() != -1L) {
            this.retryDelayMaximum = var11.getRetryDelayMaximum();
         } else {
            this.retryDelayMaximum = this.sendingAgent.getDefaultRetryDelayMaximum();
         }

         if (this.timeToLive != Long.MAX_VALUE && (double)this.retryDelayMaximum > (double)this.timeToLive * 0.5) {
            this.retryDelayMaximum = (long)((double)this.timeToLive * 0.5);
         }

         if (this.idleTimeMaximum != Long.MAX_VALUE && (double)this.retryDelayMaximum > (double)this.idleTimeMaximum * 0.5) {
            this.retryDelayMaximum = (long)((double)this.idleTimeMaximum * 0.5);
         }

         if (var11 != null && var11.getRetryDelayMultiplier() != -1L) {
            this.retryDelayMultiplier = (double)var11.getRetryDelayMultiplier();
         } else {
            this.retryDelayMultiplier = this.sendingAgent.getDefaultRetryDelayMultiplier();
         }

         this.retryController = new RetryController(this.retryDelayBase, this.retryDelayMaximum, this.retryDelayMultiplier);
         this.transportRetryListener = new TransportRetryTimerListener();
         this.messageRetryListener = new MessageRetryTimerListener();
         if (!this.isExpired() && this.idleTimeMaximum != Long.MAX_VALUE) {
            long var12 = System.currentTimeMillis();
            this.rescheduleTimeoutTimer(var12, var12 + this.idleTimeMaximum, true);
         }

         this.remoteEndpoint = this.sendingAgent.findOrCreateRemoteEndpointRuntime(var2.getDestinationURL(), var2.getDestinationType(), this.sendingAgent.getKernelTopic(var2));
         this.remoteEndpoint.addConversation(this.runtimeDelegate);
      }
   }

   public long getLastAssignedSequenceValue() {
      if (this.isClosed()) {
         return 0L;
      } else {
         Sequence var1 = this.subQueue.findSequence(this.info.getConversationName());
         return var1.getLastAssignedValue();
      }
   }

   public List getAllSequenceNumberRanges() {
      if (this.isClosed()) {
         return new ArrayList();
      } else {
         Sequence var1 = this.subQueue.findSequence(this.info.getConversationName());
         return var1.getAllSequenceNumberRanges();
      }
   }

   long getLastMsgSequenceNumber() {
      return this.lastMsgSequenceNumber;
   }

   void setLastMsgSequenceNumber(long var1) {
      this.lastMsgSequenceNumber = var1;
   }

   void setupSubscriptionQueue() throws SAFException {
      if (!this.isClosed()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': setting up subscription queue for conversation " + this.info.getConversationName());
         }

         Topic var1 = this.sendingAgent.getKernelTopic(this.info);
         HashMap var2 = new HashMap();
         var2.put("Durable", new Boolean(true));

         try {
            Kernel var3 = this.sendingAgent.getKernel();
            synchronized(var3) {
               this.subQueue = var3.findQueue(this.info.getConversationName());
               if (this.subQueue == null) {
                  this.subQueue = var3.createQueue(this.info.getConversationName(), var2);
               }
            }

            this.subQueue.setProperty("DefaultAssignSequence", this.info.getConversationName());
            this.subQueue.setProperty("Quota", var1.getQuota());
            KernelRequest var4 = new KernelRequest();
            var1.subscribe(this.subQueue, new SQLExpression("SAFConversationName='" + this.info.getConversationName() + "'"), var4);
            var4.getResult();
            this.reader = new MessageReader();
            this.subQueue.resume(16384);
            if (this.sendingAgent.isPausedForForwarding() || this.remoteEndpoint.isPausedForForwarding()) {
               this.subQueue.suspend(2);
            }

            if (this.sendingAgent.isPausedForIncoming() || this.remoteEndpoint.isPausedForIncoming()) {
               this.subQueue.suspend(1);
            }

         } catch (KernelException var7) {
            throw new SAFException(var7);
         }
      }
   }

   private boolean isDynamic() {
      return this.info.isDynamic();
   }

   private void start() throws SAFException {
      if (this.state == 1 && this.info.isConversationAlreadyCreated()) {
         this.changeState(32);
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Recover CREATED state according to conversation info");
         }
      }

      Exception var1 = null;
      if (!this.isClosed()) {
         synchronized(this) {
            if (this.isStarted()) {
               return;
            }

            if (this.state == 16) {
               return;
            }

            if (this.state == 2) {
               return;
            }

            if (!this.info.isDynamic() && !this.isCreated()) {
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFSendingAgent.debug("Marking non-dynamic conversation '" + this.info.getConversationName() + " CREATED (won't wait for a handle to be returned)");
               }

               this.changeState(32);
            }

            if (!this.isCreated()) {
               this.changeState(16);
            } else {
               this.changeState(2);
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            if (!this.isCreated()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + this.getName() + "' == start(): dyanmic conversation has not been created, creating ...");
            } else if (!this.isStarted()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + this.getName() + "' == start(): starting the reader ...");
            }
         }

         boolean var2 = false;

         try {
            if (!this.isCreated()) {
               Externalizable var3 = this.transport.createSecurityToken(this.info);
               if (var3 != null) {
                  Externalizable var4 = null;
                  synchronized(this) {
                     var4 = this.info.getContext();
                     this.info.setContext(var3);
                  }

                  try {
                     this.sendingAgent.storeConversationInfo(this.info);
                  } catch (SAFException var10) {
                     synchronized(this) {
                        this.info.setContext(var4);
                     }

                     throw var10;
                  }
               }

               SAFConversationHandle var16 = this.transport.createConversation(this.info);
               var2 = this.processConversationHandle(var16);
            }

            if (this.isCreated() && !this.isStarted()) {
               this.changeState(2);
               if (this.reader != null) {
                  this.reader.start();
               }

               this.cancelTransportRetryTimer();
               this.retryController.reset();
               this.changeState(64);
            }
         } catch (Exception var12) {
            var1 = var12;
            this.resetToLastGoodState(var12);
         }

         boolean var15 = false;
         synchronized(this) {
            if (this.isCreated() && this.firstMessage != null && this.messageRetryTimer == null) {
               var15 = true;
            }
         }

         if (var15) {
            this.rescheduleMessageRetryTimer();
         }

         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            if (this.isStarted()) {
               if (var2) {
                  SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': successfully started conversation " + this);
               }
            } else if (var1 != null) {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': failed to start conversation " + this.info.getConversationName() + " " + var1.toString() + ", will retry later");
               var1.printStackTrace();
            } else {
               SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': waiting for create conversation callback for " + this.info.getConversationName());
            }
         }

         if (var1 != null) {
            if (var1 instanceof SAFException) {
               throw (SAFException)var1;
            } else {
               throw new SAFException("Failed to start conversation " + this.info.getConversationName(), var1);
            }
         }
      }
   }

   private synchronized void resetToLastGoodState(Throwable var1) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Resetting to last 'good' state for conversation '" + this.info.getConversationName());
         if (var1 == null) {
            var1 = new Exception("Debug resetToLastGoodState");
         }

         ((Throwable)var1).printStackTrace();
      }

      if (this.state != 2 && this.state != 64) {
         if (this.state == 16) {
            this.changeState(1);
         } else if (this.state != 32 && this.state != 1) {
            this.changeState(1);
         }
      } else {
         this.changeState(32);
      }

   }

   private synchronized void changeState(int var1) {
      this.logStateChange(this.state, var1);
      this.state = var1;
      boolean var2 = this.isCreated() && !this.info.isConversationAlreadyCreated() || !this.isCreated() && this.info.isConversationAlreadyCreated();
      if (var2) {
         this.info.setConversationAlreadyCreated(!this.info.isConversationAlreadyCreated());

         try {
            this.sendingAgent.storeConversationInfo(this.info);
         } catch (SAFStoreException var4) {
            var4.printStackTrace();
         }
      }

   }

   private void logStateChange(int var1, int var2) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         String var3 = getStateStr(var1);
         String var4 = getStateStr(var2);
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.getName() + "' setting state to " + var4 + " from state " + var3);
      }

   }

   private static String getStateStr(int var0) {
      String var1;
      switch (var0) {
         case 1:
            var1 = "STATE_INITIAL";
            break;
         case 2:
            var1 = "STATE_STARTING";
            break;
         case 4:
            var1 = "STATE_DESTROYED";
            break;
         case 8:
            var1 = "STATE_EXPIRED";
            break;
         case 16:
            var1 = "STATE_CREATING";
            break;
         case 32:
            var1 = "STATE_CREATED";
            break;
         case 64:
            var1 = "STATE_STARTED";
            break;
         default:
            var1 = "UNKNOWN";
      }

      return var1;
   }

   void startFromADiffThread() {
      if (!this.isStarted()) {
         this.transportRetryTimer = this.timerManager.schedule(this.transportRetryListener, 100L);
      }

   }

   private void processConversationTimeout(long var1, SAFConversationHandle var3) {
      long var4 = var3.getConversationTimeout();
      long var6 = Long.MAX_VALUE;
      if (var4 != Long.MAX_VALUE) {
         var6 = var1 + var4;
         if (var6 < 0L) {
            var6 = Long.MAX_VALUE;
         }
      }

      this.timeToLive = var4;
      this.rescheduleTimeoutTimer(var1, var6, true);
   }

   private void processConversationMaxIdleTime(long var1, SAFConversationHandle var3) {
      long var4 = var3.getConversationMaxIdleTime();
      long var6 = Long.MAX_VALUE;
      if (this.idleTimeMaximum > var4) {
         if (var4 != Long.MAX_VALUE) {
            var6 = var1 + var4;
         }

         this.idleTimeMaximum = var4;
      } else if (this.idleTimeMaximum != Long.MAX_VALUE) {
         var6 = var1 + this.idleTimeMaximum;
      }

      if (var6 < 0L) {
         var6 = Long.MAX_VALUE;
      }

      this.rescheduleTimeoutTimer(var1, var6, true);
   }

   void createAndRecordDynamicConversation(String var1, String var2) {
      if (this.sendingAgent != null) {
         this.sendingAgent.addDynamicName(var1, var2);
         this.safManager.recordDynamicName(var1, var2);
      }

   }

   long getTimeLeft() {
      long var1 = System.currentTimeMillis();
      return this.getTimeLeft(var1, this.absTTL);
   }

   private long getTimeLeft(long var1, long var3) {
      if (var3 != 0L) {
         long var5 = var3 - var1;
         return var5 <= 0L ? 0L : var5;
      } else {
         return -1L;
      }
   }

   private MessageReference addMessage(MessageElement var1) {
      if (this.isClosed()) {
         return null;
      } else {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': message reader is about to add message: " + ((SAFRequest)var1.getMessage()).getMessageId() + "' sequence number: " + var1.getSequenceNum() + " to the conversation " + ((SAFRequest)var1.getMessage()).getConversationName());
         }

         SAFRequestImpl var2 = (SAFRequestImpl)var1.getMessage();
         var2.setSequenceNumber(var1.getSequenceNum());
         MessageReference var3 = new MessageReference(var1, this.retryDelayMultiplier, this.retryDelayBase, this.retryDelayMaximum);
         if (this.isNotAvail()) {
            this.rejectOneMessage(var3, 5, (Throwable)null, true);
            return var3;
         } else {
            synchronized(this) {
               this.addMessageToList(var3);
            }

            if (this.idleTimeMaximum != 0L && this.idleTimeMaximum != Long.MAX_VALUE) {
               this.rescheduleTimeoutTimer(System.currentTimeMillis() + this.idleTimeMaximum);
            }

            if (var2.isEndOfConversation()) {
               this.setSeenLastMsg(true);
               this.setLastMsgSequenceNumber(var2.getSequenceNumber());
            }

            return var3;
         }
      }
   }

   private void removeMessage(MessageReference var1) {
      this.removeMessage(var1, true);
   }

   private void removeMessage(MessageReference var1, boolean var2) {
      if (!this.isClosed()) {
         synchronized(this) {
            this.removeMessageFromList(var1);
            if (this.firstMessage == null) {
               this.cancelMessageRetryTimer();
            }
         }

         if (var2) {
            try {
               ArrayList var3 = new ArrayList(1);
               var3.add(var1.getElement());
               KernelRequest var4 = this.subQueue.acknowledge(var3);
               if (var4 != null) {
                  var4.getResult();
               }
            } catch (KernelException var5) {
            }
         }

         if (!this.isExpired() && this.idleTimeMaximum != 0L && this.idleTimeMaximum != Long.MAX_VALUE) {
            this.rescheduleTimeoutTimer(System.currentTimeMillis() + this.idleTimeMaximum);
         }

      }
   }

   private void rejectOneMessage(MessageReference var1, int var2, Throwable var3, boolean var4) {
      var1.setFaultCode(var2);
      ArrayList var5 = new ArrayList();
      if (var3 != null) {
         var5.add(var3);
      }

      this.rejectOneMessage(var1.getMessage(), var1.getFaultCodes(), var5, var4);
   }

   private void rejectOneMessage(SAFRequest var1, ArrayList var2, ArrayList var3, boolean var4) {
      if (!this.isClosed()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': rejecting message " + var1.getSequenceNumber());
         }

         SAFEndpointManager var5 = this.safManager.getEndpointManager(this.info.getDestinationType());
         SAFErrorHandler var6 = this.info.getErrorHandler();
         if (var6 == null) {
            var6 = var5.getErrorHandler(this.info.getDestinationURL());
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': Errorhandler =  " + var6);
         }

         boolean var7 = false;
         if (var6 != null) {
            if (var6.isAlwaysForward() && var4) {
               var7 = true;
            } else if (var5 instanceof SAFErrorAwareEndpointManager) {
               SAFErrorAwareEndpointManager var8 = (SAFErrorAwareEndpointManager)var5;
               var8.handleFailure(var6, var1, var2, var3);
            } else {
               var5.handleFailure(var6, var1, var2);
            }
         } else {
            var7 = true;
         }

         if (var7 && this.loggingEnabled) {
            SAFLogger.logExpiredMessage("'" + var1.getMessageId() + "'");
         }

         RemoteEndpointRuntimeDelegate var9 = this.sendingAgent.getRemoteEndpoint(this.info.getDestinationURL());
         var9.increaseFailedMessagesCount();
         this.sendingAgent.increaseFailedMessagesCount();
      }
   }

   private synchronized boolean isNotAvail() {
      return this.state == 8 || this.state == 4 || this.isClosed();
   }

   public synchronized boolean isNotAvailAndClosed() {
      boolean var1 = this.state == 8 || this.state == 4 || this.hasSeenLastMsg();
      return var1;
   }

   private synchronized boolean isExpired() {
      return this.state == 8;
   }

   final void close() {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' is to be closed");
      }

      this.closeInternal(false);
   }

   final synchronized void delete() {
      if (!this.isClosed()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' is to be destroyed.");
         }

         boolean var1 = true;
         this.safManager.notifyPreConversationClose(true, var1, this.info);

         try {
            this.closeInternal(var1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         try {
            this.sendingAgent.removeConversation(this.getName(), true);
            if (this.info.getDynamicConversationName() != null) {
               this.sendingAgent.removeConversation(this.info.getDynamicConversationName(), false);
            }

            this.sendingAgent = null;
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   private synchronized void closeInternal(boolean var1) {
      if (!this.isClosed()) {
         this.cancelAllTimers();
         if (this.reader != null) {
            this.reader.stop();
         }

         this.reader = null;
         if (var1) {
            try {
               if (this.isCreated() && this.needNotify) {
                  this.transport.terminateConversation(this.info);
               }
            } catch (SAFException var8) {
            }

            try {
               if (this.sendingAgent != null && this.subQueue != null) {
                  Topic var2 = this.sendingAgent.getKernelTopic(this.info);
                  synchronized(this.subQueue) {
                     if (var2 != null) {
                        var2.unsubscribe(this.subQueue, new KernelRequest());
                     }

                     this.subQueue.delete(new KernelRequest());
                  }
               }
            } catch (KernelException var7) {
               var7.printStackTrace();
            }

            this.subQueue = null;
            this.remoteEndpoint = null;
         }

         try {
            if (this.runtimeDelegate != null) {
               this.runtimeDelegate.unregister();
               this.runtimeDelegate = null;
            }
         } catch (ManagementException var5) {
         }

         if (this.running) {
            this.running = false;
         }

         this.firstMessage = this.lastMessage = null;
      }
   }

   private final void complete() {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==complete(): all acknowledgements are back, " + "we cleanup the conversation and notify receiving side");
      }

      this.needNotify = true;
      this.cancelAllTimers();
      this.delete();
   }

   private synchronized void cancelAllTimers() {
      this.cancelTimeOutTimer();
      this.cancelTransportRetryTimer();
      this.cancelMessageRetryTimer();
   }

   private synchronized boolean isDestroyed() {
      return this.state == 4;
   }

   void expireAllMessages(int var1, Throwable var2) throws KernelException {
      if (!this.isClosed()) {
         this.cancelAllTimers();
         MessageReference var3 = null;
         synchronized(this) {
            var3 = this.firstMessage;
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': expiring all messages left in the conversation" + " firstMessage = " + var3);
         }

         while(var3 != null) {
            this.destroyOneMessage(var3, var1, var2);
            synchronized(this) {
               var3 = this.firstMessage;
            }
         }

         Cursor var4 = null;

         try {
            if (this.subQueue != null) {
               var4 = this.subQueue.createCursor(true, (Expression)null, -1);
            }
         } catch (KernelException var13) {
            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               var13.printStackTrace();
            }

            throw var13;
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': expiring " + var4.size() + " messages left in the queue");
         }

         if (var4 != null) {
            ArrayList var6 = new ArrayList();
            var6.add(new Integer(var1));
            ArrayList var7 = new ArrayList();
            if (var2 != null) {
               var7.add(var2);
            }

            if (this.lastSendError != null) {
               var7.add(this.lastSendError);
            }

            MessageElement var5;
            try {
               while((var5 = var4.next()) != null) {
                  SAFRequest var8 = (SAFRequest)var5.getMessage();
                  this.rejectOneMessage(var8, var6, var7, true);
                  KernelRequest var9 = this.subQueue.delete(var5);
                  if (var9 != null) {
                     var9.getResult();
                  }
               }
            } catch (KernelException var12) {
               throw var12;
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': expired all messages");
         }

      }
   }

   public void destroy() {
      this.needNotify = true;
      this.destroyInternal();
   }

   private void destroyInternal() {
      synchronized(this) {
         if (!this.isExpired()) {
            this.changeState(4);
         }
      }

      this.destroyAllMessages();
      this.delete();
   }

   private void destroyAllMessages() {
      if (!this.isClosed()) {
         KernelRequest var1 = new KernelRequest();

         try {
            this.subQueue.empty(var1);
            var1.getResult();
         } catch (KernelException var3) {
         }

      }
   }

   private void disconnected(Exception var1) {
      if (!this.isClosed()) {
         this.remoteEndpoint.disconnected(var1);
      }
   }

   private boolean processConversationHandle(SAFConversationHandle var1) throws SAFException {
      if (var1 == null) {
         if (!this.info.isDynamic()) {
            this.changeState(32);
         }

         return false;
      } else {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Processing handle for conversation '" + this.info.getConversationName() + "' in state " + getStateStr(this.state));
         }

         long var2 = System.currentTimeMillis();
         String var4 = null;
         String var5 = var1.getDynamicConversationName();
         boolean var6 = false;
         SAFConversationInfo var7 = var1.getOffer();
         Externalizable var8 = var1.getConversationContext();
         synchronized(this) {
            if (this.info.getDynamicConversationName() != null) {
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFSendingAgent.debug("Handle already processed for conversation '" + this.info.getConversationName() + "' in state " + getStateStr(this.state) + ". Ignoring this handle");
               }

               return false;
            } else {
               this.info.setTimeToLive(this.timeToLive);
               this.info.setMaximumIdleTime(this.idleTimeMaximum);
               this.info.setConversationOffer(var1.getOffer());
               if (var8 != null) {
                  this.info.setContext(var8);
               }

               var4 = this.info.getConversationName();
               this.info.setDynamicConversationName(var5);
               if (var5 != null && !var5.equals(this.info.getConversationName())) {
                  var6 = true;
               }

               try {
                  if (var7 != null) {
                     this.safManager.registerConversationOnReceivingSide(var7, (SAFManager.ConversationNameRefinementCallback)null, (String)null);
                  }

                  this.sendingAgent.storeConversationInfo(this.info);
               } catch (SAFException var15) {
                  synchronized(this) {
                     this.info.setDynamicConversationName((String)null);
                  }

                  throw var15;
               }

               if (var6) {
                  this.createAndRecordDynamicConversation(var4, var5);
               }

               this.processConversationTimeout(var2, var1);
               this.processConversationMaxIdleTime(var2, var1);
               this.changeState(32);
               return true;
            }
         }
      }
   }

   void onCreateConversationSucceed(SAFConversationHandle var1) {
      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': onCreateConversationSucceed " + this.info.getConversationName() + " processing async/dynamic handle");
      }

      synchronized(this) {
         if (this.isClosed()) {
            return;
         }

         if (this.state != 16) {
            throw new IllegalStateException("Ouch. We haven't started creating conversation '" + this.info.getConversationName() + "' but yet we just got an async conversation handle. Cannot proceed");
         }
      }

      this.cancelTransportRetryTimer();
      boolean var2 = false;
      Object var3 = null;

      SAFException var4;
      try {
         this.processConversationHandle(var1);
      } catch (SAFException var12) {
         var4 = var12;
         synchronized(this) {
            this.resetToLastGoodState(var4);
         }

         this.rescheduleTransportRetryTimer();
         if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': failed to start conversation " + this.info.getConversationName() + " " + ((Exception)var3).getMessage() + ", will retry later");
         }

         return;
      }

      try {
         if (this.isCreated() && !this.isStarted()) {
            this.changeState(2);
            if (this.reader != null) {
               this.reader.start();
            }

            this.changeState(64);
            this.retryController.reset();
         }
      } catch (SAFException var11) {
         var4 = var11;
         synchronized(this) {
            this.resetToLastGoodState(var4);
         }
      }

      if (!this.isStarted()) {
         this.rescheduleTransportRetryTimer();
      }

      boolean var15 = false;
      synchronized(this) {
         if (this.isCreated() && this.firstMessage != null && this.messageRetryTimer == null) {
            var15 = true;
         }
      }

      if (var15) {
         this.rescheduleMessageRetryTimer();
      }

      if (SAFDebug.SAFVerbose.isDebugEnabled() && SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         if (this.isStarted()) {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "' onCreateConversationSucceeded():" + " successfully started conversation " + this.info + ", retryDelaybase = " + this.retryDelayBase + ", retryDelayMaximum = " + this.retryDelayMaximum + ", retryDelayMultiplier = " + this.retryDelayMultiplier);
         } else {
            SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "' onCreateConversationSucceeded():" + " couldn't start conversation " + this.info);
         }
      }

   }

   private static void cancelTimer(Timer var0) {
      if (var0 != null) {
         var0.cancel();
      }

   }

   private void cancelTimeOutTimer() {
      cancelTimer(this.timeOutTimer);
      this.timeOutTimer = null;
   }

   private void rescheduleTimeoutTimer(long var1) {
      this.rescheduleTimeoutTimer(System.currentTimeMillis(), var1, false);
   }

   private void rescheduleTimeoutTimer(long var1, long var3, boolean var5) {
      synchronized(this) {
         if (var3 < 0L && var1 < this.absTTL) {
            var3 = this.absTTL;
         }

         if (var5 && var3 >= this.timeoutCurrent || var3 == Long.MAX_VALUE || var3 < 0L) {
            return;
         }

         if (this.absTTL < var3) {
            var3 = this.absTTL;
         }

         cancelTimer(this.timeOutTimer);
         this.timeOutTimer = this.timerManager.schedule(this, new Date(var3));
         this.timeoutCurrent = var3;
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': reschedule timeout timer: old  = " + this.timeoutCurrent + " new = " + var3 + " currentTime = " + var1 + " isConversationSetup = " + var5);
      }

   }

   private void cancelMessageRetryTimer() {
      cancelTimer(this.messageRetryTimer);
      this.messageRetryTimer = null;
   }

   private void rescheduleMessageRetryTimer() {
      long var1 = -1L;
      synchronized(this) {
         if (this.firstMessage == null) {
            return;
         }

         this.cancelMessageRetryTimer();
         var1 = this.firstMessage.getNextRetryDelay();
         this.messageRetryTimer = this.timerManager.schedule(this.messageRetryListener, var1);
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': reschedule message retry timer: delay = " + var1);
      }

   }

   private void rescheduleTransportRetryTimer() {
      this.rescheduleTransportRetryTimer(this.retryController.getNextRetry());
   }

   private void rescheduleTransportRetryTimer(long var1) {
      synchronized(this) {
         if (this.transportRetryTimer != null) {
            return;
         }

         if (this.isStarted()) {
            return;
         }

         this.transportRetryTimer = this.timerManager.schedule(this.transportRetryListener, var1);
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': reschedule transport retry timer: delay = " + var1);
      }

   }

   private synchronized void cancelTransportRetryTimer() {
      cancelTimer(this.transportRetryTimer);
      this.transportRetryTimer = null;
   }

   void acknowledge(long var1, long var3) throws SAFException {
      if (!this.isClosed()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': acknowledging  low=" + var1 + " high=" + var3);
         }

         this.safManager.notifyAckConversation(this.info, var1, var3);
         boolean var5 = false;
         ArrayList var6 = new ArrayList();
         MessageReference var7 = null;
         MessageReference var8 = null;
         synchronized(this) {
            var7 = this.firstMessage;
            var8 = this.firstMessage;
         }

         MessageReference var9;
         for(; var7 != null && var7.getSequenceNumber() <= var3; var7 = var9) {
            var9 = var7.getNext();
            if (var7.getSequenceNumber() >= var1) {
               if (var7.getElement() != null) {
                  var6.add(var7.getElement());
               }

               this.removeMessage(var7, false);
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==acknowledge(): removed " + var7.getSequenceNumber() + " from the conversation");
               }
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==acknowledge(): number of messages to be acknowledged = " + var6.size());
         }

         int var22;
         if ((var22 = var6.size()) > 0) {
            try {
               KernelRequest var23 = this.subQueue != null ? this.subQueue.acknowledge(var6) : null;
               if (var23 != null) {
                  var23.getResult();
               }
            } catch (KernelException var21) {
               if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFSendingAgent.debug("Sending Agent '" + this.sendingAgent.getName() + "': acknowledging to kernel: failed ");
                  var21.printStackTrace();
               }
            }
         }

         boolean var24 = false;
         boolean var10 = false;
         int var25;
         synchronized(this) {
            if (var8 != this.firstMessage) {
               var10 = true;
            }

            this.lastAckedSequenceNumber = var3;
            this.windowSize += var22;
            var25 = this.windowSize;
         }

         if (this.reader != null) {
            this.reader.incrementWindow(var22);
         }

         if (var10) {
            this.rescheduleMessageRetryTimer();
         }

         Sequence var11 = this.subQueue != null ? this.subQueue.findSequence(this.info.getConversationName()) : null;
         if (var11 != null) {
            long var12 = var11.getLastAssignedValue() + 1L;
            long var14 = 0L;
            long var16 = 0L;
            boolean var18 = false;
            if (var3 >= var12) {
               var18 = true;
               var16 = var3;
               if (var1 >= var12) {
                  var14 = var1;
               } else {
                  var14 = var12;
               }
            }

            if (var18) {
               throw new SAFInvalidAcknowledgementsException("Conversation '" + this.getName() + " got acknowledgements for " + " messages that have not been sent " + var14 + ":" + var16);
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': acknowledge(): windowSize = " + var25);
         }

         this.checkCompleted();
      }
   }

   synchronized void checkCompleted() {
      if (!this.isClosed()) {
         boolean var1 = false;
         if (this.lastAckedSequenceNumber == this.lastMsgSequenceNumber && this.isDone()) {
            var1 = true;
         }

         if (var1) {
            this.sendingAgent.getWorkManager().schedule(new WorkAdapter() {
               public void run() {
                  ConversationAssembler.this.complete();
               }
            });
         }
      }
   }

   private int getAvailableSlots() {
      return this.windowSize;
   }

   private final boolean sendOneMessage(MessageReference var1) throws SAFException {
      if (!this.isCreated()) {
         throw new SAFException("Conversation Not started");
      } else if (this.isClosed()) {
         return false;
      } else {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==sendOneMessage(): sequence number =" + var1.getSequenceNumber());
         }

         if (var1.isExpired() && !this.expireOneMessage(var1, 11, !this.transport.isGapsAllowed())) {
            return false;
         } else {
            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==sendOneMessage(): sending message:" + var1.getSequenceNumber() + " to transport");
            }

            try {
               Externalizable var2 = this.transport.send(this.info, var1.getMessage());
               if (var2 != null) {
                  Externalizable var3 = null;

                  try {
                     synchronized(this.info) {
                        var3 = this.info.getContext();
                        this.info.setContext(var2);
                     }

                     if (this.sendingAgent != null) {
                        this.sendingAgent.storeConversationInfo(this.info);
                     }
                  } catch (SAFException var9) {
                     synchronized(this.info) {
                        this.info.setContext(var3);
                     }
                  }
               }

               if (this.remoteEndpoint != null) {
                  this.remoteEndpoint.connected();
               }

               return true;
            } catch (SAFTransportException var10) {
               if (this.remoteEndpoint != null) {
                  this.remoteEndpoint.disconnected(var10);
               }

               throw var10;
            }
         }
      }
   }

   private void destroyOneMessage(MessageReference var1, int var2, Throwable var3) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' has been destroyed either because timeout or" + " destroyed by the administrator");
      }

      if (!var1.hasBeenHandled()) {
         this.rejectOneMessage(var1, var2, var3, true);
      }

      this.removeMessage(var1);
   }

   private boolean expireOneMessage(MessageReference var1, int var2, boolean var3) {
      if (this.info.getErrorHandler() != null && this.info.getErrorHandler().isAlwaysForward()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==expireOneMessage(): " + var1.getMessage().getSequenceNumber() + " has expired, but will still be forwarded.");
         }

         return true;
      } else {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': message " + var1.getMessage().getSequenceNumber() + " has expired");
         }

         if (!var1.hasBeenHandled()) {
            this.rejectOneMessage(var1, var2, (Throwable)null, false);
         }

         boolean var4 = false;
         if (!var3) {
            synchronized(this) {
               if (var1 == this.firstMessage) {
                  var4 = true;
               }
            }

            this.removeMessage(var1);
            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': firstMessage = " + this.firstMessage + " lastMessage = " + this.lastMessage + " hasSeenLastMsg " + this.hasSeenLastMsg());
               if (this.firstMessage != null) {
                  SAFDebug.SAFSendingAgent.debug("first sequence number = " + this.firstMessage.getMessage().getSequenceNumber());
               }
            }

            boolean var5 = false;
            synchronized(this) {
               if (this.hasSeenLastMsg() && this.firstMessage == this.lastMessage && this.firstMessage != null && this.firstMessage.getMessage().getPayload() == null) {
                  this.removeMessage(this.firstMessage);
                  var5 = true;
                  var4 = false;
               }
            }

            if (var5) {
               this.complete();
            }
         } else {
            synchronized(var1) {
               SAFRequest var6 = var1.getMessage();
               var6.setPayload((Externalizable)null);
            }
         }

         if (var4) {
            this.rescheduleMessageRetryTimer();
         }

         return var3;
      }
   }

   ConversationRuntimeDelegate getRuntimeDelegate() {
      return this.runtimeDelegate;
   }

   public void timerExpired(Timer var1) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': timed out");
      }

      synchronized(this) {
         this.changeState(8);
         this.needNotify = false;
      }

      try {
         this.expireAllMessages(8, (Throwable)null);
         this.destroyInternal();
      } catch (KernelException var4) {
      }

   }

   private void doRun() {
      if (!this.isClosed()) {
         synchronized(this) {
            if (this.running) {
               return;
            }

            this.running = true;
         }

         this.sendingAgent.getWorkManager().schedule(this);
      }
   }

   public final void run() {
      MessageReference var1 = null;
      synchronized(this) {
         if (this.firstMessage == null) {
            this.running = false;
            return;
         }

         var1 = this.firstMessage;
      }

      while(var1 != null && !this.isDestroyed()) {
         try {
            MessageReference var2 = var1.getNext();
            if (!this.sendOneMessage(var1)) {
               synchronized(this) {
                  ++this.windowSize;
               }
            }

            if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "' ==retry(): successfully sent " + " message: " + var1.getSequenceNumber() + " to the transport");
            }

            var1 = var2;
            this.lastSendError = null;
         } catch (SAFException var9) {
            this.disconnected(var9);
            this.handleExceptionFromSendingMessage(var9, var1);
            break;
         }
      }

      boolean var10 = false;
      synchronized(this) {
         if (this.firstMessage != null) {
            var10 = true;
         }

         this.running = false;
      }

      if (var10) {
         this.rescheduleMessageRetryTimer();
      }

   }

   private void handleExceptionFromSendingMessage(Exception var1, MessageReference var2) {
      if (this.transport instanceof SAFErrorAwareTransport) {
         SAFErrorAwareTransport var3 = (SAFErrorAwareTransport)this.transport;
         if (var3.isPermanentError(var1)) {
            this.rejectOneMessage(var2, 20, var1, true);
            this.removeMessage(var2);
            return;
         }

         this.lastSendError = var1;
      } else {
         this.lastSendError = var1;
      }

   }

   private synchronized boolean isStarted() {
      return this.isCreated() && this.reader != null && this.reader.isStarted();
   }

   private synchronized boolean isCreated() {
      return this.state != 1 && this.state != 16;
   }

   private synchronized boolean isClosed() {
      return this.sendingAgent == null || this.runtimeDelegate == null || this.remoteEndpoint == null;
   }

   private void ensureStarted() {
      boolean var1 = true;
      Object var2 = null;

      try {
         this.start();
         this.lastSendError = null;
      } catch (SAFException var6) {
         var1 = this.handleSAFExceptionFromStartingConversation(var6);
      }

      if (var1) {
         synchronized(this) {
            if (!this.isStarted()) {
               this.rescheduleTransportRetryTimer();
            } else {
               this.cancelTransportRetryTimer();
            }
         }
      }

   }

   private boolean handleSAFExceptionFromStartingConversation(Exception var1) {
      boolean var2 = true;
      if (this.transport instanceof SAFErrorAwareTransport) {
         SAFErrorAwareTransport var3 = (SAFErrorAwareTransport)this.transport;
         if (var3.isPermanentError(var1)) {
            var2 = false;

            try {
               this.expireAllMessages(20, var1);
            } catch (KernelException var5) {
            }
         } else {
            this.lastSendError = var1;
         }
      } else {
         this.lastSendError = var1;
      }

      return var2;
   }

   public String toString() {
      return this.info + "," + " retryMultiplier = " + this.retryDelayMultiplier + "," + " retryDelayBase = " + this.retryDelayBase + "," + " retryDelayMaximum = " + this.retryDelayMaximum;
   }

   Queue getSubscriptionQueue() {
      return this.subQueue;
   }

   void resumeReader() throws KernelException {
      if (!this.isClosed()) {
         this.subQueue.resume(2);
      }
   }

   void pauseReader() throws KernelException {
      if (!this.isClosed()) {
         this.subQueue.suspend(2);
      }
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ConversationAssembler");
      super.dumpAttributes(var1, var2);
      var2.writeAttribute("lastAckedSequenceNumber", String.valueOf(this.lastAckedSequenceNumber));
      var2.writeAttribute("retryDelayMultiplier", String.valueOf(this.retryDelayMultiplier));
      var2.writeAttribute("retryDelayBase", String.valueOf(this.retryDelayBase));
      var2.writeAttribute("retryDelayMaximum", String.valueOf(this.retryDelayMaximum));
      var2.writeAttribute("loggingEnabled", String.valueOf(this.loggingEnabled));
      var2.writeAttribute("windowSize", String.valueOf(this.windowSize));
      var2.writeAttribute("state", String.valueOf(this.state));
      var2.writeAttribute("needNotify", String.valueOf(this.needNotify));
      ((SAFConversationInfoImpl)this.getInfo()).dump(var1, var2);
      var2.writeEndElement();
   }

   void handleAsyncFault(String var1, Exception var2) throws SAFException {
      if (!this.isClosed()) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + this.info.getConversationName() + "': handling fault related to message =" + var1 + " Exception=" + var2.getMessage());
         }

         if (var1.equals("-1")) {
            this.handleSAFExceptionFromStartingConversation(var2);
         } else {
            MessageReference var3 = null;
            synchronized(this) {
               var3 = this.firstMessage;
            }

            while(var3 != null && !var1.equals(var3.getMessage().getMessageId())) {
               var3 = var3.getNext();
            }

            if (var3 != null) {
               this.handleExceptionFromSendingMessage(var2, var3);
            }
         }
      }
   }

   private final class MessageRetryTimerListener implements NakedTimerListener {
      private MessageRetryTimerListener() {
      }

      public void timerExpired(Timer var1) {
         synchronized(ConversationAssembler.this) {
            ConversationAssembler.this.messageRetryTimer = null;
            if (ConversationAssembler.this.isNotAvail()) {
               return;
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + ConversationAssembler.this.info.getConversationName() + "': message retry timer expired");
         }

         ConversationAssembler.this.doRun();
      }

      // $FF: synthetic method
      MessageRetryTimerListener(Object var2) {
         this();
      }
   }

   private final class TransportRetryTimerListener implements NakedTimerListener {
      private TransportRetryTimerListener() {
      }

      public void timerExpired(Timer var1) {
         synchronized(ConversationAssembler.this) {
            ConversationAssembler.this.transportRetryTimer = null;
            if (ConversationAssembler.this.isNotAvail()) {
               return;
            }
         }

         if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
            SAFDebug.SAFSendingAgent.debug("Conversation '" + ConversationAssembler.this.info.getConversationName() + "': transport retry timer expired");
         }

         ConversationAssembler.this.ensureStarted();
      }

      // $FF: synthetic method
      TransportRetryTimerListener(Object var2) {
         this();
      }
   }

   private final class MessageReader extends DeliveryList implements Listener {
      private ListenRequest queueConsumer;
      private final KernelRequest completion = new KernelRequest();

      MessageReader() {
         this.setWorkManager(ConversationAssembler.this.sendingAgent.getWorkManager());
         this.initDeliveryList(ConversationAssembler.this.windowSize, 25, 0, 0);
      }

      void start() throws SAFException {
         if (!ConversationAssembler.this.isClosed()) {
            synchronized(ConversationAssembler.this) {
               int var2 = ConversationAssembler.this.getAvailableSlots();
               if (this.queueConsumer == null && var2 != 0) {
                  try {
                     this.queueConsumer = ConversationAssembler.this.subQueue.listen((Expression)null, var2, false, ConversationAssembler.this, this, ConversationAssembler.this.getConversationName(), WorkManagerFactory.getInstance().getSystem());
                  } catch (KernelException var5) {
                     throw new SAFException("Error creating consumer on kernel queue", var5);
                  }

               }
            }
         }
      }

      void incrementWindow(int var1) {
         if (!ConversationAssembler.this.isClosed()) {
            if (var1 != 0) {
               boolean var2 = false;
               synchronized(ConversationAssembler.this) {
                  try {
                     if (this.queueConsumer != null) {
                        this.queueConsumer.incrementCount(var1);
                        if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                           SAFDebug.SAFSendingAgent.debug("Sending Agent '" + ConversationAssembler.this.sendingAgent.getName() + "': message reader for " + ConversationAssembler.this.subQueue.getName() + " has incrmeneted its window by " + var1);
                        }
                     }
                  } catch (KernelException var6) {
                     var2 = true;
                     this.queueConsumer.stopAndWait();
                     this.queueConsumer = null;
                  }
               }

               if (var2) {
                  this.negativeAckAll(new ArrayList());
               }

            }
         }
      }

      boolean isStarted() {
         synchronized(ConversationAssembler.this) {
            return this.queueConsumer != null;
         }
      }

      protected void pushMessages(List var1) {
         Iterator var2 = var1.iterator();
         ArrayList var3 = new ArrayList();
         boolean var4 = !this.isStarted();

         while(var2.hasNext()) {
            if (var4) {
               var3.add((MessageElement)var2.next());
            }

            while(!var4 && var2.hasNext()) {
               MessageElement var5 = (MessageElement)var2.next();
               MessageReference var6 = null;
               boolean var7 = false;

               try {
                  var6 = ConversationAssembler.this.addMessage(var5);
                  if (ConversationAssembler.this.sendOneMessage(var6)) {
                     synchronized(ConversationAssembler.this) {
                        if (var6 == ConversationAssembler.this.firstMessage) {
                           var7 = true;
                        }

                        ConversationAssembler.this.windowSize--;
                     }

                     if (var7) {
                        ConversationAssembler.this.rescheduleMessageRetryTimer();
                     }
                  }

                  ConversationAssembler.this.retryController.reset();
                  ConversationAssembler.this.lastSendError = null;
               } catch (SAFException var13) {
                  if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
                     SAFDebug.SAFSendingAgent.debug("Conversation '" + ConversationAssembler.this.getName() + "' failed to send message " + ((SAFRequest)var5.getMessage()).getSequenceNumber() + " because of " + var13.getMessage());
                  }

                  synchronized(ConversationAssembler.this) {
                     var4 = true;
                     if (this.queueConsumer != null) {
                        this.queueConsumer.stopAndWait();
                        this.queueConsumer = null;
                     }
                  }

                  if (ConversationAssembler.this.transport instanceof SAFErrorAwareTransport) {
                     SAFErrorAwareTransport var9 = (SAFErrorAwareTransport)ConversationAssembler.this.transport;
                     if (var9.isPermanentError(var13)) {
                        ConversationAssembler.this.rejectOneMessage(var6, 20, var13, true);
                        ConversationAssembler.this.removeMessage(var6, false);
                     } else {
                        ConversationAssembler.this.lastSendError = var13;
                        ConversationAssembler.this.removeMessage(var6, false);
                        var3.add(var5);
                     }
                  } else {
                     ConversationAssembler.this.lastSendError = var13;
                  }
               }
            }
         }

         if (var4) {
            this.negativeAckAll(var3);
         }

      }

      private void negativeAckAll(ArrayList var1) {
         var1.addAll(this.getPendingMessages());
         if (var1.size() != 0) {
            this.negativeAck(var1, 0L, this.completion);
         }

         try {
            this.completion.getResult();
         } catch (KernelException var5) {
            if (SAFDebug.SAFSendingAgent.isDebugEnabled()) {
               SAFDebug.SAFSendingAgent.debug("Conversation '" + ConversationAssembler.this.info.getConversationName() + "': error NACKing kernel messages: " + var5, var5);
            }
         }

         synchronized(ConversationAssembler.this) {
            ConversationAssembler.this.resetToLastGoodState((Throwable)null);
         }

         ConversationAssembler.this.rescheduleTransportRetryTimer();
      }

      private void negativeAck(List var1, long var2, KernelRequest var4) {
         if (!ConversationAssembler.this.isClosed()) {
            var4.reset();

            try {
               ConversationAssembler.this.subQueue.negativeAcknowledge(var1, var2, var4);
               var4.getResult();
            } catch (KernelException var6) {
               var6.printStackTrace();
            }

         }
      }

      private void stop() {
         ArrayList var1 = new ArrayList();
         synchronized(ConversationAssembler.this) {
            if (this.queueConsumer != null) {
               this.queueConsumer.stopAndWait();
               this.queueConsumer = null;
            }
         }

         var1.addAll(this.getPendingMessages());
         if (var1.size() != 0) {
            this.negativeAck(var1, 0L, this.completion);
         }

      }
   }
}
