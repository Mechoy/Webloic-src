package weblogic.jms.forwarder.dd;

import java.util.HashMap;

public class DDForwardStoreManager {
   public static DDForwardStoreManager singleton = new DDForwardStoreManager();
   public HashMap storeMap = new HashMap();

   private DDForwardStoreManager() {
   }

   public synchronized void addStore(DDForwardStore var1) {
      this.storeMap.put(var1.getStore().getName(), var1);
   }

   public synchronized DDForwardStore getStore(String var1) {
      return (DDForwardStore)this.storeMap.get(var1);
   }
}
