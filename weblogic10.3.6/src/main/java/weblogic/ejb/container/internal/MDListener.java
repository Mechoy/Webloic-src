package weblogic.ejb.container.internal;

import java.security.PrivilegedExceptionAction;
import java.util.LinkedList;
import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.Context;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;
import weblogic.deployment.jms.MDBSession;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.NonDestructiveRuntimeException;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.monitoring.EJBTransactionRuntimeMBeanImpl;
import weblogic.ejb.spi.JmsMessageDrivenBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.jms.client.WLSessionImpl;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.extensions.MDBTransaction;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.transaction.RollbackException;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManager;

final class MDListener implements MessageListener, Runnable {
   protected static final DebugLogger debugLogger;
   private final JMSConnectionPoller connectionPoller;
   private final AuthenticatedSubject runAsSubject;
   private final Context m_envContext;
   private final EJBTransactionRuntimeMBeanImpl txMBean;
   private final LinkedList m_listenerList;
   private final LinkedList m_messageList;
   private final MDListener m_parent;
   private final MessageDrivenBeanInfo m_info;
   private final MessageDrivenEJBRuntimeMBean rtMBean;
   private final PoolIntf m_pool;
   private volatile MDBSession m_wrappedSession;
   private volatile Session m_session;
   private final WorkManager m_wm;
   private final TransactionManager m_tranManager;
   private final boolean m_isTransacted;
   private final int m_acknowledgeMode;
   private final int m_numChildren;
   private MDMessage m_message;
   private Throwable executeException = null;
   private Transaction m_transaction;
   private boolean rolledBack = false;
   private boolean recovered = false;
   private int m_numWaiters;
   private boolean useAcknowledgePreviousMode = false;
   private String txName;
   private boolean detached = false;
   private final boolean syncNoTranMode;

   MDListener(MDListener var1, JMSConnectionPoller var2, int var3, Context var4, Session var5, MDBSession var6, int var7, MessageDrivenEJBRuntimeMBean var8, MessageDrivenBeanInfo var9, PoolIntf var10, boolean var11, WorkManager var12, boolean var13) throws WLDeploymentException {
      this.m_info = var9;
      this.m_pool = var10;
      this.m_isTransacted = var9.isOnMessageTransacted();
      this.m_parent = var1;
      this.m_envContext = var4;
      this.m_session = var5;
      this.m_wrappedSession = var6;
      this.rtMBean = var8;
      this.txMBean = (EJBTransactionRuntimeMBeanImpl)var8.getTransactionRuntime();
      this.m_acknowledgeMode = var7;
      this.useAcknowledgePreviousMode = var11;
      this.m_tranManager = TxHelper.getTransactionManager();
      this.m_wm = var12;
      this.syncNoTranMode = var13;
      if (var5 == null && var1 == null) {
         this.detached = true;
      }

      String var14 = var9.getRunAsPrincipalName();
      this.connectionPoller = var2;
      if (var14 == null) {
         this.runAsSubject = SecurityHelper.getAnonymousUser();
      } else {
         try {
            SecurityHelper var15 = new SecurityHelper(this.m_info.getDeploymentInfo().getSecurityRealmName(), this.m_info.getJACCPolicyConfig(), this.m_info.getJACCPolicyContextId(), this.m_info.getJACCCodeSource(), this.m_info.getJACCRoleMapper());
            this.runAsSubject = var15.getSubjectForPrincipal(var14);
         } catch (Exception var16) {
            throw new WLDeploymentException(var16.toString());
         }
      }

      this.m_numChildren = var3;
      if (var3 > 0) {
         this.m_listenerList = new LinkedList();

         for(int var17 = 0; var17 < var3; ++var17) {
            this.putListener(new MDListener(this, this.connectionPoller, 0, var4, (Session)null, (MDBSession)null, var7, this.rtMBean, this.m_info, var10, var11, var12, var13));
         }
      } else {
         this.m_listenerList = null;
      }

      if (var7 == 2 && !this.m_isTransacted) {
         this.m_messageList = new LinkedList();
      } else {
         this.m_messageList = null;
      }

      this.txName = "[EJB " + this.m_info.getBeanClassName() + "." + this.m_info.getOnMessageMethodInfo().getSignature() + "]";
   }

   boolean isTransacted() {
      return this.m_isTransacted;
   }

   MDBSession getWrappedSession() {
      return this.m_parent == null ? this.m_wrappedSession : this.m_parent.m_wrappedSession;
   }

   Session getSession() {
      return this.m_parent == null ? this.m_session : this.m_parent.m_session;
   }

   boolean isDetached() {
      return this.m_parent == null ? this.detached : this.m_parent.detached;
   }

   void detach() {
      if (this.m_parent == null) {
         assert this.m_messageList == null || this.m_messageList.isEmpty() : "Cannot detach a MDListener with pending message list";

         assert this.m_message == null : "Cannot detach a MDListener with pending message";

         this.detached = true;
         this.m_wrappedSession = null;
         this.m_session = null;
      }
   }

   void attach(Session var1, MDBSession var2) {
      if (this.m_parent == null) {
         assert this.m_messageList == null || this.m_messageList.isEmpty() : "Cannot attach a MDListener with pending message list";

         assert this.m_message == null : "Cannot attach a MDListener with pending message";

         this.m_session = var1;
         this.m_wrappedSession = var2;
         this.detached = false;
      }
   }

   private MDListener getListener() {
      synchronized(this.m_listenerList) {
         while(this.m_listenerList.isEmpty()) {
            try {
               ++this.m_numWaiters;
               this.m_listenerList.wait();
            } catch (InterruptedException var9) {
            } finally {
               --this.m_numWaiters;
            }
         }

         return (MDListener)this.m_listenerList.removeFirst();
      }
   }

   private void putListener(MDListener var1) {
      if (this.m_messageList != null && var1.m_message != null) {
         MDMessage var2 = null;
         synchronized(this.m_messageList) {
            if (var1.recovered) {
               var1.m_message.setRecovered();

               for(int var4 = 0; var4 < this.m_messageList.size(); ++var4) {
                  if (((MDMessage)this.m_messageList.get(var4)).isRecovered()) {
                     var2 = (MDMessage)this.m_messageList.remove(var4);
                  }
               }
            } else {
               var1.m_message.setAcknowledged();

               while(((MDMessage)this.m_messageList.getFirst()).isAcknowledged()) {
                  var2 = (MDMessage)this.m_messageList.removeFirst();
                  if (this.m_messageList.isEmpty()) {
                     break;
                  }
               }
            }
         }

         if (var2 != null && !var1.recovered) {
            if (this.connectionPoller.isCurrentSubjectKernelIdentity()) {
               final MDMessage var3 = var2;

               try {
                  this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        var3.acknowledge();
                        return null;
                     }
                  });
               } catch (JMSException var8) {
               }
            } else {
               var2.acknowledge();
            }
         }
      }

      var1.m_message = null;
      var1.m_transaction = null;
      synchronized(this.m_listenerList) {
         this.m_listenerList.add(var1);
         if (this.m_numWaiters > 0) {
            this.m_listenerList.notify();
         }

      }
   }

   public void onMessage(final Message var1) {
      assert !this.isDetached() : "MDListener.onMessage cannot be called in detached mode";

      UserTransaction var2 = TxHelper.getUserTransaction();
      weblogic.transaction.Transaction var3 = TxHelper.getTransaction();
      Object var4 = null;

      try {
         if (this.m_isTransacted) {
            if (this.m_tranManager.getStatus() != 6) {
               this.m_tranManager.suspend();
            }

            int var5 = this.getTransactionTimeoutMS();
            if (var5 > 0) {
               var2.setTransactionTimeout(var5 / 1000);
            }

            var2.begin();
            if (var3 == null) {
               var3 = TxHelper.getTransaction();
            }

            ((weblogic.transaction.Transaction)var3).setName(this.txName);
            Integer var6 = this.m_info.getOnMessageTxIsolationLevel();
            if (var6 != null) {
               ((TransactionImpl)var3).setProperty("ISOLATION LEVEL", var6);
            }

            final Session var7 = this.getSession();
            if (var7 instanceof MDBTransaction) {
               if (!this.connectionPoller.isCurrentSubjectKernelIdentity() && !this.isRemoteDomain()) {
                  ((MDBTransaction)var7).associateTransaction(var1);
               } else {
                  this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        ((MDBTransaction)var7).associateTransaction(var1);
                        return null;
                     }
                  });
               }
            }
         }
      } catch (NotSupportedException var74) {
         var4 = var74;
      } catch (JMSException var75) {
         var4 = var75;
      } catch (SystemException var76) {
         var4 = var76;
      }

      if (var4 == null) {
         this.transactionalOnMessage(var1, true);
      } else {
         this.executeException = (Throwable)var4;

         try {
            final Session var77 = this.getSession();
            if (!this.connectionPoller.isCurrentSubjectKernelIdentity() && !this.isRemoteDomain()) {
               var77.recover();
            } else {
               this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     var77.recover();
                     return null;
                  }
               });
            }
         } catch (JMSException var72) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("[MDListener]: onMessage ", var72);
            }
         } finally {
            try {
               if (var2 != null) {
                  this.txMBean.incrementTransactionsRolledBack();
                  if (var3 != null && ((TransactionImpl)var3).isTimedOut()) {
                     this.txMBean.incrementTransactionsTimedOut();
                  }

                  var2.rollback();
               }
            } catch (SystemException var69) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("[MDListener]: onMessage ", var69);
               }
            } catch (Exception var70) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("[MDListener]: onMessage ", var70);
               }
            } finally {
               if (this.connectionPoller.isPrintErrorMessage((Throwable)var4)) {
                  EJBLogger.logerrorStartingMDBTx(((Throwable)var4).toString());
               }

            }

         }

      }
   }

   boolean transactionalOnMessage(Message var1, boolean var2) {
      if (this.m_numChildren != 0 && var2) {
         MDListener var3 = this.getListener();
         var3.m_message = new MDMessage(var1, this.getSession(), this.useAcknowledgePreviousMode);
         if (this.m_messageList != null) {
            synchronized(this.m_messageList) {
               this.m_messageList.add(var3.m_message);
            }
         }

         if (this.m_isTransacted) {
            var3.m_transaction = this.m_tranManager.forceSuspend();
         }

         this.m_wm.schedule(var3);
         return false;
      } else {
         if (var1 != null) {
            this.m_message = new MDMessage(var1, this.getSession(), this.useAcknowledgePreviousMode);
            if (this.m_messageList != null) {
               synchronized(this.m_messageList) {
                  this.m_messageList.add(this.m_message);
               }
            }
         }

         return this.execute(var2);
      }
   }

   private boolean execute(boolean var1) {
      assert !this.isDetached() : "MDListener.execute cannot be called in detached mode";

      boolean var2 = false;
      boolean var3 = false;
      this.executeException = null;
      this.rolledBack = false;
      this.recovered = false;
      if (this.m_parent != null && this.m_transaction != null) {
         this.m_tranManager.forceResume(this.m_transaction);
         this.m_transaction = null;
      }

      weblogic.transaction.Transaction var4 = TxHelper.getTransaction();
      MessageDrivenBean var5 = null;

      try {
         EJBRuntimeUtils.pushEnvironment(this.m_envContext);
         SecurityHelper.pushRunAsSubject(this.runAsSubject);
         ClassLoader var6 = this.m_info.getModuleClassLoader();
         Thread var89 = Thread.currentThread();
         ClassLoader var90 = var89.getContextClassLoader();
         var89.setContextClassLoader(var6);

         try {
            var5 = (MessageDrivenBean)this.m_pool.getBean((long)this.getTransactionTimeoutMS());
         } catch (Exception var84) {
            if (var90 != null) {
               var89.setContextClassLoader(var90);
            }

            var3 = true;
            throw var84;
         }

         try {
            MethodInvocationHelper.pushMethodObject(this.m_info);
            if (var5 instanceof JmsMessageDrivenBean) {
               this.getWrappedSession().reOpen();
               ((JmsMessageDrivenBean)var5).setSession((Session)this.getWrappedSession());
            }

            if (this.m_message != null) {
               ((MessageListener)var5).onMessage(this.m_message.getMessage());
            }
         } finally {
            MethodInvocationHelper.popMethodObject(this.m_info);
            if (var90 != null) {
               var89.setContextClassLoader(var90);
            }

            if (var5 instanceof JmsMessageDrivenBean) {
               ((JmsMessageDrivenBean)var5).setSession((Session)null);
               this.getWrappedSession().close();
            }

         }

         this.m_pool.releaseBean(var5);
         if (var4 != null) {
            switch (var4.getStatus()) {
               case 0:
                  if (var1) {
                     try {
                        var4.commit();
                        this.txMBean.incrementTransactionsCommitted();
                     } catch (Exception var83) {
                        try {
                           var4.rollback();
                        } catch (Exception var76) {
                           debugLogger.debug("[MDListener]: execute", var76);
                        } finally {
                           if (var4.getStatus() == 3) {
                              this.txMBean.incrementTransactionsCommitted();
                           } else if (var4.getStatus() == 4) {
                              this.txMBean.incrementTransactionsRolledBack();
                           }

                           if (var4.isTimedOut()) {
                              this.txMBean.incrementTransactionsTimedOut();
                           }

                           String var14 = null;
                           if (var83 instanceof RollbackException && EJBRuntimeUtils.isOptimisticLockException(((RollbackException)var83).getNested())) {
                              var14 = ((RollbackException)var83).getNested().getMessage();
                           } else {
                              var14 = StackTraceUtils.throwable2StackTrace(var83);
                           }

                           EJBLogger.logErrorDuringCommit(var4.toString(), var14);
                           this.executeException = var83;
                        }
                     }
                  } else {
                     var2 = true;
                  }
                  break;
               case 1:
               case 9:
                  this.txMBean.incrementTransactionsRolledBack();
                  if (var4.isTimedOut()) {
                     this.txMBean.incrementTransactionsTimedOut();
                  }

                  try {
                     this.rolledBack = true;
                     var4.rollback();
                  } catch (Exception var75) {
                     EJBLogger.logErrorDuringRollback(var4.toString(), StackTraceUtils.throwable2StackTrace(var75));
                     this.executeException = var75;
                  }

                  EJBLogger.logTxRolledbackInfo(this.m_info.getEJBName(), var4.toString());
               case 2:
               case 3:
               case 5:
               case 6:
               case 7:
               case 8:
               default:
                  break;
               case 4:
                  this.txMBean.incrementTransactionsRolledBack();
                  if (var4.isTimedOut()) {
                     this.txMBean.incrementTransactionsTimedOut();
                  }
            }
         } else if (this.syncNoTranMode) {
            try {
               if (this.m_message != null) {
                  this.m_message.acknowledge();
               }
            } catch (Exception var86) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("[MDListener]: execute ", var86);
               }

               try {
                  this.getSession().rollback();
               } catch (Exception var74) {
               }
            }
         }

         this.connectionPoller.initDeliveryFailureParams();
         boolean var91 = var2;
         return var91;
      } catch (Throwable var87) {
         this.executeException = var87;
         if (!var3) {
            if (var87 instanceof NonDestructiveRuntimeException) {
               this.m_pool.releaseBean(var5);
            } else {
               this.m_pool.destroyBean(var5);
            }
         }

         boolean var7 = this.connectionPoller.isPrintErrorMessage(this.executeException);
         if (var7) {
            EJBLogger.logExcepInOnMessageCallOnMDB(var87);
         }

         if (this.m_isTransacted) {
            try {
               this.rolledBack = true;
               var4.rollback();
            } catch (Exception var78) {
               if (var7) {
                  EJBLogger.logErrorOnRollback(var78);
               }
            }

            this.txMBean.incrementTransactionsRolledBack();
            if (var4.isTimedOut()) {
               this.txMBean.incrementTransactionsTimedOut();
            }

            if (var7) {
               EJBLogger.logTxRolledbackInfo(this.m_info.getEJBName(), var4.toString());
               EJBLogger.logExcepInOnMessageCallOnMDB(var87);
            }
         } else {
            try {
               if (var4 != null && var4.getStatus() != 6) {
                  try {
                     this.rolledBack = true;
                     var4.rollback();
                  } catch (Exception var80) {
                     if (var7) {
                        EJBLogger.logErrorOnRollback(var80);
                     }
                  }

                  this.txMBean.incrementTransactionsRolledBack();
                  if (var4.isTimedOut()) {
                     this.txMBean.incrementTransactionsTimedOut();
                  }

                  if (var7) {
                     EJBLogger.logTxRolledbackInfo(this.m_info.getEJBName(), var4.toString());
                  }
               }
            } catch (Exception var81) {
            }

            try {
               if (this.syncNoTranMode) {
                  this.getSession().recover();
               } else {
                  final Session var8 = this.getSession();
                  if (this.m_info.isDestinationQueue() || this.m_info.isDestinationTopic() && this.m_parent == null) {
                     if (!this.connectionPoller.isCurrentSubjectKernelIdentity() && !this.isRemoteDomain()) {
                        var8.recover();
                     } else {
                        this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
                           public Object run() throws Exception {
                              var8.recover();
                              return null;
                           }
                        });
                     }

                     this.recovered = true;
                  }
               }
            } catch (JMSException var79) {
               if (var7) {
                  EJBLogger.logExceptionRecoveringJMSSession(this.m_info.getDisplayName(), var79);
               }
            }
         }
      } finally {
         EJBRuntimeUtils.popEnvironment();
         SecurityHelper.popRunAsSubject();
         if (this.syncNoTranMode) {
            this.m_message = null;
            this.m_transaction = null;
         } else if (this.m_parent != null) {
            this.m_parent.putListener(this);
         } else {
            if (this.m_messageList != null && this.m_message != null) {
               MDMessage var17 = null;
               synchronized(this.m_messageList) {
                  if (this.recovered) {
                     this.m_message.setRecovered();

                     for(int var19 = 0; var19 < this.m_messageList.size(); ++var19) {
                        if (((MDMessage)this.m_messageList.get(var19)).isRecovered()) {
                           var17 = (MDMessage)this.m_messageList.remove(var19);
                        }
                     }
                  } else {
                     this.m_message.setAcknowledged();

                     while(((MDMessage)this.m_messageList.getFirst()).isAcknowledged()) {
                        var17 = (MDMessage)this.m_messageList.removeFirst();
                        if (this.m_messageList.isEmpty()) {
                           break;
                        }
                     }
                  }
               }

               if (var17 != null && !this.recovered) {
                  if (this.connectionPoller.isCurrentSubjectKernelIdentity()) {
                     final MDMessage var18 = var17;

                     try {
                        this.connectionPoller.doPrivilegedJMSAction(new PrivilegedExceptionAction() {
                           public Object run() throws Exception {
                              var18.acknowledge();
                              return null;
                           }
                        });
                     } catch (JMSException var73) {
                     }
                  } else {
                     var17.acknowledge();
                  }
               }
            }

            this.m_message = null;
            this.m_transaction = null;
         }

      }

      return var2;
   }

   public void run() {
      this.execute(true);
   }

   int getTransactionTimeoutMS() {
      return this.m_info.getTransactionTimeoutMS();
   }

   Throwable getExecuteException() {
      return this.executeException;
   }

   boolean getRolledBack() {
      return this.rolledBack;
   }

   int getMaxMessagesInTransaction() {
      return !this.m_isTransacted ? 1 : this.m_info.getMaxMessagesInTransaction();
   }

   private boolean isRemoteDomain() {
      boolean var1 = false;

      try {
         var1 = CrossDomainSecurityManager.getCrossDomainSecurityUtil().isRemoteDomain(this.m_info.getProviderURL());
      } catch (Exception var3) {
         var1 = true;
      }

      return var1;
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
   }

   private static final class MDMessage {
      private final Message m_message;
      private boolean m_isAcknowledged;
      private boolean m_isRecovered;
      private Session m_session;
      private final boolean m_acknowledgePreviousMode;

      MDMessage(Message var1, Session var2, boolean var3) {
         this.m_message = var1;
         this.m_session = var2;
         this.m_acknowledgePreviousMode = var3;
      }

      void setAcknowledged() {
         this.m_isAcknowledged = true;
      }

      void setRecovered() {
         this.m_isRecovered = true;
      }

      boolean isAcknowledged() {
         return this.m_isAcknowledged;
      }

      boolean isRecovered() {
         return this.m_isRecovered;
      }

      Message getMessage() {
         return this.m_message;
      }

      void acknowledge() {
         try {
            if (this.m_acknowledgePreviousMode) {
               ((WLSessionImpl)this.m_session).acknowledge(this.m_message, 2, false);
            } else {
               this.m_message.acknowledge();
            }
         } catch (JMSException var2) {
            EJBLogger.logStackTrace(var2);
         }

      }
   }
}
