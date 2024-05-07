package weblogic.management.tools;

import java.lang.reflect.Method;
import java.util.Locale;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import weblogic.management.info.ExtendedOperationInfo;
import weblogic.utils.reflect.UniqueMethod;

class OperationInfo extends MBeanOperationInfo implements ExtendedOperationInfo {
   private static final long serialVersionUID = 6938333905828247476L;
   String methodClassName = null;
   private String parameterString = "";
   private String parameterNames = "";
   private String exceptionString = "";
   private String[] legalChecks = null;
   private String[] legalResponses = null;
   private transient Method method = null;
   private boolean dynamic = false;

   public OperationInfo(String var1, Method var2, boolean var3, String[] var4, String[] var5) {
      super(var1, var2);
      this.dynamic = var3;
      this.method = var2;
      this.methodClassName = var2.getDeclaringClass().getName();
      this.legalChecks = var4;
      this.legalResponses = var5;
      this.initialize();
   }

   private static String getType(String var0) {
      if (var0.startsWith("[L")) {
         StringBuffer var1 = new StringBuffer(var0);
         var1.setLength(var1.length() + 1);
         var1.setCharAt(var1.length() - 2, '[');
         var1.setCharAt(var1.length() - 1, ']');
         var1 = var1.reverse();
         var1.setLength(var1.length() - 2);
         var1 = var1.reverse();
         return var1.toString();
      } else {
         return var0;
      }
   }

   private static String getParamName(String var0, int var1) {
      if (var0.endsWith("[]")) {
         var0 = var0.substring(0, var0.length() - 2);
      }

      int var2 = var0.lastIndexOf(46);
      if (var2 > -1) {
         var0 = var0.substring(var2 + 1, var0.length());
      }

      var0 = var0.substring(0, 1).toLowerCase(Locale.US) + var0.substring(1, var0.length());
      return "_wl_" + var0 + "_" + var1;
   }

   public Method getMethod() {
      if (this.method != null) {
         return this.method;
      } else {
         try {
            Class var1 = Class.forName(this.methodClassName);
            MBeanParameterInfo[] var2 = this.getSignature();
            Class[] var3 = new Class[var2.length];

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3[var4] = AttributeInfo.Helper.findClass(var2[var4].getType());
            }

            this.method = UniqueMethod.intern(var1.getMethod(this.name, var3));
            return this.method;
         } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
            throw new RuntimeException("error deserializing OperationInfo " + this.name);
         } catch (Exception var6) {
            var6.printStackTrace();
            throw new RuntimeException("error deserializing OperationInfo " + this.name);
         }
      }
   }

   public String getParameterString() {
      return this.parameterString;
   }

   public String getParameterNames() {
      return this.parameterNames;
   }

   public String getExceptionString() {
      return this.exceptionString;
   }

   public String getReturnType() {
      return getType(this.getMethod().getReturnType().getName());
   }

   public boolean isDynamic() {
      return this.dynamic;
   }

   public String getLegalCheck() {
      return this.legalChecks != null && this.legalChecks.length > 0 ? this.legalChecks[0] : null;
   }

   public String[] getLegalChecks() {
      return this.legalChecks;
   }

   public String[] getLegalResponses() {
      return this.legalResponses;
   }

   public String getLegalResponse() {
      return this.legalResponses != null && this.legalResponses.length > 0 ? this.legalResponses[0] : null;
   }

   public String toString() {
      String var1 = super.toString();
      var1 = var1 + ", dynamic=" + this.dynamic;
      return var1;
   }

   private void initialize() {
      Class[] var1 = this.getMethod().getParameterTypes();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = getType(var1[var2].getName());
         String var4 = "value";
         if (var1.length > 1) {
            var4 = getParamName(var3, var2);
         }

         if (this.parameterString.length() > 0) {
            this.parameterString = this.parameterString + ", ";
         }

         this.parameterString = this.parameterString + var3 + " " + var4;
         this.parameterNames = this.parameterNames + var4;
         if (var2 < var1.length - 1) {
            this.parameterNames = this.parameterNames + ", ";
         }
      }

      Class[] var5 = this.getMethod().getExceptionTypes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (this.exceptionString.length() > 0) {
            this.exceptionString = this.exceptionString + ",";
         } else {
            this.exceptionString = "\n    throws ";
         }

         this.exceptionString = this.exceptionString + var5[var6].getName();
      }

   }
}
