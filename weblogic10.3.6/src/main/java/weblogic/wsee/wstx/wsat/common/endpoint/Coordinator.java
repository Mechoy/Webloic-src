package weblogic.wsee.wstx.wsat.common.endpoint;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import javax.annotation.Resource;
import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import weblogic.transaction.WLXid;
import weblogic.transaction.internal.XidImpl;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.TransactionServices;
import weblogic.wsee.wstx.wsat.WSATException;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.WSATXAResource;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;

public class Coordinator<T> implements CoordinatorIF<T> {
   @Resource
   private WebServiceContext m_context;
   private WSATVersion<T> m_version;

   public Coordinator(WebServiceContext var1, WSATVersion<T> var2) {
      this.m_context = var1;
      this.m_version = var2;
   }

   public void preparedOperation(T var1) {
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logPreparedOperationEntered(var1);
      }

      Xid var2 = this.getXid();
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logPreparedOperation(var2);
      }

      if (!this.getWSATHelper().setDurableParticipantStatus(var2, "Prepared")) {
         this.replayOperation(var1);
      }

      if (this.isDebugEnabled()) {
         WseeWsatLogger.logPreparedOperationExited(var1);
      }

   }

   public void abortedOperation(T var1) {
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logAbortedOperationEntered(var1);
      }

      Xid var2 = this.getXid();
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logAbortedOperation(var2);
      }

      this.getWSATHelper().setDurableParticipantStatus(var2, "Aborted");
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logAbortedOperationExited(var1);
      }

   }

   public void readOnlyOperation(T var1) {
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logReadOnlyOperationEntered(var1);
      }

      Xid var2 = this.getXid();
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logReadOnlyOperation(var2);
      }

      this.getWSATHelper().setDurableParticipantStatus(var2, "ReadOnly");
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logReadOnlyOperationExited(var1);
      }

   }

   public void committedOperation(T var1) {
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logCommittedOperationEntered(var1);
      }

      Xid var2 = this.getXid();
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logCommittedOperation(var2);
      }

      this.getWSATHelper().setDurableParticipantStatus(var2, "Committed");
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logCommittedOperationExited(var1);
      }

   }

   public void replayOperation(T var1) {
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logReplayOperationEntered(var1);
      }

      Xid var2 = this.getXid();
      String var3 = this.getWSATHelper().getWSATTidFromWebServiceContextHeaderList(this.m_context);
      if (this.isDebugEnabled()) {
         WseeWsatLogger.logReplayOperation(var2);
      }

      try {
         this.getTransactionServices().replayCompletion(var3, this.createWSATXAResourceForXidFromReplyTo(var2));
      } catch (WSATException var5) {
         if (this.isDebugEnabled()) {
            WseeWsatLogger.logReplayOperationSOAPException(var2, var5);
         }
      }

      if (this.isDebugEnabled()) {
         WseeWsatLogger.logAbortedOperationExited(var1);
      }

   }

   protected TransactionServices getTransactionServices() {
      return WSATHelper.getTransactionServices();
   }

   WSATXAResource createWSATXAResourceForXidFromReplyTo(Xid var1) {
      HeaderList var2 = (HeaderList)this.m_context.getMessageContext().get("com.sun.xml.ws.api.message.HeaderList");
      WSEndpointReference var3 = var2.getReplyTo(AddressingVersion.W3C, SOAPVersion.SOAP_12);
      EndpointReference var4 = var3.toSpec();
      return new WSATXAResource(this.m_version.getVersion(), var4, var1, true);
   }

   Xid getXid() {
      WLXid var1 = this.getWSATHelper().getXidFromWebServiceContextHeaderList(this.m_context);
      String var2 = this.getWSATHelper().getBQualFromWebServiceContextHeaderList(this.m_context);
      return new XidImpl(var1.getFormatId(), var1.getGlobalTransactionId(), var2.getBytes());
   }

   boolean isDebugEnabled() {
      return WSATHelper.isDebugEnabled();
   }

   protected WSATHelper getWSATHelper() {
      return this.m_version.getWSATHelper();
   }
}
