package weblogic.ejb.container.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.monitoring.QueryCacheRuntimeMBeanImpl;
import weblogic.ejb.container.persistence.spi.EloWrapper;
import weblogic.ejb.container.persistence.spi.EoWrapper;
import weblogic.ejb20.utils.OrderedSet;
import weblogic.management.ManagementException;
import weblogic.management.runtime.QueryCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public class QueryCache implements weblogic.ejb.container.interfaces.QueryCache {
   private static final DebugLogger debugLogger;
   private ConcurrentHashMap queryAxis;
   private ConcurrentHashMap primaryKeyAxis;
   private LRUList lrulist;
   private int capacity;
   private QueryCacheRuntimeMBeanImpl rtmMBean;
   private String name;

   public QueryCache(String var1, int var2) {
      this.name = var1;
      this.capacity = var2;
      this.queryAxis = new ConcurrentHashMap();
      this.primaryKeyAxis = new ConcurrentHashMap();
      this.lrulist = new LRUList(var1, var2);
      if (debugLogger.isDebugEnabled()) {
         debug(var1, "Capacity: " + var2);
      }

   }

   public QueryCacheRuntimeMBean createRuntimeMBean(String var1, RuntimeMBean var2) throws ManagementException {
      this.rtmMBean = new QueryCacheRuntimeMBeanImpl(var1, var2);
      return this.rtmMBean;
   }

   public void setRuntimeMBean(QueryCacheRuntimeMBean var1) {
      this.rtmMBean = (QueryCacheRuntimeMBeanImpl)var1;
   }

   public Object get(Object var1, QueryCacheKey var2, boolean var3, boolean var4) throws InternalException {
      this.rtmMBean.incrementCacheAccessCount();
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "Get: " + var2 + " at: " + System.currentTimeMillis());
      }

      QueryAxisNode var5 = (QueryAxisNode)this.queryAxis.get(var2);
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "QueryAxis size: " + this.queryAxis.size());
      }

      if (var5 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug(this.name, "Cache miss: Not found: " + var2);
         }

         this.rtmMBean.incrementCacheMissCount();
         if (debugLogger.isDebugEnabled()) {
            debug(this.name, "Got null QueryAxisNode for " + var2);
         }

         return null;
      } else {
         Object var6 = var5.get(var1, var3, var4);
         if (var6 == null) {
            var5.delink();
            this.lrulist.remove(var5);
            if (debugLogger.isDebugEnabled()) {
               debug(this.name, "Cache miss: Timed out: " + var2);
            }

            if (debugLogger.isDebugEnabled()) {
               debug(this.name, "Got null from QueryAxisNode for " + var2);
            }

            return null;
         } else {
            this.lrulist.addMRU(var5);
            this.rtmMBean.incrementCacheHitCount();
            if (debugLogger.isDebugEnabled()) {
               debug(this.name, "Returning " + var6.getClass() + " for " + var2);
            }

            return var6;
         }
      }
   }

   public boolean put(QueryCacheKey var1, Collection var2) {
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "Put(coll): " + var1 + " size: " + var2.size() + " at: " + System.currentTimeMillis());
      }

      QueryAxisNode var3 = new QueryAxisNode(var1, this);
      QueryAxisNode var4 = var3.set(var2);
      this.lrulist.addMRU(var4);
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "Putting collection for " + var1 + " " + (var3 == var4));
      }

      return var3 == var4;
   }

   public boolean put(QueryCacheKey var1, QueryCacheElement var2) {
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "Put: " + var1 + " at: " + System.currentTimeMillis());
      }

      QueryAxisNode var3 = new QueryAxisNode(var1, this);
      QueryAxisNode var4 = var3.set(var2);
      this.lrulist.addMRU(var4);
      if (debugLogger.isDebugEnabled()) {
         debug(this.name, "Putting singleton for " + var1 + " " + (var3 == var4));
      }

      return var3 == var4;
   }

   public void invalidate(CacheKey var1) {
      this.invalidate(new QueryCacheElement(var1));
   }

   public void invalidateAll() {
      Iterator var1 = this.primaryKeyAxis.keySet().iterator();

      while(var1.hasNext()) {
         this.invalidate((QueryCacheElement)var1.next());
      }

   }

   protected void invalidate(QueryCacheKey var1) {
      QueryAxisNode var2 = (QueryAxisNode)this.queryAxis.remove(var1);
      if (var2 != null) {
         var2.delink();
         this.lrulist.remove(var2);
      }

   }

   protected boolean enrollQuery(Object var1, QueryCacheKey var2) throws InternalException {
      QueryAxisNode var3 = (QueryAxisNode)this.queryAxis.get(var2);
      if (var3 == null) {
         return false;
      } else {
         boolean var4 = var3.enroll(var1);
         if (var4) {
            this.lrulist.addMRU(var3);
         } else {
            var3.delink();
            this.lrulist.remove(var3);
         }

         if (debugLogger.isDebugEnabled()) {
            debug(this.name, "Enrolling for " + var2 + ": " + var4);
         }

         return var4;
      }
   }

   private void invalidate(QueryCacheElement var1) {
      PrimaryKeyAxisNode var2 = (PrimaryKeyAxisNode)this.primaryKeyAxis.remove(var1);
      if (var2 != null) {
         var2.clearAll();
      }
   }

   private static void debug(String var0, String var1) {
      debugLogger.debug("[QueryCache:" + var0 + "] " + var1);
   }

   static {
      debugLogger = EJBDebugService.cachingLogger;
   }

   private static class LRUList {
      private QueryAxisNode mostRecent;
      private QueryAxisNode leastRecent;
      private String name;
      private final int capacity;
      private int size;

      public LRUList(String var1, int var2) {
         this.capacity = var2;
         this.size = 0;
         this.name = var1;
         if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.name, "Capacity is: " + this.capacity);
         }

      }

      public int size() {
         return this.size;
      }

      public void addMRU(QueryAxisNode var1) {
         if (this.capacity != 0) {
            QueryAxisNode var2 = null;
            synchronized(this) {
               if (var1 == this.mostRecent) {
                  return;
               }

               if (this.mostRecent == null) {
                  this.mostRecent = var1;
                  this.mostRecent.older = this.leastRecent;
                  this.mostRecent.newer = null;
                  this.leastRecent = this.mostRecent;
                  this.leastRecent.newer = this.mostRecent;
                  this.leastRecent.older = null;
                  this.size = 1;
                  return;
               }

               if (var1 == this.mostRecent || var1 == this.leastRecent || var1.older != null && var1.newer != null) {
                  if (var1 == this.leastRecent) {
                     this.leastRecent = var1.newer;
                     this.leastRecent.older = null;
                     var1.newer = null;
                  } else {
                     var1.newer.older = var1.older;
                     var1.older.newer = var1.newer;
                     var1.newer = null;
                     var1.older = null;
                  }
               } else if (++this.size > this.capacity) {
                  var2 = this.shrink();
               }

               var1.older = this.mostRecent;
               this.mostRecent.newer = var1;
               this.mostRecent = var1;
            }

            if (var2 != null) {
               var2.delink();
            }

         }
      }

      public void remove(QueryAxisNode var1) {
         if (this.capacity != 0) {
            synchronized(this) {
               if (var1 == this.mostRecent || var1 == this.leastRecent || var1.older != null && var1.newer != null) {
                  if (var1.newer != null) {
                     var1.newer.older = var1.older;
                  }

                  if (var1.older != null) {
                     var1.older.newer = var1.newer;
                  }

                  if (var1 == this.mostRecent) {
                     this.mostRecent = var1.older;
                     if (this.mostRecent != null) {
                        this.mostRecent.newer = null;
                     }
                  }

                  if (var1 == this.leastRecent) {
                     this.leastRecent = var1.newer;
                     if (this.leastRecent != null) {
                        this.leastRecent.older = null;
                     }
                  }

                  var1.newer = null;
                  var1.older = null;
                  --this.size;
               }

            }
         }
      }

      private QueryAxisNode shrink() {
         QueryAxisNode var1 = this.leastRecent;
         if (this.leastRecent.newer != null) {
            this.leastRecent.newer.older = null;
         } else if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.name, "No newer");
         }

         this.leastRecent = this.leastRecent.newer;
         this.leastRecent.older = null;
         --this.size;
         if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.name, "LRUList size is " + this.size);
         }

         return var1;
      }
   }

   private static class QueryElementNode {
      public QueryCacheKey qckey;
      public QueryCacheElement qcelement;
      public QueryElementNode up;
      public QueryElementNode left;
      public QueryElementNode right;
      public PrimaryKeyAxisNode qcelnode;

      public QueryElementNode(QueryCacheKey var1, QueryCacheElement var2) {
         this.qckey = var1;
         this.qcelement = var2;
      }
   }

   private static class PrimaryKeyAxisNode {
      private QueryElementNode head;
      private QueryElementNode tail;
      private QueryCache cache;
      private QueryCacheElement key;
      private int size = 0;

      public PrimaryKeyAxisNode(QueryCacheElement var1, QueryCache var2) {
         this.key = var1;
         this.cache = var2;
      }

      public synchronized void add(QueryElementNode var1) {
         try {
            if (this.head != null) {
               this.tail.right = var1;
               var1.left = this.tail;
               this.tail = var1;
               this.tail.right = null;
               ++this.size;
               return;
            }

            this.head = var1;
            this.tail = this.head;
            this.tail.left = this.head;
            this.tail.right = null;
            var1.left = null;
            var1.right = null;
            ++this.size;
         } finally {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               this.validate("add");
            }

         }

      }

      public void clearAll() {
         ArrayList var1 = new ArrayList();
         synchronized(this) {
            try {
               for(QueryElementNode var3 = this.head; var3 != null; var3 = var3.right) {
                  QueryAxisNode var4 = (QueryAxisNode)this.cache.queryAxis.remove(var3.qckey);
                  if (var4 != null) {
                     var1.add(var4);
                  }
               }

               this.size = 0;
               this.head = null;
            } finally {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  this.validate("clearAll");
               }

            }
         }

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            QueryAxisNode var12 = (QueryAxisNode)var1.get(var2);
            var12.delink();
            this.cache.lrulist.remove(var12);
         }

      }

      public void delink(QueryElementNode var1) {
         synchronized(this) {
            try {
               if (var1.left != null) {
                  var1.left.right = var1.right;
               } else {
                  this.head = var1.right;
               }

               if (var1.right != null) {
                  var1.right.left = var1.left;
               } else {
                  this.tail = var1.left;
               }

               var1.left = null;
               var1.right = null;
               var1.qcelnode = null;
               --this.size;
            } finally {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  this.validate("delink");
               }

            }
         }

         if (this.size == 0) {
            synchronized(this.cache.primaryKeyAxis) {
               Object var3 = this.cache.primaryKeyAxis.remove(this.key);
               if (var3 != null && var3 != this) {
                  this.cache.primaryKeyAxis.put(this.key, var3);
               }
            }
         }

      }

      private void validate(String var1) {
         int var2 = 0;

         for(QueryElementNode var3 = this.head; var3 != null; var3 = var3.right) {
            ++var2;
         }

         if (var2 != this.size) {
            StringBuffer var5 = new StringBuffer();
            var5.append("PrimaryKeyAxisNode." + var1 + " FAIL: got " + var2 + ", expected " + this.size);

            for(QueryElementNode var4 = this.head; var4 != null; var4 = var4.right) {
               var5.append(var4.qckey + ", " + var4.qcelement);
            }

            QueryCache.debug(this.cache.name, var5.toString());
         }

      }
   }

   private static class QueryAxisNode {
      public QueryCacheKey key;
      private QueryElementNode head;
      private Lock rlock;
      private Lock wlock;
      private QueryCache cache;
      private boolean containsEmptyResult = false;
      private int size;
      private long expirationTime = 0L;
      private TTLManager manager;
      private Set sourceQueries;
      private Set destinationQueries;
      private Set dependentQueries;
      public QueryAxisNode older;
      public QueryAxisNode newer;

      public QueryAxisNode(QueryCacheKey var1, QueryCache var2) {
         this.key = var1;
         this.cache = var2;
         this.manager = var1.getOwnerManager();
         this.sourceQueries = var1.getSourceQueries();
         this.destinationQueries = var1.getDestinationQueries();
         this.dependentQueries = var1.getDependentQueries();
         ReentrantReadWriteLock var3 = new ReentrantReadWriteLock();
         this.rlock = var3.readLock();
         this.wlock = var3.writeLock();
         if (var1.getTimeoutMillis() != 0) {
            this.expirationTime = System.currentTimeMillis() + (long)var1.getTimeoutMillis();
            if (QueryCache.debugLogger.isDebugEnabled()) {
               QueryCache.debug(this.cache.name, "Expiration of: " + this.key + " at: " + this.expirationTime);
            }
         }

      }

      public QueryAxisNode set(QueryCacheElement var1) {
         this.wlock.lock();

         try {
            QueryAxisNode var2 = (QueryAxisNode)this.cache.queryAxis.putIfAbsent(this.key, this);
            if (var2 != null && var2 != this && !var2.hasTimedOut()) {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  QueryCache.debug(this.cache.name, "Cache put fail: Already exists: " + this.key);
               }

               if (this.sourceQueries != null) {
                  if (var2.sourceQueries == null) {
                     var2.sourceQueries = this.sourceQueries;
                  } else {
                     var2.sourceQueries.addAll(this.sourceQueries);
                  }
               }

               QueryAxisNode var9 = var2;
               return var9;
            }

            this.head = new QueryElementNode(this.key, var1);
            if (var1.isInvalidatable()) {
               PrimaryKeyAxisNode var3 = new PrimaryKeyAxisNode(this.head.qcelement, this.cache);
               PrimaryKeyAxisNode var4 = (PrimaryKeyAxisNode)this.cache.primaryKeyAxis.putIfAbsent(this.head.qcelement, var3);
               if (var4 != null) {
                  var3 = var4;
               }

               this.head.qcelnode = var3;
               var3.add(this.head);
            }

            this.size = 1;
         } finally {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               this.validate("set(QueryCacheElement)");
            }

            this.wlock.unlock();
         }

         if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.cache.name, "Size: " + this.cache.lrulist.size());
         }

         this.cache.rtmMBean.incrementTotalCachedQueriesCount();
         return this;
      }

      public QueryAxisNode set(Collection var1) {
         this.wlock.lock();

         try {
            QueryAxisNode var2 = (QueryAxisNode)this.cache.queryAxis.putIfAbsent(this.key, this);
            QueryAxisNode var15;
            if (var2 != null && var2 != this) {
               if (!var2.hasTimedOut()) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     QueryCache.debug(this.cache.name, "Cache put fail: Already exists: " + this.key);
                  }

                  if (!var2.containsEmptyResult && this.sourceQueries != null) {
                     if (var2.sourceQueries == null) {
                        var2.sourceQueries = this.sourceQueries;
                     } else {
                        var2.sourceQueries.addAll(this.sourceQueries);
                     }
                  }

                  QueryAxisNode var16 = var2;
                  return var16;
               }

               Class var3 = Thread.currentThread().getClass();
               if (var3.getSimpleName().contains("EagerRefresh")) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     QueryCache.debug(this.cache.name, "Cache put skip: EagerRefresh: " + this.key);
                  }

                  var15 = var2;
                  return var15;
               }

               if (QueryCache.debugLogger.isDebugEnabled()) {
                  QueryCache.debug(this.cache.name, "Replacing timed out result: " + this.key);
               }

               var2.delink();
               QueryAxisNode var10000 = (QueryAxisNode)this.cache.queryAxis.putIfAbsent(this.key, this);
            }

            this.size = 0;
            QueryElementNode var14 = null;
            this.head = null;
            if (var1.size() == 0) {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  QueryCache.debug(this.cache.name, "Setting empty result: " + this.key);
               }

               this.containsEmptyResult = true;
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  QueryCache.debug(this.cache.name, "Size: " + this.cache.lrulist.size());
               }

               this.cache.rtmMBean.incrementTotalCachedQueriesCount();
               var15 = this;
               return var15;
            }

            Iterator var4 = var1.iterator();
            HashMap var5 = new HashMap();

            for(boolean var6 = false; var4.hasNext(); ++this.size) {
               QueryCacheElement var7 = (QueryCacheElement)var4.next();
               if (this.head == null) {
                  this.head = new QueryElementNode(this.key, var7);
                  var14 = this.head;
               } else {
                  var14.up = new QueryElementNode(this.key, var7);
                  var14 = var14.up;
               }

               if (this.key.getReturnType() == 3 && var7.isIncludable()) {
                  if (var6) {
                     throw new AssertionError("Multiple includables found for singleton return query ");
                  }

                  var6 = true;
               }

               if (!var5.containsKey(var7) && var7.isInvalidatable()) {
                  PrimaryKeyAxisNode var8 = new PrimaryKeyAxisNode(var14.qcelement, this.cache);
                  PrimaryKeyAxisNode var9 = (PrimaryKeyAxisNode)this.cache.primaryKeyAxis.putIfAbsent(var14.qcelement, var8);
                  if (var9 != null) {
                     var8 = var9;
                  }

                  var14.qcelnode = var8;
                  var8.add(var14);
                  var5.put(var7, var7);
               }
            }
         } finally {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               this.validate("set(Collection)");
            }

            this.wlock.unlock();
         }

         if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.cache.name, "Size: " + this.cache.lrulist.size());
         }

         this.cache.rtmMBean.incrementTotalCachedQueriesCount();
         return this;
      }

      public Object get(Object var1, boolean var2, boolean var3) throws InternalException {
         if (this.hasTimedOut()) {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               QueryCache.debug(this.cache.name, "Cache miss: Timeout: " + this.key);
            }

            this.cache.rtmMBean.incrementCacheMissByTimeoutCount();
            return null;
         } else {
            this.rlock.lock();

            Object var15;
            try {
               Object var4;
               if (!this.containsEmptyResult && this.head == null) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     QueryCache.debug(this.cache.name, "Cache miss: Invalidatad or evicted: " + this.key);
                  }

                  var4 = null;
                  return var4;
               }

               var4 = null;
               Object var5;
               if (this.key.getReturnType() == 3) {
                  var4 = this.getFirstIncludable().getReturnValue(var1, var2);
                  if (var4 == null) {
                     if (QueryCache.debugLogger.isDebugEnabled()) {
                        QueryCache.debug(this.cache.name, "Cache miss: Single-valued return is null: " + this.key);
                     }

                     this.cache.rtmMBean.incrementCacheMissByBeanEvictionCount();
                  }

                  var5 = var4;
                  return var5;
               }

               var5 = null;
               if (this.key.getReturnType() == 1) {
                  var5 = new OrderedSet();
               } else {
                  var5 = new ArrayList(this.size);
               }

               if (this.containsEmptyResult) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     QueryCache.debug(this.cache.name, "Returning empty result: " + this.key);
                  }

                  Object var14 = var5;
                  return var14;
               }

               QueryElementNode var6 = this.head;

               for(int var7 = 0; var7 < this.size; ++var7) {
                  if (var6.qcelement.isIncludable()) {
                     Object var8 = var6.qcelement.getReturnValue(var1, var2);
                     if (var8 == null) {
                        if (QueryCache.debugLogger.isDebugEnabled()) {
                           QueryCache.debug(this.cache.name, "Cache miss: Multi-valued return at " + var7 + " is null: " + this.key);
                        }

                        this.cache.rtmMBean.incrementCacheMissByBeanEvictionCount();
                        Object var9 = null;
                        return var9;
                     }

                     if (this.key.getReturnType() == 1) {
                        if (var2) {
                           var8 = new EloWrapper((EJBLocalObject)var8);
                        } else {
                           var8 = new EoWrapper((EJBObject)var8);
                        }
                     }

                     if (var8 == QueryCache.NULL_VALUE) {
                        ((Collection)var5).add((Object)null);
                     } else {
                        ((Collection)var5).add(var8);
                     }
                  }

                  var6 = var6.up;
               }

               if (((Collection)var5).isEmpty()) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     StringBuffer var16 = new StringBuffer();
                     var6 = this.head;

                     for(int var17 = 0; var17 < this.size; ++var17) {
                        var16.append("For key: ").append(this.key).append(", Unincludable: ").append(var6.qcelement).append("\n");
                        var6 = var6.up;
                     }

                     QueryCache.debug(this.cache.name, var16.toString());
                  }

                  throw new AssertionError("multi-valued get, but no includable results");
               }

               if (this.enrollDestinationQueries(var1)) {
                  if (!this.enrollDependentQueries(var1)) {
                     if (QueryCache.debugLogger.isDebugEnabled()) {
                        QueryCache.debug(this.cache.name, "Cache miss: peer query enrollment failure: " + this.key);
                     }

                     this.cache.rtmMBean.incrementCacheMissByDependentQueryMissCount();
                     var15 = null;
                     return var15;
                  }

                  var15 = var5;
                  return var15;
               }

               if (QueryCache.debugLogger.isDebugEnabled()) {
                  QueryCache.debug(this.cache.name, "Cache miss: destination query enrollment failure: " + this.key);
               }

               this.cache.rtmMBean.incrementCacheMissByRelatedQueryMissCount();
               var15 = null;
            } finally {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  this.validate("get");
               }

               this.rlock.unlock();
            }

            return var15;
         }
      }

      public boolean enroll(Object var1) throws InternalException {
         if (this.hasTimedOut()) {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               QueryCache.debug(this.cache.name, "Cache enroll fail: Timeout for " + this.key);
            }

            this.cache.rtmMBean.incrementCacheMissByTimeoutCount();
            return false;
         } else {
            this.rlock.lock();

            boolean var10;
            try {
               boolean var9;
               if (this.head == null) {
                  if (QueryCache.debugLogger.isDebugEnabled()) {
                     QueryCache.debug(this.cache.name, "Cache enroll fail: null head for " + this.key);
                  }

                  var9 = false;
                  return var9;
               }

               if (this.key.getReturnType() == 3) {
                  if (!this.getFirstIncludable().enroll(var1)) {
                     if (QueryCache.debugLogger.isDebugEnabled()) {
                        QueryCache.debug(this.cache.name, "Cache single-value enroll fail: for " + this.key);
                     }

                     this.cache.rtmMBean.incrementCacheMissByBeanEvictionCount();
                     var9 = false;
                     return var9;
                  }

                  var9 = true;
                  return var9;
               }

               QueryElementNode var2 = this.head;

               for(int var3 = 0; var3 < this.size; ++var3) {
                  if (!var2.qcelement.enroll(var1)) {
                     if (QueryCache.debugLogger.isDebugEnabled()) {
                        QueryCache.debug(this.cache.name, "Cache multi-value enroll fail: for " + this.key);
                     }

                     this.cache.rtmMBean.incrementCacheMissByBeanEvictionCount();
                     boolean var4 = false;
                     return var4;
                  }

                  var2 = var2.up;
               }

               if (!this.enrollDestinationQueries(var1)) {
                  this.cache.rtmMBean.incrementCacheMissByRelatedQueryMissCount();
                  var10 = false;
                  return var10;
               }

               if (!this.enrollDependentQueries(var1)) {
                  this.cache.rtmMBean.incrementCacheMissByDependentQueryMissCount();
                  var10 = false;
                  return var10;
               }

               var10 = true;
            } finally {
               if (QueryCache.debugLogger.isDebugEnabled()) {
                  this.validate("get");
               }

               this.rlock.unlock();
            }

            return var10;
         }
      }

      public void delink() {
         if (QueryCache.debugLogger.isDebugEnabled()) {
            QueryCache.debug(this.cache.name, "Size: " + this.cache.lrulist.size());
         }

         this.cache.rtmMBean.decrementTotalCachedQueriesCount();
         this.wlock.lock();

         try {
            synchronized(this.cache.queryAxis) {
               Object var2 = this.cache.queryAxis.remove(this.key);
               if (var2 != null && var2 != this) {
                  this.cache.queryAxis.put(this.key, var2);
               }
            }

            for(QueryElementNode var1 = this.head; var1 != null; var1 = var1.up) {
               if (var1.qcelnode != null) {
                  var1.qcelnode.delink(var1);
               }
            }

            this.head = null;
            this.size = 0;
         } finally {
            if (QueryCache.debugLogger.isDebugEnabled()) {
               this.validate("delink");
            }

            this.wlock.unlock();
         }

         this.invalidateSourceQueries();
         this.invalidateDependentQueries();
      }

      private boolean enrollDestinationQueries(Object var1) throws InternalException {
         if (this.destinationQueries != null) {
            Iterator var2 = this.destinationQueries.iterator();

            while(var2.hasNext()) {
               QueryCacheKey var3 = (QueryCacheKey)var2.next();
               QueryCache var4 = (QueryCache)var3.getOwnerManager().getQueryCache();
               if (!var4.enrollQuery(var1, var3)) {
                  return false;
               }
            }
         }

         return true;
      }

      private boolean enrollDependentQueries(Object var1) throws InternalException {
         if (this.dependentQueries != null) {
            Iterator var2 = this.dependentQueries.iterator();

            while(var2.hasNext()) {
               QueryCacheKey var3 = (QueryCacheKey)var2.next();
               QueryCache var4 = (QueryCache)var3.getOwnerManager().getQueryCache();
               if (!var4.enrollQuery(var1, var3)) {
                  return false;
               }
            }
         }

         return true;
      }

      private void invalidateSourceQueries() {
         if (this.sourceQueries != null) {
            Iterator var1 = this.sourceQueries.iterator();

            while(var1.hasNext()) {
               QueryCacheKey var2 = (QueryCacheKey)var1.next();
               QueryCache var3 = (QueryCache)var2.getOwnerManager().getQueryCache();
               var3.invalidate(var2);
            }
         }

      }

      private void invalidateDependentQueries() {
         if (this.dependentQueries != null) {
            Iterator var1 = this.dependentQueries.iterator();

            while(var1.hasNext()) {
               QueryCacheKey var2 = (QueryCacheKey)var1.next();
               QueryCache var3 = (QueryCache)var2.getOwnerManager().getQueryCache();
               var3.invalidate(var2);
            }
         }

      }

      private boolean hasTimedOut() {
         return this.expirationTime != 0L && System.currentTimeMillis() > this.expirationTime;
      }

      private QueryCacheElement getFirstIncludable() {
         for(QueryElementNode var1 = this.head; var1 != null; var1 = var1.up) {
            if (var1.qcelement.isIncludable()) {
               return var1.qcelement;
            }
         }

         return null;
      }

      private void validate(String var1) {
         int var2 = 0;

         for(QueryElementNode var3 = this.head; var3 != null; var3 = var3.up) {
            ++var2;
         }

         if (var2 != this.size) {
            StringBuffer var5 = new StringBuffer();
            var5.append("QueryAxisNode.").append(var1).append(" FAIL: got ").append(var2).append(", expected ").append(this.size).append(", capacity ").append(this.cache.capacity).append("\n");

            for(QueryElementNode var4 = this.head; var4 != null; var4 = var4.up) {
               var5.append(var4.qckey).append(", ").append(var4.qcelement);
            }

            QueryCache.debug(this.cache.name, var5.toString());
         }

      }
   }
}
