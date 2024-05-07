package weblogic.xml.security.signature;

import java.security.MessageDigest;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.security.utils.XMLSecurityException;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class DigestMethod implements DSIGConstants {
   public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
   public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
   public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initFactories() {
      JCEDigestMethod.init();
   }

   public abstract MessageDigest getMessageDigest();

   public abstract String getURI();

   public String toString() {
      return "DigestMethod:  algorithmURI = " + this.getURI();
   }

   public static void register(DigestMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static DigestMethod get(String var0) throws XMLSecurityException {
      DigestMethodFactory var1 = (DigestMethodFactory)factories.get(var0);
      if (var1 == null) {
         throw new XMLSecurityException(var0 + " not supported");
      } else {
         return var1.newDigestMethod();
      }
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Algorithm", this.getURI())};
      StreamUtils.addElement(var1, var2, "DigestMethod", (String)null, var4, var3, 2);
   }

   static DigestMethod fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      DigestMethod var2 = null;
      StartElement var3 = (StartElement)StreamUtils.getElement(var0, var1, "DigestMethod");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Algorithm");
         StreamUtils.requiredAttr(var4, "DigestMethod", "Algorithm");
         var2 = get(var4);
         var2.fromXMLInternal(var0, var1);
         StreamUtils.closeScope(var0, var1, "DigestMethod");
      }

      return var2;
   }

   public void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n");
      DigestMethod var2 = fromXML(var1, "http://www.w3.org/2000/09/xmldsig#");
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
