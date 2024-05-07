package weblogic.security.acl;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.security.audit.Audit;

/** @deprecated */
public abstract class AbstractListableRealm implements ListableRealm, FlatGroup.Source {
   private String name;
   private String auditName;
   private Object delegator;
   private CachingRealm alligator;

   protected AbstractListableRealm(String var1) {
      this.auditName = var1 != null ? var1 : "Custom Realm";
   }

   public void init(String var1, Object var2) throws NotOwnerException {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public User getUser(String var1) {
      throw new UnsupportedOperationException("getUser not supported");
   }

   /** @deprecated */
   public User getUser(UserInfo var1) {
      return this.authenticate(var1);
   }

   public User authenticate(UserInfo var1) {
      User var2 = this.authInternal(var1);
      Audit.authenticateUser(this.auditName, var1, var2);
      return var2;
   }

   protected User authInternal(UserInfo var1) {
      User var2 = null;
      if (var1 instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var3 = (DefaultUserInfoImpl)var1;
         String var4 = var3.getName();
         if (var3.hasCertificates()) {
            var2 = this.authCertificates(var4, var3.getCertificates());
         }

         if (var2 == null && var3.hasPassword()) {
            var2 = this.authUserPassword(var4, var3.getPassword());
         }

         if (var2 == null && var3 instanceof SSLUserInfo) {
            SSLUserInfo var5 = (SSLUserInfo)var3;
            var2 = this.authSSLCertificates(var4, var5.getSSLCertificates());
         }
      }

      return var2;
   }

   protected User authCertificates(String var1, Vector var2) {
      return CertAuthentication.authenticate(var1, var2, false);
   }

   protected User authSSLCertificates(String var1, Vector var2) {
      return CertAuthentication.authenticate(var1, var2, true);
   }

   protected User authUserPassword(String var1, String var2) {
      return null;
   }

   public Principal getAclOwner(Object var1) {
      throw new UnsupportedOperationException("getAclOwner not supported");
   }

   public Hashtable getGroupMembers(String var1) {
      Hashtable var2 = null;
      if (this.alligator != null) {
         Group var3 = this.alligator.lookupGroup(var1);
         if (var3 != null && var3 instanceof FlatGroup) {
            var2 = ((FlatGroup)var3).getMembersHashtable();
         }
      }

      if (var2 == null) {
         var2 = this.getGroupMembersInternal(var1);
      }

      return var2;
   }

   protected Hashtable getGroupMembersInternal(String var1) {
      throw new UnsupportedOperationException("getGroupMembersInternal not supported");
   }

   public Group getGroup(String var1) {
      throw new UnsupportedOperationException("getGroup not supported");
   }

   public Acl getAcl(String var1) {
      throw new UnsupportedOperationException("getAcl not supported");
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
      throw new UnsupportedOperationException("getPermission not supported");
   }

   public void load(String var1, Object var2) throws ClassNotFoundException, IOException, NotOwnerException {
   }

   public void save(String var1) throws IOException {
   }

   public Enumeration getUsers() {
      throw new UnsupportedOperationException("getUsers not supported");
   }

   public Enumeration getGroups() {
      throw new UnsupportedOperationException("getGroups not supported");
   }

   public Enumeration getAcls() {
      throw new UnsupportedOperationException("getAcls not supported");
   }

   public Enumeration getPermissions() {
      throw new UnsupportedOperationException("getPermissions not supported");
   }

   public void setDelegator(Object var1) {
      if (this.delegator != null) {
         throw new SecurityException("attempt to change delegator");
      } else if (!(var1 instanceof BasicRealm)) {
         throw new SecurityException("attempt to set a non-realm as delegator");
      } else {
         this.delegator = var1;
         if (var1 instanceof CachingRealm) {
            this.alligator = (CachingRealm)var1;
         }

      }
   }

   public Object getDelegator() {
      return this.delegator;
   }
}
