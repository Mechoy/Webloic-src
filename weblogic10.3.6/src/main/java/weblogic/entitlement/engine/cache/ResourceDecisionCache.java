package weblogic.entitlement.engine.cache;

import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import weblogic.security.spi.Resource;
import weblogic.utils.collections.SecondChanceCacheMap;

public class ResourceDecisionCache {
   private SecondChanceCacheMap cache;
   private ResourceTreeMap resourceMap;
   private ThreadLocal keys;

   public ResourceDecisionCache(int var1) {
      this.cache = new SecondChanceCacheMap(var1);
      this.resourceMap = new ResourceTreeMap();
      this.keys = new ThreadLocal() {
         protected Object initialValue() {
            return new Key((Resource)null, (Subject)null);
         }
      };
   }

   public Object lookupDecision(Resource var1, Subject var2) {
      Key var3 = (Key)this.keys.get();
      var3.setValues(var1, var2);
      return this.cache.get(var3);
   }

   public int size() {
      return this.cache.size();
   }

   public void cacheDecision(Resource var1, Subject var2, Object var3) {
      Key var4 = new Key(var1, var2);
      synchronized(this.resourceMap) {
         Key var6 = (Key)this.cache.insert(var4, var3);
         if (var6 != null) {
            this.resourceMap.removeKey(var6);
         }

         if (var1 != null) {
            this.resourceMap.insertKey(var1, var4);
         }

      }
   }

   public void uncacheDecision(Resource var1, Subject var2) {
      Key var3 = new Key(var1, var2);
      synchronized(this.resourceMap) {
         this.cache.remove(var3);
         this.resourceMap.removeKey(var3);
      }
   }

   public void uncache(String var1) {
      Set var2;
      synchronized(this.resourceMap) {
         var2 = this.resourceMap.resourceChanged(var1);
      }

      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Key var4 = (Key)var3.next();
            this.cache.remove(var4);
         }
      }

   }
}
