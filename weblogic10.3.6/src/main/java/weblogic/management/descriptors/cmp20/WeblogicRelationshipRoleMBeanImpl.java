package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicRelationshipRoleMBeanImpl extends XMLElementMBeanDelegate implements WeblogicRelationshipRoleMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_groupName = false;
   private String groupName;
   private boolean isSet_relationshipRoleMap = false;
   private RelationshipRoleMapMBean relationshipRoleMap;
   private boolean isSet_relationshipRoleName = false;
   private String relationshipRoleName;
   private boolean isSet_dbCascadeDelete = false;
   private boolean dbCascadeDelete;

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.groupName;
      this.groupName = var1;
      this.isSet_groupName = var1 != null;
      this.checkChange("groupName", var2, this.groupName);
   }

   public RelationshipRoleMapMBean getRelationshipRoleMap() {
      return this.relationshipRoleMap;
   }

   public void setRelationshipRoleMap(RelationshipRoleMapMBean var1) {
      RelationshipRoleMapMBean var2 = this.relationshipRoleMap;
      this.relationshipRoleMap = var1;
      this.isSet_relationshipRoleMap = var1 != null;
      this.checkChange("relationshipRoleMap", var2, this.relationshipRoleMap);
   }

   public String getRelationshipRoleName() {
      return this.relationshipRoleName;
   }

   public void setRelationshipRoleName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.relationshipRoleName;
      this.relationshipRoleName = var1;
      this.isSet_relationshipRoleName = var1 != null;
      this.checkChange("relationshipRoleName", var2, this.relationshipRoleName);
   }

   public boolean getDBCascadeDelete() {
      return this.dbCascadeDelete;
   }

   public void setDBCascadeDelete(boolean var1) {
      boolean var2 = this.dbCascadeDelete;
      this.dbCascadeDelete = var1;
      this.isSet_dbCascadeDelete = true;
      this.checkChange("dbCascadeDelete", var2, this.dbCascadeDelete);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-relationship-role");
      var2.append(">\n");
      if (null != this.getRelationshipRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<relationship-role-name>").append(this.getRelationshipRoleName()).append("</relationship-role-name>\n");
      }

      if (null != this.getGroupName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<group-name>").append(this.getGroupName()).append("</group-name>\n");
      }

      if (null != this.getRelationshipRoleMap()) {
         var2.append(this.getRelationshipRoleMap().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append(this.getDBCascadeDelete() ? "<db-cascade-delete/>\n" : "");
      var2.append(ToXML.indent(var1)).append("</weblogic-relationship-role>\n");
      return var2.toString();
   }
}
