package weblogic.xml.security.signature;

import java.security.Key;
import java.security.interfaces.DSAKey;
import java.security.interfaces.RSAKey;
import javax.crypto.SecretKey;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class SignatureMethod implements DSIGConstants {
   public static final String DSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
   public static final String RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
   public static final String HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initFactories() {
      SignatureMethodRSA.init();
      SignatureMethodDSA.init();
      SignatureMethodHMAC.init();
   }

   public abstract String getURI();

   public String toString() {
      return "SignatureMethod:  algorithmURI = " + this.getURI();
   }

   static SignatureMethod get(Key var0) throws XMLSignatureException {
      if (var0 instanceof DSAKey) {
         return get("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
      } else if (var0 instanceof RSAKey) {
         return get("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
      } else if (var0 instanceof SecretKey) {
         return get("http://www.w3.org/2000/09/xmldsig#hmac-sha1");
      } else {
         throw new XMLSignatureException("cannot find algorithm for key");
      }
   }

   public static void register(SignatureMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static SignatureMethod get(String var0) throws XMLSignatureException {
      SignatureMethodFactory var1 = (SignatureMethodFactory)factories.get(var0);
      if (var1 == null) {
         throw new XMLSignatureException(var0 + " not supported");
      } else {
         return var1.newSignatureMethod();
      }
   }

   protected abstract boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException;

   protected abstract String sign(Key var1, byte[] var2) throws XMLSignatureException;

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Algorithm", this.getURI())};
      StreamUtils.addElement(var1, var2, "SignatureMethod", (String)null, var4, var3, 2);
   }

   static SignatureMethod fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      SignatureMethod var2 = null;
      StartElement var3 = (StartElement)StreamUtils.getElement(var0, var1, "SignatureMethod");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Algorithm");
         StreamUtils.requiredAttr(var4, "SignatureMethod", "Algorithm");
         var2 = get(var4);
         var2.fromXMLInternal(var0, var1);
         StreamUtils.closeScope(var0, var1, "SignatureMethod");
      }

      return var2;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>\n");
      SignatureMethod var2 = (SignatureMethod)DSIGReader.read(var1, 9);
      System.out.println(var2);
      System.out.println("Implemented by: " + var2.getClass().getName());
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }

   static {
      initFactories();
   }
}
