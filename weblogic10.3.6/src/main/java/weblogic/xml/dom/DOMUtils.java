package weblogic.xml.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public final class DOMUtils {
   public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
   private static final boolean debug = false;

   public static List getValuesByTagName(Element var0, String var1) throws DOMProcessingException {
      List var2 = getElementsByTagName(var0, var1);
      Iterator var3 = var2.iterator();
      ArrayList var4 = new ArrayList(var2.size());

      while(var3.hasNext()) {
         var4.add(getTextData((Element)var3.next()));
      }

      return var4;
   }

   public static List getElementsByTagName(Element var0, String var1) throws DOMProcessingException {
      List var2 = getOptionalElementsByTagName(var0, var1);
      if (var2.size() == 0) {
         throw new ChildCountException(1, var0.getNodeName(), var1, 0);
      } else {
         return var2;
      }
   }

   public static String getOptionalValueByTagName(Element var0, String var1) throws DOMProcessingException {
      Element var2 = getOptionalElementByTagName(var0, var1);
      return var2 == null ? null : getTextData(var2);
   }

   public static String getValueByTagName(Element var0, String var1) throws DOMProcessingException {
      Element var2 = getElementByTagName(var0, var1);
      return getTextData(var2);
   }

   public static String getValueByTagNameNS(Element var0, String var1, String var2) throws DOMProcessingException {
      Element var3 = getElementByTagNameNS(var0, var1, var2);
      return getTextData(var3);
   }

   public static String getOptionalValueByTagNameNS(Element var0, String var1, String var2) throws DOMProcessingException {
      Element var3 = getOptionalElementByTagNameNS(var0, var1, var2);
      return var3 == null ? null : getTextData(var3);
   }

   public static Element getElementByTagName(Element var0, String var1) throws DOMProcessingException {
      Element var2 = getOptionalElementByTagName(var0, var1);
      if (var2 == null) {
         throw new ChildCountException(1, var0.getNodeName(), var1, 0);
      } else {
         return var2;
      }
   }

   public static Element getOptionalElementByTagName(Element var0, String var1) throws DOMProcessingException {
      List var2 = getOptionalElementsByTagName(var0, var1);
      int var3 = var2.size();
      switch (var3) {
         case 0:
            return null;
         case 1:
            return (Element)var2.get(0);
         default:
            throw new ChildCountException(2, var0.getNodeName(), var1, var3);
      }
   }

   public static List getOptionalElementsByLocalName(Element var0, String var1) throws DOMProcessingException {
      NodeList var2 = var0.getChildNodes();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         Node var5 = var2.item(var4);
         if (var5.getNodeType() == 1) {
            Element var6 = (Element)var5;
            if (var6.getLocalName().equals(var1)) {
               var3.add(var6);
            }
         }
      }

      return var3;
   }

   public static Element getOptionalElementByLocalName(Element var0, String var1) throws DOMProcessingException {
      List var2 = getOptionalElementsByLocalName(var0, var1);
      int var3 = var2.size();
      switch (var3) {
         case 0:
            return null;
         case 1:
            return (Element)var2.get(0);
         default:
            throw new ChildCountException(2, var0.getNodeName(), var1, var3);
      }
   }

   public static Element getElementByTagNameNS(Element var0, String var1, String var2) throws DOMProcessingException {
      Element var3 = getOptionalElementByTagNameNS(var0, var1, var2);
      if (var3 == null) {
         throw new ChildCountException(1, var0.getNodeName(), var2, 0);
      } else {
         return var3;
      }
   }

   public static List getOptionalElementsByTagNameNS(Element var0, String var1, String var2) {
      ArrayList var3 = new ArrayList();

      for(Node var4 = var0.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var1.equals(var5.getNamespaceURI()) && var2.equals(var5.getLocalName())) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public static Element getOptionalElementByTagNameNS(Element var0, String var1, String var2) throws DOMProcessingException {
      for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var1.equals(var4.getNamespaceURI()) && var2.equals(var4.getLocalName())) {
               return var4;
            }
         }
      }

      return null;
   }

   public static List getOptionalElementsByTagName(Element var0, String var1) throws DOMProcessingException {
      NodeList var2 = var0.getChildNodes();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         Node var5 = var2.item(var4);
         if (var5.getNodeType() == 1) {
            Element var6 = (Element)var5;
            if (var6.getTagName().equals(var1)) {
               var3.add(var6);
            }
         }
      }

      return var3;
   }

   public static String getTextData(Node var0) throws DOMProcessingException {
      StringBuffer var1 = new StringBuffer(80);
      NodeList var2 = var0.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (var4.getNodeType() == 3 || var4.getNodeType() == 4) {
            Text var5 = (Text)var4;
            var1.append(var5.getData().trim());
         }
      }

      return new String(var1.toString());
   }

   public static List getTextDataValues(NodeList var0) throws DOMProcessingException {
      int var1 = var0.getLength();
      ArrayList var2 = new ArrayList(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.add(getTextData(var0.item(var3)));
      }

      return var2;
   }

   public static List getTextDataValues(List var0) throws DOMProcessingException {
      int var1 = var0.size();
      ArrayList var2 = new ArrayList(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.add(getTextData((Node)var0.get(var3)));
      }

      return var2;
   }

   public static boolean elementIsOneOf(Element var0, String[] var1) {
      String var2 = var0.getTagName();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public static boolean elementHas(Element var0, String var1) {
      return getElementCount(var0, var1) > 0;
   }

   public static int getElementCount(Element var0, String var1) {
      NodeList var2 = var0.getChildNodes();
      int var3 = 0;
      int var4 = 0;

      for(int var5 = var2.getLength(); var4 < var5; ++var4) {
         Node var6 = var2.item(var4);
         if (var6.getNodeType() == 1 && var1.equals(((Element)var6).getTagName())) {
            ++var3;
         }
      }

      return var3;
   }

   public static Element addValue(Element var0, String var1, String var2) {
      Element var3 = var0.getOwnerDocument().createElement(var1);
      var3.appendChild(var0.getOwnerDocument().createTextNode(var2));
      var0.appendChild(var3);
      return var3;
   }

   public static Element addValueNS(Element var0, String var1, String var2, String var3) {
      Element var4 = var0.getOwnerDocument().createElementNS(var1, var2);
      var4.appendChild(var0.getOwnerDocument().createTextNode(var3));
      var0.appendChild(var4);
      return var4;
   }

   public static void addNamespaceDeclaration(Element var0, String var1, String var2) {
      addNamespaceDeclaration(var0, var1, var2, true);
   }

   public static void addNamespaceDeclaration(Element var0, String var1, String var2, boolean var3) {
      var0.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var1, var2);
   }

   public static String getNamespaceURI(Element var0, String var1) {
      return var0.getAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var1);
   }

   public static void setDefaultNamespace(Element var0, String var1) {
      var0.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", var1);
   }

   public static void addTextData(Element var0, String var1) {
      var0.appendChild(var0.getOwnerDocument().createTextNode(var1));
   }

   public static void addEmptyElement(Element var0, String var1) {
      var0.appendChild(var0.getOwnerDocument().createElement(var1));
   }

   public static void copyNodes(Element var0, NodeList var1) throws DOMProcessingException {
      for(int var2 = 0; var2 < var1.getLength(); ++var2) {
         var0.appendChild(var0.getOwnerDocument().importNode(var1.item(var2), true));
      }

   }

   public static boolean isNameSpaceUriEmpty(Node var0) {
      String var1 = var0.getNamespaceURI();
      return var1 == null || var1.length() == 0;
   }

   public static boolean isNameSpaceUriEmpty(QName var0) {
      String var1 = var0.getNamespaceURI();
      return var1 == null || var1.length() == 0;
   }

   public static String getAttributeValueAsString(Element var0, QName var1) {
      String var2 = var1.getNamespaceURI();
      String var3 = null;
      if (var2 != null && var2.length() > 0) {
         var3 = var0.getAttributeNS(var2, var1.getLocalPart());
      } else {
         var3 = var0.getAttribute(var1.getLocalPart());
      }

      return var3;
   }

   public static boolean equalsQName(Node var0, QName var1) {
      if (!var0.getLocalName().equals(var1.getLocalPart())) {
         return false;
      } else {
         boolean var2 = isNameSpaceUriEmpty(var0);
         boolean var3 = isNameSpaceUriEmpty(var1);
         if (var2) {
            return var3;
         } else {
            return var3 ? var2 : var0.getNamespaceURI().equals(var1.getNamespaceURI());
         }
      }
   }

   public static Element getFirstElement(Node var0, QName var1) {
      if (var0.getNodeType() == 1 && equalsQName(var0, var1)) {
         return (Element)var0;
      } else {
         for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            Element var3 = getFirstElement(var2, var1);
            if (var3 != null) {
               return var3;
            }
         }

         return null;
      }
   }

   public static String getTextContent(Element var0, boolean var1) {
      StringBuffer var2 = new StringBuffer();
      NodeList var3 = var0.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 3 || var5.getNodeType() == 4) {
            Text var6 = (Text)var5;
            if (var1) {
               var2.append(var6.getData().trim());
            } else {
               var2.append(var6.getData());
            }
         }
      }

      return new String(var2.toString());
   }
}
