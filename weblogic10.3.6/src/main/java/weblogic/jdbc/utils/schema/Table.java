package weblogic.jdbc.utils.schema;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import weblogic.utils.collections.FilteringIterator;

/** @deprecated */
public final class Table implements Serializable {
   private static final long serialVersionUID = -6006667552519466897L;
   private final Database db;
   private final Map columns = new TreeMap();
   private final String name;
   private final String type;
   private boolean isManyToMany;
   private boolean isResolved;

   Table(Database var1, ResultSet var2) throws SQLException {
      this.db = var1;
      this.name = var2.getString("TABLE_NAME");
      this.type = var2.getString("TABLE_TYPE");
   }

   public Database getDatabase() {
      return this.db;
   }

   public String getName() {
      return this.name;
   }

   public String getType() {
      return this.type;
   }

   public Iterator getColumns() {
      return this.columns.values().iterator();
   }

   public Column getColumn(String var1) {
      return var1 != null && !var1.equals("") ? (Column)this.columns.get(var1.toLowerCase()) : null;
   }

   void addColumn(Column var1) {
      this.columns.put(var1.getName().toLowerCase(), var1);
   }

   public boolean isResolved() {
      return this.isResolved;
   }

   void setResolved(boolean var1) {
      this.isResolved = var1;
   }

   public boolean isManyToMany() {
      return this.isManyToMany;
   }

   void setManyToMany(boolean var1) {
      this.isManyToMany = var1;
   }

   public Iterator getPrimaryKeys() {
      return new FilteringIterator(this.getColumns()) {
         public boolean accept(Object var1) {
            Column var2 = (Column)var1;
            return var2.isPrimaryKey();
         }
      };
   }

   public Iterator getIndices() {
      return new FilteringIterator(this.getColumns()) {
         public boolean accept(Object var1) {
            Column var2 = (Column)var1;
            return var2.isIndex();
         }
      };
   }

   public Iterator getImportedKeys() {
      return new FilteringIterator(this.getColumns()) {
         public boolean accept(Object var1) {
            Column var2 = (Column)var1;
            return var2.isImportedKey();
         }
      };
   }

   public Iterator getExportedKeys() {
      return new FilteringIterator(this.getColumns()) {
         public boolean accept(Object var1) {
            Column var2 = (Column)var1;
            return var2.isExportedKey();
         }
      };
   }

   public String toString() {
      return super.toString() + " - name: '" + this.name + "'";
   }

   public Iterator getOneToManyRelations() {
      return this.getOneToManyRelations(false);
   }

   private Iterator getOneToManyRelations(boolean var1) {
      TreeMap var2 = new TreeMap();
      Iterator var3 = this.getExportedKeys();

      label59:
      while(var3.hasNext()) {
         Column var4 = (Column)var3.next();
         Iterator var5 = var4.getExportedTo();

         while(true) {
            Column var6;
            do {
               if (!var5.hasNext()) {
                  continue label59;
               }

               var6 = (Column)var5.next();
            } while(var1 ^ var6.getTable().isManyToMany());

            Object var7 = (List)var2.get(var6.getTable().getName());
            if (var7 == null) {
               var7 = new ArrayList();
               var2.put(var6.getTable().getName(), var7);
            }

            OneToManyRelation var8 = null;
            Iterator var9 = ((List)var7).iterator();

            while(var9.hasNext()) {
               OneToManyRelation var10 = (OneToManyRelation)var9.next();
               if (var10.exportedColumns.get(var4.getName()) == null) {
                  var8 = var10;
                  break;
               }
            }

            if (var8 == null) {
               var8 = new OneToManyRelation(var6.getTable());
               ((List)var7).add(var8);
            }

            var8.exportedColumns.put(var4.getName(), var6);
         }
      }

      HashSet var11 = new HashSet();
      Iterator var12 = var2.values().iterator();

      while(var12.hasNext()) {
         List var13 = (List)var12.next();
         Iterator var14 = var13.iterator();

         while(var14.hasNext()) {
            var11.add(var14.next());
         }
      }

      return var11.iterator();
   }

   public Iterator getManyToOneRelations() {
      TreeMap var1 = new TreeMap();
      HashSet var2 = new HashSet();
      Iterator var3 = this.getImportedKeys();

      label39:
      while(true) {
         Table var5;
         do {
            if (!var3.hasNext()) {
               return var1.values().iterator();
            }

            Column var4 = (Column)var3.next();
            var5 = var4.getImportedFrom().getTable();
         } while(var2.contains(var5));

         var2.add(var5);
         Iterator var6 = var5.getOneToManyRelations();

         while(true) {
            OneToManyRelation var7;
            do {
               if (!var6.hasNext()) {
                  continue label39;
               }

               var7 = (OneToManyRelation)var6.next();
            } while(var7.getExportedToTable() != this);

            ManyToOneRelation var8 = new ManyToOneRelation(var5);
            Iterator var9 = var7.getExportedColumns();

            while(var9.hasNext()) {
               Column var10 = (Column)var9.next();
               var8.importedColumns.put(var10.getName(), var10);
            }

            var1.put(((Column)var8.getImportedColumns().next()).getName(), var8);
         }
      }
   }

   public Iterator getManyToManyRelations() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.getExportedKeys();

      label39:
      while(var2.hasNext()) {
         Column var3 = (Column)var2.next();
         Iterator var4 = var3.getExportedTo();

         while(true) {
            Table var6;
            do {
               if (!var4.hasNext()) {
                  continue label39;
               }

               Column var5 = (Column)var4.next();
               var6 = var5.getTable();
            } while(!var6.isManyToMany());

            Iterator var7 = var6.getImportedKeys();

            while(var7.hasNext()) {
               Column var8 = (Column)var7.next();
               if (var8.getImportedFrom().getTable() != this) {
                  ManyToManyRelation var9 = (ManyToManyRelation)var1.get(var6);
                  if (var9 == null) {
                     var9 = new ManyToManyRelation(var6, var8.getImportedFrom().getTable());
                     var1.put(var6, var9);
                  }

                  var9.joinColumns.add(var8);
               }
            }
         }
      }

      return var1.values().iterator();
   }

   public final class ManyToManyRelation {
      private final Table targetTable;
      private final Table joinTable;
      private final Set joinColumns;

      private ManyToManyRelation(Table var2, Table var3) {
         this.joinColumns = new HashSet();
         this.joinTable = var2;
         this.targetTable = var3;
      }

      public Table getTargetTable() {
         return this.targetTable;
      }

      public Table getJoinTable() {
         return this.joinTable;
      }

      public Iterator getJoinColumns() {
         return this.joinColumns.iterator();
      }

      // $FF: synthetic method
      ManyToManyRelation(Table var2, Table var3, Object var4) {
         this(var2, var3);
      }
   }

   public final class ManyToOneRelation {
      private final Table pkTable;
      private final Map importedColumns;

      private ManyToOneRelation(Table var2) {
         this.importedColumns = new TreeMap();
         this.pkTable = var2;
      }

      public Table getImportedFromTable() {
         return this.pkTable;
      }

      public Iterator getImportedColumns() {
         return this.importedColumns.values().iterator();
      }

      // $FF: synthetic method
      ManyToOneRelation(Table var2, Object var3) {
         this(var2);
      }
   }

   public final class OneToManyRelation {
      private final Table fkTable;
      private final Map exportedColumns;

      private OneToManyRelation(Table var2) {
         this.exportedColumns = new TreeMap();
         this.fkTable = var2;
      }

      public Table getExportedToTable() {
         return this.fkTable;
      }

      public Iterator getExportedColumns() {
         return this.exportedColumns.values().iterator();
      }

      // $FF: synthetic method
      OneToManyRelation(Table var2, Object var3) {
         this(var2);
      }
   }
}
