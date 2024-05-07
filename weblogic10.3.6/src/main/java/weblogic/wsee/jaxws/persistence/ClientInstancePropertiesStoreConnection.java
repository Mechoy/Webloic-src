package weblogic.wsee.jaxws.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.persistence.Storable;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;

public class ClientInstancePropertiesStoreConnection extends StoreConnection<ClientInstanceIdentity, ClientInstanceProperties> {
   private ReentrantReadWriteLock _clientIdMapLock = new ReentrantReadWriteLock(false);
   private Map<String, Map<Serializable, ClientInstanceProperties>> _clientIdMap = new HashMap();

   public ClientInstancePropertiesStoreConnection(String var1, String var2) throws StoreException {
      super(var1, var2);
   }

   protected void recoveryStarting() {
   }

   protected void recoverValue(ClientInstanceProperties var1) {
      super.recoverValue(var1);
      Object var2 = (Map)this._clientIdMap.get(var1.getId().getClientId());
      if (var2 == null) {
         var2 = new HashMap();
         this._clientIdMap.put(var1.getId().getClientId(), var2);
      }

      ((Map)var2).put(var1.getId().getExtraId(), var1);
   }

   public ClientInstanceProperties put(ClientInstanceIdentity var1, ClientInstanceProperties var2) {
      try {
         this._clientIdMapLock.writeLock().lock();
         Object var3 = (Map)this._clientIdMap.get(var1.getClientId());
         if (var3 == null) {
            var3 = new HashMap();
            this._clientIdMap.put(var1.getClientId(), var3);
         }

         ((Map)var3).put(var1.getExtraId(), var2);
      } finally {
         this._clientIdMapLock.writeLock().unlock();
      }

      return (ClientInstanceProperties)super.put((Serializable)var1, (Storable)var2);
   }

   public Map<Serializable, ClientInstanceProperties> getByClientId(String var1) {
      HashMap var3;
      try {
         this._clientIdMapLock.readLock().lock();
         Map var2 = (Map)this._clientIdMap.get(var1);
         var3 = var2 != null ? new HashMap(var2) : null;
      } finally {
         this._clientIdMapLock.readLock().unlock();
      }

      return var3;
   }

   public ClientInstanceProperties remove(Object var1) {
      Map var4;
      try {
         ClientInstanceIdentity var2 = (ClientInstanceIdentity)var1;
         ClientInstanceProperties var3 = (ClientInstanceProperties)super.remove(var2);
         if (var3 != null) {
            this._clientIdMapLock.writeLock().lock();
            var4 = (Map)this._clientIdMap.get(var2.getClientId());
            if (var4 != null) {
               var4.remove(var2.getExtraId());
               if (var4.isEmpty()) {
                  this._clientIdMap.remove(var2.getClientId());
               }
            }

            ClientInstanceProperties var5 = var3;
            return var5;
         }

         var4 = null;
      } finally {
         this._clientIdMapLock.writeLock().unlock();
      }

      return var4;
   }
}
