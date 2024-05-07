package weblogic.wsee.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SmartNameStore {
   private Class clazz;
   private static HashMap keywords = new HashMap();

   public SmartNameStore(Class var1) {
      this.clazz = var1;
   }

   public SmartNameStore() {
      this((Class)null);
   }

   public static String getMangleName(Class var0) {
      Class var1 = getComponentType(var0);
      String var2 = getShortName(var1);
      if (var0.isArray()) {
         var2 = var2 + "s";
      }

      var2 = renameKeword(var2);
      return var2;
   }

   public Iterator getNames(Method var1) {
      Class[] var2 = var1.getParameterTypes();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = getMangleName(var2[var4]);
         var5 = this.uniqueName(var5, var3);
         var3.add(var5);
      }

      return var3.iterator();
   }

   private static Class getComponentType(Class var0) {
      while(var0.isArray()) {
         var0 = var0.getComponentType();
      }

      return var0;
   }

   private String uniqueName(String var1, ArrayList var2) {
      String var3 = var1;

      for(int var4 = 0; var2.contains(var3); ++var4) {
         var3 = var1 + var4;
      }

      return var3;
   }

   private static String renameKeword(String var0) {
      String var1 = (String)keywords.get(var0);
      return var1 == null ? var0 : var1;
   }

   private static String getShortName(Class var0) {
      String var1 = var0.getName();
      int var2 = var1.lastIndexOf(".");
      if (var2 != -1) {
         var1 = var1.substring(var2 + 1, var1.length());
      }

      if (Character.isUpperCase(var1.charAt(0))) {
         var1 = Character.toLowerCase(var1.charAt(0)) + var1.substring(1, var1.length());
      }

      return var1;
   }

   static {
      keywords.put("int", "intVal");
      keywords.put("class", "clazz");
      keywords.put("float", "floatVal");
      keywords.put("double", "doubleVal");
      keywords.put("short", "shortVal");
      keywords.put("boolean", "booleanVal");
      keywords.put("long", "longVal");
      keywords.put("char", "charVal");
      keywords.put("byte", "byteVal");
   }
}
