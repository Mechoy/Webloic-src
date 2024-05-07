package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBRelationMBeanImpl extends XMLElementMBeanDelegate implements EJBRelationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbRelationName = false;
   private String ejbRelationName;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_ejbRelationshipRoles = false;
   private List ejbRelationshipRoles;

   public String getEJBRelationName() {
      return this.ejbRelationName;
   }

   public void setEJBRelationName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbRelationName;
      this.ejbRelationName = var1;
      this.isSet_ejbRelationName = var1 != null;
      this.checkChange("ejbRelationName", var2, this.ejbRelationName);
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

   public EJBRelationshipRoleMBean[] getEJBRelationshipRoles() {
      if (this.ejbRelationshipRoles == null) {
         return new EJBRelationshipRoleMBean[0];
      } else {
         EJBRelationshipRoleMBean[] var1 = new EJBRelationshipRoleMBean[this.ejbRelationshipRoles.size()];
         var1 = (EJBRelationshipRoleMBean[])((EJBRelationshipRoleMBean[])this.ejbRelationshipRoles.toArray(var1));
         return var1;
      }
   }

   public void setEJBRelationshipRoles(EJBRelationshipRoleMBean[] var1) {
      EJBRelationshipRoleMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBRelationshipRoles();
      }

      this.isSet_ejbRelationshipRoles = true;
      if (this.ejbRelationshipRoles == null) {
         this.ejbRelationshipRoles = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbRelationshipRoles.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbRelationshipRoles.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBRelationshipRoles", var2, this.getEJBRelationshipRoles());
      }

   }

   public void addEJBRelationshipRole(EJBRelationshipRoleMBean var1) {
      this.isSet_ejbRelationshipRoles = true;
      if (this.ejbRelationshipRoles == null) {
         this.ejbRelationshipRoles = Collections.synchronizedList(new ArrayList());
      }

      this.ejbRelationshipRoles.add(var1);
   }

   public void removeEJBRelationshipRole(EJBRelationshipRoleMBean var1) {
      if (this.ejbRelationshipRoles != null) {
         this.ejbRelationshipRoles.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-relation");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getEJBRelationName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-relation-name>").append(this.getEJBRelationName()).append("</ejb-relation-name>\n");
      }

      if (null != this.getEJBRelationshipRoles()) {
         for(int var3 = 0; var3 < this.getEJBRelationshipRoles().length; ++var3) {
            var2.append(this.getEJBRelationshipRoles()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</ejb-relation>\n");
      return var2.toString();
   }
}
