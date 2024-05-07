package weblogic.wsee.util;

public class Pair<L, R> {
   L left;
   R right;

   public Pair() {
   }

   public Pair(L var1, R var2) {
      this.left = var1;
      this.right = var2;
   }

   public L getLeft() {
      return this.left;
   }

   public void setLeft(L var1) {
      this.left = var1;
   }

   public R getRight() {
      return this.right;
   }

   public void setRight(R var1) {
      this.right = var1;
   }

   public int hashCode() {
      int var1 = 23;
      var1 = HashCodeUtil.hash(var1, this.left);
      var1 = HashCodeUtil.hash(var1, this.right);
      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Pair)) {
         return false;
      } else {
         Pair var2 = (Pair)var1;
         return (this.left == var2.left || this.left != null && this.left.equals(var2.left)) && (this.right == var2.right || this.right != null && this.right.equals(var2.right));
      }
   }
}
