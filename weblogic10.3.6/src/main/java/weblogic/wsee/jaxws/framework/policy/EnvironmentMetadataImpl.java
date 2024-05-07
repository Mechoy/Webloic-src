package weblogic.wsee.jaxws.framework.policy;

public class EnvironmentMetadataImpl implements EnvironmentMetadata {
   private String domain = null;
   private String instance = null;

   public EnvironmentMetadataImpl(String var1, String var2) {
      this.domain = var1;
      this.instance = var2;
   }

   public String getDomain() {
      return this.domain;
   }

   public String getInstance() {
      return this.instance;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append(": domain=").append(this.domain);
      var1.append(", instance=").append(this.instance);
      return var1.toString();
   }
}
