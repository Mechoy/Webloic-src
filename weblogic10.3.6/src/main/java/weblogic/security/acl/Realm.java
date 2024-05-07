package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Permission;
import java.util.Hashtable;
import java.util.Properties;
import weblogic.common.internal.LogOutputStream;
import weblogic.security.audit.Audit;
import weblogic.security.principal.RealmAdapterUser;

/** @deprecated */
public final class Realm {
   static String defaultImplName = "weblogic.security.acl.internal.FileRealm";
   static Hashtable realms = new Hashtable();
   static int lockoutSeqNumber = 0;
   private static LogOutputStream log;

   public static BasicRealm getRealm(String var0) {
      return getRealm(var0, (Object)null, defaultImplName, (Properties)null);
   }

   public static BasicRealm getRealm(String var0, Object var1) {
      return getRealm(var0, var1, defaultImplName, (Properties)null);
   }

   public static BasicRealm getRealm(String var0, Object var1, String var2) {
      return getRealm(var0, var1, var2, (Properties)null);
   }

   public static BasicRealm getRealm(String var0, Object var1, String var2, Properties var3) {
      try {
         BasicRealm var4 = (BasicRealm)realms.get(var0);
         if (var4 != null) {
            return var4;
         } else {
            if (var2 == null) {
               var2 = defaultImplName;
            }

            var4 = (BasicRealm)Class.forName(var2).newInstance();
            var4.init(var0, var1);
            var4.load(var0, var1);
            realms.put(var0, var4);
            return var4;
         }
      } catch (Exception var5) {
         throw new IllegalAccessError(var5.toString());
      }
   }

   public static boolean checkPermission(Principal var0, String var1, String var2, String var3) {
      return checkPermission("Central Security", var0, var1, var2, var3, '.');
   }

   public static boolean checkPermission(String var0, Principal var1, String var2, String var3, String var4) {
      return checkPermission(var0, var1, var2, var3, var4, '.');
   }

   public static boolean checkPermission(Principal var0, String var1, String var2, String var3, char var4) {
      return checkPermission("Central Security", var0, var1, var2, var3, var4);
   }

   public static boolean checkPermission(String var0, Principal var1, String var2, String var3, String var4, char var5) {
      BasicRealm var6 = getRealm(var2);
      Permission var7 = var6.getPermission(var4);
      Acl var8 = var6.getAcl(var3, var5);
      User var9 = null;
      if (var1 instanceof RealmAdapterUser) {
         var9 = Security.getRealm().getUser(var1.getName());
      }

      boolean var10 = var1 != null && var7 != null && var8 != null && var8.checkPermission((Principal)(var9 != null ? var9 : var1), var7);
      Audit.checkPermission(var0, var8, (Principal)(var9 != null ? var9 : var1), var7, var10);
      return var10;
   }

   /** @deprecated */
   public static LogOutputStream getLog() {
      if (log == null) {
         Class var0 = Realm.class;
         synchronized(Realm.class) {
            if (log == null) {
               log = new LogOutputStream("Realm");
            }
         }
      }

      return log;
   }

   public static User authenticate(UserInfo var0) throws SecurityException {
      PasswordGuessing var1 = Security.getPasswordGuessing();
      String var2 = var0.getRealmName();
      BasicRealm var3 = getRealm(var2);
      if (var3 == null) {
         throw new SecurityException("Realm " + var2 + " is unknown here");
      } else {
         String var4 = var0.getName();
         if (var4 != null && var1 != null && var1.isLocked(var4)) {
            throw new SecurityException("Authentication for user " + var4 + " denied in realm " + var2);
         } else {
            User var5 = var3.getUser(var0);
            if (var5 == null) {
               if (var4 != null && var1 != null) {
                  var1.logFailure(var4);
               }

               throw new SecurityException("Authentication for user " + var4 + " denied in realm " + var2);
            } else {
               if (var1 != null) {
                  var1.logSuccess(var5.getName());
               }

               return var5;
            }
         }
      }
   }

   public static String getAuthenticatedName(UserInfo var0) throws SecurityException {
      return authenticate(var0).getName();
   }
}
