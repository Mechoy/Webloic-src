package weblogic.auddi.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
   private static XMLTextTransformer s_transformer = null;

   private static void init() {
      if (s_transformer == null) {
         s_transformer = new XMLTextTransformer();
      }

   }

   public static String getNodeValue(Node var0) {
      String var1 = var0.getNodeValue();
      return var1 == null ? null : var1.trim();
   }

   public static void removeWhiteSpace(Node var0) {
      if (var0 != null) {
         String var1 = var0.getNodeValue();
         if (var1 != null) {
            var0.setNodeValue(var1.trim());
         }

         NamedNodeMap var2 = var0.getAttributes();
         int var4;
         if (var2 != null) {
            int var3 = 0;

            for(var4 = var2.getLength(); var3 < var4; ++var3) {
               Node var5 = var2.item(var3);
               String var6 = var5.getNodeValue();
               if (var6 != null) {
                  var5.setNodeValue(var6.trim());
               }
            }
         }

         NodeList var7 = var0.getChildNodes();
         var4 = 0;

         for(int var8 = var7.getLength(); var4 < var8; ++var4) {
            removeWhiteSpace(var7.item(var4));
         }

      }
   }

   public static String flatten(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();
         boolean var2 = true;
         int var3 = 0;

         for(int var4 = var0.length(); var3 < var4; ++var3) {
            char var5 = var0.charAt(var3);
            if (var5 == '>') {
               var2 = true;
               var1.append(var5);
            } else if (var5 == '<') {
               var2 = false;
               var1.append(var5);
            } else if (!Character.isWhitespace(var5) || !var2) {
               var1.append(var5);
            }
         }

         return var1.toString();
      }
   }

   public static String flatten(Node var0) {
      return flatten(nodeToString(var0));
   }

   public static String getSOAPBody(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf("Body");
         if (var1 == -1) {
            return null;
         } else {
            while(var0.charAt(var1) != '>') {
               ++var1;
            }

            ++var1;
            int var2 = var0.indexOf("Body", var1);
            if (var2 == -1) {
               return null;
            } else {
               while(var0.charAt(var2) != '<') {
                  --var2;
               }

               return var0.substring(var1, var2);
            }
         }
      }
   }

   public static void printXML(Node var0) {
      (new XMLDumper(System.out)).printNode(var0);
   }

   public static String nodeToString(Node var0) {
      init();
      synchronized(s_transformer) {
         return s_transformer.transform(var0);
      }
   }
}
