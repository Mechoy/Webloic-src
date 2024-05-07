package weblogic.security.acl;

import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import weblogic.management.configuration.CachingRealmMBean;
import weblogic.management.configuration.RealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public abstract class FlatGroup extends DefaultGroupImpl {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final int LIFETIME_DEFAULT_SEC = 300;
   private static long LIFETIME_MS = -1L;
   private static boolean allowEnumeration = true;
   private long lastUpdate;
   private Source source;
   protected Hashtable members;
   private Hashtable users;
   private Hashtable groups;
   private Class userClass;
   private Class myClass;
   private boolean caseSensitive;

   private static long getLifeTimeMillis() {
      if (LIFETIME_MS == -1L) {
         Class var0 = FlatGroup.class;
         synchronized(FlatGroup.class) {
            if (LIFETIME_MS == -1L) {
               int var1 = 300;
               RealmMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm();
               if (var2 != null) {
                  allowEnumeration = var2.isEnumerationAllowed();
                  CachingRealmMBean var3 = var2.getCachingRealm();
                  if (var3 != null) {
                     var1 = var3.getGroupMembershipCacheTTL();
                  } else {
                     var1 = 300;
                  }
               }

               LIFETIME_MS = (long)(var1 * 1000);
            }
         }
      }

      return LIFETIME_MS;
   }

   protected FlatGroup(String var1, Source var2) {
      super(var1);
      this.lastUpdate = -1L;
      this.source = var2;
      this.userClass = this.getUserClass();
      this.myClass = this.getClass();
      if (ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm() != null) {
         this.caseSensitive = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm().getCacheCaseSensitive();
      }

   }

   protected FlatGroup(String var1, Source var2, Hashtable var3) {
      this(var1, var2);
      this.fillCache(var3);
   }

   protected final void ensureFreshness() {
      long var1 = System.currentTimeMillis();
      if (this.members == null || var1 > this.lastUpdate + getLifeTimeMillis()) {
         this.fillCache(this.source.getGroupMembers(this.getName()));
      }

   }

   private void fillCache(Hashtable var1) {
      if (var1 == null) {
         throw new RuntimeException("unexpected error - group \"" + this.getName() + "\" does not exist");
      } else {
         this.members = var1;
         this.groups = new Hashtable();
         this.users = new Hashtable();
         Enumeration var2 = var1.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            Object var4 = var1.get(var3);
            if (var4 instanceof Group) {
               this.groups.put(var3, var4);
               BasicRealm var5 = Security.getRealm();
               if (var5 instanceof CachingRealm) {
                  CachingRealm var6 = (CachingRealm)var5;
                  var6.addGroupToCache((Group)var4);
               }
            } else if (this.caseSensitive) {
               this.users.put(var3, var4);
            } else {
               this.users.put(var3.toLowerCase(Locale.ENGLISH), var4);
            }
         }

         this.lastUpdate = System.currentTimeMillis();
      }
   }

   public boolean isMember(Principal var1) {
      synchronized(this) {
         this.ensureFreshness();
         if (this.userClass.isAssignableFrom(var1.getClass())) {
            if (this.caseSensitive && this.users.containsKey(var1.getName())) {
               return true;
            }

            if (!this.caseSensitive && this.users.containsKey(var1.getName().toLowerCase(Locale.ENGLISH))) {
               return true;
            }
         }

         Enumeration var3 = this.groups.elements();

         Group var4;
         do {
            if (!var3.hasMoreElements()) {
               return false;
            }

            var4 = (Group)var3.nextElement();
         } while(!var4.equals(var1) && !var4.isMember(var1));

         return true;
      }
   }

   public Enumeration members() {
      if (allowEnumeration) {
         synchronized(this) {
            this.ensureFreshness();
            return this.members != null ? this.members.elements() : null;
         }
      } else {
         return null;
      }
   }

   public String toString() {
      return this.getName();
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object var1) {
      return var1 != null && this.myClass.isAssignableFrom(var1.getClass()) && this.getName().equals(((Group)var1).getName());
   }

   protected abstract Class getUserClass();

   protected boolean addMemberInternal(Principal var1) {
      synchronized(this) {
         String var3 = var1.getName();
         this.members.put(var3, var1);
         if (var1 instanceof User) {
            this.users.put(var3, var1);
         } else {
            this.groups.put(var3, var1);
         }

         return true;
      }
   }

   protected boolean removeMemberInternal(Principal var1) {
      synchronized(this) {
         String var3 = var1.getName();
         this.members.remove(var3);
         if (var1 instanceof User) {
            this.users.remove(var3);
         } else {
            this.groups.remove(var3);
         }

         return true;
      }
   }

   protected static final void setCacheTTL(long var0) {
      if (var0 <= 0L) {
         throw new IllegalArgumentException("bad TTL");
      } else {
         Class var2 = FlatGroup.class;
         synchronized(FlatGroup.class) {
            LIFETIME_MS = var0;
         }
      }
   }

   public static final long getCacheTTLMillis() {
      return getLifeTimeMillis();
   }

   public Hashtable getMembersHashtable() {
      long var1 = System.currentTimeMillis();
      return var1 > this.lastUpdate + getLifeTimeMillis() ? null : this.members;
   }

   public void clearCache() {
      synchronized(this) {
         this.members = null;
      }
   }

   /** @deprecated */
   public interface Source {
      Hashtable getGroupMembers(String var1);
   }
}
