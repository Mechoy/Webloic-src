package weblogic.xml.util;

import java.io.IOException;
import java.io.PrintWriter;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DocumentWriter {
   public static void write(Document var0, PrintWriter var1) throws IOException {
      printNode(var0, var1, 0);
   }

   public static void write(Node var0, PrintWriter var1) throws IOException {
      printNode(var0, var1, 0);
   }

   private static void printNode(Node var0, PrintWriter var1, int var2) {
      indent(var2, var1);
      if (var0.getNodeType() == 1) {
         var1.print("<" + var0.getNodeName());
      } else if (var0.getNodeType() == 3) {
         var1.print(((Text)var0).getData());
      } else if (var0.getNodeType() == 8) {
         var1.print("<!--" + ((Comment)var0).getData() + "-->");
         var1.print("\n");
      }

      if (var0.getNodeType() != 9) {
         NamedNodeMap var3 = var0.getAttributes();
         if (var3 != null) {
            int var4 = var3.getLength();

            for(int var5 = 0; var5 < var4; ++var5) {
               Node var6 = var3.item(var5);
               indent(var2 + 2, var1);
               var1.print(var6.getNodeName() + "=\"" + var6.getNodeValue() + "\"");
            }
         }

         if (var0.hasChildNodes() && var3.getLength() != 0) {
            indent(var2, var1);
            var1.print(">");
         } else if (var0.hasChildNodes() && var3.getLength() == 0) {
            var1.print(">");
         }
      }

      NodeList var7 = var0.getChildNodes();
      if (var7.getLength() > 0) {
         printNodeList(var7, var1, var2 + 2);
         indent(var2, var1);
         if (var0.getNodeType() != 8 && var0.getNodeType() != 9) {
            var1.print("</" + var0.getNodeName() + ">");
         }
      } else {
         indent(var2, var1);
         if (var0.getNodeType() == 1) {
            var1.print("/>");
         }
      }

   }

   private static void printNodeList(NodeList var0, PrintWriter var1, int var2) {
      int var3 = var0.getLength();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         if (var0.item(var4).getNodeType() == 8) {
            printNode(var0.item(var4), var1, var2);
         }
      }

      for(var4 = 0; var4 < var3; ++var4) {
         if (var0.item(var4).getNodeType() != 8) {
            printNode(var0.item(var4), var1, var2);
         }
      }

   }

   private static void indent(int var0, PrintWriter var1) {
      var1.print("\n");

      for(int var2 = 0; var2 < var0; ++var2) {
         var1.print(" ");
      }

   }
}
