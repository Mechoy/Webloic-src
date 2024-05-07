package weblogic.ejb.container.internal;

import java.util.HashMap;
import java.util.Map;
import weblogic.kernel.ResettableThreadLocal;
import weblogic.kernel.ThreadLocalInitialValue;

public final class ExtendedPersistenceContextManager {
   private static ResettableThreadLocal threadLocal = new ResettableThreadLocal(new ThreadLocalInitialValue() {
      protected Object initialValue() {
         return new HashMap();
      }
   });

   public static ExtendedPersistenceContextWrapper getExtendedPersistenceContext(String var0) {
      return (ExtendedPersistenceContextWrapper)((Map)threadLocal.get()).get(var0);
   }

   public static void setExtendedPersistenceContext(String var0, ExtendedPersistenceContextWrapper var1) {
      ((Map)threadLocal.get()).put(var0, var1);
   }

   public static void removeExtendedPersistenceContext(String var0) {
      ((Map)threadLocal.get()).remove(var0);
   }
}
