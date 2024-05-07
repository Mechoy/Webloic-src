package weblogic.jdbc.utils.schema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import weblogic.utils.collections.Iterators;

/** @deprecated */
public class Database implements Serializable {
   private static final String[] TABLE_TYPES = new String[]{"TABLE"};
   private transient DatabaseMetaData meta;
   private final Map tables = new TreeMap();
   private final String catalog;
   private final String schema;
   private final String query;
   private Set excludes;
   private Set includes;
   protected boolean m_resolveTables = true;
   protected boolean m_loadRelations = true;

   public Database(String var1, String var2, String var3) {
      this.catalog = var1;
      this.schema = var2;
      this.query = var3;
   }

   public void setExcludes(Set var1) {
      if (this.includes != null) {
         throw new IllegalStateException("Includes already set.");
      } else {
         this.excludes = var1;
      }
   }

   public void setIncludes(Set var1) {
      if (this.excludes != null) {
         throw new IllegalStateException("Excludes already set.");
      } else {
         this.includes = var1;
      }
   }

   private boolean shouldInclude(Table var1) {
      if (this.includes != null) {
         return this.includes.contains(var1.getName());
      } else if (this.excludes != null) {
         return !this.excludes.contains(var1.getName());
      } else {
         return true;
      }
   }

   public void process(Connection var1) throws SQLException {
      this.meta = var1.getMetaData();
      this.loadTables(this.query);
      if (this.m_resolveTables) {
         Iterator var2 = this.getTables();

         while(var2.hasNext()) {
            Table var3 = (Table)var2.next();
            this.resolveTable(var3);
         }

         if (this.m_loadRelations) {
            this.loadRelations();
            this.loadManyToMany();
         }
      }
   }

   public String getCatalog() {
      return this.catalog;
   }

   public String getSchema() {
      return this.schema;
   }

   public Iterator getTables() {
      return this.tables.values().iterator();
   }

   protected void addTable(Table var1) {
      this.tables.put(var1.getName().toLowerCase(), var1);
   }

   public Table getTable(String var1) {
      return (Table)this.tables.get(var1.toLowerCase());
   }

   public void resolveTable(Table var1) throws SQLException {
      if (!var1.isResolved()) {
         this.loadColumns(var1);
         var1.setResolved(true);
         this.loadIndices(var1);
         this.loadPrimaryKeys(var1);
      }
   }

   protected void loadTables(String var1) throws SQLException {
      ResultSet var2 = this.meta.getTables(this.catalog, this.schema, var1, TABLE_TYPES);

      try {
         while(var2.next()) {
            Table var3 = new Table(this, var2);
            if (this.shouldInclude(var3)) {
               this.addTable(var3);
            }
         }
      } finally {
         var2.close();
      }

   }

   protected void loadColumns(Table var1) throws SQLException {
      ResultSet var2 = this.meta.getColumns(this.catalog, this.schema, var1.getName(), "%");

      try {
         while(var2.next()) {
            var1.addColumn(new Column(var1, var2));
         }
      } finally {
         var2.close();
      }

   }

   protected void loadPrimaryKeys(Table var1) throws SQLException {
      ResultSet var2 = this.meta.getPrimaryKeys(this.catalog, this.schema, var1.getName());

      try {
         while(var2.next()) {
            String var3 = var2.getString("COLUMN_NAME");
            Column var4 = var1.getColumn(var3);
            var4.setPrimaryKey(true);
         }
      } finally {
         var2.close();
      }

   }

   protected void loadIndices(Table var1) throws SQLException {
      ResultSet var2 = null;

      try {
         var2 = this.meta.getIndexInfo(this.catalog, this.schema, var1.getName(), false, false);
      } catch (SQLException var8) {
         return;
      }

      if (var2 != null) {
         try {
            while(var2.next()) {
               String var3 = var2.getString("COLUMN_NAME");
               if (var3 != null && !"SYS_NC_OID$".equals(var3)) {
                  Column var4 = var1.getColumn(var3);
                  if (var4 != null) {
                     var4.setIndex(true);
                  }
               }
            }
         } finally {
            var2.close();
         }

      }
   }

   protected void loadRelations() throws SQLException {
      Iterator var1 = this.getTables();

      while(var1.hasNext()) {
         Table var2 = (Table)var1.next();
         ResultSet var3 = this.meta.getImportedKeys(this.catalog, this.schema, var2.getName());

         try {
            while(var3.next()) {
               String var4 = var3.getString("FKCOLUMN_NAME");
               String var5 = var3.getString("FKTABLE_NAME");
               String var6 = var3.getString("PKCOLUMN_NAME");
               String var7 = var3.getString("PKTABLE_NAME");
               Table var8 = this.getTable(var7);
               Column var9 = var8.getColumn(var6);
               Column var10 = var2.getColumn(var4);
               var10.setImportedFrom(var9);
               var9.addExportedTo(var10);
            }
         } finally {
            var3.close();
         }
      }

   }

   protected void loadManyToMany() throws SQLException {
      Iterator var1 = this.getTables();

      while(var1.hasNext()) {
         Table var2 = (Table)var1.next();
         if (Iterators.equals(var2.getColumns(), var2.getImportedKeys())) {
            var2.setManyToMany(true);
         }
      }

   }
}
