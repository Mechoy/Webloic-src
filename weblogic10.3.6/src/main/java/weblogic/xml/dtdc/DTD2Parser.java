package weblogic.xml.dtdc;

import com.ibm.xml.parser.AttDef;
import com.ibm.xml.parser.DTD;
import com.ibm.xml.parser.ElementDecl;
import com.ibm.xml.parser.InsertableElement;
import com.ibm.xml.parser.Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.BadOutputException;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class DTD2Parser extends CodeGenerator {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final String EOL = System.getProperty("line.separator");
   private static final String ROOT = "root";
   private ParserOutput currentOutput;
   private Vector outputs;
   private DTD dtd;
   private String root;
   private String packageName;
   private ElementDecl element;
   SortedMap allAttributes = this.createTreeMap();
   AttDef currentAttribute;
   private static int depth = 0;
   private boolean writingAttribute = false;

   public DTD2Parser(Getopt2 var1) {
      super(var1);
      var1.addOption("package", "weblogic.xml.parsers", "Name of the base package for objects");
      var1.addOption("root", "xsl:stylesheet", "Root element of the DTD");
   }

   protected void extractOptionValues(Getopt2 var1) {
      this.packageName = var1.getOption("package", "weblogic.xml.objects");
      this.root = var1.getOption("root");
   }

   public Enumeration outputs(Object[] var1) throws Exception {
      String[] var2 = (String[])((String[])var1);
      this.outputs = new Vector();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = new File((new File(var2[var3])).getAbsolutePath());
         FileInputStream var5 = new FileInputStream(var4);
         Parser var6 = new Parser(var4.getParentFile().toURL().toString());
         DTD var7 = var6.readDTDStream(var5);
         if (this.root == null) {
            String var8 = var4.getName();
            this.root = var8.substring(0, var8.lastIndexOf("."));
         }

         this.outputs.addElement(new ParserOutput(NameMangler.upcase(NameMangler.depackage(this.root)) + "Parser.java", this.packageName, "parser.j", var7, this.root));
      }

      return this.outputs.elements();
   }

   protected void prepare(CodeGenerator.Output var1) throws BadOutputException {
      this.currentOutput = (ParserOutput)var1;
   }

   public DTD getDTD() {
      return this.currentOutput.getDTD();
   }

   public String package_name() {
      return this.packageName;
   }

   public String element_class_name() {
      return NameMangler.upcase(NameMangler.depackage(this.root));
   }

   public String parser_class_name() {
      return this.element_class_name() + "Parser";
   }

   public String parser() {
      TreeMap var1 = this.createTreeMap();
      var1.put("<?xml", "// Ignore XML version\nwhile(chars[current++] != '>');");
      var1.put("<!DOCTYPE", "// Ignore DTD specification\nwhile(chars[current++] != '>');");
      var1.put(" ", "// Ignore whitespace");
      var1.put("\t", "// Ignore whitespace");
      var1.put("\r", "// Ignore whitespace");
      var1.put("\n", "nextLine();");
      var1.put("<!--", "eatComment(chars);");
      depth = 0;
      this.element = this.getDTD().getElementDeclaration(this.root);
      if (this.element == null) {
         throw new Error("No element <" + this.root + "> found in DTD");
      } else {
         var1.put("<" + this.root, this.readElement());
         depth = 3;
         this.writingAttribute = false;
         return this.match(var1, "chars[current++]", true);
      }
   }

   public String readElement() {
      StringBuffer var1 = new StringBuffer();
      format(var1, "sendCharacters(chars, " + (this.element.getName().length() + 1) + ");");
      format(var1, "read" + NameMangler.upcase(NameMangler.depackage(this.element.getName())) + "(chars);");
      return var1.toString();
   }

   public String element_name() {
      return this.element.getName();
   }

   public String attribute_name() {
      return NameMangler.depackage(this.currentAttribute.getName()) + "Value";
   }

   public String attribute_realname() {
      return this.currentAttribute.getName();
   }

   public String attribute_typename() {
      return NameMangler.depackage(this.currentAttribute.getName()) + "Type";
   }

   public String attribute_type() {
      return AttDef.S_TYPESTR[this.currentAttribute.getDeclaredType()];
   }

   public String read_element_declarations() throws CodeGenerationException {
      Enumeration var1 = this.getDTD().getElementDeclarations();
      StringBuffer var2 = new StringBuffer();
      depth = 2;

      while(var1.hasMoreElements()) {
         this.element = (ElementDecl)var1.nextElement();
         var2.append(this.parse(this.getProductionRule("read_element_declaration")));
      }

      return var2.toString();
   }

   public String read_element_method_name() {
      return "read" + NameMangler.upcase(NameMangler.depackage(this.element.getName()));
   }

   public String read_element_attributes_method_name() {
      return "read" + NameMangler.upcase(NameMangler.depackage(this.element.getName())) + "Attributes";
   }

   public void readAttributes(StringBuffer var1) {
      format(var1, "read" + NameMangler.upcase(NameMangler.depackage(this.element.getName())) + "Attributes(chars);");
   }

   public String declare_required_attributes() {
      Enumeration var1 = this.getDTD().getAttributeDeclarations(this.element.getName());
      StringBuffer var2 = new StringBuffer();
      depth = 1;

      while(var1.hasMoreElements()) {
         AttDef var3 = (AttDef)var1.nextElement();
         if (var3.getDefaultType() == 2) {
            format(var2, "boolean " + NameMangler.depackage(var3.getName()) + "Found = false;");
         }
      }

      return var2.toString();
   }

   public String required_flag() {
      return NameMangler.depackage(this.currentAttribute.getName()) + "Found";
   }

   public String set_defaults() {
      return "";
   }

   public String set_attribute_found() {
      return this.currentAttribute.getDefaultType() == 2 ? this.required_flag() + " = true;" : "// not required";
   }

   public String ensure_unique() {
      return this.currentAttribute.getDeclaredType() == 2 ? "if(ids.put(value, this) != null) throw new SAXParseException(\"Duplicate ID found: \" + value, this);" : "// not an id";
   }

   public String ensure_required_attributes() {
      Enumeration var1 = this.getDTD().getAttributeDeclarations(this.element.getName());
      StringBuffer var2 = new StringBuffer();
      depth = 2;

      while(var1.hasMoreElements()) {
         AttDef var3 = (AttDef)var1.nextElement();
         if (var3.getDefaultType() == 2) {
            format(var2, "if(!" + NameMangler.depackage(var3.getName()) + "Found) throw new SAXParseException(\"Required attribute " + var3.getName() + " not found\", this);");
         }
      }

      return var2.toString();
   }

   public String read_attributes() throws CodeGenerationException {
      Enumeration var1 = this.getDTD().getAttributeDeclarations(this.element.getName());
      TreeMap var2 = this.createTreeMap();
      var2.put(" ", "// Ignore whitespace");
      var2.put("\t", "// Ignore whitespace");
      var2.put("\r", "// Ignore whitespace");
      var2.put("\n", "// Next line\ncurrentLine++;\nlastLinePosition=current;");
      var2.put("/>", "// Done\nemptyTag=true;done=true;");
      var2.put(">", "// Done\ndone = true;");
      depth = 0;

      while(var1.hasMoreElements()) {
         this.currentAttribute = (AttDef)var1.nextElement();
         this.allAttributes.put(this.currentAttribute.getName(), this.currentAttribute);
         var2.put(this.currentAttribute.getName(), this.parse(this.getProductionRule("readAttribute")));
      }

      depth = 2;
      this.writingAttribute = true;
      return this.match(var2, "chars[current++]", false);
   }

   public String read_element() {
      if (this.element.getContentType() == 1) {
         return "done = true;";
      } else {
         ElementDecl var1 = this.element;
         TreeMap var2 = this.createTreeMap();
         var2.put(" ", "// Ignore whitespace");
         var2.put("\t", "// Ignore whitespace");
         var2.put("\r", "// Ignore whitespace");
         var2.put("\n", "nextLine();");
         var2.put("</" + this.element.getName() + ">", "sendCharacters(chars, " + (this.element.getName().length() + 3) + ");\n" + "done = true;");
         var2.put("&", "handleEscapes(chars);");
         var2.put("<![CDATA[", "handleCDATA(chars);");
         var2.put("<!--", "eatComment(chars);");
         Hashtable var3 = this.getDTD().prepareTable(this.element.getName());
         Enumeration var4 = var3.keys();
         depth = 0;

         while(var4.hasMoreElements()) {
            InsertableElement var5 = (InsertableElement)var3.get(var4.nextElement());
            if ((this.element = this.getDTD().getElementDeclaration(var5.name)) != null) {
               var2.put("<" + var5.name, this.readElement());
            }
         }

         depth = 2;
         this.element = var1;
         this.writingAttribute = false;
         return this.match(var2, "chars[current++]", false);
      }
   }

   public String max_attributes() {
      return "" + this.allAttributes.size();
   }

   public String set_attribute_types() {
      depth = 2;
      Iterator var1 = this.allAttributes.keySet().iterator();
      StringBuffer var2 = new StringBuffer();

      while(var1.hasNext()) {
         AttDef var3 = (AttDef)this.allAttributes.get(var1.next());
         format(var2, "htypes.put(\"" + var3.getName() + "\", \"" + AttDef.S_TYPESTR[var3.getDeclaredType()] + "\");");
      }

      return var2.toString();
   }

   public String valid_attribute() {
      if (this.currentAttribute.getDeclaredType() != 10) {
         return this.currentAttribute.getDefaultType() == 1 ? "&& !value.equals(\"" + this.currentAttribute.getDefaultStringValue() + "\")" : "&& false";
      } else {
         StringBuffer var1 = new StringBuffer();
         Enumeration var2 = this.currentAttribute.elements();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            var1.append(" && !value.equals(\"" + var3 + "\")");
         }

         return var1.toString();
      }
   }

   private static StringBuffer format(StringBuffer var0, String var1) {
      for(int var2 = depth; var2 > 0; --var2) {
         var0.append("  ");
      }

      return var0.append(var1).append(EOL);
   }

   private static StringBuffer format(StringBuffer var0, String var1, boolean var2) {
      if (var2) {
         try {
            BufferedReader var3 = new BufferedReader(new StringReader(var1));

            String var4;
            while((var4 = var3.readLine()) != null) {
               format(var0, var4);
            }
         } catch (IOException var5) {
         }
      } else {
         format(var0, var1);
      }

      return var0;
   }

   private String match(SortedMap var1, String var2, boolean var3) {
      StringBuffer var4 = new StringBuffer();
      this.makeSwitch(var4, var1, var1, 0, var2, (String)null, var3);
      return var4.toString();
   }

   private SortedMap makeTreeAtCurrentLookahead(Iterator var1, SortedMap var2, int var3) {
      String var4 = null;
      if (var3 == 0) {
         return var2;
      } else {
         TreeMap var5 = this.createTreeMap();

         while(true) {
            while(var1.hasNext()) {
               String var6 = (String)var1.next();
               if (var4 != null) {
                  if (var6.length() < var3 || var4.length() < var3 || var6.charAt(var3 - 1) != var4.charAt(var3 - 1)) {
                     return var5;
                  }

                  var5.put(var6, var2.get(var6));
               } else {
                  var5.put(var6, var2.get(var6));
                  var4 = var6;
               }
            }

            return var5;
         }
      }
   }

   private void makeSwitch(StringBuffer var1, SortedMap var2, SortedMap var3, int var4, String var5, String var6, boolean var7) {
      format(var1, "switch(" + var5 + ") {");
      Iterator var8 = var2.keySet().iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         SortedMap var10 = var2.tailMap(var9);
         int var11 = this.makeCase(var1, var10, var3, var4, var5, var7);

         while(true) {
            --var11;
            if (var11 <= 0) {
               break;
            }

            var8.next();
         }
      }

      this.makeDefault(var1, var4, var6, var7);
   }

   private void makeDefault(StringBuffer var1, int var2, String var3, boolean var4) {
      format(var1, "default:");
      ++depth;
      if (!this.writingAttribute && var2 == 0) {
         format(var1, "if(startCharacterData == -1) startCharacterData = current - 1;");
      } else if (!var4 && var3 != null && var3.charAt(0) == '<') {
         format(var1, "current -= 2; sendCharacters(chars, 0); done = true; continue;");
      } else {
         format(var1, "throw new SAXParseException(\"Could not parse: " + this.element.getName() + " starting at line \" + startLine, this);");
      }

      --depth;
      format(var1, "}");
   }

   private int makeCase(StringBuffer var1, SortedMap var2, SortedMap var3, int var4, String var5, boolean var6) {
      String var7 = (String)var2.firstKey();
      SortedMap var8 = this.makeTreeAtCurrentLookahead(var2.keySet().iterator(), var3, var4 + 1);
      if (var7.length() == var4) {
         var8.remove(var7);
         format(var1, "case '\\t': case ' ': case '\\r': case '\\n':");
         if (this.writingAttribute) {
            format(var1, "case '=':");
         } else {
            format(var1, "case '>':");
         }

         ++depth;
         format(var1, "current--;");
         format(var1, var3.get(var7).toString(), true);
         format(var1, "continue;");
      } else {
         char var9 = var7.charAt(var4);
         String var10;
         switch (var9) {
            case '\t':
               var10 = "\\t";
               break;
            case '\n':
               var10 = "\\n";
               break;
            case '\u000b':
            case '\f':
            default:
               var10 = (new Character(var9)).toString();
               break;
            case '\r':
               var10 = "\\r";
         }

         format(var1, "case '" + var10 + "':");
         ++depth;
         if (var8.size() != 1) {
            this.makeSwitch(var1, var8, var3, var4 + 1, var5, var10, var6);
         } else {
            if (var10.charAt(0) == '/') {
               format(var1, "try {");
               ++depth;
            }

            if (var7.length() != var4 + 1) {
               StringBuffer var11 = new StringBuffer();
               var11.append("match(chars, \"");

               for(int var12 = 0; var12 < var7.length() - var4 - 1; ++var12) {
                  var11.append(var7.charAt(var12 + var4 + 1));
               }

               var11.append("\", \"" + this.element.getName() + "\", startLine);");
               format(var1, var11.toString());
            }

            format(var1, var3.get(var7).toString(), true);
            if (var10.charAt(0) == '/') {
               --depth;
               format(var1, "} catch(Exception e) { current -= 2; sendCharacters(chars, 0); done = true; continue; }");
            }

            format(var1, "continue;");
         }
      }

      --depth;
      return var8.size();
   }

   public TreeMap createTreeMap() {
      TreeMap var1 = new TreeMap(new Comparator() {
         public int compare(Object var1, Object var2) {
            String var3 = (String)var1;
            String var4 = (String)var2;
            return var3.compareTo(var4);
         }
      });
      return var1;
   }
}
