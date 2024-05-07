package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyWsdlExtension implements WsdlExtension {
   private Map policies = new HashMap();

   public void addPolicy(PolicyStatement var1) {
      WsdlUtils.addPolicyToMap(this.policies, var1);
   }

   public void addAllPolicies(Map var1) {
      this.policies.putAll(var1);
   }

   public boolean policyExists(PolicyStatement var1) {
      return WsdlUtils.policyExists(this.policies, var1);
   }

   public boolean hasPolicies() {
      return this.policies.size() > 0;
   }

   public Map getPolicies() {
      return this.policies;
   }

   public String getKey() {
      return "Policy";
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.setAttachedPolices(this.getPolicies());
      Iterator var3 = this.getPolicies().values().iterator();

      while(var3.hasNext()) {
         PolicyStatement var4 = (PolicyStatement)var3.next();
         var4.write(var1, var2);
      }

   }

   public static NormalizedExpression getEffectivePolicy(Map var0) throws PolicyException {
      NormalizedExpression var1 = NormalizedExpression.createUnitializedExpression();
      PolicyStatement var3;
      if (var0 != null) {
         for(Iterator var2 = var0.values().iterator(); var2.hasNext(); var1 = PolicyMath.merge(var1, var3.normalize())) {
            var3 = (PolicyStatement)var2.next();
         }
      }

      return var1;
   }

   public static NormalizedExpression getEffectivePolicy(PolicyServer var0, Map var1, PolicyURIs var2) throws PolicyException {
      NormalizedExpression var3 = NormalizedExpression.createUnitializedExpression();
      if (var2 != null) {
         PolicyRefFactory var4 = new PolicyRefFactory(var1);
         URI[] var5 = var2.getURIs();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            PolicyRef var7 = var4.createPolicyRef((String)null, var5[var6]);
            var3 = PolicyMath.merge(var3, var7.getNormalizedPolicy(var0, false));
         }
      }

      return var3;
   }
}
