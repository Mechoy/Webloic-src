package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class RunAsRoleAssignmentMBeanImpl extends XMLElementMBeanDelegate implements RunAsRoleAssignmentMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_roleName = false;
   private String roleName;
   private boolean isSet_runAsPrincipalName = false;
   private String runAsPrincipalName;

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.roleName;
      this.roleName = var1;
      this.isSet_roleName = var1 != null;
      this.checkChange("roleName", var2, this.roleName);
   }

   public String getRunAsPrincipalName() {
      return this.runAsPrincipalName;
   }

   public void setRunAsPrincipalName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.runAsPrincipalName;
      this.runAsPrincipalName = var1;
      this.isSet_runAsPrincipalName = var1 != null;
      this.checkChange("runAsPrincipalName", var2, this.runAsPrincipalName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<run-as-role-assignment");
      var2.append(">\n");
      if (null != this.getRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-name>").append(this.getRoleName()).append("</role-name>\n");
      }

      if (null != this.getRunAsPrincipalName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<run-as-principal-name>").append(this.getRunAsPrincipalName()).append("</run-as-principal-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</run-as-role-assignment>\n");
      return var2.toString();
   }
}
