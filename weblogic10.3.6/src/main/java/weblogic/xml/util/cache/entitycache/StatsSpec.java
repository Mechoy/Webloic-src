package weblogic.xml.util.cache.entitycache;

class StatsSpec {
   Statistics stats = null;
   boolean isPersistent = false;

   StatsSpec(Statistics var1, boolean var2) {
      this.stats = var1;
      this.isPersistent = var2;
   }
}
