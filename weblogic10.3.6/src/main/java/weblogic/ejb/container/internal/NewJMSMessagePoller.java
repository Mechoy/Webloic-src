package weblogic.ejb.container.internal;

import java.security.PrivilegedExceptionAction;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public class NewJMSMessagePoller implements Runnable, TimerListener {
   private static final DebugLogger debugLogger;
   private static final int MESSAGE_RECEIVE_TIMEOUT_MILLIS = 2000;
   private static final int DEFAULT_DESTINATION_POLL_INTERVAL_MILLIS = 1000;
   private static final String DESTINATION_POLL_INTERVAL_PROPERTY = "weblogic.ejb.container.MDBDestinationPollIntervalMillis";
   static final int DESTINATION_POLL_INTERVAL_MILLIS;
   static final int LAST_SESSION_CLOSE_IDLE_THRESHOLD_MILLIS = 5000;
   private static final String TRANSACTION_NAME_PREFIX = "NewJMSMessagePoller.";
   private final int id;
   private final String mdbName;
   private final JMSConnectionPoller connectionPoller;
   private volatile MessageConsumer consumer;
   private final TransactionManager txManager;
   private final MDListener listener;
   private final String txName;
   private final JMSPollerManager pm;
   private final boolean reCreateMC;
   private final Destination dest;
   private final boolean dynamicSessionClose;
   private volatile boolean keepRunning;

   public NewJMSMessagePoller(int var1, String var2, JMSConnectionPoller var3, JMSPollerManager var4, MessageConsumer var5, MDListener var6, boolean var7, Destination var8, boolean var9) {
      this.mdbName = var2;
      this.connectionPoller = var3;
      this.pm = var4;
      this.listener = var6;
      this.consumer = var5;
      this.txManager = TxHelper.getTransactionManager();
      this.txName = "NewJMSMessagePoller." + var2;
      this.id = var1;
      this.reCreateMC = var7;
      this.dest = var8;
      this.dynamicSessionClose = var9;
   }

   public void stop() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Stopping : " + this);
      }

      this.keepRunning = false;
   }

   public void start() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Starting : " + this);
      }

      this.keepRunning = true;
   }

   public void run() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Message polling started for MDB " + this.mdbName + " poller " + this.id);
      }

      boolean var1 = false;
      JMSException var2 = null;
      int var3 = 0;
      int var4 = 0;

      while(true) {
         int var5;
         label166: {
            if (this.keepRunning) {
               var1 = false;
               var2 = null;
               var5 = this.pm.getBatchSize(this.listener.getMaxMessagesInTransaction());

               try {
                  if (this.consumer == null) {
                     assert this.listener.isDetached() : "The MDListener should be deteched when consumer is null";

                     JMSConnectionPoller.CreateSessionResult var6 = this.connectionPoller.dynamicCreateSession(this.id);
                     this.consumer = var6.consumer;
                     this.listener.attach(var6.session, var6.wrappedSession);
                  }

                  if (this.processOneMessage(var5, 2000)) {
                     if (this.listener.getExecuteException() != null) {
                        throw this.listener.getExecuteException();
                     }

                     if (this.listener.getRolledBack()) {
                        var1 = true;
                     }

                     if (this.keepRunning && this.pm.scheduleIfBusy(this)) {
                        if (debugLogger.isDebugEnabled()) {
                           Debug.say("Got scheduled as server is busy, poller : " + this);
                        }

                        return;
                     }
                     break label166;
                  }

                  if (this.pm.holdsToken(this.id) || this.pm.acquireToken(this.id)) {
                     if (DESTINATION_POLL_INTERVAL_MILLIS > 5000 && this.dynamicSessionClose) {
                        this.listener.detach();

                        try {
                           this.connectionPoller.dynamicCloseSession(this.id);
                        } catch (Throwable var9) {
                           if (debugLogger.isDebugEnabled()) {
                              Debug.say("Failed to dynamic close session:" + var9);
                           }
                        }

                        this.consumer = null;
                     }

                     this.pm.scheduleTimer(this, (long)DESTINATION_POLL_INTERVAL_MILLIS);
                     return;
                  }

                  if (this.keepRunning) {
                     if (this.dynamicSessionClose) {
                        this.listener.detach();

                        try {
                           this.connectionPoller.dynamicCloseSession(this.id);
                        } catch (Throwable var10) {
                           if (debugLogger.isDebugEnabled()) {
                              Debug.say("Failed to dynamic close session:" + var10);
                           }
                        }

                        this.consumer = null;
                     } else if (this.reCreateMC) {
                        this.consumer = this.connectionPoller.reCreateMessageConsumer(this.dest, this.id);
                     }
                  }
               } catch (JMSException var11) {
                  EJBLogger.logJMSExceptionReceivingForMDB(JMSConnectionPoller.getAllExceptionText(var11), StackTraceUtils.throwable2StackTrace(var11));
                  ++var4;
                  var2 = var11;
                  var1 = true;
                  break label166;
               } catch (SystemException var12) {
                  EJBLogger.logJMSExceptionReceivingForMDB(JMSConnectionPoller.getAllExceptionText(var12), StackTraceUtils.throwable2StackTrace(var12));
                  ++var4;
                  JMSException var7 = new JMSException(var12.toString());
                  var7.setLinkedException(var12);
                  var2 = var7;
                  var1 = true;
                  break label166;
               } catch (Throwable var13) {
                  EJBLogger.logJMSExceptionProcessingMDB(JMSConnectionPoller.getAllExceptionText(var13), StackTraceUtils.throwable2StackTrace(var13));
                  var1 = true;
                  break label166;
               }
            }

            this.pm.releaseToken(this.id);
            this.pm.returnToPool(this);
            if (debugLogger.isDebugEnabled()) {
               Debug.say("Message polling ended for MDB " + this.mdbName + " poller " + this.id);
            }

            return;
         }

         if (var1) {
            ++var3;
            this.pm.notifyError(var5);
            if (var4 >= 3 && var2 != null) {
               if (debugLogger.isDebugEnabled()) {
                  Debug.say("Too many errors, attempting re-connect");
               }

               this.connectionPoller.onException(var2);
               var4 = 0;
            } else if (JMSConnectionPoller.MAX_ERROR_COUNT > 0 && var3 >= JMSConnectionPoller.MAX_ERROR_COUNT) {
               if (debugLogger.isDebugEnabled()) {
                  Debug.say("Sleeping after error in MDB poller thread");
               }

               try {
                  Thread.sleep((long)JMSConnectionPoller.ERROR_SLEEP_TIME);
               } catch (InterruptedException var8) {
               }

               var3 = 0;
            }
         } else {
            var4 = 0;
            var3 = 0;
         }
      }
   }

   public void timerExpired(Timer var1) {
      this.run();
   }

   private boolean processOneMessage(int var1, final int var2) throws JMSException, NotSupportedException, SystemException, RollbackException {
      Message var3 = null;
      Transaction var4 = null;
      final int var5;
      if (this.listener.isTransacted()) {
         var5 = (this.listener.getTransactionTimeoutMS() + var2) / 1000;
         this.txManager.begin(this.txName, var5);
         var4 = this.txManager.getTransaction();
      }

      try {
         for(var5 = 0; var5 < var1; ++var5) {
            var3 = (Message)this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
               public Object run() throws JMSException {
                  return var5 == 0 ? NewJMSMessagePoller.this.consumer.receive((long)var2) : NewJMSMessagePoller.this.consumer.receiveNoWait();
               }
            });
            if (var3 == null) {
               if (var5 > 0) {
                  this.listener.transactionalOnMessage((Message)null, true);
                  var4 = null;
               }
               break;
            }

            if (debugLogger.isDebugEnabled()) {
               Debug.say("Got message " + var3.getJMSMessageID() + " #" + var5 + " for processing by MDB " + this.mdbName);
            }

            if (this.pm.holdsToken(this.id)) {
               this.pm.wakeUpPoller(this, (NewJMSMessagePoller)null);
            }

            if (var5 >= var1 - 1) {
               this.listener.transactionalOnMessage(var3, true);
               var4 = null;
            } else if (!this.listener.transactionalOnMessage(var3, false)) {
               var4 = null;
               break;
            }
         }
      } finally {
         if (var4 != null) {
            var4.rollback();
         }

      }

      return var3 != null;
   }

   int getId() {
      return this.id;
   }

   public String toString() {
      return "[Poller for " + this.mdbName + " with id :" + this.id + ". ]";
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
      DESTINATION_POLL_INTERVAL_MILLIS = Integer.getInteger("weblogic.ejb.container.MDBDestinationPollIntervalMillis", 1000);
   }
}
