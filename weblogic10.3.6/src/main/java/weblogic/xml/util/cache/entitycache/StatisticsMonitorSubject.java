package weblogic.xml.util.cache.entitycache;

class StatisticsMonitorSubject {
   Statistics stats = null;
   EntityCacheStats mBean = null;

   StatisticsMonitorSubject(Statistics var1, EntityCacheStats var2) {
      this.stats = var1;
      this.mBean = var2;
   }
}
