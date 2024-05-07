package weblogic.xml.crypto.encrypt.api;

import java.security.Key;

public class TBEKey extends TBEBase {
   private final Key key;

   public TBEKey(Key var1, String var2, String var3, String var4) {
      super(var2, var3, var4);
      this.key = var1;
   }

   public TBEKey(Key var1) {
      this(var1, (String)null, (String)null, (String)null);
   }

   public Key getKey() {
      return this.key;
   }
}
