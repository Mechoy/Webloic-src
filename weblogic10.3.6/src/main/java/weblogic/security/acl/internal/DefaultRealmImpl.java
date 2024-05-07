package weblogic.security.acl.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.AclEntryImpl;
import weblogic.security.acl.AclImpl;
import weblogic.security.acl.CertAuthentication;
import weblogic.security.acl.DefaultUserImpl;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.GroupImpl;
import weblogic.security.acl.ManageableRealm;
import weblogic.security.acl.PermissionImpl;
import weblogic.security.acl.SSLUserInfo;
import weblogic.security.acl.User;
import weblogic.security.acl.UserInfo;
import weblogic.security.audit.Audit;

public final class DefaultRealmImpl implements ManageableRealm {
   private static final long serialVersionUID = -2094130678440103135L;
   private static final String ACL_OWNER_NAME = "system";
   String name;
   protected Hashtable users = new Hashtable();
   private DefaultUserImpl aclOwner;
   protected Hashtable groups = new Hashtable();
   protected Hashtable acls = new Hashtable();
   protected Hashtable permissions = new Hashtable();

   public void init(String var1, Object var2) throws NotOwnerException {
      if (this.aclOwner != null && this.aclOwner != this.getAclOwner(var2)) {
         throw new NotOwnerException();
      } else {
         this.name = var1;
         this.aclOwner = new DefaultUserImpl("system", var2, this);
      }
   }

   public String getName() {
      return this.name;
   }

   public User getUser(String var1) {
      return (User)this.users.get(var1);
   }

   public User getUser(UserInfo var1) {
      return this.authInternal(var1);
   }

   private User authInternal(UserInfo var1) {
      User var2 = null;
      if (var1 instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var3 = (DefaultUserInfoImpl)var1;
         String var4 = var3.getName();
         if (var3.hasCertificates()) {
            var2 = this.authCertificates(var4, var3.getCertificates());
         }

         if (var2 == null && var3.hasPassword()) {
            var2 = this.authUserPassword(var3);
         }

         if (var2 == null && var3 instanceof SSLUserInfo) {
            SSLUserInfo var5 = (SSLUserInfo)var3;
            var2 = this.authSSLCertificates(var4, var5.getSSLCertificates());
         }
      } else {
         var2 = this.authUserPassword(var1);
      }

      return var2;
   }

   private User authUserPassword(UserInfo var1) {
      User var2 = this.getUser(var1.getName());
      User var3 = var2 instanceof DefaultUserImpl && ((DefaultUserImpl)var2).hasMatchingInfo(var1) ? var2 : null;
      Audit.authenticateUser("Default Realm", var1, var3);
      return var3;
   }

   private User authCertificates(String var1, Vector var2) {
      User var3 = CertAuthentication.authenticate(var1, var2, false);
      return var3 != null ? this.getUser(var3.getName()) : null;
   }

   private User authSSLCertificates(String var1, Vector var2) {
      User var3 = CertAuthentication.authenticate(var1, var2, true);
      return var3 != null ? this.getUser(var3.getName()) : null;
   }

   public Principal getAclOwner(Object var1) {
      return this.aclOwner.hasMatchingInfo(new DefaultUserInfoImpl("system", var1, this.name)) ? this.aclOwner : null;
   }

   public Group getGroup(String var1) {
      return (Group)this.groups.get(var1);
   }

   public Acl getAcl(String var1) {
      return (Acl)this.acls.get(var1);
   }

   public Acl getAcl(String var1, char var2) {
      Acl var3 = this.getAcl(var1);

      for(int var4 = var1.lastIndexOf(var2); var3 == null && var4 >= 0; var4 = var1.lastIndexOf(var2, var4 - 1)) {
         var1 = var1.substring(0, var4);
         var3 = this.getAcl(var1);
      }

      return var3;
   }

   public Permission getPermission(String var1) {
      if (var1 == null) {
         return null;
      } else {
         Permission var2 = (Permission)this.permissions.get(var1);
         return var2 != null ? var2 : this.newPermission(var1);
      }
   }

   public void load(String var1, Object var2) throws ClassNotFoundException, IOException, NotOwnerException {
      if (this.aclOwner != null && this.aclOwner != this.getAclOwner(var2)) {
         throw new NotOwnerException();
      } else {
         File var3 = new File(var1);
         if (var3.exists()) {
            ObjectInputStream var4 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(var3)));
            String var5 = (String)var4.readObject();
            Hashtable var6 = (Hashtable)var4.readObject();
            Hashtable var7 = (Hashtable)var4.readObject();
            Hashtable var8 = (Hashtable)var4.readObject();
            Hashtable var9 = (Hashtable)var4.readObject();
            this.users = var6;
            this.groups = var7;
            this.permissions = var8;
            this.acls = var9;
         }
      }
   }

   public void save(String var1) throws IOException {
      BufferedOutputStream var2 = new BufferedOutputStream(new FileOutputStream(var1));
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var1);
      var3.writeObject(this.users);
      var3.writeObject(this.groups);
      var3.writeObject(this.permissions);
      var3.writeObject(this.acls);
      this.closeOS(var3);
   }

   private void closeOS(OutputStream var1) throws IOException {
      var1.close();
   }

   public Enumeration getUsers() {
      return this.users.elements();
   }

   public Enumeration getGroups() {
      return this.groups.elements();
   }

   public Enumeration getAcls() {
      return this.acls.elements();
   }

   public Enumeration getPermissions() {
      return this.permissions.elements();
   }

   public User newUser(String var1, Object var2, Object var3) throws SecurityException {
      if (this.getUser(var1) == null && this.getGroup(var1) == null) {
         DefaultUserImpl var4 = new DefaultUserImpl(var1, var2, this);
         this.users.put(var1, var4);
         return var4;
      } else {
         throw new SecurityException("Principal " + var1 + " already defined in realm " + this.getName());
      }
   }

   public Group newGroup(String var1) throws SecurityException {
      if (this.getGroup(var1) != null) {
         throw new SecurityException("Group " + var1 + " already defined in realm " + this.getName());
      } else {
         GroupImpl var2 = new GroupImpl(var1);
         this.groups.put(var1, var2);
         return var2;
      }
   }

   public Acl newAcl(Principal var1, String var2) throws SecurityException {
      if (this.aclOwner != var1) {
         throw new SecurityException(var1 + " does not own the ACL");
      } else {
         AclImpl var3 = new AclImpl(var1, var2);
         this.acls.put(var2, var3);
         return var3;
      }
   }

   public Permission newPermission(String var1) throws SecurityException {
      PermissionImpl var2 = new PermissionImpl(var1);
      this.permissions.put(var1, var2);
      return var2;
   }

   public void deleteUser(User var1) throws SecurityException {
      this.users.remove(var1.getName());
      this.deletePrincipal(var1);
   }

   public void deleteGroup(Group var1) throws SecurityException {
      this.groups.remove(var1.getName());
      this.deletePrincipal(var1);
   }

   public void deletePermission(Permission var1) throws SecurityException {
      this.permissions.remove(var1 instanceof PermissionImpl ? ((PermissionImpl)var1).getName() : var1.toString());
   }

   protected void deletePrincipal(Principal var1) {
      synchronized(this.acls) {
         Enumeration var3 = ((Hashtable)this.acls.clone()).keys();

         while(var3.hasMoreElements()) {
            String var4 = var3.nextElement().toString();
            Acl var5 = this.getAcl(var4);
            Acl var6 = this.newAcl(this.aclOwner, var5.getName());
            Enumeration var7 = var5.entries();

            while(var7.hasMoreElements()) {
               AclEntry var8 = (AclEntry)var7.nextElement();
               if (!var8.getPrincipal().equals(var1)) {
                  try {
                     var6.addEntry(this.aclOwner, var8);
                  } catch (NotOwnerException var11) {
                  }
               }
            }
         }

      }
   }

   public void deleteAcl(Principal var1, Acl var2) throws SecurityException {
      if (this.aclOwner != var1) {
         throw new SecurityException(var1 + " does not own the ACL");
      } else {
         this.acls.remove(var2.getName());
      }
   }

   public void setPermission(Acl var1, Principal var2, Permission var3, boolean var4) {
      try {
         Object var5 = null;
         Object var6 = null;
         Enumeration var7 = var1.entries();

         while(var7.hasMoreElements()) {
            AclEntry var8 = (AclEntry)var7.nextElement();
            if (var8.getPrincipal().equals(var2)) {
               var1.removeEntry(this.aclOwner, var8);
               if (var8.isNegative()) {
                  var6 = var8;
               } else {
                  var5 = var8;
               }
            }
         }

         if (var4) {
            if (var5 == null) {
               var5 = new AclEntryImpl(var2);
            }

            this.addRemove(var1, (AclEntry)var5, (AclEntry)var6, var3);
         } else {
            if (var6 == null) {
               var6 = new AclEntryImpl(var2);
               ((AclEntry)var6).setNegativePermissions();
            }

            this.addRemove(var1, (AclEntry)var6, (AclEntry)var5, var3);
         }

      } catch (NotOwnerException var9) {
         SecurityLogger.logStackTrace(var9);
         throw new InternalError("aclOwner not owner");
      }
   }

   private void addRemove(Acl var1, AclEntry var2, AclEntry var3, Permission var4) throws NotOwnerException {
      var2.addPermission(var4);
      var1.addEntry(this.aclOwner, var2);
      if (var3 != null) {
         var3.removePermission(var4);
         if (var3.permissions().hasMoreElements()) {
            var1.addEntry(this.aclOwner, var3);
         }

      }
   }
}
