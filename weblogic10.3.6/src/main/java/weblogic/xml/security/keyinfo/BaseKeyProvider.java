package weblogic.xml.security.keyinfo;

import java.math.BigInteger;

public abstract class BaseKeyProvider implements KeyProvider {
   private final byte[] identifier;
   private final String name;
   private final String uri;

   protected BaseKeyProvider(String var1, byte[] var2, String var3) {
      this.name = var1;
      this.identifier = var2;
      this.uri = var3;
   }

   public KeyResult getKeyByIdentifier(byte[] var1, String var2, KeyPurpose var3) {
      return Utils.matches(var1, this.identifier) ? this.getKey(var2, var3) : null;
   }

   public KeyResult getKeyByName(String var1, String var2, KeyPurpose var3) {
      return var1 != null && var1.equals(this.name) ? this.getKey(var2, var3) : null;
   }

   public KeyResult getKeyByURI(String var1, String var2, KeyPurpose var3) {
      return var1 != null && var1.equals(this.uri) ? this.getKey(var2, var3) : null;
   }

   public KeyResult getKeyBySubjectName(String var1, String var2, KeyPurpose var3) {
      return null;
   }

   public KeyResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeyPurpose var4) {
      return null;
   }

   public byte[] getIdentifier() {
      return this.identifier;
   }

   public String getName() {
      return this.name;
   }

   public String getUri() {
      return this.uri;
   }

   public String toString() {
      return "weblogic.xml.security.keyinfo.BaseKeyProvider{identifier=" + (this.identifier == null ? null : "length:" + this.identifier.length) + ", name='" + this.name + "'" + ", uri='" + this.uri + "'" + "}";
   }
}
