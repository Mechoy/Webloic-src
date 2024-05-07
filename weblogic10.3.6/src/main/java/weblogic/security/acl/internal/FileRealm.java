package weblogic.security.acl.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import weblogic.management.DomainDir;
import weblogic.management.configuration.FileRealmMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.management.servlet.FileDistributionServlet;
import weblogic.security.MessageDigest;
import weblogic.security.MessageDigestUtils;
import weblogic.security.SecurityLogger;
import weblogic.security.WLMessageDigest;
import weblogic.security.acl.AclEntryImpl;
import weblogic.security.acl.AclImpl;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.CachingRealm;
import weblogic.security.acl.CertAuthentication;
import weblogic.security.acl.CredentialChanger;
import weblogic.security.acl.DefaultUserImpl;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.DynamicUserAcl;
import weblogic.security.acl.Everyone;
import weblogic.security.acl.GroupImpl;
import weblogic.security.acl.ManageableRealm;
import weblogic.security.acl.PermissionImpl;
import weblogic.security.acl.Realm;
import weblogic.security.acl.RefreshableRealm;
import weblogic.security.acl.SSLUserInfo;
import weblogic.security.acl.User;
import weblogic.security.acl.UserInfo;
import weblogic.security.audit.Audit;
import weblogic.security.internal.FileUtils;
import weblogic.security.internal.FileUtilsException;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.shared.LoggerWrapper;
import weblogic.utils.Hex;
import weblogic.utils.StringUtils;

public final class FileRealm implements ManageableRealm, RefreshableRealm, DynamicUserAcl {
   private static final long serialVersionUID = -1640246370085690200L;
   private static final String FILE = "fileRealm.properties";
   private static final char PRINCIPAL_SEPARATOR = ',';
   private static final char ACL_PERMISSION_SEPARATOR = '.';
   private static final String ACL_PREFIX = "acl.";
   private static final String GROUP_PREFIX = "group.";
   private static final String USER_PREFIX = "user.";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String name;
   private String password;
   private byte[] salt;
   private MessageDigest messageDigest = WLMessageDigest.getInstance("SHA");
   private DefaultUserImpl aclOwner;
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityRealm");
   private Group everyone = new Everyone(this);
   private Hashtable users = new Hashtable();
   private Hashtable groups = new Hashtable();
   private Hashtable acls = new Hashtable();
   private Hashtable permissions = new Hashtable();
   private int maxUsers;
   private int maxGroups;
   private int maxACLs;
   private boolean addedSystemUser;
   private boolean addedGuest;

   public static void convertFromClearTextPasswords(String var0, byte[] var1, Properties var2, Properties var3, Properties var4) throws FileNotFoundException, IOException {
      MessageDigest var5 = WLMessageDigest.getInstance("SHA");
      Properties var6 = new Properties();
      Enumeration var7 = var2.keys();

      String var8;
      String var9;
      String var10;
      while(var7.hasMoreElements()) {
         var8 = (String)var7.nextElement();
         checkPrincipalName(var8);
         var9 = (String)var2.get(var8);
         var9 = var9.trim();
         var10 = "user." + var8;
         String var11 = hashPassword(var5, var1, var9);
         var6.put(var10, var11);
      }

      var7 = var3.keys();

      while(var7.hasMoreElements()) {
         var8 = (String)var7.nextElement();
         checkPrincipalName(var8);
         var9 = "group." + var8;
         var10 = (String)((String)var3.get(var8));
         var6.put(var9, var10);
      }

      var7 = var4.keys();

      while(var7.hasMoreElements()) {
         var8 = (String)var7.nextElement();
         var9 = "acl." + var8;
         var10 = (String)((String)var4.get(var8));
         var6.put(var9, var10);
      }

      writeFile(var0, var6);
   }

   private static String getRestOfKey(String var0, String var1) {
      return var0.startsWith(var1) ? var0.substring(var1.length()) : null;
   }

   private static String getUserFromKey(String var0) {
      return getRestOfKey(var0, "user.");
   }

   private static String getGroupFromKey(String var0) {
      return getRestOfKey(var0, "group.");
   }

   private static String getPermDotAclFromKey(String var0) {
      return getRestOfKey(var0, "acl.");
   }

   private static String[] splitCompletely(String var0, char var1) {
      String var2 = (new Character(var1)).toString();
      return StringUtils.splitCompletely(var0, var2);
   }

   private static String getAclFromPermDotAcl(String var0) {
      String[] var1 = splitCompletely(var0, '.');
      String var2 = var1[1];

      for(int var3 = 2; var3 < var1.length; ++var3) {
         var2 = var2 + '.' + var1[var3];
      }

      return var2;
   }

   private static String getPermFromPermDotAcl(String var0) {
      String[] var1 = splitCompletely(var0, '.');
      return var1[0];
   }

   public static void main(String[] var0) {
      if (var0.length != 2) {
         System.out.println("Syntax: FileRealm fileRealmPath saltPath");
      } else {
         try {
            String var1 = var0[0];
            String var2 = var0[1];
            Properties var3 = new Properties();
            Properties var4 = new Properties();
            Properties var5 = new Properties();
            String var6 = var1 + ".src";
            FileInputStream var7 = new FileInputStream(var6);

            try {
               Properties var8 = new Properties();
               var8.load(var7);
               Enumeration var9 = var8.keys();

               while(var9.hasMoreElements()) {
                  String var10 = (String)var9.nextElement();
                  String var11 = getUserFromKey(var10);
                  if (var11 != null) {
                     var3.put(var11, var8.get(var10));
                  } else {
                     var11 = getGroupFromKey(var10);
                     if (var11 != null) {
                        var4.put(var11, var8.get(var10));
                     } else {
                        var11 = getPermDotAclFromKey(var10);
                        if (var11 != null) {
                           var5.put(var11, var8.get(var10));
                        }
                     }
                  }
               }
            } finally {
               var7.close();
            }

            convertFromClearTextPasswords(var1, SerializedSystemIni.getSalt(var2), var3, var4, var5);
         } catch (Exception var17) {
            System.out.println("Error : " + var17);
            var17.printStackTrace();
         }

      }
   }

   public void init(String var1, Object var2) throws NotOwnerException {
      this.password = getPassword(var2);
      if (this.aclOwner != null && this.aclOwner != this.getAclOwner(var2)) {
         throw new NotOwnerException();
      } else {
         this.name = var1;
         this.aclOwner = new FileRealmUserImpl("ACL Owner", this.password, this);
      }
   }

   public String getName() {
      return this.name;
   }

   private void checkMaxUsers() {
      if (this.users.size() > this.maxUsers) {
         SecurityLogger.logMaxUserWarning(Integer.toString(this.maxUsers), Integer.toString(this.users.size()));
      }

   }

   private void checkMaxGroups() {
      if (this.groups.size() > this.maxGroups) {
         SecurityLogger.logMaxGroupWarning(Integer.toString(this.maxGroups), Integer.toString(this.groups.size()));
      }

   }

   private void checkMaxAcls() {
      if (this.acls.size() > this.maxACLs) {
         SecurityLogger.logMaxAclWarning(Integer.toString(this.maxACLs), Integer.toString(this.acls.size()));
      }

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
      if (var2 != null) {
         if (!(var2 instanceof DefaultUserImpl) || !(var1 instanceof DefaultUserInfoImpl)) {
            throw new FileRealmException("FileRealm only supports DefaultUserInfoImpls : " + var1.toString());
         }

         DefaultUserImpl var3 = (DefaultUserImpl)var2;
         DefaultUserInfoImpl var4 = (DefaultUserInfoImpl)var1;
         if (var4.hasPassword()) {
            DefaultUserInfoImpl var5 = new DefaultUserInfoImpl(var4.getName(), this.hashPassword(var4.getPassword()), var4.getRealmName());
            var4 = var5;
         }

         if (!var3.hasMatchingInfo(var4)) {
            var2 = null;
         }
      }

      Audit.authenticateUser("Default Realm", var1, var2);
      return var2;
   }

   private User authCertificates(String var1, Vector var2) {
      return CertAuthentication.authenticate(var1, var2, false);
   }

   private User authSSLCertificates(String var1, Vector var2) {
      return CertAuthentication.authenticate(var1, var2, true);
   }

   public Principal getAclOwner(Object var1) {
      return this.aclOwner.hasMatchingInfo(new DefaultUserInfoImpl("ACL Owner", var1, this.name)) ? this.aclOwner : null;
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
      User var4 = this._newUser(var1, this.hashPassword(getPassword(var2)), var3);
      this.checkMaxUsers();
      this.writeFile();
      return var4;
   }

   private static void checkPrincipalName(String var0) {
      if (var0.indexOf(44) != -1) {
         throw new FileRealmException("Principal names in the FileRealm must not contain the , character: " + var0);
      }
   }

   private static void checkPermissionName(String var0) {
      if (var0.indexOf(46) != -1) {
         throw new FileRealmException("Permission names in the FileRealm must not contain the . character: " + var0);
      }
   }

   private User _newUser(String var1, Object var2, Object var3) throws SecurityException {
      checkPrincipalName(var1);
      if (this.getUser(var1) == null && this.getGroup(var1) == null) {
         FileRealmUserImpl var4 = new FileRealmUserImpl(var1, var2, this);
         this.users.put(var1, var4);
         return var4;
      } else {
         throw new SecurityException("Principal " + var1 + " already defined in realm " + this.getName());
      }
   }

   public Group newGroup(String var1) throws SecurityException {
      Group var2 = this._newGroup(var1);
      this.checkMaxGroups();
      this.writeFile();
      return var2;
   }

   private Group _newGroup(String var1) throws SecurityException {
      checkPrincipalName(var1);
      if (this.getGroup(var1) != null) {
         throw new SecurityException("Group " + var1 + " already defined in realm " + this.getName());
      } else {
         FileRealmGroupImpl var2 = new FileRealmGroupImpl(var1);
         this.groups.put(var1, var2);
         return var2;
      }
   }

   public Acl newAcl(Principal var1, String var2) throws SecurityException {
      Acl var3 = this._newAcl(var1, var2);
      this.checkMaxAcls();
      this.writeFile();
      return var3;
   }

   private Acl _newAcl(Principal var1, String var2) throws SecurityException {
      if (this.getAcl(var2) != null) {
         throw new SecurityException("Acl " + var2 + " already defined in realm " + this.getName());
      } else if (this.aclOwner != var1) {
         throw new SecurityException(var1 + " does not own the ACL");
      } else {
         FileRealmAclImpl var3 = new FileRealmAclImpl(var1, var2);
         this.acls.put(var2, var3);
         return var3;
      }
   }

   public Permission newPermission(String var1) throws SecurityException {
      checkPermissionName(var1);
      PermissionImpl var2 = new PermissionImpl(var1);
      this.permissions.put(var1, var2);
      return var2;
   }

   public void deleteUser(User var1) throws SecurityException {
      this._deleteUser(var1);
      this.writeFile();
   }

   private void _deleteUser(User var1) throws SecurityException {
      if (this.getUser(var1.getName()) == null) {
         throw new SecurityException("User " + var1.getName() + " doesn't exist in realm " + this.getName());
      } else {
         this.users.remove(var1.getName());
         this.deletePrincipal(var1);
      }
   }

   public void deleteGroup(Group var1) throws SecurityException {
      this._deleteGroup(var1);
      this.writeFile();
   }

   public void _deleteGroup(Group var1) throws SecurityException {
      if (this.getGroup(var1.getName()) == null) {
         throw new SecurityException("Group " + var1.getName() + " doesn't exist in realm " + this.getName());
      } else {
         this.groups.remove(var1.getName());
         this.deletePrincipal(var1);
      }
   }

   public void deletePermission(Permission var1) throws SecurityException {
      this.permissions.remove(var1 instanceof PermissionImpl ? ((PermissionImpl)var1).getName() : var1.toString());
   }

   private void deletePrincipal(Principal var1) {
      Enumeration var3;
      synchronized(this.groups) {
         var3 = this.getGroups();

         while(true) {
            if (!var3.hasMoreElements()) {
               break;
            }

            Group var4 = (Group)var3.nextElement();
            if (!(var4 instanceof Everyone)) {
               var4.removeMember(var1);
            }
         }
      }

      synchronized(this.acls) {
         var3 = this.getAcls();

         while(true) {
            while(var3.hasMoreElements()) {
               Acl var12 = (Acl)var3.nextElement();
               Enumeration var5 = var12.entries();

               while(var5.hasMoreElements()) {
                  AclEntry var6 = (AclEntry)var5.nextElement();
                  if (var6.getPrincipal().equals(var1)) {
                     try {
                        var12.removeEntry(this.aclOwner, var6);
                     } catch (NotOwnerException var9) {
                     }
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   public void deleteAcl(Principal var1, Acl var2) throws SecurityException {
      this._deleteAcl(var1, var2);
      this.writeFile();
   }

   public void _deleteAcl(Principal var1, Acl var2) throws SecurityException {
      if (this.aclOwner != var1) {
         throw new SecurityException(var1 + " does not own the ACL");
      } else {
         this.acls.remove(var2.getName());
      }
   }

   public void setPermission(Acl var1, Principal var2, Permission var3, boolean var4) {
      weblogic.security.acl.Security.checkPermission("FileRealm", "weblogic.admin.acl", this.getPermission("modify"), '.');

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

   private void createObjectsFromProps(Properties var1) {
      Principal var2 = this.getAclOwner(this.password);
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         String var5 = getUserFromKey(var4);
         String var6;
         if (var5 != null) {
            var6 = (String)var1.get(var4);
            this._newUser(var5, var6, (Object)null);
         } else {
            var5 = getGroupFromKey(var4);
            if (var5 != null) {
               this._newGroup(var5);
            } else {
               var5 = getPermDotAclFromKey(var4);
               if (var5 != null) {
                  var6 = getAclFromPermDotAcl(var5);
                  Acl var7 = this.getAcl(var6);
                  if (var7 == null) {
                     this._newAcl(this.aclOwner, var6);
                  }
               }
            }
         }
      }

   }

   private void ensureRequiredObjectsExist() {
      CachingRealm var1 = null;
      BasicRealm var2 = weblogic.security.acl.Security.getRealm();
      if (var2 instanceof CachingRealm) {
         var1 = (CachingRealm)var2;
      }

      if (this.getPrincipalFromAnyRealm("everyone") == null) {
         this.groups.put(this.everyone.getName(), this.everyone);
         if (var1 != null) {
            var1.clearGroupCaches();
         }
      }

      String var4 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getSystemUser();
      if (this.getPrincipalFromAnyRealm(var4) == null) {
         SecurityLogger.logNonexistentSystemUserWarning(var4);
         this._newUser(var4, this.hashPassword(var4), (Object)null);
         this.addedSystemUser = true;
      }

      if (this.getPrincipalFromAnyRealm("guest") == null) {
         String var3 = "guest";
         if (ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().isGuestDisabled()) {
            MessageDigestUtils.update(this.messageDigest, System.currentTimeMillis());
            var3 = new String(this.messageDigest.digest());
            this.messageDigest.reset();
            if (log.isDebugEnabled()) {
               log.debug("disabling guest access");
            }
         }

         if (log.isDebugEnabled()) {
            log.debug("Guest user does not exist, creating it.");
         }

         this._newUser("guest", this.hashPassword(var3), (Object)null);
         this.addedGuest = true;
      }

      if ((this.addedSystemUser || this.addedGuest) && var1 != null) {
         var1.clearUserCaches();
      }

   }

   private Principal getPrincipalFromAnyRealm(String var1) {
      BasicRealm var2 = Realm.getRealm("weblogic");
      Object var3 = var2.getUser(var1);
      if (var3 == null) {
         var3 = var2.getGroup(var1);
      }

      return (Principal)var3;
   }

   private Acl getAclFromAnyRealm(String var1) {
      BasicRealm var2 = Realm.getRealm("weblogic");
      return var2.getAcl(var1);
   }

   private void loadGroupMembersFromProps(Properties var1) {
      Enumeration var2 = var1.keys();

      while(true) {
         while(true) {
            String var3;
            String var4;
            do {
               if (!var2.hasMoreElements()) {
                  return;
               }

               var3 = (String)var2.nextElement();
               var4 = getGroupFromKey(var3);
            } while(var4 == null);

            Group var5 = this.getGroup(var4);
            if (var5 == null) {
               SecurityLogger.logMissingGroupWarning(var4);
            } else {
               String var6 = (String)var1.get(var3);
               if (var6 != null) {
                  String[] var7 = splitCompletely(var6, ',');

                  for(int var8 = 0; var8 < var7.length; ++var8) {
                     String var9 = var7[var8].trim();
                     Principal var10 = this.getPrincipalFromAnyRealm(var9);
                     if (var10 == null) {
                        SecurityLogger.logNonexistentPrincipalGroupWarning(var9, var5.toString());
                     } else {
                        ((FileRealmGroupImpl)var5)._addMember(var10, false);
                     }
                  }
               }
            }
         }
      }
   }

   private void loadAclGranteesFromProps(Properties var1) {
      Principal var2 = this.getAclOwner(this.password);
      Enumeration var3 = var1.keys();

      while(true) {
         while(true) {
            String var4;
            String var5;
            do {
               if (!var3.hasMoreElements()) {
                  return;
               }

               var4 = (String)var3.nextElement();
               var5 = getPermDotAclFromKey(var4);
            } while(var5 == null);

            String var6 = getPermFromPermDotAcl(var5);
            String var7 = getAclFromPermDotAcl(var5);
            Permission var8 = this.getPermission(var6);
            if (var8 == null) {
               SecurityLogger.logNonexistentPermissionWarning(var6);
            } else {
               Acl var9 = this.getAcl(var7);
               if (var9 == null) {
                  SecurityLogger.logNonexistentAclWarning(var7);
               } else {
                  String var10 = (String)var1.get(var4);
                  String[] var11 = splitCompletely(var10, ',');

                  for(int var12 = 0; var12 < var11.length; ++var12) {
                     this.addPermission(var8, var9, var2, var11[var12], false);
                  }
               }
            }
         }
      }
   }

   public static String getPassword(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 instanceof DefaultUserInfoImpl ? ((DefaultUserInfoImpl)var0).getPassword() : var0.toString();
      }
   }

   private void addPermission(Permission var1, Acl var2, Principal var3, String var4, boolean var5) {
      Principal var6 = this.getPrincipalFromAnyRealm(var4);
      if (var6 == null) {
         SecurityLogger.logNonexistentPrincipalAclWarning(var4, var2.getName());
      } else {
         FileRealmAclImpl var7 = (FileRealmAclImpl)var2;

         try {
            Enumeration var8 = var2.entries();

            AclEntry var9;
            do {
               if (!var8.hasMoreElements()) {
                  var7._addEntry(var3, new AclEntryImpl(var6, var1), var5);
                  return;
               }

               var9 = (AclEntry)var8.nextElement();
            } while(!var9.getPrincipal().equals(var6));

            var7._removeEntry(var3, var9, var5);
            var9.addPermission(var1);
            var7._addEntry(var3, var9, var5);
         } catch (NotOwnerException var10) {
            SecurityLogger.logStackTrace(var10);
            throw new Error("Internal error!");
         }
      }
   }

   private void addDefault(Acl var1, Principal var2, Permission var3) {
      try {
         var1.addEntry(var2, new AclEntryImpl(this.everyone, var3));
      } catch (NotOwnerException var5) {
         SecurityLogger.logStackTrace(var5);
         throw new Error("Internal error!");
      }
   }

   private Acl getNamedAcl(Principal var1, String var2) {
      Acl var3 = this.getAcl(var2);
      if (var3 != null) {
         return var3;
      } else {
         var3 = this._newAcl(var1, var2);
         this.checkMaxAcls();
         return var3;
      }
   }

   private Principal getPrincipal(String var1) {
      Object var2 = this.getGroup(var1);
      if (var2 == null) {
         var2 = this.getUser(var1);
      }

      return (Principal)var2;
   }

   public void load(String var1, Object var2) throws ClassNotFoundException, IOException, NotOwnerException {
      if (var2 != null && getPassword(var2).equals(this.password)) {
         SecurityMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity();
         FileRealmMBean var4 = var3.getRealm().getFileRealm();
         this.maxUsers = var4.getMaxUsers();
         this.maxGroups = var4.getMaxGroups();
         this.maxACLs = var4.getMaxACLs();
         this.salt = var3.getSalt();
         Properties var5 = this.loadFromAdminServer();
         this.createObjectsFromProps(var5);
      } else {
         throw new NotOwnerException();
      }
   }

   public void loadMembers() {
      this.ensureRequiredObjectsExist();
      Properties var1 = this.loadFromAdminServer();
      this.loadGroupMembersFromProps(var1);
      this.loadAclGranteesFromProps(var1);
      this.checkMaxUsers();
      this.checkMaxGroups();
      this.checkMaxAcls();
   }

   public void addRuntimeACLs() {
      try {
         String[] var1 = new String[]{"send", "receive", "browse"};
         this.addDefaultAcl("weblogic.jms", "everyone", var1);
         String[] var2 = new String[]{"lookup", "list", "modify"};
         this.addDefaultAcl("weblogic.jndi", "everyone", var2);
         String[] var3 = new String[]{"admin", "modify", "shrink", "reset"};
         String[] var4 = new String[]{"reserve"};
         String[] var5 = new String[]{"Administrators", "Deployers"};
         this.addDefaultAcl("weblogic.jdbc", "everyone", var4);
         this.addDefaultAcl("weblogic.jdbc", var5, var3);
      } catch (NotOwnerException var6) {
         throw new SecurityException("Default ACL - " + var6.toString());
      }
   }

   private void addDefaultAcl(String var1, String var2, String[] var3) throws NotOwnerException {
      Acl var4 = this.getAclFromAnyRealm(var1);
      if (var4 != null) {
         if (log.isDebugEnabled()) {
            log.debug("Default ACL - " + var1 + " already exists");
         }

      } else {
         Principal var5 = this.getPrincipalFromAnyRealm(var2);
         if (var5 == null) {
            throw new SecurityException("Default ACL - Principal " + var2 + " not found");
         } else {
            AclEntryImpl var6 = new AclEntryImpl(var5);

            for(int var7 = 0; var7 < var3.length; ++var7) {
               var6.addPermission(this.getPermission(var3[var7]));
            }

            FileRealmAclImpl var8 = new FileRealmAclImpl(this.aclOwner, var1);
            var8._addEntry(this.aclOwner, var6, false);
            if (log.isDebugEnabled()) {
               log.debug("Default " + var8.toString());
            }

            this.acls.put(var1, var8);
         }
      }
   }

   private void addDefaultAcl(String var1, String[] var2, String[] var3) throws NotOwnerException {
      if (var2 != null && var2.length > 0) {
         String var4 = null;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            try {
               var4 = var2[var5];
               Principal var6 = this.getPrincipalFromAnyRealm(var4);
               if (var6 != null) {
                  this.addDefaultAcl(var1, var4, var3);
               }
            } catch (SecurityException var7) {
               throw var7;
            }
         }
      }

   }

   public void save(String var1) throws IOException {
   }

   public synchronized void newUserAcl(String var1, char var2, Permission[] var3) throws SecurityException {
      try {
         if (this.getAcl(var1) != null) {
            return;
         }

         Acl var4 = this.getAcl(var1, var2);
         Principal var5 = this.getAclOwner(this.password);
         Acl var6 = this.newAcl(var5, var1);
         User var7 = weblogic.security.acl.Security.getCurrentUser();
         boolean var8 = false;
         AclEntry var10;
         if (var4 != null) {
            for(Enumeration var9 = var4.entries(); var9.hasMoreElements(); var6.addEntry(var5, var10)) {
               var10 = (AclEntry)var9.nextElement();
               if (var10.getPrincipal().equals(var7) && !var10.isNegative()) {
                  var8 = true;

                  for(int var11 = 0; var11 < var3.length; ++var11) {
                     var10.addPermission(var3[var11]);
                  }
               }
            }
         }

         if (!var8) {
            var6.addEntry(var5, new AclEntryImpl(var7, var3));
         }
      } catch (NotOwnerException var12) {
      } catch (Throwable var13) {
         SecurityLogger.logStackTrace(var13);
      }

   }

   private Properties loadFromAdminServer() {
      Properties var1 = new Properties();
      InputStream var2 = this.getInputStream();

      try {
         var1.load(var2);
      } catch (IOException var12) {
         throw new FileRealmException("Unable to load properties", var12);
      } finally {
         try {
            var2.close();
         } catch (IOException var11) {
            throw new FileRealmException("Unable to close stream", var11);
         }
      }

      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         String var5 = (String)var1.get(var4);
         var5 = var5.trim();
         var1.put(var4, var5);
      }

      if (log.isDebugEnabled()) {
         log.debug("Completed load of properties");
      }

      return var1;
   }

   private InputStream getInputStream() throws FileRealmException {
      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer() && ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable()) {
         URL var1 = null;
         Object var2 = null;

         try {
            var1 = FileDistributionServlet.getURL();
         } catch (MalformedURLException var6) {
            throw new FileRealmException("Unable to build properties url", var6);
         }

         try {
            URLConnection var3 = var1.openConnection();
            HttpURLConnection var4 = (HttpURLConnection)var3;
            ConnectionSigner.signConnection(var3, kernelId);
            var4.setRequestProperty("wl_request_type", "wl_file_realm_request");
            var4.setRequestProperty("Connection", "Close");
            return var4.getInputStream();
         } catch (IOException var5) {
            throw new FileRealmException("Unable to open url: " + var1.toString(), var5);
         }
      } else {
         try {
            if (log.isDebugEnabled()) {
               log.debug("reading from " + getPath());
            }

            return new FileInputStream(new File(getPath()));
         } catch (FileNotFoundException var7) {
            throw new FileRealmException("Source file not found: " + getPath(), var7);
         }
      }
   }

   public static String getPath() {
      return DomainDir.getPathRelativeRootDir("fileRealm.properties");
   }

   private static String saltPassword(byte[] var0, String var1) {
      byte[] var2 = var1.getBytes();
      int var3 = Math.max(var0.length, var2.length);
      byte[] var4 = new byte[var3];

      int var5;
      for(var5 = 0; var5 < var2.length; ++var5) {
         var4[var5] += var2[var5];
      }

      for(var5 = 0; var5 < var0.length; ++var5) {
         var4[var5] += var0[var5];
      }

      return Hex.asHex(var4);
   }

   private static String hashPassword(MessageDigest var0, byte[] var1, String var2) {
      MessageDigestUtils.updateASCII(var0, saltPassword(var1, var2));
      byte[] var3 = var0.digest();
      var0.reset();
      return Hex.asHex(var3);
   }

   private synchronized String hashPassword(String var1) {
      return hashPassword(this.messageDigest, this.salt, var1);
   }

   private String getMemberName(Object var1) {
      if (var1 instanceof User) {
         return ((User)var1).getName();
      } else if (var1 instanceof Group) {
         return ((Group)var1).getName();
      } else {
         throw new FileRealmException("member not a User or Group : " + var1.toString());
      }
   }

   private void writeUsersToProps(Properties var1) {
      String var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getSystemUser();
      String var3 = "guest";
      Enumeration var4 = this.getUsers();

      while(true) {
         User var5;
         do {
            do {
               if (!var4.hasMoreElements()) {
                  return;
               }

               var5 = (User)var4.nextElement();
            } while(this.addedSystemUser && var5.getName().equals(var2));
         } while(this.addedGuest && var5.getName().equals(var3));

         DefaultUserImpl var6 = (DefaultUserImpl)var5;
         Object var7 = var6.getCredential(this.password);
         String var8 = (String)var7;
         String var9 = "user." + var5.getName();
         var1.put(var9, var8);
      }
   }

   private void writeGroupsToProps(Properties var1) {
      Enumeration var2 = this.getGroups();

      while(true) {
         Group var3;
         do {
            if (!var2.hasMoreElements()) {
               return;
            }

            var3 = (Group)var2.nextElement();
         } while(var3 instanceof Everyone);

         String var4 = "";
         Enumeration var5 = var3.members();

         Object var7;
         for(boolean var6 = true; var5.hasMoreElements(); var4 = var4 + this.getMemberName(var7)) {
            if (!var6) {
               var4 = var4 + ',';
            } else {
               var6 = false;
            }

            var7 = var5.nextElement();
         }

         String var9 = "group." + var3.getName();
         var1.put(var9, var4);
      }
   }

   private void writeAclsToProps(Properties var1) {
      Enumeration var2 = this.getAcls();

      while(var2.hasMoreElements()) {
         Acl var3 = (Acl)var2.nextElement();
         Hashtable var4 = new Hashtable();
         Enumeration var5 = var3.entries();

         String var8;
         while(var5.hasMoreElements()) {
            AclEntry var6 = (AclEntry)var5.nextElement();
            Principal var7 = var6.getPrincipal();
            var8 = this.getMemberName(var6.getPrincipal());

            Vector var12;
            for(Enumeration var9 = var6.permissions(); var9.hasMoreElements(); var12.add(var8)) {
               PermissionImpl var10 = (PermissionImpl)var9.nextElement();
               String var11 = var10.getName();
               var12 = (Vector)var4.get(var11);
               if (var12 == null) {
                  var12 = new Vector();
                  var4.put(var11, var12);
               }
            }
         }

         Enumeration var14 = var4.keys();

         while(var14.hasMoreElements()) {
            String var15 = (String)var14.nextElement();
            var8 = "";
            Vector var16 = (Vector)var4.get(var15);
            Enumeration var17 = var16.elements();

            String var19;
            for(boolean var18 = true; var17.hasMoreElements(); var8 = var8 + var19) {
               if (!var18) {
                  var8 = var8 + ',';
               } else {
                  var18 = false;
               }

               var19 = (String)var17.nextElement();
            }

            var19 = "acl." + var15 + '.' + var3.getName();
            var1.put(var19, var8);
         }
      }

   }

   private static void writeFile(String var0, Properties var1) {
      FileRealmFileWriter var2 = new FileRealmFileWriter(var1);

      try {
         FileUtils.replace(var0, var2);
      } catch (FileUtilsException var4) {
         throw new FileRealmException("Error rewriting " + var0, var4);
      }
   }

   private synchronized void writeFile() {
      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         SecurityLogger.logInMemoryFileRealmChangeWarning();
      } else {
         Properties var1 = new Properties();
         this.writeUsersToProps(var1);
         this.writeGroupsToProps(var1);
         this.writeAclsToProps(var1);
         writeFile(getPath(), var1);
      }
   }

   public void refresh() {
      Hashtable var1 = this.users;
      Hashtable var2 = this.groups;
      Hashtable var3 = this.acls;
      this.users = new Hashtable();
      this.groups = new Hashtable();
      this.acls = new Hashtable();

      try {
         Properties var4 = this.loadFromAdminServer();
         this.createObjectsFromProps(var4);
         this.ensureRequiredObjectsExist();
         this.loadGroupMembersFromProps(var4);
         this.loadAclGranteesFromProps(var4);
         this.checkMaxUsers();
         this.checkMaxGroups();
         this.checkMaxAcls();
      } catch (Throwable var5) {
         this.users = var1;
         this.groups = var2;
         this.acls = var3;
         throw new FileRealmException("FileRealm couldn't synchronize - using old values", var5);
      }
   }

   private class FileRealmUserImpl extends DefaultUserImpl implements CredentialChanger {
      private static final long serialVersionUID = -5265009210019953032L;

      public FileRealmUserImpl(String var2, Object var3, BasicRealm var4) {
         super(var2, var3, var4);
      }

      public void changeCredential(Object var1, Object var2) throws SecurityException {
         DefaultUserInfoImpl var3 = new DefaultUserInfoImpl(this.getName(), var1, this.getRealm().getName());
         Realm.authenticate(var3);
         this.setCredential(FileRealm.this.hashPassword(FileRealm.getPassword(var2)));
         FileRealm.this.writeFile();
      }
   }

   private class FileRealmGroupImpl extends GroupImpl {
      private static final long serialVersionUID = -3323170289877387624L;

      public FileRealmGroupImpl(String var2) {
         super(var2);
      }

      public boolean addMember(Principal var1) {
         return this._addMember(var1, true);
      }

      public boolean _addMember(Principal var1, boolean var2) {
         boolean var3 = super.addMember(var1);
         if (var3 && var2) {
            FileRealm.this.writeFile();
         }

         return var3;
      }

      public boolean removeMember(Principal var1) {
         return this._removeMember(var1, true);
      }

      public boolean _removeMember(Principal var1, boolean var2) {
         boolean var3 = super.removeMember(var1);
         if (var3 && var2) {
            FileRealm.this.writeFile();
         }

         return var3;
      }
   }

   private class FileRealmAclImpl extends AclImpl {
      private static final long serialVersionUID = -6543322023693167749L;

      public FileRealmAclImpl(Principal var2, String var3) {
         super(var2, var3);
      }

      public boolean addEntry(Principal var1, AclEntry var2) throws NotOwnerException {
         return this._addEntry(var1, var2, true);
      }

      public boolean _addEntry(Principal var1, AclEntry var2, boolean var3) throws NotOwnerException {
         boolean var4 = super.addEntry(var1, var2);
         if (var4 && var3) {
            FileRealm.this.writeFile();
         }

         return var4;
      }

      public boolean removeEntry(Principal var1, AclEntry var2) throws NotOwnerException {
         return this._removeEntry(var1, var2, true);
      }

      public boolean _removeEntry(Principal var1, AclEntry var2, boolean var3) throws NotOwnerException {
         boolean var4 = super.removeEntry(var1, var2);
         if (var4 && var3) {
            FileRealm.this.writeFile();
         }

         return var4;
      }
   }
}
