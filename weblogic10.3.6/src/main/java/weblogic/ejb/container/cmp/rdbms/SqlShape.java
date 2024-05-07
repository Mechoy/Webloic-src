package weblogic.ejb.container.cmp.rdbms;

import java.util.ArrayList;
import java.util.List;

public class SqlShape {
   private String sqlShapeName;
   private List tables = new ArrayList();
   private String[] ejbRelationNames = null;
   private int passThroughColumns = 0;

   public String getSqlShapeName() {
      return this.sqlShapeName;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof SqlShape)) {
         return false;
      } else {
         SqlShape var2 = (SqlShape)var1;
         return this.sqlShapeName.equals(var2.getSqlShapeName());
      }
   }

   public int hashCode() {
      return this.sqlShapeName.hashCode();
   }

   public void setSqlShapeName(String var1) {
      this.sqlShapeName = var1;
   }

   public List getTables() {
      return this.tables;
   }

   public void addTable(Table var1) {
      this.tables.add(var1);
   }

   public void addResultColumn() {
      this.tables.add((Object)null);
   }

   public String[] getEjbRelationNames() {
      return this.ejbRelationNames;
   }

   public void setEjbRelationNames(String[] var1) {
      this.ejbRelationNames = var1;
      if (this.ejbRelationNames != null && this.ejbRelationNames.length == 0) {
         this.ejbRelationNames = null;
      }

   }

   public int getPassThroughColumns() {
      return this.passThroughColumns;
   }

   public void setPassThroughColumns(int var1) {
      this.passThroughColumns = var1;
   }

   public static class Table {
      private String name;
      private List columns = new ArrayList();
      private List ejbRelationshipRoleNames = null;

      public List getColumns() {
         return this.columns;
      }

      public String getName() {
         return this.name;
      }

      public void setColumns(List var1) {
         this.columns = var1;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void addEjbRelationshipRoleName(String var1) {
         if (this.ejbRelationshipRoleNames == null) {
            this.ejbRelationshipRoleNames = new ArrayList();
         }

         this.ejbRelationshipRoleNames.add(var1);
      }

      public List getEjbRelationshipRoleNames() {
         return this.ejbRelationshipRoleNames;
      }
   }
}
