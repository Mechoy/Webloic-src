package weblogic.entitlement.data;

public class ERoleId {
   public static final String GLOBAL_RESOURCE = "";
   private String mResourceName;
   private String mRoleName;

   public ERoleId(String var1) {
      this("", var1);
   }

   public ERoleId(String var1, String var2) {
      if (var1 == null || var1.length() == 0) {
         var1 = "";
      }

      this.mResourceName = var1;
      if (var2 == null) {
         throw new NullPointerException("null role name");
      } else {
         this.mRoleName = var2;
      }
   }

   public final String getResourceName() {
      return this.mResourceName;
   }

   public final String getRoleName() {
      return this.mRoleName;
   }

   public final boolean isGlobal() {
      return this.mResourceName == "";
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ERoleId)) {
         return false;
      } else {
         ERoleId var2 = (ERoleId)var1;
         return var2.getRoleName().equals(this.mRoleName) && var2.getResourceName().equals(this.mResourceName);
      }
   }

   public int hashCode() {
      int var1 = this.mResourceName == null ? 0 : this.mResourceName.hashCode();
      var1 += this.mRoleName.hashCode();
      return var1;
   }

   public String toString() {
      return this.mResourceName + ":" + this.mRoleName;
   }
}
