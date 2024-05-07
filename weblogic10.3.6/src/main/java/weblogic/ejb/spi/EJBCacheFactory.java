package weblogic.ejb.spi;

import java.lang.reflect.Constructor;
import weblogic.ejb.container.cache.EntityCache;
import weblogic.ejb.container.cache.NRUCache;
import weblogic.utils.AssertionError;

public class EJBCacheFactory {
   public static EJBCache createNRUCache(String var0, int var1) {
      return new NRUCache(var0, var1);
   }

   public static EJBCache createNRUCache(String var0, long var1) {
      return new NRUCache(var0, var1);
   }

   public static EJBCache createEntityCache(String var0, int var1) {
      return new EntityCache(var0, var1);
   }

   public static EJBCache createEntityCache(String var0, long var1) {
      return new EntityCache(var0, var1);
   }

   public static QueryCache createQueryCache(String var0, int var1) {
      try {
         Class var2 = Class.forName("weblogic.ejb.container.cache.QueryCache");
         Constructor var3 = var2.getConstructor(String.class, Integer.TYPE);
         return (QueryCache)var3.newInstance(var0, new Integer(var1));
      } catch (Exception var4) {
         throw new AssertionError("Error creating query cache", var4);
      }
   }
}
