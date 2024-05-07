package weblogic.security.acl;

import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Locale;
import java.util.NoSuchElementException;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.CachingRealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.enumerations.EnumerationUtils;

/** @deprecated */
public final class CachingRealm implements ManageableRealm, DebuggableRealm, RefreshableRealm {
   private static final long serialVersionUID = 4045950267181139320L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String realmName;
   protected boolean caseSensitive;
   protected TTLCache aclPosCache;
   protected TTLCache aclNegCache;
   protected final Object aclSync;
   protected TTLCache groupPosCache;
   protected TTLCache groupNegCache;
   protected final Object groupSync;
   protected TTLCache permPosCache;
   protected TTLCache permNegCache;
   protected final Object permSync;
   protected TTLCache userPosCache;
   protected TTLCache userNegCache;
   protected TTLCache authPosCache;
   protected TTLCache authNegCache;
   protected final Object userSync;
   protected LogOutputStream log;
   private ListableRealm delegate;
   private ManageableRealm manageable;
   private ManageableRealm backup;

   public CachingRealm(ListableRealm var1) {
      this(var1, (ManageableRealm)null, (Object)null);
   }

   public CachingRealm(ListableRealm var1, ManageableRealm var2, Object var3) {
      this(ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm(), var1, var2, var3);
   }

   private CachingRealm(CachingRealmMBean var1, ListableRealm var2, ManageableRealm var3, Object var4) {
      this(var2, var3, var1.getCacheCaseSensitive(), var1.getACLCacheEnable(), var1.getACLCacheSize(), var1.getACLCacheTTLPositive(), var1.getACLCacheTTLNegative(), var1.getAuthenticationCacheEnable(), var1.getAuthenticationCacheSize(), var1.getAuthenticationCacheTTLPositive(), var1.getAuthenticationCacheTTLNegative(), var1.getGroupCacheEnable(), var1.getGroupCacheSize(), var1.getGroupCacheTTLPositive(), var1.getGroupCacheTTLNegative(), var1.getPermissionCacheEnable(), var1.getPermissionCacheSize(), var1.getPermissionCacheTTLPositive(), var1.getPermissionCacheTTLNegative(), var1.getUserCacheEnable(), var1.getUserCacheSize(), var1.getUserCacheTTLPositive(), var1.getUserCacheTTLNegative(), ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getDebugSecurityRealm(), var4);
   }

   private CachingRealm(ListableRealm var1, ManageableRealm var2, boolean var3, boolean var4, int var5, int var6, int var7, boolean var8, int var9, int var10, int var11, boolean var12, int var13, int var14, int var15, boolean var16, int var17, int var18, int var19, boolean var20, int var21, int var22, int var23, boolean var24, Object var25) {
      this.realmName = "caching";
      this.caseSensitive = true;
      this.aclSync = new Object();
      this.groupSync = new Object();
      this.permSync = new Object();
      this.userSync = new Object();
      this.delegate = var1;
      this.backup = var2;
      this.setDebug(var24);
      if (!var24 && Boolean.getBoolean("weblogic.security.cachingrealm.verbose")) {
         this.log = new LogOutputStream("CachingRealm");
      }

      if (var1 instanceof ManageableRealm) {
         this.manageable = (ManageableRealm)var1;
      }

      this.caseSensitive = var3;
      DisabledCache var26 = new DisabledCache();
      if (var4) {
         if (this.log != null) {
            this.log.debug("acl size = " + var5 + ", pos ttl =" + var6 + ", neg ttl = " + var7);
         }

         this.aclPosCache = new TTLCache(var5, (long)var6 * 1000L);
         this.aclNegCache = new TTLCache(var5, (long)var7 * 1000L);
      } else {
         if (this.log != null) {
            this.log.debug("acl cache disabled");
         }

         this.aclPosCache = var26;
         this.aclNegCache = var26;
      }

      if (var8) {
         if (this.log != null) {
            this.log.debug("auth size = " + var9 + ", pos ttl = " + var10 + ", neg ttl = " + var11);
         }

         this.authPosCache = new TTLCache(var9, (long)var10 * 1000L);
         this.authNegCache = new TTLCache(var9, (long)var11 * 1000L);
      } else {
         if (this.log != null) {
            this.log.debug("auth cache disabled");
         }

         this.authPosCache = var26;
         this.authNegCache = var26;
      }

      if (var12) {
         if (this.log != null) {
            this.log.debug("group size = " + var13 + ", pos ttl = " + var14 + ", neg ttl = " + var15);
         }

         this.groupPosCache = new TTLCache(var13, (long)var14 * 1000L);
         this.groupNegCache = new TTLCache(var13, (long)var15 * 1000L);
      } else {
         if (this.log != null) {
            this.log.debug("group cache disabled");
         }

         this.groupPosCache = var26;
         this.groupNegCache = var26;
      }

      if (var16) {
         if (this.log != null) {
            this.log.debug("perm size = " + var17 + ", pos ttl = " + var18 + ", neg ttl = " + var19);
         }

         this.permPosCache = new TTLCache(var17, (long)var18 * 1000L);
         this.permNegCache = new TTLCache(var17, (long)var19 * 1000L);
      } else {
         if (this.log != null) {
            this.log.debug("perm cache disabled");
         }

         this.permPosCache = var26;
         this.permNegCache = var26;
      }

      if (var20) {
         if (this.log != null) {
            this.log.debug("user size = " + var21 + ", pos ttl = " + var22 + ", neg ttl = " + var23);
         }

         this.userPosCache = new TTLCache(var21, (long)var22 * 1000L);
         this.userNegCache = new TTLCache(var21, (long)var23 * 1000L);
      } else {
         if (this.log != null) {
            this.log.debug("user cache disabled");
         }

         this.userPosCache = var26;
         this.userNegCache = var26;
      }

   }

   private void addPermission(Acl var1, Permission var2, Principal var3, String var4) {
      Principal var5 = this.getPrincipal(var4);
      if (!this.caseSensitive) {
         var4 = var4.toLowerCase(Locale.ENGLISH);
      }

      if (var5 == null) {
         SecurityLogger.logNonexistentPrincipalWarning(var1.getName(), var4);
         String var11 = "ACL \"" + var1.getName() + "\" contains non-existent principal \"" + var4 + "\" - ignoring principal";
         System.err.println("******** Error: " + var11);
      } else {
         Enumeration var6 = var1.entries();

         try {
            AclEntry var7;
            String var9;
            do {
               if (!var6.hasMoreElements()) {
                  var1.addEntry(var3, new AclEntryImpl(var5, var2));
                  return;
               }

               var7 = (AclEntry)var6.nextElement();
               Principal var8 = var7.getPrincipal();
               var9 = var8.getName();
               if (!this.caseSensitive) {
                  var9 = var9.toLowerCase(Locale.ENGLISH);
               }
            } while(!var9.equals(var4));

            var1.removeEntry(var3, var7);
            var7.addPermission(var2);
            var1.addEntry(var3, var7);
         } catch (NotOwnerException var10) {
            throw new NestedRuntimeException("realm setup failed", var10);
         }
      }
   }

   public BasicRealm masqueradeAs(String var1) {
      BasicRealm var2 = (BasicRealm)Realm.realms.put(var1, this);
      this.realmName = var1;
      return var2;
   }

   public void init(String var1, Object var2) throws NotOwnerException {
      if (this.log != null) {
         this.log.debug("init(" + var1 + ", ***)");
      }

      this.delegate.init(var1, var2);
      if (this.backup != null) {
         this.backup.init(var1, var2);
      }

   }

   public String getName() {
      return this.realmName;
   }

   public User getUser(String var1) {
      if (this.log != null) {
         this.log.debug("getUser(\"" + var1 + "\")");
      }

      UserEntry var2 = null;
      return (var2 = this.getUserEntry(var1)) != null ? (User)var2.getValue() : null;
   }

   public Principal lookupPrincipal(String var1) {
      if (this.log != null) {
         this.log.debug("lookupPrincipal(\"" + var1 + "\")");
      }

      Object var2 = this.lookupGroup(var1);
      if (var2 == null) {
         var2 = this.lookupUser(var1);
      }

      return (Principal)var2;
   }

   public Principal getPrincipal(String var1) {
      if (this.log != null) {
         this.log.debug("getPrincipal(\"" + var1 + "\")");
      }

      Object var2 = this.getGroup(var1);
      if (var2 == null) {
         var2 = this.getUser(var1);
      }

      return (Principal)var2;
   }

   public User lookupUser(String var1) {
      if (this.log != null) {
         this.log.debug("lookupUser(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      User var3 = null;
      synchronized(this.userSync) {
         Entry var5;
         if ((var5 = (Entry)this.userPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("user: pos HIT " + var1);
            }

            var3 = (User)var5.getValue();
         }

         return var3;
      }
   }

   private UserEntry getUserEntry(String var1) {
      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      UserEntry var3 = null;
      synchronized(this.userSync) {
         if ((var3 = (UserEntry)this.userPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("user: pos HIT " + var1);
            }

            return var3;
         }

         if (this.userNegCache.containsKey(var2)) {
            if (this.log != null) {
               this.log.debug("user: neg HIT " + var1);
            }

            return null;
         }
      }

      User var4 = null;

      try {
         var4 = this.delegate.getUser(var1);
      } catch (UnsupportedOperationException var12) {
         if (this.backup == null) {
            throw var12;
         }
      }

      if (var4 != null) {
         if (this.log != null) {
            this.log.debug("user: delegate HAS " + var1);
         }

         var3 = new UserEntry(var4, this.delegate);
         synchronized(this.userSync) {
            this.userPosCache.put(var2, var3);
            this.userNegCache.remove(var2);
         }
      } else if (this.backup != null && (var4 = this.backup.getUser(var1)) != null) {
         if (this.log != null) {
            this.log.debug("user: backup HAS " + var1);
         }

         var3 = new UserEntry(var4, this.backup);
         synchronized(this.userSync) {
            this.userPosCache.put(var2, var3);
            this.userNegCache.remove(var2);
         }
      } else {
         if (this.log != null) {
            this.log.debug("user: UNPERSON " + var1);
         }

         synchronized(this.userSync) {
            this.userNegCache.put(var2);
            this.userPosCache.remove(var2);
         }
      }

      return var3;
   }

   public User getUser(UserInfo var1) {
      return this.authenticate(var1);
   }

   public User authenticate(UserInfo var1) {
      String var2 = var1.getName();
      UserEntry var3 = null;
      Object var4 = null;
      String var5 = null;
      if (var2 != null) {
         if (this.caseSensitive) {
            var5 = var2.toLowerCase(Locale.ENGLISH);
            var4 = new CaseInsensitiveUserInfo(var1);
         } else {
            var4 = var1;
         }

         var3 = this.getUserEntry(var2);
      } else {
         var4 = var1;
      }

      if (this.log != null) {
         this.log.debug("authenticate(\"" + var2 + "\")");
      }

      Entry var6 = null;
      synchronized(this.userSync) {
         if ((var6 = (Entry)this.authPosCache.get(var4)) != null) {
            if (this.log != null) {
               this.log.debug("auth: pos HIT " + var2);
            }

            User var8 = (User)var6.getValue();
            if (var3 == null) {
               var3 = this.getUserEntry(var8.getName());
            }

            if (var3 != null) {
               var3.setInfo(var1);
            }

            return var8;
         }

         if (this.authNegCache.containsKey(var4)) {
            if (this.log != null) {
               this.log.debug("auth: neg HIT " + var1.getName());
            }

            return null;
         }
      }

      User var7 = null;

      try {
         var7 = this.delegate.getUser(var1);
      } catch (UnsupportedOperationException var16) {
         if (this.backup == null) {
            throw var16;
         }
      }

      if (var7 != null) {
         if (this.log != null) {
            this.log.debug("auth: delegate PASSES " + var2);
         }

         synchronized(this.userSync) {
            this.authPosCache.put(var4, new Entry(var7, this.delegate));
            this.authNegCache.remove(var4);
         }

         if (var3 == null) {
            var3 = this.getUserEntry(var7.getName());
         }

         if (var3 != null) {
            var3.setInfo(var1);
         }
      } else if (this.backup != null && (var7 = this.backup.getUser(var1)) != null) {
         if (this.log != null) {
            this.log.debug("auth: backup PASSES " + var2);
         }

         synchronized(this.userSync) {
            this.authPosCache.put(var4, new Entry(var7, this.backup));
            this.authNegCache.remove(var4);
         }

         if (var3 == null) {
            var3 = this.getUserEntry(var7.getName());
         }

         if (var3 != null) {
            var3.setInfo(var1);
         }
      } else {
         if (this.log != null) {
            this.log.debug("auth: FAIL " + var2);
         }

         synchronized(this.userSync) {
            this.authNegCache.put(var4);
            this.authPosCache.remove(var4);
         }
      }

      return var7;
   }

   public Principal getAclOwner(Object var1) {
      if (this.log != null) {
         this.log.debug("getAclOwner(\"" + var1 + "\")");
      }

      Principal var2 = null;

      try {
         var2 = this.delegate.getAclOwner(var1);
      } catch (UnsupportedOperationException var4) {
         if (this.backup == null) {
            throw var4;
         }
      }

      if (var2 == null && this.backup != null) {
         var2 = this.backup.getAclOwner(var1);
      }

      return var2;
   }

   public Group lookupGroup(String var1) {
      if (this.log != null) {
         this.log.debug("lookupGroup(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Group var3 = null;
      synchronized(this.groupSync) {
         Entry var5;
         if ((var5 = (Entry)this.groupPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("group: pos HIT " + var1);
            }

            var3 = (Group)var5.getValue();
         }

         return var3;
      }
   }

   public Group getGroup(String var1) {
      if (this.log != null) {
         this.log.debug("getGroup(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Entry var3 = null;
      synchronized(this.groupSync) {
         if ((var3 = (Entry)this.groupPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("group: pos HIT " + var1);
            }

            return (Group)var3.getValue();
         }

         if (this.groupNegCache.containsKey(var2)) {
            if (this.log != null) {
               this.log.debug("group: neg HIT " + var1);
            }

            return null;
         }
      }

      Group var4 = null;

      try {
         var4 = this.delegate.getGroup(var1);
      } catch (UnsupportedOperationException var12) {
         if (this.backup == null) {
            throw var12;
         }
      }

      if (var4 != null) {
         if (this.log != null) {
            this.log.debug("group: delegate HAS " + var1);
         }

         var3 = new Entry(var4, this.delegate);
         synchronized(this.groupSync) {
            this.groupPosCache.put(var2, var3);
            this.groupNegCache.remove(var2);
         }
      } else if (this.backup != null && (var4 = this.backup.getGroup(var1)) != null) {
         if (this.log != null) {
            this.log.debug("group: backup HAS " + var1);
         }

         var3 = new Entry(var4, this.backup);
         synchronized(this.groupSync) {
            this.groupPosCache.put(var2, var3);
            this.groupNegCache.remove(var2);
         }
      } else {
         if (this.log != null) {
            this.log.debug("group: UNGROUP " + var1);
         }

         synchronized(this.groupSync) {
            this.groupNegCache.put(var2);
            this.groupPosCache.remove(var2);
         }
      }

      return var4;
   }

   public Acl lookupAcl(String var1) {
      if (this.log != null) {
         this.log.debug("lookupAcl(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Acl var3 = null;
      synchronized(this.aclSync) {
         Entry var5;
         if ((var5 = (Entry)this.aclPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("acl: pos HIT " + var1);
            }

            var3 = (Acl)var5.getValue();
         }

         return var3;
      }
   }

   public Acl getAcl(String var1) {
      if (this.log != null) {
         this.log.debug("getAcl(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Entry var3 = null;
      synchronized(this.aclSync) {
         if ((var3 = (Entry)this.aclPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("acl: pos HIT " + var1);
            }

            return (Acl)var3.getValue();
         }

         if (this.aclNegCache.containsKey(var2)) {
            if (this.log != null) {
               this.log.debug("acl: neg HIT " + var1);
            }

            return null;
         }
      }

      Acl var4 = null;

      try {
         var4 = this.delegate.getAcl(var1);
      } catch (UnsupportedOperationException var12) {
         if (this.backup == null) {
            throw var12;
         }
      }

      if (var4 != null) {
         if (this.log != null) {
            this.log.debug("acl: delegate HAS " + var1);
         }

         var3 = new Entry(var4, this.delegate);
         synchronized(this.aclSync) {
            this.aclPosCache.put(var2, var3);
            this.aclNegCache.remove(var2);
         }
      } else if (this.backup != null && (var4 = this.backup.getAcl(var1)) != null) {
         if (this.log != null) {
            this.log.debug("acl: backup HAS " + var1);
         }

         var3 = new Entry(var4, this.backup);
         synchronized(this.aclSync) {
            this.aclPosCache.put(var2, var3);
            this.aclNegCache.remove(var2);
         }
      } else {
         if (this.log != null) {
            this.log.debug("acl: UNACL " + var1);
         }

         synchronized(this.aclSync) {
            this.aclNegCache.put(var2);
            this.aclPosCache.remove(var2);
         }
      }

      return var4;
   }

   public Acl lookupAcl(String var1, char var2) {
      if (this.log != null) {
         this.log.debug("lookupAcl(\"" + var1 + "\", '" + var2 + "')");
      }

      Acl var3 = this.lookupAcl(var1);

      for(int var4 = var1.lastIndexOf(var2); var3 == null && var4 >= 0; var4 = var1.lastIndexOf(var2, var4 - 1)) {
         var1 = var1.substring(0, var4);
         var3 = this.lookupAcl(var1);
      }

      return var3;
   }

   public Acl getAcl(String var1, char var2) {
      if (this.log != null) {
         this.log.debug("getAcl(\"" + var1 + "\", '" + var2 + "')");
      }

      Acl var3 = this.getAcl(var1);

      for(int var4 = var1.lastIndexOf(var2); var3 == null && var4 >= 0; var4 = var1.lastIndexOf(var2, var4 - 1)) {
         var1 = var1.substring(0, var4);
         var3 = this.getAcl(var1);
      }

      return var3;
   }

   public Permission lookupPermission(String var1) {
      if (this.log != null) {
         this.log.debug("lookupPermission(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Permission var3 = null;
      synchronized(this.permSync) {
         Entry var5;
         if ((var5 = (Entry)this.permPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("perm: pos HIT " + var1);
            }

            var3 = (Permission)var5.getValue();
         }

         return var3;
      }
   }

   public Permission getPermission(String var1) {
      if (this.log != null) {
         this.log.debug("getPermission(\"" + var1 + "\")");
      }

      String var2 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
      Permission var3 = null;
      Entry var4 = null;
      synchronized(this.permSync) {
         if ((var4 = (Entry)this.permPosCache.get(var2)) != null) {
            if (this.log != null) {
               this.log.debug("perm: pos HIT " + var1);
            }

            return (Permission)var4.getValue();
         }

         if (this.permNegCache.containsKey(var2)) {
            if (this.log != null) {
               this.log.debug("perm: pos HIT " + var1);
            }

            return null;
         }
      }

      try {
         var3 = this.delegate.getPermission(var1);
      } catch (UnsupportedOperationException var13) {
         if (this.backup == null) {
            throw var13;
         }
      }

      if (var3 != null) {
         if (this.log != null) {
            this.log.debug("perm: delegate HAS " + var1);
         }

         var4 = new Entry(var3, this.delegate);
         synchronized(this.permSync) {
            this.permPosCache.put(var2, var4);
            this.permNegCache.remove(var2);
         }
      } else if (this.backup != null && (var3 = this.backup.getPermission(var1)) != null) {
         if (this.log != null) {
            this.log.debug("perm: backup HAS " + var1);
         }

         var4 = new Entry(var3, this.backup);
         synchronized(this.permSync) {
            this.permPosCache.put(var2, var4);
            this.permNegCache.remove(var2);
         }
      } else {
         if (this.log != null) {
            this.log.debug("perm: UNPERM " + var1);
         }

         synchronized(this.permSync) {
            this.permNegCache.put(var2);
            this.permPosCache.remove(var2);
         }
      }

      return var3;
   }

   public void load(String var1, Object var2) throws ClassNotFoundException, IOException, NotOwnerException {
      if (this.log != null) {
         this.log.debug("load(\"" + var1 + "\", " + var2 + ")");
      }

      this.delegate.load(var1, var2);
      if (this.backup != null) {
         this.backup.load(var1, var2);
      }

   }

   public void save(String var1) throws IOException {
      if (this.log != null) {
         this.log.debug("save(\"" + var1 + "\")");
      }

      this.delegate.save(var1);
      if (this.backup != null) {
         this.backup.save(var1);
      }

   }

   public User newUser(String var1, Object var2, Object var3) throws SecurityException {
      if (this.log != null) {
         this.log.debug("newUser(\"" + var1 + "\", " + var2 + ", " + var3 + ")");
      }

      User var4 = null;
      if (this.manageable != null) {
         try {
            var4 = this.manageable.newUser(var1, var2, var3);
         } catch (UnsupportedOperationException var9) {
            if (this.backup == null) {
               throw var9;
            }

            var4 = this.backup.newUser(var1, var2, var3);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("newUser not supported by delegate");
         }

         var4 = this.backup.newUser(var1, var2, var3);
      }

      if (var4 != null) {
         String var5 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
         synchronized(this.userSync) {
            this.userNegCache.remove(var5);
         }
      }

      return var4;
   }

   public Group newGroup(String var1) throws SecurityException {
      if (this.log != null) {
         this.log.debug("newGroup(\"" + var1 + "\")");
      }

      Group var2 = null;
      if (this.manageable != null) {
         try {
            var2 = this.manageable.newGroup(var1);
         } catch (UnsupportedOperationException var7) {
            if (this.backup == null) {
               throw var7;
            }

            var2 = this.backup.newGroup(var1);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("newGroup not supported by delegate");
         }

         var2 = this.backup.newGroup(var1);
      }

      if (var2 != null) {
         String var3 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
         synchronized(this.groupSync) {
            this.groupNegCache.remove(var3);
         }
      }

      return var2;
   }

   public Acl newAcl(Principal var1, String var2) throws SecurityException {
      if (this.log != null) {
         this.log.debug("newAcl(\"" + var1.getName() + "\", \"" + var2 + "\")");
      }

      Acl var3 = null;
      if (this.manageable != null) {
         try {
            var3 = this.manageable.newAcl(var1, var2);
         } catch (UnsupportedOperationException var8) {
            if (this.backup == null) {
               throw var8;
            }

            var3 = this.backup.newAcl(var1, var2);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("newAcl not supported by delegate");
         }

         var3 = this.backup.newAcl(var1, var2);
      }

      if (var3 != null) {
         String var4 = this.caseSensitive ? var2 : var2.toLowerCase(Locale.ENGLISH);
         synchronized(this.aclSync) {
            this.aclNegCache.remove(var4);
         }
      }

      return var3;
   }

   public Permission newPermission(String var1) throws SecurityException {
      if (this.log != null) {
         this.log.debug("newAcl(\"" + var1 + "\")");
      }

      Permission var2 = null;
      if (this.manageable != null) {
         try {
            var2 = this.manageable.newPermission(var1);
         } catch (UnsupportedOperationException var7) {
            if (this.backup == null) {
               throw var7;
            }

            var2 = this.backup.newPermission(var1);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("newPermission not supported by delegate");
         }

         var2 = this.backup.newPermission(var1);
      }

      if (var2 != null) {
         String var3 = this.caseSensitive ? var1 : var1.toLowerCase(Locale.ENGLISH);
         synchronized(this.permSync) {
            this.permNegCache.remove(var3);
         }
      }

      return var2;
   }

   public void deleteUser(User var1) throws SecurityException {
      if (this.log != null) {
         this.log.debug("deleteUser(\"" + var1.getName() + "\")");
      }

      String var2 = var1.getName();
      if (this.manageable != null) {
         try {
            this.manageable.deleteUser(var1);
         } catch (UnsupportedOperationException var8) {
            if (this.backup == null) {
               throw var8;
            }

            this.backup.deleteUser(var1);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("deleteUser not supported by delegate");
         }

         this.backup.deleteUser(var1);
      }

      String var3 = this.caseSensitive ? var2 : var2.toLowerCase(Locale.ENGLISH);
      synchronized(this.userSync) {
         UserEntry var5 = (UserEntry)this.userPosCache.get(var3);
         if (var5 != null) {
            this.userPosCache.remove(var3);
            if (var5.getInfo() != null) {
               this.authPosCache.remove(var5.getInfo());
               var5.setInfo((UserInfo)null);
            }
         }

      }
   }

   public void deleteGroup(Group var1) throws SecurityException {
      if (this.log != null) {
         this.log.debug("deleteGroup(\"" + var1.getName() + "\")");
      }

      String var2 = var1.getName();
      if (this.manageable != null) {
         try {
            this.manageable.deleteGroup(var1);
         } catch (UnsupportedOperationException var7) {
            if (this.backup == null) {
               throw var7;
            }

            this.backup.deleteGroup(var1);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("deleteGroup not supported by delegate");
         }

         this.backup.deleteGroup(var1);
      }

      String var3 = this.caseSensitive ? var2 : var2.toLowerCase(Locale.ENGLISH);
      synchronized(this.groupSync) {
         this.groupPosCache.remove(var3);
      }
   }

   public void deletePermission(Permission var1) throws SecurityException {
      String var2 = var1 instanceof PermissionImpl ? ((PermissionImpl)var1).getName() : var1.toString();
      if (this.log != null) {
         this.log.debug("deletePermission(\"" + var2 + "\")");
      }

      if (this.manageable != null) {
         try {
            this.manageable.deletePermission(var1);
         } catch (UnsupportedOperationException var7) {
            if (this.backup == null) {
               throw var7;
            }

            this.backup.deletePermission(var1);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("deletePermission not supported by delegate");
         }

         this.backup.deletePermission(var1);
      }

      String var3 = this.caseSensitive ? var2 : var2.toLowerCase(Locale.ENGLISH);
      synchronized(this.permSync) {
         this.permPosCache.remove(var3);
      }
   }

   public void deleteAcl(Principal var1, Acl var2) throws SecurityException {
      if (this.log != null) {
         this.log.debug("deleteAcl(\"" + var1.getName() + "\", \"" + var2.getName() + "\")");
      }

      String var3 = var2.getName();
      if (this.manageable != null) {
         try {
            this.manageable.deleteAcl(var1, var2);
         } catch (UnsupportedOperationException var8) {
            if (this.backup == null) {
               throw var8;
            }

            this.backup.deleteAcl(var1, var2);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("deleteAcl not supported by delegate");
         }

         this.backup.deleteAcl(var1, var2);
      }

      String var4 = this.caseSensitive ? var3 : var3.toLowerCase(Locale.ENGLISH);
      synchronized(this.aclSync) {
         this.aclPosCache.remove(var4);
      }
   }

   public void setPermission(Acl var1, Principal var2, Permission var3, boolean var4) {
      if (this.log != null) {
         this.log.debug("deleteAcl(\"" + var1.getName() + "\", \"" + var2.getName() + "\", " + var3 + ", " + var4 + ")");
      }

      String var5 = var1.getName();
      if (this.manageable != null) {
         try {
            this.manageable.setPermission(var1, var2, var3, var4);
         } catch (UnsupportedOperationException var10) {
            if (this.backup == null) {
               throw var10;
            }

            this.backup.setPermission(var1, var2, var3, var4);
         }
      } else {
         if (this.backup == null) {
            throw new UnsupportedOperationException("setPermission not supported by delegate");
         }

         this.backup.setPermission(var1, var2, var3, var4);
      }

      String var6 = this.caseSensitive ? var5 : var5.toLowerCase(Locale.ENGLISH);
      synchronized(this.aclSync) {
         if (var4) {
            this.aclNegCache.remove(var6);
         } else {
            this.aclPosCache.remove(var6);
         }

      }
   }

   private Enumeration getEnumeration(Enumeration var1, Enumeration var2) {
      if (var1 != null) {
         return (Enumeration)(var2 != null ? new ArrayEnumeration(new Enumeration[]{var1, var2}) : var1);
      } else {
         return var2 != null ? var2 : null;
      }
   }

   public Enumeration getUsers() {
      if (this.log != null) {
         this.log.debug("getUsers()");
      }

      Enumeration var1 = null;

      try {
         var1 = this.delegate.getUsers();
      } catch (UnsupportedOperationException var3) {
         if (this.backup == null) {
            throw var3;
         }
      }

      Enumeration var2 = this.backup != null ? this.backup.getUsers() : null;
      return this.getEnumeration(var1, var2);
   }

   public Enumeration getGroups() {
      if (this.log != null) {
         this.log.debug("getGroups()");
      }

      Enumeration var1 = null;

      try {
         var1 = this.delegate.getGroups();
      } catch (UnsupportedOperationException var3) {
         if (this.backup == null) {
            throw var3;
         }
      }

      Enumeration var2 = this.backup != null ? this.backup.getGroups() : null;
      return this.getEnumeration(var1, var2);
   }

   public Enumeration getAcls() {
      if (this.log != null) {
         this.log.debug("getAcls()");
      }

      Enumeration var1 = null;

      try {
         var1 = this.delegate.getAcls();
      } catch (UnsupportedOperationException var3) {
         if (this.backup == null) {
            throw var3;
         }
      }

      Enumeration var2 = this.backup != null ? this.backup.getAcls() : null;
      return this.getEnumeration(var1, var2);
   }

   public Enumeration getPermissions() {
      if (this.log != null) {
         this.log.debug("getPermissions()");
      }

      Enumeration var1 = null;

      try {
         var1 = this.delegate.getPermissions();
      } catch (UnsupportedOperationException var3) {
         if (this.backup == null) {
            throw var3;
         }
      }

      Enumeration var2 = this.backup != null ? this.backup.getPermissions() : null;
      return this.getEnumeration(var1, var2);
   }

   public void refresh() {
      this.clearCaches();
      if (this.delegate instanceof RefreshableRealm) {
         ((RefreshableRealm)this.delegate).refresh();
      }

      if (this.backup != null && this.backup instanceof RefreshableRealm) {
         ((RefreshableRealm)this.backup).refresh();
      }

   }

   public void clearCaches() {
      if (this.log != null) {
         this.log.debug("clearCaches()");
      }

      this.clearUserCaches();
      this.clearGroupCaches();
      this.clearAclCaches();
      this.clearPermCaches();
   }

   public void clearUserCaches() {
      if (this.log != null) {
         this.log.debug("clearUserCaches()");
      }

      synchronized(this.userSync) {
         this.userPosCache.clear();
         this.userNegCache.clear();
         this.authPosCache.clear();
         this.authNegCache.clear();
      }
   }

   void addGroupToCache(Group var1) {
      if (var1 != null) {
         if (this.log != null) {
            this.log.debug("adding group " + var1.getName() + " to group cache");
         }

         String var2 = this.caseSensitive ? var1.getName() : var1.getName().toLowerCase(Locale.ENGLISH);
         Entry var3 = new Entry(var1, this.delegate);
         synchronized(this.groupSync) {
            this.groupPosCache.put(var2, var3);
            this.groupNegCache.remove(var2);
         }
      }

   }

   public void clearGroupCaches() {
      if (this.log != null) {
         this.log.debug("clearGroupCaches()");
      }

      synchronized(this.groupSync) {
         this.groupPosCache.clear();
         this.groupNegCache.clear();
      }
   }

   public void clearAclCaches() {
      if (this.log != null) {
         this.log.debug("clearAclCaches()");
      }

      synchronized(this.aclSync) {
         this.aclPosCache.clear();
         this.aclNegCache.clear();
      }
   }

   public void clearPermCaches() {
      if (this.log != null) {
         this.log.debug("clearPermCaches()");
      }

      synchronized(this.permSync) {
         this.permPosCache.clear();
         this.permNegCache.clear();
      }
   }

   public void setDebug(boolean var1) {
      if (this.delegate instanceof DebuggableRealm) {
         ((DebuggableRealm)this.delegate).setDebug(var1);
      }

      if (this.backup != null && this.backup instanceof DebuggableRealm) {
         ((DebuggableRealm)this.backup).setDebug(var1);
      }

      if (var1 && this.log == null) {
         this.log = new LogOutputStream("CachingRealm");
      }

      if (!var1) {
         this.log = null;
      }

   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   public Class getDelegateClass() {
      return this.delegate.getClass();
   }

   public Object getCacheValue(Object var1) {
      Object var2 = null;
      return var2;
   }

   // $FF: synthetic method
   CachingRealm(ListableRealm var1, ManageableRealm var2, boolean var3, boolean var4, int var5, int var6, int var7, boolean var8, int var9, int var10, int var11, boolean var12, int var13, int var14, int var15, boolean var16, int var17, int var18, int var19, boolean var20, int var21, int var22, int var23, boolean var24, Object var25, Object var26) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25);
   }

   private class ArrayEnumeration implements ClosableEnumeration {
      private Enumeration[] enumerations = null;
      private int current = 0;

      ArrayEnumeration(Enumeration[] var2) {
         this.enumerations = var2;
      }

      private Enumeration current() {
         return this.current >= this.enumerations.length ? null : this.enumerations[this.current];
      }

      private void close(Enumeration var1) {
         if (var1 instanceof ClosableEnumeration) {
            try {
               ((ClosableEnumeration)var1).close();
            } catch (Throwable var3) {
               if (CachingRealm.this.log != null) {
                  SecurityLogger.logClosingEnumerationWarning(var3.toString());
               }
            }
         }

      }

      private void increment() {
         if (this.current() != null) {
            this.close(this.current());
            ++this.current;
         }
      }

      public boolean hasMoreElements() {
         if (this.current() == null) {
            return false;
         } else if (this.current().hasMoreElements()) {
            return true;
         } else {
            this.increment();
            return this.current() == null ? false : this.current().hasMoreElements();
         }
      }

      public Object nextElement() {
         if (this.current() == null) {
            throw new NoSuchElementException();
         } else {
            try {
               return this.current().nextElement();
            } catch (NoSuchElementException var2) {
               this.increment();
               if (this.current() == null) {
                  throw new NoSuchElementException();
               } else {
                  return this.current().nextElement();
               }
            }
         }
      }

      public void close() {
         while(this.current() != null) {
            this.increment();
         }

      }
   }

   /** @deprecated */
   protected static class CaseInsensitiveUserInfo implements UserInfo {
      private static final long serialVersionUID = -4798082305580933563L;
      private UserInfo info;
      private DefaultUserInfoImpl defInfo;
      private String name;
      private String realmName;
      private int hashCode;

      CaseInsensitiveUserInfo(UserInfo var1) {
         this.name = var1.getName().toLowerCase(Locale.ENGLISH);
         this.realmName = var1.getRealmName();
         this.info = var1;
         if (var1 instanceof DefaultUserInfoImpl) {
            this.defInfo = (DefaultUserInfoImpl)var1;
         }

         this.hashCode = (this.name == null ? 0 : this.name.hashCode()) ^ (this.realmName == null ? 0 : this.realmName.hashCode());
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof CaseInsensitiveUserInfo)) {
            return var1 instanceof UserInfo ? this.info.equals(var1) : false;
         } else {
            UserInfo var2 = ((CaseInsensitiveUserInfo)var1).getInfo();
            if (this.defInfo != null && var2 instanceof DefaultUserInfoImpl) {
               DefaultUserInfoImpl var3 = (DefaultUserInfoImpl)var2;
               return this.name.equals(var3.getName().toLowerCase(Locale.ENGLISH)) && this.defInfo.equalsInAllButName(var3);
            } else {
               System.err.println("***");
               System.err.println("*** Encountered an unknown kind of UserInfo object.");
               System.err.println("*** UserInfo type is: " + var2.getClass().getName());
               System.err.println("*** Server cannot check in a case-insensitive way.");
               System.err.println("***");
               return this.info.equals(var2);
            }
         }
      }

      UserInfo getInfo() {
         return this.info;
      }

      public String getName() {
         return this.name;
      }

      public String getRealmName() {
         return this.realmName;
      }
   }

   private static class UnitTest {
      public static void main(String[] var0) throws Exception {
         if (var0.length != 4) {
            System.err.println("usage: test class user passwd group");
         }

         ListableRealm var1 = (ListableRealm)Class.forName(var0[0]).newInstance();
         byte var2 = 17;
         byte var3 = 10;
         byte var4 = 5;
         CachingRealm var5 = new CachingRealm(var1, (ManageableRealm)null, true, true, var2, var3, var4, true, var2, var3, var4, true, var2, var3, var4, true, var2, var3, var4, true, var2, var3, var4, true, (Object)null);
         User var6 = var5.getUser(var0[1]);
         System.out.println("user " + var0[1] + ": " + (var6 != null ? "exists" : "unperson"));
         User var7 = var5.authenticate(new DefaultUserInfoImpl(var0[1], var0[2]));
         System.out.println("auth " + var0[1] + ": " + (var7 != null ? "succeeded" : "failed"));
         Group var8 = var5.getGroup(var0[3]);
         System.out.println("group " + var0[3] + ": " + (var8 != null ? EnumerationUtils.toString(var8.members()) : "ungroup"));
         System.out.println("users: " + EnumerationUtils.toString(var5.getUsers()));
         System.out.println("groups: " + EnumerationUtils.toString(var5.getGroups()));
         System.out.println("acls: " + EnumerationUtils.toString(var5.getAcls()));
      }
   }

   private static class DisabledCache extends TTLCache {
      DisabledCache() {
         super(0, 1L);
      }

      public Object put(Object var1, Object var2) {
         return null;
      }

      public Object put(Object var1) {
         return null;
      }

      public Object get(Object var1) {
         return null;
      }

      public Object remove(Object var1) {
         return null;
      }

      public void clear() {
      }

      public void cleanup() {
      }

      public int size() {
         return 0;
      }

      public boolean isEmpty() {
         return true;
      }

      public boolean containsKey(Object var1) {
         return false;
      }
   }

   /** @deprecated */
   protected static class UserEntry extends Entry {
      private UserInfo info;

      UserEntry(Object var1, ListableRealm var2) {
         super(var1, var2);
      }

      void setInfo(UserInfo var1) {
         this.info = var1;
      }

      UserInfo getInfo() {
         return this.info;
      }
   }

   /** @deprecated */
   protected static class Entry {
      private Object value;
      private ListableRealm source;

      Entry(Object var1, ListableRealm var2) {
         this.value = var1;
         this.source = var2;
      }

      Object getValue() {
         return this.value;
      }

      ListableRealm getSource() {
         return this.source;
      }
   }
}
