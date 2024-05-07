package weblogic.wsee.util;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.sun.xml.ws.model.RuntimeModeler;
import java.lang.reflect.Method;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import weblogic.jws.CallbackService;
import weblogic.wsee.WebServiceType;

public class ClassUtil {
   private ClassUtil() {
   }

   public static Class loadClass(String var0) throws ClassNotFoundException {
      Class var1 = null;
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var2 == null) {
         var2 = ClassUtil.class.getClassLoader();
      }

      if (var2 == null) {
         var1 = Class.forName(var0);
      } else {
         var1 = var2.loadClass(var0);
      }

      return var1;
   }

   public static String getTargetNamespace(JClass var0) {
      String var1 = null;
      JAnnotation var2 = getWebServiceAnnotation(var0);
      if (var2 != null) {
         var1 = JamUtil.getAnnotationStringValue(var2, "targetNamespace");
         if (var1 == null) {
            String var3 = JamUtil.getAnnotationStringValue(var2, "endpointInterface");
            if (var3 != null) {
               JClass var4 = var0.forName(var3);
               if (var4 != null) {
                  var2 = getWebServiceAnnotation(var4);
                  if (var2 != null) {
                     var1 = JamUtil.getAnnotationStringValue(var2, "targetNamespace");
                  }
               }
            }
         }
      }

      return var1;
   }

   public static String getServiceName(JClass var0, String var1) {
      String var2 = getServiceNameFromAnnotation(var0);
      if (StringUtil.isEmpty(var2)) {
         var2 = getDefaultName(var0, var1) + "Service";
      }

      return normalizeClassName(var2);
   }

   private static String getServiceNameFromAnnotation(JClass var0) {
      JAnnotation var1 = getWebServiceAnnotation(var0);
      return var1 != null ? JamUtil.getAnnotationStringValue(var1, "serviceName") : null;
   }

   public static String getDefaultName(JClass var0, String var1) {
      String var2 = var0.getSimpleName();
      if (var2.indexOf("$") != -1) {
         var2 = var2.substring(var2.indexOf("$") + 1);
      }

      StringBuilder var3 = new StringBuilder();
      if (var1 != null) {
         var3.append(var1);
         var3.append("_");
      }

      var3.append(var2);
      return normalizeClassName(var3.toString());
   }

   public static JAnnotation getWebServiceAnnotation(JClass var0) {
      JAnnotation var1 = var0.getAnnotation(WebService.class);
      if (var1 == null) {
         var1 = var0.getAnnotation(WebServiceProvider.class);
      }

      if (var1 == null) {
         var1 = var0.getAnnotation(CallbackService.class);
      }

      return var1;
   }

   public static boolean isWebService(JClass var0) {
      if (var0.isInterface()) {
         return false;
      } else {
         return var0.getAnnotation(WebService.class) != null || var0.getAnnotation(WebServiceProvider.class) != null;
      }
   }

   public static boolean isWebService(Class var0) {
      if (var0.isInterface()) {
         return false;
      } else {
         return var0.getAnnotation(WebService.class) != null || var0.getAnnotation(WebServiceProvider.class) != null;
      }
   }

   public static String getNamespaceFromClass(WebServiceType var0, JClass var1) {
      String var2 = var1.getQualifiedName();
      int var3 = var2.lastIndexOf(".");
      if (var3 != -1) {
         var2 = var2.substring(0, var3);
      }

      if (var0 == WebServiceType.JAXWS) {
         return RuntimeModeler.getNamespace(var2);
      } else {
         var2 = var2.replace('.', '/');
         return "http://" + var2;
      }
   }

   public static String normalizeClassName(String var0) {
      return var0.replace('$', '_');
   }

   public static final boolean equalMethods(Method var0, Method var1) {
      if (var0 != null && var1 != null) {
         if (var0.getName() == var1.getName()) {
            if (!var0.getReturnType().equals(var1.getReturnType())) {
               return false;
            }

            Class[] var2 = var0.getParameterTypes();
            Class[] var3 = var1.getParameterTypes();
            if (var2.length == var3.length) {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  if (var2[var4] != var3[var4]) {
                     return false;
                  }
               }

               return true;
            }
         }

         return false;
      } else {
         return var0 == var1;
      }
   }
}
