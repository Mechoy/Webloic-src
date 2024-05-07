package weblogic.common.internal;

import java.util.EmptyStackException;
import java.util.Stack;
import weblogic.utils.collections.ConcurrentHashMap;

public final class ObjectFactory {
   private static final boolean VERBOSE = false;
   private static final ConcurrentHashMap objBags = new ConcurrentHashMap();

   private static final Stack bag(String var0) {
      Stack var1 = (Stack)objBags.get(var0);
      if (var1 == null) {
         var1 = new Stack();
         objBags.put(var0, var1);
      }

      return var1;
   }

   public static Manufacturable get(String var0) {
      Stack var2 = bag(var0);

      Manufacturable var1;
      try {
         var1 = (Manufacturable)var2.pop();
      } catch (EmptyStackException var10) {
         try {
            var1 = (Manufacturable)Class.forName(var0).newInstance();
         } catch (ClassCastException var5) {
            throw new Error("Class " + var0 + " must be implement Manufacturable " + "(or some subInterface). [" + var5 + "]");
         } catch (ClassNotFoundException var6) {
            throw new Error("Class " + var0 + " was not found. [" + var6 + "]");
         } catch (IllegalAccessException var7) {
            throw new Error("Class " + var0 + " must be declared \"public\" and have " + "a \"public\" constructor. [" + var7 + "]");
         } catch (InstantiationException var8) {
            throw new Error("Class " + var0 + " could not be instantiated. " + "Is it abstract? [" + var8 + "]");
         } catch (NoSuchMethodError var9) {
            throw new Error("Class " + var0 + " must have a default constructor. [" + var9 + "].");
         }
      }

      var1.initialize();
      return var1;
   }

   public static void put(Manufacturable var0) {
      String var1 = var0.getClass().getName();
      var0.destroy();
      Stack var2 = bag(var1);
      var2.push(var0);
   }
}
