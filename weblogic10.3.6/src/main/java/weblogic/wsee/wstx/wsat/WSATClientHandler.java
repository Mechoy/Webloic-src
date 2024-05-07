package weblogic.wsee.wstx.wsat;

import java.util.HashSet;
import java.util.Set;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.WLXid;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.wsat.security.IssuedTokenBuilder;
import weblogic.wsee.wstx.wsat.tube.WSATTubeHelper;

public class WSATClientHandler implements SOAPHandler<SOAPMessageContext> {
   private Transaction m_suspendedTransaction = null;
   Name m_mustUnderstandName;

   public Set<QName> getHeaders() {
      return new HashSet();
   }

   public boolean handleMessage(SOAPMessageContext var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logEnterClientSideHandleMessage(var1);
      }

      boolean var2 = (Boolean)var1.get("javax.xml.ws.handler.message.outbound");
      SOAPHeader var3 = null;

      try {
         SOAPMessage var4 = var1.getMessage();
         var3 = var4.getSOAPHeader();
         this.createMustUnderstandName(var4);
      } catch (SOAPException var5) {
         var5.printStackTrace();
         return false;
      }

      boolean var6 = this.doHandleMessage(var2 ? var3 : null, var2);
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logExitClientSideHandleMessage(var6, var1);
      }

      return var6;
   }

   public void createMustUnderstandName(SOAPMessage var1) throws SOAPException {
      this.m_mustUnderstandName = var1.getSOAPPart().getEnvelope().createName("mustUnderstand", "wsa", "http://schemas.xmlsoap.org/soap/envelope");
   }

   public boolean handleFault(SOAPMessageContext var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logEnterClientSideHandleFault(var1);
      }

      boolean var2 = (Boolean)var1.get("javax.xml.ws.handler.message.outbound");
      boolean var3 = true;
      if (!var2 && this.m_suspendedTransaction != null) {
         var3 = this.resume();
      }

      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logExitClientSideHandleFault(var3, var1);
      }

      return var3;
   }

   public void close(MessageContext var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logEnterClientSideClose(var1);
      }

      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logExitClientSideClose(var1);
      }

   }

   public boolean doHandleMessage(SOAPHeader var1, boolean var2) {
      Transaction var3 = this.getTransaction();
      boolean var4 = var3 != null;
      if (var2) {
         if (!var4) {
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logOutboundApplicationMessageNoTransaction();
            }

            return true;
         }

         var3.setLocalProperty("weblogic.transaction.otsTransactionExport", (Object)null);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logOutboundApplicationMessageTransactionBeforeAddingContext(var3);
         }

         this.processTransactionalRequest(var1, var3);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logOutboundApplicationMessageTransactionAfterAddingContext(var3);
         }
      } else {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logInboundApplicationMessage();
         }

         if (this.m_suspendedTransaction != null && !this.resume()) {
            return false;
         }
      }

      return true;
   }

   public Transaction getTransaction() {
      return (Transaction)this.getTransactionHelper().getTransaction();
   }

   TransactionHelper getTransactionHelper() {
      return TransactionHelper.getTransactionHelper();
   }

   private boolean resume() {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logWillResumeInClientSideHandler(this.m_suspendedTransaction, Thread.currentThread());
      }

      try {
         this.getTransactionHelper().getTransactionManager().resume(this.m_suspendedTransaction);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logResumedInClientSideHandler(this.m_suspendedTransaction, Thread.currentThread());
         }

         return true;
      } catch (InvalidTransactionException var2) {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logInvalidTransactionExceptionInClientSideHandler(var2, this.m_suspendedTransaction, Thread.currentThread());
         }

         this.getTransactionHelper().getTransactionManager().forceResume(this.m_suspendedTransaction);
         this.m_suspendedTransaction.setRollbackOnly(var2);
         return false;
      } catch (SystemException var3) {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSystemExceptionInClientSideHandler(var3, this.m_suspendedTransaction, Thread.currentThread());
         }

         this.getTransactionHelper().getTransactionManager().forceResume(this.m_suspendedTransaction);
         this.m_suspendedTransaction.setRollbackOnly(var3);
         return false;
      }
   }

   private boolean processTransactionalRequest(SOAPHeader var1, Transaction var2) {
      if (!this.suspend()) {
         return false;
      } else {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSuspendSuccessfulInClientSideHandler(var2, Thread.currentThread());
         }

         String var3 = this.getWSATTxIdForTransaction(var2);
         long var4 = var2.getTimeToLiveMillis();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logWSATInfoInClientSideHandler(var3, var4, var2, Thread.currentThread());
         }

         try {
            this.addCoordinationContext(var1, var3, var4);
            return true;
         } catch (SOAPException var7) {
            WseeWsatLogger.logSOAPExceptionCreatingCoordinatorContext(var7, var2, Thread.currentThread());
            return false;
         }
      }
   }

   void addCoordinationContext(SOAPHeader var1, String var2, long var3) throws SOAPException {
      SOAPElement var5 = var1.addChildElement("CoordinationContext", "wscoor", "http://schemas.xmlsoap.org/ws/2004/10/wscoor");
      var5.addAttribute(this.m_mustUnderstandName, "1");
      SOAPElement var6 = var5.addChildElement("Identifier", "wscoor", "http://schemas.xmlsoap.org/ws/2004/10/wscoor");
      var6.setValue("urn:uuid:" + var2);
      var5.addChildElement("Expires", "wscoor", "http://schemas.xmlsoap.org/ws/2004/10/wscoor").setValue("" + var3);
      var5.addChildElement("CoordinationType", "wscoor", "http://schemas.xmlsoap.org/ws/2004/10/wscoor").setValue("http://schemas.xmlsoap.org/ws/2004/10/wsat");
      SOAPElement var7 = var5.addChildElement("RegistrationService", "wscoor", "http://schemas.xmlsoap.org/ws/2004/10/wscoor");
      var7.addChildElement("Address", "wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing").setValue(this.getRegistrationCoordinatorAddress());
      SOAPElement var8 = var7.addChildElement("ReferenceParameters", "wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
      var8.addChildElement("txId", "wls-wsat", "http://weblogic.wsee.wstx.wsat/ws/2008/10/wsat").setValue(var2);
      if (WSATTubeHelper.isIssuedTokenEnabled()) {
         Element var9 = IssuedTokenBuilder.v12().buildFromAppliesToElement(var6);
         var1.appendChild(var1.getOwnerDocument().importNode(var9, true));
      }

   }

   String getRegistrationCoordinatorAddress() {
      return WSATHelper.getInstance().getRegistrationCoordinatorAddress();
   }

   String getWSATTxIdForTransaction(Transaction var1) {
      return TransactionIdHelper.getInstance().xid2wsatid((WLXid)var1.getXID());
   }

   private boolean suspend() {
      try {
         ClientTransactionManager var1 = this.getTransactionHelper().getTransactionManager();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logAboutToSuspendInClientSideHandler(var1, Thread.currentThread());
         }

         this.m_suspendedTransaction = (Transaction)var1.suspend();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSuspendedInClientSideHandler(this.m_suspendedTransaction, Thread.currentThread());
         }

         return true;
      } catch (SystemException var2) {
         WseeWsatLogger.logSystemExceptionDuringSuspend(var2, this.m_suspendedTransaction, Thread.currentThread());
         return false;
      }
   }
}
