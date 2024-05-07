package weblogic.ejb.spi;

import java.util.HashSet;
import java.util.Set;

public final class DescriptorValidator {
   static Set validWarnings = new HashSet();

   public static void validateDisableWarnings(String[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (!validWarnings.contains(var0[var1])) {
            throw new IllegalArgumentException(var0[var1] + " is not a legal value for the disable-warning element");
         }
      }

   }

   static {
      validWarnings.add("BEA-010054");
      validWarnings.add("BEA-010202");
      validWarnings.add("BEA-010001");
      validWarnings.add("BEA-010200");
   }
}
