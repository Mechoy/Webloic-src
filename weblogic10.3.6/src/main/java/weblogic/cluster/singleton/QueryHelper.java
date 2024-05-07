package weblogic.cluster.singleton;

import java.util.Iterator;
import java.util.Set;

public abstract class QueryHelper {
   public static String TIME_AS_FRACTION_OF_DAY = "";
   private String TABLE_NAME;
   private String COLUMN_NAMES;
   private String INSERT_VALUES;
   private String WHERE_CLAUSE;
   private String SYSDATE;
   private String domainName;
   private String clusterName;
   private int dbType;

   public QueryHelper(String var1, String var2, String var3, int var4) {
      this.init(var1, var2, var3, var4);
   }

   public QueryHelper() {
   }

   protected void init(String var1, String var2, String var3, int var4) {
      this.dbType = var4;
      this.TABLE_NAME = var1;
      this.domainName = var2;
      this.clusterName = var3;
      if (var4 == 1 || var4 == 7) {
         TIME_AS_FRACTION_OF_DAY = "/86400";
      }

      this.INSERT_VALUES = ", '" + var2 + "' , '" + var3 + "' ";
      this.WHERE_CLAUSE = " AND DOMAINNAME='" + var2 + "' AND CLUSTERNAME='" + var3 + "'";
      this.COLUMN_NAMES = " ( SERVER, INSTANCE, DOMAINNAME, CLUSTERNAME, TIMEOUT ) ";
   }

   protected abstract String addToDate(String var1, int var2);

   protected abstract String compareDates(String var1, String var2);

   protected abstract String compareDates(String var1, String var2, boolean var3);

   protected abstract String getTimeFunction();

   protected int getDBType() {
      return this.dbType;
   }

   public String getLeaseOwnerQuery(String var1) {
      return "SELECT INSTANCE FROM " + this.TABLE_NAME + " WHERE ( " + this.compareDates("TIMEOUT", this.getTimeFunction(), true) + ") AND SERVER ='" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getPreviousLeaseOwnerQuery(String var1) {
      return "SELECT INSTANCE FROM " + this.TABLE_NAME + " WHERE SERVER ='" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getAcquireLeaseQuery(String var1, String var2) {
      return "DELETE FROM " + this.TABLE_NAME + " WHERE (" + this.compareDates(this.getTimeFunction(), "TIMEOUT") + ") AND SERVER = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getAssumeLeaseQuery(String var1, String var2, int var3) {
      return "INSERT INTO " + this.TABLE_NAME + this.COLUMN_NAMES + " VALUES ( '" + var1 + "' , '" + var2 + "' " + this.INSERT_VALUES + ", " + this.addToDate(this.getTimeFunction(), var3) + " )";
   }

   public String getRenewLeaseQuery(String var1, String var2, int var3) {
      return "UPDATE " + this.TABLE_NAME + " SET TIMEOUT = ( " + this.addToDate(this.getTimeFunction(), var3) + ") WHERE SERVER = '" + var1 + "' AND INSTANCE ='" + var2 + "'" + this.WHERE_CLAUSE;
   }

   public String getRenewLeasesQuery(String var1, Set var2, int var3) {
      StringBuilder var4 = new StringBuilder();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         var4.append("'");
         var4.append(var5.next());
         var4.append("'");
         if (var5.hasNext()) {
            var4.append(",");
         }
      }

      return "UPDATE " + this.TABLE_NAME + " SET TIMEOUT = ( " + this.addToDate(this.getTimeFunction(), var3) + ") WHERE SERVER IN (" + var4.toString() + ") AND INSTANCE ='" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getRenewAllLeasesQuery(String var1, int var2) {
      return "UPDATE " + this.TABLE_NAME + " SET TIMEOUT = ( " + this.addToDate(this.getTimeFunction(), var2) + ") WHERE INSTANCE ='" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getAbdicateLeaseQuery(String var1, String var2) {
      return "DELETE FROM " + this.TABLE_NAME + " WHERE SERVER = '" + var1 + "' AND INSTANCE ='" + var2.toString() + "'" + this.WHERE_CLAUSE;
   }

   public String getUnresponsiveMigratableServersQuery(int var1) {
      return "SELECT SERVER FROM " + this.TABLE_NAME + " WHERE ( " + this.compareDates(this.getTimeFunction(), "(" + this.addToDate("TIMEOUT", var1) + "))") + this.WHERE_CLAUSE;
   }

   public String getInsertMachineQuery(String var1, String var2, String var3) {
      return "INSERT INTO " + var3 + " VALUES ('" + var1 + "','" + var2 + "'" + this.INSERT_VALUES + ")";
   }

   public String getDeleteMachineQuery(String var1, String var2) {
      return "DELETE FROM " + var2 + " WHERE SERVER = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getRetrieveMachineQuery(String var1, String var2) {
      return "SELECT HOSTMACHINE FROM " + var2 + " WHERE SERVER='" + var1 + "'" + this.WHERE_CLAUSE;
   }
}
