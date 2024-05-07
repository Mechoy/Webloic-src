package weblogic.ejb.container.cache;

import javax.transaction.Transaction;

public final class TxPk {
   private static final boolean debug = false;
   private final Transaction tx;
   private final Object pk;
   private final int hashCode;

   public TxPk(Transaction var1, Object var2) {
      this.tx = var1;
      this.pk = var2;
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
      } else if (!(var1 instanceof TxPk)) {
         return false;
      } else {
         TxPk var2 = (TxPk)var1;
         return this.hashCode == var2.hashCode && eq(this.tx, var2.tx) && eq(this.pk, var2.pk);
      }
   }

   public Transaction getTx() {
      return this.tx;
   }

   public Object getPk() {
      return this.pk;
   }

   public String toString() {
      return "(tx=" + this.tx + ", pk=" + this.pk + ")";
   }
}
