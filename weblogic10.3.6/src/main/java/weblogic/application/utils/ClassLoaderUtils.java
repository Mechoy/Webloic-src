package weblogic.application.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.descriptor.wl.PreferApplicationPackagesBean;
import weblogic.j2ee.descriptor.wl.PreferApplicationResourcesBean;
import weblogic.utils.classloaders.FilteringClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;

public class ClassLoaderUtils {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugFilteringClassLoader");

   public static void initFilterPatterns(PreferApplicationPackagesBean var0, PreferApplicationResourcesBean var1, GenericClassLoader var2) {
      String[] var3 = null;
      if (var0 != null || var1 != null) {
         if (var0 != null) {
            var3 = var0.getPackageNames();
         }

         List var4 = validatePackages(var3);
         List var5;
         if (var1 != null) {
            var5 = Arrays.asList(var1.getResourceNames());
         } else {
            var5 = Collections.emptyList();
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Filter list contains : " + var4.size() + " elements ");
            Iterator var6 = var4.iterator();

            String var7;
            while(var6.hasNext()) {
               var7 = (String)var6.next();
               logger.debug("Class Pattern: " + var7);
            }

            var6 = var5.iterator();

            while(var6.hasNext()) {
               var7 = (String)var6.next();
               logger.debug("Resource Pattern: " + var7);
            }
         }

         if (!var4.isEmpty() || !var5.isEmpty()) {
            ClassLoader var8;
            for(var8 = var2.getParent(); var8 != null && var8 instanceof GenericClassLoader && !(var8 instanceof FilteringClassLoader); var8 = var8.getParent()) {
            }

            if (!(var8 instanceof FilteringClassLoader)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("No FilteringClassLoader in hierarchy, patterns " + var4 + " not being set.");
               }

            } else {
               FilteringClassLoader var9 = (FilteringClassLoader)var8;
               if (!var4.isEmpty()) {
                  var9.setFilterList(var4);
               }

               if (!var5.isEmpty()) {
                  var9.setResourceFilterList(var5);
               }

            }
         }
      }
   }

   private static List<String> validatePackages(String[] var0) {
      if (var0 != null && var0.length != 0) {
         List var1 = Arrays.asList(var0);
         ArrayList var2 = new ArrayList(var1.size());

         String var4;
         for(Iterator var3 = var1.iterator(); var3.hasNext(); var2.add(var4)) {
            var4 = (String)var3.next();
            if (var4.endsWith("*")) {
               var4 = var4.substring(0, var4.length() - 1);
            }

            if (!var4.endsWith(".")) {
               var4 = var4 + ".";
            }
         }

         return var2;
      } else {
         return Collections.emptyList();
      }
   }
}
