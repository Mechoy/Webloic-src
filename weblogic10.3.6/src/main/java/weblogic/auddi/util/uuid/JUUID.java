package weblogic.auddi.util.uuid;

import com.bea.security.utils.random.SecureRandomData;

public class JUUID {
   private StringBuffer uuid = new StringBuffer();

   public JUUID() {
      this.uuid.append(this.genKey(8));
      this.uuid.append("-");
      this.uuid.append(this.genKey(4));
      this.uuid.append("-");
      this.uuid.append(this.genKey(4));
      this.uuid.append("-");
      this.uuid.append(this.genKey(4));
      this.uuid.append("-");
      this.uuid.append(this.genKey(12));
   }

   private String genKey(int var1) {
      StringBuffer var2 = new StringBuffer();
      SecureRandomData var3 = SecureRandomData.getInstance();

      for(int var4 = 0; var4 < var1; ++var4) {
         byte var5 = (byte)(Math.abs(var3.getRandomInt()) % 16);
         char var6 = (char)(var5 >= 10 ? 65 + (var5 - 10) : 48 + var5);
         var2.append(var6);
      }

      return var2.toString();
   }

   public String toString() {
      return this.uuid != null ? this.uuid.toString() : null;
   }

   public static void main(String[] var0) {
      for(int var1 = 0; var1 < Integer.parseInt(var0[0]); ++var1) {
         System.out.println(new JUUID());
      }

   }
}
