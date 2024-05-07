package weblogic.wsee.persistence;

import java.io.Serializable;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class InMemoryValuesMap<K extends Serializable, V extends Storable> extends ValuesMap<K, V> {
   private static final Logger LOGGER = Logger.getLogger(InMemoryValuesMap.class.getName());
   public static final String PHYSICAL_STORE_PREFIX = "WseeInMemoryPhysicalStore-";
   public static final String PHYSICAL_STORE_NAME;
   private Map<K, V> _valuesMap = new HashMap();
   private final ReentrantReadWriteLock _valueLock = new ReentrantReadWriteLock(false);

   public InMemoryValuesMap(String var1) {
      super(var1);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == Store Created for " + this);
      }

   }

   public String toString() {
      return "storeName = " + this.getStoreName() + " connectionName = " + this._connectionName;
   }

   public String getStoreName() {
      return PHYSICAL_STORE_NAME;
   }

   public int size() {
      int var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.size();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public boolean isEmpty() {
      boolean var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.isEmpty();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public boolean containsKey(Object var1) {
      boolean var2;
      try {
         this._valueLock.readLock().lock();
         var2 = this._valuesMap.containsKey(var1);
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      boolean var2;
      try {
         this._valueLock.readLock().lock();
         var2 = this._valuesMap.containsValue(var1);
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public V get(Object var1) {
      Storable var2;
      try {
         this._valueLock.readLock().lock();
         var2 = (Storable)this._valuesMap.get(var1);
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public V put(K var1, V var2) {
      Storable var3;
      try {
         this._valueLock.writeLock().lock();
         var3 = (Storable)this._valuesMap.put(var1, var2);
      } finally {
         this._valueLock.writeLock().unlock();
      }

      return var3;
   }

   public V remove(Object var1) {
      Storable var2;
      try {
         this._valueLock.writeLock().lock();
         var2 = (Storable)this._valuesMap.remove(var1);
      } finally {
         this._valueLock.writeLock().unlock();
      }

      return var2;
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      try {
         this._valueLock.writeLock().lock();
         this._valuesMap.putAll(var1);
      } finally {
         this._valueLock.writeLock().unlock();
      }

   }

   public void clear() {
      try {
         this._valueLock.writeLock().lock();
         this._valuesMap.clear();
      } finally {
         this._valueLock.writeLock().unlock();
      }

   }

   public Set<K> keySet() {
      Set var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.keySet();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public Collection<V> values() {
      Collection var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.values();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public Set<Map.Entry<K, V>> entrySet() {
      Set var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.entrySet();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(var0);
      if (var1 != null) {
         PHYSICAL_STORE_NAME = "WseeInMemoryPhysicalStore-" + var1.getServerName();
      } else {
         PHYSICAL_STORE_NAME = "WseeInMemoryPhysicalStore-";
      }

   }
}
