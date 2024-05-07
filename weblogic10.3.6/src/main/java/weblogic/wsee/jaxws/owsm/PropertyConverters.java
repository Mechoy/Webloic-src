package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.ws.handler.MessageContext;
import weblogic.j2ee.descriptor.wl.OwsmPolicyBean;
import weblogic.j2ee.descriptor.wl.OwsmSecurityPolicyBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.framework.jaxrpc.AbstractPropertyConverter;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.PropertyConverter;
import weblogic.wsee.jaxws.framework.policy.EnvironmentMetadata;
import weblogic.wsee.jaxws.framework.policy.EnvironmentMetadataFactory;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectBinding;
import weblogic.wsee.jws.jaxws.owsm.PolicySubjectBindingFeature;
import weblogic.wsee.jws.jaxws.owsm.SecurityPoliciesFeature;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

public class PropertyConverters {
   private static final String ENABLED = "enabled";
   private Map<String, PropertyConverter> jaxrpcConverters;
   private EnvironmentFactory environment;
   private PortInfoBean portBean = null;

   public PropertyConverters(EnvironmentFactory var1) {
      this.environment = var1;
      if (this.environment instanceof ClientEnvironmentFactory) {
         this.portBean = ((ClientEnvironmentFactory)this.environment).getPortBean();
      }

   }

   public Map getConverters() {
      if (this.jaxrpcConverters == null) {
         this.initializeConverters();
      }

      return this.jaxrpcConverters;
   }

   private void initializeConverters() {
      this.jaxrpcConverters = new HashMap();
      this.jaxrpcConverters.put("weblogic.wsee.jaxws.owsm.SecurityPolicyURIList", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return PropertyConverters.this.getSecurityPolicyURIs(PropertyConverters.this.environment.getBinding());
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.jaxws.owsm.Server", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return PropertyConverters.this.environment.getBinding().getFeature(ServerEnvironmentFeature.class) != null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.jaxws.owsm.Client", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return PropertyConverters.this.environment.getBinding().getFeature(ClientEnvironmentFeature.class) != null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.jaxws.framework.policy.PolicySubjectBinding", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return PropertyConverters.this.getPolicySubjectBinding(PropertyConverters.this.environment.getBinding());
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.jaxws.framework.policy.EnvironmentMetadata", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return PropertyConverters.this.getEnvironmentMetadata(PropertyConverters.this.environment.getBinding());
         }
      });
   }

   private PolicySubjectBinding getPolicySubjectBinding(WSBinding var1) {
      PolicySubjectBindingFeature var2 = (PolicySubjectBindingFeature)var1.getFeature(PolicySubjectBindingFeature.class);
      return var2 != null ? var2.getPolicySubjectBinding() : null;
   }

   private EnvironmentMetadata getEnvironmentMetadata(WSBinding var1) {
      return var1.getFeature(ServerEnvironmentFeature.class) != null ? EnvironmentMetadataFactory.getEnvironmentMetadata() : null;
   }

   private List<String> getSecurityPolicyURIs(WSBinding var1) {
      ArrayList var2 = new ArrayList();
      SecurityPoliciesFeature var3 = (SecurityPoliciesFeature)var1.getFeature(SecurityPoliciesFeature.class);
      if (var3 != null && var3.isEnabled()) {
         Iterator var4 = var3.getPolicies().iterator();

         while(var4.hasNext()) {
            SecurityPolicyFeature var5 = (SecurityPolicyFeature)var4.next();
            var2.add(var5.getUri());
         }
      }

      SecurityPolicyFeature var6 = (SecurityPolicyFeature)var1.getFeature(SecurityPolicyFeature.class);
      if (var6 != null && var6.isEnabled()) {
         var2.add(var6.getUri());
      }

      this.addDDPolicies(var2);
      return var2;
   }

   private void addDDPolicies(List<String> var1) {
      if (this.environment.getContainer() == null) {
         this.addClientDDPolicies(var1);
      } else {
         this.addServerDDPolicies(var1);
      }

   }

   private void addServerDDPolicies(List<String> var1) {
      DeployInfo var2 = (DeployInfo)this.environment.getContainer().getSPI(DeployInfo.class);
      WebservicePolicyRefBean var3 = var2 != null ? var2.getPolicyRef() : null;
      if (var3 != null) {
         if (var3 != null) {
            String var4 = this.environment.getPort().getName().getLocalPart();
            PortPolicyBean[] var5 = var3.getPortPolicy();
            if (var5 != null) {
               PortPolicyBean[] var6 = var5;
               int var7 = var5.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  PortPolicyBean var9 = var6[var8];
                  if (var4.equals(var9.getPortName())) {
                     OwsmSecurityPolicyBean[] var10 = var9.getOwsmSecurityPolicy();
                     if (var10 != null) {
                        OwsmSecurityPolicyBean[] var11 = var10;
                        int var12 = var10.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                           OwsmSecurityPolicyBean var14 = var11[var13];
                           if (var14.getStatus().equals("enabled")) {
                              var1.add(var14.getUri());
                           } else {
                              var1.remove(var14.getUri());
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private void addClientDDPolicies(List<String> var1) {
      if (this.portBean != null) {
         OwsmPolicyBean[] var2 = this.portBean.getOwsmPolicy();
         if (var2 != null && var2.length > 0) {
            OwsmPolicyBean[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               OwsmPolicyBean var6 = var3[var5];
               if (var6.getStatus().equals("enabled")) {
                  var1.add(var6.getUri());
               } else {
                  var1.remove(var6.getUri());
               }
            }
         }

      }
   }
}
