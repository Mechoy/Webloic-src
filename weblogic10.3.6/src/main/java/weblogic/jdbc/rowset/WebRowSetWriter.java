package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.sql.rowset.spi.SyncProvider;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public final class WebRowSetWriter implements XMLSchemaConstants {
   private CachedRowSetImpl rowSet;
   private WLRowSetMetaData metaData;

   public WebRowSetWriter(CachedRowSetImpl var1) throws SQLException {
      this.rowSet = var1;
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
      XMLWriter var3 = new XMLWriter(var1, "http://java.sun.com/xml/ns/jdbc");
      ArrayList var4 = new ArrayList();
      Attribute var5 = this.getDefaultNS("http://java.sun.com/xml/ns/jdbc");
      var4.add(var5);
      var4.add(SCHEMA_INSTANCE_NAMESPACE);
      ArrayList var6 = new ArrayList();
      var6.add(this.getAttr(SCHEMA_LOCATION_NAME, "http://java.sun.com/xml/ns/jdbc/webrowset.xsd"));
      var3.writeStartElement("webRowSet", var6.iterator(), var4.iterator());
      this.writeProperties(var3);
      this.writeMetaData(var3);
      this.writeData(var3);
      var3.writeEndElement("webRowSet");
      var1.flush();
   }

   private void writeProperties(XMLWriter var1) throws SQLException, IOException {
      Object var2 = null;
      var1.writeStartElement("properties");
      var1.writeSimpleElements("command", this.rowSet.getCommand());
      var1.writeSimpleElements("concurrency", this.rowSet.getConcurrency());
      var1.writeSimpleElements("datasource", this.rowSet.getDataSourceName());
      var1.writeSimpleElements("escape-processing", this.rowSet.getEscapeProcessing());
      var1.writeSimpleElements("fetch-direction", this.rowSet.getFetchDirection());
      var1.writeSimpleElements("fetch-size", this.rowSet.getFetchSize());
      var1.writeSimpleElements("isolation-level", this.rowSet.getTransactionIsolation());
      var1.writeStartElement("key-columns");
      int[] var3 = this.rowSet.getKeyColumns();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1.writeSimpleElements("column", this.metaData.getColumnName(var3[var4]));
         }
      }

      var1.writeEndElement("key-columns");
      var1.writeStartElement("map");
      Map var7 = this.rowSet.getTypeMap();
      if (var7 != null && !var7.isEmpty()) {
         Iterator var5 = var7.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var1.writeSimpleElements("type", var6);
            var1.writeSimpleElements("class", ((Class)var7.get(var6)).getName());
         }
      }

      var1.writeEndElement("map");
      var1.writeSimpleElements("max-field-size", this.rowSet.getMaxFieldSize());
      var1.writeSimpleElements("max-rows", this.rowSet.getMaxRows());
      var1.writeSimpleElements("query-timeout", this.rowSet.getQueryTimeout());
      var1.writeSimpleElements("read-only", this.rowSet.isReadOnly());
      var1.writeSimpleElements("rowset-type", this.rowSet.getType());
      var1.writeSimpleElements("show-deleted", this.rowSet.getShowDeleted());
      var1.writeSimpleElements("table-name", this.rowSet.getTableName());
      var1.writeSimpleElements("url", this.rowSet.getUrl());
      var1.writeStartElement("sync-provider");
      SyncProvider var8 = this.rowSet.getSyncProvider();
      var1.writeSimpleElements("sync-provider-name", var8.getProviderID());
      var1.writeSimpleElements("sync-provider-vendor", "unknown");
      var1.writeSimpleElements("sync-provider-version", "unknown");
      var1.writeSimpleElements("sync-provider-grade", var8.getProviderGrade());
      var1.writeSimpleElements("data-source-lock", var8.getDataSourceLock());
      var1.writeEndElement("sync-provider");
      var1.writeEndElement("properties");
   }

   private void writeMetaData(XMLWriter var1) throws SQLException, IOException {
      var1.writeStartElement("metadata");
      var1.writeSimpleElements("column-count", this.metaData.getColumnCount());

      for(int var2 = 1; var2 <= this.metaData.getColumnCount(); ++var2) {
         var1.writeStartElement("column-definition");
         var1.writeSimpleElements("column-index", var2);
         var1.writeSimpleElements("auto-increment", this.metaData.isAutoIncrement(var2));
         var1.writeSimpleElements("column-class-name", this.metaData.isDefinitelyWritable(var2));
         var1.writeSimpleElements("definitely-writable", this.metaData.isDefinitelyWritable(var2));
         var1.writeSimpleElements("case-sensitive", this.metaData.isCaseSensitive(var2));
         var1.writeSimpleElements("currency", this.metaData.isCurrency(var2));
         var1.writeSimpleElements("nullable", this.metaData.isNullable(var2));
         var1.writeSimpleElements("signed", this.metaData.isSigned(var2));
         var1.writeSimpleElements("searchable", this.metaData.isSearchable(var2));
         var1.writeSimpleElements("column-display-size", this.metaData.getColumnDisplaySize(var2));
         var1.writeSimpleElements("column-label", this.metaData.getColumnLabel(var2));
         var1.writeSimpleElements("column-name", this.metaData.getColumnName(var2));
         var1.writeSimpleElements("column-class-name", this.metaData.getColumnClassName(var2));
         var1.writeSimpleElements("schema-name", this.metaData.getSchemaName(var2));
         var1.writeSimpleElements("column-precision", this.metaData.getPrecision(var2));
         var1.writeSimpleElements("column-scale", this.metaData.getScale(var2));
         var1.writeSimpleElements("table-name", this.metaData.getTableName(var2));
         var1.writeSimpleElements("catalog-name", this.metaData.getCatalogName(var2));
         var1.writeSimpleElements("column-type", this.metaData.getColumnType(var2));
         var1.writeSimpleElements("column-type-name", this.metaData.getColumnTypeName(var2));
         var1.writeEndElement("column-definition");
      }

      var1.writeEndElement("metadata");
   }

   private void writeData(XMLWriter var1) throws SQLException, IOException {
      var1.writeStartElement("data");
      Iterator var2 = this.rowSet.getCachedRows().iterator();

      while(var2.hasNext()) {
         this.writeRow((CachedRow)var2.next(), var1);
      }

      var1.writeEndElement("data");
   }

   private void writeRow(CachedRow var1, XMLWriter var2) throws SQLException, IOException {
      if (var1.isInsertRow()) {
         var2.writeStartElement("insertRow");
         this.writeColumns(var1, var2);
         var2.writeEndElement("insertRow");
      } else if (var1.isDeletedRow()) {
         var2.writeStartElement("deleteRow");
         this.writeColumns(var1, var2);
         var2.writeEndElement("deleteRow");
      } else if (var1.isUpdatedRow()) {
         var2.writeStartElement("modifyRow");
         this.writeColumns(var1, var2);
         var2.writeEndElement("modifyRow");
      } else {
         var2.writeStartElement("currentRow");
         this.writeColumns(var1, var2);
         var2.writeEndElement("currentRow");
      }

   }

   private void writeColumns(CachedRow var1, XMLWriter var2) throws SQLException, IOException {
      for(int var3 = 0; var3 < var1.getColumnCount(); ++var3) {
         if (var1.isUpdatedRow() && var1.isModified(var3 + 1)) {
            var2.writeStartElement("columnValue");
            var2.writeCharacterData(TypeMapper.getXMLValue(this.metaData.getColumnType(var3 + 1), var1.getOldColumn(var3 + 1)));
            var2.writeEndElement("columnValue");
            var2.writeStartElement("updateValue");
            var2.writeCharacterData(TypeMapper.getXMLValue(this.metaData.getColumnType(var3 + 1), var1.getColumn(var3 + 1)));
            var2.writeEndElement("updateValue");
         } else {
            var2.writeStartElement("columnValue");
            var2.writeCharacterData(TypeMapper.getXMLValue(this.metaData.getColumnType(var3 + 1), var1.getColumn(var3 + 1)));
            var2.writeEndElement("columnValue");
         }
      }

   }
}
