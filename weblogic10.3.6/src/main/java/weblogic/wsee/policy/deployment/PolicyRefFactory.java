package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.framework.PolicyStatement;

public class PolicyRefFactory {
   private static final boolean debug = false;
   private Map localRefs;

   public PolicyRefFactory() {
      this((Map)null);
   }

   public PolicyRefFactory(Map var1) {
      this.localRefs = new HashMap();
      if (var1 != null && var1 != null) {
         this.localRefs = var1;
      }

   }

   public PolicyRef createPolicyRef(String var1, URI var2, byte[] var3, QName var4) {
      return new PolicyRef(var1, var2, var3, var4);
   }

   public PolicyRef createPolicyRef(String var1, URI var2) {
      if (var1 == null && var2 == null) {
         throw new IllegalArgumentException("NULL uri and RefName received!");
      } else {
         PolicyRef var3 = null;
         if (var2 != null && var2.getFragment() != null) {
            PolicyStatement var4 = (PolicyStatement)this.localRefs.get(var2.getFragment());
            if (var4 != null) {
               var3 = new PolicyRef(var1, var2, var4);
               return var3;
            }
         }

         if (var2 != null && "policy".equals(var2.getScheme())) {
            String var6 = var2.getRawSchemeSpecificPart();
            PolicyStatement var5 = (PolicyStatement)this.localRefs.get(var6);
            if (var5 != null) {
               var3 = new PolicyRef(var1, var2, var5);
            } else {
               var3 = new PolicyRef(var1, var2);
            }
         } else {
            var3 = new PolicyRef(var1, var2);
         }

         return var3;
      }
   }
}
