package weblogic.security.audit;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Permission;
import weblogic.security.acl.Security;
import weblogic.security.acl.User;
import weblogic.security.acl.UserInfo;
import weblogic.security.principal.RealmAdapterUser;

/** @deprecated */
public final class Audit {
   private static Object sync = new Object();
   private static AuditProvider provider = null;

   private Audit() {
   }

   public static void setProvider(AuditProvider var0) {
      if (var0 == null) {
         throw new NullPointerException("null provider");
      } else {
         Object var1 = sync;
         if (var1 == null) {
            throw new SecurityException("audit provider already set");
         } else {
            synchronized(var1) {
               if (sync == null) {
                  throw new SecurityException("audit provider already set");
               } else {
                  provider = var0;
                  sync = null;
               }
            }
         }
      }
   }

   public static void authenticateUser(String var0, UserInfo var1, User var2) {
      if (provider != null) {
         provider.authenticateUser(var0, var1, var2);
      }

   }

   public static void checkPermission(String var0, Acl var1, Principal var2, Permission var3, boolean var4) {
      if (provider != null) {
         User var5 = null;
         if (var2 instanceof RealmAdapterUser) {
            var5 = Security.getRealm().getUser(var2.getName());
         }

         provider.checkPermission(var0, var1, (Principal)(var5 != null ? var5 : var2), var3, var4);
      }

   }

   public static AuditProvider getProvider() {
      return provider;
   }
}
