package weblogic.management.descriptors.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityRoleAssignmentMBeanImpl extends XMLElementMBeanDelegate implements SecurityRoleAssignmentMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_roleName = false;
   private String roleName;
   private boolean isSet_principalNames = false;
   private List principalNames;
   private boolean isSet_externallyDefined = false;
   private boolean externallyDefined = false;

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

   public String[] getPrincipalNames() {
      if (this.principalNames == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.principalNames.size()];
         var1 = (String[])((String[])this.principalNames.toArray(var1));
         return var1;
      }
   }

   public void setPrincipalNames(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getPrincipalNames();
      }

      this.isSet_principalNames = true;
      if (this.principalNames == null) {
         this.principalNames = Collections.synchronizedList(new ArrayList());
      } else {
         this.principalNames.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.principalNames.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("PrincipalNames", var2, this.getPrincipalNames());
      }

   }

   public void addPrincipalName(String var1) {
      this.isSet_principalNames = true;
      if (this.principalNames == null) {
         this.principalNames = Collections.synchronizedList(new ArrayList());
      }

      this.principalNames.add(var1);
   }

   public void removePrincipalName(String var1) {
      if (this.principalNames != null) {
         this.principalNames.remove(var1);
      }
   }

   public boolean getExternallyDefined() {
      return this.externallyDefined;
   }

   public void setExternallyDefined(boolean var1) {
      boolean var2 = this.externallyDefined;
      this.externallyDefined = var1;
      this.isSet_externallyDefined = true;
      this.checkChange("externallyDefined", var2, this.externallyDefined);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security-role-assignment");
      var2.append(">\n");
      if (null != this.getRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-name>").append(this.getRoleName()).append("</role-name>\n");
      }

      if (this.isSet_principalNames) {
         for(int var3 = 0; var3 < this.getPrincipalNames().length; ++var3) {
            var2.append(ToXML.indent(var1 + 2)).append("<principal-name>").append(this.getPrincipalNames()[var3]).append("</principal-name>\n");
         }
      }

      if (this.isSet_externallyDefined || this.getExternallyDefined()) {
         var2.append(ToXML.indent(var1 + 2)).append("<externally-defined>").append(ToXML.capitalize(Boolean.valueOf(this.getExternallyDefined()).toString())).append("</externally-defined>\n");
      }

      var2.append(ToXML.indent(var1)).append("</security-role-assignment>\n");
      return var2.toString();
   }
}
