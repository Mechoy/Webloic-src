package weblogic.xml.crypto.dsig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;

class JCEDigestMethod extends DigestMethodImpl implements DigestMethodFactory, DigestMethod {
   private static final String JCE_SHA1 = "SHA-1";
   private static final String JCE_SHA256 = "SHA-256";
   private static final String JCE_SHA512 = "SHA-512";
   private final String jceAlgorithmID;
   private final MessageDigest messageDigest;

   private JCEDigestMethod(String var1, String var2) {
      super(var1);
      this.jceAlgorithmID = var2;
      this.messageDigest = newMessageDigest(var2);
   }

   private static MessageDigest newMessageDigest(String var0) {
      MessageDigest var1 = null;

      try {
         var1 = MessageDigest.getInstance(var0);
      } catch (NoSuchAlgorithmException var3) {
      }

      return var1;
   }

   public static void init() {
      register(new JCEDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", "SHA-1"));
      register(new JCEDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", "SHA-256"));
      register(new JCEDigestMethod("http://www.w3.org/2001/04/xmlenc#sha512", "SHA-512"));
   }

   public MessageDigest getMessageDigest() {
      return this.messageDigest;
   }

   public WLDigestMethod newDigestMethod(DigestMethodParameterSpec var1) throws NoSuchAlgorithmException {
      return new JCEDigestMethod(this.algorithmURI, this.jceAlgorithmID);
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return null;
   }

   public byte[] digest(byte[] var1) {
      this.getMessageDigest().update(var1);
      return this.getMessageDigest().digest();
   }
}
