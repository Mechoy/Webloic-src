package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import weblogic.xml.security.utils.Utils;

public class X509Credential implements Serializable {
   private static final long serialVersionUID = 5618358587801780416L;
   private X509Certificate cert;
   private transient PrivateKey key;
   private byte[] keyIdentifier;
   private CertPath certPath;

   public X509Credential(X509Certificate var1) {
      this((X509Certificate)var1, (PrivateKey)null);
   }

   public X509Credential(X509Certificate var1, PrivateKey var2) {
      this.cert = null;
      this.key = null;
      this.keyIdentifier = null;
      this.certPath = null;
      this.cert = var1;
      this.key = var2;
   }

   public X509Credential(CertPath var1) {
      this.cert = null;
      this.key = null;
      this.keyIdentifier = null;
      this.certPath = null;
      this.certPath = var1;
      this.cert = (X509Certificate)var1.getCertificates().get(0);
   }

   public X509Credential(CertPath var1, PrivateKey var2) {
      this(var1);
      this.key = var2;
   }

   public X509Credential(byte[] var1) {
      this.cert = null;
      this.key = null;
      this.keyIdentifier = null;
      this.certPath = null;
      this.keyIdentifier = var1;
   }

   public X509Certificate getCertificate() {
      return this.cert;
   }

   public PrivateKey getPrivateKey() {
      return this.key;
   }

   public byte[] getKeyIdentifier() {
      if (this.keyIdentifier != null) {
         return this.keyIdentifier;
      } else {
         if (this.cert != null) {
            this.keyIdentifier = Utils.getSubjectKeyIdentifier(this.cert);
         }

         return this.keyIdentifier;
      }
   }

   public CertPath getCertPath() {
      if (this.certPath != null) {
         return this.certPath;
      } else {
         if (this.cert != null) {
            X509Certificate[] var1 = new X509Certificate[]{this.cert};
            this.certPath = Utils.generateCertPath(var1);
         }

         return this.certPath;
      }
   }

   public static boolean matches(X509Credential var0, X509Credential var1) {
      CertPath var2 = var0.getCertPath();
      if (var2 != null) {
         return var2.equals(var1.getCertPath());
      } else {
         X509Certificate var3 = var0.getCertificate();
         return var3.equals(var1.getCertificate());
      }
   }
}
