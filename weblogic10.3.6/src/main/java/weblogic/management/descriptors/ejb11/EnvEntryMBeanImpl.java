package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EnvEntryMBeanImpl extends XMLElementMBeanDelegate implements EnvEntryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_envEntryName = false;
   private String envEntryName;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_envEntryType = false;
   private String envEntryType;
   private boolean isSet_envEntryValue = false;
   private String envEntryValue;

   public String getEnvEntryName() {
      return this.envEntryName;
   }

   public void setEnvEntryName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.envEntryName;
      this.envEntryName = var1;
      this.isSet_envEntryName = var1 != null;
      this.checkChange("envEntryName", var2, this.envEntryName);
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

   public String getEnvEntryType() {
      return this.envEntryType;
   }

   public void setEnvEntryType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.envEntryType;
      this.envEntryType = var1;
      this.isSet_envEntryType = var1 != null;
      this.checkChange("envEntryType", var2, this.envEntryType);
   }

   public String getEnvEntryValue() {
      return this.envEntryValue;
   }

   public void setEnvEntryValue(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.envEntryValue;
      this.envEntryValue = var1;
      this.isSet_envEntryValue = var1 != null;
      this.checkChange("envEntryValue", var2, this.envEntryValue);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<env-entry");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getEnvEntryName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<env-entry-name>").append(this.getEnvEntryName()).append("</env-entry-name>\n");
      }

      if (null != this.getEnvEntryType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<env-entry-type>").append(this.getEnvEntryType()).append("</env-entry-type>\n");
      }

      if (null != this.getEnvEntryValue()) {
         var2.append(ToXML.indent(var1 + 2)).append("<env-entry-value>").append(this.getEnvEntryValue()).append("</env-entry-value>\n");
      }

      var2.append(ToXML.indent(var1)).append("</env-entry>\n");
      return var2.toString();
   }
}
