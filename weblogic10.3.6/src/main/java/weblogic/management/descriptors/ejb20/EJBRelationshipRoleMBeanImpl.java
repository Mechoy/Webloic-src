package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBRelationshipRoleMBeanImpl extends XMLElementMBeanDelegate implements EJBRelationshipRoleMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_cmrField = false;
   private CMRFieldMBean cmrField;
   private boolean isSet_multiplicity = false;
   private String multiplicity;
   private boolean isSet_cascadeDelete = false;
   private boolean cascadeDelete;
   private boolean isSet_ejbRelationshipRoleName = false;
   private String ejbRelationshipRoleName;
   private boolean isSet_relationshipRoleSource = false;
   private RelationshipRoleSourceMBean relationshipRoleSource;

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

   public CMRFieldMBean getCMRField() {
      return this.cmrField;
   }

   public void setCMRField(CMRFieldMBean var1) {
      CMRFieldMBean var2 = this.cmrField;
      this.cmrField = var1;
      this.isSet_cmrField = var1 != null;
      this.checkChange("cmrField", var2, this.cmrField);
   }

   public String getMultiplicity() {
      return this.multiplicity;
   }

   public void setMultiplicity(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.multiplicity;
      this.multiplicity = var1;
      this.isSet_multiplicity = var1 != null;
      this.checkChange("multiplicity", var2, this.multiplicity);
   }

   public boolean getCascadeDelete() {
      return this.cascadeDelete;
   }

   public void setCascadeDelete(boolean var1) {
      boolean var2 = this.cascadeDelete;
      this.cascadeDelete = var1;
      this.isSet_cascadeDelete = true;
      this.checkChange("cascadeDelete", var2, this.cascadeDelete);
   }

   public String getEJBRelationshipRoleName() {
      return this.ejbRelationshipRoleName;
   }

   public void setEJBRelationshipRoleName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbRelationshipRoleName;
      this.ejbRelationshipRoleName = var1;
      this.isSet_ejbRelationshipRoleName = var1 != null;
      this.checkChange("ejbRelationshipRoleName", var2, this.ejbRelationshipRoleName);
   }

   public RelationshipRoleSourceMBean getRelationshipRoleSource() {
      return this.relationshipRoleSource;
   }

   public void setRelationshipRoleSource(RelationshipRoleSourceMBean var1) {
      RelationshipRoleSourceMBean var2 = this.relationshipRoleSource;
      this.relationshipRoleSource = var1;
      this.isSet_relationshipRoleSource = var1 != null;
      this.checkChange("relationshipRoleSource", var2, this.relationshipRoleSource);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-relationship-role");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getEJBRelationshipRoleName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-relationship-role-name>").append(this.getEJBRelationshipRoleName()).append("</ejb-relationship-role-name>\n");
      }

      if (null != this.getMultiplicity()) {
         var2.append(ToXML.indent(var1 + 2)).append("<multiplicity>").append(this.getMultiplicity()).append("</multiplicity>\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append(this.getCascadeDelete() ? "<cascade-delete/>\n" : "");
      if (null != this.getRelationshipRoleSource()) {
         var2.append(this.getRelationshipRoleSource().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getCMRField()) {
         var2.append(this.getCMRField().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-relationship-role>\n");
      return var2.toString();
   }
}
