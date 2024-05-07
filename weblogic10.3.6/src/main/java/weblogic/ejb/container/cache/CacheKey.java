package weblogic.ejb.container.cache;

import weblogic.ejb.container.interfaces.CachingManager;

public final class CacheKey {
   private final Object pk;
   private final CachingManager callback;
   private final int hashCode;

   public CacheKey(Object var1, CachingManager var2) {
      assert var1 != null;

      assert var2 != null;

      this.pk = var1;
      this.callback = var2;
      this.hashCode = var1.hashCode() ^ var2.hashCode();

      assert this.equals(this);

   }

   private static boolean eq(Object var0, Object var1) {
      return var0 == var1 || var0.equals(var1);
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CacheKey)) {
         return false;
      } else {
         CacheKey var2 = (CacheKey)var1;
         return this.hashCode == var2.hashCode && eq(this.pk, var2.pk) && eq(this.callback, var2.callback);
      }
   }

   public Object getPrimaryKey() {
      return this.pk;
   }

   public CachingManager getCallback() {
      return this.callback;
   }

   public String toString() {
      return "(" + this.pk + ", " + this.callback + ")";
   }
}
