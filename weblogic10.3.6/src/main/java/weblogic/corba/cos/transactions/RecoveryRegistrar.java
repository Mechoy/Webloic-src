package weblogic.corba.cos.transactions;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CosTransactions.NotPrepared;
import org.omg.CosTransactions.Status;
import weblogic.iiop.IIOPLogger;
import weblogic.t3.srvr.T3Srvr;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class RecoveryRegistrar extends WorkAdapter {
   private static final boolean DEBUG = false;
   private ResourceImpl res;
   private static final int MAX_RETRIES = 5;
   private int retries = 0;

   RecoveryRegistrar(ResourceImpl var1) {
      this.res = var1;
   }

   public void run() {
      if (T3Srvr.getT3Srvr().getRunState() != 2) {
         WorkManagerFactory.getInstance().getSystem().schedule(this);
      } else {
         synchronized(this.res) {
            ResourceImpl.ResourceActivationID var2 = (ResourceImpl.ResourceActivationID)this.res.getActivationID();
            if (OTSHelper.isDebugEnabled()) {
               IIOPLogger.logDebugOTS("recovering tx: " + var2.getXid());
            }

            Transaction var3 = null;

            try {
               var3 = (Transaction)TxHelper.getTransactionManager().getTransaction(var2.getXid());
               if (var3 == null) {
                  ResourceImpl.releaseResource(this.res);
               } else {
                  switch (var3.getStatus()) {
                     case 3:
                     case 8:
                        ResourceImpl.releaseLogRecord(this.res);
                        var3.setLocalProperty("weblogic.transaction.otsReplayCompletionExecuteRequest", (Object)null);
                        return;
                     case 6:
                        ResourceImpl.releaseResource(this.res);
                        var3.setLocalProperty("weblogic.transaction.otsReplayCompletionExecuteRequest", (Object)null);
                        return;
                     default:
                        Status var4 = var2.getRecoveryCoordinator().replay_completion(this.res);
                        if (OTSHelper.isDebugEnabled()) {
                           IIOPLogger.logDebugOTS("tx: " + var2.getXid() + " status is: " + var4.value());
                        }

                        switch (var4.value()) {
                           case 2:
                           case 7:
                              return;
                           case 3:
                           case 8:
                              getTMResource().commit(var2.getXid(), false);
                              ResourceImpl.releaseResource(this.res);
                              return;
                           case 4:
                           case 5:
                           case 6:
                           default:
                              try {
                                 getTMResource().rollback(var2.getXid());
                              } catch (XAException var35) {
                                 if (var35.errorCode != -4) {
                                    throw var35;
                                 }
                              } finally {
                                 ResourceImpl.releaseResource(this.res);
                              }
                        }
                  }
               }
            } catch (NotPrepared var37) {
               ResourceImpl.releaseResource(this.res);
            } catch (COMM_FAILURE var38) {
               if (OTSHelper.isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("couldn't contact coordinator, retrying");
               }

               var3.setLocalProperty("weblogic.transaction.otsReplayCompletionExecuteRequest", this);
               var3.setLocalProperty("weblogic.transaction.otsLogRecord", this.res);
            } catch (OBJECT_NOT_EXIST var39) {
               if (OTSHelper.isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("coordinator hasn't exported the RecoveryCoordinator, retrying");
               }

               var3.setLocalProperty("weblogic.transaction.otsReplayCompletionExecuteRequest", this);
               var3.setLocalProperty("weblogic.transaction.otsLogRecord", this.res);

               try {
                  try {
                     getTMResource().rollback(var2.getXid());
                  } catch (XAException var33) {
                     if (var33.errorCode != -4) {
                        IIOPLogger.logOTSError("rollback of " + this.res + " failed", (Throwable)null);

                        try {
                           ResourceImpl.releaseResource(this.res);
                        } catch (RuntimeException var32) {
                        }

                        return;
                     }
                  }

               } finally {
                  ResourceImpl.releaseResource(this.res);
               }
            } catch (Exception var40) {
               IIOPLogger.logOTSError("recovery of " + this.res + " failed", var40);

               try {
                  ResourceImpl.releaseResource(this.res);
               } catch (RuntimeException var31) {
               }

            }
         }
      }
   }

   private static final XAResource getTMResource() {
      return TxHelper.getServerInterposedTransactionManager().getXAResource();
   }

   private static final void p(String var0) {
      System.err.println("<RecoveryRegistrar> " + var0);
   }
}
