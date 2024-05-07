package weblogic.management.configuration;

import java.io.Serializable;
import java.security.AccessController;
import weblogic.management.internal.RemoteRealmManager;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public final class RealmManager implements Serializable {
   private static final long serialVersionUID = 1127506422744561803L;
   private static boolean debug = false;
   private RemoteRealmManager helper;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RealmManager(RemoteRealmManager var1) {
      if (debug) {
         this.trace("constructor");
      }

      this.helper = var1;
   }

   public User getUser(String var1) throws RealmException {
      if (debug) {
         this.trace("getUser(" + var1 + ")");
      }

      try {
         return this.helper.userExists(var1) ? new User(var1, this.helper) : null;
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.getUser", var3);
      }
   }

   public Group getGroup(String var1) throws RealmException {
      if (debug) {
         this.trace("getGroup(" + var1 + ")");
      }

      try {
         return this.helper.groupExists(var1) ? new Group(var1, this.helper) : null;
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.getGroup", var3);
      }
   }

   public Acl getAcl(String var1) throws RealmException {
      if (debug) {
         this.trace("getAcl(" + var1 + ")");
      }

      try {
         return this.helper.aclExists(var1) ? new Acl(var1, this.helper) : null;
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.getAcl", var3);
      }
   }

   public boolean addUserFailsOver() {
      return this.isReadonlyUserGroupAlternateRealm();
   }

   public boolean addGroupFailsOver() {
      return this.isReadonlyUserGroupAlternateRealm();
   }

   public boolean addAclFailsOver() {
      return false;
   }

   public User createUser(String var1, Object var2) throws RealmException {
      if (debug) {
         this.trace("createUser(" + var1 + ")");
      }

      try {
         this.helper.createUser(var1, var2);
         return new User(var1, this.helper);
      } catch (Throwable var4) {
         throw new RealmException("RealmManager.createUser", var4);
      }
   }

   public Group createGroup(String var1) throws RealmException {
      if (debug) {
         this.trace("createGroup(" + var1 + ")");
      }

      try {
         this.helper.createGroup(var1);
         return new Group(var1, this.helper);
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.createGroup", var3);
      }
   }

   public Acl createAcl(String var1) throws RealmException {
      if (debug) {
         this.trace("createAcl(" + var1 + ")");
      }

      try {
         this.helper.createAcl(var1);
         return new Acl(var1, this.helper);
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.createAcl", var3);
      }
   }

   public void removeUser(User var1) throws RealmException {
      if (debug) {
         this.trace("removeUser(" + var1.getName() + ")");
      }

      try {
         this.helper.removeUser(var1.getName());
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.removeUser", var3);
      }
   }

   public void removeGroup(Group var1) throws RealmException {
      if (debug) {
         this.trace("removeGroup(" + var1.getName() + ")");
      }

      try {
         this.helper.removeGroup(var1.getName());
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.removeGroup", var3);
      }
   }

   public void removeAcl(Acl var1) throws RealmException {
      if (debug) {
         this.trace("removeAcl(" + var1.getName() + ")");
      }

      try {
         this.helper.removeAcl(var1.getName());
      } catch (Throwable var3) {
         throw new RealmException("RealmManager.removeAcl", var3);
      }
   }

   public RealmIterator listUsers() throws RealmException {
      if (debug) {
         this.trace("listUsers");
      }

      try {
         return new RealmIterator(this.helper.listUsers(), new RealmIterator.ElementHandler() {
            public Object handle(Object var1) {
               if (RealmManager.debug) {
                  RealmManager.this.trace("listUsers handle " + (String)var1);
               }

               return new User((String)var1, RealmManager.this.helper);
            }
         });
      } catch (Throwable var2) {
         throw new RealmException("RealmManager.listUsers", var2);
      }
   }

   public RealmIterator listGroups() throws RealmException {
      if (debug) {
         this.trace("listGroups");
      }

      try {
         return new RealmIterator(this.helper.listGroups(), new RealmIterator.ElementHandler() {
            public Object handle(Object var1) {
               if (RealmManager.debug) {
                  RealmManager.this.trace("listGroups handle " + (String)var1);
               }

               return new Group((String)var1, RealmManager.this.helper);
            }
         });
      } catch (Throwable var2) {
         throw new RealmException("RealmManager.listGroups", var2);
      }
   }

   public RealmIterator listAcls() throws RealmException {
      if (debug) {
         this.trace("listAcls");
      }

      try {
         return new RealmIterator(this.helper.listAcls(), new RealmIterator.ElementHandler() {
            public Object handle(Object var1) {
               if (RealmManager.debug) {
                  RealmManager.this.trace("listAcls handle " + (String)var1);
               }

               return new Acl((String)var1, RealmManager.this.helper);
            }
         });
      } catch (Throwable var2) {
         throw new RealmException("RealmManager.listAcls", var2);
      }
   }

   private boolean isReadonlyUserGroupAlternateRealm() {
      CachingRealmMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm();
      if (var1 != null) {
         BasicRealmMBean var2 = var1.getBasicRealm();
         if (var2 instanceof LDAPRealmMBean || var2 instanceof UnixRealmMBean || var2 instanceof NTRealmMBean) {
            return true;
         }

         if (var2 instanceof CustomRealmMBean) {
            CustomRealmMBean var3 = (CustomRealmMBean)var2;
            String var4 = var3.getRealmClassName();
            if ("weblogic.security.ldaprealmv2.LDAPRealm".equals(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   private void trace(String var1) {
      System.out.println("RealmManger " + var1);
   }
}
