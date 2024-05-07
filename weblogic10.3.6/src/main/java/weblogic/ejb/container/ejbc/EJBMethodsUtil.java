package weblogic.ejb.container.ejbc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.utils.annotation.BeaSynthetic.Helper;
import weblogic.utils.collections.EnumerationIterator;
import weblogic.utils.reflect.MethodKey;
import weblogic.utils.reflect.MethodText;
import weblogic.utils.reflect.ReflectUtils;
import weblogic.wsee.deploy.DeployUtil;

public final class EJBMethodsUtil {
   public static final short STANDARD_METHOD = 0;
   public static final short HOME_METHOD = 1;
   private static final Set<MethodKey> EO_METHS = initWith(EJBObject.class);
   private static final Set<MethodKey> EH_METHS = initWith(EJBHome.class);
   private static final Set<MethodKey> ELO_METHS = initWith(EJBLocalObject.class);
   private static final Set<MethodKey> ELH_METHS = initWith(EJBLocalHome.class);

   private static Set<MethodKey> initWith(Class<?> var0) {
      Method[] var1 = var0.getMethods();
      HashSet var2 = new HashSet(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(new MethodKey(var1[var3]));
      }

      return var2;
   }

   private EJBMethodsUtil() {
   }

   public static Map<Method, String> getMethodSigs(Method[] var0) {
      return methodSigsInternal(var0, false);
   }

   public static Map<Method, String> getHomeMethodSigs(Method[] var0) {
      return methodSigsInternal(var0, true);
   }

   private static Map<Method, String> methodSigsInternal(Method[] var0, boolean var1) {
      if (var0 == null) {
         return Collections.emptyMap();
      } else {
         HashMap var2 = new HashMap(var0.length);
         MethodText var3 = new MethodText();
         Method[] var4 = var0;
         int var5 = var0.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method var7 = var4[var6];
            var3.setMethod(var7);
            var3.setOptions(128);
            if (var1) {
               var2.put(var7, homeClassMethodNameMapper(var3.toString()));
            } else {
               var2.put(var7, var3.toString());
            }
         }

         return var2;
      }
   }

   public static String homeClassMethodNameMapper(String var0) {
      if (var0.startsWith("create")) {
         return "ejbC" + var0.substring(1);
      } else {
         return var0.startsWith("find") ? "ejbF" + var0.substring(1) : "ejbHome" + var0;
      }
   }

   public static Method[] getRemoteMethods(Class<?> var0, boolean var1) {
      final Set var2 = EO_METHS;
      return getMethodsInternal(var0, new MethodFilter() {
         public boolean accept(Method var1) {
            return !var2.contains(new MethodKey(var1));
         }
      }, var1);
   }

   public static Method[] getLocalMethods(Class<?> var0, boolean var1) {
      final Set var2 = ELO_METHS;
      return getMethodsInternal(var0, new MethodFilter() {
         public boolean accept(Method var1) {
            return !var2.contains(new MethodKey(var1));
         }
      }, var1);
   }

   public static Method[] getMethods(Class<?> var0, boolean var1) {
      return getMethodsInternal(var0, (MethodFilter)null, var1);
   }

   private static Method[] getMethodsInternal(Class<?> var0, MethodFilter var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      if (var2) {
         Enumeration var4 = ReflectUtils.distinctInterfaceMethods(var0);

         while(true) {
            Method var5;
            do {
               do {
                  if (!var4.hasMoreElements()) {
                     return (Method[])var3.toArray(new Method[var3.size()]);
                  }

                  var5 = (Method)var4.nextElement();
               } while(Helper.isBeaSyntheticMethod(var5));
            } while(var1 != null && !var1.accept(var5));

            var3.add(var5);
         }
      } else {
         Method[] var8 = var0.getMethods();
         int var9 = var8.length;

         for(int var6 = 0; var6 < var9; ++var6) {
            Method var7 = var8[var6];
            if (!Helper.isBeaSyntheticMethod(var7) && (var1 == null || var1.accept(var7))) {
               var3.add(var7);
            }
         }

         return (Method[])var3.toArray(new Method[var3.size()]);
      }
   }

   public static Method[] getBeanHomeClassMethods(Class<?> var0) {
      ArrayList var1 = new ArrayList();
      Method[] var2 = var0.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3].getName();
         if (var4.startsWith("ejbHome") || var4.startsWith("ejbCreate") || var4.startsWith("ejbFind")) {
            var1.add(var2[var3]);
         }
      }

      return (Method[])var1.toArray(new Method[var1.size()]);
   }

   public static Method[] getLocalCreateMethods(Class<?> var0) {
      return getMethodsInternal(var0, ELH_METHS, "create");
   }

   public static Method[] getCreateMethods(Class<?> var0) {
      return getMethodsInternal(var0, EH_METHS, "create");
   }

   public static Method[] getLocalFindMethods(Class<?> var0) {
      return getMethodsInternal(var0, ELH_METHS, "find");
   }

   public static Method[] getFindMethods(Class<?> var0) {
      return getMethodsInternal(var0, EH_METHS, "find");
   }

   private static Method[] getMethodsInternal(Class<?> var0, Set<MethodKey> var1, String var2) {
      ArrayList var3 = new ArrayList();
      Method[] var4 = var0.getMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (!var1.contains(new MethodKey(var7)) && var7.getName().startsWith(var2)) {
            var3.add(var7);
         }
      }

      if (var3.isEmpty()) {
         return null;
      } else {
         return (Method[])var3.toArray(new Method[var3.size()]);
      }
   }

   public static Method[] getWebserviceMethods(SessionBeanInfo var0) {
      Class var1 = var0.getServiceEndpointClass();
      Object var2;
      if (var0.isEJB30()) {
         List var3 = DeployUtil.getWebServiceMethods(var0.getBeanClass(), var1);
         var2 = var3.iterator();
      } else {
         Enumeration var6 = ReflectUtils.distinctInterfaceMethods(var1);
         var2 = new EnumerationIterator(var6);
      }

      ArrayList var7 = new ArrayList();

      while(((Iterator)var2).hasNext()) {
         Method var4 = (Method)((Iterator)var2).next();
         if (!Modifier.isVolatile(var4.getModifiers()) && !ELO_METHS.contains(new MethodKey(var4))) {
            String var5 = var4.getName();
            if (!var5.equals("remove") && !var5.equals("create") && !var5.equals("getEJBHome") && !var5.equals("getPrimaryKey") && !var5.equals("getHandle") && !var5.equals("isIdentical")) {
               var7.add(var4);
            }
         }
      }

      return (Method[])var7.toArray(new Method[var7.size()]);
   }

   public static String methodDescriptorPrefix(short var0) {
      switch (var0) {
         case 0:
         default:
            return "md_eo_";
         case 1:
            return "md_";
      }
   }

   private interface MethodFilter {
      boolean accept(Method var1);
   }
}
