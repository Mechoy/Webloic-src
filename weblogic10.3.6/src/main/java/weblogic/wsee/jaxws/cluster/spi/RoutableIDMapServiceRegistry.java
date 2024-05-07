package weblogic.wsee.jaxws.cluster.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import weblogic.wsee.WseeCoreLogger;

public class RoutableIDMapServiceRegistry implements ServerNameMapService.ServerAddressChangeListener {
   private static final RoutableIDMapServiceRegistry _instance = new RoutableIDMapServiceRegistry();
   private ArrayList<RoutableIDMapService> _mappers = new ArrayList();
   private ServerNameMapService _serverNameMapService = null;
   private ReentrantReadWriteLock _mappersLock = new ReentrantReadWriteLock(false);
   private ReentrantReadWriteLock _serverNameToAddressMapLock = new ReentrantReadWriteLock();
   private Map<String, ServerNameMapService.ServerAddress> _serverNameToAddressMap = new HashMap();
   private long _lastUpdateTime = 0L;

   public static RoutableIDMapServiceRegistry getInstance() {
      return _instance;
   }

   private RoutableIDMapServiceRegistry() {
   }

   public boolean addMapper(RoutableIDMapService var1) {
      boolean var2;
      try {
         this._mappersLock.writeLock().lock();
         if (this._mappers.contains(var1)) {
            var2 = false;
            return var2;
         }

         this._mappers.add(var1);
         if (var1 instanceof ServerNameMapService) {
            this._serverNameMapService = (ServerNameMapService)var1;
            this._serverNameMapService.addServerAddressChangeListener(this);
         }

         var2 = true;
      } finally {
         this._mappersLock.writeLock().unlock();
      }

      return var2;
   }

   public boolean removeMapper(RoutableIDMapService var1) {
      boolean var2;
      try {
         this._mappersLock.writeLock().lock();
         if (this._serverNameMapService == var1) {
            this._serverNameMapService.removeServerAddressChangeListener(this);
            this._serverNameMapService = null;
         }

         var2 = this._mappers.remove(var1);
      } finally {
         this._mappersLock.writeLock().unlock();
      }

      return var2;
   }

   public RoutableIDMapService[] getMappers() {
      RoutableIDMapService[] var1;
      try {
         this._mappersLock.readLock().lock();
         var1 = (RoutableIDMapService[])this._mappers.toArray(new RoutableIDMapService[this._mappers.size()]);
      } finally {
         this._mappersLock.readLock().unlock();
      }

      return var1;
   }

   public boolean hasNewInfoSince(long var1) {
      return this._lastUpdateTime > var1;
   }

   public Map<String, String> getCurrentRoutableIDToServerMap() {
      HashMap var1 = new HashMap();

      try {
         this._mappersLock.readLock().lock();
         Iterator var2 = this._mappers.iterator();

         while(var2.hasNext()) {
            RoutableIDMapService var3 = (RoutableIDMapService)var2.next();

            try {
               var1.putAll(var3.getCurrentRoutableIDToServerMap());
            } catch (Exception var8) {
               WseeCoreLogger.logUnexpectedException(var8.toString(), var8);
            }
         }
      } finally {
         this._mappersLock.readLock().unlock();
      }

      this.refreshServerNameToAddressMap(var1);
      return var1;
   }

   public ServerNameMapService.ServerAddress getServerAddress(String var1) {
      ServerNameMapService.ServerAddress var2 = null;

      try {
         this._serverNameToAddressMapLock.readLock().lock();
         var2 = (ServerNameMapService.ServerAddress)this._serverNameToAddressMap.get(var1);
      } finally {
         this._serverNameToAddressMapLock.readLock().unlock();
      }

      if (var2 == null && this._serverNameMapService != null) {
         try {
            this._serverNameToAddressMapLock.writeLock().lock();
            var2 = this._serverNameMapService.getServerAddress(var1);
            if (var2 != null) {
               this._serverNameToAddressMap.put(var1, var2);
            }
         } finally {
            this._serverNameToAddressMapLock.writeLock().unlock();
         }
      }

      return var2;
   }

   private void refreshServerNameToAddressMap(Map<String, String> var1) {
      if (this._serverNameMapService != null) {
         HashSet var2 = new HashSet();
         Iterator var3 = var1.values().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add(var4);
         }

         HashSet var14 = new HashSet();

         String var5;
         Iterator var15;
         try {
            this._serverNameToAddressMapLock.readLock().lock();
            var15 = var2.iterator();

            while(var15.hasNext()) {
               var5 = (String)var15.next();
               if (this._serverNameToAddressMap.containsKey(var5)) {
                  var14.add(var5);
               }
            }
         } finally {
            this._serverNameToAddressMapLock.readLock().unlock();
         }

         if (!var14.isEmpty()) {
            try {
               this._serverNameToAddressMapLock.writeLock().lock();
               this._lastUpdateTime = System.currentTimeMillis();
               var15 = var14.iterator();

               while(var15.hasNext()) {
                  var5 = (String)var15.next();
                  ServerNameMapService.ServerAddress var6 = this._serverNameMapService.getServerAddress(var5);
                  if (var6 != null) {
                     this._serverNameToAddressMap.put(var5, var6);
                  }
               }
            } finally {
               this._serverNameToAddressMapLock.writeLock().unlock();
            }
         }
      }

   }

   public void serverAddressChanged(String var1, ServerNameMapService var2) {
      try {
         this._serverNameToAddressMapLock.writeLock().lock();
         this._lastUpdateTime = System.currentTimeMillis();
         if (var1 == null) {
            this._serverNameToAddressMap.clear();
         } else {
            ServerNameMapService.ServerAddress var3 = var2.getServerAddress(var1);
            this._serverNameToAddressMap.put(var1, var3);
         }
      } finally {
         this._serverNameToAddressMapLock.writeLock().unlock();
      }

   }
}
