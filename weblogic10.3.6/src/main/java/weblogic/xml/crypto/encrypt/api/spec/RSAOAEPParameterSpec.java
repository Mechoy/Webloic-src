package weblogic.xml.crypto.encrypt.api.spec;

import weblogic.xml.crypto.dsig.api.DigestMethod;

public class RSAOAEPParameterSpec implements EncryptionMethodParameterSpec {
   DigestMethod digestMethod;
   byte[] oaepParams;

   public RSAOAEPParameterSpec(DigestMethod var1) {
   }

   public RSAOAEPParameterSpec(DigestMethod var1, byte[] var2) {
      this.digestMethod = var1;
      int var3 = var2.length;
      this.oaepParams = new byte[var3];
      System.arraycopy(var2, 0, var2, 0, var3);
   }

   public DigestMethod getDigestMethod() {
      return this.digestMethod;
   }

   public byte[] getOAEPParams() {
      return this.oaepParams;
   }
}
