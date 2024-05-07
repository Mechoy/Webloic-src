package weblogic.wsee.jws.container;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Map;
import weblogic.jws.Callback;
import weblogic.jws.Context;
import weblogic.jws.ServiceClient;
import weblogic.utils.collections.WeakConcurrentHashMap;

class FieldHelper {
   private static Map<ClassLoader, WeakReference<Class>> contextClassCache = new WeakConcurrentHashMap();

   private FieldHelper() {
   }

   static boolean isStub(Field var0) {
      return var0.isAnnotationPresent(ServiceClient.class);
   }

   static boolean isContext(Field var0) {
      if (var0.isAnnotationPresent(Context.class)) {
         return true;
      } else {
         Class var1 = null;
         ClassLoader var2 = Thread.currentThread().getContextClassLoader();

         try {
            WeakReference var3 = (WeakReference)contextClassCache.get(var2);
            if (var3 != null) {
               var1 = (Class)var3.get();
               if (var1 != null) {
                  return var0.isAnnotationPresent(var1);
               }
            }

            var1 = var2.loadClass("weblogic.controls.jws.Common$Context");
            contextClassCache.put(var2, new WeakReference(var1));
            return var0.isAnnotationPresent(var1);
         } catch (ClassNotFoundException var4) {
            contextClassCache.put(var2, new WeakReference(Void.class));
            return false;
         }
      }
   }

   static boolean isCallback(Field var0) {
      return var0.isAnnotationPresent(Callback.class);
   }

   static void setFieldValue(Field var0, Object var1, Object var2) {
      try {
         if (!var0.isAccessible()) {
            var0.setAccessible(true);
         }

         var0.set(var1, var2);
      } catch (Exception var4) {
         throw new InvokeException("Unable to assign Field Value", var4);
      }
   }

   static Object getFieldValue(Field var0, Object var1) {
      Object var2 = null;

      try {
         if (!var0.isAccessible()) {
            var0.setAccessible(true);
         }

         var2 = var0.get(var1);
         return var2;
      } catch (IllegalAccessException var4) {
         throw new InvokeException("Unable to get Field Value", var4);
      }
   }
}
