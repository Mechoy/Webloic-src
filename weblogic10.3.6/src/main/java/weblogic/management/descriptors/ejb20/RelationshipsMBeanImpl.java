package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class RelationshipsMBeanImpl extends XMLElementMBeanDelegate implements RelationshipsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_ejbRelations = false;
   private List ejbRelations;

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

   public EJBRelationMBean[] getEJBRelations() {
      if (this.ejbRelations == null) {
         return new EJBRelationMBean[0];
      } else {
         EJBRelationMBean[] var1 = new EJBRelationMBean[this.ejbRelations.size()];
         var1 = (EJBRelationMBean[])((EJBRelationMBean[])this.ejbRelations.toArray(var1));
         return var1;
      }
   }

   public void setEJBRelations(EJBRelationMBean[] var1) {
      EJBRelationMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBRelations();
      }

      this.isSet_ejbRelations = true;
      if (this.ejbRelations == null) {
         this.ejbRelations = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbRelations.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbRelations.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBRelations", var2, this.getEJBRelations());
      }

   }

   public void addEJBRelation(EJBRelationMBean var1) {
      this.isSet_ejbRelations = true;
      if (this.ejbRelations == null) {
         this.ejbRelations = Collections.synchronizedList(new ArrayList());
      }

      this.ejbRelations.add(var1);
   }

   public void removeEJBRelation(EJBRelationMBean var1) {
      if (this.ejbRelations != null) {
         this.ejbRelations.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<relationships");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getEJBRelations()) {
         for(int var3 = 0; var3 < this.getEJBRelations().length; ++var3) {
            var2.append(this.getEJBRelations()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</relationships>\n");
      return var2.toString();
   }
}
