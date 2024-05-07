package weblogic.deploy.api.spi.config;

import javax.enterprise.deploy.shared.ModuleType;

public class DeployableObjectKey {
   private String name;
   private ModuleType type;
   private String contextRoot;

   public DeployableObjectKey(String var1, ModuleType var2) {
      this.name = var1;
      this.type = var2;
   }

   public DeployableObjectKey(String var1, ModuleType var2, String var3) {
      this.name = var1;
      this.type = var2;
      this.contextRoot = var3;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof DeployableObjectKey) {
         DeployableObjectKey var2 = (DeployableObjectKey)var1;
         if (this.name.equals(var2.getName()) && this.type.getValue() == var2.getType().getValue()) {
            if (this.contextRoot == null) {
               if (var2.getContextRoot() == null) {
                  return true;
               }

               return false;
            }

            if (this.contextRoot.equals(var2.getContextRoot())) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   public final int hashCode() {
      int var1 = this.name.hashCode() ^ this.type.getValue();
      if (this.contextRoot != null) {
         var1 ^= this.contextRoot.hashCode();
      }

      return var1;
   }

   public final String getName() {
      return this.name;
   }

   public final void setName(String var1) {
      this.name = var1;
   }

   public final ModuleType getType() {
      return this.type;
   }

   public final void setType(ModuleType var1) {
      this.type = var1;
   }

   public final String getContextRoot() {
      return this.contextRoot;
   }

   public final void setContextRoot(String var1) {
      this.contextRoot = var1;
   }

   public String toString() {
      StringBuffer var3 = new StringBuffer(1000);
      var3.append("name=").append(this.name);
      var3.append("  ");
      var3.append("type=").append(this.type);
      var3.append("  ");
      if (this.contextRoot != null) {
         var3.append("contextRoot=").append(this.contextRoot);
      }

      return var3.toString();
   }
}
