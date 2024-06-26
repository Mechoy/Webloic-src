package weblogic.xml.dtdc;

import com.ibm.xml.parser.AttDef;
import com.ibm.xml.parser.DTD;
import com.ibm.xml.parser.ElementDecl;
import com.ibm.xml.parser.InsertableElement;
import com.ibm.xml.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.BadOutputException;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class DTD2Java extends CodeGenerator {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final String EOL = System.getProperty("line.separator");
   static final String PACKAGE = "package";
   private ElementOutput currentOutput;
   private Vector outputs;
   private DTD dtd;
   private String packageName;
   private AttDef currentAttribute;
   private String currentSubElement;

   public DTD2Java(Getopt2 var1) {
      super(var1);
      var1.addOption("package", "weblogic.xml.objects", "Name of the base package for objects");
   }

   protected void extractOptionValues(Getopt2 var1) {
      this.packageName = var1.getOption("package", "weblogic.xml.objects");
   }

   public Enumeration outputs(Object[] var1) throws Exception {
      String[] var2 = (String[])((String[])var1);
      this.outputs = new Vector();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = new File((new File(var2[var3])).getAbsolutePath());
         FileInputStream var5 = new FileInputStream(var4);
         Parser var6 = new Parser(var4.getParentFile().toURL().toString());
         DTD var7 = var6.readDTDStream(var5);
         Enumeration var8 = var7.getElementDeclarations();

         while(var8.hasMoreElements()) {
            ElementDecl var9 = (ElementDecl)var8.nextElement();
            String var10 = var9.getName();
            String var11 = this.packageName;
            var11 = var11 + NameMangler.getpackage(var10);
            this.outputs.addElement(new ElementOutput(NameMangler.upcase(NameMangler.depackage(var10)) + ".java", var11, "element.j", var9, var7));
         }
      }

      return this.outputs.elements();
   }

   protected void prepare(CodeGenerator.Output var1) throws BadOutputException {
      this.currentOutput = (ElementOutput)var1;
   }

   public ElementDecl getElement() {
      return this.currentOutput.getElementDecl();
   }

   public DTD getDTD() {
      return this.currentOutput.getDTD();
   }

   public String element_realname() {
      return this.getElement().getName();
   }

   public String element_name() {
      return NameMangler.depackage(this.getElement().getName());
   }

   public String element_class_name() {
      return NameMangler.upcase(this.element_name());
   }

   public String package_name() {
      return this.packageName;
   }

   public String is_empty() {
      return this.getElement().getContentType() == 1 ? "true" : "false";
   }

   public String attributes() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Enumeration var2 = this.getDTD().getAttributeDeclarations(this.getElement().getName());

      while(var2.hasMoreElements()) {
         this.currentAttribute = (AttDef)var2.nextElement();
         var1.append(this.parse(this.getProductionRule("attribute")));
      }

      return var1.toString();
   }

   public String set_attributes() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Enumeration var2 = this.getDTD().getAttributeDeclarations(this.getElement().getName());

      while(var2.hasMoreElements()) {
         this.currentAttribute = (AttDef)var2.nextElement();
         var1.append(this.parse(this.getProductionRule("set_attribute")));
      }

      return var1.toString();
   }

   public String attribute_realname() {
      return this.currentAttribute.getName();
   }

   public String attribute_name() {
      return NameMangler.depackage(this.currentAttribute.getName());
   }

   public String attribute_varname() {
      return (this.attribute_name() + "Value").replace(':', '_').replace('-', '_');
   }

   public String attribute_default() {
      int var1 = this.currentAttribute.getDefaultType();
      return var1 != 1 && var1 != -1 ? "" : this.currentAttribute.getDefaultStringValue();
   }

   public String attribute_value_getter() {
      return "get" + NameMangler.upcase(this.attribute_name()) + "Attribute";
   }

   public String attribute_value_setter() {
      return "set" + NameMangler.upcase(this.attribute_name()) + "Attribute";
   }

   public String subelements() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Hashtable var2 = this.getDTD().prepareTable(this.getElement().getName());
      Enumeration var3 = var2.keys();

      while(var3.hasMoreElements()) {
         InsertableElement var4 = (InsertableElement)var2.get(var3.nextElement());
         if (this.getDTD().getElementDeclaration(var4.name) != null) {
            this.currentSubElement = NameMangler.depackage(var4.name);
            var1.append(this.parse(this.getProductionRule("subelement")));
         }
      }

      return var1.toString();
   }

   public String sub_element_name() {
      return NameMangler.depackage(this.currentSubElement);
   }

   public String sub_element_varname() {
      return this.sub_element_name() + "SubElements";
   }

   public String sub_elements_getter() {
      return this.sub_element_getter() + "s";
   }

   public String sub_element_class_name() {
      return NameMangler.upcase(this.sub_element_name());
   }

   public String sub_element_getter() {
      return "get" + NameMangler.upcase(this.sub_element_name()) + "Element";
   }

   public String sub_element_adder() {
      return "add" + NameMangler.upcase(this.sub_element_name()) + "Element";
   }
}
