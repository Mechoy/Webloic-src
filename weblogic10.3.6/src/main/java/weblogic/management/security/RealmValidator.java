package weblogic.management.security;

public class RealmValidator {
   public static void validateMaxWebLogicPrincipalsInCache(Integer var0) throws IllegalArgumentException {
      if (var0 <= 0) {
         throw new IllegalArgumentException("Illegal value for MaxWebLogicPrincipalsInCache: " + var0);
      }
   }
}
