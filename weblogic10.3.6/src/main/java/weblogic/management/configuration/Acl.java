package weblogic.management.configuration;

import java.io.Serializable;
import weblogic.management.PrincipalInfo;
import weblogic.management.internal.RemoteRealmManager;

/** @deprecated */
public final class Acl implements Serializable {
   private static final long serialVersionUID = 397151266611957559L;
   private static boolean debug = false;
   private String name;
   private RemoteRealmManager helper;

   public Acl(String var1, RemoteRealmManager var2) {
      this.name = var1;
      this.helper = var2;
      if (debug) {
         this.trace("constructor");
      }

   }

   public String getName() {
      return this.name;
   }

   public String[] getPermissions() throws RealmException {
      if (debug) {
         this.trace("getPermissions");
      }

      try {
         return this.helper.getPermissions(this.getName());
      } catch (Throwable var2) {
         throw new RealmException("Acl.getPermissions", var2);
      }
   }

   public RealmIterator getGrantees(String var1) throws RealmException {
      if (debug) {
         this.trace("getGrantees(" + var1 + ")");
      }

      try {
         return new RealmIterator(this.helper.getGrantees(this.getName(), var1), new RealmIterator.ElementHandler() {
            public Object handle(Object var1) {
               PrincipalInfo var2 = (PrincipalInfo)var1;
               return var2.isGroup() ? new Group(var2.getName(), Acl.this.helper) : new User(var2.getName(), Acl.this.helper);
            }
         });
      } catch (Throwable var3) {
         throw new RealmException("Acl.getGrantees", var3);
      }
   }

   public void grantPermission(Principal var1, String var2) throws RealmException {
      if (debug) {
         this.trace("grantPermission(" + var1.getName() + "," + var2 + ")");
      }

      try {
         this.helper.grantPermission(this.getName(), var1.getName(), var2);
      } catch (Throwable var4) {
         throw new RealmException("Acl.grantPermission", var4);
      }
   }

   public void revokePermission(Principal var1, String var2) throws RealmException {
      if (debug) {
         this.trace("revokePermission(" + var1.getName() + "," + var2 + ")");
      }

      try {
         this.helper.revokePermission(this.getName(), var1.getName(), var2);
      } catch (Throwable var4) {
         throw new RealmException("Acl.revokePermission", var4);
      }
   }

   private void trace(String var1) {
      System.out.println("Acl " + this.getName() + " " + var1);
   }
}
