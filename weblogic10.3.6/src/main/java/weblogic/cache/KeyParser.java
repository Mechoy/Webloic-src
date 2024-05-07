package weblogic.cache;

import java.util.NoSuchElementException;

public class KeyParser {
   private String key;
   private String keyScope;

   public KeyParser(String var1) {
      int var2 = var1.indexOf(".");
      if (var2 == -1) {
         this.key = var1;
         this.keyScope = "any";
      } else {
         this.key = var1.substring(var2 + 1);
         this.keyScope = var1.substring(0, var2);
         if (this.key.length() == 0 || this.keyScope.length() == 0) {
            throw new NoSuchElementException("Invalid string attribute key = " + var1);
         }
      }

   }

   public String getKey() {
      return this.key;
   }

   public String getKeyScope() {
      return this.keyScope;
   }
}
