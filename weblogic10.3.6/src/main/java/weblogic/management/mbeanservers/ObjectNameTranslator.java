package weblogic.management.mbeanservers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.WebLogicObjectName;

public class ObjectNameTranslator {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXCore");

   public static Hashtable get81Keys(ObjectName var0) {
      Hashtable var1 = new Hashtable();
      String var2 = calculate81Type(var0.getKeyProperty("Type"));
      var1.put("Type", var2);
      String var3 = var0.getKeyProperty("Name");
      var1.put("Name", var3);
      if (var2.equals("Domain")) {
         var1.put(var2, var3);
      }

      String var4 = var0.getKeyProperty("Path");
      String[] var5 = var4.split("/");
      Class var6 = null;

      for(int var7 = 0; var7 < var5.length - 1; ++var7) {
         String var8 = var5[var7];
         String[] var9 = var8.split("\\[|\\]");
         String var10 = var9[0];
         if (var6 == null) {
            try {
               var1.put(var10, var9[1]);
               if (!var10.equals("DomainRuntime")) {
                  var6 = Class.forName(translateTypeTo90(var10));
               }
            } catch (ClassNotFoundException var13) {
               throw new AssertionError("Unable to get class for type " + var10);
            }
         } else {
            try {
               Method var11 = var6.getMethod("get" + var10, (Class[])null);
               var6 = var11.getReturnType();
               if (var6.isArray()) {
                  var6 = var6.getComponentType();
               }

               String var12 = calculate81Type(var6.getName());
               var1.put(var12, var9[1]);
            } catch (NoSuchMethodException var14) {
               throw new AssertionError("Unable to get method for specified attribute " + var10);
            }
         }
      }

      if (debug.isDebugEnabled()) {
         debug.debug("get81Keys: for " + var0 + " returns " + var1);
      }

      return var1;
   }

   public static ObjectName translateTo81(ObjectName var0) {
      Hashtable var1 = get81Keys(var0);
      String var2 = (String)var1.get("DomainRuntime");
      if (var2 != null) {
         var1.remove("DomainRuntime");
      } else {
         var2 = (String)var1.get("Domain");
         var1.remove("Domain");
      }

      try {
         return new ObjectName(var2, var1);
      } catch (MalformedObjectNameException var4) {
         throw new RuntimeException("Unble to translate ObjectName", var4);
      }
   }

   private static Method matchAttributeNameWithClass(Class var0, String var1) {
      Method var2 = null;

      try {
         var2 = var0.getMethod("get" + var1, (Class[])null);
         return var2;
      } catch (NoSuchMethodException var5) {
         try {
            String var3 = pluralize(var1);
            var2 = var0.getMethod("get" + var3, (Class[])null);
            return var2;
         } catch (NoSuchMethodException var4) {
            return null;
         }
      }
   }

   private static Method matchAttributeNameWithClass(Class var0, Class var1, String var2) {
      Method var3 = matchAttributeNameWithClass(var0, var2);
      if (var3 != null) {
         return var3;
      } else {
         if (var2.endsWith("Runtime")) {
            String var4 = var2.substring(0, var2.length() - 7);
            var3 = matchAttributeNameWithClass(var0, var4);
            if (var3 != null) {
               return var3;
            }
         }

         Method[] var9 = var0.getMethods();

         for(int var5 = 0; var5 < var9.length; ++var5) {
            Method var6 = var9[var5];
            Class var7 = var6.getReturnType();
            if (var6.getName().startsWith("get") && var6.getParameterTypes().length == 0) {
               if (var7.isArray()) {
                  Class var8 = var7.getComponentType();
                  if (var8.isAssignableFrom(var1)) {
                     if (var1.equals(var8)) {
                        if (var3 == null) {
                           var3 = var6;
                        }
                     } else {
                        var3 = var6;
                     }
                  }
               } else if (var7.isAssignableFrom(var1)) {
                  if (var1.equals(var7)) {
                     if (var3 == null) {
                        var3 = var6;
                     }
                  } else {
                     var3 = var6;
                  }
               }
            }
         }

         if (var3 == null) {
            throw new RuntimeException("Unable to find getter method for " + var0.getName() + " returning " + var1.getName());
         } else {
            return var3;
         }
      }
   }

   public static ObjectName translateTo90(WebLogicObjectName var0) {
      Hashtable var1 = new Hashtable();
      String var2 = var0.getType();
      String var3 = translateTypeTo90(var2);
      var1.put("Type", var3);
      String var4 = var0.getName();
      var1.put("Name", var4);
      String var5 = var0.getDomain();
      WebLogicObjectName var6 = var0.getParent();
      ArrayList var7 = new ArrayList();
      String var8 = var2;
      String var9 = translateTypeTo90(var2);
      Class var10 = null;

      try {
         var10 = Class.forName(var9);
      } catch (ClassNotFoundException var20) {
         throw new RuntimeException("Unable to map tag to class " + var2, var20);
      }

      String var11;
      for(var11 = var4; var6 != null; var6 = var6.getParent()) {
         String var12 = var6.getType();
         String var13 = translateTypeTo90(var12);
         Class var14 = null;

         try {
            var14 = Class.forName(var13);
         } catch (ClassNotFoundException var19) {
            throw new RuntimeException("Unable to map tag to class " + var12, var19);
         }

         Method var15 = matchAttributeNameWithClass(var14, var10, var8);
         String var16 = var15.getName().substring(3);
         String var17 = var16 + "[" + var11 + "]";
         var7.add(0, var17);
         var11 = var6.getName();
         var10 = var14;
         var8 = var12;
      }

      StringBuffer var21 = new StringBuffer();
      if (!var2.endsWith("Runtime")) {
         var21.append("Domain[");
         var21.append(var5);
         var21.append("]");
      } else {
         var21.append("DomainRuntime[");
         var21.append(var5);
         var21.append("]/");
         var21.append("ServerRuntime[");
         var21.append(var11);
         var21.append("]");
      }

      if (var7.size() != 0) {
         var21.append("/");
      }

      Iterator var22 = var7.iterator();

      while(var22.hasNext()) {
         String var23 = (String)var22.next();
         var21.append(var23);
         if (var22.hasNext()) {
            var21.append("/");
         }
      }

      var1.put("Path", var21.toString());
      if (debug.isDebugEnabled()) {
         debug.debug("translateTo90: for " + var0 + " has keys " + var1);
      }

      try {
         return new ObjectName("com.bea", var1);
      } catch (MalformedObjectNameException var18) {
         System.out.println(var1);
         throw new RuntimeException(var18);
      }
   }

   public static String translateTypeTo90(String var0) {
      if (var0.endsWith("Runtime")) {
         return "weblogic.management.runtime." + var0 + "MBean";
      } else {
         return var0.endsWith("Config") ? "weblogic.management.configuration." + var0.substring(0, var0.length() - 7) + "MBean" : "weblogic.management.configuration." + var0 + "MBean";
      }
   }

   public static String calculate81Type(String var0) {
      int var1 = var0.lastIndexOf(46) + 1;
      int var2 = var0.length();
      if (var0.endsWith("MBean")) {
         var2 -= 5;
      } else if (var0.endsWith("Bean")) {
         var2 -= 4;
      }

      return var0.substring(var1, var2);
   }

   public static String pluralize(String var0) {
      String var1 = null;
      if (!var0.endsWith("s") && !var0.endsWith("ch") && !var0.endsWith("x") && !var0.endsWith("sh")) {
         if (var0.endsWith("y") && !var0.endsWith("ay") && !var0.endsWith("ey") && !var0.endsWith("iy") && !var0.endsWith("oy") && !var0.endsWith("uy")) {
            var1 = var0.substring(0, var0.length() - 1) + "ies";
         } else {
            var1 = var0 + "s";
         }
      } else {
         var1 = var0 + "es";
      }

      return var1;
   }
}
