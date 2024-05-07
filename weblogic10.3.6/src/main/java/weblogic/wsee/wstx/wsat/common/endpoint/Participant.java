package weblogic.wsee.wstx.wsat.common.endpoint;

import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import weblogic.transaction.WLXid;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.TransactionServices;
import weblogic.wsee.wstx.wsat.WSATException;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.ParticipantIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;

public class Participant<T> implements ParticipantIF<T> {
   private WebServiceContext m_context;
   private WSATVersion<T> m_version;

   public Participant(WebServiceContext var1, WSATVersion<T> var2) {
      this.m_context = var1;
      this.m_version = var2;
   }

   public void prepare(T var1) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("prepare enter:" + var1);
      }

      CoordinatorIF var2 = null;
      byte[] var3 = null;

      try {
         var3 = this.getWSATTid();
         var2 = this.getCoordinatorPortType();
         String var4 = this.getTransactionaService().prepare(var3);
         if (WSATHelper.isDebugEnabled()) {
            this.debug("preparedOperation complete vote:" + var4 + " for tid:" + this.stringForTidByteArray(var3));
         }

         if (var4.equals("ReadOnly")) {
            var2.readOnlyOperation(this.createNotification());
         } else if (var4.equals("Prepared")) {
            var2.preparedOperation(this.createNotification());
         }
      } catch (Exception var5) {
         this.log("prepare resulted in exception, sending aborted for tid:" + this.stringForTidByteArray(var3) + " " + var5);
         if (var2 == null) {
            this.log("prepare resulted in exception, unable to send abort as coordinatorPort was nullfor tid:" + this.stringForTidByteArray(var3) + " " + var5);
            throw new WebServiceException("coordinator port null during prepare");
         }

         var2.abortedOperation(this.createNotification());
      }

      if (WSATHelper.isDebugEnabled()) {
         this.debug("prepare exit:" + var1 + " for tid:" + this.stringForTidByteArray(var3));
      }

   }

   public void commit(T var1) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("commit enter:" + var1);
      }

      CoordinatorIF var2 = null;
      boolean var3 = false;
      byte[] var4 = null;
      Object var5 = null;

      try {
         var4 = this.getWSATTid();
         var2 = this.getCoordinatorPortTypeForReplyTo();
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.commit coordinatorPort:" + var2 + " for tid:" + this.stringForTidByteArray(var4));
         }

         if (this.isInForeignContextMap()) {
            this.getTransactionaService().commit(var4);
         }

         var3 = true;
         if (WSATHelper.isDebugEnabled()) {
            this.debug("committedOperation complete for tid:" + this.stringForTidByteArray(var4));
         }
      } catch (WSATException var7) {
         var5 = var7;
         this.log("WSATException during commit for tid:" + this.stringForTidByteArray(var4) + " " + var7);
      } catch (IllegalArgumentException var8) {
         var5 = var8;
         this.log("IllegalArgumentException during commit for tid:" + this.stringForTidByteArray(var4) + " " + var8);
      }

      if (var2 == null) {
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.commit coordinatorPort null, about to create from replyto for tid:" + this.stringForTidByteArray(var4) + " ");
         }

         var2 = this.getCoordinatorPortType();
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.commit coordinatorPort null attempting to create from replyto coordinatorPort:" + var2 + "for tid:" + this.stringForTidByteArray(var4));
         }

         if (var2 == null) {
            throw new WebServiceException("WS-AT coordinator port null during commit for transaction id:" + this.stringForTidByteArray(var4));
         }

         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.commit coordinatorPort obtained from replyto:" + var2 + "for tid:" + this.stringForTidByteArray(var4));
         }
      }

      if (!var3 && WSATHelper.isDebugEnabled()) {
         this.debug("Participant.commit was not successful, presuming previous completion occurred and sending committed for tid:" + this.stringForTidByteArray(var4) + " Exception:" + var5);
      }

      var2.committedOperation(this.createNotification());
      if (WSATHelper.isDebugEnabled()) {
         this.debug("committed reply sent, local commit success is " + var3 + " , coordinatorPort:" + var2 + " for tid:" + this.stringForTidByteArray(var4));
      }

      if (WSATHelper.isDebugEnabled()) {
         this.debug("commit exit:" + var1 + " for tid:" + this.stringForTidByteArray(var4));
      }

   }

   public void rollback(T var1) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("rollback parameters:" + var1);
      }

      CoordinatorIF var2 = null;
      byte[] var3 = null;

      try {
         var3 = this.getWSATTid();
         var2 = this.getCoordinatorPortTypeForReplyTo();
         if (this.isInForeignContextMap()) {
            this.getTransactionaService().rollback(var3);
         }

         if (WSATHelper.isDebugEnabled()) {
            this.debug("rollback abortedOperation complete for tid:" + this.stringForTidByteArray(var3));
         }
      } catch (IllegalArgumentException var5) {
         this.log("rollback IllegalArgumentException for tid:" + this.stringForTidByteArray(var3) + " " + var5);
      } catch (WSATException var6) {
         this.log("rollback WSATException for tid:" + this.stringForTidByteArray(var3) + " " + var6);
         throw new WebServiceException("Participant.rollback WSATException for tid:" + this.stringForTidByteArray(var3) + " " + var6);
      }

      if (var2 != null) {
         var2.abortedOperation(this.createNotification());
      } else {
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.rollback coordinatorPort null attempting to create from replyto for tid:" + this.stringForTidByteArray(var3));
         }

         var2 = this.getCoordinatorPortType();
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Participant.rollback coordinatorPort null attempting to create from replyto for tid:" + this.stringForTidByteArray(var3) + " coordinatorPort:" + var2);
         }

         if (var2 == null) {
            this.log("Coordinator port null during rollback for tid:" + this.stringForTidByteArray(var3) + " about to throw exception/fault.");
            throw new WebServiceException("WS-AT Coordinator port null during rollback for tid:" + this.stringForTidByteArray(var3));
         }

         var2.abortedOperation(this.createNotification());
      }

      if (WSATHelper.isDebugEnabled()) {
         this.debug("rollback exit:" + var1 + " for tid:" + this.stringForTidByteArray(var3));
      }

   }

   CoordinatorIF<T> getCoordinatorPortTypeForReplyTo() {
      HeaderList var1 = (HeaderList)this.m_context.getMessageContext().get("com.sun.xml.ws.api.message.HeaderList");
      AddressingVersion var2 = this.m_version.getAddressingVersion();
      WSEndpointReference var3 = var1.getReplyTo(var2, this.m_version.getSOPAVersion());
      if (var3 != null && !var3.isNone() && var3.isAnonymous()) {
         Header var4 = var1.get(var2.fromTag, true);
         if (var4 != null) {
            try {
               var3 = var4.readAsEPR(var2);
            } catch (XMLStreamException var7) {
               this.log("XMLStreamException while reading ReplyTo EndpointReference:" + var7);
            }
         }
      }

      if (var3 != null && !var3.isNone() && !var3.isAnonymous()) {
         EndpointReference var8 = var3.toSpec();
         CoordinatorProxyBuilder var5 = (CoordinatorProxyBuilder)this.m_version.newCoordinatorProxyBuilder().to(var8);
         CoordinatorIF var6 = var5.build();
         if (WSATHelper.isDebugEnabled()) {
            this.debug("getCoordinatorPortType replytocoordinatorPort:" + var6 + "for wsReplyTo/from:" + var3 + " and replyTo/from:" + var8);
         }

         return var6;
      } else {
         return null;
      }
   }

   TransactionServices getTransactionaService() {
      return WSATHelper.getTransactionServices();
   }

   byte[] getWSATTid() {
      byte[] var1 = this.getWSATHelper().getWSATTidFromWebServiceContextHeaderList(this.m_context).replace("urn:", "").replace("uuid:", "").getBytes();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("getWSATTid tid:" + this.stringForTidByteArray(var1));
      }

      return var1;
   }

   CoordinatorIF<T> getCoordinatorPortType() {
      String var1 = this.getWSATHelper().getWSATTidFromWebServiceContextHeaderList(this.m_context);
      WLXid var2 = TransactionIdHelper.getInstance().wsatid2xid(var1);
      EndpointReference var3 = this.getTransactionaService().getParentReference(var2);
      CoordinatorProxyBuilder var4 = (CoordinatorProxyBuilder)this.m_version.newCoordinatorProxyBuilder().to(var3);
      CoordinatorIF var5 = var4.build();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("getCoordinatorPortType coordinatorPort:" + var5 + "for txid:" + var1 + " xid:" + var2 + " parentRef:" + var3);
      }

      return var5;
   }

   boolean isInForeignContextMap() {
      try {
         String var1 = this.getWSATHelper().getWSATTidFromWebServiceContextHeaderList(this.m_context);
         WLXid var2 = TransactionIdHelper.getInstance().wsatid2xid(var1);
         this.getTransactionaService().getParentReference(var2);
         return true;
      } catch (Throwable var3) {
         return false;
      }
   }

   private String stringForTidByteArray(byte[] var1) {
      return var1 == null ? null : new String(var1);
   }

   protected T createNotification() {
      return this.m_version.newNotificationBuilder().build();
   }

   protected WSATHelper getWSATHelper() {
      return this.m_version.getWSATHelper();
   }

   private void log(String var1) {
      WseeWsatLogger.logWSATParticipant(var1);
   }

   private void debug(String var1) {
      WSATHelper.getInstance().debug("Participant:" + var1);
   }
}
