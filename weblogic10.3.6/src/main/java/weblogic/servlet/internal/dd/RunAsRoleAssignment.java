package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.management.descriptors.webappext.RunAsRoleAssignmentMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class RunAsRoleAssignment extends BaseServletDescriptor implements RunAsRoleAssignmentMBean {
   private static final String RUN_AS_IDENTITY = "run-as-role-assignment";
   private static final String ROLE_NAME = "role-name";
   private static final String RUN_AS_PRINCIPAL_NAME = "run-as-principal-name";
   private String runAsPrincipalName = null;
   private SecurityRoleMBean securityRole;

   public RunAsRoleAssignment(SecurityRoleMBean var1) {
      this.securityRole = var1;
      if (this.securityRole == null) {
         throw new NullPointerException();
      } else {
         this.runAsPrincipalName = null;
      }
   }

   public RunAsRoleAssignment(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      String var3 = DOMUtils.getValueByTagName(var2, "role-name");
      this.securityRole = this.findRole(var1, var3);
      if (this.securityRole == null) {
         throw new DOMProcessingException("SecurityRole with name " + var3 + " not defined in web.xml");
      } else {
         this.setRunAsPrincipalName(DOMUtils.getValueByTagName(var2, "run-as-principal-name"));
      }
   }

   public void setRoleName(String var1) throws DOMProcessingException {
      this.securityRole.setRoleName(var1);
      this.setIdentity();
   }

   public String getRoleName() {
      return this.securityRole.getRoleName();
   }

   public void setSecurityRole(SecurityRoleMBean var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.securityRole = var1;
         this.setIdentity();
      }
   }

   public SecurityRoleMBean getSecurityRole() {
      return this.securityRole;
   }

   public void setRunAsPrincipalName(String var1) {
      String var2 = this.runAsPrincipalName;
      this.runAsPrincipalName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("runAsPrincipalName", var2, var1);
      }

      this.setIdentity();
   }

   public String getRunAsPrincipalName() {
      return this.runAsPrincipalName;
   }

   private void setIdentity() {
      if (this.securityRole != null && this.runAsPrincipalName != null) {
         this.securityRole.setRunAsIdentity(this.runAsPrincipalName);
      }

   }

   private SecurityRoleMBean findRole(WebAppDescriptor var1, String var2) {
      SecurityRoleMBean[] var3 = var1.getSecurityRoles();
      if (var3 == null) {
         return null;
      } else {
         SecurityRoleMBean var4 = null;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5] != null && var2.equals(var3[var5].getRoleName())) {
               var4 = var3[var5];
               break;
            }
         }

         return var4;
      }
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
      String var2 = this.getRunAsPrincipalName();
      if (var2 != null && var2.trim().length() != 0) {
         var2 = "";
         var2 = var2 + this.indentStr(var1) + "<" + "run-as-role-assignment" + ">\n";
         var1 += 2;
         var2 = var2 + this.indentStr(var1) + "<" + "role-name" + ">" + this.getRoleName() + "</" + "role-name" + ">\n";
         var2 = var2 + this.indentStr(var1) + "<" + "run-as-principal-name" + ">" + this.getRunAsPrincipalName() + "</" + "run-as-principal-name" + ">\n";
         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</" + "run-as-role-assignment" + ">\n";
         return var2;
      } else {
         return "";
      }
   }
}
