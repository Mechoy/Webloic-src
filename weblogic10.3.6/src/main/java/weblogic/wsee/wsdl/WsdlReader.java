package weblogic.wsee.wsdl;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.dom.DOMParser;

public final class WsdlReader {
   private static final QName DEFAULT_NAMESPACE_ATTR = new QName("http://www.w3.org/2000/xmlns/", "xmlns");

   public static boolean isDocumentation(Node var0) {
      return var0.getNodeType() != 1 ? false : "documentation".equals(var0.getLocalName());
   }

   public static boolean isWhiteSpace(Node var0) {
      if (var0.getNodeType() == 8) {
         return true;
      } else {
         if (var0.getNodeType() == 3) {
            String var1 = var0.getNodeValue();
            if (var1 == null || "".equals(var1.trim())) {
               return true;
            }
         }

         return false;
      }
   }

   public static void checkWsdlNamespace(Element var0) throws WsdlException {
      String var1 = var0.getNamespaceURI();
      if (!WsdlConstants.wsdlNS.equals(var1)) {
         throw new WsdlException("Found an element with unexpected namespace '" + var1 + "' . Was expecting '" + WsdlConstants.wsdlNS + "'");
      }
   }

   public static void checkWsdlDefinitions(String var0, Element var1) throws WsdlException {
      if (!var0.equals(var1.getLocalName())) {
         throw new WsdlException("The XML document specified is not a valid WSDL document. The name of the top level element should be '" + var0 + "' but found '" + var1.getLocalName() + "'");
      }
   }

   public static void checkDomElement(Node var0) throws WsdlException {
      if (var0.getNodeType() != 1) {
         throw new WsdlException("Found an un expeced Node " + var0.getNodeName() + " with name = " + var0.getLocalName() + " and with text content = " + var0.getTextContent());
      }
   }

   public static String getMustAttribute(Element var0, String var1, String var2) throws WsdlException {
      String var3 = getAttribute(var0, var1, var2);
      if (var3 != null && !"".equals(var3)) {
         return var3;
      } else {
         throw new WsdlException("Must attribute '" + var2 + "' not" + "found in element '" + var0.getLocalName());
      }
   }

   public static String getAttribute(Element var0, String var1, String var2, boolean var3) {
      String var4;
      if (var1 == null) {
         var4 = var0.getAttribute(var2);
      } else {
         var4 = var0.getAttributeNS(var1, var2);
      }

      if (var4 == null) {
         return null;
      } else if ("".equals(var4)) {
         return var3 ? "" : null;
      } else {
         return var4;
      }
   }

   public static String getAttribute(Element var0, String var1, String var2) {
      return getAttribute(var0, var1, var2, false);
   }

   public static QName createQName(Element var0, String var1) throws WsdlException {
      int var2 = var1.indexOf(":");
      String var3;
      if (var2 == -1) {
         var3 = getDefaultNamespace(var0);
         return var3 == null ? new QName(var1) : new QName(var3, var1);
      } else {
         var3 = var1.substring(0, var2);
         String var4 = getNamespace(var3, var0);
         if (var4 == null) {
            throw new WsdlException("Unable to find namespace for prefix '" + var3 + "'. This is used in element " + var0);
         } else {
            return new QName(var4, var1.substring(var2 + 1, var1.length()));
         }
      }
   }

   private static String getNamespace(String var0, Element var1) {
      String var2 = null;

      do {
         var2 = var1.getAttribute("xmlns:" + var0);
         if (var2 != null && !var2.equals("")) {
            return var2;
         }

         Node var3 = var1.getParentNode();
         if (var3 instanceof Element) {
            var1 = (Element)var3;
         } else {
            var1 = null;
         }
      } while(var1 != null);

      return null;
   }

   private static String getDefaultNamespace(Element var0) {
      String var1 = null;

      do {
         if (var0.hasAttributeNS(DEFAULT_NAMESPACE_ATTR.getNamespaceURI(), DEFAULT_NAMESPACE_ATTR.getLocalPart())) {
            var1 = var0.getAttributeNS(DEFAULT_NAMESPACE_ATTR.getNamespaceURI(), DEFAULT_NAMESPACE_ATTR.getLocalPart());
            if (var1.length() == 0) {
               return null;
            }

            return var1;
         }

         Node var2 = var0.getParentNode();
         if (var2 instanceof Element) {
            var0 = (Element)var2;
         } else {
            var0 = null;
         }
      } while(var0 != null);

      return null;
   }

   public static boolean tagEquals(Element var0, String var1, String var2) {
      if (!var1.equals(var0.getLocalName())) {
         return false;
      } else if (var2 == null) {
         return var0.getNamespaceURI() == null;
      } else {
         return var2.equals(var0.getNamespaceURI());
      }
   }

   public static Document getDocument(String var0) throws WsdlException {
      return getDocument((TransportInfo)null, var0);
   }

   public static Document getDocument(TransportInfo var0, String var1) throws WsdlException {
      try {
         return DOMParser.getDocument(var0, var1);
      } catch (IOException var3) {
         throw new WsdlException("Failed to read wsdl file from url due to -- " + var3, var3);
      }
   }

   public static Document getDocument(InputSource var0) throws WsdlException {
      try {
         return DOMParser.getDocument(var0);
      } catch (IOException var2) {
         throw new WsdlException("Failed to read wsdl file from InputSource due to -- " + var2, var2);
      }
   }
}
