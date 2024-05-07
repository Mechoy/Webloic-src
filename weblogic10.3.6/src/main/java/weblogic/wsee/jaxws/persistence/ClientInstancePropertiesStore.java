package weblogic.wsee.jaxws.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;

public class ClientInstancePropertiesStore extends LogicalStore<ClientInstanceIdentity, ClientInstanceProperties> {
   private static final String CONNECTION_NAME = ClientInstancePropertiesStore.class.getName();
   private static final ClientInstancePropertiesStore _instance = new ClientInstancePropertiesStore();

   public static ClientInstancePropertiesStore getStore(String var0) throws StoreException {
      return (ClientInstancePropertiesStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private ClientInstancePropertiesStore() {
   }

   ClientInstancePropertiesStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
   }

   public ClientInstancePropertiesStore createLogicalStore(String var1, String var2) throws StoreException {
      return new ClientInstancePropertiesStore(var1);
   }

   public ClientInstancePropertiesStoreConnection createStoreConnection(String var1, String var2) throws StoreException {
      return new ClientInstancePropertiesStoreConnection(var1, var2);
   }

   public Map<Serializable, ClientInstanceProperties> getByClientId(String var1) {
      List var2 = this.getStoreConnections();
      HashMap var3 = new HashMap();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         StoreConnection var5 = (StoreConnection)var4.next();
         Map var6 = ((ClientInstancePropertiesStoreConnection)var5).getByClientId(var1);
         if (var6 != null) {
            var3.putAll(var6);
         }
      }

      return var3;
   }

   static {
      _instance.toString();
   }
}
