package weblogic.xml.security.signature;

import java.io.OutputStream;
import java.util.Map;
import weblogic.utils.Debug;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.schema.types.XSDNMTokens;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class CanonicalizationMethod implements DSIGConstants {
   public static final String W3C = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
   public static final String W3C_WC = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
   public static final String EXC = "http://www.w3.org/2001/10/xml-exc-c14n#";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();
   private String[] inclusiveNamespaces = null;

   private static final void initFactories() {
      CanonicalizationMethodW3C.init();
   }

   public abstract String getURI();

   public String toString() {
      return "CanonicalizationMethod:  algorithmURI = " + this.getURI();
   }

   public static void register(CanonicalizationMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static CanonicalizationMethod get(String var0) throws XMLSignatureException {
      CanonicalizationMethodFactory var1 = (CanonicalizationMethodFactory)factories.get(var0);
      if (var1 == null) {
         throw new XMLSignatureException(var0 + " not supported");
      } else {
         return var1.newCanonicalizationMethod();
      }
   }

   public abstract XMLOutputStream canonicalize(OutputStream var1, Map var2);

   protected String[] getInclusiveNamesspacesPrefixList() {
      return this.inclusiveNamespaces;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Algorithm", this.getURI())};
      StreamUtils.addElement(var1, var2, "CanonicalizationMethod", (String)null, var4, var3, 2);
   }

   static CanonicalizationMethod fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      CanonicalizationMethod var2 = null;
      StartElement var3 = (StartElement)StreamUtils.getElement(var0, var1, "CanonicalizationMethod");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Algorithm");
         StreamUtils.requiredAttr(var4, "CanonicalizationMethod", "Algorithm");
         var2 = get(var4);
         String[] var5 = getInclusiveNamesSpacesFromXml(var0, var1);
         var2.inclusiveNamespaces = var5;
         StreamUtils.closeScope(var0, var1, "CanonicalizationMethod");
      }

      return var2;
   }

   protected static String[] getInclusiveNamesSpacesFromXml(XMLInputStream var0, String var1) throws XMLStreamException {
      String[] var2 = null;
      XMLEvent var3 = StreamUtils.peekElement(var0);
      if (StreamUtils.matches(var3, "InclusiveNamespaces")) {
         XMLEvent var4 = var0.next();
         String var5 = StreamUtils.getAttributeByName("PrefixList", (String)null, (StartElement)var4);
         if (var5 != null) {
            var2 = XSDNMTokens.convertXml(var5, "#default");
            if (VERBOSE) {
               print(var2);
            }
         }

         StreamUtils.closeScope(var0, "InclusiveNamespaces");
      }

      return var2;
   }

   private static void print(String[] var0) {
      String var1 = "";

      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var2 != 0) {
            var1 = var1 + " ";
         }

         var1 = var1 + var0[var2];
      }

      Debug.say(" +++ - " + var1 + " - ");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<CanonicalizationMethod xmlns=\"http://www.w3.org/2000/09/xmldsig#\" Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n");
      CanonicalizationMethod var2 = (CanonicalizationMethod)DSIGReader.read(var1, 1);
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
