package weblogic.ejb.container.utils;

import java.security.AccessController;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.interfaces.WLCMPPersistenceManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.jdbc.wrapper.JTSConnection;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public abstract class TableVerifier {
   protected static final DebugLogger debugLogger;
   private static final AuthenticatedSubject kernelId;
   public static final int TABLE_NOT_EXISTS = 0;
   protected static final int TABLE_EXISTS_AND_OK = 1;
   protected static final int POOL_NOT_EXISTS = 2;
   protected static final int TABLE_MISSING_COLUMNS = 3;
   protected static final int TABLE_CREATED_BY_CONTAINER = 4;
   protected static final int TABLE_CREATED_BY_USER = 5;
   public static final int SEQUENCE_NOT_EXISTS = 10;
   public static final int SEQUENCE_PROD_EXISTS_AND_OK = 11;
   public static final int SEQUENCE_DEV_EXISTS_AND_OK = 12;
   public static final int SEQUENCE_PROD_INCREMENT_LT_DBINCREMENT = 13;
   public static final int SEQUENCE_PROD_INCREMENT_GT_DBINCREMENT = 14;
   public static final int SEQUENCE_DEV_INCREMENT_LT_DBINCREMENT = 15;
   public static final int SEQUENCE_DEV_INCREMENT_GT_DBINCREMENT = 16;
   protected WLDeploymentException wldexception = null;
   protected Set newColumns;
   protected Set removedColumns;
   protected String createDefaultDBMSTable;
   private static Boolean productionModeEnabled;

   public static boolean isProductionModeEnabled() {
      if (productionModeEnabled == null) {
         boolean var0 = ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
         if (var0) {
            productionModeEnabled = Boolean.TRUE;
         } else {
            productionModeEnabled = Boolean.FALSE;
         }
      }

      return productionModeEnabled == Boolean.TRUE;
   }

   public void verifyOrCreateOrAlterTable(WLCMPPersistenceManager var1, Connection var2, String var3, List var4, boolean var5, List var6, Map var7, Map var8, String var9, boolean var10) throws WLDeploymentException {
      String[] var11 = (String[])((String[])var4.toArray(new String[0]));
      if (isProductionModeEnabled() && !var9.equals("Disabled")) {
         EJBLogger.logTableCannotBeCreatedInProductionMode();
         var9 = "Disabled";
      }

      if (var10 && var9 != null && !var9.equalsIgnoreCase("Disabled")) {
         EJBLogger.logTableUsesTriggerCannotBeDroppedOrCreated(var3);
         var9 = "Disabled";
      }

      if (var9 != null && (var9.equals("DropAndCreateAlways") || var9.equals("DropAndCreate") || var9.equals("AlterOrCreate"))) {
         int var12 = this.isTableCreatedByContainer(var1, var2, var3);
         if (var12 == 0) {
            var1.createDefaultDBMSTable(var3);
            return;
         }

         if (var12 == 5) {
            EJBLogger.logTableCreatedByUser(var3);
            var9 = "Disabled";
         }
      }

      this.createDefaultDBMSTable = var9;
      if (var9 != null && !var9.equalsIgnoreCase("Disabled")) {
         if (var9.equalsIgnoreCase("DropAndCreateAlways")) {
            var1.dropAndCreateDefaultDBMSTable(var3);
         } else if (var9.equalsIgnoreCase("DropAndCreate")) {
            this.dropAndCreate(var1, var2, var3, var11, var5, var6, var7, var8);
         } else if (var9.equalsIgnoreCase("AlterOrCreate")) {
            this.alterOrCreate(var1, var2, var3, var11, var5, var6, var7, var8);
         } else if (var9.equalsIgnoreCase("CreateOnly")) {
            this.createOnly(var1, var2, var3, var11, var5, var6, var7, var8);
         }
      } else {
         this.verifyTable(var1, var2, var3, var11, var5, var6, var7, var8);
      }

   }

   private void dropAndCreate(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) throws WLDeploymentException {
      int var9 = this.checkTableAndColumns(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 != 1) {
         var1.dropAndCreateDefaultDBMSTable(var3);
      }
   }

   private void createOnly(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) throws WLDeploymentException {
      int var9 = this.checkTableAndColumns(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 == 0) {
         var1.createDefaultDBMSTable(var3);
      } else if (var9 == 3) {
         if (!this.newColumns.isEmpty()) {
            String[] var12 = new String[this.newColumns.size()];
            var12 = (String[])((String[])this.newColumns.toArray(var12));
            Loggable var11 = EJBLogger.logCmpTableMissingColumnsLoggable(var3, this.strArrayToCommaList(var12));
            throw new WLDeploymentException(var11.getMessage());
         }

         Loggable var10 = EJBLogger.logCmpTableMissingColumnsLoggable(var3, this.strArrayToCommaList(new String[0]));
         throw new WLDeploymentException(var10.getMessage());
      }

   }

   private void alterOrCreate(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) throws WLDeploymentException {
      int var9 = this.checkTableAndColumns(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 == 0) {
         var1.createDefaultDBMSTable(var3);
      } else {
         var1.alterDefaultDBMSTable(var3, this.newColumns, this.removedColumns);
      }

   }

   private void verifyTable(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8) throws WLDeploymentException {
      int var9 = this.checkTableAndColumns(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 != 1) {
         if (var9 == 0) {
            Loggable var12 = EJBLogger.logDeploymentFailedTableDoesNotExistLoggable(var1.getEjbName(), var3);
            var12.log();
            throw new WLDeploymentException(var12.getMessage());
         } else if (var9 == 3) {
            if (!this.newColumns.isEmpty()) {
               String[] var10 = new String[this.newColumns.size()];
               var10 = (String[])((String[])this.newColumns.toArray(var10));
               Loggable var11 = EJBLogger.logCmpTableMissingColumnsLoggable(var3, this.strArrayToCommaList(var10));
               throw new WLDeploymentException(var11.getMessage());
            } else {
               throw new AssertionError("The table verification failed for unknown reason.Please try redeploying the bean");
            }
         }
      }
   }

   public abstract int checkTableAndColumns(WLCMPPersistenceManager var1, Connection var2, String var3, String[] var4, boolean var5, List var6, Map var7, Map var8);

   public abstract int isTableCreatedByContainer(WLCMPPersistenceManager var1, Connection var2, String var3);

   protected String strArrayToCommaList(String[] var1) {
      assert var1 != null;

      assert var1.length > 0;

      StringBuffer var2 = new StringBuffer(200);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var3 != 0) {
            var2.append(", ");
         }

         var2.append(var1[var3]);
      }

      return var2.toString();
   }

   public String verifyOrCreateOrAlterSequence(Connection var1, String var2, int var3, String var4, int var5) throws WLDeploymentException {
      if (isProductionModeEnabled() && !var4.equals("Disabled")) {
         EJBLogger.logSequenceCannotBeAlteredInProductionMode(var2);
         var4 = "Disabled";
      }

      int var6 = this.verifySequence(var1, var2, var3, var5);
      if (var5 == 1 && var6 == 10) {
         String var7 = var2;
         var2 = this.resolveIfOracleSynonym(var1, var2);
         if (debugLogger.isDebugEnabled() && var2 != var7) {
            debug(var7 + " resolved to sequence: " + var2 + ", verifying the validity of the actual Oracle sequence");
         }

         var6 = this.verifySequence(var1, var2, var3, var5);
         if ((var6 == 11 || var6 == 12) && debugLogger.isDebugEnabled()) {
            debug(var7 + " resolved to a valid oracle sequence: " + var2);
            var2 = var7;
         }
      }

      if (isProductionModeEnabled()) {
         if (var6 == 11) {
            return var2;
         }

         if (var6 == 13) {
            this.logWarningSequenceIncrementLesserThanDBIncrement(var2, var3);
            return var2;
         }

         if (var6 == 14) {
            this.throwSequenceIncrementGreaterThanDBIncrement(var2, var3);
         }

         this.throwSequenceNotExists(var2, var3);
      }

      if (var6 == 11) {
         return var2;
      } else if (var6 == 12) {
         return RDBMSUtils.getContainerSequenceName(var2);
      } else if (var6 == 13) {
         this.logWarningSequenceIncrementLesserThanDBIncrement(var2, var3);
         return var2;
      } else {
         if (var4 == null || var4.equalsIgnoreCase("Disabled")) {
            if (var6 == 10) {
               this.throwSequenceNotExists(var2, var3);
            } else {
               if (var6 == 15) {
                  this.logWarningSequenceIncrementLesserThanDBIncrement(RDBMSUtils.getContainerSequenceName(var2), var3);
                  return RDBMSUtils.getContainerSequenceName(var2);
               }

               if (var6 == 14) {
                  this.throwSequenceIncrementGreaterThanDBIncrement(var2, var3);
               } else {
                  if (var6 != 16) {
                     throw new AssertionError("Sequence: '" + var2 + "' increment '" + var3 + "' Unknown SEQUENCE STATUS CODE: " + var6);
                  }

                  this.throwSequenceIncrementGreaterThanDBIncrement(RDBMSUtils.getContainerSequenceName(var2), var3);
               }
            }
         }

         if (var6 == 14) {
            this.throwSequenceIncrementGreaterThanDBIncrement(var2, var3);
         }

         if (var4.equalsIgnoreCase("CreateOnly")) {
            return this.createOnlySequence(var1, var6, RDBMSUtils.getContainerSequenceName(var2), var3, var5);
         } else {
            assert var4.equalsIgnoreCase("DropAndCreateAlways") || var4.equalsIgnoreCase("DropAndCreate") || var4.equalsIgnoreCase("AlterOrCreate") : "Unknown table creation option code " + var4;

            return this.alterOrCreateSequence(var1, var6, RDBMSUtils.getContainerSequenceName(var2), var3, var5);
         }
      }
   }

   private String resolveIfOracleSynonym(Connection var1, String var2) throws WLDeploymentException {
      PreparedStatement var3 = null;
      ResultSet var4 = null;
      String var5 = null;
      boolean var6 = false;
      String var7 = null;
      String var8 = null;
      String var9 = null;

      try {
         int var26;
         if ((var26 = var2.indexOf(".")) != -1) {
            var7 = var2.substring(0, var26);
            var8 = var2.substring(var26 + 1);
            var5 = "SELECT TABLE_NAME FROM ALL_SYNONYMS WHERE SYNONYM_NAME= UPPER( ? ) AND OWNER = UPPER( ? )";
            var3 = var1.prepareStatement(var5);
            var3.setString(1, var8);
            var3.setString(2, var7);
         } else {
            var5 = "SELECT TABLE_NAME FROM ALL_SYNONYMS WHERE SYNONYM_NAME= UPPER( ? )";
            var3 = var1.prepareStatement(var5);
            var3.setString(1, var2);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("Query for resolving the synonym to its sequenceName: " + var5);
         }

         var4 = var3.executeQuery();
         if (var4.next()) {
            var9 = var4.getString("TABLE_NAME");
         } else {
            var9 = var2;
         }
      } catch (Exception var24) {
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var23) {
         }

         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var22) {
         }

      }

      return var9;
   }

   private int verifySequence(Connection var1, String var2, int var3, int var4) throws WLDeploymentException {
      Statement var5 = null;
      ResultSet var6 = null;
      String var7 = null;
      boolean var8 = false;
      String var9 = null;
      String var10 = null;
      String var11 = null;
      int var39;
      if ((var39 = var2.indexOf(".")) != -1) {
         var9 = var2.substring(0, var39);
         var10 = var2.substring(var39 + 1);
         var7 = this.getSequenceQuery(var4, var10, var9);
      } else {
         var7 = this.getSequenceQuery(var4, var2, (String)null);
      }

      byte var41;
      try {
         var5 = var1.createStatement();
         var6 = var5.executeQuery(var7);
         int var12;
         if (!var6.next()) {
            if (var9 != null) {
               var11 = RDBMSUtils.getContainerSequenceName(var10);
               var7 = this.getSequenceQuery(var4, var11, var9);
            } else {
               var11 = RDBMSUtils.getContainerSequenceName(var2);
               var7 = this.getSequenceQuery(var4, var11, (String)null);
            }

            var6 = var5.executeQuery(var7);
            if (var6.next()) {
               var12 = var6.getInt(1);
               if (var3 < Math.abs(var12)) {
                  var41 = 15;
                  return var41;
               }

               if (var3 > Math.abs(var12)) {
                  var41 = 16;
                  return var41;
               }

               var41 = 12;
               return var41;
            }

            byte var40 = 10;
            return var40;
         }

         var12 = var6.getInt(1);
         if (var3 < Math.abs(var12)) {
            var41 = 13;
            return var41;
         }

         if (var3 > Math.abs(var12)) {
            var41 = 14;
            return var41;
         }

         var41 = 11;
      } catch (Exception var37) {
         Loggable var13 = EJBLogger.logSequenceSetupFailureLoggable(var2, Integer.toString(var3), var37.getMessage());
         throw new WLDeploymentException(var13.getMessage(), var37);
      } finally {
         try {
            if (var6 != null) {
               var6.close();
            }
         } catch (Exception var36) {
         }

         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var35) {
         }

      }

      return var41;
   }

   private String getSequenceQuery(int var1, String var2, String var3) {
      switch (var1) {
         case 1:
            if (var3 != null) {
               return "SELECT INCREMENT_BY FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = UPPER('" + var2 + "') " + "AND SEQUENCE_OWNER = UPPER('" + var3 + "')";
            }

            return "SELECT INCREMENT_BY FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = UPPER('" + var2 + "')";
         case 2:
         default:
            throw new AssertionError("Unexpected dbType: " + var1);
         case 3:
            if (var3 != null) {
               return "SELECT SEQ.INC_VAL FROM SYSSEQUENCES SEQ, SYSTABLES TAB WHERE UPPER(TAB.TABNAME)=UPPER('" + var2 + "') " + "AND UPPER(TAB.OWNER)=UPPER('" + var3 + "') " + "AND SEQ.TABID=TAB.TABID";
            }

            return "SELECT SEQ.INC_VAL FROM SYSSEQUENCES SEQ, SYSTABLES TAB WHERE UPPER(TAB.TABNAME)=UPPER('" + var2 + "') " + "AND SEQ.TABID=TAB.TABID";
         case 4:
            return var3 != null ? "SELECT INCREMENT FROM SYSIBM.SYSSEQUENCES WHERE UPPER(SEQNAME)=UPPER('" + var2 + "') " + "AND UPPER(SEQSCHEMA)=UPPER('" + var3 + "')" : "SELECT INCREMENT FROM SYSIBM.SYSSEQUENCES WHERE UPPER(SEQNAME)=UPPER('" + var2 + "')";
      }
   }

   private String createOnlySequence(Connection var1, int var2, String var3, int var4, int var5) throws WLDeploymentException {
      Debug.assertion(RDBMSUtils.isContainerSequenceName(var3), "called with a non-development sequence name '" + var3 + "'");
      if (var2 == 16 || var2 == 15) {
         this.throwIncrementMismatch(var3, var4);
      }

      return this.createSequence(var1, var3, var4, var5);
   }

   private String alterOrCreateSequence(Connection var1, int var2, String var3, int var4, int var5) throws WLDeploymentException {
      Debug.assertion(RDBMSUtils.isContainerSequenceName(var3), "called with a non-development sequence name '" + var3 + "'");
      if (var2 == 12) {
         return var3;
      } else if (var2 == 10) {
         return this.createOnlySequence(var1, var2, var3, var4, var5);
      } else if (var2 != 16 && var2 != 15) {
         throw new AssertionError("Unknown SEQUENCE STATUS CODE: " + var2);
      } else {
         return this.alterSequence(var1, var3, var4, var5);
      }
   }

   private String createSequence(Connection var1, String var2, int var3, int var4) throws WLDeploymentException {
      Debug.assertion(RDBMSUtils.isContainerSequenceName(var2), "called with a non-development sequence name '" + var2 + "'");
      Statement var5 = null;
      Object var6 = null;
      String var7 = this.getSequenceCreateStatement(var4, var2, var3);

      String var8;
      try {
         Loggable var9;
         try {
            var5 = var1.createStatement();

            try {
               var5.executeUpdate(var7);
            } catch (Exception var19) {
               var9 = EJBLogger.logFailedToCreateSequenceLoggable(var2, Integer.toString(var3), var19.getMessage());
               throw new WLDeploymentException(var9.getMessage(), var19);
            }

            var8 = var2;
         } catch (Exception var20) {
            var9 = EJBLogger.logSequenceSetupFailureLoggable(var2, Integer.toString(var3), var20.getMessage());
            throw new WLDeploymentException(var9.getMessage(), var20);
         }
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var18) {
         }

      }

      return var8;
   }

   private String getSequenceCreateStatement(int var1, String var2, int var3) {
      switch (var1) {
         case 1:
         case 4:
            return "CREATE SEQUENCE " + var2 + " INCREMENT BY " + var3;
         case 2:
         default:
            throw new AssertionError("Unexpected dbType: " + var1);
         case 3:
            return "CREATE SEQUENCE " + var2 + " INCREMENT " + var3;
      }
   }

   private String alterSequence(Connection var1, String var2, int var3, int var4) throws WLDeploymentException {
      Debug.assertion(RDBMSUtils.isContainerSequenceName(var2), "called with a non-development sequence name '" + var2 + "'");
      Statement var5 = null;
      String var6 = this.getSequenceAlterStatement(var4, var2, var3);

      String var7;
      try {
         var5 = var1.createStatement();
         var5.executeUpdate(var6);
         var7 = var2;
      } catch (Exception var17) {
         Loggable var8 = EJBLogger.logFailedToAlterSequenceLoggable(var2, Integer.toString(var3), var17.getMessage());
         throw new WLDeploymentException(var8.getMessage(), var17);
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var16) {
         }

      }

      return var7;
   }

   private String getSequenceAlterStatement(int var1, String var2, int var3) {
      switch (var1) {
         case 1:
         case 4:
            return "ALTER SEQUENCE " + var2 + " INCREMENT BY " + var3;
         case 2:
         default:
            throw new AssertionError("Unexpected dbType: " + var1);
         case 3:
            return "ALTER SEQUENCE " + var2 + " INCREMENT " + var3;
      }
   }

   private void throwSequenceNotExists(String var1, int var2) throws WLDeploymentException {
      Loggable var3 = EJBLogger.logSequenceNotExistLoggable(var1, Integer.toString(var2));
      throw new WLDeploymentException(var3.getMessage());
   }

   private void throwIncrementMismatch(String var1, int var2) throws WLDeploymentException {
      Loggable var3 = EJBLogger.logSequenceIncrementMismatchLoggable(var1, Integer.toString(var2));
      throw new WLDeploymentException(var3.getMessage());
   }

   private void logWarningSequenceIncrementLesserThanDBIncrement(String var1, int var2) {
      EJBLogger.logWarningSequenceIncrementLesserThanDBIncrement(var1, Integer.toString(var2));
   }

   private void throwSequenceIncrementGreaterThanDBIncrement(String var1, int var2) throws WLDeploymentException {
      Loggable var3 = EJBLogger.logErrorSequenceIncrementGreaterThanDBIncrementLoggable(var1, Integer.toString(var2));
      throw new WLDeploymentException(var3.getMessage());
   }

   public int verifyDatabaseType(Connection var1, int var2) {
      try {
         DatabaseMetaData var3 = var1.getMetaData();
         String var4 = var3.getDatabaseProductName();
         String var5 = var3.getDriverName();
         if (debugLogger.isDebugEnabled()) {
            debug("DB product name is " + var4);
            debug("DB Driver name is " + var5);
         }

         if (var4.indexOf("Microsoft") != -1) {
            var4 = "SQL_SERVER";
         }

         if (var4.indexOf("Derby") != -1) {
            var4 = "DERBY";
         } else if (var4.indexOf("DB2") != -1) {
            var4 = "DB2";
         } else if (var4.indexOf("Sybase") == -1 && var4.indexOf("Adaptive Server Enterprise") == -1 && !var5.startsWith("Sybase") && !var5.startsWith("jConnect")) {
            if (var4.indexOf("Informix") != -1) {
               var4 = "INFORMIX";
            }
         } else {
            var4 = "SYBASE";
         }

         if (var2 == 0) {
            Integer var6 = (Integer)DDConstants.DBTYPE_MAP.get(var4.toUpperCase(Locale.ENGLISH));
            if (var6 != null) {
               var2 = var6;
            }
         } else {
            String var8 = DDConstants.getDBNameForType(var2);
            if (var8 != null && var8.equalsIgnoreCase("SQLSERVER")) {
               var8 = "SQL_SERVER";
            }

            if (var8 != null && var8.equalsIgnoreCase("SQLSERVER2000")) {
               var8 = "SQL_SERVER";
            }

            if (var8 != null && !var4.equalsIgnoreCase(var8)) {
               EJBLogger.logErrorAboutDatabaseType(var8, var4);
            }
         }
      } catch (Exception var7) {
      }

      return var2;
   }

   private String getPoolName(Connection var1) {
      String var2 = "";
      if (var1 instanceof JTSConnection) {
         JTSConnection var3 = (JTSConnection)var1;
         var2 = var3.getPool();
      }

      return var2;
   }

   private static void debug(String var0) {
      debugLogger.debug("[TableVerifier] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
