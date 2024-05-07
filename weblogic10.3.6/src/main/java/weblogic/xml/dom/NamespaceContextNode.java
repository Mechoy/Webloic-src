package weblogic.xml.dom;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamespaceContextNode implements NamespaceContext {
   private final Element current;

   public NamespaceContextNode(Element var1) {
      this.current = var1;
   }

   public String getNamespaceURI(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null prefix");
      } else if ("xml".equals(var1)) {
         return "http://www.w3.org/XML/1998/namespace";
      } else if ("xmlns".equals(var1)) {
         return "http://www.w3.org/2000/xmlns/";
      } else {
         return this.current == null ? null : this.getNamespaceURI(this.current, var1);
      }
   }

   public String getPrefix(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null namespaceURI");
      } else if ("http://www.w3.org/XML/1998/namespace".equals(var1)) {
         return "xml";
      } else if ("http://www.w3.org/2000/xmlns/".equals(var1)) {
         return "xmlns";
      } else {
         return this.current == null ? null : this.getPrefix(this.current, var1);
      }
   }

   private String getNamespaceURI(Element var1, String var2) {
      String var3 = NamespaceUtils.getNamespaceOnElement(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         Node var4 = var1.getParentNode();
         if (var4 == null) {
            return null;
         } else {
            return var4.getNodeType() != 1 ? null : this.getNamespaceURI((Element)var4, var2);
         }
      }
   }

   public String getPrefix(Element var1, String var2) {
      String var3 = NamespaceUtils.getPrefixOnElement(var1, var2, true);
      if (var3 != null) {
         return var3;
      } else {
         Node var4 = var1.getParentNode();
         if (var4 == null) {
            return null;
         } else {
            return var4.getNodeType() != 1 ? null : this.getPrefix((Element)var4, var2);
         }
      }
   }

   private void collectPrefixes(Element var1, String var2, ArrayList var3) {
      NamedNodeMap var4 = var1.getAttributes();

      for(int var5 = 0; var5 < var4.getLength(); ++var5) {
         Attr var6 = (Attr)var4.item(var5);
         if (var6.getValue().equals(var2) && var6.getNamespaceURI().equals(ElementNode.XMLNS_URI)) {
            var3.add(var6.getLocalName());
         }
      }

      Node var7 = var1.getParentNode();
      if (var7 != null) {
         if (var7.getNodeType() == 1) {
            this.collectPrefixes((Element)var7, var2, var3);
         }
      }
   }

   public Iterator getPrefixes(String var1) {
      ArrayList var2 = new ArrayList();
      this.collectPrefixes(this.current, var1, var2);
      return var2.iterator();
   }
}
