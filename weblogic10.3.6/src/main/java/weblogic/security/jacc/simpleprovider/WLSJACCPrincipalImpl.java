package weblogic.security.jacc.simpleprovider;

import com.sun.security.auth.PrincipalComparator;
import java.security.Principal;
import javax.security.auth.Subject;
import weblogic.security.principal.RealmAdapterUser;
import weblogic.security.principal.WLSAbstractPrincipal;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

public class WLSJACCPrincipalImpl implements Principal, PrincipalComparator {
   private String name = null;

   public WLSJACCPrincipalImpl(String var1) {
      this.name = var1;
   }

   public String getName() {
      return new String(this.name);
   }

   public String toString() {
      return this.getName();
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (!(var1 instanceof WLSAbstractPrincipal)) {
         return false;
      } else {
         WLSAbstractPrincipal var2 = (WLSAbstractPrincipal)var1;
         if (!(var2 instanceof WLSUserImpl) && !(var2 instanceof WLSGroupImpl) && !(var2 instanceof RealmAdapterUser)) {
            return false;
         } else if (this.name != null && var2.getName() != null) {
            return this.name.equals(var2.getName());
         } else {
            return this.name == var2.getName();
         }
      }
   }

   public boolean implies(Subject var1) {
      if (var1 == null) {
         return false;
      } else {
         Principal[] var2 = new Principal[var1.getPrincipals().size()];
         var1.getPrincipals().toArray(var2);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof WLSAbstractPrincipal && this.equals((WLSAbstractPrincipal)var2[var3])) {
               return true;
            }
         }

         return false;
      }
   }
}
