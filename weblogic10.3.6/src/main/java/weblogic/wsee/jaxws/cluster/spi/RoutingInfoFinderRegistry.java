package weblogic.wsee.jaxws.cluster.spi;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import weblogic.wsee.wstx.wsat.cluster.WSATRoutingInfoFinder;

public class RoutingInfoFinderRegistry {
   private static final RoutingInfoFinderRegistry _instance = new RoutingInfoFinderRegistry();
   private ArrayList<RoutingInfoFinder> _finders = new ArrayList();
   private ReentrantReadWriteLock _findersLock = new ReentrantReadWriteLock(false);

   public static RoutingInfoFinderRegistry getInstance() {
      return _instance;
   }

   private RoutingInfoFinderRegistry() {
      this.addFinder(new MessageIDRoutingInfoFinder());
      this.addFinder(new PhysicalStoreNameRoutingInfoFinder());
      this.addFinder(new WSATRoutingInfoFinder());
   }

   public boolean addFinder(RoutingInfoFinder var1) {
      try {
         this._findersLock.writeLock().lock();
         if (this._finders.contains(var1)) {
            boolean var9 = false;
            return var9;
         } else {
            int var2 = -1;
            int var3 = 0;

            while(true) {
               if (var3 < this._finders.size()) {
                  if (((RoutingInfoFinder)this._finders.get(var3)).getFinderPriority() >= var1.getFinderPriority()) {
                     ++var3;
                     continue;
                  }

                  var2 = var3;
               }

               if (var2 < 0) {
                  var2 = this._finders.size();
               }

               this._finders.add(var2, var1);
               boolean var8 = true;
               return var8;
            }
         }
      } finally {
         this._findersLock.writeLock().unlock();
      }
   }

   public boolean removeFinder(RoutingInfoFinder var1) {
      boolean var2;
      try {
         this._findersLock.writeLock().lock();
         var2 = this._finders.remove(var1);
      } finally {
         this._findersLock.writeLock().unlock();
      }

      return var2;
   }

   public RoutingInfoFinder[] getFinders() {
      RoutingInfoFinder[] var1;
      try {
         this._findersLock.readLock().lock();
         var1 = (RoutingInfoFinder[])this._finders.toArray(new RoutingInfoFinder[this._finders.size()]);
      } finally {
         this._findersLock.readLock().unlock();
      }

      return var1;
   }
}
