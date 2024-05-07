package weblogic.xml.dom;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

public final class Util {
   public static final NodeList EMPTY_NODELIST = new NodeList() {
      public int getLength() {
         return 0;
      }

      public Node item(int var1) {
         return null;
      }
   };
   public static final NamedNodeMap NAMED_NODE_MAP = new NamedNodeMap() {
      public int getLength() {
         return 0;
      }

      public Node getNamedItem(String var1) {
         return null;
      }

      public Node getNamedItemNS(String var1, String var2) {
         return null;
      }

      public Node item(int var1) {
         return null;
      }

      public Node removeNamedItem(String var1) {
         return null;
      }

      public Node removeNamedItemNS(String var1, String var2) {
         return null;
      }

      public Node setNamedItem(Node var1) {
         throw new UnsupportedOperationException("This NamedNodeMap is readOnly");
      }

      public Node setNamedItemNS(Node var1) {
         throw new UnsupportedOperationException("This NamedNodeMap is readOnly");
      }
   };

   private Util() {
   }

   public static final String getPrefix(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         return var1 < 0 ? null : var0.substring(0, var1);
      }
   }

   public static final String getLocalName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         return var1 < 0 ? var0 : var0.substring(var1 + 1);
      }
   }

   public static String printNode(Node var0) {
      StringBuffer var1 = new StringBuffer();
      switch (var0.getNodeType()) {
         case 1:
            String var9 = var0.getNodeName();
            var1.append("<" + var9);
            NamedNodeMap var6 = var0.getAttributes();

            for(int var7 = 0; var7 < var6.getLength(); ++var7) {
               Node var11 = var6.item(var7);
               var1.append(" " + var11.getNodeName() + "=\"" + var11.getNodeValue() + "\"");
            }

            var1.append(">");
            NodeList var10 = var0.getChildNodes();
            if (var10 != null) {
               for(int var12 = 0; var12 < var10.getLength(); ++var12) {
                  var1.append(printNode(var10.item(var12)));
               }
            }

            var1.append("</" + var9 + ">");
            break;
         case 2:
         case 5:
         case 6:
         default:
            throw new IllegalArgumentException("Unable to process " + var0);
         case 3:
         case 4:
            var1.append(var0.getNodeValue());
            break;
         case 7:
            ProcessingInstruction var8 = (ProcessingInstruction)var0;
            var1.append("<?" + var8.getTarget() + " " + var8.getData() + "?>");
            break;
         case 8:
            var1.append("<!--" + ((Comment)var0).getData() + "-->\n");
            break;
         case 9:
            Document var2 = (Document)var0;
            var1.append(printNode(var2.getDocumentElement()));
            break;
         case 10:
            var1.append("<xml version=\"1.0\">\n");
            break;
         case 11:
            NodeList var3 = var0.getChildNodes();
            int var4 = var3.getLength();

            for(int var5 = 0; var5 < var4; ++var5) {
               var1.append(printNode(var3.item(var5)));
            }
      }

      return var1.toString();
   }
}
