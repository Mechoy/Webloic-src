package weblogic.cache.webapp;

import java.io.Serializable;
import weblogic.cache.CacheValue;

public interface CacheListener {
   String ATTRIBUTE = "weblogic.cache.CacheListener";

   void cacheUpdateOccurred(CacheEvent var1);

   void cacheAccessOccurred(CacheEvent var1);

   void cacheFlushOccurred(CacheEvent var1);

   public static class CacheEvent implements Serializable {
      private int time;
      private CacheValue value;
      private int size = -1;
      private int timeout = -1;
      private String scope;
      private String scopeType;
      private String name;
      private KeySet keySet;

      public void setTime(int var1) {
         this.time = var1;
      }

      public int getTime() {
         return this.time;
      }

      public void setValue(CacheValue var1) {
         this.value = var1;
      }

      public CacheValue getValue() {
         return this.value;
      }

      public void setSize(int var1) {
         this.size = var1;
      }

      public int getSize() {
         return this.size;
      }

      public void setScope(String var1) {
         this.scope = var1;
      }

      public String getScope() {
         return this.scope;
      }

      public void setScopeType(String var1) {
         this.scopeType = var1;
      }

      public String getScopeType() {
         return this.scopeType;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public String getName() {
         return this.name;
      }

      public void setKeySet(KeySet var1) {
         this.keySet = var1;
      }

      public KeySet getKeySet() {
         return this.keySet;
      }
   }
}
