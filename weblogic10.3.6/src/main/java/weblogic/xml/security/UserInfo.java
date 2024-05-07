package weblogic.xml.security;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import weblogic.xml.security.utils.Utils;

/** @deprecated */
public class UserInfo {
   private final byte[] username;
   private final byte[] password;
   private final byte[] passwordDigest;
   private final String nonce;
   private final String created;

   public UserInfo(String var1) {
      this.username = var1.getBytes();
      this.password = null;
      this.nonce = null;
      this.created = null;
      this.passwordDigest = null;
   }

   public UserInfo(String var1, String var2) {
      this.username = var1.getBytes();
      this.password = var2.getBytes();
      this.nonce = null;
      this.created = null;
      this.passwordDigest = null;
   }

   public UserInfo(String var1, byte[] var2, String var3, String var4) {
      this.username = var1.getBytes();
      this.passwordDigest = var2;
      this.password = null;
      this.nonce = var3;
      this.created = var4;
   }

   public String getUsername() {
      return new String(this.username);
   }

   public String getPassword() {
      return new String(this.password);
   }

   public byte[] getPasswordDigest() {
      return this.passwordDigest;
   }

   public boolean verifyPassword(String var1) throws NoSuchAlgorithmException {
      if (var1 != null) {
         return this.getPassword().equals(var1);
      } else if (this.passwordDigest == null) {
         return var1 == null;
      } else {
         return Arrays.equals(this.passwordDigest, Utils.passwordDigest(this.nonce, this.created, var1));
      }
   }
}
