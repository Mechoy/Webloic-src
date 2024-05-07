package weblogic.management.configuration;

import weblogic.management.PrincipalInfo;
import weblogic.management.internal.RemoteRealmManager;

/** @deprecated */
public class Group extends Principal {
   private static final long serialVersionUID = 2891615425511196993L;
   private static boolean debug = false;
   private RemoteRealmManager helper;

   Group(String var1, RemoteRealmManager var2) {
      super(var1);
      this.helper = var2;
      if (debug) {
         this.trace("constructor");
      }

   }

   public RealmIterator getMembers() throws RealmException {
      if (debug) {
         this.trace("getMembers");
      }

      try {
         return new RealmIterator(this.helper.getMembers(this.getName()), new RealmIterator.ElementHandler() {
            public Object handle(Object var1) {
               PrincipalInfo var2 = (PrincipalInfo)var1;
               return var2.isGroup() ? new Group(var2.getName(), Group.this.helper) : new User(var2.getName(), Group.this.helper);
            }
         });
      } catch (Throwable var2) {
         throw new RealmException("Group.getMembers", var2);
      }
   }

   public void addMember(Principal var1) throws RealmException {
      if (debug) {
         this.trace("addMember(" + var1.getName() + ")");
      }

      try {
         this.helper.addMember(this.getName(), var1.getName());
      } catch (Throwable var3) {
         throw new RealmException("Group.addMember", var3);
      }
   }

   public void removeMember(Principal var1) throws RealmException {
      if (debug) {
         this.trace("removeMember(" + var1.getName() + ")");
      }

      try {
         this.helper.removeMember(this.getName(), var1.getName());
      } catch (Throwable var3) {
         throw new RealmException("Group.removeMember", var3);
      }
   }

   private void trace(String var1) {
      System.out.println("Group " + this.getName() + " " + var1);
   }
}
