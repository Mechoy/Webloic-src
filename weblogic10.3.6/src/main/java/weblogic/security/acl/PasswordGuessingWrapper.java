package weblogic.security.acl;

/** @deprecated */
public final class PasswordGuessingWrapper {
   PasswordGuessing pg;

   public PasswordGuessingWrapper(PasswordGuessing var1) {
      this.pg = var1;
   }

   public boolean isLocked(String var1) {
      return this.pg == null ? false : this.pg.isLocked(var1);
   }

   public void logFailure(String var1) {
      if (this.pg != null) {
         this.pg.logFailure(var1);
      }
   }

   public void logSuccess(String var1) {
      if (this.pg != null) {
         this.pg.logSuccess(var1);
      }
   }
}
