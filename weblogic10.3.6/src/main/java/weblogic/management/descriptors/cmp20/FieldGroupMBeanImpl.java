package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class FieldGroupMBeanImpl extends XMLElementMBeanDelegate implements FieldGroupMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_groupName = false;
   private String groupName;
   private boolean isSet_cmrFields = false;
   private List cmrFields;
   private boolean isSet_cmpFields = false;
   private List cmpFields;

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

   public String[] getCMRFields() {
      if (this.cmrFields == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.cmrFields.size()];
         var1 = (String[])((String[])this.cmrFields.toArray(var1));
         return var1;
      }
   }

   public void setCMRFields(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getCMRFields();
      }

      this.isSet_cmrFields = true;
      if (this.cmrFields == null) {
         this.cmrFields = Collections.synchronizedList(new ArrayList());
      } else {
         this.cmrFields.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.cmrFields.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("CMRFields", var2, this.getCMRFields());
      }

   }

   public void addCMRField(String var1) {
      this.isSet_cmrFields = true;
      if (this.cmrFields == null) {
         this.cmrFields = Collections.synchronizedList(new ArrayList());
      }

      this.cmrFields.add(var1);
   }

   public void removeCMRField(String var1) {
      if (this.cmrFields != null) {
         this.cmrFields.remove(var1);
      }
   }

   public String[] getCMPFields() {
      if (this.cmpFields == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.cmpFields.size()];
         var1 = (String[])((String[])this.cmpFields.toArray(var1));
         return var1;
      }
   }

   public void setCMPFields(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getCMPFields();
      }

      this.isSet_cmpFields = true;
      if (this.cmpFields == null) {
         this.cmpFields = Collections.synchronizedList(new ArrayList());
      } else {
         this.cmpFields.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.cmpFields.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("CMPFields", var2, this.getCMPFields());
      }

   }

   public void addCMPField(String var1) {
      this.isSet_cmpFields = true;
      if (this.cmpFields == null) {
         this.cmpFields = Collections.synchronizedList(new ArrayList());
      }

      this.cmpFields.add(var1);
   }

   public void removeCMPField(String var1) {
      if (this.cmpFields != null) {
         this.cmpFields.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<field-group");
      var2.append(">\n");
      if (null != this.getGroupName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<group-name>").append(this.getGroupName()).append("</group-name>\n");
      }

      int var3;
      if (this.isSet_cmrFields) {
         for(var3 = 0; var3 < this.getCMRFields().length; ++var3) {
            var2.append(ToXML.indent(var1 + 2)).append("<cmr-field>").append(this.getCMRFields()[var3]).append("</cmr-field>\n");
         }
      }

      if (this.isSet_cmpFields) {
         for(var3 = 0; var3 < this.getCMPFields().length; ++var3) {
            var2.append(ToXML.indent(var1 + 2)).append("<cmp-field>").append(this.getCMPFields()[var3]).append("</cmp-field>\n");
         }
      }

      var2.append(ToXML.indent(var1)).append("</field-group>\n");
      return var2.toString();
   }
}
