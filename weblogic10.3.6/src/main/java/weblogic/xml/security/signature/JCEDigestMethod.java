package weblogic.xml.security.signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import weblogic.xml.security.encryption.XMLEncConstants;
import weblogic.xml.security.utils.XMLSecurityException;

public class JCEDigestMethod extends DigestMethod implements DigestMethodFactory, DSIGConstants, XMLEncConstants {
   public static final String URI_SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
   public static final String URI_SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
   public static final String URI_SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
   private static final String JCE_SHA1 = "SHA-1";
   private static final String JCE_SHA256 = "SHA-256";
   private static final String JCE_SHA512 = "SHA-512";
   private final String algorithmURI;
   private final String jceAlgorithmID;
   private final MessageDigest messageDigest;

   private JCEDigestMethod(String var1, String var2) throws XMLSecurityException {
      this.algorithmURI = var1;
      this.jceAlgorithmID = var2;

      try {
         this.messageDigest = MessageDigest.getInstance(var2);
      } catch (NoSuchAlgorithmException var4) {
         throw new XMLSecurityException("No such algorithm: " + var2);
      }
   }

   public static void init() {
      try {
         register(new JCEDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", "SHA-1"));
      } catch (XMLSecurityException var1) {
      }

   }

   public DigestMethod newDigestMethod() throws XMLSecurityException {
      return new JCEDigestMethod(this.algorithmURI, this.jceAlgorithmID);
   }

   public MessageDigest getMessageDigest() {
      return this.messageDigest;
   }

   public String getURI() {
      return this.algorithmURI;
   }
}
