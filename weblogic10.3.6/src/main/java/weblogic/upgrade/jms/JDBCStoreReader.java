package weblogic.upgrade.jms;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.sql.DataSource;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.store.PersistentStoreException;
import weblogic.store.admin.JDBCAdminHandler;
import weblogic.store.io.jdbc.JDBCHelper;

public class JDBCStoreReader implements StoreReader {
   private static final boolean debug = false;
   private String prefixName;
   private UpgradeIOBypass ioBypass;
   private ClearOrEncryptedService encryptionService;
   private String prefix = "";
   private boolean didUpgrade;
   private boolean recoveredState;
   private boolean recoveryComplete;
   private ResultSet recoverResults;
   private long recoverCount;
   private Map st;
   private int dbmsType;
   private DataSource dataSource;
   private Connection conn;
   private PreparedStatement selectStmt1;
   private PreparedStatement selectStmt2;
   private PreparedStatement selectStmt3;
   private PreparedStatement selectStmt4;
   private boolean tablesExist;
   private static final int CURRENT_VERSION = 1;
   private int version;
   boolean isOpen;

   public JDBCStoreReader(JDBCStoreMBean var1, UpgradeIOBypass var2, ClearOrEncryptedService var3) throws JMSException {
      this.prefixName = var1.getPrefixName();
      this.ioBypass = var2;
      this.encryptionService = var3;
      this.prefix = this.prefixName;
      if (this.prefix == null) {
         this.prefix = "";
      } else {
         for(int var4 = this.prefix.indexOf("."); var4 >= 0; var4 = this.prefix.indexOf(".")) {
            this.prefix = this.prefix.substring(var4 + 1);
         }
      }

      this.st = new HashMap();

      try {
         this.dataSource = JDBCAdminHandler.createDataSource(var1, var3);
      } catch (PersistentStoreException var10) {
         throw new weblogic.jms.common.JMSException("Can't create database connection: " + var10, var10);
      }

      this.opendb();

      try {
         try {
            this.dbmsType = JDBCHelper.getDBMSType(this.conn.getMetaData(), (String[])null);
         } catch (SQLException var9) {
            throw new weblogic.jms.common.JMSException("Can't get database type: " + var9, var9);
         }

         if (this.tablesInitialized()) {
            this.tablesExist = true;
            this.prepareStatements();
            this.version = this.getVersion();
         }

         this.isOpen = true;
      } finally {
         if (!this.isOpen) {
            this.closedb();
         }

      }

   }

   public boolean requiresUpgrade() {
      return this.tablesExist && this.version == 1;
   }

   public boolean alreadyUpgraded() {
      return false;
   }

   public void reOpen() {
      this.recoveryComplete = false;
      this.recoverCount = 0L;
   }

   private StoreReader.Record doRecover() throws JMSException {
      if (!this.recoveredState) {
         this.recoverCount = 0L;
         this.doRecoverStates();
         this.recoverResults = null;
         this.recoveredState = true;
      }

      if (this.recoveryComplete) {
         return null;
      } else {
         if (this.recoverResults == null) {
            try {
               this.recoverResults = this.selectStmt3.executeQuery();
            } catch (SQLException var2) {
               throw new weblogic.jms.common.JMSException("Error executing recovery query", var2);
            }
         }

         StoreReader.Record var1 = this.doRecoverBodies();
         if (var1 == null) {
            this.ensureClosed(this.recoverResults);
            this.recoverResults = null;
            this.recoveryComplete = true;
         }

         return var1;
      }
   }

   private void doRecoverStates() throws JMSException {
      boolean var1 = false;

      try {
         if (this.conn == null) {
            this.opendb();
         }

         this.recoverResults = this.selectStmt2.executeQuery();
         var1 = true;

         while(this.recoverResults.next()) {
            long var2 = this.recoverResults.getLong("recordHandle");
            int var6 = this.recoverResults.getInt("recordState");
            long var4 = this.recoverResults.getLong("recordGeneration");
            if (var2 != -1L && var2 != -2L) {
               Long var7 = new Long(var2);
               JMSState var8 = (JMSState)this.st.get(var7);
               JMSState var9 = new JMSState(var6, var4);
               if (var8 != null) {
                  if (var8.generation < var4) {
                     this.st.put(var7, var9);
                  }
               } else {
                  this.st.put(var7, var9);
               }
            }
         }
      } catch (Exception var13) {
         var1 = false;
         throw new weblogic.jms.common.JMSException("Error recovering JMSState table", var13);
      } finally {
         if (var1) {
            this.ensureClosed(this.recoverResults);
         }

         this.recoverResults = null;
      }

   }

   private StoreReader.Record doRecoverBodies() throws JMSException {
      try {
         while(true) {
            if (this.recoverResults.next()) {
               long var1 = this.recoverResults.getLong("recordHandle");
               int var5 = this.recoverResults.getInt("recordState");
               byte[] var3 = this.recoverResults.getBytes("record");
               JMSState var4 = (JMSState)this.st.get(new Long(var1));
               if (var4 != null) {
                  if (var4.state == -1) {
                     continue;
                  }

                  var5 = var4.state;
               }

               Object var6;
               try {
                  BufferDataInputStream var7 = new BufferDataInputStream(this.ioBypass, ByteBuffer.wrap(var3));
                  var6 = var7.readObject();
               } catch (IOException var8) {
                  throw new weblogic.jms.common.JMSException(var8);
               } catch (ClassNotFoundException var9) {
                  throw new weblogic.jms.common.JMSException(var9);
               }

               ++this.recoverCount;
               return new StoreReader.Record(var1, var5, var6);
            }

            return null;
         }
      } catch (Exception var10) {
         throw new weblogic.jms.common.JMSException("Error recovering JMS messages", var10);
      }
   }

   public StoreReader.Record recover() throws JMSException {
      if (this.didUpgrade) {
         return this.doRecover();
      } else {
         this.didUpgrade = true;
         return this.doRecover();
      }
   }

   public void close() {
      this.isOpen = false;
      this.closedb();
   }

   private boolean tablesInitialized() throws JMSException {
      ResultSet var1 = null;
      String var2 = null;
      String var3 = null;

      try {
         DatabaseMetaData var4 = this.conn.getMetaData();
         boolean var7;
         boolean var8;
         boolean var9;
         if (this.dbmsType == 6) {
            var3 = var4.getUserName().toUpperCase();
         } else if (this.prefixName != null && this.prefixName != "") {
            int var5 = this.prefixName.indexOf(".");
            int var6 = -1;
            var7 = true;
            var8 = false;
            var9 = false;
            if (var5 >= 0) {
               var6 = this.prefixName.indexOf(".", var5 + 1);
               var9 = true;
            }

            if (var6 >= 0) {
               this.prefixName.indexOf(".", var6 + 1);
               var8 = true;
            }

            if (var8) {
               var2 = this.prefixName.substring(0, var5);
               var3 = this.prefixName.substring(var5 + 1, var6);
            } else if (var9) {
               var3 = this.prefixName.substring(0, var5);
            }
         }

         String var15 = "";
         switch (this.dbmsType) {
            case 1:
               var15 = this.prefix.toUpperCase() + "JMS";
            case 2:
            case 4:
            default:
               break;
            case 3:
               var15 = this.prefix + "JMS";
               break;
            case 5:
               var15 = this.prefix.toLowerCase() + "jms";
         }

         byte var16;
         if (this.dbmsType == 1 && var4.getDriverMajorVersion() == 10 && var4.getDatabaseProductVersion().startsWith("Oracle8i")) {
            if (var3 == null) {
               var3 = "";
            } else {
               var3 = " and OWNER LIKE '" + var3 + "'";
            }

            var1 = this.conn.createStatement().executeQuery("select TABLE_NAME from ALL_CATALOG where TABLE_NAME like '" + var15 + "%'" + var3);
            var16 = 1;
         } else {
            var1 = var4.getTables(var2, var3, var15 + "%", (String[])null);
            var16 = 3;
         }

         var7 = false;
         var8 = false;

         while(true) {
            if (var1.next()) {
               String var17 = var1.getString(var16);
               if (var17.equalsIgnoreCase(this.prefix + "JMSStore")) {
                  var8 = true;
                  if (!var7) {
                     continue;
                  }
               } else {
                  if (!var17.equalsIgnoreCase(this.prefix + "JMSState")) {
                     continue;
                  }

                  var7 = true;
                  if (!var8) {
                     continue;
                  }
               }
            }

            var9 = var7 && var8;
            return var9;
         }
      } catch (Exception var13) {
         throw new weblogic.jms.common.JMSException("Error checking for database tables", var13);
      } finally {
         this.ensureClosed(var1);
      }
   }

   private void ensureClosed(Connection var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (Exception var3) {
         }
      }

   }

   private void ensureClosed(Statement var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (Exception var3) {
         }
      }

   }

   private void ensureClosed(ResultSet var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (Exception var3) {
         }
      }

   }

   public int getVersion() {
      ResultSet var1 = null;
      int var2 = -1;
      boolean var3 = false;
      boolean var4 = false;

      try {
         if (this.conn == null) {
            this.opendb();
         }

         var1 = this.selectStmt4.executeQuery();
         var4 = true;
         if (var1.next()) {
            var2 = var1.getInt("recordState");
         }
      } catch (Exception var9) {
         var4 = false;
      } finally {
         if (var4) {
            this.ensureClosed(var1);
         }

      }

      return var2;
   }

   private void opendb() throws JMSException {
      if (this.conn == null) {
         try {
            this.conn = this.dataSource.getConnection();
            this.conn.setAutoCommit(true);
         } catch (Exception var2) {
            this.closedb();
            throw new weblogic.jms.common.JMSException("Error connecting to the database", var2);
         }
      }
   }

   private void prepareStatements() throws JMSException {
      try {
         this.selectStmt1 = this.conn.prepareStatement("SELECT record FROM " + this.prefix + "JMSStore " + "WHERE recordHandle = ?");
         this.selectStmt2 = this.conn.prepareStatement("SELECT recordHandle, recordState, recordGeneration FROM " + this.prefix + "JMSState");
         this.selectStmt3 = this.conn.prepareStatement("SELECT recordHandle, recordState, record FROM " + this.prefix + "JMSStore");
         this.selectStmt4 = this.conn.prepareStatement("SELECT recordHandle, recordState, recordGeneration FROM " + this.prefix + "JMSState" + " WHERE recordHandle = -1");
      } catch (Exception var2) {
         this.closedb();
         throw new weblogic.jms.common.JMSException("Error preparing SQL statements", var2);
      }
   }

   private void closedb() {
      try {
         this.ensureClosed((Statement)this.selectStmt1);
      } catch (Exception var6) {
      }

      try {
         this.ensureClosed((Statement)this.selectStmt2);
      } catch (Exception var5) {
      }

      try {
         this.ensureClosed((Statement)this.selectStmt3);
      } catch (Exception var4) {
      }

      try {
         this.ensureClosed((Statement)this.selectStmt4);
      } catch (Exception var3) {
      }

      try {
         this.ensureClosed(this.conn);
      } catch (Exception var2) {
      }

   }

   private static final class JMSScavenge {
      long handle;
      long generation;
      int state;
      JMSScavenge next;

      JMSScavenge(long var1, int var3, long var4) {
         this.handle = var1;
         this.generation = var4;
         this.state = var3;
      }
   }

   private static final class JMSState {
      int state;
      long generation;

      JMSState(int var1, long var2) {
         this.state = var1;
         this.generation = var2;
      }
   }
}
