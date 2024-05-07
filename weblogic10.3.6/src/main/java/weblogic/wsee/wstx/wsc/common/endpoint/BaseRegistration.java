package weblogic.wsee.wstx.wsc.common.endpoint;

import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import weblogic.transaction.WLXid;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.TransactionServices;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.WSATException;
import weblogic.wsee.wstx.wsat.WSATFaultFactory;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.WSATSynchronization;
import weblogic.wsee.wstx.wsat.WSATXAResource;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.RegistrationIF;
import weblogic.wsee.wstx.wsc.common.WSCUtil;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;

public abstract class BaseRegistration<T extends EndpointReference, K, P> implements RegistrationIF<T, K, P> {
   WebServiceContext context;
   Transactional.Version version;

   protected BaseRegistration(WebServiceContext var1, Transactional.Version var2) {
      this.context = var1;
      this.version = var2;
   }

   public BaseRegisterResponseType<T, P> registerOperation(BaseRegisterType<T, K> var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logRegisterOperationEntered(var1);
      }

      String var2 = WSATHelper.getInstance().getWSATTidFromWebServiceContextHeaderList(this.context);
      WLXid var3 = TransactionIdHelper.getInstance().wsatid2xid(var2);
      this.checkIssuedTokenAtApplicationLevel(var2);
      byte[] var4 = this.processRegisterTypeAndEnlist(var1, var3);
      BaseRegisterResponseType var5 = this.createRegisterResponseType(var3, var4);
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logRegisterOperationExited(var5);
      }

      return var5;
   }

   byte[] processRegisterTypeAndEnlist(BaseRegisterType<T, K> var1, Xid var2) {
      if (var1 == null) {
         WSATFaultFactory.throwInvalidParametersFault();
      }

      String var3 = var1.getProtocolIdentifier();
      if (var1.isDurable()) {
         return this.enlistResource(var2, var1.getParticipantProtocolService());
      } else if (var1.isVolatile()) {
         this.registerSynchronization(var2, var1.getParticipantProtocolService());
         return null;
      } else {
         WseeWsatLogger.logUnknownParticipantIdentifier(var3);
         throw new WebServiceException("unknown participant identifier:" + var3);
      }
   }

   BaseRegisterResponseType<T, P> createRegisterResponseType(WLXid var1, byte[] var2) {
      BaseRegisterResponseType var3 = this.newRegisterResponseType();
      String var4 = this.getCoordinatorAddress();
      String var5 = TransactionIdHelper.getInstance().xid2wsatid(var1);
      String var6 = new String(var2);
      EndpointReferenceBuilder var7 = this.getEndpointReferenceBuilder();
      EndpointReference var8 = var7.address(var4).referenceParameter(WSCUtil.referenceElementTxId(var5), WSCUtil.referenceElementBranchQual(var6), WSCUtil.referenceElementRoutingInfo()).build();
      var3.setCoordinatorProtocolService(var8);
      return var3;
   }

   private byte[] enlistResource(Xid var1, T var2) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logEnlistResource(var2, var1);
      }

      WSATXAResource var3 = new WSATXAResource(this.version, var2, var1);

      try {
         byte[] var4 = this.getTransactionServices().enlistResource(var3, var1);
         var3.setBranchQualifier(var4);
         return var4;
      } catch (WSATException var5) {
         WseeWsatLogger.logExceptionWSATXAResourceEnlist(var2, var1, var5);
         throw new WebServiceException(var5);
      }
   }

   private void registerSynchronization(Xid var1, T var2) {
      WseeWsatLogger.logRegisterSynchronization(var2, var1);
      WSATSynchronization var3 = new WSATSynchronization(this.version, var2, var1);

      try {
         this.getTransactionServices().registerSynchronization(var3, var1);
      } catch (WSATException var5) {
         WseeWsatLogger.logExceptionDuringRegisterSynchronization(var5);
         WSATFaultFactory.throwContextRefusedFault();
      }

   }

   private void checkIssuedTokenAtApplicationLevel(String var1) {
      SOAPMessageContext var2 = EnvironmentFactory.getContext(this.context.getMessageContext());
      SCCredential var3 = var2 == null ? null : (SCCredential)var2.getProperty("weblogic.wsee.wssc.sct");
      if (var3 != null) {
         String var4 = var3.getAppliesToElement() == null ? null : var3.getAppliesToElement().getTextContent();
         if (!("urn:uuid:" + var1).equals(var4)) {
            if (WSATHelper.isDebugEnabled()) {
               WSATHelper.getInstance().debug("the token used to protect the registration message doesn't known the coordination identifier");
            }

            throw new WebServiceException("unknown context identifier:" + var1);
         }
      }

   }

   protected abstract EndpointReferenceBuilder<T> getEndpointReferenceBuilder();

   protected abstract BaseRegisterResponseType<T, P> newRegisterResponseType();

   protected abstract String getCoordinatorAddress();

   protected TransactionServices getTransactionServices() {
      return WSATHelper.getTransactionServices();
   }
}
