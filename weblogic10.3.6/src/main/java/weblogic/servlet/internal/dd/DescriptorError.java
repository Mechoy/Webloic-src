package weblogic.servlet.internal.dd;

public final class DescriptorError {
   String err = null;
   String msg = null;
   String type = null;

   public DescriptorError(String var1) {
      this.err = var1;
   }

   public DescriptorError(String var1, String var2) {
      this.err = var1;
      this.msg = var2;
   }

   public DescriptorError(String var1, String var2, String var3) {
      this.err = var1;
      this.msg = var2;
      this.type = var3;
   }

   public String getError() {
      return this.err;
   }

   public String toString() {
      String var1 = "DescriptorError(";
      if (this.err != null) {
         var1 = var1 + this.err;
      }

      if (this.msg != null) {
         var1 = var1 + "," + this.msg;
      }

      if (this.type != null) {
         var1 = var1 + "," + this.type;
      }

      var1 = var1 + ")";
      return var1;
   }
}
