package weblogic.xml.security.encryption;

import java.util.Locale;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class EncryptionMethod implements XMLEncConstants {
   public static final String TRIPLEDES_CBC = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   public static final String AES128_CBC = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
   public static final String AES192_CBC = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
   public static final String AES256_CBC = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
   public static final String KW_TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
   public static final String KW_AES128 = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
   public static final String KW_AES192 = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
   public static final String KW_AES256 = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
   public static final String KW_RSA_1_5 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   public static final String KW_RSA_OAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();
   private static final ConcurrentHashMap keyWraps = new ConcurrentHashMap();

   private static final void initFactories() {
      EncryptionAlgorithmDES3.init();
      EncryptionAlgorithmAES.init();
      KeyWrapDES3.init();
      KeyWrapAES.init();
      KeyWrapRSA.init();
      KeyWrapRSAOAEP.init();
   }

   public abstract String getURI();

   public String toString() {
      return "EncryptionMethod:  algorithmURI = " + this.getURI();
   }

   public static void register(EncryptionMethodFactory var0) {
      factories.put(var0.getURI(), var0);
      if (var0 instanceof KeyWrapFactory) {
         registerKeyWrap((KeyWrapFactory)var0);
      }

   }

   private static void registerKeyWrap(KeyWrapFactory var0) {
      keyWraps.put(var0.getAlgorithm().toLowerCase(Locale.ENGLISH), var0);
   }

   public static EncryptionMethod get(String var0) throws EncryptionException {
      EncryptionMethodFactory var1 = (EncryptionMethodFactory)factories.get(var0);
      if (var1 == null) {
         throw new EncryptionException(var0 + " not supported");
      } else {
         return var1.newEncryptionMethod();
      }
   }

   static KeyWrap getKeyWrap(String var0) throws EncryptionException {
      KeyWrapFactory var1 = (KeyWrapFactory)keyWraps.get(var0.toLowerCase(Locale.ENGLISH));
      if (var1 == null) {
         throw new EncryptionException(var0 + " not supported");
      } else {
         return var1.newKeyWrap();
      }
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Algorithm", this.getURI())};
      StreamUtils.addStart(var1, var2, "EncryptionMethod", var4, var3);
      this.toXMLInternal(var1, var2, var3);
      StreamUtils.addEnd(var1, var2, "EncryptionMethod", var3);
   }

   protected void toXMLInternal(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
   }

   static EncryptionMethod fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      EncryptionMethod var2 = null;
      StartElement var3 = (StartElement)StreamUtils.getElement(var0, var1, "EncryptionMethod");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Algorithm");
         StreamUtils.requiredAttr(var4, "EncryptionMethod", "Algorithm");
         var2 = get(var4);
         StreamUtils.getElement(var0, var1, "KeySize");
         var2.fromXMLInternal(var0, var1);
         StreamUtils.closeScope(var0, var1, "EncryptionMethod");
      }

      return var2;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<EncryptionMethod xmlns=\"http://www.w3.org/2001/04/xmlenc#\" Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\"/>\n");
      EncryptionMethod var2 = fromXML(var1, "http://www.w3.org/2001/04/xmlenc#");
      System.out.println(var2);
      System.out.println("Implemented by: " + var2.getClass().getName());
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2001/04/xmlenc#", 0);
      var3.flush();
   }

   static {
      initFactories();
   }
}
