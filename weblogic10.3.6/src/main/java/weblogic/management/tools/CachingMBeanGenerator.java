package weblogic.management.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.NotificationBroadcaster;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.BadOutputException;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class CachingMBeanGenerator extends CodeGenerator {
   public static final String CLASS_SUFFIX = "_Stub";
   private static final String VERBOSE = "verbose";
   private static final String PACKAGE = "package";
   private static final Set excludeClassList = new HashSet(Arrays.asList((Object[])(new String[]{"weblogic.management.configuration.AdminMBean"})));
   private static final Set excludeOperationsList = new HashSet(Arrays.asList((Object[])(new String[]{"registerConfigMBean", "unRegisterConfigMBean"})));
   private boolean verbose;
   private Output currentOutput;
   private Method method;
   private MBeanReflector.Attribute attribute;
   private MBeanReflector reflector;
   private Set attributeSet;
   private String attributeName;
   private String attributeFieldName;

   public CachingMBeanGenerator(Getopt2 var1) {
      super(var1);
      var1.addFlag("verbose", "Verbose output.");
      var1.setUsageArgs("[directory|file]");
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
            if (!excludeClassList.contains(var6)) {
               Class var7 = AttributeInfo.Helper.findClass(var6);
               if (!Throwable.class.isAssignableFrom(var7)) {
                  int var9 = var6.lastIndexOf(46);
                  String var8;
                  if (var9 != -1) {
                     var8 = var6.substring(0, var9);
                  } else {
                     var8 = "";
                  }

                  Output var10 = new Output(var7, var8);
                  String var11 = var10.getOutputFile().replace('/', File.separatorChar);
                  File var12 = this.targetFile(var11, var10.getPackage());
                  if (var5.lastModified() > var12.lastModified()) {
                     this.verbose(var5 + " has changed, regenerating.");
                     var2.put(var10, var10);
                  }
               }
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
      String var1 = this.currentOutput.getPackage();
      return var1 == null ? "" : "package " + var1 + ";";
   }

   public String genClassName() {
      return this.currentOutput.getClassName();
   }

   public String genInterfaceName() {
      return this.currentOutput.getInterface().getName();
   }

   public String genOptionalSVUID() {
      try {
         return "private static final long serialVersionUID = " + this.currentOutput.getInterface().getDeclaredField("CACHING_STUB_SVUID").getLong((Object)null) + "L;";
      } catch (IllegalAccessException var2) {
         throw new AssertionError(var2);
      } catch (NoSuchFieldException var3) {
         return "";
      }
   }

   public String genAccessors() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      MBeanReflector.Attribute[] var2 = this.reflector.getAttributes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.attribute = var2[var3];
         var1.append(this.parse(this.getProductionRule("attributeDeclaration")));
         this.method = this.reflector.getAttributeGetMethod(this.attribute);
         if (this.method != null) {
            var1.append(this.parse(this.getProductionRule("getter")));
         }

         this.method = this.reflector.getAttributeSetMethod(this.attribute);
         if (this.method != null) {
            var1.append(this.parse(this.getProductionRule("setter")));
         }
      }

      return var1.toString();
   }

   public String genOperations() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      MBeanReflector.Operation[] var2 = this.reflector.getOperations();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.method = var2[var3].getMethod();
         if (this.method.getDeclaringClass() != NotificationBroadcaster.class && !excludeOperationsList.contains(this.method.getName())) {
            var1.append(this.parse(this.getProductionRule("operation")));
         }
      }

      return var1.toString();
   }

   public String genOperationBody() throws CodeGenerationException {
      return this.method.getReturnType() == Void.TYPE ? this.parse(this.getProductionRule("voidOperationBody")) : this.parse(this.getProductionRule("operationBody"));
   }

   public String genAttributeCacheInvalidatorFragment() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      MBeanReflector.Attribute[] var2 = this.reflector.getAttributes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.attribute = var2[var3];
         var1.append(this.parse(this.getProductionRule("attributeCacheInvalidator")));
      }

      return var1.toString();
   }

   public String genAttributeName() {
      return this.attribute.getName();
   }

   public String genAttributeFieldName() {
      return this.attribute.getFieldName();
   }

   public String genAttributeTempName() {
      return this.attribute.getFieldName() + "Temp";
   }

   public String genAttributeType() {
      return this.prettyPrintType(this.attribute.getType());
   }

   public String genAttributeIsCached() {
      return this.attribute.getFieldName() + "IsCached";
   }

   public String genMethodName() {
      return this.method.getName();
   }

   public String genParameterList() {
      StringBuffer var1 = new StringBuffer();
      Class[] var2 = this.method.getParameterTypes();
      if (var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(", ");
            }

            var1.append(this.prettyPrintType(var2[var3]) + " arg" + var3);
         }
      }

      return var1.toString();
   }

   public String genTypeCastedExpressionforReturnedObject() {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.method.getReturnType();
      if (var2.isPrimitive()) {
         if (var2 == Integer.TYPE) {
            var1.append("((Integer)returnedObject).intValue()");
         } else if (var2 == Long.TYPE) {
            var1.append("((Long)returnedObject).longValue()");
         } else if (var2 == Boolean.TYPE) {
            var1.append("((Boolean)returnedObject).booleanValue()");
         } else if (var2 == Double.TYPE) {
            var1.append("((Double)returnedObject).doubleValue()");
         }
      } else if (var2.isArray()) {
         var1.append("(" + var2.getComponentType().getName() + "[]) returnedObject");
      } else {
         var1.append("(" + var2.getName() + ") returnedObject");
      }

      return var1.toString();
   }

   public String resetAttributeField() {
      if (this.attribute.getType() == Integer.TYPE) {
         return "0";
      } else if (this.attribute.getType() == Long.TYPE) {
         return "0";
      } else if (this.attribute.getType() == Double.TYPE) {
         return "0";
      } else {
         return this.attribute.getType() == Boolean.TYPE ? "false" : "null";
      }
   }

   public String genParameters() {
      StringBuffer var1 = new StringBuffer();
      Class[] var2 = this.method.getParameterTypes();
      if (var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(", ");
            }

            if (var2[var3].isPrimitive()) {
               var1.append("new " + AttributeInfo.Helper.wrapClass(var2[var3]).getName() + "(");
            }

            var1.append("arg" + var3);
            if (var2[var3].isPrimitive()) {
               var1.append(")");
            }
         }
      }

      return var1.toString();
   }

   public String genReturnType() {
      return this.prettyPrintType(this.method.getReturnType());
   }

   public String genSignature() {
      StringBuffer var1 = new StringBuffer();
      Class[] var2 = this.method.getParameterTypes();
      if (var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(", ");
            }

            var1.append("\"" + var2[var3].getName() + "\"");
         }
      }

      return var1.toString();
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

   public String genThrowException() {
      StringBuffer var1 = new StringBuffer();
      Class[] var2 = this.method.getExceptionTypes();
      if (var2.length != 0) {
         int var3;
         String var4;
         for(var3 = 0; var3 < var2.length; ++var3) {
            var4 = var2[var3].getName();
            if (this.method.getDeclaringClass() == ServerRuntimeMBean.class && (this.method.getName().equals("shutdown") || this.method.getName().equals("forceShutdown"))) {
               var1.append("\n\tif (thr instanceof weblogic.rmi.extensions.RemoteRuntimeException) {return;}");
            }

            var1.append("\n\tif (thr instanceof RuntimeException) throw (RuntimeException)thr; ");
            var1.append("\n\tif (thr instanceof " + var4 + ") throw (" + var4 + ")thr;");
         }

         var1.append("\n\tif (thr instanceof MBeanException) {");
         var1.append("\n\t\tThrowable target = ((MBeanException)thr).getTargetException();");

         for(var3 = 0; var3 < var2.length; ++var3) {
            var4 = var2[var3].getName();
            var1.append("\n\t\tif (target instanceof " + var4 + ") throw (" + var4 + ")target;");
         }

         var1.append("\n\t}");
      }

      var1.append("\n\tthrow new ManagementRuntimeException(thr);");
      return var1.toString();
   }

   protected void extractOptionValues(Getopt2 var1) {
      this.verbose = var1.hasOption("verbose");
   }

   protected void prepare(CodeGenerator.Output var1) throws BadOutputException {
      this.currentOutput = (Output)var1;
      this.reflector = new MBeanReflector(this.currentOutput.getInterface());
   }

   private void addSourceFilesFromDir(File var1, List var2) throws Exception {
      this.verbose("Looking in " + var1.getCanonicalPath());
      String[] var3 = var1.list();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].toLowerCase().endsWith("mbean.java")) {
            File var5 = new File(var1, var3[var4]);
            var2.add(var5);
         }
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

      public Output(Class var1, String var2) {
         super(getFileName(var1), "CachingMBean.j", var2);
         this.ifc = var1;
      }

      public static String getClassName(Class var0) {
         String var1 = var0.getName();
         int var2 = var1.lastIndexOf(46);
         return var2 > -1 ? var1.substring(var2 + 1) + "_Stub" : var1 + "_Stub";
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
   }
}
