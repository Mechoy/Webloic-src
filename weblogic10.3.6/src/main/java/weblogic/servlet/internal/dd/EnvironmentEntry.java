package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.EnvEntryMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class EnvironmentEntry extends BaseServletDescriptor implements EnvEntryMBean {
   private static final long serialVersionUID = -4909291169304419758L;
   private String description;
   private String name;
   private String value;
   private String type;

   public EnvironmentEntry() {
   }

   public EnvironmentEntry(EnvEntryMBean var1) {
   }

   public EnvironmentEntry(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setEnvEntryName(DOMUtils.getValueByTagName(var1, "env-entry-name"));
      this.setEnvEntryValue(DOMUtils.getOptionalValueByTagName(var1, "env-entry-value"));
      this.setEnvEntryType(DOMUtils.getValueByTagName(var1, "env-entry-type"));
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setEnvEntryName(String var1) {
      String var2 = this.name;
      this.name = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("envEntryName", var2, var1);
      }

   }

   public String getEnvEntryName() {
      return this.name;
   }

   public void setEnvEntryValue(String var1) {
      String var2 = this.value;
      this.value = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("envEntryValue", var2, var1);
      }

   }

   public String getEnvEntryValue() {
      return this.value;
   }

   public void setEnvEntryType(String var1) {
      String var2 = this.type;
      this.type = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("envEntryType", var2, var1);
      }

   }

   public String getEnvEntryType() {
      return this.type;
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toString() {
      return "EnvironmentEntry(" + this.hashCode() + "," + this.getEnvEntryName() + ")";
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<env-entry>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<env-entry-name>" + this.getEnvEntryName() + "</env-entry-name>\n";
      String var4 = this.getEnvEntryValue();
      if (var4 != null) {
         var2 = var2 + this.indentStr(var1) + "<env-entry-value>" + var4 + "</env-entry-value>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<env-entry-type>" + this.getEnvEntryType() + "</env-entry-type>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</env-entry>\n";
      return var2;
   }
}
