package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatefulSessionCacheMBeanImpl extends XMLElementMBeanDelegate implements StatefulSessionCacheMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_cacheType = false;
   private String cacheType = "NRU";
   private boolean isSet_maxBeansInCache = false;
   private int maxBeansInCache = 1000;
   private boolean isSet_sessionTimeoutSeconds = false;
   private int sessionTimeoutSeconds = 600;
   private boolean isSet_idleTimeoutSeconds = false;
   private int idleTimeoutSeconds = 600;

   public String getCacheType() {
      return this.cacheType;
   }

   public void setCacheType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cacheType;
      this.cacheType = var1;
      this.isSet_cacheType = var1 != null;
      this.checkChange("cacheType", var2, this.cacheType);
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

   public int getSessionTimeoutSeconds() {
      return this.sessionTimeoutSeconds;
   }

   public void setSessionTimeoutSeconds(int var1) {
      int var2 = this.sessionTimeoutSeconds;
      this.sessionTimeoutSeconds = var1;
      this.isSet_sessionTimeoutSeconds = var1 != -1;
      this.checkChange("sessionTimeoutSeconds", var2, this.sessionTimeoutSeconds);
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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateful-session-cache");
      var2.append(">\n");
      if (this.isSet_maxBeansInCache || 1000 != this.getMaxBeansInCache()) {
         var2.append(ToXML.indent(var1 + 2)).append("<max-beans-in-cache>").append(this.getMaxBeansInCache()).append("</max-beans-in-cache>\n");
      }

      if (this.isSet_idleTimeoutSeconds || 600 != this.getIdleTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<idle-timeout-seconds>").append(this.getIdleTimeoutSeconds()).append("</idle-timeout-seconds>\n");
      }

      if (this.isSet_sessionTimeoutSeconds || 600 != this.getSessionTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<session-timeout-seconds>").append(this.getSessionTimeoutSeconds()).append("</session-timeout-seconds>\n");
      }

      if ((this.isSet_cacheType || !"NRU".equals(this.getCacheType())) && null != this.getCacheType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cache-type>").append(this.getCacheType()).append("</cache-type>\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateful-session-cache>\n");
      return var2.toString();
   }
}
