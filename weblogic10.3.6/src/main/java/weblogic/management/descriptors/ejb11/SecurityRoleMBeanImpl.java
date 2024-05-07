package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityRoleMBeanImpl extends XMLElementMBeanDelegate implements SecurityRoleMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_roleName = false;
   private String roleName;
   private boolean isSet_description = false;
   private String description;

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security-role");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-name>").append(this.getRoleName()).append("</role-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</security-role>\n");
      return var2.toString();
   }
}
