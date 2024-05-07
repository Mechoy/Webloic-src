package weblogic.cache.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.CacheValue;
import weblogic.cache.RefWrapper;
import weblogic.cache.StallEvent;
import weblogic.cache.StallListener;
import weblogic.cache.utils.BubblingCache;

public class CacheSystem {
   protected boolean verbose = false;
   protected boolean debug = false;
   protected List scopes = new ArrayList();
   protected Map scopeMap = new HashMap();
   protected Set currentLocks = new HashSet();
   protected List listeners;
   protected boolean listening = true;
   protected int stallTime = 60000;
   protected List stallListeners = new ArrayList(1);

   private List getListeners() {
      if (this.listeners == null) {
         try {
            Object var1 = this.getValueFromAnyScope("weblogic.cache.CacheListener");
            if (var1 instanceof List) {
               this.listeners = (List)var1;
               this.listening = true;
            } else if (var1 != null) {
               ArrayList var2 = new ArrayList(1);
               var2.add(var1);
               this.listeners = var2;
               this.listening = true;
            } else {
               this.listeners = new ArrayList(0);
               this.listening = false;
            }
         } catch (CacheException var3) {
            this.listeners = new ArrayList(0);
            this.listening = false;
         }
      }

      return this.listeners;
   }

   protected void sendCacheUpdateEvent(CacheListener.CacheEvent var1) {
      int var2 = this.getListeners().size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((CacheListener)this.listeners.get(var3)).cacheUpdateOccurred(var1);
      }

   }

   protected void sendCacheAccessEvent(CacheListener.CacheEvent var1) {
      int var2 = this.getListeners().size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((CacheListener)this.listeners.get(var3)).cacheAccessOccurred(var1);
      }

   }

   protected void sendCacheFlushEvent(CacheListener.CacheEvent var1) {
      int var2 = this.getListeners().size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((CacheListener)this.listeners.get(var3)).cacheFlushOccurred(var1);
      }

   }

   public void setStallTime(int var1) {
      this.stallTime = var1;
   }

   public int getStallTime() {
      return this.stallTime;
   }

   public void addStallListener(StallListener var1) {
      this.stallListeners.add(var1);
   }

   public void removeStallListener(StallListener var1) {
      this.stallListeners.remove(var1);
   }

   protected CacheScope getScopeObject(String var1) {
      synchronized(this.scopeMap) {
         return (CacheScope)this.scopeMap.get(var1);
      }
   }

   protected Object getValueFromAnyScope(String var1) throws CacheException {
      synchronized(this.scopeMap) {
         Iterator var3 = this.scopes.iterator();

         Object var5;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            CacheScope var4 = (CacheScope)var3.next();
            var5 = var4.getAttribute(var1);
         } while(var5 == null);

         return var5;
      }
   }

   public boolean takeLock(String var1, String var2) throws CacheException {
      String var3 = var2 + ".lock";
      synchronized(this.getScopeObject(var1)) {
         if (this.getValueFromScope(var1, var3) != null) {
            if (this.verbose) {
               System.out.println("Not acquired (" + var1 + "): " + var3);
            }

            return false;
         }

         Object var5 = new Object();
         this.setValueInScope(var1, var3, var5);
         this.currentLocks.add(new CacheLock(var1, var2, var5));
      }

      if (this.verbose) {
         System.out.println("Taken (" + var1 + "): " + var3);
      }

      return true;
   }

   public void waitOnLock(String var1, String var2) throws CacheException {
      String var3 = var2 + ".lock";
      CacheScope var4 = this.getScopeObject(var1);
      Object var5 = this.getValueFromScope(var1, var3);
      if (var5 == null) {
         synchronized(var4) {
            var5 = this.getValueFromScope(var1, var3);
            if (var5 == null) {
               Object var7 = new Object();
               this.setValueInScope(var1, var3, var7);
               this.currentLocks.add(new CacheLock(var1, var2, var7));
               if (this.verbose) {
                  System.out.println("Taken (" + var1 + "): " + var3);
               }

               return;
            }
         }
      }

      while(var5 != null) {
         long var6 = System.currentTimeMillis();
         synchronized(var5) {
            try {
               var5.wait((long)this.stallTime);
            } catch (InterruptedException var12) {
            }
         }

         if ((double)(System.currentTimeMillis() - var6) > (double)this.stallTime * 0.99) {
            StallEvent var8 = new StallEvent(this.stallTime, var1, var2);
            Iterator var9 = this.stallListeners.iterator();

            while(var9.hasNext()) {
               StallListener var10 = (StallListener)var9.next();
               var10.stall(var8);
            }
         }

         var5 = this.getValueFromScope(var1, var3);
         if (var5 == null) {
            synchronized(var4) {
               var5 = this.getValueFromScope(var1, var3);
               if (var5 == null) {
                  Object var16 = new Object();
                  this.setValueInScope(var1, var3, var16);
                  this.currentLocks.add(new CacheLock(var1, var2, var16));
                  break;
               }
            }
         }
      }

      if (this.verbose) {
         System.out.println("Taken (" + var1 + "): " + var3);
      }

   }

   public void releaseLock(String var1, String var2) throws CacheException {
      String var3 = var2 + ".lock";
      CacheScope var4 = this.getScopeObject(var1);
      synchronized(var4) {
         Object var6 = this.getValueFromScope(var1, var3);
         this.removeValueInScope(var1, var3);
         this.currentLocks.remove(var2);
         if (var6 != null) {
            synchronized(var6) {
               var6.notifyAll();
            }
         }
      }

      if (this.verbose) {
         System.out.println("Released (" + var1 + "): " + var3);
      }

   }

   public void releaseAllLocks() throws CacheException {
      Iterator var1 = this.currentLocks.iterator();
      boolean var2 = false;

      while(var1.hasNext()) {
         CacheLock var3 = (CacheLock)var1.next();

         try {
            this.releaseLock(var3.scope, var3.key);
         } catch (CacheException var5) {
            var2 = true;
         }
      }

      if (var2) {
         throw new CacheException("Could not release all locks");
      }
   }

   public void registerScope(String var1, CacheScope var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Name of scope cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Scope cannot be null");
      } else {
         synchronized(this.scopeMap) {
            if (this.scopeMap.containsKey(var1)) {
               throw new IllegalArgumentException("Scope already registered under " + var1);
            } else {
               this.scopeMap.put(var1, var2);
               this.scopes.add(var2);
            }
         }
      }
   }

   public CacheValue getCurrentCache(String var1, String var2) throws CacheException {
      Object var3 = this.getValueFromScope(var1, var2);
      if (var3 != null && !(var3 instanceof RefWrapper)) {
         System.out.println(var3);
         throw new CacheException("You are using the same name for two caches that are not the same: " + var2);
      } else {
         RefWrapper var4 = (RefWrapper)var3;
         CacheValue var5 = (CacheValue)(var4 == null ? null : var4.get());
         return var5;
      }
   }

   public CacheValue getCache(String var1, String var2) throws CacheException {
      long var3 = 0L;
      if (this.listening) {
         var3 = System.currentTimeMillis();
      }

      boolean var5 = this.takeLock(var1, var2);
      if ("true".equals(this.getValueFromAnyScope("_cache_refresh"))) {
         if (!var5) {
            this.waitOnLock(var1, var2);
         }

         if (this.verbose) {
            System.out.println("_cache_refresh is active");
         }

         return null;
      } else {
         CacheValue var6;
         RefWrapper var7;
         for(var6 = this.getCurrentCache(var1, var2); !var5 && var6 == null; var6 = (CacheValue)(var7 == null ? null : var7.get())) {
            this.waitOnLock(var1, var2);
            var5 = true;
            var7 = (RefWrapper)this.getValueFromScope(var1, var2);
         }

         if (!var5) {
            return var6;
         } else if (var6 != null) {
            int var10 = var6.getTimeout();
            if (var10 != -1) {
               long var8 = var6.getCreated();
               if (System.currentTimeMillis() - var8 > (long)var10) {
                  if (this.verbose) {
                     System.out.println("Cache " + var1 + ":" + var2 + " timed out");
                  }

                  return null;
               }
            }

            if (var6.getFlush()) {
               return null;
            } else {
               this.releaseLock(var1, var2);
               if (this.verbose) {
                  System.out.println("Returning contents of cache");
               }

               if (this.listening) {
                  CacheListener.CacheEvent var11 = new CacheListener.CacheEvent();
                  var11.setTime((int)(System.currentTimeMillis() - var3));
                  var11.setScope(var1);
                  var11.setScopeType(this.getScopeObject(var1).getClass().getName());
                  var11.setName(var2);
                  this.sendCacheAccessEvent(var11);
               }

               return var6;
            }
         } else {
            if (this.verbose) {
               System.out.println("Cache " + var1 + ":" + var2 + " has nothing in it");
            }

            return null;
         }
      }
   }

   public void flushCache(String var1, String var2) throws CacheException {
      this.removeValueInScope(var1, var2);
      if (this.listening) {
         CacheListener.CacheEvent var3 = new CacheListener.CacheEvent();
         var3.setScope(var1);
         var3.setScopeType(this.getScopeObject(var1).getClass().getName());
         var3.setName(var2);
         this.sendCacheFlushEvent(var3);
      }

   }

   public void setCache(String var1, String var2, CacheValue var3, int var4) throws CacheException {
      if (var3 == null) {
         this.removeValueInScope(var1, var2);
      } else {
         if (var3.getTimeout() != -1) {
            var3.setCreated(System.currentTimeMillis());
         }

         if (this.verbose) {
            System.out.println("Cache " + var1 + ":" + var2 + " is now set");
         }

         this.setValueInScope(var1, var2, new RefWrapper(var3));
      }

      this.releaseLock(var1, var2);
      if (this.listening) {
         CacheListener.CacheEvent var5 = new CacheListener.CacheEvent();
         var5.setTime(var4);
         var5.setScope(var1);
         var5.setScopeType(this.getScopeObject(var1).getClass().getName());
         var5.setName(var2);
         var5.setValue(var3);
         this.sendCacheUpdateEvent(var5);
      }

   }

   public CacheValue getCurrentCache(String var1, String var2, int var3, KeySet var4) throws CacheException {
      String var5 = var4.getKey();
      this.waitOnLock(var1, var2);

      Object var6;
      try {
         var6 = (Map)this.getValueFromScope(var1, var2);
      } catch (ClassCastException var9) {
         var6 = null;
      }

      if (var6 == null) {
         if (var3 == -1) {
            var6 = Collections.synchronizedMap(new HashMap());
         } else {
            var6 = new BubblingCache(var3);
         }

         this.setValueInScope(var1, var2, var6);
      }

      this.releaseLock(var1, var2);
      RefWrapper var7 = (RefWrapper)((Map)var6).get(var5);
      CacheValue var8 = (CacheValue)(var7 == null ? null : var7.get());
      return var8;
   }

   public CacheValue getCache(String var1, String var2, int var3, KeySet var4) throws CacheException {
      String var5 = var4.getKey();
      long var6 = 0L;
      if (this.listening) {
         var6 = System.currentTimeMillis();
      }

      String var8 = var2 + '\u0000' + var5;
      boolean var9 = this.takeLock(var1, var8);
      if ("true".equals(this.getValueFromAnyScope("_cache_refresh"))) {
         if (!var9) {
            this.waitOnLock(var1, var8);
         }

         return null;
      } else {
         CacheValue var10;
         for(var10 = this.getCurrentCache(var1, var2, var3, var4); !var9 && var10 == null; var10 = this.getCurrentCache(var1, var2, var3, var4)) {
            this.waitOnLock(var1, var8);
            var9 = true;
         }

         if (!var9) {
            return var10;
         } else if (var10 != null) {
            int var11 = var10.getTimeout();
            if (var11 != -1) {
               long var12 = var10.getCreated();
               if (System.currentTimeMillis() - var12 > (long)var11) {
                  return null;
               }
            }

            if (var10.getFlush()) {
               return null;
            } else {
               this.releaseLock(var1, var8);
               if (this.listening) {
                  CacheListener.CacheEvent var14 = new CacheListener.CacheEvent();
                  var14.setTime((int)(System.currentTimeMillis() - var6));
                  var14.setScope(var1);
                  var14.setScopeType(this.getScopeObject(var1).getClass().getName());
                  var14.setName(var2);
                  var14.setKeySet(var4);
                  this.sendCacheAccessEvent(var14);
               }

               return var10;
            }
         } else {
            return null;
         }
      }
   }

   public void setCache(String var1, String var2, int var3, KeySet var4, CacheValue var5, int var6) throws CacheException {
      if (var3 != 0 && var3 >= -1) {
         String var7 = var4.getKey();
         this.waitOnLock(var1, var2);

         Object var8;
         try {
            var8 = (Map)this.getValueFromScope(var1, var2);
         } catch (ClassCastException var11) {
            var8 = null;
         }

         if (var8 == null) {
            if (var3 == -1) {
               var8 = Collections.synchronizedMap(new HashMap());
            } else {
               var8 = new BubblingCache(var3);
            }

            this.setValueInScope(var1, var2, var8);
         }

         if (var5 == null) {
            ((Map)var8).remove(var7);
         } else {
            if (var5.getTimeout() != -1) {
               var5.setCreated(System.currentTimeMillis());
            }

            ((Map)var8).put(var7, new RefWrapper(var5));
         }

         this.setValueInScope(var1, var2, var8);
         this.releaseLock(var1, var2);
         String var9 = var2 + '\u0000' + var7;
         this.releaseLock(var1, var9);
         if (this.listening) {
            CacheListener.CacheEvent var10 = new CacheListener.CacheEvent();
            var10.setTime(var6);
            var10.setScope(var1);
            var10.setScopeType(this.getScopeObject(var1).getClass().getName());
            var10.setName(var2);
            var10.setKeySet(var4);
            var10.setValue(var5);
            var10.setSize(var3);
            this.sendCacheUpdateEvent(var10);
         }

      } else {
         throw new CacheException("Invalid value for cache size: " + var3);
      }
   }

   public void flushCache(String var1, String var2, KeySet var3) throws CacheException {
      String var4 = var3.getKey();
      this.waitOnLock(var1, var2);

      Map var5;
      try {
         var5 = (Map)this.getValueFromScope(var1, var2);
      } catch (ClassCastException var7) {
         var5 = null;
      }

      if (var5 != null) {
         this.releaseLock(var1, var2);
         var5.remove(var4);
         if (this.listening) {
            CacheListener.CacheEvent var6 = new CacheListener.CacheEvent();
            var6.setScope(var1);
            var6.setScopeType(this.getScopeObject(var1).getClass().getName());
            var6.setName(var2);
            var6.setKeySet(var3);
            this.sendCacheFlushEvent(var6);
         }

      }
   }

   public Object getValueFromScope(String var1, String var2) throws CacheException {
      if (var1.equals("any")) {
         return this.getValueFromAnyScope(var2);
      } else {
         CacheScope var3;
         synchronized(this.scopeMap) {
            var3 = (CacheScope)this.scopeMap.get(var1);
         }

         if (var3 == null) {
            throw new CacheException("Could not find cache scope: " + var1);
         } else {
            return var3.getAttribute(var2);
         }
      }
   }

   public void setValueInScope(String var1, String var2, Object var3) throws CacheException {
      CacheScope var4;
      synchronized(this.scopeMap) {
         var4 = (CacheScope)this.scopeMap.get(var1);
      }

      if (var4 == null) {
         throw new CacheException("Could not find cache scope: " + var1);
      } else {
         var4.setAttribute(var2, var3);
      }
   }

   public void removeValueInScope(String var1, String var2) throws CacheException {
      CacheScope var3;
      synchronized(this.scopeMap) {
         var3 = (CacheScope)this.scopeMap.get(var1);
      }

      if (var3 == null) {
         throw new CacheException("Could not find cache scope: " + var1);
      } else {
         var3.removeAttribute(var2);
      }
   }

   class CacheLock {
      String key;
      String scope;
      Object lock;

      CacheLock(String var2, String var3, Object var4) {
         this.scope = var2;
         this.key = var3;
         this.lock = var4;
      }

      public int hashCode() {
         return this.key == null ? 0 : this.key.hashCode();
      }

      public boolean equals(Object var1) {
         return this.key == ((CacheLock)var1).key;
      }
   }
}
