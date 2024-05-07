package weblogic.wsee.util;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import com.bea.util.jam.JamClassLoader;
import com.bea.util.jam.JamService;
import com.bea.util.jam.JamServiceFactory;
import com.bea.util.jam.JamServiceParams;
import com.sun.javadoc.ClassDoc;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import weblogic.utils.StringUtils;
import weblogic.wsee.tools.WsBuildException;

public class JamUtil {
   private JamUtil() {
   }

   public static boolean isObjectMethod(JMethod var0) {
      if (var0.getParent().getQualifiedName().equals(Object.class.getName())) {
         return true;
      } else if (!var0.getSimpleName().equals("finalize") && !var0.getSimpleName().equals("toString") && !var0.getSimpleName().equals("hashCode") && !var0.getSimpleName().equals("clone") || var0.getParameters() != null && var0.getParameters().length != 0) {
         return var0.getSimpleName().equals("equals") && var0.getParameters() != null && var0.getParameters().length == 1 && var0.getParameters()[0].getType().getQualifiedName().equals(Object.class.getName());
      } else {
         return true;
      }
   }

   public static String getAnnotationStringValue(JAnnotation var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         JAnnotationValue var2 = var0.getValue(var1);
         if (var2 == null) {
            return null;
         } else {
            String var3 = var2.asString();
            return StringUtils.isEmptyString(var3) ? null : var3;
         }
      }
   }

   public static boolean getAnnotationBooleanValue(JAnnotation var0, String var1, boolean var2) {
      JAnnotationValue var3 = var0.getValue(var1);
      return var3 == null ? var2 : var3.asBoolean();
   }

   public static <T extends Enum<T>> T getAnnotationEnumValue(JAnnotation var0, String var1, Class<T> var2, T var3) {
      String var4 = getAnnotationStringValue(var0, var1);
      return StringUtil.isEmpty(var4) ? var3 : Enum.valueOf(var2, var4);
   }

   public static JClass parseSource(File var0, String var1, String var2) throws WsBuildException {
      return parseSource((File)var0, var1, var2, (String)null);
   }

   public static JClass[] parseSource(File[] var0, String var1, String var2) throws WsBuildException {
      return parseSource((File[])var0, var1, var2, (String)null);
   }

   public static JClass parseSource(File var0, String var1, String var2, String var3) throws WsBuildException {
      JClass[] var4 = parseSource(new File[]{var0}, var1, var2, var3);
      if (var4 != null && var4.length != 0) {
         if (var4.length > 1) {
            throw new WsBuildException("Too many source files." + var4.length);
         } else {
            return var4[0];
         }
      } else {
         throw new WsBuildException("Unable load source file using jam: " + var0);
      }
   }

   public static JClass[] parseSource(File[] var0, String var1, String var2, String var3) throws WsBuildException {
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();

      JClass[] var19;
      try {
         JamServiceFactory var5 = JamServiceFactory.getInstance();
         JamServiceParams var6 = var5.createServiceParams();
         File[] var7 = var0;
         int var8 = var0.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            File var10 = var7[var9];
            var6.includeSourceFile(var10);
         }

         var6.setProperty("javadoc.args", "-source 1.5");
         if (var3 != null) {
            var6.setCharacterEncoding(var3);
         }

         StringTokenizer var16;
         if (var1 != null) {
            var16 = new StringTokenizer(var1, File.pathSeparator);

            while(var16.hasMoreTokens()) {
               var6.addSourcepath(new File(var16.nextToken()));
            }
         }

         if (var2 != null) {
            var16 = new StringTokenizer(var2, File.pathSeparator);

            while(var16.hasMoreTokens()) {
               var6.addClasspath(new File(var16.nextToken()));
            }
         }

         JamService var17 = var5.createService(var6);
         JClass[] var18 = var17.getAllClasses();
         if (var18.length != var0.length) {
            throw new WsBuildException("Wrong number of class files.  Expected " + var0.length + ", got " + var18.length);
         }

         var19 = var18;
      } catch (IOException var14) {
         throw new WsBuildException("Unable to load source files");
      } finally {
         Thread.currentThread().setContextClassLoader(var4);
      }

      return var19;
   }

   public static JMethod getOverrideMethod(JClass var0, JMethod var1) {
      if (var1.getContainingClass() == var0) {
         return var1;
      } else {
         JMethod[] var2 = var0.getMethods();
         JMethod[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            JMethod var6 = var3[var5];
            if (var6.getSimpleName().equals(var1.getSimpleName()) && (var1.getReturnType().isVoidType() && var6.getReturnType().isVoidType() || var1.getReturnType().isAssignableFrom(var6.getReturnType()))) {
               JParameter[] var7 = var6.getParameters();
               JParameter[] var8 = var1.getParameters();
               if (var7.length == var8.length) {
                  boolean var9 = true;

                  for(int var10 = 0; var9 && var10 < var7.length; ++var10) {
                     var9 = var8[var10].getType().isAssignableFrom(var7[var10].getType());
                  }

                  if (var9) {
                     JClass[] var15 = var6.getExceptionTypes();
                     JClass[] var11 = var1.getExceptionTypes();

                     for(int var12 = 0; var9 && var12 < var15.length; ++var12) {
                        boolean var13 = false;

                        for(int var14 = 0; !var13 && var14 < var11.length; ++var14) {
                           var13 = var11[var14].isAssignableFrom(var15[var12]);
                        }

                        var9 = var13;
                     }

                     if (var9) {
                        return var6;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   public static JClass loadJClass(String var0, ClassLoader var1) {
      JamServiceFactory var2 = JamServiceFactory.getInstance();
      JamClassLoader var3 = var2.createJamClassLoader(var1);
      return loadJClass(var0, var3, false);
   }

   public static JClass loadJClass(String var0, JamClassLoader var1, boolean var2) {
      JClass var3 = var1.loadClass(var0);
      if (var2 && var3.getContainingClass() == null) {
         var3 = loadInnerJClass(var1, var3);
      }

      return var3;
   }

   private static JClass loadInnerJClass(JamClassLoader var0, JClass var1) {
      JClass var2 = var1;
      if (isInnerClass(var1)) {
         String var3 = var1.getQualifiedName();
         int var4 = var3.lastIndexOf(46);
         if (var4 != -1) {
            var3 = var3.substring(0, var4) + "$" + var3.substring(var4 + 1);
            var2 = var0.loadClass(var3);
         }
      }

      return var2;
   }

   private static boolean isInnerClass(JClass var0) {
      Object var1 = var0.getArtifact();
      if (var1 instanceof ClassDoc) {
         ClassDoc var2 = (ClassDoc)var1;
         return var2.containingClass() != null;
      } else {
         return false;
      }
   }

   public static boolean isSoapEncJavaPrimitiveWrapperClass(JClass var0) {
      return soapEncodedTypeForJavaPrimitiveWrapper(var0) != null;
   }

   public static String soapEncodedTypeForJavaPrimitiveWrapper(JClass var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getQualifiedName();
         if (var1.equals("java.lang.Integer")) {
            return "int";
         } else if (var1.equals("java.lang.Float")) {
            return "float";
         } else if (var1.equals("java.lang.Long")) {
            return "long";
         } else if (var1.equals("java.lang.Double")) {
            return "double";
         } else if (var1.equals("java.lang.Byte")) {
            return "byte";
         } else if (var1.equals("java.lang.Short")) {
            return "short";
         } else {
            return var1.equals("java.lang.Boolean") ? "boolean" : null;
         }
      }
   }

   public static boolean isFinalizeMethod(JMethod var0) {
      return "finalize".equals(var0.getSimpleName()) && (var0.getParameters() == null || var0.getParameters().length == 0);
   }

   public static boolean equals(JAnnotation var0, JAnnotation var1) {
      Map var2 = buildMap(var0.getValues());
      Map var3 = buildMap(var1.getValues());
      if (!var2.keySet().equals(var3.keySet())) {
         return false;
      } else {
         Iterator var4 = var2.keySet().iterator();

         JAnnotationValue var6;
         JAnnotationValue var7;
         do {
            if (!var4.hasNext()) {
               return true;
            }

            String var5 = (String)var4.next();
            var6 = (JAnnotationValue)var2.get(var5);
            var7 = (JAnnotationValue)var3.get(var5);
         } while(ObjectUtil.equals(var6.asString(), var7.asString()));

         return false;
      }
   }

   private static Map<String, JAnnotationValue> buildMap(JAnnotationValue[] var0) {
      HashMap var1 = new HashMap(var0.length);
      JAnnotationValue[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JAnnotationValue var5 = var2[var4];
         var1.put(var5.getName(), var5);
      }

      return var1;
   }
}
