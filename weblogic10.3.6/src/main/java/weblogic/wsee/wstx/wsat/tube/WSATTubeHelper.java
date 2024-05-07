package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import java.security.AccessController;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.jws.jaxws.ClientPolicyFeature;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.WLXid;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.security.IssuedTokenCPBuilder;
import weblogic.wsee.wstx.wsc.common.WSCBuilderFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class WSATTubeHelper {
   private static boolean isIssuedTokenEnabled = false;
   private static String transportSecurityMode = "SSLRequired";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static boolean isIssuedTokenEnabled() {
      return isIssuedTokenEnabled;
   }

   public static boolean isSSLRequired() {
      return "SSLRequired".equals(transportSecurityMode) || "ClientCertRequired".equals(transportSecurityMode);
   }

   public static CredentialProvider getCredentialProvider(HeaderList var0) {
      Header var1 = var0.get("http://schemas.xmlsoap.org/ws/2005/02/trust", "IssuedTokens", true);
      if (var1 == null) {
         var1 = var0.get("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "IssuedTokens", true);
      }

      if (var1 == null) {
         return null;
      } else {
         SOAPMessage var2 = null;

         try {
            var2 = WLMessageFactory.getInstance().getMessageFactory(false).createMessage();
            var1.writeTo(var2);
            NodeList var3 = var2.getSOAPHeader().getElementsByTagNameNS("http://schemas.xmlsoap.org/ws/2005/02/trust", "IssuedTokens");
            if (var3.getLength() == 0) {
               var3 = var2.getSOAPHeader().getElementsByTagNameNS("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "IssuedTokens");
            }

            if (var3.getLength() == 0 && isIssuedTokenEnabled()) {
               throw new WebServiceException("no required issuedToken in the incoming soap message!");
            } else {
               IssuedTokenCPBuilder var4 = new IssuedTokenCPBuilder();
               Element var5 = (Element)var3.item(0);
               return var4.issuedTokens(var5).build();
            }
         } catch (SOAPException var6) {
            throw new WebServiceException("fail to import issuedToken!", var6);
         }
      }
   }

   public static ClientPolicyFeature getPolicyFeature(WSCBuilderFactory var0) {
      return isIssuedTokenEnabled ? var0.newWSATReqistrationClientPolicyFeatureBuilder().newClientPolicyFeature() : null;
   }

   public static TransactionalAttribute getTransactionalAttribute(TransactionalFeature var0, Packet var1, WSDLPort var2) {
      if (var2 == null) {
         boolean var7 = var0.isEnabled() && TransactionFlowType.NEVER != var0.getFlowType();
         boolean var8 = TransactionFlowType.MANDATORY == var0.getFlowType();
         if (WSATHelper.isDebugEnabled()) {
            debug("no wsdl port found, the effective transaction attribute is: enabled(" + var7 + "),required(" + var8 + "), version(" + var0.getVersion() + ").");
         }

         return new TransactionalAttribute(var7, var8, var0.getVersion());
      } else {
         WSDLBoundOperation var3 = var1.getMessage().getOperation(var2);
         if (var3 != null && var3.getOperation() != null && !var3.getOperation().isOneWay()) {
            String var4 = var3.getName().getLocalPart();
            boolean var5 = var0.isEnabled(var4) && TransactionFlowType.NEVER != var0.getFlowType(var4);
            boolean var6 = TransactionFlowType.MANDATORY == var0.getFlowType(var4);
            if (WSATHelper.isDebugEnabled()) {
               debug("the effective transaction attribute for operation' " + var4 + "' is : enabled(" + var5 + "),required(" + var6 + "), version(" + var0.getVersion() + ").");
            }

            return new TransactionalAttribute(var5, var6, var0.getVersion());
         } else {
            if (WSATHelper.isDebugEnabled()) {
               debug("no twoway operation found for this request, the effective transaction attribute is disabled.");
            }

            return new TransactionalAttribute(false, false, Version.DEFAULT);
         }
      }
   }

   static Transaction getTransaction() {
      return (Transaction)getTransactionHelper().getTransaction();
   }

   static TransactionHelper getTransactionHelper() {
      return TransactionHelper.getTransactionHelper();
   }

   static String getWSATTxIdForTransaction(Transaction var0) {
      return TransactionIdHelper.getInstance().xid2wsatid((WLXid)var0.getXID());
   }

   private static void debug(String var0) {
      WSATHelper.getInstance().debug("WSATTubeHelper:" + var0);
   }

   static {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
      if (var0 != null) {
         DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         if (var1 != null) {
            JTAMBean var2 = var1.getJTA();
            if (var2 != null) {
               isIssuedTokenEnabled = var2.isWSATIssuedTokenEnabled();
            }

            transportSecurityMode = var2.getWSATTransportSecurityMode();
         }
      }

   }
}
