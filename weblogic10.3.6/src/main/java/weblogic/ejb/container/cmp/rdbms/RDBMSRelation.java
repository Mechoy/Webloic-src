package weblogic.ejb.container.cmp.rdbms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.utils.Debug;

public final class RDBMSRelation implements Serializable {
   private static final long serialVersionUID = 3661666804538715956L;
   public static final boolean verbose = false;
   public static final boolean debug = false;
   private String m_name = null;
   private String m_tableName = null;
   private RDBMSRole m_role1 = null;
   private RDBMSRole m_role2 = null;

   public void setName(String var1) {
      this.m_name = var1;
   }

   public String toXML() {
      String var1 = new String();
      var1 = var1 + "  &lt;weblogic-rdbms-relation&gt;<p>";
      var1 = var1 + "    &lt;relation-name&gt;" + this.getName() + "&lt;/relation-name&gt;<p>";
      String var2 = this.getTableName();
      if (null != var2 && var2.length() > 0) {
         var1 = var1 + "    &lt;table-name&gt;" + var2 + "&lt;/table-name&gt;<p>";
      }

      RDBMSRole var3 = this.getRole1();
      var1 = var1 + "    &lt;weblogic-relationship-role&gt;<p>";
      var1 = var1 + var3.toXML();
      var1 = var1 + "    &lt;/weblogic-relationship-role&gt;<p>";
      RDBMSRole var4 = this.getRole2();
      if (null != var4) {
         var1 = var1 + var4.toXML();
      }

      var1 = var1 + "  &lt;/weblogic-rdbms-relation&gt;<p>";
      return var1;
   }

   public String getName() {
      return this.m_name;
   }

   public void setTableName(String var1) {
      this.m_tableName = var1;
   }

   public String getTableName() {
      return this.m_tableName;
   }

   public void setRole1(RDBMSRole var1) {
      this.m_role1 = var1;
   }

   public RDBMSRole getRole1() {
      return this.m_role1;
   }

   public void setRole2(RDBMSRole var1) {
      this.m_role2 = var1;
   }

   public RDBMSRole getRole2() {
      return this.m_role2;
   }

   private static void p(String var0) {
      Debug.say("*** <RDBMSRelation> " + var0);
   }

   public String toString() {
      return "[RDBMSRelation name:" + this.m_name + " table:" + this.m_tableName + " role1:" + this.m_role1 + " rol2:" + this.m_role2 + "]";
   }

   public static class RDBMSRole implements Serializable {
      private static final long serialVersionUID = -8166176319205262263L;
      private String m_name = null;
      private String m_groupName = null;
      private Map m_columnMap = new HashMap();
      private Map m_keyTableMap = null;
      boolean dbCascadeDelete = false;
      private boolean queryCachingEnabled = false;
      private String m_fkTableName = null;
      private String m_pkTableName = null;

      public void setName(String var1) {
         this.m_name = var1;
      }

      public String getName() {
         return this.m_name;
      }

      public void setForeignKeyTableName(String var1) {
         this.m_fkTableName = var1;
      }

      public String getForeignKeyTableName() {
         return this.m_fkTableName;
      }

      public void setPrimaryKeyTableName(String var1) {
         this.m_pkTableName = var1;
      }

      public String getPrimaryKeyTableName() {
         return this.m_pkTableName;
      }

      public void setGroupName(String var1) {
         this.m_groupName = var1;
      }

      public String getGroupName() {
         return this.m_groupName;
      }

      public void setColumnMap(Map var1) {
         this.m_columnMap = var1;
      }

      public Map getColumnMap() {
         return this.m_columnMap;
      }

      public void setDBCascadeDelete(boolean var1) {
         this.dbCascadeDelete = var1;
      }

      public boolean getDBCascadeDelete() {
         return this.dbCascadeDelete;
      }

      public void setQueryCachingEnabled(boolean var1) {
         this.queryCachingEnabled = var1;
      }

      public boolean isQueryCachingEnabled() {
         return this.queryCachingEnabled;
      }

      private void p(String var1) {
         Debug.say("*** <RDBMSRole> " + var1);
      }

      public String toString() {
         return "[RDBMSRole name:" + this.m_name + " group:" + this.m_groupName + " columnMap:" + this.m_columnMap + "]";
      }

      public String toXML() {
         String var1 = new String();
         var1 = var1 + "      &lt;relationship-role-name&gt;" + this.getName() + "&lt;/relationship-role-name;<p>";
         String var2 = this.getGroupName();
         if (null != var2 && var2.length() > 0) {
            var1 = var1 + "      &lt;group-name&gt;" + var2 + "&lt;/group-name;<p>";
         }

         Map var3 = this.getColumnMap();
         Set var4 = var3.entrySet();

         String var8;
         for(Iterator var5 = var4.iterator(); var5.hasNext(); var1 = var1 + "        &lt;key-column&gt;" + var8 + "&lt;/key-column&gt;<p>") {
            Map.Entry var6 = (Map.Entry)var5.next();
            String var7 = (String)var6.getKey();
            var8 = (String)var6.getValue();
            var1 = var1 + "        &lt;foreign-key-column&gt;" + var7 + "&lt;/foreign-key-column&gt;<p>";
         }

         if (this.dbCascadeDelete) {
            var1 = var1 + "      &lt;db-on-delete-cascade&gt;true&lt;/db-on-delete-cascade&gt;<p>";
         } else {
            var1 = var1 + "      &lt;db-on-delete-cascade&gt;false&lt;/db-on-delete-cascade&gt;<p>";
         }

         return var1;
      }
   }
}
