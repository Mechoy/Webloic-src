package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityCacheRefMBeanImpl extends XMLElementMBeanDelegate implements EntityCacheRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_entityCacheName = false;
   private String entityCacheName;
   private boolean isSet_cacheBetweenTransactions = false;
   private boolean cacheBetweenTransactions = false;
   private boolean isSet_readTimeoutSeconds = false;
   private int readTimeoutSeconds = 600;
   private boolean isSet_idleTimeoutSeconds = false;
   private int idleTimeoutSeconds = 0;
   private boolean isSet_concurrencyStrategy = false;
   private String concurrencyStrategy;
   private boolean isSet_estimatedBeanSize = false;
   private int estimatedBeanSize = 100;

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

   public boolean getCacheBetweenTransactions() {
      return this.cacheBetweenTransactions;
   }

   public void setCacheBetweenTransactions(boolean var1) {
      boolean var2 = this.cacheBetweenTransactions;
      this.cacheBetweenTransactions = var1;
      this.isSet_cacheBetweenTransactions = true;
      this.checkChange("cacheBetweenTransactions", var2, this.cacheBetweenTransactions);
   }

   public int getReadTimeoutSeconds() {
      return this.readTimeoutSeconds;
   }

   public void setReadTimeoutSeconds(int var1) {
      int var2 = this.readTimeoutSeconds;
      this.readTimeoutSeconds = var1;
      this.isSet_readTimeoutSeconds = var1 != -1;
      this.checkChange("readTimeoutSeconds", var2, this.readTimeoutSeconds);
   }

   public int getIdleTimeoutSeconds() {
      return this.idleTimeoutSeconds;
   }

   public void setIdleTimeoutSeconds(int var1) {
      int var2 = this.idleTimeoutSeconds;
      this.idleTimeoutSeconds = var1;
      this.isSet_idleTimeoutSeconds = var1 != -1;
      this.checkChange("idleTimeoutSeconds", var2, this.idleTimeoutSeconds);
   }

   public String getConcurrencyStrategy() {
      return this.concurrencyStrategy;
   }

   public void setConcurrencyStrategy(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.concurrencyStrategy;
      this.concurrencyStrategy = var1;
      this.isSet_concurrencyStrategy = var1 != null;
      this.checkChange("concurrencyStrategy", var2, this.concurrencyStrategy);
   }

   public int getEstimatedBeanSize() {
      return this.estimatedBeanSize;
   }

   public void setEstimatedBeanSize(int var1) {
      int var2 = this.estimatedBeanSize;
      this.estimatedBeanSize = var1;
      this.isSet_estimatedBeanSize = var1 != -1;
      this.checkChange("estimatedBeanSize", var2, this.estimatedBeanSize);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-cache-ref");
      var2.append(">\n");
      if (null != this.getEntityCacheName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<entity-cache-name>").append(this.getEntityCacheName()).append("</entity-cache-name>\n");
      }

      if (this.isSet_readTimeoutSeconds || 600 != this.getReadTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<read-timeout-seconds>").append(this.getReadTimeoutSeconds()).append("</read-timeout-seconds>\n");
      }

      if (null != this.getConcurrencyStrategy()) {
         var2.append(ToXML.indent(var1 + 2)).append("<concurrency-strategy>").append(this.getConcurrencyStrategy()).append("</concurrency-strategy>\n");
      }

      if (this.isSet_cacheBetweenTransactions || this.getCacheBetweenTransactions()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cache-between-transactions>").append(ToXML.capitalize(Boolean.valueOf(this.getCacheBetweenTransactions()).toString())).append("</cache-between-transactions>\n");
      }

      if (this.isSet_estimatedBeanSize || 100 != this.getEstimatedBeanSize()) {
         var2.append(ToXML.indent(var1 + 2)).append("<estimated-bean-size>").append(this.getEstimatedBeanSize()).append("</estimated-bean-size>\n");
      }

      if (this.isSet_idleTimeoutSeconds || 0 != this.getIdleTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<idle-timeout-seconds>").append(this.getIdleTimeoutSeconds()).append("</idle-timeout-seconds>\n");
      }

      var2.append(ToXML.indent(var1)).append("</entity-cache-ref>\n");
      return var2.toString();
   }
}
