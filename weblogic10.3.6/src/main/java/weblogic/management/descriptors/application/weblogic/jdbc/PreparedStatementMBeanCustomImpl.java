package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PreparedStatementMBeanCustomImpl extends XMLElementMBeanDelegate implements PreparedStatementMBean {
   private String descrEncoding;
   private String descriptorVersion;
   private Boolean profilingEnabled;
   private Integer profilingThreshold;
   private Integer cacheSize;
   private Boolean paramLoggingEnabled;
   private Integer maxParamLen;
   private String cacheType;

   public void setEncoding(String var1) {
      this.descrEncoding = var1;
   }

   public String getEncoding() {
      return this.descrEncoding;
   }

   public void setVersion(String var1) {
      String var2 = this.descriptorVersion;
      this.descriptorVersion = var1;
      this.checkChange("version", this.descriptorVersion, var2);
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public void setProfilingEnabled(boolean var1) {
      Boolean var2 = this.profilingEnabled;
      this.profilingEnabled = new Boolean(var1);
      this.checkChange("profilingEnabled", var2, this.profilingEnabled);
   }

   public boolean isProfilingEnabled() {
      return this.profilingEnabled != null ? this.profilingEnabled : false;
   }

   public void setCacheProfilingThreshold(int var1) {
      Integer var2 = this.profilingThreshold;
      this.profilingThreshold = new Integer(var1);
      this.checkChange("cacheProfilingThreshold", var2, this.profilingThreshold);
   }

   public int getCacheProfilingThreshold() {
      return this.profilingThreshold != null ? this.profilingThreshold : 10;
   }

   public void setCacheSize(int var1) {
      Integer var2 = this.cacheSize;
      this.cacheSize = new Integer(var1);
      this.checkChange("cacheSize", var2, this.cacheSize);
   }

   public int getCacheSize() {
      return this.cacheSize != null ? this.cacheSize : 10;
   }

   public void setParameterLoggingEnabled(boolean var1) {
      Boolean var2 = this.paramLoggingEnabled;
      this.paramLoggingEnabled = new Boolean(var1);
      this.checkChange("parameterLoggingEnabled", var2, this.paramLoggingEnabled);
   }

   public boolean isParameterLoggingEnabled() {
      return this.paramLoggingEnabled != null ? this.paramLoggingEnabled : false;
   }

   public void setMaxParameterLength(int var1) {
      Integer var2 = this.maxParamLen;
      this.maxParamLen = new Integer(var1);
      this.checkChange("maxParameterLength", var2, this.maxParamLen);
   }

   public int getMaxParameterLength() {
      return this.maxParamLen != null ? this.maxParamLen : 10;
   }

   public void setCacheType(String var1) {
      String var2 = this.cacheType;
      this.cacheType = new String(var1);
      this.checkChange("cacheType", var2, this.cacheType);
   }

   public String getCacheType() {
      return this.cacheType != null ? this.cacheType : "LRU";
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<prepared-statement");
      var2.append(">\n");
      var2.append(ToXML.indent(var1 + 2)).append("<profiling-enabled>").append(ToXML.capitalize((new Boolean(this.isProfilingEnabled())).toString())).append("</profiling-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<cache-profiling-threshold>").append(this.getCacheProfilingThreshold()).append("</cache-profiling-threshold>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<cache-size>").append(this.getCacheSize()).append("</cache-size>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<parameter-logging-enabled>").append(ToXML.capitalize((new Boolean(this.isParameterLoggingEnabled())).toString())).append("</parameter-logging-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<max-parameter-length>").append(this.getMaxParameterLength()).append("</max-parameter-length>\n");
      if (null != this.getCacheType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cache-type>").append(this.getCacheType()).append("</cache-type>\n");
      }

      var2.append(ToXML.indent(var1)).append("</prepared-statement>\n");
      return var2.toString();
   }
}
