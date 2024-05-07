package weblogic.management.security;

public class ProviderValidator {
   public static void validateProviders(ProviderMBean[] var0) throws IllegalArgumentException {
      if (var0 != null && var0.length != 0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            String var2 = var0[var1].getName();

            for(int var3 = var1 + 1; var3 < var0.length; ++var3) {
               String var4 = var0[var3].getName();
               if (var2.equals(var4)) {
                  throw new IllegalArgumentException("The provider " + var2 + " already exists");
               }
            }
         }

      }
   }
}
