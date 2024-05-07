package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class EncodedCertPath extends EncodedToken {
   private CertPath certPath;
   private String encoding;

   public EncodedCertPath(XMLInputStream var1, String var2) throws XMLStreamException {
      super(var1, var2);
   }

   public EncodedCertPath(CertPath var1) {
      this(var1, VALUETYPE_PKIPATH);
   }

   public EncodedCertPath(CertPath var1, String var2) {
      super(ENCODING_BASE64);
      this.certPath = var1;
      this.validateEncoding(var2);
      this.encoding = var2;
   }

   public void setEncoding(String var1) {
      this.validateEncoding(var1);
      this.encoding = var1;
   }

   private void validateEncoding(String var1) {
      if (!var1.equals(VALUETYPE_PKIPATH) && !var1.equals(VALUETYPE_PKCS7)) {
         throw new IllegalArgumentException(var1 + " is not a legal encoding " + "for WS Security");
      }
   }

   protected byte[] getValue() {
      try {
         return this.certPath.getEncoded(this.encoding);
      } catch (CertificateEncodingException var2) {
         throw new AssertionError("Unable to encode certpath: " + var2);
      }
   }

   Key getSecretKey() {
      return null;
   }

   X509Certificate getCertificate() {
      return (X509Certificate)this.getCertPath().getCertificates().get(0);
   }

   X509Certificate[] getCertificateChain() {
      List var1 = this.getCertPath().getCertificates();
      X509Certificate[] var2 = new X509Certificate[var1.size()];
      return (X509Certificate[])((X509Certificate[])var1.toArray(var2));
   }

   CertPath getCertPath() {
      if (this.certPath == null) {
         this.decodeCertPath();
      }

      return this.certPath;
   }

   private void decodeCertPath() {
      UnsyncByteArrayInputStream var1 = new UnsyncByteArrayInputStream(this.getDecodedValue());

      try {
         this.certPath = Utils.getCertFactory().generateCertPath(var1, this.encoding);
      } catch (CertificateException var3) {
         throw new AssertionError("Unable to decode certificate: " + var3);
      }
   }

   String getValueType() {
      return this.encoding.equals(VALUETYPE_PKIPATH) ? VALUETYPE_PKIPATH : VALUETYPE_PKCS7;
   }

   static void init() {
      EncodedToken.registerValueType(VALUETYPE_PKIPATH, new EncodedToken.Factory() {
         public EncodedToken newInstance(XMLInputStream var1, String var2) throws XMLStreamException {
            return new EncodedCertPath(var1, var2);
         }

         public EncodedToken newInstance(Object var1) {
            if (!(var1 instanceof CertPath)) {
               throw new IllegalArgumentException("Cannot make PKIPath token from " + var1);
            } else {
               return new EncodedCertPath((CertPath)var1);
            }
         }
      });
      EncodedToken.registerValueType(VALUETYPE_PKCS7, new EncodedToken.Factory() {
         public EncodedToken newInstance(XMLInputStream var1, String var2) throws XMLStreamException {
            EncodedCertPath var3 = new EncodedCertPath(var1, var2);
            var3.setEncoding(WSSEConstants.VALUETYPE_PKCS7);
            return var3;
         }

         public EncodedToken newInstance(Object var1) {
            EncodedCertPath var2;
            if (var1 instanceof CertPath) {
               var2 = new EncodedCertPath((CertPath)var1, WSSEConstants.VALUETYPE_PKCS7);
            } else {
               if (!(var1 instanceof X509Certificate[])) {
                  throw new IllegalArgumentException("Cannot make PKCS7 token from " + var1);
               }

               X509Certificate[] var3 = (X509Certificate[])((X509Certificate[])var1);
               CertPath var4 = Utils.generateCertPath(var3);
               var2 = new EncodedCertPath(var4, WSSEConstants.VALUETYPE_PKCS7);
            }

            return var2;
         }
      });
   }
}
