package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.JDBCEventInfo;

public class JDBCEventInfoImpl implements JDBCEventInfo {
   private String sql = null;
   private String pool = null;
   private boolean infectedSet = false;
   public boolean infected = false;

   public JDBCEventInfoImpl(String var1, String var2) {
      this.sql = var1;
      this.pool = var2;
   }

   public JDBCEventInfoImpl(String var1, String var2, boolean var3) {
      this.sql = var1;
      this.pool = var2;
      this.infected = var3;
      this.infectedSet = true;
   }

   public String getSql() {
      return this.sql;
   }

   public void setSql(String var1) {
      this.sql = var1;
   }

   public String getPool() {
      return this.pool;
   }

   public void setPool(String var1) {
      this.pool = var1;
   }

   public boolean getInfectedSet() {
      return this.infectedSet;
   }

   public boolean getInfected() {
      return this.infected;
   }

   public void setInfected(boolean var1) {
      this.infected = var1;
      this.infectedSet = true;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.pool != null) {
         var1.append("pool=");
         var1.append(this.pool);
         var1.append(",");
      }

      if (this.infectedSet) {
         var1.append("infected=");
         var1.append(this.infected);
         var1.append(",");
      }

      var1.append("sql=");
      var1.append(this.sql);
      return var1.toString();
   }
}
