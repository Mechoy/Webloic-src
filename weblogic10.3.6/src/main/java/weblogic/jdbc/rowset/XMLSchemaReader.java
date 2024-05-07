package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.util.TypeFilter;

public final class XMLSchemaReader implements XMLSchemaConstants {
   private static final boolean DEBUG = true;
   private static final boolean VERBOSE = true;
   private CachedRowSetMetaData metaData;
   private XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();

   public XMLSchemaReader(WLRowSetMetaData var1) {
      this.metaData = (CachedRowSetMetaData)var1;
   }

   public void loadSchema(XMLInputStream var1) throws IOException, SQLException {
      this.parse(this.factory.newInputStream(var1, new TypeFilter(2)));
   }

   private void parse(XMLInputStream var1) throws IOException, SQLException {
      while(true) {
         if (var1.skip(ELEMENT_NAME)) {
            StartElement var2 = this.getNext(var1);
            String var3 = XMLUtils.getRequiredAttribute(var2, "name").getValue();
            if (!this.metaData.claimSchema(var3)) {
               continue;
            }

            this.parseRowSet(var1, var2);
            return;
         }

         throw new IOException("XMLInputStream did not contain a row set matching the name: " + this.metaData.getRowSetName() + ".  Please ensure your XMLInputStream does contain a rowset " + "schema.  Also ensure that the WLRowSetMetaData.getRowSetName()" + " matches the name attribute in the schema");
      }
   }

   private void parseRowSet(XMLInputStream var1, StartElement var2) throws IOException, SQLException {
      String var3 = XMLUtils.getRequiredAttribute(var2, WLDD_DEFAULT_NAMESPACE).getValue();
      this.metaData.setDefaultNamespace(var3);
      String var4 = XMLUtils.getRequiredAttribute(var2, "name").getValue();
      this.metaData.setRowSetName(var4);
      String var5 = XMLUtils.getOptionalStringAttribute(var2, WLDD_WRITE_TABLE_NAME);
      this.metaData.setWriteTableName(var5);
      String var6 = XMLUtils.getOptionalStringAttribute(var2, WLDD_OPTPOLICY);
      if (var6 != null) {
         this.metaData.setOptimisticPolicyAsString(var6);
      }

      int var7;
      try {
         var7 = Integer.parseInt(XMLUtils.getOptionalStringAttribute(var2, WLDD_METADATA_VERSION));
      } catch (NumberFormatException var10) {
         var7 = 1;
      }

      this.metaData.setVersion(var7);
      if (var7 >= 2) {
         boolean var8 = XMLUtils.getOptionalBooleanAttribute(var2, WLDD_VALID_METADATA);
         DatabaseMetaDataHolder var9 = null;
         if (var8) {
            var9 = new DatabaseMetaDataHolder(Integer.parseInt(XMLUtils.getOptionalStringAttribute(var2, WLDD_MAX_CATALOG_NAME_LENGTH)), Integer.parseInt(XMLUtils.getOptionalStringAttribute(var2, WLDD_MAX_SCHEMA_NAME_LENGTH)), Integer.parseInt(XMLUtils.getOptionalStringAttribute(var2, WLDD_MAX_TABLE_NAME_LENGTH)), XMLUtils.getOptionalStringAttribute(var2, WLDD_IDENTIFIER_QUOTE_STRING), XMLUtils.getOptionalStringAttribute(var2, WLDD_CATALOG_SEPARATOR), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_CATALOG_AT_START), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_SUPPORTS_SCHEMAS_IN_DML), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_SUPPORTS_CATALOGS_IN_DML), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_UPPER_CASE_IDENTIFIERS), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_LOWER_CASE_IDENTIFIERS), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_MIXED_CASE_IDENTIFIERS), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_UPPER_CASE_QUOTED_IDENTIFIERS), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_LOWER_CASE_QUOTED_IDENTIFIERS), XMLUtils.getOptionalBooleanAttribute(var2, WLDD_STORES_MIXED_CASE_QUOTED_IDENTIFIERS));
         }

         this.metaData.setMetaDataHolder(var9);
      } else {
         this.metaData.setMetaDataHolder((DatabaseMetaDataHolder)null);
      }

      var1.skip(ELEMENT_NAME);
      this.parseRow(var1);
   }

   private void parseRow(XMLInputStream var1) throws IOException, SQLException {
      StartElement var2 = this.getNext(var1);
      this.checkName(var2, ELEMENT_NAME);
      this.metaData.readRowAttributes(var2);
      String var3 = XMLUtils.getRequiredAttribute(var2, "name").getValue();
      this.metaData.setRowName(var3);
      var1.skip(ELEMENT_NAME);
      this.parseColumns(var1);
   }

   private void parseColumns(XMLInputStream var1) throws IOException, SQLException {
      Object var2 = new ArrayList();

      StartElement var4;
      while(var1.hasNext()) {
         XMLEvent var3 = var1.peek();
         if (var3 instanceof StartElement) {
            if (!ELEMENT_NAME.equals(var3.getName())) {
               break;
            }

            var4 = (StartElement)var1.next();
            ((List)var2).add(var4);
         } else {
            var1.next();
         }
      }

      if (this.metaData.getColumnCount() > 0) {
         var2 = this.pruneColumnList((List)var2);
      } else {
         this.metaData.setColumnCount(((List)var2).size());
      }

      for(int var5 = 0; var5 < ((List)var2).size(); ++var5) {
         var4 = (StartElement)((List)var2).get(var5);
         this.metaData.readXMLAttributes(var5, var4);
      }

   }

   private List pruneColumnList(List var1) throws IOException, SQLException {
      if (this.metaData.getColumnCount() > var1.size()) {
         throw new IOException("Existing WLCachedRowSetMetaData contains " + this.metaData.getColumnCount() + " columns, but the XML Schema contains " + var1.size() + " columns.  The XML Schema must be a " + "superset of the existing WLCachedRowSetMetaData.");
      } else {
         Iterator var2 = var1.iterator();
         ArrayList var3 = new ArrayList();
         int var4 = 1;

         int var7;
         while(var2.hasNext()) {
            StartElement var5 = (StartElement)var2.next();
            String var6 = XMLUtils.getRequiredAttribute(var5, "name").getValue();

            try {
               var7 = this.metaData.findColumn(var6);
               if (var7 != var4) {
                  throw new IOException("The column named: " + var6 + " was found in the existing WLCachedRowSetMetaData at " + "column " + var7 + " however we expected it at " + var4 + ".  This could indicate the column " + this.metaData.getColumnName(var4) + " is not in the XML Schema" + ", or the order of columns in the schema is not the same " + "as the order of columns in the WLCachedRowSetMetaData.");
               }

               var3.add(var5);
               ++var4;
            } catch (SQLException var8) {
            }
         }

         if (var4 == this.metaData.getColumnCount() + 1) {
            return var3;
         } else {
            String var9 = "";
            StringBuffer var10 = new StringBuffer();

            for(var7 = this.metaData.getColumnCount() + 1; var4 != var7; ++var4) {
               var10.append(var9);
               var9 = ", ";
               var10.append(this.metaData.getColumnName(var4));
            }

            throw new IOException("The following columns are in the existing WLCachedRowSetMetaData, but are not in the XML Schema: " + var10.toString() + ".  The XML Schema must be a superset of the " + "existing WLCachedRowSetMetaData.");
         }
      }
   }

   private StartElement getNext(XMLInputStream var1) throws IOException {
      return var1.hasNext() ? (StartElement)var1.next() : null;
   }

   private void checkName(StartElement var1, XMLName var2) throws IOException {
      if (var1 == null) {
         throw new IOException("Expected Element: " + var2 + " but found end of file (EOF)");
      } else if (!var2.equals(var1.getName())) {
         throw new IOException("Expected Element: " + var2 + " but found " + var1.getName());
      }
   }
}
