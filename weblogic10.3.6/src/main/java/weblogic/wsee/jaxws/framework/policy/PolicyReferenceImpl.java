package weblogic.wsee.jaxws.framework.policy;

import java.util.List;

public class PolicyReferenceImpl implements PolicyReference {
   private String category = null;
   private boolean enabled = true;
   private List<OverrideProperty> overrides = null;
   private String policyUri = null;

   public PolicyReferenceImpl(String var1, boolean var2, List<OverrideProperty> var3, String var4) {
      this.category = var1;
      this.enabled = var2;
      this.overrides = var3;
      this.policyUri = var4;
   }

   public String getCategory() {
      return this.category;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public List<OverrideProperty> getOverrideProperties() {
      return this.overrides;
   }

   public String getPolicyURI() {
      return this.policyUri;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append(": category=").append(this.category);
      var1.append(", enabled=").append(this.enabled);
      var1.append(", uri=").append(this.policyUri);
      var1.append(", overrides=").append(this.overrides != null ? this.overrides.toString() : "null");
      return var1.toString();
   }
}
