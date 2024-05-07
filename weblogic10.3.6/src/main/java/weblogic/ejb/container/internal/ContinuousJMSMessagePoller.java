package weblogic.ejb.container.internal;

import java.security.PrivilegedExceptionAction;
import java.util.LinkedList;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerImpl;

public class ContinuousJMSMessagePoller extends JMSMessagePoller {
   private static final String TRANSACTION_NAME_PREFIX = "ContinuousJMSMessagePoller.";
   private TransactionManager tranManager = TxHelper.getTransactionManager();
   private String transactionName;
   private LinkedList supendedChildren = new LinkedList();

   public ContinuousJMSMessagePoller(String var1, JMSConnectionPoller var2, JMSMessagePoller var3, MessageConsumer var4, MDListener var5, WorkManager var6, int var7) {
      super(var1, var2, var3, var4, var5, var6, var7, false);
      this.transactionName = "ContinuousJMSMessagePoller." + var1;
   }

   boolean processOneMessage(boolean var1, int var2) throws JMSException, NotSupportedException, SystemException, RollbackException {
      Message var3 = null;
      boolean var4 = false;
      int var5 = this.getMDListener().getTransactionTimeoutMS();
      var5 += this.getMessageWaitTime();
      var5 /= 1000;
      this.tranManager.begin(this.transactionName, var5);
      Transaction var6 = this.tranManager.getTransaction();

      try {
         for(int var11 = 0; var11 < var2; ++var11) {
            var3 = (Message)this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return ContinuousJMSMessagePoller.this.getConsumer().receive((long)ContinuousJMSMessagePoller.this.getMessageWaitTime());
               }
            });
            if (var3 == null) {
               if (var11 > 0) {
                  this.getMDListener().transactionalOnMessage((Message)null, true);
                  var6 = null;
               }
               break;
            }

            if (this.getDebugLogger().isDebugEnabled()) {
               Debug.say(this + " batched message #" + var11 + ". Message is " + var3.getJMSMessageID());
            }

            if (this.isParent()) {
               this.wakeUpChildContinuousPoller();
            }

            if (var11 >= var2 - 1) {
               this.getMDListener().transactionalOnMessage(var3, true);
               var6 = null;
            } else if (!this.getMDListener().transactionalOnMessage(var3, false)) {
               var6 = null;
               break;
            }
         }
      } finally {
         if (var6 != null) {
            var6.rollback();
         }

      }

      return var3 != null;
   }

   private void wakeUpChildContinuousPoller() {
      ContinuousJMSMessagePoller var1 = (ContinuousJMSMessagePoller)this.getChildFromPool();
      if (var1 != null) {
         if (this.getDebugLogger().isDebugEnabled()) {
            Debug.say("Using daemon thread for child :" + var1);
         }

         WorkManagerImpl.executeDaemonTask("ContinuousJMSMessagePoller :" + this.getMDBName(), 5, var1);
      }

   }

   void pollForChild() {
      this.pollContinuously();
      ContinuousJMSMessagePoller var1 = (ContinuousJMSMessagePoller)this.getParentPoller();
      if (this.getDebugLogger().isDebugEnabled()) {
         Debug.assertion(var1 != null);
         Debug.say("Adding :" + this + " to suspended list");
      }

      var1.addToSuspendedList(this);
   }

   void pollForParent() {
      this.wakeupSuspendedChildren();
      this.pollContinuously();
   }

   private synchronized void wakeupSuspendedChildren() {
      ContinuousJMSMessagePoller var1;
      for(; this.supendedChildren.size() > 0; WorkManagerImpl.executeDaemonTask("ContinuousJMSMessagePoller :" + this.getMDBName(), 5, var1)) {
         var1 = (ContinuousJMSMessagePoller)this.supendedChildren.removeFirst();
         if (this.getDebugLogger().isDebugEnabled()) {
            Debug.assertion(var1 != null);
            Debug.say("Waking up the suspended child :" + var1);
         }
      }

   }

   private synchronized void addToSuspendedList(ContinuousJMSMessagePoller var1) {
      this.supendedChildren.addLast(var1);
   }

   public String toString() {
      return "[Continuous] " + super.toString();
   }
}
