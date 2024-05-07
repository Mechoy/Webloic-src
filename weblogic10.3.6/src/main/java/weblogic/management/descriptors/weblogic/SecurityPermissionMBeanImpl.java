package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityPermissionMBeanImpl extends XMLElementMBeanDelegate implements SecurityPermissionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_securityPermissionSpec = false;
   private String securityPermissionSpec;

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

   public String getSecurityPermissionSpec() {
      return this.securityPermissionSpec;
   }

   public void setSecurityPermissionSpec(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.securityPermissionSpec;
      this.securityPermissionSpec = var1;
      this.isSet_securityPermissionSpec = var1 != null;
      this.checkChange("securityPermissionSpec", var2, this.securityPermissionSpec);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security-permission");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</security-permission>\n");
      return var2.toString();
   }
}
