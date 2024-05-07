package weblogic.servlet.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import javax.el.BeanELResolver;
import weblogic.servlet.HTTPLogger;

public class BeanELResolverCachePurger {
   private static Map cache = null;

   private BeanELResolverCachePurger() {
   }

   public static void purgeCache(ClassLoader var0) {
      if (var0 != null && cache != null && !cache.isEmpty()) {
         Iterator var1 = cache.keySet().iterator();

         while(var1.hasNext()) {
            Class var2 = (Class)var1.next();
            if (isSameOrChild(var0, var2.getClassLoader())) {
               var1.remove();
            }
         }

      }
   }

   private static boolean isSameOrChild(ClassLoader var0, ClassLoader var1) {
      for(ClassLoader var2 = var0; var2 != ClassLoader.getSystemClassLoader(); var2 = var2.getParent()) {
         if (var2 == var1) {
            return true;
         }
      }

      return false;
   }

   static {
      try {
         Field var0 = BeanELResolver.class.getDeclaredField("properties");
         var0.setAccessible(true);
         cache = (Map)var0.get((Object)null);
      } catch (NoSuchFieldException var1) {
         HTTPLogger.logBeanELResolverPurgerException(var1);
      } catch (IllegalAccessException var2) {
         HTTPLogger.logBeanELResolverPurgerException(var2);
      } catch (Exception var3) {
         HTTPLogger.logBeanELResolverPurgerException(var3);
      }

   }
}
