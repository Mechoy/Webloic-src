package weblogic.servlet.utils;

import weblogic.servlet.HTTPLogger;

public class URLMappingFactory {
   public static ServletMapping createDefaultURLMapping() {
      return new ServletMapping();
   }

   public static ServletMapping createDefaultURLMapping(boolean var0, boolean var1) {
      return new ServletMapping(var0, var1);
   }

   public static URLMapping createCustomURLMapping(String var0, ClassLoader var1, boolean var2) {
      try {
         Class var3 = var1.loadClass(var0);
         URLMapping var4 = (URLMapping)var3.newInstance();
         var4.setCaseInsensitive(var2);
         var4.setExtensionCaseInsensitive(var2);
         return var4;
      } catch (ClassNotFoundException var5) {
         HTTPLogger.logCouldNotLoadUrlMatchMapClass(var0, var5);
      } catch (InstantiationException var6) {
         HTTPLogger.logCouldNotLoadUrlMatchMapClass(var0, var6);
      } catch (IllegalAccessException var7) {
         HTTPLogger.logCouldNotLoadUrlMatchMapClass(var0, var7);
      } catch (ClassCastException var8) {
         HTTPLogger.logCouldNotLoadUrlMatchMapClass(var0, var8);
      }

      return null;
   }

   public static StandardURLMapping createCompatibleURLMapping(String var0, ClassLoader var1, boolean var2, boolean var3) {
      return (StandardURLMapping)(var0 != null && var0.equals(OC4JURLMapping.class.getName()) ? (StandardURLMapping)createCustomURLMapping(var0, var1, var2) : createDefaultURLMapping(var2, var3));
   }

   public static boolean isInvalidUrlPattern(String var0, String var1) {
      if (var1 == null) {
         return true;
      } else {
         return (var0 == null || !var0.equals(OC4JURLMapping.class.getName())) && var1.length() > 1 && var1.endsWith("*") && !var1.endsWith("/*");
      }
   }
}
