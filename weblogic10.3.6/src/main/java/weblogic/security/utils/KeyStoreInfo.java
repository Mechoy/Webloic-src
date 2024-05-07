package weblogic.security.utils;

import java.security.KeyStore;
import java.util.Arrays;

public final class KeyStoreInfo {
   private String filename;
   private String type;
   private char[] passphrase;

   KeyStoreInfo(String var1, String var2, String var3) {
      this.filename = var1;
      this.type = var2 != null && var2.length() > 0 ? var2 : KeyStore.getDefaultType();
      this.passphrase = var3 != null && var3.length() > 0 ? var3.toCharArray() : null;
   }

   public String getFileName() {
      return this.filename;
   }

   public String getType() {
      return this.type;
   }

   public char[] getPassPhrase() {
      return this.passphrase;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof KeyStoreInfo)) {
         return false;
      } else {
         KeyStoreInfo var2 = (KeyStoreInfo)var1;
         return equals(this.filename, var2.filename) && equals(this.type, var2.type) && Arrays.equals(this.passphrase, var2.passphrase);
      }
   }

   private static final boolean equals(String var0, String var1) {
      return var0 == var1 || var0 != null && var0.equals(var1);
   }

   public int hashCode() {
      int var1 = this.filename == null ? 1 : this.filename.hashCode();
      var1 = var1 * 31 + (this.type == null ? 1 : this.type.hashCode());
      var1 = var1 * 31 + Arrays.hashCode(this.passphrase);
      return var1;
   }

   public String toString() {
      return "FileName=" + this.filename + ", Type=" + this.type + ", PassPhraseUsed=" + (this.passphrase != null);
   }
}
