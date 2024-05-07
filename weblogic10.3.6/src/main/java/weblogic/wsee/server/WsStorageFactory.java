package weblogic.wsee.server;

import java.util.HashMap;
import weblogic.store.ObjectHandler;
import weblogic.store.PersistentStoreException;

public class WsStorageFactory {
   private static HashMap stores = new HashMap();

   public static synchronized WsStorage getStorage(String var0) {
      return getStorage(var0, (ObjectHandler)null, false, false);
   }

   public static synchronized WsStorage getStorage(String var0, ObjectHandler var1) {
      return getStorage(var0, var1, false, false);
   }

   public static synchronized WsStorage getStorage(String var0, ObjectHandler var1, boolean var2) {
      return getStorage(var0, var1, var2, false);
   }

   public static synchronized WsStorage getStorage(String var0, ObjectHandler var1, boolean var2, boolean var3) {
      WsStorage var4 = (WsStorage)stores.get(var0);
      if (var4 == null) {
         var4 = new WsStorage(var0, var1, var2, var3);
         stores.put(var0, var4);
      }

      return var4;
   }

   public static synchronized void closeStorage(WsStorage var0) throws PersistentStoreException {
      stores.remove(var0.getName());
      var0.close();
   }
}
