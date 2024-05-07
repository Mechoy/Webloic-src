package weblogic.jms.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicSession;
import javax.jms.TransactionInProgressException;
import javax.naming.Context;
import javax.transaction.xa.XAResource;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.IllegalStateException;
import weblogic.kernel.KernelStatus;
import weblogic.transaction.TransactionHelper;

public final class JMSXASession extends JMSSession implements XASessionInternal, Reconnectable, Cloneable {
   private static final String TX_HELPER_CLASS = "weblogic.transaction.TxHelper";
   private final boolean originalTransacted;

   protected JMSXASession(JMSConnection var1, boolean var2, boolean var3) throws JMSException {
      super(var1, false, 2, var3);
      this.setUserTransactionsEnabled(true);
      this.originalTransacted = var2;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public boolean getTransacted() throws JMSException {
      this.checkClosed();
      return this.originalTransacted;
   }

   public void commit() throws JMSException {
      if (TransactionHelper.getTransactionHelper().getTransaction() == null) {
         throw new IllegalStateException(JMSClientExceptionLogger.logNoTransactionLoggable());
      } else {
         throw new TransactionInProgressException(JMSClientExceptionLogger.logErrorCommittingSessionLoggable().getMessage());
      }
   }

   public void rollback() throws JMSException {
      if (TransactionHelper.getTransactionHelper().getTransaction() == null) {
         throw new IllegalStateException(JMSClientExceptionLogger.logNoTransaction2Loggable());
      } else {
         throw new TransactionInProgressException(JMSClientExceptionLogger.logErrorRollingBackSessionLoggable().getMessage());
      }
   }

   public XAResource getXAResource() {
      if (!KernelStatus.isServer()) {
         throw new java.lang.IllegalStateException(JMSClientExceptionLogger.logOnlyFromServerLoggable().getMessage());
      } else {
         try {
            Class var1 = Class.forName("weblogic.transaction.TxHelper");
            Method var2 = var1.getMethod("getServerInterposedTransactionManager");
            Object var3 = var2.invoke((Object)null);
            if (var3 == null) {
               return null;
            } else {
               var2 = var3.getClass().getMethod("getXAResource");
               return (XAResource)var2.invoke(var3);
            }
         } catch (InvocationTargetException var4) {
            throw new AssertionError(var4);
         } catch (ClassNotFoundException var5) {
            throw new AssertionError(var5);
         } catch (NoSuchMethodException var6) {
            throw new AssertionError(var6);
         } catch (IllegalAccessException var7) {
            throw new AssertionError(var7);
         }
      }
   }

   public XAResource getXAResource(Context var1, String var2) {
      try {
         Class var3 = Class.forName("weblogic.transaction.TxHelper");
         Method var4 = var3.getMethod("getClientInterposedTransactionManager", Context.class, String.class);
         Object var5 = var4.invoke((Object)null, var1, var2);
         if (var5 == null) {
            return null;
         } else {
            var4 = var5.getClass().getMethod("getXAResource");
            return (XAResource)var4.invoke(var5);
         }
      } catch (InvocationTargetException var6) {
         throw new AssertionError(var6);
      } catch (ClassNotFoundException var7) {
         throw new AssertionError(var7);
      } catch (NoSuchMethodException var8) {
         throw new AssertionError(var8);
      } catch (IllegalAccessException var9) {
         throw new AssertionError(var9);
      }
   }

   public QueueSession getQueueSession() {
      return (QueueSession)this.getSession();
   }

   public TopicSession getTopicSession() {
      return (TopicSession)this.getSession();
   }

   public Session getSession() {
      return this;
   }
}
