package weblogic.xml.security.keyinfo;

import java.security.Key;

public class KeyResult {
   private final Key key;

   public KeyResult(Key var1) {
      this.key = var1;
   }

   public Key getKey() {
      return this.key;
   }
}
