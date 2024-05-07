package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityCacheMBeanImpl extends XMLElementMBeanDelegate implements EntityCacheMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_maxBeansInCache = false;
   private int maxBeansInCache = 1000;
   private boolean isSet_cacheBetweenTransactions = false;
   private boolean cacheBetweenTransactions = false;
   private boolean isSet_readTimeoutSeconds = false;
   private int readTimeoutSeconds = 600;
   private boolean isSet_idleTimeoutSeconds = false;
   private int idleTimeoutSeconds = 600;
   private boolean isSet_concurrencyStrategy = false;
   private String concurrencyStrategy = "Database";

   public int getMaxBeansInCache() {
      return this.maxBeansInCache;
   }

   public void setMaxBeansInCache(int var1) {
      int var2 = this.maxBeansInCache;
      this.maxBeansInCache = var1;
      this.isSet_maxBeansInCache = var1 != -1;
      this.checkChange("maxBeansInCache", var2, this.maxBeansInCache);
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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-cache");
      var2.append(">\n");
      if (this.isSet_maxBeansInCache || 1000 != this.getMaxBeansInCache()) {
         var2.append(ToXML.indent(var1 + 2)).append("<max-beans-in-cache>").append(this.getMaxBeansInCache()).append("</max-beans-in-cache>\n");
      }

      if (this.isSet_idleTimeoutSeconds || 600 != this.getIdleTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<idle-timeout-seconds>").append(this.getIdleTimeoutSeconds()).append("</idle-timeout-seconds>\n");
      }

      if (this.isSet_readTimeoutSeconds || 600 != this.getReadTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<read-timeout-seconds>").append(this.getReadTimeoutSeconds()).append("</read-timeout-seconds>\n");
      }

      if ((this.isSet_concurrencyStrategy || !"Database".equals(this.getConcurrencyStrategy())) && null != this.getConcurrencyStrategy()) {
         var2.append(ToXML.indent(var1 + 2)).append("<concurrency-strategy>").append(this.getConcurrencyStrategy()).append("</concurrency-strategy>\n");
      }

      if (this.isSet_cacheBetweenTransactions || this.getCacheBetweenTransactions()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cache-between-transactions>").append(ToXML.capitalize(Boolean.valueOf(this.getCacheBetweenTransactions()).toString())).append("</cache-between-transactions>\n");
      }

      var2.append(ToXML.indent(var1)).append("</entity-cache>\n");
      return var2.toString();
   }
}
