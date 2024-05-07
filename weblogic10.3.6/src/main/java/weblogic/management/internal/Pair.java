package weblogic.management.internal;

import java.io.Serializable;

public final class Pair implements Serializable {
   static final long serialVersionUID = 1L;
   Object key;
   Object value;
   private int hashCode;

   public Pair(Object var1, Object var2) {
      this.key = var1;
      this.value = var2;
   }

   public final String getName() {
      return this.key.toString();
   }

   public final Object getKey() {
      return this.key;
   }

   public final Object getValue() {
      return this.value;
   }

   public int hashCode() {
      return this.hashCode != 0 ? this.hashCode : (this.hashCode = (this.value != null ? this.value.hashCode() : 0) ^ (this.key != null ? this.key.hashCode() : 0));
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Pair)) {
         return false;
      } else {
         Pair var2 = (Pair)var1;
         if (this.value != null && this.key != null) {
            return this.value.equals(var2.value) && this.key.equals(var2.key);
         } else if (this.value == null && this.key != null) {
            return var2.value == null && this.key.equals(var2.key);
         } else if (this.value != null && this.key == null) {
            return var2.key == null && this.value.equals(var2.value);
         } else if (this.value == null && this.key == null) {
            return var2.key == null && var2.value == null;
         } else {
            return false;
         }
      }
   }
}
