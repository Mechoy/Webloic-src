package weblogic.xml.crypto.utils;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;

public final class AttrNodeAsHashKey implements Serializable {
   private String id;
   private String name;
   private String namespaceURI;

   private AttrNodeAsHashKey() {
   }

   public static AttrNodeAsHashKey valueOf(Node var0) {
      AttrNodeAsHashKey var1 = new AttrNodeAsHashKey();
      if (var0 == null) {
         return var1;
      } else {
         String var2 = var0.getLocalName();
         if (var2 == null) {
            var2 = var0.getNodeName();
         }

         var1.name = var2;
         var1.id = var0.getNodeValue();
         var1.namespaceURI = var0.getNamespaceURI();
         return var1;
      }
   }

   public static AttrNodeAsHashKey valueOf(String var0, QName var1) {
      AttrNodeAsHashKey var2 = new AttrNodeAsHashKey();
      var2.id = var0;
      if (var1 != null) {
         var2.name = var1.getLocalPart();
         var2.namespaceURI = var1.getNamespaceURI();
      }

      return var2;
   }

   private boolean compareStrings(String var1, String var2) {
      if (var1 == null && var2 == null) {
         return true;
      } else {
         return var1 != null && var1.equals(var2);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof AttrNodeAsHashKey)) {
         return false;
      } else {
         AttrNodeAsHashKey var2 = (AttrNodeAsHashKey)var1;
         return this.compareStrings(this.id, var2.id) && this.compareStrings(this.name, var2.name) && this.compareStrings(this.namespaceURI == null ? "" : this.namespaceURI, var2.namespaceURI == null ? "" : var2.namespaceURI);
      }
   }

   public int hashCode() {
      return ((this.id == null ? "#&#" : this.id) + "&&&" + (this.name == null ? "#&#" : this.id) + "&&&" + (this.namespaceURI == null ? "" : this.namespaceURI)).hashCode();
   }
}
