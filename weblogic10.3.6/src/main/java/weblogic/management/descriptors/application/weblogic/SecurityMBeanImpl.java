package weblogic.management.descriptors.application.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityMBeanImpl extends XMLElementMBeanDelegate implements SecurityMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_roleAssignments = false;
   private List roleAssignments;
   private boolean isSet_realmName = false;
   private String realmName;

   public SecurityRoleAssignmentMBean[] getRoleAssignments() {
      if (this.roleAssignments == null) {
         return new SecurityRoleAssignmentMBean[0];
      } else {
         SecurityRoleAssignmentMBean[] var1 = new SecurityRoleAssignmentMBean[this.roleAssignments.size()];
         var1 = (SecurityRoleAssignmentMBean[])((SecurityRoleAssignmentMBean[])this.roleAssignments.toArray(var1));
         return var1;
      }
   }

   public void setRoleAssignments(SecurityRoleAssignmentMBean[] var1) {
      SecurityRoleAssignmentMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getRoleAssignments();
      }

      this.isSet_roleAssignments = true;
      if (this.roleAssignments == null) {
         this.roleAssignments = Collections.synchronizedList(new ArrayList());
      } else {
         this.roleAssignments.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.roleAssignments.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("RoleAssignments", var2, this.getRoleAssignments());
      }

   }

   public void addRoleAssignment(SecurityRoleAssignmentMBean var1) {
      this.isSet_roleAssignments = true;
      if (this.roleAssignments == null) {
         this.roleAssignments = Collections.synchronizedList(new ArrayList());
      }

      this.roleAssignments.add(var1);
   }

   public void removeRoleAssignment(SecurityRoleAssignmentMBean var1) {
      if (this.roleAssignments != null) {
         this.roleAssignments.remove(var1);
      }
   }

   public String getRealmName() {
      return this.realmName;
   }

   public void setRealmName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.realmName;
      this.realmName = var1;
      this.isSet_realmName = var1 != null;
      this.checkChange("realmName", var2, this.realmName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security");
      var2.append(">\n");
      if (null != this.getRealmName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<realm-name>").append(this.getRealmName()).append("</realm-name>\n");
      }

      if (null != this.getRoleAssignments()) {
         for(int var3 = 0; var3 < this.getRoleAssignments().length; ++var3) {
            var2.append(this.getRoleAssignments()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</security>\n");
      return var2.toString();
   }
}
