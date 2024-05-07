package weblogic.wsee.addressing.policy.api;

import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public final class UsingAddressingVersionInfo {
   private WSAVersion version;
   private boolean isRequired;

   public UsingAddressingVersionInfo(WSAVersion var1, boolean var2) {
      this.version = var1;
      this.isRequired = var2;
   }

   public WSAVersion getWSAVersion() {
      return this.version;
   }

   public boolean isRequired() {
      return this.isRequired;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.isRequired ? 1231 : 1237);
      var2 = 31 * var2 + (this.version == null ? 0 : this.version.hashCode());
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         UsingAddressingVersionInfo var2 = (UsingAddressingVersionInfo)var1;
         if (this.isRequired != var2.isRequired) {
            return false;
         } else {
            if (this.version == null) {
               if (var2.version != null) {
                  return false;
               }
            } else if (!this.version.equals(var2.version)) {
               return false;
            }

            return true;
         }
      }
   }
}
