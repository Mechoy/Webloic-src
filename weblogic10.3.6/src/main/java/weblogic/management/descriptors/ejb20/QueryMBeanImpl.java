package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class QueryMBeanImpl extends XMLElementMBeanDelegate implements QueryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_queryMethod = false;
   private QueryMethodMBean queryMethod;
   private boolean isSet_ejbQl = false;
   private String ejbQl;
   private boolean isSet_resultTypeMapping = false;
   private String resultTypeMapping = "Local";

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

   public QueryMethodMBean getQueryMethod() {
      return this.queryMethod;
   }

   public void setQueryMethod(QueryMethodMBean var1) {
      QueryMethodMBean var2 = this.queryMethod;
      this.queryMethod = var1;
      this.isSet_queryMethod = var1 != null;
      this.checkChange("queryMethod", var2, this.queryMethod);
   }

   public String getEJBQl() {
      return this.ejbQl;
   }

   public void setEJBQl(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbQl;
      this.ejbQl = var1;
      this.isSet_ejbQl = var1 != null;
      this.checkChange("ejbQl", var2, this.ejbQl);
   }

   public String getResultTypeMapping() {
      return this.resultTypeMapping;
   }

   public void setResultTypeMapping(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resultTypeMapping;
      this.resultTypeMapping = var1;
      this.isSet_resultTypeMapping = var1 != null;
      this.checkChange("resultTypeMapping", var2, this.resultTypeMapping);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<query");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getQueryMethod()) {
         var2.append(this.getQueryMethod().toXML(var1 + 2)).append("\n");
      }

      if ((this.isSet_resultTypeMapping || !"Local".equals(this.getResultTypeMapping())) && null != this.getResultTypeMapping()) {
         var2.append(ToXML.indent(var1 + 2)).append("<result-type-mapping>").append(this.getResultTypeMapping()).append("</result-type-mapping>\n");
      }

      if (null != this.getEJBQl()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-ql>").append("<![CDATA[" + this.getEJBQl() + "]]>").append("</ejb-ql>\n");
      }

      var2.append(ToXML.indent(var1)).append("</query>\n");
      return var2.toString();
   }
}
