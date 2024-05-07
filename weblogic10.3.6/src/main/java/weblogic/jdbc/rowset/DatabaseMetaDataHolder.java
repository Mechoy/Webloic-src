package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

final class DatabaseMetaDataHolder implements Serializable {
   private static final long serialVersionUID = -3120626766920233516L;
   private int maxCatalogNameLength = 0;
   private int maxSchemaNameLength = 30;
   private int maxTableNameLength = 30;
   private String identifierQuoteString = "\"";
   private String catalogSeparator = "";
   private boolean isCatalogAtStart = false;
   private boolean supportsSchemasInDataManipulation = true;
   private boolean supportsCatalogsInDataManipulation = false;
   private boolean storesUpperCaseIdentifiers = true;
   private boolean storesLowerCaseIdentifiers = false;
   private boolean storesMixedCaseIdentifiers = false;
   private boolean storesUpperCaseQuotedIdentifiers = false;
   private boolean storesLowerCaseQuotedIdentifiers = false;
   private boolean storesMixedCaseQuotedIdentifiers = true;

   DatabaseMetaDataHolder(DatabaseMetaData var1) throws SQLException {
      if (var1 == null) {
         throw new SQLException("Invalid parameter, can't initialize DatabaseMetaDataHolder.");
      } else {
         this.maxCatalogNameLength = var1.getMaxCatalogNameLength();
         this.maxSchemaNameLength = var1.getMaxSchemaNameLength();
         this.maxTableNameLength = var1.getMaxTableNameLength();
         this.identifierQuoteString = var1.getIdentifierQuoteString();
         this.catalogSeparator = var1.getCatalogSeparator();
         this.isCatalogAtStart = var1.isCatalogAtStart();
         this.supportsSchemasInDataManipulation = var1.supportsSchemasInDataManipulation();
         this.supportsCatalogsInDataManipulation = var1.supportsCatalogsInDataManipulation();
         this.storesUpperCaseIdentifiers = var1.storesUpperCaseIdentifiers();
         this.storesLowerCaseIdentifiers = var1.storesLowerCaseIdentifiers();
         this.storesMixedCaseIdentifiers = var1.storesMixedCaseIdentifiers();
         this.storesUpperCaseQuotedIdentifiers = var1.storesUpperCaseQuotedIdentifiers();
         this.storesLowerCaseQuotedIdentifiers = var1.storesLowerCaseQuotedIdentifiers();
         this.storesMixedCaseQuotedIdentifiers = var1.storesMixedCaseQuotedIdentifiers();
      }
   }

   DatabaseMetaDataHolder(int var1, int var2, int var3, String var4, String var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14) {
      this.maxCatalogNameLength = var1;
      this.maxSchemaNameLength = var2;
      this.maxTableNameLength = var2;
      this.identifierQuoteString = var4;
      this.catalogSeparator = var5;
      this.isCatalogAtStart = var6;
      this.supportsSchemasInDataManipulation = var7;
      this.supportsCatalogsInDataManipulation = var8;
      this.storesUpperCaseIdentifiers = var9;
      this.storesLowerCaseIdentifiers = var10;
      this.storesMixedCaseIdentifiers = var11;
      this.storesUpperCaseQuotedIdentifiers = var12;
      this.storesLowerCaseQuotedIdentifiers = var13;
      this.storesMixedCaseQuotedIdentifiers = var14;
   }

   int getMaxCatalogNameLength() {
      return this.maxCatalogNameLength;
   }

   int getMaxSchemaNameLength() {
      return this.maxSchemaNameLength;
   }

   int getMaxTableNameLength() {
      return this.maxTableNameLength;
   }

   String getIdentifierQuoteString() {
      return this.identifierQuoteString;
   }

   String getCatalogSeparator() {
      return this.catalogSeparator;
   }

   boolean isCatalogAtStart() {
      return this.isCatalogAtStart;
   }

   boolean supportsCatalogsInDataManipulation() {
      return this.supportsCatalogsInDataManipulation;
   }

   boolean supportsSchemasInDataManipulation() {
      return this.supportsSchemasInDataManipulation;
   }

   boolean storesUpperCaseIdentifiers() {
      return this.storesUpperCaseIdentifiers;
   }

   boolean storesLowerCaseIdentifiers() {
      return this.storesLowerCaseIdentifiers;
   }

   boolean storesMixedCaseIdentifiers() {
      return this.storesMixedCaseIdentifiers;
   }

   boolean storesUpperCaseQuotedIdentifiers() {
      return this.storesUpperCaseQuotedIdentifiers;
   }

   boolean storesLowerCaseQuotedIdentifiers() {
      return this.storesLowerCaseQuotedIdentifiers;
   }

   boolean storesMixedCaseQuotedIdentifiers() {
      return this.storesMixedCaseQuotedIdentifiers;
   }

   boolean supportsIdentifierQuoting() {
      return !this.identifierQuoteString.equals(" ");
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof DatabaseMetaDataHolder)) {
         return false;
      } else {
         DatabaseMetaDataHolder var2 = (DatabaseMetaDataHolder)var1;
         return this.maxCatalogNameLength == var2.maxCatalogNameLength && this.maxSchemaNameLength == var2.maxSchemaNameLength && this.maxTableNameLength == var2.maxTableNameLength && this.identifierQuoteString.equals(var2.identifierQuoteString) && this.catalogSeparator.equals(var2.catalogSeparator) && this.isCatalogAtStart == var2.isCatalogAtStart && this.supportsSchemasInDataManipulation == var2.supportsSchemasInDataManipulation && this.supportsCatalogsInDataManipulation == var2.supportsCatalogsInDataManipulation && this.storesUpperCaseIdentifiers == var2.storesUpperCaseIdentifiers && this.storesLowerCaseIdentifiers == var2.storesLowerCaseIdentifiers && this.storesMixedCaseIdentifiers == var2.storesMixedCaseIdentifiers && this.storesUpperCaseQuotedIdentifiers == var2.storesUpperCaseQuotedIdentifiers && this.storesLowerCaseQuotedIdentifiers == var2.storesLowerCaseQuotedIdentifiers && this.storesMixedCaseQuotedIdentifiers == var2.storesMixedCaseQuotedIdentifiers;
      }
   }

   public int hashCode() {
      int var1 = 17;
      var1 = 37 * var1 + this.maxCatalogNameLength;
      var1 = 37 * var1 + this.maxSchemaNameLength;
      var1 = 37 * var1 + this.maxTableNameLength;
      var1 = 37 * var1 + (this.identifierQuoteString == null ? 0 : this.identifierQuoteString.hashCode());
      var1 = 37 * var1 + (this.catalogSeparator == null ? 0 : this.catalogSeparator.hashCode());
      var1 = 37 * var1 + (this.isCatalogAtStart ? 0 : 1);
      var1 = 37 * var1 + (this.supportsSchemasInDataManipulation ? 0 : 1);
      var1 = 37 * var1 + (this.supportsCatalogsInDataManipulation ? 0 : 1);
      var1 = 37 * var1 + (this.storesUpperCaseIdentifiers ? 0 : 1);
      var1 = 37 * var1 + (this.storesLowerCaseIdentifiers ? 0 : 1);
      var1 = 37 * var1 + (this.storesMixedCaseIdentifiers ? 0 : 1);
      var1 = 37 * var1 + (this.storesUpperCaseQuotedIdentifiers ? 0 : 1);
      var1 = 37 * var1 + (this.storesLowerCaseQuotedIdentifiers ? 0 : 1);
      var1 = 37 * var1 + (this.storesMixedCaseQuotedIdentifiers ? 0 : 1);
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("maxCatalogNameLength=" + this.maxCatalogNameLength).append(",");
      var1.append("maxSchemaNameLength=" + this.maxSchemaNameLength).append(",");
      var1.append("maxTableNameLength=" + this.maxTableNameLength).append(",");
      var1.append("identifierQuoteString=" + this.identifierQuoteString).append(",");
      var1.append("catalogSeparator=" + this.catalogSeparator).append(",");
      var1.append("isCatalogAtStart=" + this.isCatalogAtStart).append(",");
      var1.append("supportsSchemasInDataManipulation=" + this.supportsSchemasInDataManipulation).append(",");
      var1.append("supportsCatalogsInDataManipulation=" + this.supportsCatalogsInDataManipulation).append(",");
      var1.append("storesUpperCaseIdentifiers=" + this.storesUpperCaseIdentifiers).append(",");
      var1.append("storesLowerCaseIdentifiers=" + this.storesLowerCaseIdentifiers).append(",");
      var1.append("storesMixedCaseIdentifiers=" + this.storesMixedCaseIdentifiers).append(",");
      var1.append("storesUpperCaseQuotedIdentifiers=" + this.storesUpperCaseQuotedIdentifiers).append(",");
      var1.append("storesLowerCaseQuotedIdentifiers=" + this.storesLowerCaseQuotedIdentifiers).append(",");
      var1.append("storesMixedCaseQuotedIdentifiers=" + this.storesMixedCaseQuotedIdentifiers);
      return var1.toString();
   }
}
