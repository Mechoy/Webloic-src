package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.RunAsMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class RunAsDescriptor extends BaseServletDescriptor implements RunAsMBean {
   private String description;
   private String roleName;

   public RunAsDescriptor() {
   }

   public RunAsDescriptor(RunAsMBean var1) {
      this.setDescription(var1.getDescription());
      this.setRoleName(var1.getRoleName());
   }

   public RunAsDescriptor(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setRoleName(DOMUtils.getValueByTagName(var1, "role-name"));
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

   public void setRoleName(String var1) {
      String var2 = this.roleName;
      this.roleName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("roleName", var2, var1);
      }

   }

   public String getRoleName() {
      return this.roleName;
   }

   public String toString() {
      return this.getRoleName();
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<run-as>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<role-name>" + this.getRoleName() + "</role-name>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</run-as>\n";
      return var2;
   }
}
