package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.ws.WebServiceException;
import weblogic.j2ee.descriptor.wl.OwsmPolicyBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.jws.jaxws.PoliciesFeature;
import weblogic.jws.jaxws.PolicyFeature;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.ListenerUsage;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jws.jaxws.owsm.SecurityPoliciesFeature;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;
import weblogic.wsee.mtom.internal.MtomXopClientHandler;
import weblogic.wsee.policy.deployment.WseePolicyReferenceInfo;

public class ClientTubelineDeploymentListener extends TubelineDeploymentListener {
   private static Class handlerClass;
   private static final String ENABLED = "enabled";
   private List<String> preceding = Collections.singletonList("PRE_INVOKE_HANDLER");
   private List<String> following = new ArrayList();

   public ClientTubelineDeploymentListener() {
      super(ClientTubelineDeploymentListener.class, ListenerUsage.CLIENT_ONLY);
      this.following.add("ADDRESSING_HANDLER");
      this.following.add("POLICY_CLIENT_RT_HANDLER");
      this.following.add("RELIABILITY_HANDLER");
      this.following.add("ASYNC_HANDLER");
   }

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (this.hasSecurityPolicyAnnotation(var1) || this.hasDDPolicy(var1)) {
         if (this.hasPolicyFeature(var1)) {
            throw new WebServiceException("SecurityPolicyFeature and PolicyFeature can not be set simultaneously.");
         }

         handlerClass = this.getHandler(var1).getClass();
         this.setClient(var1);
         super.createClient(var1, var2);
      }

   }

   private boolean hasPolicyFeature(ClientTubeAssemblerContext var1) {
      WSBinding var2 = var1.getBinding();
      PoliciesFeature var3 = (PoliciesFeature)var2.getFeature(PoliciesFeature.class);
      PolicyFeature var4 = (PolicyFeature)var2.getFeature(PolicyFeature.class);
      return var3 != null || var4 != null;
   }

   private boolean hasSecurityPolicyAnnotation(ClientTubeAssemblerContext var1) {
      WSBinding var2 = var1.getBinding();
      SecurityPoliciesFeature var3 = (SecurityPoliciesFeature)var2.getFeature(SecurityPoliciesFeature.class);
      SecurityPolicyFeature var4 = (SecurityPolicyFeature)var2.getFeature(SecurityPolicyFeature.class);
      return var3 != null && var3.isEnabled() || var4 != null && var4.isEnabled();
   }

   private boolean hasDDPolicy(ClientTubeAssemblerContext var1) {
      PortInfoBean var2 = ConfigUtil.getPortInfoBeanForClient(var1);
      if (var2 == null) {
         return false;
      } else {
         OwsmPolicyBean[] var3 = var2.getOwsmPolicy();
         return var3 != null && var3.length > 0;
      }
   }

   private void setDDPolicies(EnvironmentFactory var1, List<String> var2) {
      PortInfoBean var3 = ((ClientEnvironmentFactory)var1).getPortBean();
      if (var3 != null) {
         OwsmPolicyBean[] var4 = var3.getOwsmPolicy();
         if (var4 != null && var4.length > 0) {
            OwsmPolicyBean[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               OwsmPolicyBean var8 = var5[var7];
               if (var8.getStatus().equals("enabled")) {
                  var2.add(var8.getUri());
               } else {
                  var2.remove(var8.getUri());
               }
            }
         }

      }
   }

   private void setDDPoliciesInfos(EnvironmentFactory var1, List<WseePolicyReferenceInfo> var2) {
      PortInfoBean var3 = ((ClientEnvironmentFactory)var1).getPortBean();
      if (var3 != null) {
         OwsmPolicyBean[] var4 = var3.getOwsmPolicy();
         if (var4 != null && var4.length > 0) {
            OwsmPolicyBean[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               OwsmPolicyBean var8 = var5[var7];
               if (var8.getStatus().equals("enabled")) {
                  var2.add(new WseePolicyReferenceInfo("owsm-security", var8.getUri(), var8.getStatus(), getOverrides(var8)));
               } else {
                  var2.remove(var8.getUri());
               }
            }
         }

      }
   }

   private static Map<String, String> getOverrides(OwsmPolicyBean var0) {
      HashMap var1 = new HashMap();
      PropertyNamevalueBean[] var2 = var0.getSecurityConfigurationProperties();
      if (var2 != null) {
         PropertyNamevalueBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PropertyNamevalueBean var6 = var3[var5];
            var1.put(var6.getName(), var6.getValue());
         }
      }

      return var1;
   }

   private SecurityPolicyHandler getHandler(ClientTubeAssemblerContext var1) throws WebServiceException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      return this.loadHandler(var2);
   }

   private void setClient(ClientTubeAssemblerContext var1) {
      WSBinding var2 = var1.getBinding();
      ClientEnvironmentFeature var3 = new ClientEnvironmentFeature();
      ((WebServiceFeatureList)var2.getFeatures()).add(var3);
   }

   Class getHandlerClass() {
      return handlerClass;
   }

   List getPrecedingHandlers() {
      return this.preceding;
   }

   List getFollowingHandlers() {
      return this.following;
   }

   protected void setProperties(EnvironmentFactory var1, Map var2) {
      WSBinding var3 = var1.getBinding();
      List var4 = this.getSecurityPolicyFeatureURIs(var1);
      Object var5 = new ArrayList();
      boolean var6 = var4.size() == 0;
      if (var6) {
         this.setDDPolicies(var1, var4);
         this.setDDPoliciesInfos(var1, (List)var5);
      } else {
         var5 = this.getWseeReferenceInfos(var4);
      }

      var2.put("weblogic.wsee.jaxws.owsm.SecurityPolicyURIList", var4);
      if (var3.getFeature(ClientEnvironmentFeature.class) != null) {
         var2.put("weblogic.wsee.jaxws.owsm.Client", true);
      }

      this.setAdditionalOwsmPropertiesWithOverrides(var1, var2, (List)var5, true);
   }

   private List<WseePolicyReferenceInfo> getWseeReferenceInfos(List<String> var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add(new WseePolicyReferenceInfo("owsm-security", var4));
         }
      }

      return var2;
   }

   List getFollowingXopHandlers() {
      return Collections.singletonList("OWSM_SECURITY_POLICY_HANDLER");
   }

   List getPrecedingXopHandlers() {
      return Collections.singletonList("CONNECTION_HANDLER");
   }

   HandlerInfo getXopHandlerInfo() {
      return new HandlerInfo(MtomXopClientHandler.class, new HashMap(), (QName[])null);
   }
}
