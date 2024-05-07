package weblogic.wsee.wstx.wsat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.EndpointReference;
import weblogic.transaction.internal.XidImpl;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class WSATXAResource implements WSATConstants, XAResource, Serializable {
   static final long serialVersionUID = -5827137400010343968L;
   private Xid m_xid;
   static final String ACTIVE = "ACTIVE";
   private volatile String m_status;
   private Transactional.Version m_version;
   private boolean m_isRemovedFromMap;
   private transient EndpointReference m_epr;

   public WSATXAResource(EndpointReference var1, Xid var2) {
      this(Version.WSAT10, var1, var2, false);
   }

   public WSATXAResource(Transactional.Version var1, EndpointReference var2, Xid var3) {
      this(var1, var2, var3, false);
   }

   public WSATXAResource(Transactional.Version var1, EndpointReference var2, Xid var3, boolean var4) {
      this.m_status = "ACTIVE";
      this.m_isRemovedFromMap = false;
      this.m_version = var1;
      if (var2 == null) {
         throw new IllegalArgumentException("endpoint reference can't be null");
      } else {
         this.m_epr = var2;
         this.m_xid = var3;
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logWSATXAResource(this.m_epr.toString(), this.m_xid, "");
         }

         if (var4) {
            this.m_status = "Prepared";
         }

      }
   }

   WSATHelper getWSATHelper() {
      return WSATHelper.getInstance(this.m_version);
   }

   public void setStatus(String var1) {
      this.m_status = var1;
   }

   public int prepare(Xid var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logPrepare(this.m_epr.toString(), this.m_xid);
      }

      this.getWSATHelper().prepare(this.m_epr, this.m_xid, this);

      try {
         synchronized(this) {
            if (this.m_status.equals("ReadOnly")) {
               return 3;
            }

            if (this.m_status.equals("Prepared")) {
               if (WSATHelper.isDebugEnabled()) {
                  WseeWsatLogger.logPreparedBeforeWait(this.m_epr.toString(), this.m_xid);
               }

               return 0;
            }

            if (this.m_status.equals("Aborted")) {
               throw this.newFailedStateXAExceptionForMethodNameAndErrorcode("prepare", 100);
            }

            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logPrepareWaitingForReply(this.m_epr.toString(), this.m_xid);
            }

            this.wait((long)this.getWaitForReplyTimeout());
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logPrepareFinishedWaitingForReply(this.m_epr.toString(), this.m_xid);
            }
         }

         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logPrepareReceivedReplyStatus(this.m_status, this.m_epr.toString(), this.m_xid);
         }

         if (this.m_status.equals("ReadOnly")) {
            this.logSuccess("preparereadonly");
            return 3;
         } else if (this.m_status.equals("Prepared")) {
            this.logSuccess("prepareprepared");
            return 0;
         } else if (this.m_status.equals("Aborted")) {
            throw this.newFailedStateXAExceptionForMethodNameAndErrorcode("prepare", 100);
         } else {
            WseeWsatLogger.logFailedStateForPrepare(this.m_status, this.m_epr.toString(), this.m_xid);
            throw this.newFailedStateXAExceptionForMethodNameAndErrorcode("prepare", -7);
         }
      } catch (InterruptedException var5) {
         WseeWsatLogger.logInterruptedExceptionDuringPrepare(var5, this.m_epr.toString(), this.m_xid);
         XAException var3 = new XAException("InterruptedException during WS-AT XAResource prepare");
         var3.errorCode = -7;
         var3.initCause(var5);
         throw var3;
      }
   }

   private XAException newFailedStateXAExceptionForMethodNameAndErrorcode(String var1, int var2) {
      XAException var3 = new XAException("Failed state during " + var1 + " of WS-AT XAResource:" + this);
      var3.errorCode = var2;
      return var3;
   }

   protected void finalize() throws Throwable {
      super.finalize();
      if (!this.m_isRemovedFromMap) {
         this.getWSATHelper().removeDurableParticipant(this);
      }

   }

   public void commit(Xid var1, boolean var2) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logCommit(this.m_epr.toString(), this.m_xid);
      }

      this.getWSATHelper().commit(this.m_epr, this.m_xid, this);

      try {
         synchronized(this) {
            if (this.m_status.equals("Committed")) {
               if (WSATHelper.isDebugEnabled()) {
                  WseeWsatLogger.logCommitBeforeWait(this.m_epr.toString(), this.m_xid);
               }

               this.getWSATHelper().removeDurableParticipant(this);
               this.m_isRemovedFromMap = true;
               return;
            }

            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logCommitWaitingForReply(this.m_epr.toString(), this.m_xid);
            }

            this.wait((long)this.getWaitForReplyTimeout());
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logCommitFinishedWaitingForReply(this.m_epr.toString(), this.m_xid);
            }
         }

         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logCommitReceivedReplyStatus(this.m_status, this.m_epr.toString(), this.m_xid);
         }

         if (this.m_status.equals("Committed")) {
            this.logSuccess("preparecommitted");
            this.getWSATHelper().removeDurableParticipant(this);
            this.m_isRemovedFromMap = true;
         } else {
            XAException var3;
            if (this.m_status.equals("Prepared")) {
               WseeWsatLogger.logFailedStateForCommit(this.m_status, this.m_epr.toString(), this.m_xid);
               var3 = this.newFailedStateXAExceptionForMethodNameAndErrorcode("commit", -7);
               this.log("Failed state during WS-AT XAResource commit:" + this.m_status);
               throw var3;
            } else {
               WseeWsatLogger.logFailedStateForCommit(this.m_status, this.m_epr.toString(), this.m_xid);
               var3 = this.newFailedStateXAExceptionForMethodNameAndErrorcode("commit", -6);
               this.log("Failed state during WS-AT XAResource commit:" + this.m_status);
               throw var3;
            }
         }
      } catch (InterruptedException var10) {
         WseeWsatLogger.logInterruptedExceptionDuringCommit(var10, this.m_epr.toString(), this.m_xid);
         XAException var4 = new XAException("InterruptedException during WS-AT XAResource commit:" + var10);
         var4.errorCode = -7;
         var4.initCause(var10);
         throw var4;
      } finally {
         this.getWSATHelper().removeDurableParticipant(this);
      }
   }

   int getWaitForReplyTimeout() {
      return this.getWSATHelper().getWaitForReplyTimeout();
   }

   public void rollback(Xid var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logRollback(this.m_epr.toString(), this.m_xid);
      }

      this.getWSATHelper().rollback(this.m_epr, this.m_xid, this);

      try {
         synchronized(this) {
            if (this.m_status.equals("Aborted")) {
               if (WSATHelper.isDebugEnabled()) {
                  WseeWsatLogger.logRollbackBeforeWait(this.m_epr.toString(), this.m_xid);
               }

               this.getWSATHelper().removeDurableParticipant(this);
               this.m_isRemovedFromMap = true;
               return;
            }

            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logRollbackWaitingForReply(this.m_epr.toString(), this.m_xid);
            }

            this.wait((long)this.getWaitForReplyTimeout());
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logRollbackFinishedWaitingForReply(this.m_epr.toString(), this.m_xid);
            }
         }

         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logRollbackReceivedReplyStatus(this.m_status, this.m_epr.toString(), this.m_xid);
         }

         if (!this.m_status.equals("Aborted")) {
            if (this.m_status.equals("Prepared")) {
               WseeWsatLogger.logFailedStateForRollback(this.m_status, this.m_epr.toString(), this.m_xid);
               throw this.newFailedStateXAExceptionForMethodNameAndErrorcode("rollback", -7);
            }

            WseeWsatLogger.logFailedStateForRollback(this.m_status, this.m_epr.toString(), this.m_xid);
            throw this.newFailedStateXAExceptionForMethodNameAndErrorcode("rollback", -7);
         }

         this.logSuccess("rollbackaborted");
         this.getWSATHelper().removeDurableParticipant(this);
         this.m_isRemovedFromMap = true;
      } catch (InterruptedException var9) {
         WseeWsatLogger.logInterruptedExceptionDuringRollback(var9, this.m_epr.toString(), this.m_xid);
         XAException var3 = new XAException("InterruptedException during WS-AT XAResource rollback");
         var3.errorCode = -7;
         var3.initCause(var9);
         throw var3;
      } finally {
         this.getWSATHelper().removeDurableParticipant(this);
      }

   }

   public void forget(Xid var1) throws XAException {
   }

   public boolean setTransactionTimeout(int var1) throws XAException {
      return true;
   }

   public void start(Xid var1, int var2) throws XAException {
   }

   public void end(Xid var1, int var2) throws XAException {
   }

   public int getTransactionTimeout() throws XAException {
      return Integer.MAX_VALUE;
   }

   public boolean isSameRM(XAResource var1) throws XAException {
      return false;
   }

   public Xid[] recover(int var1) throws XAException {
      return new Xid[0];
   }

   public Xid getXid() {
      return this.m_xid;
   }

   public void setBranchQualifier(byte[] var1) {
      this.m_xid = new XidImpl(this.m_xid.getFormatId(), this.m_xid.getGlobalTransactionId(), var1);
   }

   public boolean equals(Object var1) {
      return var1 instanceof WSATXAResource && ((WSATXAResource)var1).getXid().equals(this.m_xid) && ((WSATXAResource)var1).m_epr.toString().equals(this.m_epr.toString());
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      this.m_epr.writeTo(new StreamResult(var2));
      byte[] var3 = var2.toByteArray();
      var1.writeInt(var3.length);
      var1.write(var3);
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      byte[] var3 = new byte[var2];
      var1.readFully(var3);
      this.m_epr = EndpointReference.readFrom(new StreamSource(new ByteArrayInputStream(var3)));
   }

   private void log(String var1) {
      WseeWsatLogger.logWSATXAResourceInfo(var1);
   }

   private void logSuccess(String var1) {
      WSATHelper.getInstance().debug("success state during " + var1 + " of WS-AT XAResource:" + this);
   }

   public String toString() {
      return "WSATXAResource: xid" + this.m_xid + " status:" + this.m_status + " epr:" + this.m_epr + " isRemovedFromMap:" + this.m_isRemovedFromMap;
   }
}
