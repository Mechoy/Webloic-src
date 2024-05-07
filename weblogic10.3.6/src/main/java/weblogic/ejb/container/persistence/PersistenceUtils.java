package weblogic.ejb.container.persistence;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.dd.xml.DDUtils;

public final class PersistenceUtils {
   public static final String PERSISTENCE_VERBOSE_PROP = "weblogic.ejb.container.persistence.verbose";
   public static final String PERSISTENCE_DEBUG_PROP = "weblogic.ejb.container.persistence.debug";
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   public static final String PARSER_NAME = "com.sun.xml.parser.ValidatingParser";
   public static final String[] validPersistencePublicIds = new String[]{"-//BEA Systems, Inc.//DTD WebLogic 6.0.0 Persistence Vendor//EN"};
   public static String RDBMS_CMP_RESOURCE_NAME = "WebLogic_CMP_RDBMS.xml";

   public static Map getAccessorMethodMap(Class var0) {
      Class var1 = var0;

      HashMap var2;
      for(var2 = new HashMap(); var1 != null && var1 != Object.class; var1 = var1.getSuperclass()) {
         Method[] var3 = var1.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getName();
            if (var5.startsWith("get") || var5.startsWith("set")) {
               int var6 = var3[var4].getModifiers();
               if (Modifier.isAbstract(var6) && (var1 == var0 || Modifier.isPublic(var6) || Modifier.isProtected(var6))) {
                  var2.put(var3[var4].getName(), var3[var4]);
               }
            }
         }
      }

      return var2;
   }

   public static Collection getAbstractMethodCollection(Class var0) {
      Class var1 = var0;
      ArrayList var2 = new ArrayList();
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();
      HashSet var5 = new HashSet();

      while(var1 != null && var1 != Object.class) {
         var5.addAll(Arrays.asList((Object[])var1.getDeclaredMethods()));
         if (var1 == var0) {
            var5.addAll(Arrays.asList((Object[])var1.getMethods()));
         }

         Iterator var6 = var5.iterator();

         while(true) {
            while(var6.hasNext()) {
               Method var7 = (Method)var6.next();
               int var8 = var7.getModifiers();
               String var9;
               if (Modifier.isAbstract(var8) && (var1 == var0 || Modifier.isPublic(var8) || Modifier.isProtected(var8))) {
                  var9 = DDUtils.getMethodSignature(var7);
                  if (!var4.contains(var9) && !var3.contains(var9)) {
                     var2.add(var7);
                     var3.add(var9);
                  }
               } else {
                  var9 = DDUtils.getMethodSignature(var7);
                  var4.add(var9);
               }
            }

            var1 = var1.getSuperclass();
            var5.clear();
            break;
         }
      }

      return var2;
   }

   public static Method getMethodIncludeSuper(Class var0, String var1, Class[] var2) {
      Class var3 = var0;
      boolean var4 = false;

      Method var5;
      for(var5 = null; var3 != null && !var4; var3 = var3.getSuperclass()) {
         try {
            var5 = var3.getMethod(var1, var2);
            int var6 = var5.getModifiers();
            if (var3 == var0 || Modifier.isPublic(var6) || Modifier.isProtected(var6)) {
               var4 = true;
            }
         } catch (NoSuchMethodException var7) {
         }
      }

      return var4 ? var5 : null;
   }
}
