package weblogic.wsee.security.wst.binding;

import java.io.Serializable;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.dom.marshal.WLDOMStructure;

public abstract class TrustDOMStructure implements WLDOMStructure, Serializable {
   private static final long serialVersionUID = -1594356277339777383L;
   protected String namespaceUri = "ERROR !  default WS-Trust Namespace from weblogic.wsee.security.wst.binding.TrustDOMStructure is in use. The WS-Trust must be strictly determinded.  This needs to be fixed!";
   protected String prefix = "wst";

   public String getNamespaceURI() {
      return this.namespaceUri;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      createNamespacePrefix(var3, this.namespaceUri, this.prefix);
      Element var4 = createElement(var1, new QName(this.namespaceUri, this.getName()), this.prefix);
      this.marshalContents(var4, var3);
      if (var2 != null) {
         var1.insertBefore(var4, var2);
      } else {
         var1.appendChild(var4);
      }

   }

   public void unmarshal(Node var1) throws MarshalException {
      if (!var1.getLocalName().equals(this.getName())) {
         throw new MarshalException("Expected to unmarshal " + this.getName() + ", but unmarshalling " + var1.getLocalName());
      } else if (!(var1 instanceof Element)) {
         throw new MarshalException(var1.getLocalName() + " is not an element");
      } else {
         this.unmarshalContents((Element)var1);
      }
   }

   public abstract void marshalContents(Element var1, Map var2) throws MarshalException;

   public abstract void unmarshalContents(Element var1) throws MarshalException;

   public abstract String getName();

   static final String createNamespacePrefix(Map var0, String var1, String var2) {
      String var3 = (String)var0.get(var1);
      if (var3 == null) {
         if (var2 == null) {
            var3 = "p";
         } else {
            var3 = var2;
         }

         for(int var4 = 0; var0.containsValue(var3); ++var4) {
            var3 = var3 + var4;
         }

         var0.put(var1, var3);
      }

      return var3;
   }

   static void setAttribute(Element var0, String var1, String var2) {
      var0.setAttributeNS((String)null, var1, var2);
   }

   static Element createElement(Element var0, QName var1, String var2) {
      Element var3 = var0.getOwnerDocument().createElementNS(var1.getNamespaceURI(), var2 + ":" + var1.getLocalPart());
      return var3;
   }

   static String getAttributeValueAsString(Element var0, QName var1) {
      String var2 = var1.getNamespaceURI();
      String var3 = null;
      if (var2 != null && var2.length() > 0) {
         var3 = var0.getAttributeNS(var2, var1.getLocalPart());
      } else {
         var3 = var0.getAttribute(var1.getLocalPart());
      }

      return var3;
   }

   static void addTextContent(Element var0, String var1) {
      var0.appendChild(var0.getOwnerDocument().createTextNode(var1));
   }

   static String getTextContent(Element var0) {
      StringBuffer var1 = new StringBuffer();
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

   static Element getElementByTagName(Element var0, String var1, boolean var2) throws MarshalException {
      for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var1.equals(var4.getLocalName())) {
               return var4;
            }
         }
      }

      if (var2) {
         return null;
      } else {
         throw new MarshalException("Can not find element: " + var1 + " under " + var0.getLocalName());
      }
   }

   static Element getFirstElement(Node var0) {
      Node var1;
      for(var1 = var0.getFirstChild(); var1.getNodeType() != 1; var1 = var1.getNextSibling()) {
      }

      return (Element)var1;
   }

   static Element getNextSiblingElement(Node var0) {
      Node var1 = var0.getNextSibling();
      if (var1 == null) {
         return null;
      } else {
         do {
            if (var1.getNodeType() == 1) {
               return (Element)var1;
            }

            var1 = var1.getNextSibling();
         } while(var1 != null);

         return null;
      }
   }
}
