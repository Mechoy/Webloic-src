package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicRDBMSRelationMBeanImpl extends XMLElementMBeanDelegate implements WeblogicRDBMSRelationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_relationName = false;
   private String relationName;
   private boolean isSet_weblogicRelationshipRoles = false;
   private List weblogicRelationshipRoles;
   private boolean isSet_tableName = false;
   private String tableName;

   public String getRelationName() {
      return this.relationName;
   }

   public void setRelationName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.relationName;
      this.relationName = var1;
      this.isSet_relationName = var1 != null;
      this.checkChange("relationName", var2, this.relationName);
   }

   public WeblogicRelationshipRoleMBean[] getWeblogicRelationshipRoles() {
      if (this.weblogicRelationshipRoles == null) {
         return new WeblogicRelationshipRoleMBean[0];
      } else {
         WeblogicRelationshipRoleMBean[] var1 = new WeblogicRelationshipRoleMBean[this.weblogicRelationshipRoles.size()];
         var1 = (WeblogicRelationshipRoleMBean[])((WeblogicRelationshipRoleMBean[])this.weblogicRelationshipRoles.toArray(var1));
         return var1;
      }
   }

   public void setWeblogicRelationshipRoles(WeblogicRelationshipRoleMBean[] var1) {
      WeblogicRelationshipRoleMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getWeblogicRelationshipRoles();
      }

      this.isSet_weblogicRelationshipRoles = true;
      if (this.weblogicRelationshipRoles == null) {
         this.weblogicRelationshipRoles = Collections.synchronizedList(new ArrayList());
      } else {
         this.weblogicRelationshipRoles.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.weblogicRelationshipRoles.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("WeblogicRelationshipRoles", var2, this.getWeblogicRelationshipRoles());
      }

   }

   public void addWeblogicRelationshipRole(WeblogicRelationshipRoleMBean var1) {
      this.isSet_weblogicRelationshipRoles = true;
      if (this.weblogicRelationshipRoles == null) {
         this.weblogicRelationshipRoles = Collections.synchronizedList(new ArrayList());
      }

      this.weblogicRelationshipRoles.add(var1);
   }

   public void removeWeblogicRelationshipRole(WeblogicRelationshipRoleMBean var1) {
      if (this.weblogicRelationshipRoles != null) {
         this.weblogicRelationshipRoles.remove(var1);
      }
   }

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.tableName;
      this.tableName = var1;
      this.isSet_tableName = var1 != null;
      this.checkChange("tableName", var2, this.tableName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-rdbms-relation");
      var2.append(">\n");
      if (null != this.getRelationName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<relation-name>").append(this.getRelationName()).append("</relation-name>\n");
      }

      if (null != this.getTableName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<table-name>").append(this.getTableName()).append("</table-name>\n");
      }

      if (null != this.getWeblogicRelationshipRoles()) {
         for(int var3 = 0; var3 < this.getWeblogicRelationshipRoles().length; ++var3) {
            var2.append(this.getWeblogicRelationshipRoles()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-rdbms-relation>\n");
      return var2.toString();
   }
}
