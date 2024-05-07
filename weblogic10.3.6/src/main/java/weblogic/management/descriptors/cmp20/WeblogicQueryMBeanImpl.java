package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.ejb20.QueryMethodMBean;
import weblogic.management.tools.ToXML;

public class WeblogicQueryMBeanImpl extends XMLElementMBeanDelegate implements WeblogicQueryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_sqlSelectDistinct = false;
   private boolean sqlSelectDistinct = false;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_includeUpdates = false;
   private boolean includeUpdates = true;
   private boolean isSet_queryMethod = false;
   private QueryMethodMBean queryMethod;
   private boolean isSet_maxElements = false;
   private int maxElements = 0;
   private boolean isSet_groupName = false;
   private String groupName;
   private boolean isSet_weblogicQl = false;
   private String weblogicQl;
   private boolean isSet_cachingName = false;
   private String cachingName;

   public boolean getSqlSelectDistinct() {
      return this.sqlSelectDistinct;
   }

   public void setSqlSelectDistinct(boolean var1) {
      boolean var2 = this.sqlSelectDistinct;
      this.sqlSelectDistinct = var1;
      this.isSet_sqlSelectDistinct = true;
      this.checkChange("sqlSelectDistinct", var2, this.sqlSelectDistinct);
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.description;
      this.description = var1;
      this.isSet_description = var1 != null;
      this.checkChange("description", var2, this.description);
   }

   public boolean isIncludeUpdates() {
      return this.includeUpdates;
   }

   public void setIncludeUpdates(boolean var1) {
      boolean var2 = this.includeUpdates;
      this.includeUpdates = var1;
      this.isSet_includeUpdates = true;
      this.checkChange("includeUpdates", var2, this.includeUpdates);
   }

   public QueryMethodMBean getQueryMethod() {
      return this.queryMethod;
   }

   public void setQueryMethod(QueryMethodMBean var1) {
      QueryMethodMBean var2 = this.queryMethod;
      this.queryMethod = var1;
      this.isSet_queryMethod = var1 != null;
      this.checkChange("queryMethod", var2, this.queryMethod);
   }

   public int getMaxElements() {
      return this.maxElements;
   }

   public void setMaxElements(int var1) {
      int var2 = this.maxElements;
      this.maxElements = var1;
      this.isSet_maxElements = var1 != -1;
      this.checkChange("maxElements", var2, this.maxElements);
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.groupName;
      this.groupName = var1;
      this.isSet_groupName = var1 != null;
      this.checkChange("groupName", var2, this.groupName);
   }

   public String getWeblogicQl() {
      return this.weblogicQl;
   }

   public void setWeblogicQl(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.weblogicQl;
      this.weblogicQl = var1;
      this.isSet_weblogicQl = var1 != null;
      this.checkChange("weblogicQl", var2, this.weblogicQl);
   }

   public String getCachingName() {
      return this.cachingName;
   }

   public void setCachingName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cachingName;
      this.cachingName = var1;
      this.isSet_cachingName = var1 != null;
      this.checkChange("cachingName", var2, this.cachingName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-query");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getQueryMethod()) {
         var2.append(this.getQueryMethod().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getWeblogicQl()) {
         var2.append(ToXML.indent(var1 + 2)).append("<weblogic-ql>").append("<![CDATA[" + this.getWeblogicQl() + "]]>").append("</weblogic-ql>\n");
      }

      if (null != this.getGroupName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<group-name>").append(this.getGroupName()).append("</group-name>\n");
      }

      if (null != this.getCachingName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<caching-name>").append(this.getCachingName()).append("</caching-name>\n");
      }

      if (this.isSet_maxElements || 0 != this.getMaxElements()) {
         var2.append(ToXML.indent(var1 + 2)).append("<max-elements>").append(this.getMaxElements()).append("</max-elements>\n");
      }

      if (this.isSet_includeUpdates || !this.isIncludeUpdates()) {
         var2.append(ToXML.indent(var1 + 2)).append("<include-updates>").append(ToXML.capitalize(Boolean.valueOf(this.isIncludeUpdates()).toString())).append("</include-updates>\n");
      }

      if (this.isSet_sqlSelectDistinct || this.getSqlSelectDistinct()) {
         var2.append(ToXML.indent(var1 + 2)).append("<sql-select-distinct>").append(ToXML.capitalize(Boolean.valueOf(this.getSqlSelectDistinct()).toString())).append("</sql-select-distinct>\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-query>\n");
      return var2.toString();
   }
}
