package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class EncodedCertificate extends EncodedToken {
   private static final String valueType;
   private X509Certificate certificate;

   EncodedCertificate(X509Certificate var1) {
      super(ENCODING_BASE64);
      if (var1 == null) {
         throw new IllegalArgumentException("Certificate cannot be null");
      } else {
         this.certificate = var1;
      }
   }

   EncodedCertificate(XMLInputStream var1, String var2) throws XMLStreamException {
      super(var1, var2);
   }

   protected final byte[] getValue() {
      if (this.certificate != null) {
         try {
            return this.certificate.getEncoded();
         } catch (CertificateEncodingException var2) {
            throw new AssertionError("Unable to encode certificate:" + this.certificate);
         }
      } else {
         return null;
      }
   }

   X509Certificate getCertificate() {
      if (this.certificate == null) {
         UnsyncByteArrayInputStream var1 = new UnsyncByteArrayInputStream(this.getDecodedValue());

         try {
            this.certificate = (X509Certificate)Utils.getCertFactory().generateCertificate(var1);
         } catch (CertificateException var3) {
            throw new AssertionError("Unable to decode certificate: " + var3);
         }
      }

      return this.certificate;
   }

   X509Certificate[] getCertificateChain() {
      X509Certificate[] var1 = new X509Certificate[]{this.getCertificate()};
      return var1;
   }

   CertPath getCertPath() {
      ArrayList var1 = new ArrayList();
      var1.add(this.certificate);
      return Utils.generateCertPath((List)var1);
   }

   final String getValueType() {
      return valueType;
   }

   Key getSecretKey() {
      return null;
   }

   static void init() {
      EncodedToken.registerValueType(VALUETYPE_X509V3, new EncodedToken.Factory() {
         public EncodedToken newInstance(XMLInputStream var1, String var2) throws XMLStreamException {
            return new EncodedCertificate(var1, var2);
         }

         public EncodedToken newInstance(Object var1) {
            if (!(var1 instanceof X509Certificate)) {
               throw new IllegalArgumentException("Cannot encode " + var1 + " as X509 certificate");
            } else {
               return new EncodedCertificate((X509Certificate)var1);
            }
         }
      });
   }

   static {
      valueType = VALUETYPE_X509V3;
   }
}
