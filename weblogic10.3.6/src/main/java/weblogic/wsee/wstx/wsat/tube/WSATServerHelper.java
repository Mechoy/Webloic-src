package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.message.HeaderList;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.WLXid;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.internal.ForeignRecoveryContext;
import weblogic.wsee.wstx.internal.ForeignRecoveryContextManager;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.WSATException;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.CoordinationContextBuilder;
import weblogic.wsee.wstx.wsc.common.RegistrationIF;
import weblogic.wsee.wstx.wsc.common.WSCBuilderFactory;
import weblogic.wsee.wstx.wsc.common.client.RegistrationMessageBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationProxyBuilder;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;

public class WSATServerHelper implements WSATServer {
   public void doHandleRequest(HeaderList var1, TransactionalAttribute var2) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("processRequest HeaderList:" + var1 + " TransactionalAttribute:" + var2 + " isEnabled:" + var2.isEnabled());
      }

      if (var2.isEnabled()) {
         CoordinationContextBuilder var3 = CoordinationContextBuilder.headers(var1, var2.getVersion());
         if (var3 != null) {
            this.processIncomingTransaction(var1, var3);
         } else if (var2.isRequired()) {
            throw new WebServiceException("transaction context is required to be inflowed");
         }
      }

   }

   public void doHandleResponse(TransactionalAttribute var1) {
      if (var1 != null && var1.isEnabled()) {
         if (WSATHelper.isDebugEnabled()) {
            this.debug("processResponse isTransactionalAnnotationPresent about to suspend");
         }

         Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();
         if (WSATHelper.isDebugEnabled()) {
            this.debug("processResponse suspend was successful tx:" + var2);
         }
      }

   }

   public void doHandleException(Throwable var1) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("processException about to suspend if transaction is present due to:" + var1);
      }

      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("processException suspend was successful tx:" + var2);
      }

   }

   private void processIncomingTransaction(HeaderList var1, CoordinationContextBuilder var2) {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("in processingIncomingTransaction");
      }

      CoordinationContextIF var3 = var2.buildFromHeader();
      long var4 = var3.getExpires().getValue();
      String var6 = var3.getIdentifier().getValue().replace("urn:", "").replaceAll("uuid:", "");
      boolean var7 = TransactionIdHelper.getInstance().getXid(var6.getBytes()) == null;

      try {
         Xid var8 = WSATHelper.getTransactionServices().importTransaction((int)var4, var6.getBytes());
         if (var7) {
            this.register(var1, var2, var3, var8, var4);
         }

      } catch (WSATException var9) {
         throw new WebServiceException(var9);
      }
   }

   private void register(HeaderList var1, CoordinationContextBuilder var2, CoordinationContextIF var3, Xid var4, long var5) {
      String var7 = TransactionIdHelper.getInstance().xid2wsatid((WLXid)var4);
      Transactional.Version var8 = var2.getVersion();
      WSCBuilderFactory var9 = WSCBuilderFactory.newInstance(var8);
      RegistrationMessageBuilder var10 = var9.newWSATRegistrationRequestBuilder();
      BaseRegisterType var11 = var10.durable(true).txId(var7).routing().build();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("About to suspend tx before registerOperation call");
      }

      Transaction var12 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("Suspend was successful for tx:" + var12);
      }

      RegistrationProxyBuilder var13 = var9.newRegistrationProxyBuilder();
      var13.feature(WSATTubeHelper.getPolicyFeature(var9)).credentialProvider(WSATTubeHelper.getCredentialProvider(var1)).to(var3.getRegistrationService()).txIdForReference(var7).timeout(var5);
      RegistrationIF var14 = var13.build();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("Before registerOperation call, suspend was successful for tx:" + var12 + " registration service proxy:" + var14);
      }

      BaseRegisterResponseType var15 = var14.registerOperation(var11);
      if (WSATHelper.isDebugEnabled()) {
         this.debug("Return from registerOperation call:" + var15);
      }

      if (var15 != null) {
         EndpointReference var16 = var15.getCoordinatorProtocolService();
         ForeignRecoveryContext var17 = ForeignRecoveryContextManager.getInstance().addAndGetForeignRecoveryContextForTidByteArray(var4);
         var17.setEndpointReference(var16, var2.getVersion());
         if (((weblogic.transaction.Transaction)var12).getProperty("weblogic.wsee.wstx.foreignContext") == null) {
            ((weblogic.transaction.Transaction)var12).setProperty("weblogic.wsee.wstx.foreignContext", var17);
         }

         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var12);
         if (WSATHelper.isDebugEnabled()) {
            this.debug("Returned from registerOperation call resumed tx:" + var12);
         }

      } else {
         this.log("Sending fault. Context refused registerResponseType is null");
         throw new WebServiceException("Sending fault. Context refused registerResponseType is null");
      }
   }

   public void log(String var1) {
      WseeWsatLogger.logWSATServerHelper("WSATServerInterceptor:" + var1);
   }

   private void debug(String var1) {
      WSATHelper.getInstance().debug("WSATServerInterceptor:" + var1);
   }
}
