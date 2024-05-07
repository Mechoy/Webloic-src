package weblogic.cache.webapp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletContext;
import weblogic.cache.CacheException;
import weblogic.cache.CacheValue;
import weblogic.cache.KeyEnumerator;

public class ServletCacheUtils {
   public static final String CACHING = "weblogic.cache.tag.CacheTag.caching";

   public static void removeCacheListener(ServletContext var0, CacheListener var1) {
      List var2 = (List)var0.getAttribute("weblogic.cache.CacheListener");
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   public static void addCacheListener(ServletContext var0, CacheListener var1) {
      Object var2 = (List)var0.getAttribute("weblogic.cache.CacheListener");
      if (var2 == null) {
         var2 = new ArrayList();
         var0.setAttribute("weblogic.cache.CacheListener", var2);
      }

      if (!((List)var2).contains(var1)) {
         ((List)var2).add(var1);
      }

   }

   public static int getTimeout(String var0) throws CacheException {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = var0.length();

         int var2;
         for(var2 = 0; var2 < var1 && Character.isDigit(var0.charAt(var2)); ++var2) {
         }

         if (var2 == 0) {
            return -1;
         } else {
            int var3 = Integer.parseInt(var0.substring(0, var2));
            String var4 = var0.substring(var2);
            if (!var4.equals("s") && !var4.equals("")) {
               if (var4.equals("ms")) {
                  return var3;
               } else if (var4.equals("h")) {
                  return var3 * 1000 * 3600;
               } else if (var4.equals("m")) {
                  return var3 * 1000 * 60;
               } else if (var4.equals("d")) {
                  return var3 * 1000 * 3600 * 24;
               } else {
                  throw new CacheException("Invalid time unit: " + var4 + " in " + var0);
               }
            } else {
               return 1000 * var3;
            }
         }
      }
   }

   public static void saveVars(CacheSystem var0, CacheValue var1, String var2, String var3) throws CacheException {
      if (var2 != null) {
         Hashtable var4 = new Hashtable();
         KeyEnumerator var5 = new KeyEnumerator(var2);

         while(var5.hasMoreKeys()) {
            String var6 = var5.getNextKey();
            String var7 = var5.getKeyScope();
            Object var8 = var0.getValueFromScope(var7, var6);
            if (var8 != null) {
               var0.setValueInScope(var3, var6, var8);
               var4.put(var6, var8);
            } else {
               var0.removeValueInScope(var3, var6);
            }
         }

         var1.setVariables(var4);
      }
   }

   public static void restoreVars(CacheSystem var0, CacheValue var1, String var2, String var3) throws CacheException {
      if (var2 != null) {
         Hashtable var4 = var1.getVariables();
         if (var4 == null) {
            throw new CacheException("Variable not present, probably an inconsistent cache state");
         } else {
            KeyEnumerator var5 = new KeyEnumerator(var2);

            while(var5.hasMoreKeys()) {
               String var6 = var5.getNextKey();
               String var7 = var5.getKeyScope();
               if (var7.equals("any")) {
                  var7 = var3;
               }

               Object var8 = var4.get(var6);
               if (var8 == null) {
                  var0.removeValueInScope(var7, var6);
                  var0.removeValueInScope(var3, var6);
               } else {
                  var0.setValueInScope(var7, var6, var8);
                  var0.setValueInScope(var3, var6, var8);
               }
            }

         }
      }
   }
}
