package weblogic.diagnostics.harvester;

import java.util.Date;
import weblogic.diagnostics.accessor.ColumnInfo;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.utils.SharedConstants;
import weblogic.utils.PlatformConstants;

public final class HarvesterDataSample extends DataRecord {
   static final long serialVersionUID = 1L;
   private static final ColumnInfo[] columnInfos = ArchiveConstants.getColumns(2);
   private static final int RECORD_SIZE;
   private static final int RECORDID_INDEX;
   private static final int TIMESTAMP_INDEX;
   private static final int DOMAIN_INDEX;
   private static final int SERVER_INDEX;
   private static final int TYPE_INDEX;
   private static final int INST_NAME_INDEX;
   private static final int ATTR_NAME_INDEX;
   private static final int ATTR_TYPE_INDEX;
   private static final int ATTR_VALUE_INDEX;

   public HarvesterDataSample() {
   }

   public HarvesterDataSample(long var1, String var3, String var4, String var5, Object var6) {
      super(new Object[RECORD_SIZE]);
      Object[] var7 = this.getValues();
      var7[TIMESTAMP_INDEX] = new Long(var1);
      var7[DOMAIN_INDEX] = SharedConstants.DOMAIN_NAME;
      var7[SERVER_INDEX] = SharedConstants.SERVER_NAME;
      var7[TYPE_INDEX] = var3;
      var7[INST_NAME_INDEX] = var4;
      var7[ATTR_NAME_INDEX] = var5;
      var7[ATTR_TYPE_INDEX] = new Integer(this.getValueType(var6));
      var7[ATTR_VALUE_INDEX] = var6;
   }

   public long getRecordID() {
      Object var1 = this.getValues()[RECORDID_INDEX];
      return var1 instanceof Long ? (Long)var1 : 0L;
   }

   public long getTimestamp() {
      Object var1 = this.getValues()[TIMESTAMP_INDEX];
      return var1 instanceof Long ? (Long)var1 : 0L;
   }

   public String getInstanceName() {
      return (String)this.getValues()[INST_NAME_INDEX];
   }

   public String getDomainName() {
      return (String)this.getValues()[DOMAIN_INDEX];
   }

   public String getServerName() {
      return (String)this.getValues()[SERVER_INDEX];
   }

   public String getTypeName() {
      return (String)this.getValues()[TYPE_INDEX];
   }

   public String getAttributeName() {
      return (String)this.getValues()[ATTR_NAME_INDEX];
   }

   public Object getAttributeValue() {
      return this.getValues()[ATTR_VALUE_INDEX];
   }

   public String toString() {
      return this.toStringLong();
   }

   public String toStringShort() {
      StringBuffer var1 = new StringBuffer();
      String var2 = columnInfos[ATTR_TYPE_INDEX].getColumnTypeName();
      var1.append("Sample: ^" + this.getAttributeName() + "^" + this.getAttributeValue() + "^" + this.getTypeName() + "^" + var2 + "^" + this.getInstanceName());
      var1.append(PlatformConstants.EOL);
      return var1.toString();
   }

   public String toStringLong() {
      StringBuffer var1 = new StringBuffer();
      long var2 = this.getTimestamp();
      var1.append("   RecordID: " + this.getRecordID() + PlatformConstants.EOL);
      var1.append("  Timestamp: [" + var2 + "] (sometime slightly after) " + new Date(var2) + PlatformConstants.EOL);
      var1.append("  Domain: " + this.getDomainName() + PlatformConstants.EOL);
      var1.append("  Server: " + this.getServerName() + PlatformConstants.EOL);
      var1.append("  Type: " + this.getTypeName() + PlatformConstants.EOL);
      var1.append("  Instance: " + this.getInstanceName() + PlatformConstants.EOL);
      var1.append("  Attribute: " + this.getAttributeName() + PlatformConstants.EOL);
      String var4 = columnInfos[ATTR_TYPE_INDEX].getColumnTypeName();
      var1.append("  Attribute type: " + var4 + PlatformConstants.EOL);
      var1.append("  Attribute value: " + this.getAttributeValue() + PlatformConstants.EOL);
      var1.append(PlatformConstants.EOL);
      return var1.toString();
   }

   private static final int getColumnIndex(String var0) {
      for(int var1 = 0; var1 < columnInfos.length; ++var1) {
         ColumnInfo var2 = columnInfos[var1];
         String var3 = var2.getColumnName();
         if (var3.equals(var0)) {
            return var1;
         }
      }

      throw new AssertionError(LogSupport.getGenericHarvesterProblemText("Column name " + var0 + " not found in column info " + "from diagnostics achive subsystem"));
   }

   private int getValueType(Object var1) {
      if (var1 instanceof Number) {
         return 4;
      } else {
         return var1 instanceof String ? 5 : 6;
      }
   }

   static {
      RECORD_SIZE = ArchiveConstants.HARVESTER_ARCHIVE_COLUMNS_COUNT;
      RECORDID_INDEX = getColumnIndex("RECORDID");
      TIMESTAMP_INDEX = getColumnIndex("TIMESTAMP");
      DOMAIN_INDEX = getColumnIndex("DOMAIN");
      SERVER_INDEX = getColumnIndex("SERVER");
      TYPE_INDEX = getColumnIndex("TYPE");
      INST_NAME_INDEX = getColumnIndex("NAME");
      ATTR_NAME_INDEX = getColumnIndex("ATTRNAME");
      ATTR_TYPE_INDEX = getColumnIndex("ATTRTYPE");
      ATTR_VALUE_INDEX = getColumnIndex("ATTRVALUE");
   }
}
