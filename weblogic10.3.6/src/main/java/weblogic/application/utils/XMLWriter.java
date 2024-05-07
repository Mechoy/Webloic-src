package weblogic.application.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class XMLWriter {
   private static final String sep = System.getProperty("line.separator");
   private static final int DEFAULT_INDENT = 2;
   private static final int DEFAULT_MAX_ATTRS_ON_SAME_LINE = 2;
   private boolean wroteFirstElement;
   private boolean hasElements;
   private boolean incompleteOpenTag;
   private boolean hasAttributes;
   private boolean closeOnNewLine;
   private final Stack elementStack;
   private final PrintWriter pw;
   private final int indent;
   private final int maxAttrsOnSameLine;
   private boolean textOnSameLineAsParentElement;

   public XMLWriter(PrintWriter var1) {
      this(var1, 2);
   }

   public XMLWriter(PrintWriter var1, int var2) {
      this(var1, var2, 2);
   }

   public XMLWriter(PrintWriter var1, int var2, int var3) {
      this.wroteFirstElement = false;
      this.hasElements = false;
      this.incompleteOpenTag = false;
      this.hasAttributes = false;
      this.closeOnNewLine = false;
      this.elementStack = new Stack();
      this.textOnSameLineAsParentElement = true;
      this.indent = var2;
      this.maxAttrsOnSameLine = var3;
      this.pw = var1;
   }

   public void flush() {
      this.pw.flush();
   }

   public void startDocumentDTD(String var1, String var2, String var3) {
      if (this.hasElements) {
         throw new MalformedXMLRuntimeException("Cannot init document after elements have been added");
      } else {
         StringBuffer var4 = new StringBuffer();
         var4.append("<!DOCTYPE ").append(var1);
         if (var2 != null) {
            var4.append(" PUBLIC \"").append(var2).append("\"");
         }

         if (var3 != null) {
            var4.append(" \"").append(var3).append("\"");
         }

         var4.append(">").append(sep);
         this.writeGeneric(var4.toString());
      }
   }

   public void setTextOnSameLineAsParentElement(boolean var1) {
      this.textOnSameLineAsParentElement = var1;
   }

   public void addComment(String var1) {
      this.finishIncompleteTag();
      StringBuffer var2 = new StringBuffer();
      var2.append(sep).append(this.getIndent()).append("<!--").append(var1).append("-->");
      this.writeGeneric(var2.toString());
   }

   public void addAttribute(String var1, String var2) {
      if (!this.incompleteOpenTag) {
         throw new MalformedXMLRuntimeException("Illegal call to addAttribute");
      } else {
         this.hasAttributes = true;
         if (this.closeOnNewLine) {
            this.pw.print(sep);
            this.pw.print(this.getIndent());
         } else {
            this.pw.print(" ");
         }

         this.pw.print(var1);
         this.pw.print("=\"");
         this.pw.print(var2);
         this.pw.print("\"");
      }
   }

   public void addAttribute(Map var1) {
      String[] var2 = new String[var1.size() * 2];
      int var3 = 0;

      for(Iterator var4 = var1.entrySet().iterator(); var4.hasNext(); var3 += 2) {
         Map.Entry var5 = (Map.Entry)var4.next();
         var2[var3] = (String)var5.getKey();
         var2[var3 + 1] = (String)var5.getValue();
      }

      this.addAttribute(var2);
   }

   public void addAttribute(String[] var1) {
      if (!this.hasAttributes && var1.length / 2 > this.maxAttrsOnSameLine) {
         this.closeOnNewLine = true;
      }

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         String var3 = var1[var2];
         String var4 = var1[var2 + 1];
         this.addAttribute(var3, var4);
      }

   }

   public void addNestedElements(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addElement(var1[var2]);
      }

   }

   public void addEmptyElements(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addEmptyElement(var1[var2]);
      }

   }

   public void addEmptyElement(String var1) {
      this.addElement(var1);
      this.closeElement();
   }

   public void addElement(String var1) {
      this.hasElements = true;
      this.finishIncompleteTag();
      if (this.wroteFirstElement) {
         this.pw.print(sep);
      } else {
         this.wroteFirstElement = true;
      }

      this.pw.print(this.getIndent());
      this.pw.print("<");
      this.pw.print(var1);
      this.elementStack.push(var1);
      this.incompleteOpenTag = true;
      this.hasAttributes = false;
      this.closeOnNewLine = false;
   }

   public void addElement(String var1, String var2) {
      this.addElement(var1);
      this.addText(var2, !this.textOnSameLineAsParentElement);
      this.closeElement(!this.textOnSameLineAsParentElement);
   }

   public void addElement(String var1, String[] var2) {
      this.addElement(var1);
      this.addAttribute(var2);
   }

   public void closeElement() {
      this.closeElement(true);
   }

   private void closeElement(boolean var1) {
      if (this.elementStack.isEmpty()) {
         throw new MalformedXMLRuntimeException("Illegal call to closeElement");
      } else {
         String var2 = this.elementStack.pop();
         if (this.incompleteOpenTag) {
            if (this.closeOnNewLine) {
               this.pw.print(sep);
               this.pw.print(this.getIndent());
            }

            this.pw.print("/>");
            this.incompleteOpenTag = false;
         } else {
            if (var1) {
               this.pw.print(sep);
               this.pw.print(this.getIndent());
            }

            this.pw.print("</");
            this.pw.print(var2);
            this.pw.print(">");
         }

         this.hasAttributes = false;
      }
   }

   public void addText(String var1) {
      this.addText(var1, true);
   }

   private void addText(String var1, boolean var2) {
      if (var1 != null) {
         if (var1.trim().length() != 0) {
            this.finishIncompleteTag();
            if (var2) {
               this.pw.print(sep);
               this.pw.print(this.getIndent());
            }

            this.pw.print(var1.trim());
         }
      }
   }

   public void addCDATA(String var1) {
      if (var1.trim().length() != 0) {
         this.finishIncompleteTag();
         StringBuffer var2 = new StringBuffer();
         var2.append(sep).append(this.getIndent()).append("<![CDATA[");
         String var3 = var1.trim();
         StringTokenizer var4 = new StringTokenizer(var3, sep);

         while(var4.hasMoreTokens()) {
            var2.append(sep).append(this.getIndent()).append(this.getIndentUnit()).append(var4.nextToken().trim());
         }

         var2.append(sep).append(this.getIndent()).append("]]>");
         this.writeGeneric(var2.toString());
      }
   }

   public void finish() {
      this.closeAll();
      this.pw.flush();
   }

   private void closeAll() {
      while(!this.elementStack.isEmpty()) {
         this.closeElement();
      }

   }

   private void finishIncompleteTag() {
      if (this.incompleteOpenTag) {
         if (this.closeOnNewLine) {
            this.pw.print(sep);
            this.pw.print(this.getIndent(this.getStackSize() - 1));
         }

         this.pw.print(">");
         this.incompleteOpenTag = false;
      }

   }

   private void writeGeneric(String var1) {
      this.pw.print(var1);
   }

   private String getIndent() {
      return this.getIndent(this.getStackSize());
   }

   private String getIndent(int var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = var1 * this.indent;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.append(" ");
      }

      return var2.toString();
   }

   private String getIndentUnit() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.indent; ++var2) {
         var1.append(" ");
      }

      return var1.toString();
   }

   private int getStackSize() {
      return this.elementStack.size();
   }

   public static void main(String[] var0) {
      XMLWriter var1 = new XMLWriter(new PrintWriter(System.out), 2, 3);
      var1.addElement("project");
      var1.addAttribute("name", "foo-project");
      var1.addAttribute("default", "default-target");
      var1.addAttribute("a3", "v3");
      var1.addAttribute("a4", "v4");
      var1.addElement("description", "this is a project of some sort");
      var1.addComment("A comment between description and target");
      var1.addElement("target", new String[]{"a1", "v1", "a2", "v2", "a3", "v3"});
      var1.addAttribute("name", "default-target");
      var1.addAttribute("a5", "v5");
      var1.addElement("test-suite");
      var1.addAttribute("testunit", "foo-testunit");
      var1.addElement("test");
      var1.addAttribute("name", "foo-test");
      var1.addElement("javatest");
      var1.addAttribute("testclass", "weblogic.qa.frame.test.foo");
      var1.addCDATA("Some CDATA nested inside the second \"javatest\" element");
      var1.closeElement();
      var1.addElement("javatest", new String[]{"a1", "v1", "a2", "v2", "a3", "v3", "a4", "v4"});
      var1.addText("Some text nested inside the second \"javatest\" element");
      var1.closeElement();
      var1.addElement("javatest", new String[]{"a1", "v1", "a2", "v2", "a3", "v3"});
      var1.closeElement();
      var1.addComment("Some nested elements");
      var1.addNestedElements(new String[]{"e1", "e2", "e3", "e4"});
      var1.finish();
      System.out.println(sep);
   }

   private class Stack extends ArrayList {
      private static final long serialVersionUID = -7341083262157803283L;

      private Stack() {
      }

      public void push(String var1) {
         super.add(0, var1);
      }

      public String pop() {
         return (String)super.remove(0);
      }

      // $FF: synthetic method
      Stack(Object var2) {
         this();
      }
   }

   private class MalformedXMLRuntimeException extends RuntimeException {
      private static final long serialVersionUID = 5122932004316476798L;

      private MalformedXMLRuntimeException(String var2) {
         super(var2);
      }

      // $FF: synthetic method
      MalformedXMLRuntimeException(String var2, Object var3) {
         this(var2);
      }
   }
}
