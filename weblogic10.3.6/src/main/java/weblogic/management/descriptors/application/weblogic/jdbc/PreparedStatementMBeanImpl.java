package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PreparedStatementMBeanImpl extends XMLElementMBeanDelegate implements PreparedStatementMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_cacheSize = false;
   private int cacheSize;
   private boolean isSet_parameterLoggingEnabled = false;
   private boolean parameterLoggingEnabled;
   private boolean isSet_maxParameterLength = false;
   private int maxParameterLength;
   private boolean isSet_cacheProfilingThreshold = false;
   private int cacheProfilingThreshold;
   private boolean isSet_cacheType = false;
   private String cacheType;
   private boolean isSet_profilingEnabled = false;
   private boolean profilingEnabled;

   public int getCacheSize() {
      return this.cacheSize;
   }

   public void setCacheSize(int var1) {
      int var2 = this.cacheSize;
      this.cacheSize = var1;
      this.isSet_cacheSize = var1 != -1;
      this.checkChange("cacheSize", var2, this.cacheSize);
   }

   public boolean isParameterLoggingEnabled() {
      return this.parameterLoggingEnabled;
   }

   public void setParameterLoggingEnabled(boolean var1) {
      boolean var2 = this.parameterLoggingEnabled;
      this.parameterLoggingEnabled = var1;
      this.isSet_parameterLoggingEnabled = true;
      this.checkChange("parameterLoggingEnabled", var2, this.parameterLoggingEnabled);
   }

   public int getMaxParameterLength() {
      return this.maxParameterLength;
   }

   public void setMaxParameterLength(int var1) {
      int var2 = this.maxParameterLength;
      this.maxParameterLength = var1;
      this.isSet_maxParameterLength = var1 != -1;
      this.checkChange("maxParameterLength", var2, this.maxParameterLength);
   }

   public int getCacheProfilingThreshold() {
      return this.cacheProfilingThreshold;
   }

   public void setCacheProfilingThreshold(int var1) {
      int var2 = this.cacheProfilingThreshold;
      this.cacheProfilingThreshold = var1;
      this.isSet_cacheProfilingThreshold = var1 != -1;
      this.checkChange("cacheProfilingThreshold", var2, this.cacheProfilingThreshold);
   }

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

   public boolean isProfilingEnabled() {
      return this.profilingEnabled;
   }

   public void setProfilingEnabled(boolean var1) {
      boolean var2 = this.profilingEnabled;
      this.profilingEnabled = var1;
      this.isSet_profilingEnabled = true;
      this.checkChange("profilingEnabled", var2, this.profilingEnabled);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<prepared-statement");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</prepared-statement>\n");
      return var2.toString();
   }
}
