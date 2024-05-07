package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ResourceEnvRefMBeanImpl extends XMLElementMBeanDelegate implements ResourceEnvRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_resourceEnvRefType = false;
   private String resourceEnvRefType;
   private boolean isSet_resourceEnvRefName = false;
   private String resourceEnvRefName;

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

   public String getResourceEnvRefType() {
      return this.resourceEnvRefType;
   }

   public void setResourceEnvRefType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resourceEnvRefType;
      this.resourceEnvRefType = var1;
      this.isSet_resourceEnvRefType = var1 != null;
      this.checkChange("resourceEnvRefType", var2, this.resourceEnvRefType);
   }

   public String getResourceEnvRefName() {
      return this.resourceEnvRefName;
   }

   public void setResourceEnvRefName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resourceEnvRefName;
      this.resourceEnvRefName = var1;
      this.isSet_resourceEnvRefName = var1 != null;
      this.checkChange("resourceEnvRefName", var2, this.resourceEnvRefName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<resource-env-ref");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getResourceEnvRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<resource-env-ref-name>").append(this.getResourceEnvRefName()).append("</resource-env-ref-name>\n");
      }

      if (null != this.getResourceEnvRefType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<resource-env-ref-type>").append(this.getResourceEnvRefType()).append("</resource-env-ref-type>\n");
      }

      var2.append(ToXML.indent(var1)).append("</resource-env-ref>\n");
      return var2.toString();
   }
}
