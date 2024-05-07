package weblogic.jdbc.utils.schema;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Column implements Serializable {
   private static final boolean CHAR_TO_BOOLEAN = true;
   private static final boolean NO_BIG_NUMBERS = true;
   private final Table table;
   private final String defaultValue;
   private final String name;
   private final String remarks;
   private final int decimalDigits;
   private final int nullable;
   private final int ordinalPosition;
   private final int radix;
   private final int size;
   private final short type;
   private boolean isIndex;
   private boolean isPrimaryKey;
   private Column importedFrom;
   private final Set exportedTo = new HashSet();

   Column(Table var1, ResultSet var2) throws SQLException {
      this.table = var1;
      this.name = var2.getString("COLUMN_NAME");
      this.type = var2.getShort("DATA_TYPE");
      this.size = var2.getInt("COLUMN_SIZE");
      this.decimalDigits = var2.getInt("DECIMAL_DIGITS");
      this.radix = var2.getInt("NUM_PREC_RADIX");
      this.nullable = var2.getInt("NULLABLE");
      this.remarks = var2.getString("REMARKS");
      this.defaultValue = var2.getString("COLUMN_DEF");
      this.ordinalPosition = var2.getInt("ORDINAL_POSITION");
   }

   public Table getTable() {
      return this.table;
   }

   public String getName() {
      return this.name;
   }

   public short getType() {
      return this.type;
   }

   public int getSize() {
      return this.size;
   }

   public int getDecimalDigits() {
      return this.decimalDigits;
   }

   public int getRadix() {
      return this.radix;
   }

   public int getNullable() {
      return this.nullable;
   }

   public String getRemarks() {
      return this.remarks;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public int getOrdinalPosition() {
      return this.ordinalPosition;
   }

   public boolean isPrimaryKey() {
      return this.isPrimaryKey;
   }

   void setPrimaryKey(boolean var1) {
      this.isPrimaryKey = var1;
   }

   public boolean isIndex() {
      return this.isIndex;
   }

   void setIndex(boolean var1) {
      this.isIndex = var1;
   }

   public Column getImportedFrom() {
      return this.importedFrom;
   }

   void setImportedFrom(Column var1) {
      this.importedFrom = var1;
   }

   public boolean isImportedKey() {
      return this.importedFrom != null;
   }

   public Iterator getExportedTo() {
      return this.exportedTo.iterator();
   }

   void addExportedTo(Column var1) {
      this.exportedTo.add(var1);
   }

   public boolean isExportedKey() {
      return this.exportedTo.size() != 0;
   }

   public String getJavaType() {
      switch (this.getType()) {
         case -7:
            return "boolean";
         case -6:
            return "byte";
         case -5:
            return "long";
         case -4:
         case -3:
         case -2:
         case 2004:
            return "byte[]";
         case -1:
         case 12:
         case 2005:
            return "String";
         case 1:
            if (this.getSize() == 1) {
               return "boolean";
            }

            return "String";
         case 2:
         case 3:
            if (this.getDecimalDigits() > 0) {
               if (this.getSize() < 17) {
                  return "double";
               }

               return "double";
            } else if (this.getSize() < 10) {
               return "int";
            } else {
               if (this.getSize() < 19) {
                  return "long";
               }

               return "long";
            }
         case 4:
            return "int";
         case 5:
            return "short";
         case 6:
         case 8:
            return "double";
         case 7:
            return "float";
         case 91:
         case 92:
         case 93:
            return "java.util.Date";
         default:
            return "Object";
      }
   }

   public String getJavaObjectType() {
      String var1 = this.getJavaType();
      if (var1.equals("String")) {
         return "String";
      } else if (var1.equals("boolean")) {
         return "Boolean";
      } else if (var1.equals("byte")) {
         return "Byte";
      } else if (var1.equals("char")) {
         return "Character";
      } else if (var1.equals("double")) {
         return "Double";
      } else if (var1.equals("float")) {
         return "Float";
      } else if (var1.equals("int")) {
         return "Integer";
      } else if (var1.equals("long")) {
         return "Long";
      } else {
         return var1.equals("short") ? "Short" : var1;
      }
   }

   public String toString() {
      return super.toString() + " - name: '" + this.name + "'";
   }
}
