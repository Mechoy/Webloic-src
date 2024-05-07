package weblogic.xml.util.xed;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class XEDParser {
   private Reader reader;
   private char current;
   private boolean EOF = false;
   private static final boolean debug = true;
   private int line = 1;

   public XEDParser(Reader var1) throws IOException, StreamEditorException {
      this.reader = var1;
      this.accept();
   }

   public boolean reachedEOF() {
      return this.EOF;
   }

   public char current() {
      return this.current;
   }

   public boolean isSpace() {
      return this.current == ' ' || this.current == '\r' || this.current == '\t' || this.current == '\n';
   }

   public void skipSpace() throws IOException {
      while(this.isSpace() && !this.reachedEOF()) {
         this.accept();
      }

   }

   public String error() {
      return "\nParse error at line:" + this.line + " char:" + this.current();
   }

   public void accept() throws IOException {
      int var1 = this.reader.read();
      if (var1 == -1) {
         this.EOF = true;
      }

      this.current = (char)var1;
      if (var1 == 10) {
         ++this.line;
      }

   }

   public void accept(char var1) throws IOException, StreamEditorException {
      if (this.current() == var1) {
         this.accept();
      } else {
         throw new StreamEditorException("Unable to match character[" + var1 + "]" + this.error());
      }
   }

   public void accept(String var1) throws IOException, StreamEditorException {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if (var1.charAt(var2) != this.current()) {
            throw new StreamEditorException("Unable to match string[" + var1 + "]" + this.error());
         }

         this.accept();
      }

   }

   public String readString() throws IOException, StreamEditorException {
      StringBuffer var1 = new StringBuffer();

      while(this.current() != ' ' && this.current() != '=' && this.current() != '+' && this.current() != ';' && !this.reachedEOF()) {
         var1.append(this.current());
         this.accept();
      }

      return var1.toString();
   }

   public String readString(char var1) throws IOException, StreamEditorException {
      StringBuffer var2 = new StringBuffer();

      while(this.current() != var1 && !this.reachedEOF()) {
         var2.append(this.current());
         this.accept();
      }

      return var2.toString();
   }

   public String readXPathString(char var1) throws IOException, StreamEditorException {
      StringBuffer var2 = new StringBuffer();
      int var3 = 0;

      while(this.current() != var1 && !this.reachedEOF()) {
         if (this.current() == '(') {
            ++var3;
         }

         if (this.current() == ')') {
            --var3;
         }

         if (var3 < 0) {
            break;
         }

         var2.append(this.current());
         this.accept();
      }

      return var2.toString();
   }

   public String readXMLString() throws IOException, StreamEditorException {
      int var1 = 0;
      StringBuffer var2 = new StringBuffer();

      while(this.current() != ';' && !this.reachedEOF()) {
         if (this.current() == '(') {
            ++var1;
         }

         if (this.current() == ')') {
            --var1;
         }

         if (var1 < 0) {
            break;
         }

         var2.append(this.current());
         this.accept();
      }

      return var2.toString();
   }

   public Operation parse() throws IOException, StreamEditorException {
      Operation var1 = this.parseOperation();
      if (this.reachedEOF()) {
         System.out.println("Input parsed successfully");
      }

      return var1;
   }

   public Operation parseOperation() throws IOException, StreamEditorException {
      Operation var1 = new Operation();
      this.skipSpace();

      while(!this.reachedEOF()) {
         Command var2 = null;
         switch (this.current()) {
            case 'd':
               var2 = this.parseDelete();
               break;
            case 'i':
               this.accept("insert-");
               if (this.current() == 'b') {
                  var2 = this.parseInsertBefore();
               } else if (this.current() == 'a') {
                  var2 = this.parseInsertAfter();
               } else {
                  if (this.current() != 'c') {
                     throw new StreamEditorException("Valid commands are insert-before, insert-child,  or insert-after" + this.error());
                  }

                  var2 = this.parseInsertChild();
               }
               break;
            case 'r':
               var2 = this.parseReplace();
               break;
            default:
               throw new StreamEditorException("Valid commands areinsert-before insert-child insert-after replace delete" + this.error());
         }

         this.skipSpace();
         var1.add(var2);
      }

      return var1;
   }

   public String parseXML() throws IOException, StreamEditorException {
      this.skipSpace();
      String var1;
      if (this.current() == '<') {
         var1 = this.readXMLString();
      } else {
         String var2 = this.readString(')');
         this.skipSpace();
         System.out.println("file ----->" + var2);
         FileReader var3 = new FileReader(new File(var2));
         StringBuffer var4 = new StringBuffer();
         boolean var5 = false;

         while(!var5) {
            int var6 = var3.read();
            if (var6 == -1) {
               var5 = true;
               break;
            }

            var4.append((char)var6);
         }

         var1 = var4.toString();
      }

      return var1;
   }

   public Command parseInsertBefore() throws IOException, StreamEditorException {
      InsertBefore var1 = new InsertBefore();
      this.accept("before");
      this.skipSpace();
      this.accept('(');
      this.skipSpace();
      String var2 = this.readXPathString(',');
      this.accept(',');
      this.skipSpace();
      var1.setXPath(var2);
      var1.setXML(this.parseXML());
      this.skipSpace();
      this.accept(')');
      this.skipSpace();
      this.accept(';');
      System.out.println("[INSERTBEFORE][" + var2 + "][" + var1.getXML() + "]");
      return var1;
   }

   public Command parseInsertChild() throws IOException, StreamEditorException {
      InsertChild var1 = new InsertChild();
      this.accept("child");
      this.skipSpace();
      this.accept('(');
      this.skipSpace();
      String var2 = this.readXPathString(',');
      this.accept(',');
      this.skipSpace();
      var1.setXPath(var2);
      var1.setXML(this.parseXML());
      this.skipSpace();
      this.accept(')');
      this.skipSpace();
      this.accept(';');
      System.out.println("[INSERTCHILD][" + var2 + "][" + var1.getXML() + "]");
      return var1;
   }

   public Command parseInsertAfter() throws IOException, StreamEditorException {
      InsertAfter var1 = new InsertAfter();
      this.accept("after");
      this.skipSpace();
      this.accept('(');
      this.skipSpace();
      String var2 = this.readXPathString(',');
      this.accept(',');
      this.skipSpace();
      var1.setXPath(var2);
      var1.setXML(this.parseXML());
      this.skipSpace();
      this.accept(')');
      this.skipSpace();
      this.accept(';');
      System.out.println("[INSERTAFTER][" + var2 + "][" + var1.getXML() + "]");
      return var1;
   }

   public Command parseDelete() throws IOException, StreamEditorException {
      Delete var1 = new Delete();
      this.accept("delete");
      this.skipSpace();
      this.accept('(');
      this.skipSpace();
      String var2 = this.readXPathString(';');
      this.skipSpace();
      var1.setXPath(var2);
      this.skipSpace();
      this.accept(')');
      this.skipSpace();
      this.accept(';');
      System.out.println("[DELETE][" + var2 + "]");
      return var1;
   }

   public Command parseReplace() throws IOException, StreamEditorException {
      Replace var1 = new Replace();
      this.accept("replace");
      this.skipSpace();
      this.accept('(');
      this.skipSpace();
      String var2 = this.readXPathString(',');
      var1.setXPath(var2);
      this.skipSpace();
      System.out.print("[REPLACE][" + var2 + "]");
      switch (this.current()) {
         case ')':
            this.accept();
            this.skipSpace();
            this.parseAction(var1);
            break;
         case ',':
            this.accept();
            this.skipSpace();
            String var3 = this.readXMLString();
            this.skipSpace();
            var1.setXML(var3);
            this.skipSpace();
            this.accept(')');
            this.skipSpace();
            this.accept(';');
            System.out.println("[" + var3 + "]");
            break;
         default:
            throw new StreamEditorException("Replace command has the following structure:'replace' Xpath (XML ';' | { Assignments ... })" + this.error());
      }

      return var1;
   }

   public void parseAction(Replace var1) throws IOException, StreamEditorException {
      this.accept('{');
      System.out.print("\n\t");
      this.skipSpace();

      while(this.current == '$') {
         var1.add(this.parseAssignment());
      }

      this.accept('}');
   }

   public Variable parseVariable() throws IOException, StreamEditorException {
      this.accept('$');
      Object var1;
      if (this.current() == '@') {
         this.accept();
         var1 = new AttributeReference();
      } else {
         var1 = new Variable();
      }

      ((Variable)var1).setName(this.readString());
      return (Variable)var1;
   }

   public Assignment parseAssignment() throws IOException, StreamEditorException {
      Variable var1 = this.parseVariable();
      Object var2;
      if (var1.isAttributeRef()) {
         var2 = new AttributeAssignment();
      } else {
         var2 = new Assignment();
      }

      ((Assignment)var2).setLHS(var1);
      System.out.print("[" + var1 + "=");
      this.skipSpace();
      this.accept('=');
      this.skipSpace();
      Variable var3;
      String var4;
      switch (this.current()) {
         case '"':
         case '\'':
            var4 = this.parseConstant();
            System.out.print("'" + var4 + "'");
            ((Assignment)var2).addRHS(new StringConstant(var4));
            break;
         case '$':
            var3 = this.parseVariable();
            System.out.print(var3);
            ((Assignment)var2).addRHS(var3);
            break;
         case '[':
            this.parseAttributeList((Assignment)var2);
      }

      this.skipSpace();

      for(; this.current() == '+'; this.skipSpace()) {
         System.out.print("+");
         this.accept();
         switch (this.current()) {
            case '"':
            case '\'':
               var4 = this.parseConstant();
               ((Assignment)var2).addRHS(new StringConstant(var4));
               System.out.print("'" + var4 + "'");
               break;
            case '$':
               var3 = this.parseVariable();
               ((Assignment)var2).addRHS(var3);
               System.out.print(var3);
               break;
            case '[':
               this.parseAttributeList((Assignment)var2);
         }
      }

      System.out.println("]");
      this.accept(';');
      this.skipSpace();
      return (Assignment)var2;
   }

   public String parseConstant() throws IOException, StreamEditorException {
      String var1 = "";
      if (this.current() == '"') {
         this.accept();
         var1 = this.readString('"');
      } else {
         if (this.current() != '\'') {
            throw new StreamEditorException("String consants must be enclosed in quotes" + this.error());
         }

         this.accept();
         var1 = this.readString('\'');
      }

      this.accept();
      return var1;
   }

   public void parseAttributeList(Assignment var1) throws IOException, StreamEditorException {
      this.accept('[');

      while(this.current != ']' && !this.reachedEOF()) {
         this.skipSpace();
         String var2 = this.readString();
         this.skipSpace();
         this.accept('=');
         String var3 = this.parseConstant();
         System.out.print("[" + var2 + "=" + var3 + "]");
         var1.addRHS(new AttributeVariable(var2, var3));
      }

      this.accept(']');
   }

   public static void main(String[] var0) throws Exception {
      XEDParser var1 = new XEDParser(new FileReader(var0[0]));
      System.out.println(var1.parse());
   }
}
