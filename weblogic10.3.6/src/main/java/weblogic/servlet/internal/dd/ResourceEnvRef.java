package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ResourceEnvRefMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ResourceEnvRef extends BaseServletDescriptor implements ResourceEnvRefMBean {
   private static final long serialVersionUID = 8512993589478342470L;
   String refName;
   String refType;
   String description;

   public ResourceEnvRef() {
   }

   public ResourceEnvRef(String var1, String var2, String var3) {
      this.setDescription(var1);
      this.setRefName(var2);
      this.setRefType(var3);
   }

   public ResourceEnvRef(ResourceEnvRefMBean var1) {
      this.setDescription(var1.getDescription());
      this.setRefName(var1.getRefName());
      this.setRefType(var1.getRefType());
   }

   public ResourceEnvRef(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setRefName(DOMUtils.getValueByTagName(var1, "resource-env-ref-name"));
      this.setRefType(DOMUtils.getValueByTagName(var1, "resource-env-ref-type"));
   }

   public String getRefName() {
      return this.refName;
   }

   public void setRefName(String var1) {
      String var2 = this.refName;
      this.refName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("refName", var2, var1);
      }

   }

   public String getRefType() {
      return this.refType;
   }

   public void setRefType(String var1) {
      String var2 = this.refType;
      this.refType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("refType", var2, var1);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<resource-env-ref>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<resource-env-ref-name>" + this.getRefName() + "</resource-env-ref-name>\n";

      try {
         var2 = var2 + this.indentStr(var1) + "<resource-env-ref-type>" + this.getRefType() + "</resource-env-ref-type>\n";
      } catch (Exception var5) {
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</resource-env-ref>\n";
      return var2;
   }
}
