package weblogic.xml.security.wsse.v200207;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class EncodedToken extends EncodedString {
   private static Map ENCODED_VALUE_TYPES = new HashMap();

   private static void initFactories() {
      EncodedCertificate.init();
      EncodedCertPath.init();
   }

   static void registerValueType(String var0, Factory var1) {
      ENCODED_VALUE_TYPES.put(var0, var1);
   }

   static EncodedToken createToken(String var0, Object var1) {
      Factory var2 = (Factory)ENCODED_VALUE_TYPES.get(var0);
      if (var2 == null) {
         throw new IllegalArgumentException("Unsupported token type: " + var0);
      } else {
         return var2.newInstance(var1);
      }
   }

   static EncodedToken fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      StartElement var2;
      try {
         var2 = (StartElement)var0.peek();
      } catch (ClassCastException var5) {
         throw new AssertionError(var5);
      }

      String var3 = StreamUtils.getAttribute(var2, "ValueType");
      if (var3 == null) {
         throw new SecurityProcessingException("Unable to find valueType attr for encoded token");
      } else {
         Factory var4 = (Factory)ENCODED_VALUE_TYPES.get(var3);
         return var4.newInstance(var0, var1);
      }
   }

   protected EncodedToken(XMLInputStream var1, String var2) throws XMLStreamException {
      super(var1, var2);
   }

   protected EncodedToken(String var1) {
      super(var1);
   }

   protected final String getElementName() {
      return "BinarySecurityToken";
   }

   protected abstract byte[] getValue();

   abstract Key getSecretKey();

   abstract String getValueType();

   abstract X509Certificate getCertificate();

   abstract X509Certificate[] getCertificateChain();

   abstract CertPath getCertPath();

   KeyIdentifier getKeyIdentifier() {
      return getX509KeyIdentifier(this.getCertificate());
   }

   PublicKey getPublicKey() {
      X509Certificate var1 = this.getCertificate();
      PublicKey var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.getPublicKey();
      }

      return var2;
   }

   protected final void writeAttributes(List var1) throws XMLStreamException {
      var1.add(StreamUtils.createAttribute("ValueType", this.getValueType()));
   }

   protected void readAttributes(StartElement var1, Map var2) throws XMLStreamException {
   }

   protected static KeyIdentifier getX509KeyIdentifier(X509Certificate var0) {
      byte[] var1 = Utils.getSubjectKeyIdentifier(var0);
      KeyIdentifierImpl var2;
      if (var1 != null) {
         var2 = new KeyIdentifierImpl(var1, VALUETYPE_X509SKID);
      } else {
         var2 = null;
      }

      return var2;
   }

   static {
      initFactories();
   }

   protected interface Factory {
      EncodedToken newInstance(XMLInputStream var1, String var2) throws XMLStreamException;

      EncodedToken newInstance(Object var1);
   }
}
