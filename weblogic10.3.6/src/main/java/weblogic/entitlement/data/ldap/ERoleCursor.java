package weblogic.entitlement.data.ldap;

import java.util.Properties;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPSearchResults;
import weblogic.entitlement.data.ERole;
import weblogic.entitlement.data.EnCursorRoleFilter;
import weblogic.entitlement.data.EnRoleCursor;
import weblogic.entitlement.expression.EAuxiliary;
import weblogic.security.shared.LoggerWrapper;

class ERoleCursor extends ECursorBase implements EnRoleCursor {
   private EnCursorRoleFilter filter = null;

   public ERoleCursor(LDAPConnection var1, LDAPSearchResults var2, int var3, EData var4, LoggerWrapper var5) {
      super("RolCur", var1, var2, var3, var4, var5);
   }

   public ERoleCursor(EnCursorRoleFilter var1, LDAPConnection var2, LDAPSearchResults var3, int var4, EData var5, LoggerWrapper var6) {
      super("RolCurFil", var2, var3, var4, var5, var6);
      this.filter = var1;
      this.getCurrentRole();
   }

   public Properties getCurrentProperties() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("getCurrentProperties ERole");
      }

      ERole var1 = this.data.getRoleFromEntry(this.getCurrentEntry());
      if (var1 == null) {
         return null;
      } else {
         Properties var2 = new Properties();
         var2.setProperty("RoleName", var1.getName());
         var2.setProperty("Expression", this.getEntitlement(var1));
         String var3 = var1.getResourceName();
         if ("" != var3) {
            var2.setProperty("ResourceId", var3);
         }

         if (var1.isDeployData()) {
            var2.setProperty("SourceData", "Deployment");
         }

         EAuxiliary var4 = var1.getAuxiliary();
         if (var4 != null) {
            var2.setProperty("AuxiliaryData", var4.toString());
         }

         String var5 = var1.getCollectionName();
         if (var5 != null) {
            var2.setProperty("CollectionName", var5);
         }

         return var2;
      }
   }

   public ERole getCurrentRole() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("getCurrentRole");
      }

      ERole var1 = this.data.getRoleFromEntry(this.getCurrentEntry());
      if (this.filter != null && var1 != null) {
         if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
            this.traceLogger.debug("getCurrentRole filter");
         }

         for(boolean var2 = this.filter.isValidRole(var1); !var2; var2 = this.filter.isValidRole(var1)) {
            this.advance(false);
            var1 = this.data.getRoleFromEntry(this.getCurrentEntry());
            if (var1 == null) {
               break;
            }
         }
      }

      return var1;
   }

   public ERole next() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("next ERole");
      }

      ERole var1 = this.getCurrentRole();
      this.advance();
      return var1;
   }

   public void advance() {
      super.advance();
      if (this.filter != null) {
         this.getCurrentRole();
      }

   }
}
