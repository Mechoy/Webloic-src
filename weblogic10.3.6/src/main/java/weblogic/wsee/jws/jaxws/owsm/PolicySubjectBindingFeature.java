package weblogic.wsee.jws.jaxws.owsm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.jaxws.framework.policy.OverridePropertyImpl;
import weblogic.wsee.jaxws.framework.policy.PolicyReference;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectBinding;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectBindingImpl;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectMetadata;
import weblogic.wsee.jaxws.framework.policy.impl.PolicyReferenceImpl;
import weblogic.wsee.policy.deployment.WseePolicyReferenceInfo;

public class PolicySubjectBindingFeature extends WebServiceFeature {
   private static final String ID = PolicySubjectBindingFeature.class.getName();
   private PolicySubjectBinding psb = null;

   public String getID() {
      return ID;
   }

   public PolicySubjectBindingFeature(PolicySubjectBinding var1) {
      this.psb = var1;
   }

   public List<PolicyReference> getPolicyReferences() {
      return this.psb != null ? this.psb.getPolicyReferences() : null;
   }

   public boolean hasPolicyReferences() {
      return this.psb != null && this.psb.getPolicyReferences() != null && !this.psb.getPolicyReferences().isEmpty();
   }

   public PolicySubjectBinding getPolicySubjectBinding() {
      return this.psb;
   }

   public static PolicySubjectBindingFeature create(List<String> var0, PolicySubjectMetadata var1) {
      ArrayList var2 = new ArrayList();
      if (var0 != null) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add(new PolicyReferenceImpl("owsm-security", true, (List)null, var4));
         }
      }

      return new PolicySubjectBindingFeature(new PolicySubjectBindingImpl(var2, var1));
   }

   public static PolicySubjectBindingFeature createFeature(List<WseePolicyReferenceInfo> var0, PolicySubjectMetadata var1) {
      ArrayList var2 = new ArrayList();
      WseePolicyReferenceInfo var4;
      ArrayList var5;
      if (var0 != null) {
         for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.add(new PolicyReferenceImpl("owsm-security", var4.getStatus().equals("enabled"), var5, var4.getUri()))) {
            var4 = (WseePolicyReferenceInfo)var3.next();
            var5 = new ArrayList();
            if (var4.getOverrides() != null && var4.getOverrides().keySet() != null) {
               Iterator var6 = var4.getOverrides().keySet().iterator();

               while(var6.hasNext()) {
                  String var7 = (String)var6.next();
                  var5.add(new OverridePropertyImpl(var7, (String)var4.getOverrides().get(var7)));
               }
            }
         }
      }

      return new PolicySubjectBindingFeature(new PolicySubjectBindingImpl(var2, var1));
   }

   public static PolicySubjectBindingFeature create(PolicyReference[] var0, PolicySubjectMetadata var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         var2.add(var0[var3]);
      }

      return new PolicySubjectBindingFeature(new PolicySubjectBindingImpl(var2, var1));
   }
}
