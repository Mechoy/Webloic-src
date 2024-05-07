package weblogic.cluster.singleton;

import java.util.Locale;

public class QueryDumper {
   public static final String TABLE_NAME = "dummyTable";
   public static final String MACHINE_TABLE_NAME = "dummyMachineTable";
   public static final String LEASE_NAME = "dummyLease";
   public static final int LEASE_TIMEOUT = 15;

   private static int getDBType(String var0) {
      var0 = var0.toLowerCase(Locale.ENGLISH);

      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var2) {
         if (var0.indexOf("oracle") != -1) {
            return 1;
         } else if (var0.indexOf("sybase") != -1) {
            return 3;
         } else if (var0.indexOf("microsoft") == -1 && var0.indexOf("mssql") == -1) {
            if (var0.indexOf("informix") != -1) {
               return 5;
            } else if (var0.indexOf("db2") != -1) {
               return 6;
            } else if (var0.indexOf("mysql") != -1) {
               return 9;
            } else {
               return var0.indexOf("timesten") != -1 ? 7 : 0;
            }
         } else {
            return 4;
         }
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         System.out.println("err: " + var0);
         System.exit(1);
      }

      String var1 = var0[0];
      QueryHelper var2 = getQueryHelper(var1, "dummyTable", "dummyDomain", "dummyCluster", getDBType(var0[1]));
      if (var2 == null) {
         System.out.println("no query helper");
         System.exit(2);
      }

      String var3 = new String("165L/server1");
      String var4 = null;
      var4 = var2.getAcquireLeaseQuery("dummyLease", var3);
      System.out.println("getAcquireLeaseQuery: \n\t" + var4 + "\n");
      var4 = var2.getAssumeLeaseQuery("dummyLease", var3, 15);
      System.out.println("getAssumeLeaseQuery: \n\t" + var4 + "\n");
      var4 = var2.getRenewLeaseQuery("dummyLease", var3, 15);
      System.out.println("getRenewLeaseQuery: \n\t" + var4 + "\n");
      var4 = var2.getAbdicateLeaseQuery("dummyLease", var3);
      System.out.println("getAbdicateLeaseQuery: \n\t" + var4 + "\n");
      var4 = var2.getRenewAllLeasesQuery(var3, 15);
      System.out.println("getRenewAllLeasesQuery: \n\t" + var4 + "\n");
      var4 = var2.getLeaseOwnerQuery("dummyLease");
      System.out.println("getLeaseOwnerQuery: \n\t" + var4 + "\n");
      var4 = var2.getUnresponsiveMigratableServersQuery(60);
      System.out.println("getUnresponsiveMigratableServersQuery: \n\t" + var4 + "\n");
      var4 = var2.getDeleteMachineQuery("dummyServer", "dummyMachineTable");
      System.out.println("getDeleteMachineQuery: \n\t" + var4 + "\n");
      var4 = var2.getInsertMachineQuery("dummyServer", "dummyMachine", "dummyMachineTable");
      System.out.println("getInsertMachineQuery: \n\t" + var4 + "\n");
      var4 = var2.getRetrieveMachineQuery("dummyServer", "dummyMachineTable");
      System.out.println("getRetrieveMachineQuery: \n\t" + var4 + "\n");
   }

   private static QueryHelper getQueryHelper(String var0, String var1, String var2, String var3, int var4) {
      try {
         return new QueryHelperImpl(var1, var2, var3, var4);
      } catch (Throwable var6) {
         System.out.println("Error creating " + var0 + ": " + var6);
         var6.printStackTrace();
         return null;
      }
   }
}
