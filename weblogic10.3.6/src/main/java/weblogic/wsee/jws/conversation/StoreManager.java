package weblogic.wsee.jws.conversation;

import java.util.HashMap;
import java.util.Map;
import weblogic.wsee.jws.conversation.database.DBStoreFactory;

public class StoreManager {
   private static Map<String, StoreFactory> storeFactories = new HashMap();
   private static Map<StoreConfig, Store> storeCache = new HashMap();

   public static synchronized void register(StoreFactory var0) {
      storeFactories.put(var0.getType(), var0);
   }

   public static synchronized void unregister(String var0) {
      storeFactories.remove(var0);
   }

   public static synchronized Store getStore(StoreConfig var0) throws StoreException {
      if (var0 == null) {
         throw new IllegalArgumentException("config may not be null");
      } else {
         Store var1 = (Store)storeCache.get(var0);
         if (var1 == null) {
            StoreFactory var2 = (StoreFactory)storeFactories.get(var0.getType());
            if (var2 == null) {
               throw new StoreException("unknown store factory type: " + var0.getType());
            }

            var1 = var2.createStore(var0);
            storeCache.put(var0, var1);
         }

         return var1;
      }
   }

   static {
      register(new FileStoreFactory());
      register(new DBStoreFactory());
   }
}
