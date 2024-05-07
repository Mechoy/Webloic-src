package weblogic.jms.forwarder.dd.internal;

import java.util.HashMap;
import java.util.Map;
import weblogic.jms.cache.Cache;

public class DDMembersCacheManagerImpl {
   public static DDMembersCacheManagerImpl ddMembersCacheManager = new DDMembersCacheManagerImpl();
   private static Map ddCacheMap = new HashMap();

   private DDMembersCacheManagerImpl() {
   }

   public synchronized void addDDMembersCache(Cache var1) {
      ddCacheMap.put(var1.getName(), var1);
   }

   public synchronized Cache getDDMembersCache(String var1) {
      return (Cache)ddCacheMap.get(var1);
   }
}
