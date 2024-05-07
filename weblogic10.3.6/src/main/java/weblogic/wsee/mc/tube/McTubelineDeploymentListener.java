package weblogic.wsee.mc.tube;

import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.mc.internal.MCSupported;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.ws.WsPort;

public class McTubelineDeploymentListener implements TubelineDeploymentListener {
   public static final String MC_INITIATOR_TUBE = "McInitiator";
   public static final String MC_RECEIVER_TUBE = "McReceiver";
   private static final Logger LOGGER = Logger.getLogger(McTubelineDeploymentListener.class.getName());

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      EnvironmentFactory var3 = JAXRPCEnvironmentFeature.getFactory(var1);
      if (var3 != null) {
         EnvironmentFactory.SingletonService var4 = var3.getService();
         if (var4 != null) {
            WSDLPort var5 = var3.getPort();
            if (var5 != null) {
               QName var6 = var5.getName();
               WsPort var7 = var4.getPort(var6.getLocalPart());
               PolicyServer var8 = var4.getPolicyServer();

               NormalizedExpression var9;
               try {
                  var9 = var8.getEndpointPolicy(var7.getWsdlPort());
               } catch (PolicyException var17) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Exception getting policy for " + var1.getService().getServiceName());
                  }

                  WseeMCLogger.logUnexpectedException(var17.toString(), var17);
                  return;
               }

               MCSupported var10 = (MCSupported)var9.getPolicyAssertion(MCSupported.class);
               if (var10 != null) {
                  WSBinding var11 = var1.getBinding();
                  AsyncClientTransportFeature var12 = (AsyncClientTransportFeature)var11.getFeature(AsyncClientTransportFeature.class);
                  boolean var13 = var12 != null && var12.isEnabled();
                  if (var13) {
                     if (var10.isOptional()) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.fine("AsyncClientTransportFeature is enabled, so MakeConnection won't be enabled by the optional MC assertion");
                        }

                     } else {
                        throw new WebServiceException(WseeMCLogger.logCannotUseAsyncClientTransportLoggable().getMessage());
                     }
                  } else {
                     if (var10.isOptional()) {
                        McFeature var14 = (McFeature)var11.getFeature(McFeature.class);
                        EndpointAddress var15 = var1.getAddress();
                        AddressingVersion var16 = var11.getAddressingVersion();
                        if ((var14 == null || !var14.isEnabled()) && (var16 == null || !var15.toString().equals(var16.anonymousUri))) {
                           if (LOGGER.isLoggable(Level.FINE)) {
                              LOGGER.fine("No user-defined MC feature, so MakeConnection won't be enabled by the optional MC assertion");
                           }

                           return;
                        }
                     }

                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Creating MakeConnection initiator tubeline items for " + var1.getService().getServiceName());
                     }

                     AbstractTubeFactory var18 = new AbstractTubeFactory() {
                        public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
                           WSBinding var3 = var2.getBinding();
                           WebServiceFeatureList var5 = (WebServiceFeatureList)var3.getFeatures();
                           McFeature var4;
                           if (!var5.isEnabled(McFeature.class)) {
                              var4 = new McFeature();
                              var5.add(var4);
                           } else {
                              var4 = (McFeature)var3.getFeature(McFeature.class);
                           }

                           if (!var5.isEnabled(MemberSubmissionAddressingFeature.class) && !var5.isEnabled(AddressingFeature.class)) {
                              var5.add(new AddressingFeature(true));
                           }

                           if (!var5.isEnabled(OneWayFeature.class)) {
                              var5.add(new OneWayFeature(true));
                           }

                           ClientIdentityFeature var6 = (ClientIdentityFeature)var5.get(ClientIdentityFeature.class);
                           if (var6 != null && var6.getRawClientId() != null) {
                              var4.setBinding(var2.getBinding());
                              return new McInitiatorTube(var1, var2);
                           } else {
                              throw new IllegalStateException(WseeMCLogger.logClientIdentityNotProvidedLoggable().getMessage());
                           }
                        }
                     };
                     TubelineAssemblerItem var19 = new TubelineAssemblerItem("McInitiator", var18);
                     HashSet var20 = new HashSet();
                     var20.add("WS_SECURITY_1.1");
                     var20.add("PRE_WS_SECURITY_POLICY_1.2");
                     var19.setGoAfter(var20);
                     HashSet var21 = new HashSet();
                     var21.add("WsrmClient");
                     var21.add("ADDRESSING_HANDLER");
                     var21.add("client");
                     var19.setGoBefore(var21);
                     var2.add(var19);
                  }
               }
            }
         }
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      EnvironmentFactory var3 = JAXRPCEnvironmentFeature.getFactory(var1.getEndpoint());
      if (var3 != null) {
         WSDLPort var4 = var3.getPort();
         if (var4 != null) {
            EnvironmentFactory.SingletonService var5 = var3.getService();
            if (var5 != null) {
               QName var6 = var4.getName();
               WsPort var7 = var5.getPort(var6.getLocalPart());
               PolicyServer var8 = var5.getPolicyServer();
               if (var8 != null) {
                  NormalizedExpression var9;
                  try {
                     var9 = var8.getEndpointPolicy(var7.getWsdlPort());
                  } catch (PolicyException var14) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Exception getting policy for " + var1.getEndpoint().getServiceName());
                     }

                     WseeMCLogger.logUnexpectedException(var14.toString(), var14);
                     return;
                  }

                  if (var9.containsPolicyAssertion(MCSupported.class)) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Creating MakeConnection server tubeline items for " + var1.getEndpoint().getServiceName());
                     }

                     AbstractTubeFactory var10 = new AbstractTubeFactory() {
                        public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
                           WSBinding var3 = var2.getEndpoint().getBinding();
                           WebServiceFeatureList var4 = (WebServiceFeatureList)var3.getFeatures();
                           if (!var4.isEnabled(AddressingFeature.class)) {
                              var4.add(new AddressingFeature(true));
                           }

                           return new McReceiverTube(var1, var2);
                        }
                     };
                     TubelineAssemblerItem var11 = new TubelineAssemblerItem("McReceiver", var10);
                     HashSet var12 = new HashSet();
                     var12.add("ADDRESSING_HANDLER");
                     var12.add("WS_SECURITY_1.1");
                     var11.setGoBefore(var12);
                     HashSet var13 = new HashSet();
                     var13.add("WsrmServer");
                     var11.setGoAfter(var13);
                     var2.add(var11);
                  }
               }
            }
         }
      }
   }
}
