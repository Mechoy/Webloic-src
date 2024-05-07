package weblogic.entitlement.data.ldap;

import java.util.Properties;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPSearchResults;
import weblogic.entitlement.data.EResource;
import weblogic.entitlement.data.EnCursorResourceFilter;
import weblogic.entitlement.data.EnResourceCursor;
import weblogic.security.shared.LoggerWrapper;

class EResourceCursor extends ECursorBase implements EnResourceCursor {
   private EnCursorResourceFilter filter = null;

   public EResourceCursor(LDAPConnection var1, LDAPSearchResults var2, int var3, EData var4, LoggerWrapper var5) {
      super("ResCur", var1, var2, var3, var4, var5);
   }

   public EResourceCursor(EnCursorResourceFilter var1, LDAPConnection var2, LDAPSearchResults var3, int var4, EData var5, LoggerWrapper var6) {
      super("ResCurFil", var2, var3, var4, var5, var6);
      this.filter = var1;
      this.getCurrentResource();
   }

   public Properties getCurrentProperties() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("getCurrentProperties EResource");
      }

      EResource var1 = this.data.getResourceFromEntry(this.getCurrentEntry());
      if (var1 == null) {
         return null;
      } else {
         Properties var2 = new Properties();
         var2.setProperty("Expression", this.getEntitlement(var1));
         var2.setProperty("ResourceId", var1.getName());
         if (var1.isDeployData()) {
            var2.setProperty("SourceData", "Deployment");
         }

         String var3 = var1.getCollectionName();
         if (var3 != null) {
            var2.setProperty("CollectionName", var3);
         }

         return var2;
      }
   }

   public EResource getCurrentResource() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("getCurrentResource");
      }

      EResource var1 = this.data.getResourceFromEntry(this.getCurrentEntry());
      if (this.filter != null && var1 != null) {
         if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
            this.traceLogger.debug("getCurrentResource filter");
         }

         for(boolean var2 = this.filter.isValidResource(var1); !var2; var2 = this.filter.isValidResource(var1)) {
            this.advance(false);
            var1 = this.data.getResourceFromEntry(this.getCurrentEntry());
            if (var1 == null) {
               break;
            }
         }
      }

      return var1;
   }

   public EResource next() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("next EResource");
      }

      EResource var1 = this.getCurrentResource();
      this.advance();
      return var1;
   }

   public void advance() {
      super.advance();
      if (this.filter != null) {
         this.getCurrentResource();
      }

   }
}
