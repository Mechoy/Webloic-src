package weblogic.xml.security.transforms;

import java.util.Map;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public abstract class Transform implements DSIGConstants {
   protected static final XMLOutputStreamFactory xfactory = XMLOutputStreamFactory.newInstance();
   private static final String W3 = "http://www.w3.org";
   public static final String XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   public static final String W3C_C14N = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
   public static final String W3C_C14N_WC = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
   public static final String W3C_EXC_C14N = "http://www.w3.org/2001/10/xml-exc-c14n#";
   public static final String W3C_EXC_C14N_WC = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
   public static final String ENVELOPED = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";

   public static Transform getTransform(String var0) throws TransformException {
      if (var0.equals("http://www.w3.org/TR/1999/REC-xpath-19991116")) {
         return new XPathTransform();
      } else if (var0.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) {
         return new C14NTransform(false);
      } else if (var0.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")) {
         return new C14NTransform(true);
      } else if (var0.equals("http://www.w3.org/2000/09/xmldsig#enveloped-signature")) {
         return new EnvelopedSignatureTransform();
      } else if (var0.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) {
         return new ExcC14NTransform(false);
      } else if (var0.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) {
         return new ExcC14NTransform(true);
      } else {
         throw new TransformException("Unknown Transform: " + var0);
      }
   }

   public abstract String getURI();

   public void setParameter(String var1, String var2) throws TransformException {
   }

   public abstract void setNamespaces(Map var1);

   public abstract XMLOutputStream getXMLOutputStream() throws XMLStreamException;

   public final void setDest(Transform var1) throws IncompatibleTransformException {
      if (var1 instanceof NodeTransform) {
         this.setDest((NodeTransform)var1);
      } else {
         this.setDest((OctetTransform)var1);
      }

   }

   protected abstract void setDest(NodeTransform var1) throws IncompatibleTransformException;

   protected abstract void setDest(OctetTransform var1) throws IncompatibleTransformException;

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Algorithm", this.getURI())};
      StreamUtils.addStart(var1, var2, "Transform", var4, var3);
      this.toXMLInternal(var1, var2, var3);
      StreamUtils.addEnd(var1, var2, "Transform", var3);
   }

   static Transform fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      Transform var2 = null;
      StartElement var3 = (StartElement)StreamUtils.getElement(var0, var1, "Transform");
      if (var3 != null) {
         String var4 = StreamUtils.getAttribute(var3, "Algorithm");
         StreamUtils.requiredAttr(var4, "Transform", "Algorithm");
         var2 = getTransform(var4);
         var2.fromXMLInternal(var0, var1);
         StreamUtils.closeScope(var0, var1, "Transform");
      }

      return var2;
   }

   protected void toXMLInternal(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<Transform Algorithm=\"http://www.w3.org/TR/1999/REC-xpath-19991116\">\n  <XPath>\n    self::text()\n  </XPath>\n</Transform>\n");
      Transform var2 = fromXML(var1, "http://www.w3.org/2000/09/xmldsig#");
      System.out.println(var2);
      System.out.println("Implemented by: " + var2.getClass().getName());
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
