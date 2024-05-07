package weblogic.security.acl;

import java.util.Hashtable;

/** @deprecated */
public class RealmProxy {
   static String defaultImplName = "weblogic.security.acl.RealmProxy";
   static Hashtable realmProxies = new Hashtable();
   String name = null;

   public static RealmProxy getRealmProxy(String var0) {
      return getRealmProxy(var0, defaultImplName);
   }

   public static RealmProxy getRealmProxy(String var0, String var1) {
      try {
         RealmProxy var2 = (RealmProxy)realmProxies.get(var0);
         if (var2 != null) {
            return var2;
         } else {
            var2 = (RealmProxy)Class.forName(var1).newInstance();
            var2.name = var0;
            realmProxies.put(var0, var2);
            return var2;
         }
      } catch (Exception var3) {
         throw new IllegalAccessError(var3.toString());
      }
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public UserInfo createUserInfo(String var1, Object var2) {
      return new DefaultUserInfoImpl(var1, var2, this.name);
   }
}
