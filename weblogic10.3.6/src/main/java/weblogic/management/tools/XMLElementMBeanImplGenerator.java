package weblogic.management.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import weblogic.management.WebLogicMBean;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.BadOutputException;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class XMLElementMBeanImplGenerator extends CodeGenerator {
   private static final String VERBOSE = "verbose";
   private static final String PACKAGE = "package";
   private boolean verbose;
   private Output m_currentOutput;
   private Method method;
   private MBeanReflector.Attribute attribute;
   private MBeanReflector m_reflector;
   private Set attributeSet;
   private String attributeName;
   private String attributeFieldName;
   private TagParser m_currentTagParser;

   public XMLElementMBeanImplGenerator(Getopt2 var1) {
      super(var1);
      var1.addFlag("verbose", "Verbose output.");
      var1.setUsageArgs("[directory|file]");
   }

   public static String genDefaultValue(TagParser var0, Method var1, Output var2) throws CodeGenerationException {
      TaggedMethod[] var3 = var0.getMethodsWithTag("@default");
      Class var4 = var1.getReturnType();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (-1 != var3[var5].getMethodSignature().indexOf(var1.getName())) {
            String var6 = var3[var5].getTagValue("@default");
            if (var4.isAssignableFrom(String.class)) {
               return " = " + var6;
            }

            if (var4.isAssignableFrom(Boolean.TYPE)) {
               return " = " + Boolean.valueOf(var6);
            }

            if (var4.isAssignableFrom(Integer.TYPE)) {
               try {
                  return " = " + Integer.valueOf(var6);
               } catch (NumberFormatException var8) {
                  throw new CodeGenerationException("Error parsing int for " + var1 + " of the " + var2.getClassName() + " interface.", var8);
               }
            }

            throw new CodeGenerationException("Can't set default value for " + var1 + ". Its type isn't supported");
         }
      }

      return "";
   }

   public String getterMethodName() {
      return "get" + this.attribute.getName();
   }

   public Enumeration outputs(Object[] var1) throws Exception {
      try {
         Hashtable var2 = new Hashtable();
         ArrayList var3 = new ArrayList();

         File var5;
         for(int var4 = 0; var4 < var1.length; ++var4) {
            var5 = new File((String)var1[var4]);
            if (!var5.exists()) {
               throw new FileNotFoundException(var5.getPath());
            }

            if (var5.isDirectory()) {
               this.addSourceFilesFromDir(var5, var3);
            } else {
               var3.add(var5);
            }
         }

         Iterator var14 = var3.iterator();

         while(var14.hasNext()) {
            var5 = (File)var14.next();
            String var6 = var5.getPath().replace(File.separatorChar, '.');
            var6 = var6.substring(0, var6.length() - 5);
            Class var7 = AttributeInfo.Helper.findClass(var6);
            if (!Throwable.class.isAssignableFrom(var7)) {
               int var9 = var6.lastIndexOf(46);
               String var8;
               if (var9 != -1) {
                  var8 = var6.substring(0, var9);
               } else {
                  var8 = "";
               }

               Output var10 = new Output(var7, var8, var5.getAbsolutePath());
               String var11 = var10.getOutputFile().replace('/', File.separatorChar);
               this.targetFile(var11, var10.getPackage());
               this.verbose(var5 + " has changed, regenerating.");
               var2.put(var10, var10);
            }
         }

         return var2.elements();
      } catch (Throwable var13) {
         var13.printStackTrace();
         return null;
      }
   }

   public String genAuthor() {
      return "@author";
   }

   public String genPackageDeclaration() {
      String var1 = this.m_currentOutput.getPackage();
      return var1 == null ? "" : "package " + var1 + ";";
   }

   public String genClassName() {
      return this.m_currentOutput.getClassName();
   }

   public String genInterfaceName() {
      return this.m_currentOutput.getInterface().getName();
   }

   public String genAttributes() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      HashSet var2 = this.getAttributes();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         this.attribute = (MBeanReflector.Attribute)var3.next();
         if (this.isAttributeArray()) {
            var1.append(this.parse(this.getProductionRule("attributeFieldArrayDeclaration")));
         } else {
            var1.append(this.parse(this.getProductionRule("attributeFieldDeclaration")));
         }
      }

      return var1.toString();
   }

   public String genAccessors() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      HashSet var2 = this.getAttributes();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         this.attribute = (MBeanReflector.Attribute)var3.next();
         this.method = this.m_reflector.getAttributeGetMethod(this.attribute);
         if (this.method != null) {
            if (this.isAttributeArray()) {
               var1.append(this.parse(this.getProductionRule("arrayGetter")));
            } else if (this.isAttributeBoolean()) {
               var1.append(this.parse(this.getProductionRule("isGetter")));
            } else {
               var1.append(this.parse(this.getProductionRule("getter")));
            }
         }

         this.method = this.m_reflector.getAttributeSetMethod(this.attribute);
         if (this.method != null) {
            if (this.isAttributeArray()) {
               var1.append(this.parse(this.getProductionRule("arraySetter")));
            } else {
               var1.append(this.parse(this.getProductionRule("setter")));
            }
         }

         if (this.isAttributeArray()) {
            try {
               this.method = this.m_reflector.getAttributeAddMethod(this.attribute);
            } catch (Exception var6) {
               throw new CodeGenerationException("Error getting add method", var6);
            }

            if (this.method != null) {
               var1.append(this.parse(this.getProductionRule("arrayAdder")));
            }

            try {
               this.method = this.m_reflector.getAttributeRemoveMethod(this.attribute);
            } catch (Exception var5) {
               throw new CodeGenerationException("Error getting remove method", var5);
            }

            if (this.method != null) {
               var1.append(this.parse(this.getProductionRule("arrayRemover")));
            }
         }
      }

      return var1.toString();
   }

   public String genChildrenRegistration() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      HashSet var2 = this.getAttributes();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         this.attribute = (MBeanReflector.Attribute)var3.next();
         if (this.isAttributeMBean()) {
            if (this.isAttributeArray()) {
               var1.append(this.parse(this.getProductionRule("arrayRegister")));
            } else {
               var1.append(this.parse(this.getProductionRule("childRegister")));
            }
         }
      }

      return var1.toString();
   }

   public String emptyStringCheck() {
      return this.isAttributeString() ? "if (value != null && value.trim().length() == 0) value = null;\n" : "";
   }

   public String attributeIsSetExpression() {
      if (!this.isAttributePrimitive()) {
         return "(value != null)";
      } else {
         return this.isAttributeBoolean() ? "true" : "(value != " + this.unsetNumberLiteral() + ")";
      }
   }

   public String genOperations() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      MBeanReflector.Operation[] var2 = this.m_reflector.getOperations();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.method = var2[var3].getMethod();
         var1.append(this.parse(this.getProductionRule("operation")));
      }

      return var1.toString();
   }

   public String genAttributeName() {
      return this.attribute.getName();
   }

   public String genAttributeFieldName() {
      return this.attribute.getFieldName();
   }

   public String genAttributeFieldSetTesterName() {
      String var1 = this.genAttributeFieldName();
      return "isSet_" + this.genAttributeFieldName();
   }

   public String genDefaultValue() throws CodeGenerationException {
      Method var1 = this.m_reflector.getAttributeGetMethod(this.attribute);
      return genDefaultValue(this.m_currentTagParser, var1, this.m_currentOutput);
   }

   public String genAttributeType() {
      return this.prettyPrintType(this.attribute.getType());
   }

   public String genAttributeTypeMinusArrayBrackets() {
      return this.attribute.getType().getComponentType().getName();
   }

   public String genMethodName() {
      return this.method.getName();
   }

   public String genReturnType() {
      return this.prettyPrintType(this.method.getReturnType());
   }

   public String genThrowsClause() {
      StringBuffer var1 = new StringBuffer();
      Class[] var2 = this.method.getExceptionTypes();
      if (var2.length != 0) {
         var1.append("throws ");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(", ");
            }

            var1.append(var2[var3].getName());
         }
      }

      return var1.toString();
   }

   public String toXML() {
      String var1 = null;

      try {
         var1 = ToXML.toXML(this.m_currentTagParser);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   protected void extractOptionValues(Getopt2 var1) {
      this.verbose = var1.hasOption("verbose");
   }

   protected void prepare(CodeGenerator.Output var1) throws BadOutputException {
      this.m_currentOutput = (Output)var1;
      Output var2 = (Output)var1;
      this.m_currentTagParser = new TagParser(var2.getAbsolutePath());

      try {
         this.m_currentTagParser.parse();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.m_reflector = new MBeanReflector(this.m_currentOutput.getInterface());
   }

   private boolean isAttributeString() {
      return this.attribute.getType().getName().equals("java.lang.String");
   }

   private boolean isAttributePrimitive() {
      if (this.isAttributeArray()) {
         return false;
      } else {
         String var1 = this.attribute.getType().getName();
         return "boolean".equals(var1) || "byte".equals(var1) || "short".equals(var1) || "char".equals(var1) || "int".equals(var1) || "float".equals(var1) || "long".equals(var1) || "double".equals(var1);
      }
   }

   private boolean isAttributeMBean() {
      return this.isAttributeArray() ? this.genAttributeTypeMinusArrayBrackets().endsWith("MBean") : this.attribute.getType().getName().endsWith("MBean");
   }

   private boolean isAttributeArray() {
      return this.attribute.getType().isArray();
   }

   private boolean isAttributeBoolean() {
      return this.attribute.getType().getName().equals("boolean");
   }

   private HashSet getAttributes() {
      HashSet var1 = new HashSet(Arrays.asList((Object[])this.m_reflector.getAttributes()));
      MBeanReflector var2 = new MBeanReflector(WebLogicMBean.class);
      List var3 = Arrays.asList((Object[])var2.getAttributes());
      var1.removeAll(var3);
      return var1;
   }

   private void addSourceFilesFromDir(File var1, List var2) throws Exception {
      this.verbose("Looking in " + var1.getCanonicalPath());
      String[] var3 = var1.list();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].toLowerCase(Locale.US).endsWith("mbean.java")) {
            File var5 = new File(var1, var3[var4]);
            var2.add(var5);
         }
      }

   }

   private String unsetNumberLiteral() {
      String var1 = this.attribute.getType().getName();
      if (!"byte".equals(var1) && !"short".equals(var1) && !"int".equals(var1)) {
         if ("char".equals(var1)) {
            return "0xffff";
         } else if ("float".equals(var1)) {
            return "-1.0";
         } else if ("long".equals(var1)) {
            return "-1L";
         } else {
            return "double".equals(var1) ? "-1.0D" : "__FIXME__THIS__IS__BROKEN__!!!";
         }
      } else {
         return "-1";
      }
   }

   private String prettyPrintType(Class var1) {
      return var1.isArray() ? var1.getComponentType().getName() + "[]" : var1.getName();
   }

   private void verbose(String var1) {
      if (this.verbose) {
         this.info(var1);
      }

   }

   private void info(String var1) {
      System.out.println("<MBean Compiler>" + var1);
   }

   private static class Output extends CodeGenerator.Output {
      private Class ifc;
      private String clazz;
      private String absolutePath;

      public Output(Class var1, String var2, String var3) {
         super(getFileName(var1), "XMLElementMBeanImpl.j", var2);
         this.ifc = var1;
         this.absolutePath = var3;
      }

      public static String getClassName(Class var0) {
         String var1 = var0.getName();
         int var2 = var1.lastIndexOf(46);
         return var2 > -1 ? var1.substring(var2 + 1) + "Impl" : var1 + "Impl";
      }

      private static String getFileName(Class var0) {
         return getClassName(var0) + ".java";
      }

      public String getClassName() {
         return getClassName(this.ifc);
      }

      public Class getInterface() {
         return this.ifc;
      }

      public String getAbsolutePath() {
         return this.absolutePath;
      }
   }
}
