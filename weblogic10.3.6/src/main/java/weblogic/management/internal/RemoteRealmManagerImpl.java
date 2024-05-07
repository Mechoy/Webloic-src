package weblogic.management.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.HashSet;
import weblogic.management.PrincipalInfo;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ListResults;
import weblogic.management.configuration.RealmMBean;
import weblogic.management.configuration.RemoteEnumeration;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.server.UnicastRemoteObject;
import weblogic.security.acl.CredentialChanger;
import weblogic.security.acl.ManageableRealm;
import weblogic.security.acl.Realm;
import weblogic.security.acl.User;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.enumerations.IteratorEnumerator;

public class RemoteRealmManagerImpl implements RemoteRealmManager {
   private static boolean debug = false;
   private int batchSize = 200;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RemoteRealmManagerImpl() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (var1 != null) {
         SecurityMBean var2 = var1.getSecurity();
         if (var2 != null) {
            RealmMBean var3 = var2.getRealm();
            if (var3 != null) {
               this.batchSize = var3.getResultsBatchSize();
            }
         }
      }

      if (debug) {
         this.trace("constructor");
      }

   }

   public ListResults getMembers(String var1) throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("getMembers(" + var1 + ")");
      }

      this.checkReadAccess();
      Group var2 = this.getGroup(var1);
      return this.getListResults(var2.members(), new BatchedEnumeration.ElementHandler() {
         public Object handle(Object var1) {
            if (RemoteRealmManagerImpl.debug) {
               RemoteRealmManagerImpl.this.trace("getMembers.handle(" + ((Principal)var1).getName() + ")");
            }

            return RemoteRealmManagerImpl.this.getPrincipalInfo((Principal)var1);
         }
      });
   }

   public String[] getPermissions(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("getPermissions(" + var1 + ")");
      }

      this.checkReadAccess();
      Acl var2 = this.getAcl(var1);
      HashSet var3 = new HashSet();
      Enumeration var4 = var2.entries();

      while(var4.hasMoreElements()) {
         AclEntry var5 = (AclEntry)var4.nextElement();
         Enumeration var6 = var5.permissions();

         while(var6.hasMoreElements()) {
            String var7 = this.getPermissionName((Permission)var6.nextElement());
            var3.add(var7);
         }
      }

      return (String[])((String[])var3.toArray(new String[0]));
   }

   public ListResults getGrantees(String var1, String var2) throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("getGrantees(" + var1 + "," + var2 + ")");
      }

      this.checkReadAccess();
      Acl var3 = this.getAcl(var1);
      HashSet var4 = new HashSet();
      Enumeration var5 = var3.entries();

      while(var5.hasMoreElements()) {
         AclEntry var6 = (AclEntry)var5.nextElement();
         Principal var7 = var6.getPrincipal();
         Enumeration var8 = var6.permissions();

         while(var8.hasMoreElements()) {
            if (this.getPermissionName((Permission)var8.nextElement()).equals(var2)) {
               var4.add(this.getPrincipalInfo(var7));
            }
         }
      }

      return this.getListResults(new IteratorEnumerator(var4.iterator()), new BatchedEnumeration.ElementHandler() {
         public Object handle(Object var1) {
            return var1;
         }
      });
   }

   public void createUser(String var1, Object var2) throws RemoteRealmException {
      if (debug) {
         this.trace("createUser(" + var1 + ")");
      }

      this.checkWriteAccess();
      this.getRealm().newUser(var1, var2, (Object)null);
   }

   public void createGroup(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("createGroup(" + var1 + ")");
      }

      this.checkWriteAccess();
      this.getRealm().newGroup(var1);
   }

   public void createAcl(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("createAcl(" + var1 + ")");
      }

      this.checkWriteAccess();
      this.getRealm().newAcl(this.getAclOwner(), var1);
   }

   public void removeUser(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("removeUser(" + var1 + ")");
      }

      this.checkWriteAccess();
      User var2 = this.getUser(var1);
      this.getRealm().deleteUser(var2);
   }

   public void removeGroup(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("removeGroup(" + var1 + ")");
      }

      this.checkWriteAccess();
      Group var2 = this.getGroup(var1);
      this.getRealm().deleteGroup(var2);
   }

   public void removeAcl(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("removeAcl(" + var1 + ")");
      }

      this.checkWriteAccess();
      Acl var2 = this.getAcl(var1);
      this.getRealm().deleteAcl(this.getAclOwner(), var2);
   }

   public boolean userExists(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("userExists(" + var1 + ")");
      }

      this.checkReadAccess();
      return this.getRealm().getUser(var1) != null;
   }

   public boolean groupExists(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("groupExists(" + var1 + ")");
      }

      this.checkReadAccess();
      return this.getRealm().getGroup(var1) != null;
   }

   public boolean aclExists(String var1) throws RemoteRealmException {
      if (debug) {
         this.trace("aclExists(" + var1 + ")");
      }

      this.checkReadAccess();
      return this.getRealm().getAcl(var1) != null;
   }

   public ListResults listUsers() throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("listUsers");
      }

      if (!this.isAdmin()) {
         return null;
      } else {
         this.checkReadAccess();
         return this.getListResults(this.getRealm().getUsers(), new BatchedEnumeration.ElementHandler() {
            public Object handle(Object var1) {
               if (RemoteRealmManagerImpl.debug) {
                  RemoteRealmManagerImpl.this.trace("listUsers.handle(" + ((User)var1).getName() + ")");
               }

               return var1 == null ? null : ((User)var1).getName();
            }
         });
      }
   }

   public ListResults listGroups() throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("listGroups");
      }

      if (!this.isAdmin()) {
         return null;
      } else {
         this.checkReadAccess();
         return this.getListResults(this.getRealm().getGroups(), new BatchedEnumeration.ElementHandler() {
            public Object handle(Object var1) {
               if (RemoteRealmManagerImpl.debug) {
                  RemoteRealmManagerImpl.this.trace("listGroup.handle(" + ((Group)var1).getName() + ")");
               }

               return ((Group)var1).getName();
            }
         });
      }
   }

   public ListResults listAcls() throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("listAcls");
      }

      if (!this.isAdmin()) {
         return null;
      } else {
         this.checkReadAccess();
         return this.getListResults(this.getRealm().getAcls(), new BatchedEnumeration.ElementHandler() {
            public Object handle(Object var1) {
               if (RemoteRealmManagerImpl.debug) {
                  RemoteRealmManagerImpl.this.trace("listAcls.handle(" + ((Acl)var1).getName() + ")");
               }

               return ((Acl)var1).getName();
            }
         });
      }
   }

   public boolean changeCredential(String var1, Object var2, Object var3) throws RemoteException, RemoteRealmException {
      if (debug) {
         this.trace("changeCredential(" + var1 + "," + var2 + "," + var3 + ")");
      }

      this.checkWriteAccess();
      User var4 = this.getUser(var1);
      if (var4 instanceof CredentialChanger) {
         CredentialChanger var5 = (CredentialChanger)var4;
         var5.changeCredential(var2, var3);
         return true;
      } else {
         return false;
      }
   }

   public void addMember(String var1, String var2) throws RemoteRealmException {
      if (debug) {
         this.trace("addMember(" + var1 + "," + var2 + ")");
      }

      this.checkWriteAccess();
      Group var3 = this.getGroup(var1);
      Principal var4 = this.getPrincipal(var2);
      if (!var3.addMember(var4)) {
         throw new RemoteRealmException(var2 + " is already a member of the " + var1 + " group.");
      }
   }

   public void removeMember(String var1, String var2) throws RemoteRealmException {
      if (debug) {
         this.trace("removeMember(" + var1 + "," + var2 + ")");
      }

      this.checkWriteAccess();
      Group var3 = this.getGroup(var1);
      Principal var4 = this.getPrincipal(var2);
      if (!var3.removeMember(var4)) {
         throw new RemoteRealmException(var2 + " is not a member of the " + var1 + " group.");
      }
   }

   public void grantPermission(String var1, String var2, String var3) throws RemoteRealmException {
      if (debug) {
         this.trace("grantPermission(" + var1 + "," + var2 + "," + var3 + ")");
      }

      this.checkWriteAccess();
      Acl var4 = this.getAcl(var1);
      Principal var5 = this.getPrincipal(var2);
      Permission var6 = this.getRealm().getPermission(var3);
      this.getRealm().setPermission(var4, var5, var6, true);
   }

   public void revokePermission(String var1, String var2, String var3) throws RemoteRealmException {
      if (debug) {
         this.trace("revokePermission(" + var1 + "," + var2 + "," + var3 + ")");
      }

      this.checkWriteAccess();
      Acl var4 = this.getAcl(var1);
      Principal var5 = this.getPrincipal(var2);
      Permission var6 = this.getRealm().getPermission(var3);
      Enumeration var7 = var4.entries();

      AclEntry var8;
      do {
         if (!var7.hasMoreElements()) {
            throw new RemoteRealmException(var3 + " for " + var2 + " not on " + var1);
         }

         var8 = (AclEntry)var7.nextElement();
      } while(!var8.getPrincipal().equals(var5));

      AclEntry var9 = (AclEntry)var8.clone();

      try {
         if (!var9.removePermission(var6) || !var4.removeEntry(this.getAclOwner(), var8) || !var4.addEntry(this.getAclOwner(), var9)) {
            throw new RemoteRealmException("Couldn't revoke permission " + var3 + " for " + var2 + " to " + var1);
         }
      } catch (NotOwnerException var11) {
         throw new RemoteRealmException("Couldn't revoke permission " + var3 + " for " + var2 + " to " + var1);
      }
   }

   private String getPermissionName(Permission var1) {
      return var1.toString();
   }

   private PrincipalInfo getPrincipalInfo(Principal var1) {
      return var1 instanceof Group ? new PrincipalInfo(var1.getName(), true) : new PrincipalInfo(var1.getName(), false);
   }

   private Principal getPrincipal(String var1) throws RemoteRealmException {
      Group var2 = this.getRealm().getGroup(var1);
      if (var2 != null) {
         return var2;
      } else {
         User var3 = this.getRealm().getUser(var1);
         if (var3 != null) {
            return var3;
         } else {
            throw new RemoteRealmException("Principal " + var1 + " doesn't exist.");
         }
      }
   }

   private User getUser(String var1) throws RemoteRealmException {
      User var2 = this.getRealm().getUser(var1);
      if (var2 == null) {
         throw new RemoteRealmException("User " + var1 + " doesn't exist.");
      } else {
         return var2;
      }
   }

   private Group getGroup(String var1) throws RemoteRealmException {
      Group var2 = this.getRealm().getGroup(var1);
      if (var2 == null) {
         throw new RemoteRealmException("Group " + var1 + " doesn't exist.");
      } else {
         return var2;
      }
   }

   private Acl getAcl(String var1) throws RemoteRealmException {
      Acl var2 = this.getRealm().getAcl(var1);
      if (var2 == null) {
         throw new RemoteRealmException("Acl " + var1 + " doesn't exist.");
      } else {
         return var2;
      }
   }

   private ManageableRealm getRealm() {
      return (ManageableRealm)Realm.getRealm("weblogic");
   }

   private Principal getAclOwner() {
      String var1 = (String)SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            return ManagementService.getPropertyService(RemoteRealmManagerImpl.kernelId).getTimestamp2();
         }
      });
      return this.getRealm().getAclOwner(var1);
   }

   private boolean isAdmin() {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer();
   }

   private ListResults getListResults(Enumeration var1, BatchedEnumeration.ElementHandler var2) throws RemoteException {
      if (debug) {
         this.trace("getListResults");
      }

      boolean var3 = false;
      BatchedEnumeration var4 = new BatchedEnumeration(var1, this.batchSize, var2);
      Object[] var5 = var4.getNextBatch();
      RemoteEnumerationImpl var6 = var5 != null && var4.hasMoreElements() ? new RemoteEnumerationImpl(var4) : null;
      RemoteEnumeration var7 = var6 != null ? (RemoteEnumeration)((RemoteEnumeration)UnicastRemoteObject.exportObject(var6)) : null;
      return new ListResults(var5, var7);
   }

   private void trace(String var1) {
      System.out.println("RemoteRealmManagerImpl " + var1);
   }

   private void checkReadAccess() throws RemoteRealmException {
   }

   private void checkWriteAccess() throws RemoteRealmException {
      if (!this.isAdmin()) {
         throw new RemoteRealmException("Realms cannot be managed by managed servers.");
      }
   }
}
