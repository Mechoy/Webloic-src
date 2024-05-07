package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.management.descriptors.webapp.SecurityRoleRefMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SecurityRoleRefDescriptor extends BaseServletDescriptor implements SecurityRoleRefMBean, ToXML {
   private static final long serialVersionUID = 5435403745362765252L;
   private String description;
   private String roleName;
   private SecurityRoleMBean roleLink;
   private String linkName;

   public SecurityRoleRefDescriptor() {
   }

   public SecurityRoleRefDescriptor(SecurityRoleRefMBean var1) {
      this.setDescription(var1.getDescription());
      this.setRoleName(var1.getRoleName());
      this.setRoleLink(var1.getRoleLink());
   }

   public SecurityRoleRefDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var2, "description"));
      this.setRoleName(DOMUtils.getValueByTagName(var2, "role-name"));
      this.linkName = DOMUtils.getOptionalValueByTagName(var2, "role-link");
      SecurityRoleMBean[] var3 = var1.getSecurityRoles();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getRoleName().equals(this.linkName)) {
            this.setRoleLink(var3[var4]);
         }
      }

   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", this.description, var1);
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

   public void setRoleLink(SecurityRoleMBean var1) {
      this.linkName = null;
      SecurityRoleMBean var2 = this.roleLink;
      this.roleLink = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("roleLink", var2, var1);
      }

   }

   public SecurityRoleMBean getRoleLink() {
      return this.roleLink;
   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.roleName == null || this.roleName.length() <= 0) {
         this.addDescriptorError("NO_SECURITY_ROLE_NAME_SET");
         var1 = false;
      }

      if (this.roleLink == null) {
         this.addDescriptorError("NO_SECURITY_ROLE_LINK_SET");
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<security-role-ref>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<role-name>" + this.getRoleName() + "</role-name>\n";
      SecurityRoleMBean var4 = this.getRoleLink();
      if (var4 != null) {
         var2 = var2 + this.indentStr(var1) + "<role-link>" + var4.getRoleName() + "</role-link>\n";
      } else if (this.linkName != null && (this.linkName = this.linkName.trim()).length() != 0) {
         var2 = var2 + this.indentStr(var1) + "<role-link>" + this.linkName + "</role-link>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</security-role-ref>\n";
      return var2;
   }
}
