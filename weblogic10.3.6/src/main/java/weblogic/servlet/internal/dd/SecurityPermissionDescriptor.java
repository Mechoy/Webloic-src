package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.SecurityPermissionMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SecurityPermissionDescriptor extends BaseServletDescriptor implements SecurityPermissionMBean, ToXML {
   private static final String SECURITY_PERMISSION = "security-permission";
   private static final String SECURITY_PERMISSION_SPEC = "security-permission-spec";
   private static final String DESCRIPTION = "description";
   private String spec;
   private String desc;

   public SecurityPermissionDescriptor() {
      this.spec = null;
      this.desc = null;
   }

   public SecurityPermissionDescriptor(String var1, String var2) {
      this.spec = var1;
      this.desc = var2;
   }

   public SecurityPermissionDescriptor(Element var1) throws DOMProcessingException {
      this.desc = DOMUtils.getOptionalValueByTagName(var1, "description");
      this.spec = DOMUtils.getValueByTagName(var1, "security-permission-spec");
   }

   public void setDescription(String var1) {
      String var2 = this.desc;
      this.desc = var1;
      if (!comp(var2, this.desc)) {
         this.firePropertyChange("description", var2, this.desc);
      }

   }

   public String getDescription() {
      return this.desc;
   }

   public void setSecurityPermissionSpec(String var1) {
      String var2 = this.spec;
      this.spec = var1;
      if (!comp(var2, this.spec)) {
         this.firePropertyChange("securityPermissionSpec", var2, this.spec);
      }

   }

   public String getSecurityPermissionSpec() {
      return this.spec;
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<" + "security-permission" + ">\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<" + "security-permission-spec" + ">" + this.getSecurityPermissionSpec() + "</" + "security-permission-spec" + ">\n";
      var2 = var2 + this.indentStr(var1) + "<" + "description" + ">" + this.getDescription() + "</" + "description" + ">\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</" + "security-permission" + ">\n";
      return var2;
   }
}
