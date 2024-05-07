package weblogic.xml.security;

import javax.crypto.SecretKey;
import weblogic.xml.security.encryption.ReferenceList;

/** @deprecated */
public class NamedKey {
   private final SecretKey key;
   private final String name;
   private final byte[] identifier;
   private transient ReferenceList referenceList;

   public NamedKey(SecretKey var1, String var2) {
      this.key = var1;
      this.name = var2;
      this.identifier = null;
   }

   public NamedKey(SecretKey var1, byte[] var2) {
      this.key = var1;
      this.identifier = var2;
      this.name = null;
   }

   public byte[] getIdentifier() {
      return this.identifier;
   }

   public void setReferenceList(ReferenceList var1) {
      this.referenceList = var1;
   }

   public ReferenceList getReferenceList() {
      return this.referenceList;
   }

   public String getName() {
      return this.name;
   }

   public SecretKey getKey() {
      return this.key;
   }

   public NamedKey copy(ReferenceList var1) {
      NamedKey var2;
      if (this.identifier == null) {
         var2 = new NamedKey(this.key, this.name);
      } else {
         var2 = new NamedKey(this.key, this.identifier);
      }

      var2.setReferenceList(var1);
      return var2;
   }
}
