package weblogic.wsee.jaxws.framework.policy;

public class OverridePropertyImpl implements OverrideProperty {
   private String name = null;
   private String value = null;

   public OverridePropertyImpl(String var1, String var2) {
      this.name = var1;
      this.value = var2;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append(": name=").append(this.name);
      var1.append(", value=").append(this.value);
      return var1.toString();
   }
}
