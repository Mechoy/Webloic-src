package weblogic.ejb.container.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.cache.QueryCacheElement;
import weblogic.ejb.container.cache.QueryCacheKey;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.manager.TTLManager;

public class QueryCachingHandler {
   private TTLManager ownerManager;
   private String finderIdentifierOrQuery;
   private Object[] arguments;
   private int maxElements = -1;
   private QueryCacheKey queryCacheKey;
   private Map queryCacheEntries;
   private boolean isDynamic = false;
   private Finder finder;

   public QueryCachingHandler(String var1, Object[] var2, Finder var3, TTLManager var4) {
      this.finderIdentifierOrQuery = var1;
      this.arguments = var2;
      this.finder = var3;
      this.ownerManager = var4;
      this.isDynamic = false;
      Class var5 = var3.getReturnClassType();
      if (Set.class.isAssignableFrom(var5)) {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 1);
      } else if (Collection.class.isAssignableFrom(var5)) {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 2);
      } else {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 3);
      }

      this.queryCacheEntries = new HashMap();
   }

   public QueryCachingHandler(String var1, int var2, Finder var3, TTLManager var4) {
      this.finderIdentifierOrQuery = var1;
      this.maxElements = var2;
      this.finder = var3;
      this.ownerManager = var4;
      this.isDynamic = true;
      Class var5 = var3.getReturnClassType();
      if (Set.class.isAssignableFrom(var5)) {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 1);
      } else if (Collection.class.isAssignableFrom(var5)) {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 2);
      } else {
         this.queryCacheKey = new QueryCacheKey(var1, var2, var4, 3);
      }

      this.queryCacheEntries = new HashMap();
   }

   public QueryCachingHandler(Finder var1) {
      this.finder = var1;
      this.queryCacheEntries = new HashMap();
   }

   public boolean isQueryCachingEnabledForFinder() {
      return this.finder.isQueryCachingEnabled();
   }

   public void addQueryCachingEntry(TTLManager var1, QueryCacheElement var2) {
      QueryCacheKey var3 = this.generateQueryCacheKey(var1);
      if (!var3.equals(this.queryCacheKey)) {
         var3.addSourceQuery(this.queryCacheKey);
         this.queryCacheKey.addDependentQuery(var3);
      }

      this.addToEntryCollection(var1, var3, var2);
   }

   public void addQueryCachingEntry(TTLManager var1, QueryCacheKey var2, QueryCacheElement var3) {
      if (this.finder.isQueryCachingEnabled()) {
         var2.addSourceQuery(this.queryCacheKey);
         this.queryCacheKey.addDependentQuery(var2);
      }

      this.addToEntryCollection(var1, var2, var3);
   }

   public void putInQueryCache() {
      Iterator var1 = this.queryCacheEntries.keySet().iterator();

      while(var1.hasNext()) {
         TTLManager var2 = (TTLManager)var1.next();
         Map var3 = (Map)this.queryCacheEntries.get(var2);
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            QueryCacheKey var5 = (QueryCacheKey)var4.next();
            var2.putInQueryCache(var5, (Collection)var3.get(var5));
         }
      }

   }

   protected QueryCacheKey generateQueryCacheKey(TTLManager var1) {
      if (var1.equals(this.ownerManager) && this.queryCacheKey != null) {
         return this.queryCacheKey;
      } else {
         return this.isDynamic ? new QueryCacheKey(this.finderIdentifierOrQuery, this.maxElements, var1, 0) : new QueryCacheKey(this.finderIdentifierOrQuery, this.arguments, var1, 0);
      }
   }

   private void addToEntryCollection(TTLManager var1, QueryCacheKey var2, QueryCacheElement var3) {
      Object var4 = (Map)this.queryCacheEntries.get(var1);
      if (var4 == null) {
         var4 = new HashMap();
         this.queryCacheEntries.put(var1, var4);
      }

      Object var5 = (List)((Map)var4).get(var2);
      if (var5 == null) {
         var5 = new ArrayList();
         ((Map)var4).put(var2, var5);
      }

      ((List)var5).add(var3);
   }
}
