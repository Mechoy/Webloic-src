package weblogic.messaging.saf.store;

import java.util.HashMap;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.messaging.saf.SAFException;

public final class SAFStoreManager {
   private static final SAFStoreManager SINGLETON = new SAFStoreManager();
   private static final HashMap stores = new HashMap();

   private SAFStoreManager() {
   }

   public static SAFStoreManager getManager() {
      return SINGLETON;
   }

   public SAFStore createSAFStore(PersistentStoreMBean var1, String var2, boolean var3) throws SAFException {
      return findOrCreateSAFStore(var1 == null ? (String)null : var1.getName(), var2, var3);
   }

   private static synchronized SAFStore findOrCreateSAFStore(String var0, String var1, boolean var2) throws SAFException {
      String var3 = var1 + (var2 ? ".RA" : ".SA");
      SAFStore var4 = (SAFStore)stores.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         var4 = new SAFStore(var0, var3, var2);
         stores.put(var3, var4);
         return var4;
      }
   }
}
