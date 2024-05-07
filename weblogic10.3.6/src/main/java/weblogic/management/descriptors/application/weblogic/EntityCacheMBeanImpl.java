package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityCacheMBeanImpl extends XMLElementMBeanDelegate implements EntityCacheMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_maxCacheSize = false;
   private MaxCacheSizeMBean maxCacheSize;
   private boolean isSet_entityCacheName = false;
   private String entityCacheName;
   private boolean isSet_cachingStrategy = false;
   private String cachingStrategy = "MultiVersion";
   private boolean isSet_maxBeansInCache = false;
   private int maxBeansInCache = 1000;

   public MaxCacheSizeMBean getMaxCacheSize() {
      return this.maxCacheSize;
   }

   public void setMaxCacheSize(MaxCacheSizeMBean var1) {
      MaxCacheSizeMBean var2 = this.maxCacheSize;
      this.maxCacheSize = var1;
      this.isSet_maxCacheSize = var1 != null;
      this.checkChange("maxCacheSize", var2, this.maxCacheSize);
   }

   public String getEntityCacheName() {
      return this.entityCacheName;
   }

   public void setEntityCacheName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.entityCacheName;
      this.entityCacheName = var1;
      this.isSet_entityCacheName = var1 != null;
      this.checkChange("entityCacheName", var2, this.entityCacheName);
   }

   public String getCachingStrategy() {
      return this.cachingStrategy;
   }

   public void setCachingStrategy(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cachingStrategy;
      this.cachingStrategy = var1;
      this.isSet_cachingStrategy = var1 != null;
      this.checkChange("cachingStrategy", var2, this.cachingStrategy);
   }

   public int getMaxBeansInCache() {
      return this.maxBeansInCache;
   }

   public void setMaxBeansInCache(int var1) {
      int var2 = this.maxBeansInCache;
      this.maxBeansInCache = var1;
      this.isSet_maxBeansInCache = var1 != -1;
      this.checkChange("maxBeansInCache", var2, this.maxBeansInCache);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-cache");
      var2.append(">\n");
      if (null != this.getEntityCacheName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<entity-cache-name>").append(this.getEntityCacheName()).append("</entity-cache-name>\n");
      }

      if (null != this.getMaxCacheSize()) {
         var2.append(this.getMaxCacheSize().toXML(var1 + 2)).append("\n");
      } else if (this.isSet_maxBeansInCache || 1000 != this.getMaxBeansInCache()) {
         var2.append(ToXML.indent(var1 + 2)).append("<max-beans-in-cache>").append(this.getMaxBeansInCache()).append("</max-beans-in-cache>\n");
      }

      if ((this.isSet_cachingStrategy || !"MultiVersion".equals(this.getCachingStrategy())) && null != this.getCachingStrategy()) {
         var2.append(ToXML.indent(var1 + 2)).append("<caching-strategy>").append(this.getCachingStrategy()).append("</caching-strategy>\n");
      }

      var2.append(ToXML.indent(var1)).append("</entity-cache>\n");
      return var2.toString();
   }
}
