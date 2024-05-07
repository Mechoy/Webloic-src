package weblogic.wsee.persistence;

import java.io.Serializable;
import weblogic.kernel.KernelStatus;
import weblogic.store.PersistentStore;

public abstract class WLSStoreValuesMap<K extends Serializable, V extends Storable> extends ValuesMap<K, V> {
   protected PersistentStore _store;

   public WLSStoreValuesMap(PersistentStore var1, String var2) {
      super(var2);
      this._store = var1;
   }

   public PersistentStore getStore() {
      return this._store;
   }

   public String getStoreName() {
      return this._store.getName();
   }

   public void close() throws StoreException {
      if (!KernelStatus.isServer()) {
         try {
            this._store.close();
         } catch (Exception var2) {
            throw new StoreException(var2.toString(), var2);
         }
      }

   }
}
