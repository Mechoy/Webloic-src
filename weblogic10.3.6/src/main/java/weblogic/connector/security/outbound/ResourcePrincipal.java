package weblogic.connector.security.outbound;

import java.security.Principal;

public final class ResourcePrincipal implements Principal {
   private String password;
   private String userName;

   public ResourcePrincipal(String var1, String var2) {
      this.userName = var1;
      this.password = var2;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof ResourcePrincipal)) {
         return false;
      } else {
         ResourcePrincipal var2 = (ResourcePrincipal)var1;
         return this.userName.equals(var2.getName()) && this.password.equals(var2.getPassword());
      }
   }

   public int hashCode() {
      return this.userName.hashCode() ^ this.password.hashCode();
   }

   public String toString() {
      return "ResourcePrincipal(userName=" + this.userName + ",password=" + this.password + ")";
   }

   public String getName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }
}
