package weblogic.security.acl;

/** @deprecated */
final class UnlockUserRecord extends SecurityMulticastRecord {
   private static final long serialVersionUID = -8859390933291167473L;
   String user_name = null;

   UnlockUserRecord(String var1, int var2, long var3, String var5) {
      super(var1, var2, var3);
      this.user_name = var5;
   }

   String userName() {
      return this.user_name;
   }

   public String toString() {
      return super.toString() + " username: " + this.user_name;
   }
}
