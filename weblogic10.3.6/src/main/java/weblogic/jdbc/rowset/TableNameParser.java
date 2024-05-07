package weblogic.jdbc.rowset;

import java.util.ArrayList;
import java.util.regex.Pattern;

final class TableNameParser {
   private static final String EMPTY_STRING = "";
   private static final String SCHEMA_SEPARATOR = ".";
   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");
   private static final int PATTERN_CATALOG_SCHEMA_TABLE = 1;
   private static final int PATTERN_CATALOG_SCHEMA_TABLE_X = 2;
   private static final int PATTERN_SCHEMA_TABLE_CATALOG = 3;
   private static final int PATTERN_CATALOG_TABLE = 4;
   private static final int PATTERN_TABLE_CATALOG = 5;
   private static final int PATTERN_SCHEMA_TABLE = 6;
   private static final int PATTERN_TABLE = 7;
   private String qualifiedTableName = null;
   private DatabaseMetaDataHolder metaData = null;

   TableNameParser(String var1, DatabaseMetaDataHolder var2) throws ParseException {
      if (var1 != null && var2 != null) {
         this.qualifiedTableName = var1.trim();
         this.metaData = var2;
      } else {
         throw new ParseException("Invalid parameters, construction is failed.");
      }
   }

   String[] parse() throws ParseException {
      String[] var1 = null;
      int var2 = this.getPatternType();
      switch (var2) {
         case 1:
            var1 = this.parsePatternCatalogSchemaTable();
            break;
         case 2:
            var1 = this.parsePatternCatalogSchemaTableX();
            break;
         case 3:
            var1 = this.parsePatternSchemaTableCatalog();
            break;
         case 4:
            var1 = this.parsePatternCatalogTable();
            break;
         case 5:
            var1 = this.parsePatternTableCatalog();
            break;
         case 6:
            var1 = this.parsePatternSchemaTable();
            break;
         case 7:
            var1 = this.parsePatternTable();
            break;
         default:
            throw new ParseException("Parse error, unknown pattern.");
      }

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (this.metaData.supportsIdentifierQuoting() && isQuotedIdentifier(var1[var3], this.metaData.getIdentifierQuoteString())) {
            if (this.metaData.storesUpperCaseQuotedIdentifiers()) {
               var1[var3] = var1[var3].toUpperCase();
            } else if (this.metaData.storesLowerCaseQuotedIdentifiers()) {
               var1[var3] = var1[var3].toLowerCase();
            }
         } else {
            if (!isValidIdentifier(var1[var3])) {
               throw new ParseException("Parse error, identifier should not contain whitespace characters, expected <" + this.getPatternName(var2) + ">, " + "got <" + this.qualifiedTableName + ">");
            }

            if (this.metaData.storesUpperCaseIdentifiers()) {
               var1[var3] = var1[var3].toUpperCase();
            } else if (this.metaData.storesLowerCaseIdentifiers()) {
               var1[var3] = var1[var3].toLowerCase();
            }
         }
      }

      if (var1[0].length() > this.metaData.getMaxCatalogNameLength()) {
         throw new ParseException("Parse error, catalog name length must <= " + this.metaData.getMaxCatalogNameLength() + ".");
      } else if (var1[1].length() > this.metaData.getMaxSchemaNameLength()) {
         throw new ParseException("Parse error, schema name length must <= " + this.metaData.getMaxSchemaNameLength() + ".");
      } else if (var1[2].length() > this.metaData.getMaxTableNameLength()) {
         throw new ParseException("Parse error, table name length must <= " + this.metaData.getMaxTableNameLength() + ".");
      } else {
         return var1;
      }
   }

   private int getPatternType() {
      if (this.metaData.supportsCatalogsInDataManipulation()) {
         if (this.metaData.supportsSchemasInDataManipulation()) {
            if (".".equals(this.metaData.getCatalogSeparator())) {
               return 1;
            } else {
               return this.metaData.isCatalogAtStart() ? 2 : 3;
            }
         } else {
            return this.metaData.isCatalogAtStart() ? 4 : 5;
         }
      } else {
         return this.metaData.supportsSchemasInDataManipulation() ? 6 : 7;
      }
   }

   private String getPatternName(int var1) {
      String var2;
      switch (var1) {
         case 1:
            var2 = "[[catalog.]schema.]table";
            break;
         case 2:
            var2 = "[[catalog" + this.metaData.getCatalogSeparator() + "]schema" + "." + "]table";
            break;
         case 3:
            var2 = "[schema.]table[" + this.metaData.getCatalogSeparator() + "catalog]";
            break;
         case 4:
            var2 = "[catalog" + this.metaData.getCatalogSeparator() + "]table";
            break;
         case 5:
            var2 = "table[" + this.metaData.getCatalogSeparator() + "catalog]";
            break;
         case 6:
            var2 = "[schema.]table";
            break;
         case 7:
            var2 = "table";
            break;
         default:
            var2 = "Unknown pattern";
      }

      return var2;
   }

   private String[] parsePatternCatalogSchemaTable() throws ParseException {
      String[] var1 = null;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var1 = parseQuotedFull(this.qualifiedTableName, ".", this.getPatternName(1), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parseFull(this.qualifiedTableName, ".", this.getPatternName(1));
      }

      if (var1.length == 1) {
         return new String[]{"", "", var1[0]};
      } else {
         return var1.length == 2 ? new String[]{"", var1[0], var1[1]} : var1;
      }
   }

   private String[] parsePatternCatalogSchemaTableX() throws ParseException {
      String[] var1 = null;
      String[] var2 = null;
      boolean var3 = false;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var3 = true;
      }

      if (var3) {
         var1 = parseQuotedPart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(2), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parsePart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(2));
      }

      if (var1.length == 1) {
         if (var3) {
            var2 = parseQuotedPart(var1[0], ".", this.getPatternName(6), this.metaData.getIdentifierQuoteString());
         } else {
            var2 = parsePart(var1[0], ".", this.getPatternName(6));
         }

         return var2.length == 1 ? new String[]{"", "", var2[0]} : new String[]{"", var2[0], var2[1]};
      } else {
         if (var3) {
            var2 = parseQuotedPart(var1[1], ".", this.getPatternName(6), this.metaData.getIdentifierQuoteString());
         } else {
            var2 = parsePart(var1[1], ".", this.getPatternName(6));
         }

         return var2.length == 1 ? new String[]{var1[0], "", var2[0]} : new String[]{var1[0], var2[0], var2[1]};
      }
   }

   private String[] parsePatternSchemaTableCatalog() throws ParseException {
      String[] var1 = null;
      String[] var2 = null;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var1 = parseQuotedPart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(3), this.metaData.getIdentifierQuoteString());
         var2 = parseQuotedPart(var1[0], ".", this.getPatternName(6), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parsePart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(3));
         var2 = parsePart(var1[0], ".", this.getPatternName(6));
      }

      if (var1.length == 1) {
         return var2.length == 1 ? new String[]{"", "", var2[0]} : new String[]{"", var2[0], var2[1]};
      } else {
         return var2.length == 1 ? new String[]{var1[1], "", var2[0]} : new String[]{var1[1], var2[0], var2[1]};
      }
   }

   private String[] parsePatternCatalogTable() throws ParseException {
      String[] var1 = null;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var1 = parseQuotedPart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(4), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parsePart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(4));
      }

      return var1.length == 1 ? new String[]{"", "", var1[0]} : new String[]{var1[0], "", var1[1]};
   }

   private String[] parsePatternTableCatalog() throws ParseException {
      String[] var1 = null;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var1 = parseQuotedPart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(5), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parsePart(this.qualifiedTableName, this.metaData.getCatalogSeparator(), this.getPatternName(5));
      }

      return var1.length == 1 ? new String[]{"", "", var1[0]} : new String[]{var1[1], "", var1[0]};
   }

   private String[] parsePatternSchemaTable() throws ParseException {
      String[] var1 = null;
      if (this.metaData.supportsIdentifierQuoting() && this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString())) {
         var1 = parseQuotedPart(this.qualifiedTableName, ".", this.getPatternName(6), this.metaData.getIdentifierQuoteString());
      } else {
         var1 = parsePart(this.qualifiedTableName, ".", this.getPatternName(6));
      }

      return var1.length == 1 ? new String[]{"", "", var1[0]} : new String[]{"", var1[0], var1[1]};
   }

   private String[] parsePatternTable() throws ParseException {
      if (this.qualifiedTableName != null && "".equals(this.qualifiedTableName) && (!this.metaData.supportsIdentifierQuoting() || (this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString()) || !this.qualifiedTableName.contains(".")) && (!this.qualifiedTableName.contains(this.metaData.getIdentifierQuoteString()) || isQuotedIdentifier(this.qualifiedTableName, this.metaData.getIdentifierQuoteString())))) {
         return new String[]{"", "", this.qualifiedTableName};
      } else {
         throw new ParseException("Parse error, expected <" + this.getPatternName(7) + ">, " + "got <" + this.qualifiedTableName + ">");
      }
   }

   private static String[] parseFull(String var0, String var1, String var2) throws ParseException {
      if (var0 != null && !"".equals(var0)) {
         if (var1 != null && !"".equals(var1)) {
            if (".".equals(var1)) {
               var1 = "\\" + var1;
            }

            String[] var3 = var0.split(var1);
            if (var3.length != 0 && var3.length <= 3 && (var3.length < 1 || var3[0].length() != 0) && (var3.length < 2 || var3[1].length() != 0) && (var3.length < 3 || var3[2].length() != 0)) {
               return var3;
            } else {
               throw new ParseException("Parse error, expected <" + var2 + ">, " + "got <" + var0 + ">");
            }
         } else {
            throw new ParseException("Parse error, separator should not be empty.");
         }
      } else {
         throw new ParseException("Parse error, parsed string should not be empty.");
      }
   }

   private static String[] parseQuotedFull(String var0, String var1, String var2, String var3) throws ParseException {
      if (var0 != null && !"".equals(var0)) {
         if (var1 != null && !"".equals(var1)) {
            String[] var4 = split(var0, var1, var3);
            if (var4.length != 0 && var4.length <= 3 && (var4.length < 1 || var4[0].length() != 0) && (var4.length < 2 || var4[1].length() != 0) && (var4.length < 3 || var4[2].length() != 0) && isQuotedIdentifiers(var4, var3)) {
               return var4;
            } else {
               throw new ParseException("Parse error, expected <" + var2 + ">, " + "got <" + var0 + ">");
            }
         } else {
            throw new ParseException("Parse error, separator should not be empty.");
         }
      } else {
         throw new ParseException("Parse error, parsed string should not be empty.");
      }
   }

   private static String[] parsePart(String var0, String var1, String var2) throws ParseException {
      if (var0 != null && !"".equals(var0)) {
         if (var1 != null && !"".equals(var1)) {
            if (".".equals(var1)) {
               var1 = "\\" + var1;
            }

            String[] var3 = var0.split(var1);
            if (var3.length == 0 || var3.length > 2 || var3.length >= 1 && var3[0].length() == 0 || var3.length >= 2 && var3[1].length() == 0) {
               throw new ParseException("Parse error, expected <" + var2 + ">, " + "got <" + var0 + ">");
            } else {
               return var3;
            }
         } else {
            throw new ParseException("Parse error, separator should not be empty.");
         }
      } else {
         throw new ParseException("Parse error, string to be parsed should not be empty.");
      }
   }

   private static String[] parseQuotedPart(String var0, String var1, String var2, String var3) throws ParseException {
      if (var0 != null && !"".equals(var0)) {
         if (var1 != null && !"".equals(var1)) {
            String[] var4 = split(var0, var1, var3);
            if (var4.length != 0 && var4.length <= 2 && (var4.length < 1 || var4[0].length() != 0) && (var4.length < 2 || var4[1].length() != 0) && isQuotedIdentifiers(var4, var3)) {
               return var4;
            } else {
               throw new ParseException("Parse error, expected <" + var2 + ">, " + "got <" + var0 + ">");
            }
         } else {
            throw new ParseException("Parse error, separator should not be empty.");
         }
      } else {
         throw new ParseException("Parse error, string to be parsed should not be empty.");
      }
   }

   private static String[] split(String var0, String var1, String var2) throws ParseException {
      Integer[] var3 = getQuoteStringPositions(var0, var2);
      if (var3.length % 2 == 0 && var3.length >= 2 && var3.length <= 6) {
         int var4 = -var1.length();
         ArrayList var5 = new ArrayList();

         while(true) {
            int var6;
            for(var6 = var0.indexOf(var1, var4 + var1.length()); var6 != -1 && !isValidSeparator(var1, var6, var2, var3); var6 = var0.indexOf(var1, var6 + var1.length())) {
            }

            if (var6 == -1) {
               var5.add(var0.substring(var4 + var1.length()));
               return (String[])var5.toArray(new String[var5.size()]);
            }

            var5.add(var0.substring(var4 + var1.length(), var6));
            var4 = var6;
         }
      } else {
         throw new ParseException("Parse error, string to be parsed is invalid.");
      }
   }

   private static Integer[] getQuoteStringPositions(String var0, String var1) {
      if (var0 != null && !"".equals(var0) && var1 != null && !"".equals(var1)) {
         int var2 = -var1.length();
         ArrayList var3 = new ArrayList();

         while(true) {
            var2 = var0.indexOf(var1, var2 + var1.length());
            if (var2 == -1) {
               return (Integer[])var3.toArray(new Integer[var3.size()]);
            }

            var3.add(var2);
         }
      } else {
         return new Integer[0];
      }
   }

   private static boolean isValidSeparator(String var0, int var1, String var2, Integer[] var3) {
      if (var1 + var0.length() > var3[0] && var3[var3.length - 1] + var2.length() > var1) {
         for(int var4 = 0; var4 < var3.length; var4 += 2) {
            if (var3[var4] + var2.length() <= var1 && var1 + var0.length() <= var3[var4 + 1]) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   private static boolean isQuotedIdentifiers(String[] var0, String var1) {
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (isQuotedIdentifier(var5, var1)) {
            return true;
         }
      }

      return false;
   }

   static boolean isQuotedIdentifier(String var0, String var1) {
      return var0.startsWith(var1) && var0.endsWith(var1);
   }

   private static boolean isValidIdentifier(String var0) {
      return !WHITESPACE_PATTERN.matcher(var0).matches();
   }

   boolean identifierEqual(String var1, String var2) {
      if (var2 != null && !"".equals(var2)) {
         if (var1 != null && !"".equals(var1)) {
            if (this.metaData.supportsIdentifierQuoting()) {
               String var3 = this.metaData.getIdentifierQuoteString();
               if (isQuotedIdentifier(var2, var3)) {
                  var2 = var2.replace(var3, "");
               }

               if (isQuotedIdentifier(var1, var3)) {
                  var1 = var1.replace(var3, "");
               }
            }

            return var2.equals(var1);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }
}
