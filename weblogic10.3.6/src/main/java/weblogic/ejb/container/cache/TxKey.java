package weblogic.ejb.container.cache;

import javax.transaction.Transaction;

public final class TxKey {
   private static final boolean debug = false;
   private final Transaction tx;
   private final CacheKey key;
   private final int hashCode;

   public TxKey(Transaction var1, CacheKey var2) {
      this.tx = var1;
      this.key = var2;
      this.hashCode = var1.hashCode() ^ var2.hashCode();
   }

   public int hashCode() {
      return this.hashCode;
   }

   private static boolean eq(Object var0, Object var1) {
      return var0 == var1 || var0.equals(var1);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TxKey)) {
         return false;
      } else {
         TxKey var2 = (TxKey)var1;
         return this.hashCode == var2.hashCode && eq(this.tx, var2.tx) && eq(this.key, var2.key);
      }
   }

   public Transaction getTx() {
      return this.tx;
   }

   public CacheKey getKey() {
      return this.key;
   }

   public String toString() {
      return "(tx=" + this.tx + ", key=" + this.key + ")";
   }
}
