package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.security.wsse.internal.BaseToken;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class BinarySecurityTokenImpl extends BaseToken implements BinarySecurityToken, WSSEConstants {
   private final EncodedToken encodedToken;
   private PrivateKey privateKey;

   public BinarySecurityTokenImpl(X509Certificate var1, PrivateKey var2) {
      this(VALUETYPE_X509V3, var1, var2);
   }

   public BinarySecurityTokenImpl(CertPath var1, PrivateKey var2) {
      this(VALUETYPE_PKIPATH, var1, var2);
   }

   public BinarySecurityTokenImpl(X509Certificate[] var1, PrivateKey var2) {
      this(VALUETYPE_PKCS7, var1, var2);
   }

   private BinarySecurityTokenImpl(String var1, Object var2, PrivateKey var3) {
      if (var2 == null) {
         throw new IllegalArgumentException("Received null for certificate: must provide a valid certificate");
      } else {
         this.privateKey = var3;
         this.encodedToken = EncodedToken.createToken(var1, var2);
      }
   }

   public BinarySecurityTokenImpl(XMLInputStream var1, String var2) throws XMLStreamException {
      this.encodedToken = new EncodedCertificate(var1, var2);
   }

   public String getId() {
      return this.encodedToken.getId();
   }

   public void setId(String var1) {
      this.encodedToken.setId(var1);
   }

   public String getEncodedValue() {
      return this.encodedToken.getEncodedValue();
   }

   public String getEncodingType() {
      return this.encodedToken.getEncodingType();
   }

   public String getValueType() {
      return this.encodedToken.getValueType();
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, WSSE_URI, 0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      this.encodedToken.toXML(var1, var2, 0);
   }

   public PublicKey getPublicKey() {
      return this.encodedToken.getPublicKey();
   }

   public PrivateKey getPrivateKey() {
      return this.privateKey;
   }

   public X509Certificate getCertificate() {
      return this.encodedToken.getCertificate();
   }

   public X509Certificate[] getCertificateChain() {
      return this.encodedToken.getCertificateChain();
   }

   public CertPath getCertPath() {
      return this.encodedToken.getCertPath();
   }

   public Key getSecretKey() {
      return this.encodedToken.getSecretKey();
   }

   public KeyIdentifier getKeyIdentifier() {
      return this.encodedToken.getKeyIdentifier();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("BinarySecurityTokenImpl:").append("\n  Id:").append(this.encodedToken.getId()).append("\n  ValueType: ").append(this.encodedToken.getValueType()).append("\n  Encoding: ").append(this.encodedToken.getEncodingType()).append("\n  Value:" + this.getCertificate());
      return var1.toString();
   }
}
