package weblogic.xml.security.keyinfo;

import weblogic.xml.security.utils.XMLSecurityException;

public class KeyResolverException extends XMLSecurityException {
   private final KeyInfo keyInfo;

   public KeyResolverException(String var1, Throwable var2, KeyInfo var3) {
      super(var1, var2);
      this.keyInfo = var3;
   }

   public KeyResolverException(String var1, KeyInfo var2) {
      this(var1, (Throwable)null, var2);
   }

   public KeyResolverException(Throwable var1, KeyInfo var2) {
      this(var1 != null ? var1.getLocalizedMessage() : null, var1, var2);
   }

   public KeyInfo getKeyInfo() {
      return this.keyInfo;
   }

   public String getMessage() {
      return super.getMessage() + " using " + this.keyInfo;
   }
}
