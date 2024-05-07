package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.utils.AssertionError;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public final class XMLInstanceWriter implements XMLSchemaConstants {
   private WLRowSetInternal rowSet;
   private WLRowSetMetaData metaData;
   private static ColumnFilter all = new ColumnFilter() {
      public boolean include(CachedRow var1, int var2) {
         return true;
      }
   };
   private static ColumnFilter mod = new ColumnFilter() {
      public boolean include(CachedRow var1, int var2) throws SQLException {
         return var1.isModified(var2 + 1);
      }
   };

   public XMLInstanceWriter(WLCachedRowSet var1) throws SQLException {
      this.rowSet = (WLRowSetInternal)var1;
      this.metaData = (WLRowSetMetaData)var1.getMetaData();
   }

   private Attribute getAttr(String var1, String var2) {
      return ElementFactory.createAttribute(var1, var2);
   }

   private Attribute getAttr(XMLName var1, String var2) {
      return ElementFactory.createAttribute(var1, var2);
   }

   private Attribute getDefaultNS(String var1) {
      return ElementFactory.createNamespaceAttribute((String)null, var1);
   }

   public void writeXML(XMLOutputStream var1, int var2) throws SQLException, IOException {
      XMLWriter var3 = new XMLWriter(var1, this.metaData.getDefaultNamespace());
      RowWriter var4 = XMLInstanceWriter.RowWriter.getWriter(var3, this.metaData, var2);
      ArrayList var5 = new ArrayList();
      Attribute var6 = this.getDefaultNS(this.metaData.getDefaultNamespace());
      var5.add(var6);
      var5.add(SCHEMA_INSTANCE_NAMESPACE);
      var5.add(WLDATA_NAMESPACE);
      ArrayList var7 = new ArrayList();
      var7.add(this.getAttr(SCHEMA_LOCATION_NAME, this.metaData.getXMLSchemaLocation()));
      var3.writeStartElement(this.metaData.getRowSetName(), var7.iterator(), var5.iterator());
      List var8 = this.rowSet.getCachedRows();
      int var9 = 0;
      Iterator var10 = var8.iterator();

      while(var10.hasNext()) {
         CachedRow var11 = (CachedRow)var10.next();
         var4.writeRow(var11, var9++);
      }

      var3.writeEndElement(this.metaData.getRowSetName());
      var1.flush();
   }

   private static class AllRowsWriter extends RowWriter {
      AllRowsWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         this.writeRow(var1, true, var2, XMLInstanceWriter.all);
         if (var1.isUpdatedRow() && !var1.isInsertRow()) {
            this.writeRow(var1, false, var2, XMLInstanceWriter.mod);
         }

      }
   }

   private static class ChangedAllWriter extends RowWriter {
      ChangedAllWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         if (var1.isInsertRow() || var1.isDeletedRow() || var1.isUpdatedRow()) {
            this.writeRow(var1, true, var2, XMLInstanceWriter.all);
            if (var1.isUpdatedRow()) {
               this.writeRow(var1, false, var2, XMLInstanceWriter.mod);
            }
         }

      }
   }

   private static class CurrentAllWriter extends RowWriter {
      CurrentAllWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         this.writeRow(var1, true, var2, XMLInstanceWriter.all);
      }
   }

   private static class ChangedOriginalWriter extends RowWriter {
      ChangedOriginalWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         if (var1.isUpdatedRow()) {
            this.writeRow(var1, false, var2, XMLInstanceWriter.mod);
         }

      }
   }

   private static class ChangedCurrentWriter extends RowWriter {
      ChangedCurrentWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         if (var1.isInsertRow() || var1.isDeletedRow() || var1.isUpdatedRow()) {
            this.writeRow(var1, true, var2, XMLInstanceWriter.all);
         }

      }
   }

   private static class UnchangedCurrentWriter extends RowWriter {
      UnchangedCurrentWriter(XMLWriter var1, WLRowSetMetaData var2) {
         super(var1, var2);
      }

      void writeRow(CachedRow var1, int var2) throws SQLException, IOException {
         if (!var1.isInsertRow() && !var1.isDeletedRow() && !var1.isUpdatedRow()) {
            this.writeRow(var1, true, var2, XMLInstanceWriter.all);
         }
      }
   }

   private abstract static class RowWriter {
      private WLRowSetMetaData metaData;
      private XMLWriter writer;

      static RowWriter getWriter(XMLWriter var0, WLRowSetMetaData var1, int var2) {
         switch (var2) {
            case 1:
               return new UnchangedCurrentWriter(var0, var1);
            case 2:
               return new ChangedCurrentWriter(var0, var1);
            case 4:
               return new ChangedOriginalWriter(var0, var1);
            case 8:
               return new CurrentAllWriter(var0, var1);
            case 16:
               return new ChangedAllWriter(var0, var1);
            case 32:
               return new AllRowsWriter(var0, var1);
            default:
               throw new AssertionError("Unexpected rowStates: " + var2);
         }
      }

      RowWriter(XMLWriter var1, WLRowSetMetaData var2) {
         this.writer = var1;
         this.metaData = var2;
      }

      abstract void writeRow(CachedRow var1, int var2) throws SQLException, IOException;

      private String getRowStateString(CachedRow var1, boolean var2) {
         if (var1.isInsertRow()) {
            return "Inserted";
         } else if (var1.isUpdatedRow() && var2) {
            return "Modified";
         } else if (var1.isUpdatedRow() && !var2) {
            return "ModifiedOriginal";
         } else if (var1.isDeletedRow()) {
            return "DeletedOriginal";
         } else {
            throw new AssertionError("Unexpected row: " + var1);
         }
      }

      protected void writeRow(CachedRow var1, boolean var2, int var3, ColumnFilter var4) throws SQLException, IOException {
         if (var2 && !var1.isUpdatedRow() && !var1.isInsertRow() && !var1.isDeletedRow()) {
            this.writer.writeStartElement(this.metaData.getRowName(), XMLSchemaConstants.WLDD_ROWID, String.valueOf(var3));
         } else {
            this.writer.writeStartElement(this.metaData.getRowName(), XMLSchemaConstants.WLDD_ROWID, String.valueOf(var3), XMLSchemaConstants.WLDD_ROWSTATE, this.getRowStateString(var1, var2));
         }

         for(int var5 = 0; var5 < var1.getColumnCount(); ++var5) {
            if (var4.include(var1, var5)) {
               String var6 = this.metaData.getColumnName(var5 + 1);
               Object var7 = null;
               if (var2) {
                  if (var1.isDeletedRow() && var1.isUpdatedRow()) {
                     var7 = var1.getOldColumn(var5 + 1);
                  } else {
                     var7 = var1.getColumn(var5 + 1);
                  }
               } else {
                  var7 = var1.getOldColumn(var5 + 1);
               }

               if (var7 == null) {
                  this.writer.writeStartElement(var6, XMLSchemaConstants.NIL_NAME, "true");
               } else {
                  this.writer.writeStartElement(var6);
                  this.writer.writeCharacterData(TypeMapper.getXMLValue(this.metaData.getColumnType(var5 + 1), var7));
               }

               this.writer.writeEndElement(var6);
            }
         }

         this.writer.writeEndElement(this.metaData.getRowName());
      }
   }

   private interface ColumnFilter {
      boolean include(CachedRow var1, int var2) throws SQLException;
   }
}
