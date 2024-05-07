package weblogic.auddi.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDumper {
   private static int INDENT = 2;
   private PrintStream m_out;
   private boolean printEntityReference;
   private boolean printComment;
   private boolean printEmptyContent;

   public XMLDumper() {
      this(System.out);
   }

   public XMLDumper(String var1) throws FileNotFoundException {
      this(new PrintStream(new FileOutputStream(var1)));
   }

   public XMLDumper(PrintStream var1) {
      this.m_out = var1;
      this.printEntityReference = false;
      this.printComment = false;
      this.printEmptyContent = false;
   }

   public void includeComments(boolean var1) {
      this.printComment = var1;
   }

   public void includeEntityReference(boolean var1) {
      this.printEntityReference = var1;
   }

   public void includeEmptyContent(boolean var1) {
      this.printEmptyContent = var1;
   }

   public void printNode(Node var1) {
      if (var1 != null) {
         this.printNode(var1, 0);
      }
   }

   private void printNode(Node var1, int var2) {
      this.dumpNode(var1, var2);
      NodeList var3 = var1.getChildNodes();
      int var4 = 0;

      for(int var5 = var3.getLength(); var4 < var5; ++var4) {
         this.printNode(var3.item(var4), var2 + INDENT);
      }

   }

   private void dumpNode(Node var1, int var2) {
      switch (var1.getNodeType()) {
         case 3:
            String var3 = this.getNodeValue(var1);
            if (!this.printEmptyContent && (var3 == null || var3.equals(""))) {
               return;
            }

            this.printContent(var1, var2 - INDENT);
            break;
         case 5:
            if (!this.printEntityReference) {
               return;
            }

            this.printElement(var1, var2);
            this.printAttributes(var1, var2);
            break;
         case 8:
            if (!this.printComment) {
               return;
            }

            this.printComment(var1, var2);
            break;
         default:
            this.printElement(var1, var2);
            this.printAttributes(var1, var2);
      }

   }

   private void printElement(Node var1, int var2) {
      String var3 = this.getNodeValue(var1);
      this.printIndent(var2);
      if (var1.getNodeType() == 1) {
         this.m_out.print("Element: " + var1.getNodeName());
      } else {
         this.m_out.print("Name: " + var1.getNodeName());
         this.m_out.print("  Type: " + this.getNodeType(var1));
      }

      if (var3 == null) {
         this.m_out.println();
      } else {
         this.m_out.println("  Value: \"" + var3 + "\"");
      }

   }

   private void printContent(Node var1, int var2) {
      this.printIndent(var2);
      this.m_out.println("Content: " + var1.getNodeValue());
   }

   private void printComment(Node var1, int var2) {
      this.printIndent(var2);
      this.m_out.println("Comment: " + var1.getNodeValue());
   }

   private void printAttributes(Node var1, int var2) {
      NamedNodeMap var3 = var1.getAttributes();
      if (var3 != null) {
         int var4 = 0;

         for(int var5 = var3.getLength(); var4 < var5; ++var4) {
            Node var6 = var3.item(var4);
            this.printIndent(var2);
            this.m_out.print("Attribute Name: " + var6.getNodeName());
            this.m_out.println("  Value: \"" + this.getNodeValue(var6) + "\"");
         }
      }

   }

   private void printIndent(int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.m_out.print(" ");
      }

   }

   private String getNodeType(Node var1) {
      switch (var1.getNodeType()) {
         case 1:
            return "ELEMENT";
         case 2:
            return "ATTRIBUTE";
         case 3:
            return "TEXT";
         case 4:
            return "CDATA_SECTION";
         case 5:
            return "ENTITY_REFERENCE";
         case 6:
            return "ENTITY";
         case 7:
            return "PROCESSING_INSTRUCTION";
         case 8:
            return "COMMENT";
         case 9:
            return "DOCUMENT";
         case 10:
            return "DOCUMENT_TYPE";
         case 11:
            return "DOCUMENT_FRAGMENT";
         case 12:
            return "NOTATION";
         default:
            return "Not Found!!!!!!!";
      }
   }

   private String getNodeValue(Node var1) {
      String var2 = var1.getNodeValue();
      return var2 == null ? null : var2.trim();
   }
}
