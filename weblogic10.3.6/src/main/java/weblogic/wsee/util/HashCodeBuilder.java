package weblogic.wsee.util;

public class HashCodeBuilder {
   int hashCode = 17;

   public void add(Object var1) {
      this.hashCode = 37 * this.hashCode + getObjectHashCode(var1);
   }

   private static int getObjectHashCode(Object var0) {
      int var1 = 0;
      if (var0 != null) {
         var1 = var0.hashCode();
      }

      return var1;
   }

   public int hashCode() {
      return this.hashCode;
   }
}
