package weblogic.management.descriptors.cmp11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicRDBMSBeanMBeanImpl extends XMLElementMBeanDelegate implements WeblogicRDBMSBeanMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_fieldMaps = false;
   private List fieldMaps;
   private boolean isSet_enableTunedUpdates = false;
   private boolean enableTunedUpdates = true;
   private boolean isSet_poolName = false;
   private String poolName;
   private boolean isSet_tableName = false;
   private String tableName;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_dataSourceName = false;
   private String dataSourceName;
   private boolean isSet_finders = false;
   private List finders;

   public FieldMapMBean[] getFieldMaps() {
      if (this.fieldMaps == null) {
         return new FieldMapMBean[0];
      } else {
         FieldMapMBean[] var1 = new FieldMapMBean[this.fieldMaps.size()];
         var1 = (FieldMapMBean[])((FieldMapMBean[])this.fieldMaps.toArray(var1));
         return var1;
      }
   }

   public void setFieldMaps(FieldMapMBean[] var1) {
      FieldMapMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFieldMaps();
      }

      this.isSet_fieldMaps = true;
      if (this.fieldMaps == null) {
         this.fieldMaps = Collections.synchronizedList(new ArrayList());
      } else {
         this.fieldMaps.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.fieldMaps.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("FieldMaps", var2, this.getFieldMaps());
      }

   }

   public void addFieldMap(FieldMapMBean var1) {
      this.isSet_fieldMaps = true;
      if (this.fieldMaps == null) {
         this.fieldMaps = Collections.synchronizedList(new ArrayList());
      }

      this.fieldMaps.add(var1);
   }

   public void removeFieldMap(FieldMapMBean var1) {
      if (this.fieldMaps != null) {
         this.fieldMaps.remove(var1);
      }
   }

   public boolean getEnableTunedUpdates() {
      return this.enableTunedUpdates;
   }

   public void setEnableTunedUpdates(boolean var1) {
      boolean var2 = this.enableTunedUpdates;
      this.enableTunedUpdates = var1;
      this.isSet_enableTunedUpdates = true;
      this.checkChange("enableTunedUpdates", var2, this.enableTunedUpdates);
   }

   public String getPoolName() {
      return this.poolName;
   }

   public void setPoolName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.poolName;
      this.poolName = var1;
      this.isSet_poolName = var1 != null;
      this.checkChange("poolName", var2, this.poolName);
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

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbName;
      this.ejbName = var1;
      this.isSet_ejbName = var1 != null;
      this.checkChange("ejbName", var2, this.ejbName);
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.dataSourceName;
      this.dataSourceName = var1;
      this.isSet_dataSourceName = var1 != null;
      this.checkChange("dataSourceName", var2, this.dataSourceName);
   }

   public FinderMBean[] getFinders() {
      if (this.finders == null) {
         return new FinderMBean[0];
      } else {
         FinderMBean[] var1 = new FinderMBean[this.finders.size()];
         var1 = (FinderMBean[])((FinderMBean[])this.finders.toArray(var1));
         return var1;
      }
   }

   public void setFinders(FinderMBean[] var1) {
      FinderMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFinders();
      }

      this.isSet_finders = true;
      if (this.finders == null) {
         this.finders = Collections.synchronizedList(new ArrayList());
      } else {
         this.finders.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.finders.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Finders", var2, this.getFinders());
      }

   }

   public void addFinder(FinderMBean var1) {
      this.isSet_finders = true;
      if (this.finders == null) {
         this.finders = Collections.synchronizedList(new ArrayList());
      }

      this.finders.add(var1);
   }

   public void removeFinder(FinderMBean var1) {
      if (this.finders != null) {
         this.finders.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-rdbms-bean");
      var2.append(">\n");
      if (null != this.getEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-name>").append(this.getEJBName()).append("</ejb-name>\n");
      }

      if (this.isSet_poolName) {
         if (null != this.getPoolName()) {
            var2.append(ToXML.indent(var1 + 2)).append("<pool-name>").append(this.getPoolName()).append("</pool-name>\n");
         }
      } else if (this.isSet_dataSourceName && null != this.getDataSourceName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<data-source-name>").append(this.getDataSourceName()).append("</data-source-name>\n");
      }

      if (null != this.getTableName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<table-name>").append(this.getTableName()).append("</table-name>\n");
      }

      int var3;
      if (null != this.getFieldMaps()) {
         for(var3 = 0; var3 < this.getFieldMaps().length; ++var3) {
            var2.append(this.getFieldMaps()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getFinders()) {
         for(var3 = 0; var3 < this.getFinders().length; ++var3) {
            var2.append(this.getFinders()[var3].toXML(var1 + 2));
         }
      }

      if (this.isSet_enableTunedUpdates || !this.getEnableTunedUpdates()) {
         var2.append(ToXML.indent(var1 + 2)).append("<enable-tuned-updates>").append(ToXML.capitalize(Boolean.valueOf(this.getEnableTunedUpdates()).toString())).append("</enable-tuned-updates>\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-rdbms-bean>\n");
      return var2.toString();
   }
}
