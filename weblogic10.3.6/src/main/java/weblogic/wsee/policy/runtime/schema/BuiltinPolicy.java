package weblogic.wsee.policy.runtime.schema;

public class BuiltinPolicy {
   private int id;
   private String policyName;
   private int[] catIds;
   private static int idCount = 0;

   private void init(String var1) {
      this.id = idCount++;
      this.policyName = var1;
   }

   public BuiltinPolicy(String var1) {
      this.init(var1);
   }

   public BuiltinPolicy(String var1, int var2) {
      this(var1);
      this.addCatId(var2);
   }

   public BuiltinPolicy(String var1, int[] var2) {
      this(var1);
      this.catIds = var2;
   }

   public BuiltinPolicy(BuiltinPolicyType var1) {
      if (null == var1) {
         throw new IllegalArgumentException("null BuiltinPolicyType found");
      } else {
         this.init(var1.getPolicyName());
         this.catIds = BuiltinPolicyHelper.getIntegerArray(var1.getCategoryArray());
      }
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public String getPolicyName() {
      return this.policyName;
   }

   public void setPolicyName(String var1) {
      this.policyName = var1;
   }

   public int[] getCatIds() {
      return this.catIds;
   }

   public void setCatIds(int[] var1) {
      this.catIds = var1;
   }

   public void addCatId(int var1) {
      if (null == this.catIds) {
         this.catIds = new int[1];
         this.catIds[0] = var1;
      } else if (!this.hasCategoryId(var1)) {
         int[] var2 = new int[this.catIds.length + 1];

         for(int var3 = 0; var3 < this.catIds.length; ++var3) {
            var2[var3] = this.catIds[var3];
         }

         var2[this.catIds.length] = var1;
         this.catIds = var2;
      }
   }

   public boolean hasCategoryId(int var1) {
      if (null == this.catIds) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.catIds.length; ++var2) {
            if (this.catIds[var2] == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public int getCategorySize() {
      return null == this.catIds ? 0 : this.catIds.length;
   }
}
