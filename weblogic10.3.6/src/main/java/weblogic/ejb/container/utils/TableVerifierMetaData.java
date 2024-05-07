package weblogic.ejb.container.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.WLCMPPersistenceManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.utils.collections.ArraySet;

public final class TableVerifierMetaData extends TableVerifier {
   public int checkTableAndColumns(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) {
      ResultSet var9 = null;
      if (debugLogger.isDebugEnabled()) {
         debug("Validating Table using meta data");
      }

      boolean var10 = false;

      byte var12;
      try {
         try {
            DatabaseMetaData var11 = var2.getMetaData();
            String var41 = "";
            String var42 = null;
            int var14 = var3.indexOf(".");
            if (var14 != -1) {
               var41 = var3.substring(var14 + 1, var3.length());
               var42 = var3.substring(0, var14);
               var42 = this.getSchemas(var11, var42);
               var41 = this.getStoredTableName(var11, var41, var42);
            } else {
               var41 = this.getStoredTableName(var11, var3, (String)null);
            }

            if (var41 == null) {
               byte var43 = 0;
               return var43;
            }

            var10 = true;
            if (debugLogger.isDebugEnabled()) {
               debug("The table name is " + var41);
               debug("The schema name is " + var42);
            }

            var9 = var11.getColumns((String)null, var42, var41, (String)null);
            ArrayList var15 = new ArrayList();
            ArrayList var16 = new ArrayList();
            HashMap var17 = new HashMap();

            while(var9.next()) {
               String var18 = var9.getString("COLUMN_NAME");
               var15.add(var18);
               var16.add(new Integer(var9.getShort("DATA_TYPE")));
               var17.put(var18.toUpperCase(Locale.ENGLISH), new Boolean(var9.getShort("NULLABLE") != 0));
            }

            this.newColumns = new ArraySet();
            this.removedColumns = new ArraySet();

            for(int var44 = 0; var44 < var4.length; ++var44) {
               String var19 = var4[var44];
               if (!this.isColumnPresentInTable(var15, var19)) {
                  this.newColumns.add(var19);
               } else {
                  if (var5) {
                     var7.put(var6.get(var44), var16.get(var44));
                  }

                  if (var8 != null) {
                     var8.put(var6.get(var44), var17.get(var19.toUpperCase(Locale.ENGLISH)));
                  }
               }
            }

            this.isColumnPresentInTable(var15, "WLS_TEMP");
            this.removedColumns.addAll(var15);
            byte var45;
            if (this.newColumns.isEmpty() && this.removedColumns.isEmpty()) {
               var45 = 1;
               return var45;
            }

            if (this.newColumns.isEmpty()) {
               if (this.createDefaultDBMSTable != null && !this.createDefaultDBMSTable.equals("CreateOnly") && !this.createDefaultDBMSTable.equals("Disabled")) {
                  var45 = 3;
                  return var45;
               }

               var45 = 1;
               return var45;
            }

            var45 = 3;
            return var45;
         } catch (SQLException var37) {
            Loggable var40 = EJBLogger.logErrorGettingTableInformationLoggable(var3, var1.getEjbName(), var37.getMessage());
            this.wldexception = new WLDeploymentException(var40.getMessage(), var37);
            byte var13;
            if (var10) {
               var13 = 3;
               return var13;
            }

            var13 = 0;
            return var13;
         } catch (Exception var38) {
            if (var10) {
               var12 = 3;
               return var12;
            }
         }

         var12 = 0;
      } finally {
         try {
            if (var9 != null) {
               var9.close();
            }
         } catch (Exception var36) {
         }

      }

      return var12;
   }

   public int isTableCreatedByContainer(WLCMPPersistenceManager var1, Connection var2, String var3) {
      ResultSet var4 = null;
      boolean var5 = false;

      byte var8;
      try {
         try {
            DatabaseMetaData var6 = var2.getMetaData();
            String var26 = "";
            String var27 = null;
            int var9 = var3.indexOf(".");
            if (var9 != -1) {
               var26 = var3.substring(var9 + 1, var3.length());
               var27 = var3.substring(0, var9);
               var27 = this.getSchemas(var6, var27);
               var26 = this.getStoredTableName(var6, var26, var27);
            } else {
               var26 = this.getStoredTableName(var6, var3, var27);
            }

            byte var28;
            if (var26 == null) {
               var28 = 0;
               return var28;
            }

            var5 = true;
            var4 = var6.getColumns((String)null, var27, var26, (String)null);

            String var10;
            do {
               if (!var4.next()) {
                  var28 = 5;
                  return var28;
               }

               var10 = var4.getString("COLUMN_NAME");
            } while(!var10.equalsIgnoreCase("WLS_TEMP"));

            byte var11 = 4;
            return var11;
         } catch (Exception var24) {
            Loggable var7 = EJBLogger.logErrorGettingTableInformationLoggable(var3, var1.getEjbName(), var24.getMessage());
            this.wldexception = new WLDeploymentException(var7.getMessage(), var24);
            if (!var5) {
               var8 = 0;
               return var8;
            }
         }

         var8 = 5;
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var23) {
         }

      }

      return var8;
   }

   private String getStoredTableName(DatabaseMetaData var1, String var2, String var3) {
      ResultSet var4 = null;

      String var6;
      try {
         var4 = var1.getTables((String)null, var3, (String)null, (String[])null);

         String var5;
         while(var4.next()) {
            var5 = var4.getString("TABLE_NAME");
            if (var2.equalsIgnoreCase(var5)) {
               var6 = var5;
               return var6;
            }
         }

         var5 = null;
         return var5;
      } catch (SQLException var17) {
         var6 = null;
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var16) {
         }

      }

      return var6;
   }

   private String getSchemas(DatabaseMetaData var1, String var2) {
      ResultSet var3 = null;

      String var5;
      try {
         var3 = var1.getSchemas();

         String var4;
         do {
            if (!var3.next()) {
               var4 = var2;
               return var4;
            }

            var4 = var3.getString("TABLE_SCHEM");
         } while(!var2.equalsIgnoreCase(var4));

         var5 = var4;
         return var5;
      } catch (SQLException var16) {
         var5 = var2;
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var15) {
         }

      }

      return var5;
   }

   private boolean isColumnPresentInTable(List var1, String var2) {
      ListIterator var3 = var1.listIterator();

      Object var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = var3.next();
      } while(!var2.equalsIgnoreCase((String)var4));

      var3.remove();
      return true;
   }

   private static void debug(String var0) {
      debugLogger.debug("[TableVerifierMetaData] " + var0);
   }
}
