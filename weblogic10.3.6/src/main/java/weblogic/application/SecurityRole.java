package weblogic.application;

public class SecurityRole {
   private final String[] principalNames;
   private final boolean isExternallyDefined;

   public SecurityRole(String[] var1) {
      this.principalNames = var1;
      this.isExternallyDefined = false;
   }

   public SecurityRole() {
      this.principalNames = null;
      this.isExternallyDefined = true;
   }

   public String toString() {
      if (this.isExternallyDefined()) {
         return "externally-defined";
      } else {
         StringBuffer var1 = new StringBuffer("principals: ");
         if (this.principalNames != null) {
            for(int var2 = 0; var2 < this.principalNames.length; ++var2) {
               var1.append(this.principalNames[var2]).append(" ");
            }
         }

         return var1.toString();
      }
   }

   public String[] getPrincipalNames() {
      return this.principalNames;
   }

   public boolean isExternallyDefined() {
      return this.isExternallyDefined;
   }
}
