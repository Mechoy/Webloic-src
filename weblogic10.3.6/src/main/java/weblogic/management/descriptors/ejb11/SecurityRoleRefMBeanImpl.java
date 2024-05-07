package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityRoleRefMBeanImpl extends XMLElementMBeanDelegate implements SecurityRoleRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_roleName = false;
   private String roleName;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_roleLink = false;
   private String roleLink;

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

   public String getRoleLink() {
      return this.roleLink;
   }

   public void setRoleLink(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.roleLink;
      this.roleLink = var1;
      this.isSet_roleLink = var1 != null;
      this.checkChange("roleLink", var2, this.roleLink);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security-role-ref");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-name>").append(this.getRoleName()).append("</role-name>\n");
      }

      if (null != this.getRoleLink()) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-link>").append(this.getRoleLink()).append("</role-link>\n");
      }

      var2.append(ToXML.indent(var1)).append("</security-role-ref>\n");
      return var2.toString();
   }
}
