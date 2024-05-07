package weblogic.servlet.ejb2jsp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.IndentingWriter;

public class EJBMethodGenerator extends CodeGenerator {
   protected Hashtable rules = new Hashtable();
   protected BeanGenerator bg;
   protected String generatedClassName;
   protected EJBMethodDescriptor md;

   public EJBMethodDescriptor getDescriptor() {
      return this.md;
   }

   static void p(String var0) {
      System.err.println("[EJBMethodDesc]: " + var0);
   }

   public EJBMethodGenerator(Getopt2 var1, BeanGenerator var2, EJBMethodDescriptor var3) {
      super(var1);
      this.md = var3;
      this.bg = var2;
   }

   public String getTagName() {
      return this.md.getTagName();
   }

   protected String getTemplatePath() {
      return "/weblogic/servlet/ejb2jsp/ejbtag.j";
   }

   public String getRemoteClassName() {
      return this.bg.getDD().getRemoteType();
   }

   public String getMethodName() {
      return this.md.getName();
   }

   public String getReturnType() {
      return this.md.getReturnType();
   }

   public String toString() {
      return this.getReturnType() + " " + this.getMethodName() + "(" + this.getArgListString() + ")";
   }

   protected Enumeration outputs(Object[] var1) throws IOException, CodeGenerationException {
      TaglibOutput var2 = new TaglibOutput(this.simpleClassName() + ".java", this.getTemplatePath(), this.bg.getPackage());
      this.processTemplate(this.rules, var2, this.getTemplatePath());
      Vector var3 = new Vector();
      var3.addElement(var2);
      if (this.needExtraInfoClass()) {
         TaglibOutput var4 = new TaglibOutput(this.simpleTagClassName() + ".java", this.getTagExtraTemplatePath(), this.bg.getPackage());
         var3.addElement(var4);
      }

      return var3.elements();
   }

   public String getArgListString() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append(var2[var4].getType());
         var1.append(' ');
         var1.append(var2[var4].getName());
         if (var4 + 1 != var3) {
            var1.append(',');
         }
      }

      return var1.toString();
   }

   public int hashCode() {
      return this.getMethodName().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof EJBMethodGenerator)) {
         return false;
      } else {
         EJBMethodGenerator var2 = (EJBMethodGenerator)var1;
         if (!this.getMethodName().equals(var2.getMethodName())) {
            return false;
         } else if (!this.getReturnType().equals(var2.getReturnType())) {
            return false;
         } else {
            MethodParamDescriptor[] var3 = this.md.getParams();
            MethodParamDescriptor[] var4 = var2.md.getParams();
            if (var3.length != var4.length) {
               return false;
            } else {
               for(int var5 = 0; var5 < var3.length; ++var5) {
                  if (!var3[var5].getType().equals(var4[var5].getType())) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   protected PrintWriter makeOutputStream(File var1) throws IOException {
      try {
         return "false".equalsIgnoreCase(System.getProperty("weblogic.codegen.indent")) ? super.makeOutputStream(var1) : new PrintWriter(new IndentingWriter(new FileWriter(var1)));
      } catch (SecurityException var3) {
         return super.makeOutputStream(var1);
      }
   }

   public void setGeneratedClassName(String var1) {
      this.generatedClassName = var1;
   }

   public String generated_class_name() {
      if (this.generatedClassName == null) {
         String var1 = this.getRemoteClassName();
         int var2 = var1.lastIndexOf(46);
         if (var2 > 0) {
            var1 = var1.substring(var2 + 1);
         }

         this.generatedClassName = this.bg.getPackage() + "._" + var1 + "_" + this.getMethodName() + "Tag";
      }

      return this.generatedClassName;
   }

   public String packageStatement() {
      return "package " + this.bg.getPackage() + ";";
   }

   public String simpleClassName() {
      String var1 = this.generated_class_name();
      int var2 = var1.lastIndexOf(46);
      if (var2 > 0) {
         var1 = var1.substring(var2 + 1);
      }

      return var1;
   }

   public String evalOut() {
      if (Utils.isVoid(this.md.getReturnType())) {
         return "";
      } else {
         boolean var1 = this.md.isEvalOut();
         return !var1 ? "// don't eval return to output" : "// eval-out is true - print result\npageContext.getOut().print(_ret);\npageContext.getOut().flush();\n";
      }
   }

   public String importStatements() {
      return this.bg.getImportString();
   }

   public String varDeclaration() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append(var2[var4].getType() + " " + var2[var4].getName() + ";\n");
      }

      return var1.toString();
   }

   public String boolDeclaration() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append("boolean " + var2[var4].getName() + "_isSet = false;\n");
      }

      return var1.toString();
   }

   public String setterMethods() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append("public void set" + var2[var4].getName() + "(" + var2[var4].getType() + " x) {\n");
         var1.append("this." + var2[var4].getName() + " = x;\n");
         var1.append("this." + var2[var4].getName() + "_isSet = true;\n}\n");
      }

      return var1.toString();
   }

   public String remoteType() {
      return this.getRemoteClassName();
   }

   public String invoke() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getMethodName());
      var1.append("(");
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append(var2[var4].getName());
         if (var4 != var3 - 1) {
            var1.append(", ");
         }
      }

      var1.append(")");
      return var1.toString();
   }

   public String releaseBody() {
      StringBuffer var1 = new StringBuffer();
      if (!Utils.isVoid(this.md.getReturnType())) {
         var1.append("_return = null;\n");
      }

      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.append(var2[var4].getName() + "_isSet = false;\n");
      }

      var1.append("super.release();\n");
      return var1.toString();
   }

   public String verifyAttributes() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4].getDefault();
         var1.append("if (!" + var2[var4].getName() + "_isSet) ");
         if ("EXPRESSION".equalsIgnoreCase(var5)) {
            var1.append(var2[var4].getName() + " = " + var2[var4].getDefaultValue() + ";\n");
         } else if ("METHOD".equalsIgnoreCase(var5)) {
            var1.append(var2[var4].getName() + " = _get_" + var2[var4].getName() + "_default();\n");
         } else {
            var1.append("throw new JspException(\"" + var2[var4].getName() + "not set and has no default\");\n");
         }
      }

      return var1.toString();
   }

   public String retDecl() {
      String var1 = this.md.getReturnType();
      return Utils.isVoid(var1) ? "" : var1 + " _ret = ";
   }

   public String retSet() {
      String var1 = this.md.getReturnType();
      if (Utils.isVoid(var1)) {
         return "";
      } else {
         return Utils.isPrimitive(var1) ? "if (_return != null) { pageContext.setAttribute(_return, " + Utils.primitive2Object(var1, "_ret") + "); }\n" : "if (_return != null) { pageContext.setAttribute(_return, _ret); }\n";
      }
   }

   public String homeJNDIName() {
      return '"' + this.bg.getDD().getJNDIName() + '"';
   }

   public String homeType() {
      return this.bg.getDD().getHomeType();
   }

   public String homeTagInterfaceName() {
      return this.bg.getHomeTagInterfaceName();
   }

   public String createInvoke() {
      return "create()";
   }

   public String returnAttribute() {
      return Utils.isVoid(this.md.getReturnType()) ? "" : "String _return = null;\npublic void set_return(String r) { _return = r; }";
   }

   public String defaultMethods() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         if ("METHOD".equalsIgnoreCase(var2[var4].getDefault())) {
            var1.append("private " + var2[var4].getType() + " _get_" + var2[var4].getName() + "_default() throws Exception {\n");
            var1.append("//begin user code....\n");
            var1.append(var2[var4].getDefaultMethod());
            var1.append("//end user code....\n");
            var1.append("\n}\n");
         }
      }

      return var1.toString();
   }

   public String getEJB() throws CodeGenerationException {
      return this.bg.getDD().isStatefulBean() ? this.parse((String)this.rules.get("entityBeanGetEJB")) : this.parse((String)this.rules.get("sessionBeanGetEJB"));
   }

   public boolean needExtraInfoClass() {
      int var1 = this.md.getParams().length;
      if (var1 > 0) {
         return true;
      } else {
         return !Utils.isVoid(this.md.getReturnType());
      }
   }

   public String getTagExtraTemplatePath() {
      return "/weblogic/servlet/ejb2jsp/ejbTEI.j";
   }

   public String defaultReturnVar() {
      if (Utils.isVoid(this.md.getReturnType())) {
         return "";
      } else {
         String var1 = "private static final String DEFAULT_RETURN_VAR = ";
         String var2 = this.md.getReturnVarName();
         if (var2 == null) {
            var1 = var1 + "null;";
         } else {
            var1 = var1 + "\"" + var2 + "\";";
         }

         return var1;
      }
   }

   public String getVariableInfoBody() {
      if (Utils.isVoid(this.md.getReturnType())) {
         return "//returns void\nreturn null;";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("String varname = data.getAttributeString(\"_return\");\n");
         var1.append("if (varname == null) return null;\n");
         var1.append("VariableInfo[] info = new VariableInfo[1];\n");
         if (this instanceof HomeCollectionGenerator) {
            var1.append("info[0] = new VariableInfo(varname, \"" + this.bg.getDD().getRemoteType() + "\", true, " + "VariableInfo.NESTED);\n");
         } else {
            String var2 = this.md.getReturnType();
            if (Utils.isPrimitive(var2)) {
               var2 = Utils.primitive2Object(var2);
            }

            var1.append("info[0] = new VariableInfo (varname,");
            var1.append("\"" + var2 + "\",");
            var1.append("true,VariableInfo.AT_BEGIN);\n");
         }

         var1.append("return info;");
         return var1.toString();
      }
   }

   public String simpleTagClassName() {
      return this.simpleClassName() + "TEI";
   }

   public String isValidBody() {
      StringBuffer var1 = new StringBuffer();
      MethodParamDescriptor[] var2 = this.md.getParams();
      int var3 = var2.length;
      if (var3 == 0) {
         return "return true ;";
      } else {
         for(int var4 = 0; var4 < var3; ++var4) {
            if (!"EXPRESSION".equalsIgnoreCase(var2[var4].getDefault()) && !"METHOD".equalsIgnoreCase(var2[var4].getDefault())) {
               var1.append("if (data.getAttribute(\"" + var2[var4].getName() + "\") == null) return false;\n");
            }
         }

         var1.append("return true;");
         return var1.toString();
      }
   }
}
