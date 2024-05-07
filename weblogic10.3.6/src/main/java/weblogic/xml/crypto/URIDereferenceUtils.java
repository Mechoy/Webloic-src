package weblogic.xml.crypto;

import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class URIDereferenceUtils {
   public static Node findNodeById(String var0, Set var1, Node var2) {
      Node var3 = null;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         var3 = findNodeByIdQName(var0, (QName)var4.next(), var2);
         if (var3 != null) {
            break;
         }
      }

      return var3;
   }

   public static Node findNodeByIdQName(String var0, QName var1, Node var2) {
      NamedNodeMap var3 = var2.getAttributes();
      int var5;
      if (var3 != null) {
         int var4 = var3.getLength();

         for(var5 = 0; var5 < var4; ++var5) {
            Node var6 = var3.item(var5);
            if (namespacesMatch(var6, var1) && localNamesMatch(var6, var1) && var6.getNodeValue().equals(var0)) {
               return var2;
            }
         }
      }

      NodeList var8 = var2.getChildNodes();
      if (var8 != null) {
         var5 = var8.getLength();

         for(int var9 = 0; var9 < var5; ++var9) {
            Node var7 = findNodeByIdQName(var0, var1, var8.item(var9));
            if (var7 != null) {
               return var7;
            }
         }
      }

      return null;
   }

   private static boolean localNamesMatch(Node var0, QName var1) {
      String var2 = var0.getLocalName();
      if (var2 == null) {
         var2 = var0.getNodeName();
      }

      return var1.getLocalPart().equals(var2);
   }

   private static boolean namespacesMatch(Node var0, QName var1) {
      String var2 = var0.getNamespaceURI();
      String var3 = var1.getNamespaceURI();
      if (isEmptyNamespace(var2) && isEmptyNamespace(var3)) {
         return true;
      } else {
         return var2 != null && var2.equals(var3);
      }
   }

   private static boolean isEmptyNamespace(String var0) {
      return var0 == null || "".equals(var0);
   }
}
