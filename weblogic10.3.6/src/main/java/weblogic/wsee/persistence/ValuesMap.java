package weblogic.wsee.persistence;

import java.io.Serializable;
import java.util.Map;

public abstract class ValuesMap<K extends Serializable, V extends Storable> implements Map<K, V> {
   protected String _connectionName;

   protected ValuesMap(String var1) {
      this._connectionName = var1;
   }

   public abstract String getStoreName();

   public void close() throws StoreException {
   }
}
