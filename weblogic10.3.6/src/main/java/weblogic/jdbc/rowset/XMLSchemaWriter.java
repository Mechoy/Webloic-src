package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public final class XMLSchemaWriter implements XMLSchemaConstants {
   private CachedRowSetMetaData metaData;

   public XMLSchemaWriter(WLRowSetMetaData var1) {
      this.metaData = (CachedRowSetMetaData)var1;
   }

   private Attribute getAttr(String var1, String var2) {
      return ElementFactory.createAttribute(var1, var2);
   }

   private Attribute getAttr(XMLName var1, String var2) {
      return ElementFactory.createAttribute(var1, var2);
   }

   private Attribute getAttr(XMLName var1, boolean var2) {
      return ElementFactory.createAttribute(var1, var2 ? "true" : "false");
   }

   private Attribute getAttr(XMLName var1, int var2) {
      return ElementFactory.createAttribute(var1, "" + var2);
   }

   private Attribute getDefaultNS(String var1) {
      return ElementFactory.createNamespaceAttribute((String)null, var1);
   }

   private void writeRowSet(XMLWriter var1) throws IOException, SQLException {
      ArrayList var2 = new ArrayList();
      var2.add(this.getAttr("name", this.metaData.getRowSetName()));
      var2.add(this.getAttr(WLDD_DEFAULT_NAMESPACE, this.metaData.getDefaultNamespace()));
      var2.add(ISROWSET_ATTR);
      if (this.metaData.isReadOnly()) {
         var2.add(ISREADONLY_ATTR);
      } else {
         String var3 = this.metaData.getWriteTableName();
         if (var3 != null && !"".equals(var3)) {
            var2.add(this.getAttr(WLDD_WRITE_TABLE_NAME, var3));
         }

         if (this.metaData.getOptimisticPolicy() != 1) {
            var2.add(this.getAttr(WLDD_OPTPOLICY, this.metaData.getOptimisticPolicyAsString()));
         }

         int var4 = this.metaData.getVersion();
         if (var4 >= 2) {
            var2.add(this.getAttr(WLDD_METADATA_VERSION, var4));
            boolean var5 = this.metaData.isValidMetaData();
            var2.add(this.getAttr(WLDD_VALID_METADATA, var5));
            if (var5) {
               DatabaseMetaDataHolder var6 = this.metaData.getMetaDataHolder();
               var2.add(this.getAttr(WLDD_MAX_CATALOG_NAME_LENGTH, var6.getMaxCatalogNameLength()));
               var2.add(this.getAttr(WLDD_MAX_SCHEMA_NAME_LENGTH, var6.getMaxSchemaNameLength()));
               var2.add(this.getAttr(WLDD_MAX_TABLE_NAME_LENGTH, var6.getMaxTableNameLength()));
               var2.add(this.getAttr(WLDD_IDENTIFIER_QUOTE_STRING, var6.getIdentifierQuoteString()));
               var2.add(this.getAttr(WLDD_CATALOG_SEPARATOR, var6.getCatalogSeparator()));
               var2.add(this.getAttr(WLDD_CATALOG_AT_START, var6.isCatalogAtStart()));
               var2.add(this.getAttr(WLDD_SUPPORTS_SCHEMAS_IN_DML, var6.supportsSchemasInDataManipulation()));
               var2.add(this.getAttr(WLDD_SUPPORTS_CATALOGS_IN_DML, var6.supportsCatalogsInDataManipulation()));
               var2.add(this.getAttr(WLDD_STORES_UPPER_CASE_IDENTIFIERS, var6.storesUpperCaseIdentifiers()));
               var2.add(this.getAttr(WLDD_STORES_LOWER_CASE_IDENTIFIERS, var6.storesLowerCaseIdentifiers()));
               var2.add(this.getAttr(WLDD_STORES_MIXED_CASE_IDENTIFIERS, var6.storesMixedCaseIdentifiers()));
               var2.add(this.getAttr(WLDD_STORES_UPPER_CASE_QUOTED_IDENTIFIERS, var6.storesUpperCaseQuotedIdentifiers()));
               var2.add(this.getAttr(WLDD_STORES_LOWER_CASE_QUOTED_IDENTIFIERS, var6.storesLowerCaseQuotedIdentifiers()));
               var2.add(this.getAttr(WLDD_STORES_MIXED_CASE_QUOTED_IDENTIFIERS, var6.storesMixedCaseQuotedIdentifiers()));
            }
         }
      }

      var1.writeStartElement(ELEMENT_NAME, var2.iterator());
      var1.writeStartElement(COMPLEX_TYPE_NAME);
      var1.writeStartElement(CHOICE_NAME, "maxOccurs", "unbounded");
      this.writeRow(var1);
      var1.writeEndElement(CHOICE_NAME);
      var1.writeEndElement(COMPLEX_TYPE_NAME);
      var1.writeEndElement(ELEMENT_NAME);
   }

   private void writeRow(XMLWriter var1) throws IOException, SQLException {
      Map var2 = this.metaData.getAllRowAttributes();
      if (var2.isEmpty()) {
         var1.writeStartElement(ELEMENT_NAME, "name", this.metaData.getRowName());
      } else {
         ArrayList var3 = new ArrayList();
         var3.add(this.getAttr("name", this.metaData.getRowName()));
         XMLUtils.addAttributesFromPropertyMap(var3, var2);
         var1.writeStartElement(ELEMENT_NAME, var3.iterator());
      }

      var1.writeStartElement(COMPLEX_TYPE_NAME);
      this.writeColumns(var1);
      var1.writeEndElement(COMPLEX_TYPE_NAME);
      var1.writeEndElement(ELEMENT_NAME);
   }

   private void writeColumns(XMLWriter var1) throws IOException, SQLException {
      var1.writeStartElement(SEQUENCE_NAME);

      for(int var2 = 0; var2 < this.metaData.getColumnCount(); ++var2) {
         ArrayList var3 = new ArrayList();
         var3.add(this.getAttr("name", this.metaData.getColumnName(var2 + 1)));
         int var4 = this.metaData.getColumnType(var2 + 1);
         XMLName var5 = TypeMapper.getXSDType(var4);
         var3.add(this.getAttr("type", var5.getPrefix() + ":" + var5.getLocalName()));
         var3.add(this.getAttr(WLDD_JDBC_TYPE, TypeMapper.getJDBCTypeAsString(var4)));
         var3.add(this.getAttr("minOccurs", "0"));
         this.metaData.setXMLAttributes(var2, var3);
         var1.writeStartElement(ELEMENT_NAME, var3.iterator());
         var1.writeEndElement(ELEMENT_NAME);
      }

      var1.writeEndElement(SEQUENCE_NAME);
      if (!this.metaData.isReadOnly()) {
         var1.writeStartElement(ANY_ATTRIBUTE_NAME, "namespace", "http://www.bea.com/2002/10/weblogicdata", "processContents", "skip");
         var1.writeEndElement(ANY_ATTRIBUTE_NAME);
      }

   }

   public void writeSchema(XMLOutputStream var1) throws IOException, SQLException {
      XMLWriter var2 = new XMLWriter(var1);
      Attribute var3 = this.getAttr("targetNamespace", this.metaData.getDefaultNamespace());
      Attribute var4 = this.getDefaultNS(this.metaData.getDefaultNamespace());
      ArrayList var5 = new ArrayList();
      var5.add(var3);
      var5.add(var4);
      var5.add(SCHEMA_NAMESPACE);
      var5.add(WLDATA_NAMESPACE);
      var5.add(ELEMENT_FORM_DEFAULT_ATTR);
      var5.add(ATTRIBUTE_FORM_DEFAULT_ATTR);
      var2.writeStartElement(SCHEMA_NAME, var5.iterator());
      this.writeRowSet(var2);
      var2.writeEndElement(SCHEMA_NAME);
      var1.flush();
   }
}
