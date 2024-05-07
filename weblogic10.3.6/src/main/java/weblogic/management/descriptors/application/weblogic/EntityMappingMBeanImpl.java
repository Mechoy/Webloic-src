package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityMappingMBeanImpl extends XMLElementMBeanDelegate implements EntityMappingMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_systemId = false;
   private String systemId;
   private boolean isSet_publicId = false;
   private String publicId;
   private boolean isSet_cacheTimeoutInterval = false;
   private int cacheTimeoutInterval;
   private boolean isSet_entityMappingName = false;
   private String entityMappingName;
   private boolean isSet_whenToCache = false;
   private String whenToCache;
   private boolean isSet_entityURI = false;
   private String entityURI;

   public String getSystemId() {
      return this.systemId;
   }

   public void setSystemId(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.systemId;
      this.systemId = var1;
      this.isSet_systemId = var1 != null;
      this.checkChange("systemId", var2, this.systemId);
   }

   public String getPublicId() {
      return this.publicId;
   }

   public void setPublicId(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.publicId;
      this.publicId = var1;
      this.isSet_publicId = var1 != null;
      this.checkChange("publicId", var2, this.publicId);
   }

   public int getCacheTimeoutInterval() {
      return this.cacheTimeoutInterval;
   }

   public void setCacheTimeoutInterval(int var1) {
      int var2 = this.cacheTimeoutInterval;
      this.cacheTimeoutInterval = var1;
      this.isSet_cacheTimeoutInterval = var1 != -1;
      this.checkChange("cacheTimeoutInterval", var2, this.cacheTimeoutInterval);
   }

   public String getEntityMappingName() {
      return this.entityMappingName;
   }

   public void setEntityMappingName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.entityMappingName;
      this.entityMappingName = var1;
      this.isSet_entityMappingName = var1 != null;
      this.checkChange("entityMappingName", var2, this.entityMappingName);
   }

   public String getWhenToCache() {
      return this.whenToCache;
   }

   public void setWhenToCache(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.whenToCache;
      this.whenToCache = var1;
      this.isSet_whenToCache = var1 != null;
      this.checkChange("whenToCache", var2, this.whenToCache);
   }

   public String getEntityURI() {
      return this.entityURI;
   }

   public void setEntityURI(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.entityURI;
      this.entityURI = var1;
      this.isSet_entityURI = var1 != null;
      this.checkChange("entityURI", var2, this.entityURI);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-mapping");
      var2.append(">\n");
      if (null != this.getEntityMappingName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<entity-mapping-name>").append(this.getEntityMappingName()).append("</entity-mapping-name>\n");
      }

      if (null != this.getPublicId()) {
         var2.append(ToXML.indent(var1 + 2)).append("<public-id>").append(this.getPublicId()).append("</public-id>\n");
      }

      if (null != this.getSystemId()) {
         var2.append(ToXML.indent(var1 + 2)).append("<system-id>").append(this.getSystemId()).append("</system-id>\n");
      }

      if (null != this.getEntityURI()) {
         var2.append(ToXML.indent(var1 + 2)).append("<entity-uri>").append(this.getEntityURI()).append("</entity-uri>\n");
      }

      if (null != this.getWhenToCache()) {
         var2.append(ToXML.indent(var1 + 2)).append("<when-to-cache>").append(this.getWhenToCache()).append("</when-to-cache>\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<cache-timeout-interval>").append(this.getCacheTimeoutInterval()).append("</cache-timeout-interval>\n");
      var2.append(ToXML.indent(var1)).append("</entity-mapping>\n");
      return var2.toString();
   }
}
