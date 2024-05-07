package weblogic.wsee.policy.runtime.schema;

public class BuiltinPolicyHelper {
   private static String[] idTable = null;

   private static void init() {
      idTable = new String[getCategorySize()];

      for(int var0 = 0; var0 < idTable.length; ++var0) {
         idTable[var0] = getCategoryString(var0 + 1);
      }

   }

   public static int getCategoryId(String var0) {
      if (null == idTable) {
         init();
      }

      for(int var1 = 0; var1 < idTable.length; ++var1) {
         if (idTable[var1].equals(var0)) {
            return var1 + 1;
         }
      }

      throw new ArrayIndexOutOfBoundsException("Error on category = " + var0);
   }

   public static int getCategoryId(CategoryEnum.Enum var0) {
      return getCategoryId(String.valueOf(var0));
   }

   public static String getCategoryString(int var0) {
      return String.valueOf(CategoryEnum.Enum.forInt(var0));
   }

   public static int[] getIntegerArray(CategoryEnum.Enum[] var0) {
      if (null != var0 && var0.length != 0) {
         int[] var1 = new int[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = getCategoryId(var0[var2]);
         }

         return var1;
      } else {
         return null;
      }
   }

   public static int getCategorySize() {
      return null == idTable ? 16 : idTable.length;
   }
}
