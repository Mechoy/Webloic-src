package weblogic.management.tools;

public class DefaultValue {
   private String m_value;
   private boolean m_isPrimitive;

   public DefaultValue(String var1, boolean var2) {
      this.m_value = var1;
      this.m_isPrimitive = var2;
   }

   public String testForEquality(String var1) {
      return this.m_isPrimitive ? this.m_value + " == " + var1 : this.m_value + ".equals(" + var1 + ")";
   }

   public String toString() {
      return "[DefaultValue:" + this.m_value + "]";
   }
}
