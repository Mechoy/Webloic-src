package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.tubeline.TubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.WSATConstants;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.policy.ATPolicyHelper;
import weblogic.wsee.wstx.wsat.validation.TXAttributesValidator;

public class WSATTubelineDeploymentListener implements TubelineDeploymentListener {
   private static final Logger LOGGER = Logger.getLogger(WSATTubelineDeploymentListener.class.getName());
   private static boolean isOnServer = KernelStatus.isServer();

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (isOnServer) {
         WSBinding var3 = var1.getBinding();
         EnvironmentFactory var4 = JAXRPCEnvironmentFeature.getFactory(var1);
         final TransactionalFeature var5 = this.getTransactionalFeature(var3, var4, true);
         if (var5 != null && var5.isEnabled()) {
            TubeFactory var6 = new TubeFactory() {
               public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
                  return new WSATClientTube(var1, var2, var5);
               }

               public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
                  return var1;
               }
            };
            TubelineAssemblerItem var7 = new TubelineAssemblerItem("WSATClient", var6);
            HashSet var8 = new HashSet();
            var8.add("WS_SECURITY_1.1");
            var7.setGoAfter(var8);
            HashSet var9 = new HashSet();
            var9.add("ADDRESSING_HANDLER");
            var7.setGoBefore(var9);
            var2.add(var7);
         }
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (isOnServer) {
         WSEndpoint var3 = var1.getEndpoint();
         WSBinding var4 = var3.getBinding();
         EnvironmentFactory var5 = JAXRPCEnvironmentFeature.getFactory(var3);
         final TransactionalFeature var6 = this.getTransactionalFeature(var4, var5, false);
         if (var6 != null && var6.isEnabled()) {
            this.validateEJBTxAttributes(var1, var6);
            Iterable var7 = var1.getWsdlModel().getBinding().getPortType().getOperations();
            inheritATAtrtibuteFromPortLevel(var7, var6);
            TubeFactory var8 = new TubeFactory() {
               public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
                  return var1;
               }

               public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
                  return new WSATServerTube(var1, var2, var6);
               }
            };
            Set var9 = ((BindingImpl)var5.getBinding()).getKnownHeaders();
            this.registerKnownHeaders(var6, var9);
            TubelineAssemblerItem var10 = new TubelineAssemblerItem("WSATServer", var8);
            HashSet var11 = new HashSet();
            var11.add("POST_WS_SECURITY_POLICY_1.2");
            var11.add("OWSM_SECURITY_POLICY_HANDLER");
            var10.setGoAfter(var11);
            HashSet var12 = new HashSet();
            var12.add("ADDRESSING_HANDLER");
            var10.setGoBefore(var12);
            var2.add(var10);
         }
      }
   }

   private void validateEJBTxAttributes(ServerTubeAssemblerContext var1, TransactionalFeature var2) {
      DeployInfo var3 = (DeployInfo)var1.getEndpoint().getContainer().getSPI(DeployInfo.class);
      if (var3 != null && var3 instanceof EJBDeployInfo) {
         EJBDeployInfo var4 = (EJBDeployInfo)var3;
         Map var5 = var4.getTransactionAttributes();
         if (var5 == null) {
            return;
         }

         Short var6 = (Short)var5.get("--*");
         if (var6 != null) {
            this.validateProviderBasedEJB(var1, var2, var6);
         } else {
            this.validSEIBasedEJB(var2, var5);
         }
      }

   }

   private void validSEIBasedEJB(TransactionalFeature var1, Map<String, Short> var2) {
      TXAttributesValidator var3 = new TXAttributesValidator();
      Set var4 = var2.entrySet();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Map.Entry var6 = (Map.Entry)var5.next();
         Transactional.TransactionFlowType var7 = var1.getFlowType((String)var6.getKey());
         var3.visitOperation((String)var6.getKey(), (Short)var6.getValue(), var7);
      }

      var3.validate();
   }

   private void validateProviderBasedEJB(ServerTubeAssemblerContext var1, TransactionalFeature var2, Short var3) {
      if (var3 != null) {
         TXAttributesValidator var4 = new TXAttributesValidator();
         Iterable var5 = var1.getWsdlModel().getBinding().getPortType().getOperations();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            WSDLOperation var7 = (WSDLOperation)var6.next();
            String var8 = var7.getName().getLocalPart();
            Transactional.TransactionFlowType var9 = var2.getFlowType(var8);
            var4.visitOperation(var8, var3, var9);
         }

         var4.validate();
      }

   }

   private TransactionalFeature getTransactionalFeature(WSBinding var1, EnvironmentFactory var2, boolean var3) {
      WSDLPort var4 = var2.getPort();
      if (var4 == null) {
         return var3 && var1.getSOAPVersion() != null ? (TransactionalFeature)var1.getFeature(TransactionalFeature.class) : null;
      } else {
         EnvironmentFactory.SingletonService var5 = var2.getService();
         QName var6 = var4.getName();
         WsPort var7 = var5.getPort(var6.getLocalPart());
         PolicyServer var8 = var5.getPolicyServer();
         TransactionalFeature var9 = (TransactionalFeature)var1.getFeature(TransactionalFeature.class);
         if (var9 == null) {
            var9 = ATPolicyHelper.buildFeatureFromWsdl(var7.getWsdlPort(), var8);
            if (var3 && var9 != null) {
               ((WebServiceFeatureList)var1.getFeatures()).add(var9);
            }
         }

         return var9;
      }
   }

   private void registerKnownHeaders(TransactionalFeature var1, Set<QName> var2) {
      boolean var3 = isOnServer && WSATTubeHelper.isIssuedTokenEnabled();
      Transactional.Version var4 = var1.getVersion();
      if (var4 == Version.WSAT10) {
         this.addWSAT10Header(var2, var3);
      } else if (var4 == Version.DEFAULT) {
         this.addWSAT10Header(var2, var3);
         this.addWSAT11Header(var2, var3);
      } else if (var4 == Version.WSAT11 || var4 == Version.WSAT12) {
         this.addWSAT11Header(var2, var3);
      }

   }

   private void addWSAT11Header(Set<QName> var1, boolean var2) {
      var1.add(WSATConstants.WSCOOR11_CONTEXT_QNAME);
      if (var2) {
         var1.add(new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "IssuedTokens"));
      }

   }

   private void addWSAT10Header(Set<QName> var1, boolean var2) {
      var1.add(WSATConstants.WSCOOR_CONTEXT_QNAME);
      if (var2) {
         var1.add(new QName("http://schemas.xmlsoap.org/ws/2005/02/trust", "IssuedTokens"));
      }

   }

   private static void inheritATAtrtibuteFromPortLevel(Iterable<? extends WSDLOperation> var0, TransactionalFeature var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         WSDLOperation var3 = (WSDLOperation)var2.next();
         String var4 = var3.getName().getLocalPart();
         if (!var3.isOneWay()) {
            if (var1.isEnabled(var4)) {
               var1.setEnabled(var4, var1.isEnabled(var4));
               var1.setFlowType(var4, var1.getFlowType(var4));
            }
         } else {
            var1.setEnabled(var4, false);
         }
      }

   }
}
