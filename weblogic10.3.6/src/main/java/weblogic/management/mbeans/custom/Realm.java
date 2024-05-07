package weblogic.management.mbeans.custom;

import weblogic.management.configuration.RealmException;
import weblogic.management.configuration.RealmManager;
import weblogic.management.internal.RemoteRealmManagerImpl;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.RefreshableRealm;

public final class Realm extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = -1624882119477172375L;
   private RealmManager realmManager;

   public Realm(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void refresh() throws RealmException {
      if (!this.isAdmin()) {
         BasicRealm var1 = weblogic.security.acl.Realm.getRealm("weblogic");
         if (var1 instanceof RefreshableRealm) {
            try {
               ((RefreshableRealm)var1).refresh();
            } catch (Throwable var3) {
               throw new RealmException("The realm didn't successfully refresh", var3);
            }
         } else {
            throw new RealmException("The realm doesn't support refresh.");
         }
      }
   }

   public synchronized RealmManager manager() {
      if (this.realmManager == null) {
         this.realmManager = new RealmManager(new RemoteRealmManagerImpl());
      }

      return this.realmManager;
   }
}
