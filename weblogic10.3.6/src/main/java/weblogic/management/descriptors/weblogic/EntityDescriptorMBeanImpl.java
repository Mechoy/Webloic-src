package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityDescriptorMBeanImpl extends XMLElementMBeanDelegate implements EntityDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_persistence = false;
   private PersistenceMBean persistence;
   private boolean isSet_pool = false;
   private PoolMBean pool;
   private boolean isSet_enableDynamicQueries = false;
   private boolean enableDynamicQueries = false;
   private boolean isSet_invalidationTarget = false;
   private InvalidationTargetMBean invalidationTarget;
   private boolean isSet_entityCacheRef = false;
   private EntityCacheRefMBean entityCacheRef;
   private boolean isSet_entityClustering = false;
   private EntityClusteringMBean entityClustering;
   private boolean isSet_entityCache = false;
   private EntityCacheMBean entityCache;

   public PersistenceMBean getPersistence() {
      return this.persistence;
   }

   public void setPersistence(PersistenceMBean var1) {
      PersistenceMBean var2 = this.persistence;
      this.persistence = var1;
      this.isSet_persistence = var1 != null;
      this.checkChange("persistence", var2, this.persistence);
   }

   public PoolMBean getPool() {
      return this.pool;
   }

   public void setPool(PoolMBean var1) {
      PoolMBean var2 = this.pool;
      this.pool = var1;
      this.isSet_pool = var1 != null;
      this.checkChange("pool", var2, this.pool);
   }

   public boolean isEnableDynamicQueries() {
      return this.enableDynamicQueries;
   }

   public void setEnableDynamicQueries(boolean var1) {
      boolean var2 = this.enableDynamicQueries;
      this.enableDynamicQueries = var1;
      this.isSet_enableDynamicQueries = true;
      this.checkChange("enableDynamicQueries", var2, this.enableDynamicQueries);
   }

   public InvalidationTargetMBean getInvalidationTarget() {
      return this.invalidationTarget;
   }

   public void setInvalidationTarget(InvalidationTargetMBean var1) {
      InvalidationTargetMBean var2 = this.invalidationTarget;
      this.invalidationTarget = var1;
      this.isSet_invalidationTarget = var1 != null;
      this.checkChange("invalidationTarget", var2, this.invalidationTarget);
   }

   public EntityCacheRefMBean getEntityCacheRef() {
      return this.entityCacheRef;
   }

   public void setEntityCacheRef(EntityCacheRefMBean var1) {
      EntityCacheRefMBean var2 = this.entityCacheRef;
      this.entityCacheRef = var1;
      this.isSet_entityCacheRef = var1 != null;
      this.checkChange("entityCacheRef", var2, this.entityCacheRef);
   }

   public EntityClusteringMBean getEntityClustering() {
      return this.entityClustering;
   }

   public void setEntityClustering(EntityClusteringMBean var1) {
      EntityClusteringMBean var2 = this.entityClustering;
      this.entityClustering = var1;
      this.isSet_entityClustering = var1 != null;
      this.checkChange("entityClustering", var2, this.entityClustering);
   }

   public EntityCacheMBean getEntityCache() {
      return this.entityCache;
   }

   public void setEntityCache(EntityCacheMBean var1) {
      EntityCacheMBean var2 = this.entityCache;
      this.entityCache = var1;
      this.isSet_entityCache = var1 != null;
      this.checkChange("entityCache", var2, this.entityCache);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-descriptor");
      var2.append(">\n");
      if (null != this.getPool()) {
         var2.append(this.getPool().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getEntityCache()) {
         var2.append(this.getEntityCache().toXML(var1 + 2)).append("\n");
      } else if (null != this.getEntityCacheRef()) {
         var2.append(this.getEntityCacheRef().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getPersistence()) {
         var2.append(this.getPersistence().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getEntityClustering()) {
         var2.append(this.getEntityClustering().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getInvalidationTarget()) {
         var2.append(this.getInvalidationTarget().toXML(var1 + 2)).append("\n");
      }

      if (this.isSet_enableDynamicQueries || this.isEnableDynamicQueries()) {
         var2.append(ToXML.indent(var1 + 2)).append("<enable-dynamic-queries>").append(ToXML.capitalize(Boolean.valueOf(this.isEnableDynamicQueries()).toString())).append("</enable-dynamic-queries>\n");
      }

      var2.append(ToXML.indent(var1)).append("</entity-descriptor>\n");
      return var2.toString();
   }
}
