package weblogic.wsee.jaxws.framework.jaxrpc.client;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.server.Container;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import weblogic.jws.jaxws.ClientPolicyFeature;
import weblogic.jws.jaxws.policy.PolicySource;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.security.policy.SecurityPolicyCustomizer;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.Pair;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class ClientEnvironmentFactory extends EnvironmentFactory {
   private WSService service;
   private Map<Pair<PolicySource, String>, NormalizedExpression> policyCache;

   public ClientEnvironmentFactory(ClientTubeAssemblerContext var1) {
      this(var1.getBinding(), var1.getService(), var1.getWsdlModel());
   }

   public ClientEnvironmentFactory(WSBinding var1, WSService var2, WSDLPort var3) {
      super(var1, var3, (Container)null);
      this.policyCache = new HashMap();
      this.service = var2;
   }

   public WSService getWSService() {
      return this.service;
   }

   protected void initPolicyContext(EnvironmentFactory.SingletonService var1) {
      WsdlDefinitions var2 = this.getWsdlDef();
      if (var2 != null) {
         WsdlPolicySubject var3 = new WsdlPolicySubject(var2);
         WssPolicyContext var4 = new WssPolicyContext(false);
         var4.getPolicyServer().addPolicyFinder(new PolicyFinder() {
            private URL resolveURL(URL var1, String var2) {
               try {
                  URL var3;
                  if (!"jar".equals(var1.getProtocol()) && !"zip".equals(var1.getProtocol())) {
                     var3 = var1.toURI().resolve(new URI(var2)).toURL();
                  } else {
                     String var4 = var1.toString();
                     int var5 = var4.indexOf(33);
                     if (var5 > 0) {
                        var3 = new URL(var4.substring(0, var5 + 1) + (new URI(var4.substring(var5 + 1))).resolve(new URI(var2)).toString());
                     } else {
                        var3 = new URL(var2);
                     }
                  }

                  return var3;
               } catch (URISyntaxException var6) {
                  throw new WebServiceException(var6);
               } catch (MalformedURLException var7) {
                  throw new WebServiceException(var7);
               }
            }

            public PolicyStatement findPolicy(String var1, String var2) throws PolicyException {
               var1 = checkFileExtension(var1);
               URL var3 = ClientEnvironmentFactory.this.service.getWSDLDocumentLocation();
               if (var3 != null) {
                  URL var4 = this.resolveURL(var3, var1);
                  if (var4 != null) {
                     try {
                        return readPolicyFromStream(var1, var4.openStream(), true);
                     } catch (IOException var7) {
                     }
                  }

                  var4 = this.resolveURL(var3, "policies/" + var1);
                  if (var4 != null) {
                     try {
                        return readPolicyFromStream(var1, var4.openStream(), true);
                     } catch (IOException var6) {
                     }
                  }
               }

               return null;
            }
         });
         var4.getPolicyServer().addPolicies(var3.getPolicies());
         var1.setWssPolicyContext(var4);
         this.loadClientSidePolicy(var1);
      }

   }

   private void loadClientSidePolicy(EnvironmentFactory.SingletonService var1) {
      ClientPolicyFeature var2 = (ClientPolicyFeature)this.getBinding().getFeature(ClientPolicyFeature.class);
      if (var2 != null) {
         try {
            String var3 = this.getPort().getName().getLocalPart();
            var1.setUsingPolicy(true);
            WsPort var4 = var1.getPort(var3);
            Iterator var5 = var4.getEndpoint().getMethods();

            while(var5.hasNext()) {
               WsMethod var6 = (WsMethod)var5.next();
               var6.setCachedEffectiveInboundPolicy(this.getInputPolicyForOperation(var2, var3, var6.getOperationName()));
               var6.setCachedEffectiveOutboundPolicy(this.getOutputPolicyForOperation(var2, var3, var6.getOperationName()));
            }
         } catch (Exception var7) {
            throw new WebServiceException(var7);
         }
      }

   }

   private NormalizedExpression getPolicy(PolicySource var1, String var2) {
      Pair var3 = new Pair(var1, var2);
      NormalizedExpression var4 = (NormalizedExpression)this.policyCache.get(var3);
      if (var4 == null) {
         Collection var5 = var1.getPolicy(var2);
         if (var5.size() > 0) {
            Iterator var6 = var5.iterator();
            if (var6.hasNext()) {
               var4 = getNormalizedPolicy(var2, (InputStream)var6.next());
            }

            while(var6.hasNext()) {
               var4 = PolicyMath.merge(var4, getNormalizedPolicy(var2, (InputStream)var6.next()));
            }
         }

         this.policyCache.put(var3, var4);
      }

      return var4;
   }

   private static NormalizedExpression getNormalizedPolicy(String var0, InputStream var1) {
      PolicyStatement var2 = null;

      try {
         var2 = PolicyFinder.readPolicyFromStream(var0, var1, true);
         if (SecurityPolicyCustomizer.isSecurityPolicyAbstract(var0, var2)) {
            throw new WebServiceException("Abstract policy can not be attached to client dynamically.");
         } else {
            return var2.normalize();
         }
      } catch (PolicyException var4) {
         throw new WebServiceException(var4);
      }
   }

   public NormalizedExpression getInputPolicyForOperation(ClientPolicyFeature var1, String var2, QName var3) {
      NormalizedExpression var4 = null;
      PolicySource var5 = var1.getEffectivePolicy();
      if (var5 != null) {
         var4 = this.merge(var4, this.getPolicy(var5, var2));
      }

      PolicySource var6 = var1.getEffectivePolicyForInputMessage();
      if (var6 != null) {
         var4 = this.merge(var4, this.getPolicy(var6, var2));
      }

      PolicySource var7 = var1.getEffectivePolicyForOperation(var3);
      if (var7 != null) {
         var4 = this.merge(var4, this.getPolicy(var7, var2));
      }

      PolicySource var8 = var1.getEffectivePolicyForInputMessage(var3);
      if (var8 != null) {
         var4 = this.merge(var4, this.getPolicy(var8, var2));
      }

      return var4;
   }

   public NormalizedExpression getOutputPolicyForOperation(ClientPolicyFeature var1, String var2, QName var3) {
      NormalizedExpression var4 = null;
      PolicySource var5 = var1.getEffectivePolicy();
      if (var5 != null) {
         var4 = this.merge(var4, this.getPolicy(var5, var2));
      }

      PolicySource var6 = var1.getEffectivePolicyForOutputMessage();
      if (var6 != null) {
         var4 = this.merge(var4, this.getPolicy(var6, var2));
      }

      PolicySource var7 = var1.getEffectivePolicyForOperation(var3);
      if (var7 != null) {
         var4 = this.merge(var4, this.getPolicy(var7, var2));
      }

      PolicySource var8 = var1.getEffectivePolicyForOutputMessage(var3);
      if (var8 != null) {
         var4 = this.merge(var4, this.getPolicy(var8, var2));
      }

      return var4;
   }

   private NormalizedExpression merge(NormalizedExpression var1, NormalizedExpression var2) {
      if (var1 == null) {
         return var2;
      } else {
         return var2 == null ? var1 : PolicyMath.merge(var1, var2);
      }
   }
}
