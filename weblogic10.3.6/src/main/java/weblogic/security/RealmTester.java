package weblogic.security;

import java.security.AccessController;
import weblogic.management.configuration.Acl;
import weblogic.management.configuration.Group;
import weblogic.management.configuration.Principal;
import weblogic.management.configuration.RealmException;
import weblogic.management.configuration.RealmIterator;
import weblogic.management.configuration.RealmManager;
import weblogic.management.configuration.User;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class RealmTester {
   private static boolean verboseex = true;
   private RealmManager realm;
   private static boolean debugPrintRealm = false;
   private static boolean debugEditRealm = false;

   public static void runTest() {
      if (debugPrintRealm | debugEditRealm) {
         new RealmTester();
      }

   }

   private void handleEx(Throwable var1) {
      if (verboseex) {
         System.out.println("Unexpected exception : " + var1);
         var1.printStackTrace();
      }

   }

   private void handleEx(Throwable var1, boolean var2) {
      if (var2) {
         this.handleEx(var1);
      }

   }

   private String print(User var1) {
      return var1.getName();
   }

   private String print(Group var1) {
      String var2 = var1.getName() + '(';

      try {
         RealmIterator var3 = var1.getMembers();

         try {
            for(boolean var4 = true; var3.hasNext(); var2 = var2 + this.print((Principal)((Principal)var3.next()))) {
               if (!var4) {
                  var2 = var2 + ',';
               } else {
                  var4 = false;
               }
            }
         } finally {
            var3.close();
         }
      } catch (Exception var10) {
         this.handleEx(var10);
         var2 = var2 + var10;
      }

      var2 = var2 + ')';
      return var2;
   }

   private String print(Principal var1) {
      String var2 = "";
      if (var1 instanceof User) {
         var2 = var2 + "User";
      } else if (var1 instanceof Group) {
         var2 = var2 + "Group";
      } else {
         var2 = var2 + "Unknown";
      }

      var2 = var2 + '{' + var1.getName() + '}';
      return var2;
   }

   private String print(Acl var1) {
      String var2 = var1.getName() + '(';
      String[] var3 = null;

      try {
         var3 = var1.getPermissions();
      } catch (Exception var13) {
         this.handleEx(var13);
         var2 = var2 + var13;
      }

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         if (var4 != 0) {
            var2 = var2 + ',';
         }

         String var5 = var3[var4];
         var2 = var2 + '[' + var5 + '=';

         try {
            RealmIterator var6 = var1.getGrantees(var5);

            try {
               for(boolean var7 = true; var6.hasNext(); var2 = var2 + this.print((Principal)((Principal)var6.next()))) {
                  if (!var7) {
                     var2 = var2 + ',';
                  } else {
                     var7 = false;
                  }
               }
            } finally {
               var6.close();
            }
         } catch (RealmException var15) {
            this.handleEx(var15);
            var2 = var2 + var15;
         }

         var2 = var2 + ']';
      }

      var2 = var2 + ')';
      return var2;
   }

   private String getPass(boolean var1) {
      return var1 ? "SUCCESS : " : "FAILURE : ";
   }

   private String getFail(boolean var1) {
      return !var1 ? "SUCCESS : " : "FAILURE : ";
   }

   private User createUser(String var1, boolean var2) {
      try {
         User var3 = this.realm.createUser(var1, var1 + "_password");
         System.out.println(this.getPass(var2) + " created user " + this.print(var3));
         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't create user " + var1 + " : " + var4);
         return null;
      }
   }

   private User getUser(String var1, boolean var2) {
      try {
         User var3 = this.realm.getUser(var1);
         if (var3 != null) {
            System.out.println(this.getPass(var2) + " got user " + this.print(var3));
         } else {
            System.out.println(this.getFail(var2) + " couldn't get user " + var1);
         }

         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println("FAILURE :  error getting user " + var1 + " : " + var4);
         return null;
      }
   }

   private void removeUser(User var1, boolean var2) {
      try {
         this.realm.removeUser(var1);
         System.out.println(this.getPass(var2) + " removed user " + this.print(var1));
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't remove user " + this.print(var1) + " : " + var4);
      }

   }

   private Group createGroup(String var1, boolean var2) {
      try {
         Group var3 = this.realm.createGroup(var1);
         System.out.println(this.getPass(var2) + " created group " + this.print(var3));
         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't create group " + var1 + " : " + var4);
         return null;
      }
   }

   private Group getGroup(String var1, boolean var2) {
      try {
         Group var3 = this.realm.getGroup(var1);
         if (var3 != null) {
            System.out.println(this.getPass(var2) + " got group " + this.print(var3));
         } else {
            System.out.println(this.getFail(var2) + " couldn't get group " + var1);
         }

         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println("FAILURE :  error getting group " + var1 + " : " + var4);
         return null;
      }
   }

   private void removeGroup(Group var1, boolean var2) {
      try {
         this.realm.removeGroup(var1);
         System.out.println(this.getPass(var2) + " removed group " + this.print(var1));
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't remove group " + this.print(var1) + " : " + var4);
      }

   }

   private void changeUserCredential(User var1, Object var2, Object var3, boolean var4) {
      try {
         boolean var5 = var1.changeCredential(var2, var3);
         if (var5) {
            System.out.println(this.getPass(var4) + " changed credential for " + this.print(var1) + " from " + var2 + " to " + var3);
         } else {
            System.out.println(this.getPass(var4) + " wouldn't change credential for " + this.print(var1) + " from " + var2 + " to " + var3);
         }
      } catch (Exception var6) {
         this.handleEx(var6, var4);
         System.out.println(this.getFail(var4) + " couldn't change credential for " + this.print(var1) + " from " + var2 + " to " + var3);
      }

   }

   private void addGroupMember(Group var1, Principal var2, boolean var3) {
      try {
         var1.addMember(var2);
         System.out.println(this.getPass(var3) + " added " + this.print(var2) + " to " + this.print(var1));
      } catch (Exception var5) {
         this.handleEx(var5, var3);
         System.out.println(this.getFail(var3) + " couldn't add " + this.print(var2) + " to " + this.print(var1) + " : " + var5);
      }

   }

   private void removeGroupMember(Group var1, Principal var2, boolean var3) {
      try {
         var1.removeMember(var2);
         System.out.println(this.getPass(var3) + " removed " + this.print(var2) + " from " + this.print(var1));
      } catch (Exception var5) {
         this.handleEx(var5, var3);
         System.out.println(this.getFail(var3) + " couldn't remove " + this.print(var2) + " from " + this.print(var1) + " : " + var5);
      }

   }

   private Acl createAcl(String var1, boolean var2) {
      try {
         Acl var3 = this.realm.createAcl(var1);
         System.out.println(this.getPass(var2) + " created acl " + this.print(var3));
         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't create acl " + var1 + " : " + var4);
         return null;
      }
   }

   private Acl getAcl(String var1, boolean var2) {
      try {
         Acl var3 = this.realm.getAcl(var1);
         if (var3 != null) {
            System.out.println(this.getPass(var2) + " got acl " + this.print(var3));
         } else {
            System.out.println(this.getFail(var2) + " couldn't get acl " + var1);
         }

         return var3;
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println("FAILURE :  error getting acl " + var1 + " : " + var4);
         return null;
      }
   }

   private void removeAcl(Acl var1, boolean var2) {
      try {
         this.realm.removeAcl(var1);
         System.out.println(this.getPass(var2) + " removed acl " + this.print(var1));
      } catch (Exception var4) {
         this.handleEx(var4, var2);
         System.out.println(this.getFail(var2) + " couldn't remove acl " + this.print(var1) + " : " + var4);
      }

   }

   private void grantPermission(Acl var1, Principal var2, String var3, boolean var4) {
      try {
         var1.grantPermission(var2, var3);
         System.out.println(this.getPass(var4) + " added " + var3 + " for " + this.print(var2) + " to " + this.print(var1));
      } catch (Exception var6) {
         this.handleEx(var6, var4);
         System.out.println(this.getFail(var4) + " couldn't add " + var3 + " for " + this.print(var2) + " to " + this.print(var1) + " : " + var6);
      }

   }

   private void revokePermission(Acl var1, Principal var2, String var3, boolean var4) {
      try {
         var1.revokePermission(var2, var3);
         System.out.println(this.getPass(var4) + " revoked " + var3 + " for " + this.print(var2) + " from " + this.print(var1));
      } catch (Exception var6) {
         this.handleEx(var6, var4);
         System.out.println(this.getFail(var4) + " couldn't revoke " + var3 + " for " + this.print(var2) + " from " + this.print(var1) + " : " + var6);
      }

   }

   private void getGrantees(Acl var1, String var2, boolean var3) {
      try {
         RealmIterator var4 = var1.getGrantees(var2);
         var4.close();
         System.out.println(this.getPass(var3) + " got grantees for " + var2 + " from " + this.print(var1));
      } catch (Exception var5) {
         this.handleEx(var5, var3);
         System.out.println(this.getFail(var3) + " couldn't get grantees for " + var2 + " from " + this.print(var1) + " : " + var5);
      }

   }

   private void printRealm() {
      RealmIterator var1;
      try {
         var1 = this.realm.listUsers();

         try {
            System.out.println("users = ");

            while(var1.hasNext()) {
               System.out.println('\t' + this.print((User)((User)var1.next())));
            }
         } finally {
            var1.close();
         }
      } catch (Throwable var31) {
         this.handleEx(var31);
         System.out.println("FAILURE : listUsers failed : " + var31);
      }

      try {
         var1 = this.realm.listGroups();

         try {
            System.out.println("groups = ");

            while(var1.hasNext()) {
               System.out.println('\t' + this.print((Group)((Group)var1.next())));
            }
         } finally {
            var1.close();
         }
      } catch (Throwable var29) {
         this.handleEx(var29);
         System.out.println("FAILURE : listGroups failed : " + var29);
      }

      try {
         var1 = this.realm.listAcls();

         try {
            System.out.println("acls = ");

            while(var1.hasNext()) {
               System.out.println('\t' + this.print((Acl)((Acl)var1.next())));
            }
         } finally {
            var1.close();
         }
      } catch (Throwable var27) {
         this.handleEx(var27);
         System.out.println("FAILURE : listAcls failed : " + var27);
      }

   }

   private void testEdit() {
      if (debugEditRealm) {
         User var1 = this.createUser(" \t=:#!u \t=:#!0 \t=:#!", true);
         Group var2 = this.createGroup(" \t=:#!g \t=:#!0 \t=:#!", true);
         Acl var3 = this.createAcl(" \t=:#!a \t=:#!0 \t=:#!", true);
         this.addGroupMember(var2, var1, true);
         this.grantPermission(var3, var2, " \t=:#!p \t=:#!0 \t=:#!", true);
         this.grantPermission(var3, var1, " \t=:#!p \t=:#!0 \t=:#!", true);
         this.createUser("u,u", false);
         this.createGroup("g,g", false);
         this.grantPermission(var3, var1, "p.p", false);
         this.getUser("noSuchUser", false);
         this.getGroup("noSuchGroup", false);
         this.getAcl("noSuchAcl", false);
         User var4 = this.createUser("u1", true);
         this.getUser("u1", true);
         this.createUser("u1", false);
         Group var5 = this.createGroup("g1", true);
         this.getGroup("g1", true);
         this.createGroup("g1", false);
         Acl var6 = this.createAcl("a1", true);
         this.getAcl("a1", true);
         this.createAcl("a1", false);
         this.getGrantees(var6, "noSuchPermission", true);
         this.changeUserCredential(var4, "u1_password_bogus", "u1_password1", false);
         this.changeUserCredential(var4, "u1_password", "u1_password2", true);
         this.changeUserCredential(var4, "u1_password", "u1_password3", false);
         this.changeUserCredential(var4, "u1_password2", "u1_password4", true);
         this.addGroupMember(var5, var4, true);
         this.addGroupMember(var5, var4, false);
         this.grantPermission(var6, var4, "p1", true);
         this.grantPermission(var6, var4, "p1", false);
         Group var7 = this.createGroup("g2", true);
         this.addGroupMember(var5, var7, true);
         this.addGroupMember(var5, var7, false);
         this.grantPermission(var6, var7, "p1", true);
         this.grantPermission(var6, var7, "p1", false);
         Group var8 = this.getGroup("everyone", true);
         this.removeGroupMember(var8, var4, false);
         this.addGroupMember(var5, var5, false);
         this.removeGroupMember(var5, var5, false);
         User var9 = this.createUser("u3", true);
         this.addGroupMember(var5, var9, true);
         this.removeGroupMember(var5, var9, true);
         this.removeGroupMember(var5, var9, false);
         this.grantPermission(var6, var9, "p1", true);
         this.revokePermission(var6, var9, "p1", true);
         this.revokePermission(var6, var9, "p1", false);
         Group var10 = this.createGroup("g3", true);
         this.addGroupMember(var5, var10, true);
         this.removeGroupMember(var5, var10, true);
         this.removeGroupMember(var5, var10, false);
         this.grantPermission(var6, var10, "p1", true);
         this.revokePermission(var6, var10, "p1", true);
         this.revokePermission(var6, var10, "p1", false);
         User var11 = this.createUser("u4", true);
         this.addGroupMember(var5, var11, true);
         this.grantPermission(var6, var11, "p1", true);
         this.removeUser(var11, true);
         this.removeUser(var11, false);
         Group var12 = this.createGroup("g4", true);
         this.addGroupMember(var5, var12, true);
         this.grantPermission(var6, var12, "p1", true);
         this.removeGroup(var12, true);
         this.removeGroup(var12, false);
         Acl var13 = this.createAcl("a4", true);
         this.removeAcl(var13, true);
         this.removeAcl(var13, false);
         User var14 = this.createUser("u5", true);
         Group var15 = this.createGroup("g5", true);
         this.grantPermission(var6, var14, "p5", true);
         this.grantPermission(var6, var15, "p5", true);
         this.revokePermission(var6, var14, "p5", true);
         this.revokePermission(var6, var15, "p5", true);
         User var16 = this.createUser("uOld", true);
         User var17 = this.createUser("uNew", true);
         this.changeUserCredential(var17, "uNew_password", "uOld_password", true);
      }
   }

   private void testPrint() {
      if (debugPrintRealm) {
         this.printRealm();
      }
   }

   private void test() {
      this.testEdit();
      this.testPrint();
   }

   private RealmTester() {
      try {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.realm = ManagementService.getRuntimeAccess(var1).getDomain().getSecurity().getRealm().manager();
      } catch (Exception var2) {
         this.handleEx(var2);
         throw new Error("error running Realm tester: " + var2);
      }

      this.test();
   }
}
