package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.util.TypeFilter;

public final class XMLInstanceReader implements XMLSchemaConstants {
   private static final int ROW_UNCHANGED = 1;
   private static final int ROW_INSERTED = 2;
   private static final int ROW_MODIFIED = 4;
   private static final int ROW_MODIFIED_ORIGINAL = 8;
   private static final int ROW_DELETED_ORIGINAL = 16;
   private WLCachedRowSet rowSet;
   private WLRowSetMetaData metaData;
   private XMLName rowName;
   private XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();

   public XMLInstanceReader(WLCachedRowSet var1) throws SQLException {
      this.rowSet = var1;
      this.metaData = (WLRowSetMetaData)var1.getMetaData();
      this.rowName = ElementFactory.createXMLName(this.metaData.getDefaultNamespace(), this.metaData.getRowName());
   }

   private BitSet readRow(XMLInputStream var1, CachedRow var2) throws IOException, SQLException {
      String var3 = null;
      BitSet var4 = new BitSet();

      XMLEvent var5;
      do {
         if (!var1.hasNext()) {
            return var4;
         }

         var5 = var1.next();
         int var6;
         if (var5 instanceof StartElement) {
            var3 = var5.getName().getLocalName();
            if (XMLUtils.getOptionalBooleanAttribute((StartElement)var5, NIL_NAME)) {
               if (var3 == null) {
                  throw new IOException("Found null column value:  with no corresponding column name.");
               }

               var6 = this.metaData.findColumn(var3);
               var2.setColumn(var6, (Object)null);
               var4.set(var6 - 1);
            }
         } else if (var5 instanceof CharacterData) {
            if (var3 == null) {
               throw new IOException("Found column value: " + ((CharacterData)var5).getContent() + " with no corresponding column name.");
            }

            var6 = this.metaData.findColumn(var3);
            Object var7 = TypeMapper.getJavaValue(this.metaData.getColumnType(var6), ((CharacterData)var5).getContent());
            var2.setColumn(var6, var7);
            var4.set(var6 - 1);
         }

         var5 = var1.peek();
      } while(!(var5 instanceof StartElement) || !this.rowName.equals(((StartElement)var5).getName()));

      return var4;
   }

   private int getRowId(StartElement var1) throws IOException {
      Attribute var2 = var1.getAttributeByName(WLDD_ROWID);
      if (var2 == null) {
         throw new IOException("Unable to find required id attribute on row");
      } else {
         String var3 = var2.getValue();
         if (var3 == null) {
            throw new IOException("Row's id attribute was found, but the value was null");
         } else {
            try {
               return new Integer(var3);
            } catch (NumberFormatException var5) {
               throw new IOException("Could not convert row's id value: " + var3 + " into an integer value: " + var5);
            }
         }
      }
   }

   private int getRowState(StartElement var1) throws IOException {
      Attribute var2 = var1.getAttributeByName(WLDD_ROWSTATE);
      if (var2 == null) {
         return 1;
      } else {
         String var3 = var2.getValue();
         if ("unchanged".equalsIgnoreCase(var3)) {
            return 1;
         } else if ("inserted".equalsIgnoreCase(var3)) {
            return 2;
         } else if ("modified".equalsIgnoreCase(var3)) {
            return 4;
         } else if ("modifiedoriginal".equalsIgnoreCase(var3)) {
            return 8;
         } else if ("deletedoriginal".equalsIgnoreCase(var3)) {
            return 16;
         } else {
            throw new IOException("Unrecognized row state: " + var3);
         }
      }
   }

   private void setRowStateFlags(CachedRow var1, int var2) throws SQLException {
      switch (var2) {
         case 1:
            var1.setInsertRow(false);
            var1.setDeletedRow(false);
            var1.setUpdatedRow(false);
            break;
         case 2:
            var1.setInsertRow(true);
            var1.setDeletedRow(false);
            var1.setUpdatedRow(false);
            break;
         case 4:
            var1.setInsertRow(false);
            var1.setDeletedRow(false);
            var1.setUpdatedRow(true);
            break;
         case 8:
            var1.setInsertRow(false);
            var1.setDeletedRow(false);
            var1.setUpdatedRow(false);
            break;
         case 16:
            var1.setInsertRow(false);
            var1.setDeletedRow(true);
            var1.setUpdatedRow(false);
            break;
         default:
            throw new AssertionError(var2);
      }

   }

   private void addRow(Map var1, Map var2, CachedRow var3, int var4, int var5) throws IOException, SQLException {
      Integer var6 = new Integer(var4);
      CachedRow var7 = (CachedRow)var1.get(var6);
      if (var7 == null) {
         this.setRowStateFlags(var3, var5);
         var1.put(var6, var3);
      } else {
         BitSet var8 = (BitSet)var2.get(var6);
         if (var5 == 4) {
            var7.mergeNewValues(var3, var8);
         } else {
            if (var5 != 8) {
               throw new IOException("Found multiple rows with an id: " + var4 + ", but the row states did not indicate they were the same row");
            }

            var7.mergeOriginalValues(var3, var8);
         }
      }

   }

   private void readRows(XMLInputStream var1) throws IOException, SQLException {
      TreeMap var2 = new TreeMap();

      CachedRow var5;
      int var7;
      int var8;
      for(HashMap var3 = new HashMap(); var1.hasNext(); this.addRow(var2, var3, var5, var7, var8)) {
         StartElement var4 = (StartElement)var1.next();
         var5 = new CachedRow(this.metaData);
         BitSet var6 = this.readRow(var1, var5);
         var7 = this.getRowId(var4);
         var8 = this.getRowState(var4);
         if (var8 == 8) {
            var3.put(new Integer(var7), var6);
         }
      }

      ((WLRowSetInternal)this.rowSet).getCachedRows().addAll(var2.values());
   }

   public void loadXML(XMLInputStream var1) throws IOException, SQLException {
      XMLInputStream var2 = this.factory.newInputStream(var1, new TypeFilter(18));
      this.parseRowSet(var2);
   }

   private void parseRowSet(XMLInputStream var1) throws IOException, SQLException {
      if (!var1.hasNext()) {
         throw new IOException("XML Instance document is empty");
      } else {
         StartElement var2 = (StartElement)var1.next();
         String var3 = XMLUtils.getOptionalStringAttribute(var2, SCHEMA_LOCATION_NAME);
         if (var3 != null) {
            this.metaData.setXMLSchemaLocation(var3);
         }

         var1.skip(this.rowName);
         this.readRows(var1);
      }
   }
}
