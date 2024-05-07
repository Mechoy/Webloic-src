package weblogic.jms.backend;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.jms.JMSException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.RedeliveryParameters;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.subject.SubjectManager;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

final class BEForwardingConsumer extends BEDeliveryList implements Listener, JMSPeerGoneListener {
   private static final int DEFAULT_WINDOW_SIZE;
   private static final int DEFAULT_TRAN_TIMEOUT_SECONDS = 180;
   private static final long DEFAULT_FAILURE_DELAY_MILLIS = 18000L;
   private static final String TRAN_NAME = "weblogic.jms.backend.BEForwardingConsumer";
   static final boolean DD_FORWARDING_DEBUG;
   private String name;
   private Queue queue;
   private BackEnd backEnd;
   private JMSID id;
   private ListenRequest listenRequest;
   private DestinationImpl forwardingDest;
   private JMSDispatcher dispatcher;
   private ForwardingStatusListener statusListener;
   private TransactionManager tranManager;
   private int refCount;
   private boolean started;
   static final TimeComparator TIME_COMPARATOR;
   private byte[] signatureSecret;
   private String memberName;
   private int memberSecurityMode;
   private boolean resetDeliveryCount;
   private static final AuthenticatedSubject kernelId;

   BEForwardingConsumer(BackEnd var1, String var2, JMSID var3, Queue var4) {
      super(var1);
      this.backEnd = var1;
      this.name = var2;
      this.id = var3;
      this.queue = var4;
      this.resetDeliveryCount = true;
      this.tranManager = TxHelper.getTransactionManager();
   }

   BEForwardingConsumer(BackEnd var1, String var2, JMSID var3, Queue var4, boolean var5) {
      super(var1);
      this.backEnd = var1;
      this.name = var2;
      this.id = var3;
      this.queue = var4;
      this.resetDeliveryCount = var5;
      this.tranManager = TxHelper.getTransactionManager();
   }

   Queue getQueue() {
      return this.queue;
   }

   void setStatusListener(ForwardingStatusListener var1) {
      this.statusListener = var1;
   }

   synchronized void start(DestinationImpl var1, String var2, int var3) throws JMSException {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Starting forwarding consumer for " + var1);
      }

      this.memberName = var2;
      if (this.forwardingDest != var1) {
         this.stop();
         this.forwardingDest = var1;
      }

      RuntimeAccess var4 = ManagementService.getRuntimeAccess(kernelId);
      String var5 = var4.getDomainName();
      String var6 = var4.getServer().getCluster() != null ? var4.getServer().getCluster().getName() : null;
      if (var6 != null) {
         this.signatureSecret = JMSServerUtilities.generateSecret(var5 + var6 + var2 + this.forwardingDest.getId());
      }

      if (this.started) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Forwarding consumer already started for " + var1);
         }

      } else {
         try {
            this.dispatcher = JMSDispatcherManager.dispatcherFindOrCreate(var1.getDispatcherId());
            this.memberSecurityMode = this.dispatcher.isLocal() ? 15 : var3;
            this.dispatcher.addDispatcherPeerGoneListener(this);
         } catch (DispatcherException var10) {
            String var8;
            if (var1.isQueue()) {
               var8 = "Error contacting dispatcher for distributed queue member";
            } else {
               var8 = "Error contacting dispatcher for distributed topic member";
            }

            throw new weblogic.jms.common.JMSException(var8, var10);
         }

         try {
            this.setWorkManager(this.backEnd.getAsyncPushWorkManager());
            this.listenRequest = this.queue.listen((Expression)null, DEFAULT_WINDOW_SIZE, false, this, this, (String)null, this.backEnd.getAsyncPushWorkManager());
         } catch (KernelException var9) {
            throw new weblogic.jms.common.JMSException("Error creating consumer on kernel queue", var9);
         }

         this.started = true;
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Started forwarding to dist dest member " + JMSService.getDestinationName(this.forwardingDest) + " from " + this.queue.getName());
         }

      }
   }

   synchronized void stop() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Stopping forwarding consumer ");
      }

      if (this.dispatcher != null) {
         this.dispatcher.removeDispatcherPeerGoneListener(this);
      }

      if (this.listenRequest != null) {
         this.listenRequest.stopAndWait();
      }

      this.started = false;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Received a peer gone while DD forwarding: " + var1 + " statusListener " + this.statusListener);
      }

      this.stop();
      if (this.statusListener != null) {
         synchronized(this.statusListener) {
            synchronized(this) {
               this.statusListener.forwardingFailed(this);
            }
         }
      }

   }

   public synchronized int incrementRefCount() {
      return ++this.refCount;
   }

   public synchronized int decrementRefCount() {
      return --this.refCount;
   }

   public ID getId() {
      return this.id;
   }

   protected void pushMessages(List var1) {
      ListenRequest var2;
      synchronized(this) {
         var2 = this.listenRequest;
      }

      try {
         SecurityManager.pushSubject(kernelId, kernelId);

         try {
            this.processMessages(var1);
         } finally {
            SecurityManager.popSubject(kernelId);
         }

         try {
            var2.incrementCount(var1.size());
         } catch (KernelException var15) {
            JMSDebug.JMSDistTopic.debug("Error requesting more messages from messaging kernel", var15);
         }
      } catch (JMSException var20) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Scheduling increment of consumer window in 18000");
         }

         this.backEnd.getTimerManager().schedule(new RestartListener(var1.size()), 18000L);
      } catch (Exception var21) {
         Exception var23 = var21;

         try {
            JMSLogger.logDDForwardingError(this.name, var23.toString(), var23);
         } catch (ArrayIndexOutOfBoundsException var18) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Received an ArrayIndexOutOfBoundsException attempting in logDDForwardingError " + var18);
            }
         }

         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("UNEXPECTED Exception " + var21);
            var21.printStackTrace();
            JMSDebug.JMSDistTopic.debug("Scheduling increment of consumer window in 18000");
         }

         this.backEnd.getTimerManager().schedule(new RestartListener(var1.size()), 18000L);
      } catch (Throwable var22) {
         Throwable var3 = var22;

         try {
            JMSLogger.logDDForwardingError(this.name, var3.toString(), var3);
         } catch (ArrayIndexOutOfBoundsException var19) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Received an ArrayIndexOutOfBoundsException attempting in logDDForwardingError " + var19);
            }
         }

         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("UNEXPECTED Throwable Exception " + var22);
            var22.printStackTrace();
            JMSDebug.JMSDistTopic.debug("Scheduling increment of consumer window in 18000");
         }

         this.backEnd.getTimerManager().schedule(new RestartListener(var1.size()), 18000L);
      }

   }

   private void processMessages(List var1) throws JMSException {
      int var2 = var1.size();
      BEProducerSendRequest[] var3 = new BEProducerSendRequest[var2];
      int var4 = 0;
      if (this.queue.getComparator() == null) {
         Collections.sort(var1, TIME_COMPARATOR);
      } else {
         Collections.sort(var1, new UserComparator(this.queue.getComparator()));
      }

      for(Iterator var5 = var1.iterator(); var5.hasNext(); ++var4) {
         MessageElement var6 = (MessageElement)var5.next();
         MessageImpl var7 = (MessageImpl)var6.getMessage();
         if (this.dispatcher.isLocal()) {
            var7 = var7.cloneit();
         }

         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug(var4 + 1 + "/" + var2 + " Forwarding message: " + var7.getJMSMessageID() + " to " + this.memberName + " orig deliveryCount " + var6.getDeliveryCount() + " new deliveryCount " + var7.getDeliveryCount() + " resetDeliveryCount " + this.resetDeliveryCount);
         }

         if (!this.resetDeliveryCount && var6.getDeliveryCount() > 1) {
            var7.setDeliveryCount(var6.getDeliveryCount() - 1);
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("!!!BEForwardinConsumer.processMessage override deliveryCount " + var7.getDeliveryCount());
            }
         } else if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("!!!BEForwardingConsumer.processMessage delivery count RESET (default) " + var7.getDeliveryCount());
         }

         var3[var4] = new BEProducerSendRequest(this.forwardingDest.getId(), var7, (JMSID)null, 0L, (JMSID)null);
      }

      final BEForwardRequest var22 = new BEForwardRequest(this.forwardingDest.getId(), var3, this.signatureSecret);
      Transaction var23 = null;

      try {
         this.tranManager.begin("weblogic.jms.backend.BEForwardingConsumer", 180);
         var23 = this.tranManager.getTransaction();
         this.queue.associate(var1, (RedeliveryParameters)null);
      } catch (NotSupportedException var10) {
         this.handleTransactionFailure(var10, var1, var23);
      } catch (SystemException var11) {
         this.handleTransactionFailure(var11, var1, var23);
      } catch (KernelException var12) {
         this.handleTransactionFailure(var12, var1, var23);
      } catch (Exception var13) {
         this.handleTransactionFailure(var13, var1, var23);
      }

      try {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("About to forward DD msgs, s-mode=" + this.memberSecurityMode);
         }

         var22.setSecurityMode(this.memberSecurityMode);
         switch (this.memberSecurityMode) {
            case 11:
            case 12:
            case 13:
               try {
                  SubjectManager.getSubjectManager().getAnonymousSubject().doAs(kernelId, new PrivilegedExceptionAction() {
                     public Object run() throws JMSException, DispatcherException {
                        BEForwardingConsumer.this.dispatcher.dispatchAsync(var22);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var14) {
                  Exception var9 = var14.getException();
                  if (var9 instanceof JMSException) {
                     throw (JMSException)var9;
                  }

                  if (var9 instanceof DispatcherException) {
                     throw (DispatcherException)var9;
                  }
               }
               break;
            case 14:
               if (this.dispatcher.isLocal()) {
                  throw new weblogic.jms.common.JMSException("unexpected fwd mode 2");
               }

               this.dispatcher.dispatchAsync(var22);
               break;
            case 15:
               if (!this.dispatcher.isLocal()) {
                  throw new weblogic.jms.common.JMSException("unexpected fwd mode 1");
               }

               this.dispatcher.dispatchAsync(var22);
               break;
            default:
               throw new weblogic.jms.common.JMSException("unexpected fwd mode 3");
         }

         var22.getResult();
         var23.commit();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Successfully forwarded " + var2 + " messages to " + JMSService.getDestinationName(this.forwardingDest));
         }
      } catch (SystemException var15) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("SystemException during processMessages() " + var15);
         }

         this.handleForwardingFailure(var15, var23);
      } catch (RollbackException var16) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("RollbackException during processMessages() " + var16);
         }

         this.handleForwardingFailure(var16, var23);
      } catch (HeuristicMixedException var17) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("HeuristicMixedException during processMessages() " + var17);
         }

         this.handleForwardingFailure(var17, var23);
      } catch (HeuristicRollbackException var18) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("HeuristicRollbackException during processMessages() " + var18);
         }

         this.handleForwardingFailure(var18, var23);
      } catch (JMSException var19) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("JMSException during processMessages() " + var19);
         }

         this.handleForwardingFailure(var19, var23);
      } catch (DispatcherException var20) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("DispatcherException during processMessages() " + var20);
         }

         this.handleForwardingFailure(var20, var23);
      } catch (Exception var21) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Unexpected Exception during processMessages() " + var21);
         }

         this.handleForwardingFailure(var21, var23);
      }

   }

   private void handleTransactionFailure(Exception var1, List var2, Transaction var3) throws JMSException {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleTransactionFailure() " + var1.toString());
      }

      try {
         JMSLogger.logDDForwardingError(this.name, var1.toString(), var1);
      } catch (ArrayIndexOutOfBoundsException var21) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleTransactionFailure() Received an ArrayIndexOutOfBoundsException attempting in logDDForwardingError " + var21);
         }
      }

      if (var3 != null) {
         try {
            var3.rollback();
         } catch (SystemException var19) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Error forwarding & rollback transaction, distributed destination", var19);
            }
         } catch (IllegalStateException var20) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Error forwarding & rollback transaction, distributed destination", var20);
            }
         }
      }

      try {
         synchronized(this) {
            this.stop();
         }
      } finally {
         try {
            KernelRequest var8 = new KernelRequest();
            this.queue.negativeAcknowledge(var2, 0L, var8);
            var8.getResult();
         } catch (KernelException var17) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Error NACKing kernel messages: " + var17, var17);
            }
         }

      }

      if (this.statusListener != null) {
         this.statusListener.forwardingFailed(this);
      }

      throw new weblogic.jms.common.JMSException(var1);
   }

   private void handleForwardingFailure(Exception var1, Transaction var2) throws JMSException {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleForwardingFailure() " + var1.toString() + " tran: " + var2);
      }

      try {
         JMSLogger.logDDForwardingError(this.name, var1.toString(), var1);
      } catch (ArrayIndexOutOfBoundsException var6) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleForwardingFailure() Received an ArrayIndexOutOfBoundsException attempting in logDDForwardingError " + var6);
         }
      }

      if (var2 != null) {
         try {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleForwardingFailure() rollback transaction " + var2);
            }

            var2.rollback();
         } catch (SystemException var4) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Error forwarding & rollback transaction, distributed destination", var4);
            }
         } catch (IllegalStateException var5) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Error forwarding & rollback transaction, distributed destination", var5);
            }
         }
      }

      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("BEForwardingConsumer.handleForwardingFailure() throw JMSException for " + var1.toString());
      }

      throw new weblogic.jms.common.JMSException(var1);
   }

   static {
      int var0 = 64;
      String var1 = System.getProperty("weblogic.jms.DDWindowSize", "64");

      try {
         var0 = Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         var3.printStackTrace();
      }

      DEFAULT_WINDOW_SIZE = var0;
      String var2 = System.getProperty("weblogic.jms.DDForwardingDebug", "");
      var2 = var2.toLowerCase(Locale.ENGLISH).trim();
      DD_FORWARDING_DEBUG = var2.equals("true");
      TIME_COMPARATOR = new TimeComparator();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private final class RestartListener implements NakedTimerListener {
      private int incrementSize;

      RestartListener(int var2) {
         this.incrementSize = var2;
      }

      public void timerExpired(Timer var1) {
         synchronized(BEForwardingConsumer.this) {
            if (BEForwardingConsumer.this.started) {
               try {
                  BEForwardingConsumer.this.listenRequest.incrementCount(this.incrementSize);
               } catch (KernelException var5) {
                  JMSDebug.JMSDistTopic.debug("Error requesting more messages from messaging kernel", var5);
               }
            }

         }
      }
   }

   static final class UserComparator implements Comparator {
      private final Comparator userComparator;

      UserComparator(Comparator var1) {
         this.userComparator = var1;
      }

      public int compare(Object var1, Object var2) {
         return this.userComparator == null ? 0 : this.userComparator.compare(((MessageElement)var1).getMessage(), ((MessageElement)var2).getMessage());
      }

      public boolean equals(Object var1) {
         return var1 instanceof UserComparator;
      }

      public int hashCode() {
         return 0;
      }
   }

   static final class TimeComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         JMSMessageId var3 = ((MessageImpl)((MessageElement)var1).getMessage()).getId();
         JMSMessageId var4 = ((MessageImpl)((MessageElement)var2).getMessage()).getId();
         return var3.compareTime(var4);
      }

      public boolean equals(Object var1) {
         return var1 instanceof TimeComparator;
      }

      public int hashCode() {
         return 0;
      }
   }
}
