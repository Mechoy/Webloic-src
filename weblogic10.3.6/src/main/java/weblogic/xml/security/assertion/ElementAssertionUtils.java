package weblogic.xml.security.assertion;

import weblogic.xml.security.specs.ElementIdentifier;

/** @deprecated */
public class ElementAssertionUtils {
   static boolean satisfies(String var0, String var1, String var2, ElementIdentifier var3) {
      if (!var0.equals(var3.getLocalName())) {
         return false;
      } else {
         String var4 = var3.getNamespace();
         if (var4 != null && !var4.equals(var1)) {
            return false;
         } else {
            return var2 == null || var2.equals(var3.getRestriction());
         }
      }
   }
}
