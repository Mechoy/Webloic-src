package weblogic.tgiop;

import com.bea.core.jatmi.common.ntrace;
import java.security.AccessController;
import weblogic.iiop.Connection;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.EndPointImpl;
import weblogic.iiop.ReplyMessage;
import weblogic.iiop.RequestMessage;
import weblogic.iiop.SequencedRequestMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.SyncKeyTable;
import weblogic.wtc.WTCLogger;

public final class TGIOPEndPointImpl extends EndPointImpl {
   private AuthenticatedSubject user;
   private static final SyncKeyTable pendingResponses = new SyncKeyTable();
   private static int nextRequestID = 1;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.tgiop.security");

   protected boolean isSecure(Connection var1) {
      return false;
   }

   public boolean supportsForwarding() {
      return false;
   }

   public int getNextRequestID() {
      return getNextInternalRequestID();
   }

   private static synchronized int getNextInternalRequestID() {
      return ++nextRequestID;
   }

   public SequencedRequestMessage getPendingResponse(int var1) {
      return (SequencedRequestMessage)pendingResponses.get(var1);
   }

   public SequencedRequestMessage removePendingResponse(int var1) {
      boolean var2 = ntrace.isTraceEnabled(65000);
      if (var2) {
         ntrace.doTrace("[/TGIOPEndpoint/removePendingResponse requestid = " + var1);
      }

      super.removePendingResponse(var1);
      SequencedRequestMessage var3 = (SequencedRequestMessage)pendingResponses.remove(var1);
      if (var2) {
         ntrace.doTrace("]/TGIOPEndpoint/removePendingResponse returning msg = " + var3);
      }

      return var3;
   }

   public void registerPendingResponse(SequencedRequestMessage var1) {
      boolean var2 = ntrace.isTraceEnabled(65000);
      if (var2) {
         ntrace.doTrace("[/TGIOPEndpoint/registgerPendingResponse requestID = " + var1.getRequestID());
      }

      super.registerPendingResponse(var1);
      pendingResponses.put(var1);
      if (var2) {
         ntrace.doTrace("]/TGIOPEndpoint/registerPendingResponse");
      }

   }

   public TGIOPEndPointImpl(Connection var1, ConnectionManager var2, AuthenticatedSubject var3) {
      super(var1, var2);
      this.user = var3;
   }

   public AuthenticatedSubject getSubject(RequestMessage var1) {
      if (this.user == null) {
         this.user = SecurityServiceManager.getCurrentSubject(kernelId);
      }

      return this.user;
   }

   public void setSubject(RequestMessage var1, AuthenticatedSubject var2) {
      if (debugSecurity.isEnabled()) {
         WTCLogger.logDebugSecurity("outbound request user set to: " + var2);
      }

   }

   public Object getInboundRequestTxContext(RequestMessage var1) {
      Object var2 = var1.getCachedTxContext();
      if (var2 == null) {
         var2 = TxHelper.getTransaction();
         var1.setCachedTxContext(var2);
      }

      return var2;
   }

   public Object getInboundResponseTxContext(ReplyMessage var1) {
      return TxHelper.getTransaction();
   }

   public void setOutboundResponseTxContext(ReplyMessage var1, Object var2) {
   }

   public void setOutboundRequestTxContext(RequestMessage var1, Object var2) {
   }
}
