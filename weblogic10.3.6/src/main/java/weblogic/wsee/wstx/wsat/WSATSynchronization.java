package weblogic.wsee.wstx.wsat;

import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import weblogic.transaction.TransactionHelper;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class WSATSynchronization implements Synchronization {
   Xid m_xid;
   String m_status;
   private static final String UNKNOWN = "UNKNOWN";
   boolean m_isRemovedFromMap;
   Transactional.Version m_version;
   EndpointReference m_epr;

   public WSATSynchronization(EndpointReference var1, Xid var2) {
      this(Version.WSAT10, var1, var2);
   }

   public WSATSynchronization(Transactional.Version var1, EndpointReference var2, Xid var3) {
      this.m_status = "UNKNOWN";
      this.m_isRemovedFromMap = false;
      this.m_version = var1;
      this.m_xid = var3;
      this.m_epr = var2;
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logWSATSynchronization(this.m_epr.toString(), this.m_xid, "");
      }

   }

   public void setStatus(String var1) {
      this.m_status = var1;
   }

   public void beforeCompletion() {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logBeforeCompletionEntered(this.m_epr.toString(), this.m_xid);
      }

      try {
         WSATHelper.getInstance().beforeCompletion(this.m_epr, this.m_xid, this);
         synchronized(this) {
            if (this.m_status.equals("Committed")) {
               if (WSATHelper.isDebugEnabled()) {
                  WseeWsatLogger.logBeforeCompletionCommittedBeforeWait(this.m_epr.toString(), this.m_xid);
               }

               return;
            }

            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logBeforeCompletionWaitingForReply(this.m_epr.toString(), this.m_xid);
            }

            this.wait((long)WSATHelper.getInstance().getWaitForReplyTimeout());
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logBeforeCompletionFinishedWaitingForReply(this.m_epr.toString(), this.m_xid);
            }
         }

         WseeWsatLogger.logBeforeCompletionReceivedReplyWithStatus(this.m_status, this.m_epr.toString(), this.m_xid);
         if (!this.m_status.equals("Committed")) {
            WseeWsatLogger.logBeforeCompletionUnexceptedStatus(this.m_status, this.m_epr.toString(), this.m_xid);
            this.setRollbackOnly();
         }
      } catch (InterruptedException var9) {
         WseeWsatLogger.logBeforeCompletionInterruptedException(var9, this.m_epr.toString(), this.m_xid);
         this.setRollbackOnly();
      } catch (Exception var10) {
         WseeWsatLogger.logBeforeCompletionException(var10, this.m_epr.toString(), this.m_xid);
         this.setRollbackOnly();
      } finally {
         WSATHelper.getInstance().removeVolatileParticipant(this.m_xid);
         this.m_isRemovedFromMap = true;
      }

   }

   private void setRollbackOnly() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      if (var1 != null) {
         try {
            var1.setRollbackOnly();
         } catch (SystemException var3) {
            WseeWsatLogger.logBeforeCompletionSystemExceptionDuringSetRollbackOnly(var3, this.m_epr.toString(), this.m_xid);
         }
      } else {
         WseeWsatLogger.logBeforeCompletionTransactionNullDuringSetRollbackOnly(this.m_epr.toString(), this.m_xid);
      }

   }

   public void afterCompletion(int var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logAfterCompletionStatus(this.m_epr.toString(), this.m_xid, "" + var1);
      }

   }

   Xid getXid() {
      return this.m_xid;
   }

   public boolean equals(Object var1) {
      return var1 instanceof WSATSynchronization && ((WSATSynchronization)var1).getXid().equals(this.m_xid);
   }

   protected void finalize() throws Throwable {
      super.finalize();
      if (!this.m_isRemovedFromMap) {
         WSATHelper.getInstance().removeVolatileParticipant(this.m_xid);
      }

   }
}
