package weblogic.wsee.tools.clientgen.stubgen;

import java.rmi.RemoteException;
import weblogic.wsee.tools.source.JsFault;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsReturnType;
import weblogic.wsee.tools.source.JsTypeBase;
import weblogic.wsee.util.jspgen.JspGenBase;
import weblogic.xml.schema.binding.internal.NameUtil;

public abstract class JavaFileGenBase extends JspGenBase {
   protected String packageName;
   protected String className;
   protected boolean shouldConvertInnerClassString = false;

   public Class<? extends Throwable> getRemoteExceptionClass() {
      return RemoteException.class;
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public void setClassName(String var1) {
      this.className = var1;
   }

   public void setShouldConvertInnerClassString(boolean var1) {
      this.shouldConvertInnerClassString = var1;
   }

   public static String getJavaName(String var0) {
      boolean var1 = Character.isLowerCase(var0.charAt(0));
      String var2 = NameUtil.getJavaName(var0);
      return var1 ? NameUtil.lowercaseFirstLetter(var2) : var2;
   }

   public String getThrowJAXRPCException() {
      return "throw new " + this.getRemoteExceptionClass().getName() + "(e.getMessage(), e.getLinkedCause());\n";
   }

   public String getThrowAllException() {
      return "throw new " + this.getRemoteExceptionClass().getName() + "(e.getMessage(), e );";
   }

   public boolean isTrue(String var1) {
      return "true".equals(var1);
   }

   public String getTypeFromPart(JsTypeBase var1) {
      return var1 == null ? "void" : this.convertInnerClassString(var1.getType());
   }

   public String argumentString(JsMethod var1) {
      return this.convertInnerClassString(var1.getArgumentString());
   }

   protected String convertInnerClassString(String var1) {
      if (this.shouldConvertInnerClassString) {
         var1 = var1.replace('$', '.');
      }

      return var1;
   }

   public String getJavaTypeName(Class var1) {
      if (var1 == null) {
         return "void";
      } else {
         int var2;
         for(var2 = 0; var1.isArray(); var1 = var1.getComponentType()) {
            ++var2;
         }

         String var3 = var1.getName();

         for(int var4 = 0; var4 < var2; ++var4) {
            var3 = var3 + "[]";
         }

         return var3;
      }
   }

   public String getJAXRPCClassName(String var1) {
      return NameUtil.getJAXRPCClassName(var1);
   }

   protected String getReturnStatement(JsReturnType var1) {
      if (var1 != null && !"void".equals(var1.getType())) {
         String var2 = var1.getType();
         String var3;
         if (var2.equals("int")) {
            var3 = "((java.lang.Integer)_result).intValue();";
         } else if (var2.equals("float")) {
            var3 = "((java.lang.Float)_result).floatValue();";
         } else if (var2.equals("long")) {
            var3 = "((java.lang.Long)_result).longValue();";
         } else if (var2.equals("double")) {
            var3 = "((java.lang.Double)_result).doubleValue();";
         } else if (var2.equals("short")) {
            var3 = "((java.lang.Short)_result).shortValue();";
         } else if (var2.equals("boolean")) {
            var3 = "((java.lang.Boolean)_result).booleanValue();";
         } else if (var2.equals("char")) {
            var3 = "((java.lang.Character)_result).charValue();";
         } else if (var2.equals("byte")) {
            var3 = "((java.lang.Byte)_result).byteValue();";
         } else {
            var3 = "(" + this.convertInnerClassString(var2) + ")_result;";
         }

         return "return " + var3 + "\n";
      } else {
         return "";
      }
   }

   protected String throwException(JsMethod var1) {
      StringBuffer var2 = new StringBuffer(" throws ");
      var2.append(this.getRemoteExceptionClass().getName());
      JsFault[] var3 = var1.getFaults();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         JsFault var5 = var3[var4];
         var2.append(", " + var5.getJsr109MappingFileExceptionClass());
      }

      return var2.toString();
   }

   public String extendsRemote() {
      return " extends java.rmi.Remote";
   }

   public String makeFirstCharBig(String var1) {
      if (var1.length() > 0) {
         char var2 = var1.charAt(0);
         var1 = Character.toUpperCase(var2) + var1.substring(1, var1.length());
      }

      return var1;
   }
}
