package weblogic.ejb.container.internal;

import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.LinkedList;
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
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManager;

public class JMSMessagePoller implements Runnable {
   private static final DebugLogger debugLogger;
   private static final int DEFAULT_EMPTY_QUEUE_WAIT_SECS = 2;
   private static final int SHORT_EMPTY_QUEUE_WAIT = 250;
   private static final String MESSAGE_WAIT_TIME_PROPERTY = "weblogic.ejb.container.MDBMessageWaitTime";
   private static final String TRANSACTION_NAME_PREFIX = "JMSMessagePoller.";
   private static final int MESSAGE_WAIT_TIME;
   private final String mdbName;
   protected final JMSConnectionPoller connectionPoller;
   private MessageConsumer consumer;
   private final TransactionManager tranManager;
   private final MDListener listener;
   private final WorkManager wm;
   private final String transactionName;
   private LinkedList availableChildren;
   private LinkedList allChildren;
   private JMSMessagePoller parentPoller;
   private volatile boolean keepRunning;
   private boolean isRunning;
   private int errorCount;
   private int jmsErrorCount;
   private int childCount = 0;
   private int childNo = 0;
   private int id;
   private boolean dynamicSessionClose;

   public JMSMessagePoller(String var1, JMSConnectionPoller var2, JMSMessagePoller var3, MessageConsumer var4, MDListener var5, WorkManager var6, int var7, boolean var8) {
      if (debugLogger.isDebugEnabled()) {
         Debug.assertion(var4 != null, "A consumer object must be provided");
         Debug.assertion(var5 != null, "An MDListener object must be provided");
      }

      this.mdbName = var1;
      this.connectionPoller = var2;
      this.parentPoller = var3;
      this.listener = var5;
      this.consumer = var4;
      this.tranManager = TxHelper.getTransactionManager();
      this.wm = var6;
      this.transactionName = "JMSMessagePoller." + var1;
      this.id = var7;
      this.dynamicSessionClose = var8;
   }

   public synchronized void addChild(JMSMessagePoller var1) {
      if (this.allChildren == null) {
         this.allChildren = new LinkedList();
      }

      this.allChildren.add(var1);
      var1.childNo = ++this.childCount;
      if (this.availableChildren == null) {
         this.availableChildren = new LinkedList();
      }

      this.availableChildren.add(var1);
   }

   public synchronized void stop() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Stopping :" + this);
      }

      if (this.keepRunning) {
         this.keepRunning = false;
         if (this.allChildren != null) {
            Iterator var1 = this.allChildren.iterator();

            while(var1.hasNext()) {
               JMSMessagePoller var2 = (JMSMessagePoller)var1.next();
               var2.stop();
            }
         }

      }
   }

   public synchronized void start() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Starting :" + this);
      }

      this.keepRunning = true;
      if (this.allChildren != null) {
         Iterator var1 = this.allChildren.iterator();

         while(var1.hasNext()) {
            JMSMessagePoller var2 = (JMSMessagePoller)var1.next();
            var2.start();
         }
      }

   }

   public synchronized boolean getRunning() {
      return this.isRunning;
   }

   private synchronized void wakeUpChildPoller() {
      JMSMessagePoller var1 = (JMSMessagePoller)this.getChildFromPool();
      if (var1 != null) {
         if (debugLogger.isDebugEnabled()) {
            Debug.say("Scheduling work on :" + this.wm + " for child :" + var1);
         }

         this.wm.schedule(var1);
      }

   }

   synchronized Object getChildFromPool() {
      return this.availableChildren != null && this.availableChildren.size() > 0 ? this.availableChildren.removeFirst() : null;
   }

   private synchronized void returnChildToPool(JMSMessagePoller var1) {
      if (this.availableChildren != null) {
         this.availableChildren.addFirst(var1);
      }

   }

   boolean processOneMessage(final boolean var1, int var2) throws JMSException, NotSupportedException, SystemException, RollbackException {
      if (this.consumer == null) {
         assert this.listener.isDetached() : "The MDListener should be deteched when consumer is null";

         JMSConnectionPoller.CreateSessionResult var3 = this.connectionPoller.dynamicCreateSession(this.id);
         this.consumer = var3.consumer;
         this.listener.attach(var3.session, var3.wrappedSession);
      }

      Message var11 = null;
      Transaction var4 = null;
      final int var5;
      if (this.listener.isTransacted()) {
         var5 = this.listener.getTransactionTimeoutMS();
         if (var1) {
            var5 += MESSAGE_WAIT_TIME;
         }

         var5 /= 1000;
         this.tranManager.begin(this.transactionName, var5);
         var4 = this.tranManager.getTransaction();
      }

      try {
         for(var5 = 0; var5 < var2; ++var5) {
            var11 = (Message)this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
               public Object run() throws JMSException {
                  if (var5 == 0) {
                     return var1 ? JMSMessagePoller.this.consumer.receive((long)JMSMessagePoller.MESSAGE_WAIT_TIME) : JMSMessagePoller.this.consumer.receive(250L);
                  } else {
                     return JMSMessagePoller.this.consumer.receiveNoWait();
                  }
               }
            });
            if (var11 == null) {
               if (var5 > 0) {
                  this.listener.transactionalOnMessage((Message)null, true);
                  var4 = null;
               }
               break;
            }

            if (debugLogger.isDebugEnabled()) {
               Debug.say("Got message " + var11.getJMSMessageID() + " #" + var5 + " for processing by MDB " + this.mdbName);
            }

            if (var1) {
               this.wakeUpChildPoller();
            }

            if (var5 >= var2 - 1) {
               this.listener.transactionalOnMessage(var11, true);
               var4 = null;
            } else if (!this.listener.transactionalOnMessage(var11, false)) {
               var4 = null;
               break;
            }
         }
      } finally {
         if (var4 != null) {
            var4.rollback();
         }

      }

      return var11 != null;
   }

   public void pollContinuously() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say(this + " started polling.");
      }

      int var3 = 0;
      synchronized(this) {
         this.isRunning = true;
      }

      while(true) {
         while(true) {
            while(this.keepRunning) {
               boolean var1 = false;
               JMSException var2 = null;
               int var4;
               if (var3 > 0) {
                  var4 = 1;
                  --var3;
               } else {
                  var4 = this.listener.getMaxMessagesInTransaction();
               }

               try {
                  if (this.processOneMessage(true, var4)) {
                     if (this.listener.getExecuteException() != null) {
                        throw this.listener.getExecuteException();
                     }

                     if (this.listener.getRolledBack()) {
                        var1 = true;
                     }
                  }
               } catch (JMSException var9) {
                  EJBLogger.logJMSExceptionReceivingForMDB(JMSConnectionPoller.getAllExceptionText(var9), StackTraceUtils.throwable2StackTrace(var9));
                  ++this.jmsErrorCount;
                  var2 = var9;
                  var1 = true;
               } catch (SystemException var10) {
                  EJBLogger.logJMSExceptionReceivingForMDB(JMSConnectionPoller.getAllExceptionText(var10), StackTraceUtils.throwable2StackTrace(var10));
                  ++this.jmsErrorCount;
                  JMSException var6 = new JMSException(var10.toString());
                  var6.setLinkedException(var10);
                  var2 = var6;
                  var1 = true;
               } catch (Throwable var11) {
                  EJBLogger.logJMSExceptionProcessingMDB(JMSConnectionPoller.getAllExceptionText(var11), StackTraceUtils.throwable2StackTrace(var11));
                  var1 = true;
               }

               if (var1) {
                  ++this.errorCount;
                  if (var4 > 1) {
                     var3 = var4;
                  }

                  if (this.jmsErrorCount >= 3 && var2 != null) {
                     this.connectionPoller.onException(var2);
                     this.jmsErrorCount = 0;
                  } else if (JMSConnectionPoller.MAX_ERROR_COUNT > 0 && this.errorCount >= JMSConnectionPoller.MAX_ERROR_COUNT) {
                     if (debugLogger.isDebugEnabled()) {
                        Debug.say("Sleeping after error in MDB poller thread");
                     }

                     try {
                        Thread.sleep((long)JMSConnectionPoller.ERROR_SLEEP_TIME);
                     } catch (InterruptedException var12) {
                     }

                     this.errorCount = 0;
                  }
               } else {
                  this.errorCount = this.jmsErrorCount = 0;
               }
            }

            if (debugLogger.isDebugEnabled()) {
               Debug.say(this + " has stopped");
            }

            synchronized(this) {
               this.isRunning = false;
               this.notifyAll();
               return;
            }
         }
      }
   }

   private void pollForAWhile() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Child message polling loop started for MDB " + this.mdbName);
      }

      try {
         while(this.keepRunning) {
            if (!this.processOneMessage(false, this.listener.getMaxMessagesInTransaction())) {
               if (this.dynamicSessionClose) {
                  this.consumer = null;
                  this.listener.detach();
                  this.connectionPoller.dynamicCloseSession(this.id);
               }
               break;
            }

            if (this.listener.getExecuteException() != null) {
               throw this.listener.getExecuteException();
            }

            if (this.listener.getRolledBack()) {
               break;
            }

            if (this.wm.scheduleIfBusy(this)) {
               return;
            }
         }
      } catch (JMSException var2) {
         EJBLogger.logJMSExceptionReceivingForMDB(JMSConnectionPoller.getAllExceptionText(var2), StackTraceUtils.throwable2StackTrace(var2));
      } catch (Throwable var3) {
         EJBLogger.logJMSExceptionProcessingMDB(JMSConnectionPoller.getAllExceptionText(var3), StackTraceUtils.throwable2StackTrace(var3));
      }

      this.parentPoller.returnChildToPool(this);
      if (debugLogger.isDebugEnabled()) {
         Debug.say("Child message polling loop ended for MDB " + this.mdbName);
      }

   }

   void pollForChild() {
      this.pollForAWhile();
   }

   void pollForParent() {
      this.pollContinuously();
   }

   public void run() {
      if (this.parentPoller != null) {
         this.pollForChild();
      } else {
         this.pollForParent();
      }

   }

   String getMDBName() {
      return this.mdbName;
   }

   MessageConsumer getConsumer() {
      return this.consumer;
   }

   MDListener getMDListener() {
      return this.listener;
   }

   int getMessageWaitTime() {
      return MESSAGE_WAIT_TIME;
   }

   DebugLogger getDebugLogger() {
      return debugLogger;
   }

   int getChildCount() {
      return this.childCount;
   }

   boolean getKeepRunning() {
      return this.keepRunning;
   }

   boolean isParent() {
      return this.parentPoller == null;
   }

   JMSMessagePoller getParentPoller() {
      return this.parentPoller;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.parentPoller == null) {
         var1.append("[parent poller for " + this.mdbName + " with " + this.childCount + " children. ] ");
      } else {
         var1.append("[child poller for " + this.mdbName + " with childNo :" + this.childNo + ". ]");
      }

      return var1.toString();
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
      MESSAGE_WAIT_TIME = Integer.getInteger("weblogic.ejb.container.MDBMessageWaitTime", 2) * 1000;
   }
}
