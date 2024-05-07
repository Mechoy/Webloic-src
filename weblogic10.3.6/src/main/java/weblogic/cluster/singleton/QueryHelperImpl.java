package weblogic.cluster.singleton;

public class QueryHelperImpl extends QueryHelper {
   public QueryHelperImpl(String var1, String var2, String var3, int var4) {
      super(var1, var2, var3, var4);
   }

   protected String addToDate(String var1, int var2) {
      switch (this.getDBType()) {
         case 1:
         case 7:
            return "(" + var1 + " + (" + var2 + TIME_AS_FRACTION_OF_DAY + "))";
         case 2:
         case 8:
         default:
            throw new AssertionError("Unsupported database driver");
         case 3:
         case 4:
            return "DATEADD(second, " + var2 + "," + var1 + ")";
         case 5:
            return var1 + " + " + var2 + " UNITS SECOND";
         case 6:
            return var1 + " + " + var2 + " SECONDS";
         case 9:
            return "ADDDATE(" + var1 + ", INTERVAL " + var2 + " SECOND)";
      }
   }

   protected String compareDates(String var1, String var2) {
      return this.compareDates(var1, var2, false);
   }

   protected String compareDates(String var1, String var2, boolean var3) {
      switch (this.getDBType()) {
         case 1:
         case 5:
         case 6:
         case 7:
         case 9:
            return var3 ? var1 + " >= " + var2 : var1 + " > " + var2;
         case 2:
         case 8:
         default:
            throw new AssertionError("Unsupported database driver");
         case 3:
         case 4:
            return var3 ? "DATEDIFF(second," + var1 + "," + var2 + ") <= 0" : "DATEDIFF(second," + var1 + "," + var2 + ") < 0";
      }
   }

   protected String getTimeFunction() {
      switch (this.getDBType()) {
         case 1:
            if (MigratableServerService.theOne() == null) {
               return "CURRENT_DATE";
            } else {
               if (MigratableServerService.theOne().isBEADriver()) {
                  return "CURRENT_DATE";
               }

               return "SYSDATE";
            }
         case 2:
         case 8:
         default:
            throw new AssertionError("Unsupported database driver");
         case 3:
            return "GETDATE()";
         case 4:
            return "GETDATE()";
         case 5:
            return "(CURRENT YEAR TO SECOND)";
         case 6:
            return "CURRENT TIMESTAMP";
         case 7:
            return "SYSDATE";
         case 9:
            return "NOW()";
      }
   }
}
