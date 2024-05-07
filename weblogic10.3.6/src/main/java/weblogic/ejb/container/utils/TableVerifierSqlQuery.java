package weblogic.ejb.container.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSPersistenceManager;
import weblogic.ejb.container.interfaces.WLCMPPersistenceManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.utils.collections.ArraySet;

public final class TableVerifierSqlQuery extends TableVerifier {
   private boolean fireSql(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8, String[] var9) {
      assert var2 != null;

      assert var3 != null;

      assert var4 != null;

      StringBuffer var10 = new StringBuffer(200);
      var10.append("SELECT ");
      String var11 = this.strArrayToCommaList(var4);
      var10.append(var11);
      var10.append(" FROM ");
      var10.append(var3);
      if (var1 instanceof RDBMSPersistenceManager) {
         int var12 = ((RDBMSPersistenceManager)var1).getDatabaseType();
         if (var12 == 4) {
            var10.append(" FETCH FIRST 1 ROWS ONLY");
         } else {
            var10.append(" WHERE 1 = 0");
         }
      } else {
         var10.append(" WHERE 1 = 0");
      }

      String var36 = var10.toString();
      if (debugLogger.isDebugEnabled()) {
         debug("** Using query: " + var36);
      }

      Statement var13 = null;
      ResultSet var14 = null;
      ResultSetMetaData var15 = null;

      boolean var16;
      try {
         var13 = var2.createStatement();
         var14 = var13.executeQuery(var36);
         if (var5) {
            var15 = var14.getMetaData();
            Iterator var37 = var6.iterator();

            for(int var38 = 1; var38 <= var15.getColumnCount(); ++var38) {
               assert var37.hasNext() : "Have no variable names but a result column.";

               if (var11.trim().equals("*")) {
                  String var40 = var15.getColumnName(var38);
                  if (!this.isDbColumnInDeploymentDescriptor(var9, var40) && !"WLS_TEMP".equalsIgnoreCase(var40)) {
                     this.removedColumns.add(var40);
                  }
               } else {
                  Object var41 = var37.next();
                  var7.put(var41, new Integer(var15.getColumnType(var38)));
                  var8.put(var41, new Boolean(var15.isNullable(var38) != 0));
               }
            }

            boolean var39 = true;
            return var39;
         }

         var16 = true;
      } catch (Exception var34) {
         Loggable var17 = EJBLogger.logErrorGettingTableInformationLoggable(var3, var1.getEjbName(), var34.getMessage());
         this.wldexception = new WLDeploymentException(var17.getMessage(), var34);
         boolean var18 = false;
         return var18;
      } finally {
         try {
            if (var14 != null) {
               var14.close();
            }
         } catch (Exception var33) {
         }

         try {
            if (var13 != null) {
               var13.close();
            }
         } catch (Exception var32) {
         }

      }

      return var16;
   }

   private boolean isDbColumnInDeploymentDescriptor(String[] var1, String var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2.equalsIgnoreCase(var1[var3])) {
            return true;
         }
      }

      return false;
   }

   public int checkTableAndColumns(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) {
      this.newColumns = new ArraySet();
      this.removedColumns = new ArraySet();
      if (this.fireSql(var1, var2, var3, var4, var5, var6, var7, var8, var4)) {
         if (this.createDefaultDBMSTable != null && !this.createDefaultDBMSTable.equals("CreateOnly") && !this.createDefaultDBMSTable.equals("Disabled")) {
            if (this.fireSql(var1, var2, var3, new String[]{"*"}, true, var6, var7, var8, var4)) {
               this.removeTempColumnFromTheSet(this.removedColumns, "WLS_TEMP");
               return this.removedColumns.isEmpty() ? 1 : 3;
            } else {
               return 3;
            }
         } else {
            return 1;
         }
      } else if (!this.fireSql(var1, var2, var3, new String[]{"*"}, true, var6, var7, var8, var4)) {
         return 0;
      } else {
         for(int var9 = 0; var9 < var4.length; ++var9) {
            if (!this.fireSql(var1, var2, var3, new String[]{var4[var9]}, false, (List)null, (Map)null, (Map)null, var4)) {
               this.newColumns.add(var4[var9]);
            }
         }

         if (!this.newColumns.isEmpty()) {
            return 3;
         } else {
            throw new AssertionError("The table verification failed for an unknown reason.  Please try redeploying the bean");
         }
      }
   }

   public int isTableCreatedByContainer(WLCMPPersistenceManager var1, Connection var2, String var3) {
      StringBuffer var4 = new StringBuffer("SELECT * FROM ");
      var4.append(var3);
      if (var1 instanceof RDBMSPersistenceManager) {
         int var5 = ((RDBMSPersistenceManager)var1).getDatabaseType();
         if (var5 == 4) {
            var4.append(" FETCH FIRST 1 ROWS ONLY");
         } else {
            var4.append(" WHERE 1 = 0");
         }
      } else {
         var4.append(" WHERE 1 = 0");
      }

      Statement var23 = null;

      try {
         var23 = var2.createStatement();
         var23.executeQuery(var4.toString());
      } catch (Exception var20) {
         try {
            if (var23 != null) {
               var23.close();
            }
         } catch (Exception var18) {
         }

         return 0;
      }

      var4 = new StringBuffer();
      var4.append("SELECT ");
      var4.append("WLS_TEMP");
      var4.append(" FROM ");
      var4.append(var3);

      byte var7;
      try {
         var23 = var2.createStatement();
         var23.executeQuery(var4.toString());
         byte var6 = 4;
         return var6;
      } catch (Exception var21) {
         var7 = 5;
      } finally {
         try {
            if (var23 != null) {
               var23.close();
            }
         } catch (Exception var19) {
         }

      }

      return var7;
   }

   private boolean removeTempColumnFromTheSet(Set var1, String var2) {
      Iterator var3 = var1.iterator();

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
      debugLogger.debug("[TableVerifierSqlQuery] " + var0);
   }
}
