package weblogic.management.configuration;

import weblogic.management.internal.RemoteRealmManager;

/** @deprecated */
public class User extends Principal {
   private static boolean debug = false;
   private RemoteRealmManager helper;

   User(String var1, RemoteRealmManager var2) {
      super(var1);
      this.helper = var2;
      if (debug) {
         this.trace("constructor");
      }

   }

   public boolean changeCredential(Object var1, Object var2) throws RealmException {
      if (debug) {
         this.trace("changeCredential(" + var1 + "," + var2 + ")");
      }

      try {
         return this.helper.changeCredential(this.getName(), var1, var2);
      } catch (Throwable var4) {
         throw new RealmException("User.changeCredential", var4);
      }
   }

   private void trace(String var1) {
      System.out.println("User " + this.getName() + " " + var1);
   }
}
