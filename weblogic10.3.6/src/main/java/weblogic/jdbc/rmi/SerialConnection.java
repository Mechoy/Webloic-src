package weblogic.jdbc.rmi;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.transaction.Transaction;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.ConnectionLeakProfile;
import weblogic.jdbc.common.internal.ProfileStorage;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.StackTraceUtils;

public class SerialConnection extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = 7761262947309720591L;
   protected Set lobsets = Collections.synchronizedSet(new HashSet());
   private Connection rmi_conn;
   private Set<Statement> stmts = Collections.synchronizedSet(new HashSet());
   private Throwable stackTraceSource = null;
   private String poolName = null;
   private boolean connectionClosed = false;
   private boolean createdInThisVM = true;
   private boolean inited = false;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, var3);
         return null;
      } else {
         try {
            if (var3 instanceof CallableStatement) {
               CallableStatement var7 = SerialCallableStatement.makeSerialCallableStatement((CallableStatement)var3, this);
               this.stmts.add(var7);
               super.postInvocationHandler(var1, var2, var7);
               return var7;
            }

            if (var3 instanceof PreparedStatement) {
               PreparedStatement var6 = SerialPreparedStatement.makeSerialPreparedStatement((PreparedStatement)var3, this);
               this.stmts.add(var6);
               super.postInvocationHandler(var1, var2, var6);
               return var6;
            }

            if (var3 instanceof Statement) {
               Statement var4 = SerialStatement.makeSerialStatement((Statement)var3, this);
               this.stmts.add(var4);
               super.postInvocationHandler(var1, var2, var4);
               return var4;
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void preInvocationHandler(String var1, Object[] var2) throws Exception {
      super.preInvocationHandler(var1, var2);
      if (!var1.equals("close") && !var1.equals("isClosed") && !var1.equals("isValid") && this.connectionClosed) {
         throw new SQLException("Connection has already been closed.");
      }
   }

   public SerialConnection() {
   }

   public SerialConnection(Connection var1) {
      this.init(var1);
   }

   public void finalize() {
      if (!this.connectionClosed && !this.createdInThisVM && this.inited && this.stackTraceSource != null) {
         if (this.poolName != null) {
            ConnectionLeakProfile var1 = new ConnectionLeakProfile(this.poolName, StackTraceUtils.throwable2StackTrace(this.stackTraceSource));
            ProfileStorage.storeLeakedConnTrace(var1);
         }

         String var5 = StackTraceUtils.throwable2StackTrace(this.stackTraceSource);

         try {
            String var2 = ":";
            var5 = var5.substring(var5.indexOf(var2) + var2.length());
         } catch (Exception var4) {
         }

         JDBCLogger.logConnectionLeakWarning(var5);

         try {
            this.close();
         } catch (Exception var3) {
         }
      }

   }

   public void init(Connection var1) {
      this.rmi_conn = var1;
      this.stackTraceSource = new Exception("rmi connection initialized:");
      this.inited = true;
   }

   public Object readResolve() throws ObjectStreamException {
      SerialConnection var1 = null;

      try {
         var1 = (SerialConnection)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialConnection", this.rmi_conn, false);
         var1.init(this.rmi_conn);
         return (Connection)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.rmi_conn;
      }
   }

   public void addToLobSet(Object var1) {
      this.lobsets.add(var1);
   }

   public Connection getDelegate() {
      return this.rmi_conn;
   }

   void removeStatement(SerialStatement var1) {
      this.stmts.remove(var1);
   }

   public Statement createStatement() throws SQLException {
      Statement var1 = null;
      String var2 = "createStatement";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         Statement var4 = this.rmi_conn.createStatement();
         if (var4 != null) {
            var1 = SerialStatement.makeSerialStatement(var4, this);
            this.stmts.add(var1);
         }

         super.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public Statement createStatement(int var1, int var2) throws SQLException {
      Statement var3 = null;
      String var4 = "createStatement";
      Object[] var5 = new Object[]{new Integer(var1), new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         Statement var6 = this.rmi_conn.createStatement(var1, var2);
         if (var6 != null) {
            var3 = SerialStatement.makeSerialStatement(var6, this);
            this.stmts.add(var3);
         }

         super.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public PreparedStatement prepareStatement(String var1) throws SQLException {
      PreparedStatement var2 = null;
      String var3 = "prepareStatement";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         PreparedStatement var5 = this.rmi_conn.prepareStatement(var1);
         if (var5 != null) {
            var2 = SerialPreparedStatement.makeSerialPreparedStatement(var5, this);
            this.stmts.add(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public PreparedStatement prepareStatement(String var1, int var2, int var3) throws SQLException {
      PreparedStatement var4 = null;
      String var5 = "prepareStatement";
      Object[] var6 = new Object[]{var1, new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         PreparedStatement var7 = this.rmi_conn.prepareStatement(var1, var2, var3);
         if (var7 != null) {
            var4 = SerialPreparedStatement.makeSerialPreparedStatement(var7, this);
            this.stmts.add(var4);
         }

         super.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public CallableStatement prepareCall(String var1) throws SQLException {
      CallableStatement var2 = null;
      String var3 = "prepareCall";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         CallableStatement var5 = this.rmi_conn.prepareCall(var1);
         if (var5 != null) {
            var2 = SerialCallableStatement.makeSerialCallableStatement(var5, this);
            this.stmts.add(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public CallableStatement prepareCall(String var1, int var2, int var3) throws SQLException {
      CallableStatement var4 = null;
      String var5 = "prepareCall";
      Object[] var6 = new Object[]{var1, new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         CallableStatement var7 = this.rmi_conn.prepareCall(var1, var2, var3);
         if (var7 != null) {
            var4 = SerialCallableStatement.makeSerialCallableStatement(var7, this);
            this.stmts.add(var4);
         }

         super.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public void close() throws SQLException {
      String var1 = "close";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         if (!this.connectionClosed) {
            this.connectionClosed = true;
            Exception var3 = null;

            try {
               this.closeAndClearAllLobs();
            } catch (Exception var7) {
               if (var3 == null) {
                  var3 = var7;
               }
            }

            try {
               this.closeAndClearAllStatements();
            } catch (Exception var6) {
               if (var3 == null) {
                  var3 = var6;
               }
            }

            try {
               this.rmi_conn.close();
            } catch (Exception var5) {
               if (var3 == null) {
                  var3 = var5;
               }
            }

            if (var3 != null) {
               throw var3;
            }
         }

         super.postInvocationHandler(var1, var2, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var1, var2, var8);
      }

   }

   public boolean isClosed() throws SQLException {
      String var1 = "isClosed";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         super.postInvocationHandler(var1, var2, new Boolean(this.connectionClosed));
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

      return this.connectionClosed;
   }

   public DatabaseMetaData getMetaData() throws SQLException {
      DatabaseMetaData var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         Transaction var4 = null;

         try {
            var4 = suspend();
            DatabaseMetaData var5 = this.rmi_conn.getMetaData();
            if (var5 != null) {
               SerialDatabaseMetaData var6 = (SerialDatabaseMetaData)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialDatabaseMetaData", var5, false);
               var1 = (DatabaseMetaData)var6;
            }
         } finally {
            if (var4 != null) {
               resume(var4);
            }

         }

         super.postInvocationHandler(var2, var3, var1);
      } catch (Exception var12) {
         this.invocationExceptionHandler(var2, var3, var12);
      }

      return var1;
   }

   public void setReadOnly(boolean var1) throws SQLException {
      Transaction var2 = null;
      String var3 = "setReadOnly";
      Object[] var4 = new Object[]{new Boolean(var1)};

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var2 = suspend();
            this.rmi_conn.setReadOnly(var1);
         } finally {
            resume(var2);
         }

         super.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

   }

   public boolean isReadOnly() throws SQLException {
      Transaction var1 = null;
      boolean var2 = false;
      String var3 = "isReadOnly";
      Object[] var4 = new Object[0];

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var1 = suspend();
            var2 = this.rmi_conn.isReadOnly();
         } finally {
            resume(var1);
         }

         super.postInvocationHandler(var3, var4, new Boolean(var2));
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

      return var2;
   }

   public void setCatalog(String var1) throws SQLException {
      Transaction var2 = null;
      String var3 = "setCatalog";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var2 = suspend();
            this.rmi_conn.setCatalog(var1);
         } finally {
            resume(var2);
         }

         super.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

   }

   public String getCatalog() throws SQLException {
      String var1 = null;
      String var2 = "getCatalog";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         Transaction var4 = null;

         try {
            var4 = suspend();
            var1 = this.rmi_conn.getCatalog();
         } finally {
            if (var4 != null) {
               resume(var4);
            }

         }

         super.postInvocationHandler(var2, var3, var1);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var2, var3, var10);
      }

      return var1;
   }

   public SQLWarning getWarnings() throws SQLException {
      Transaction var1 = null;
      SQLWarning var2 = null;
      String var3 = "getWarnings";
      Object[] var4 = new Object[0];

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var1 = suspend();
            var2 = this.rmi_conn.getWarnings();
         } finally {
            resume(var1);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

      return var2;
   }

   public void clearWarnings() throws SQLException {
      Transaction var1 = null;
      String var2 = "clearWarnings";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);

         try {
            var1 = suspend();
            this.rmi_conn.clearWarnings();
         } finally {
            resume(var1);
         }

         super.postInvocationHandler(var2, var3, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var2, var3, var9);
      }

   }

   public Map getTypeMap() throws SQLException {
      Transaction var1 = null;
      Map var2 = null;
      String var3 = "getTypeMap";
      Object[] var4 = new Object[0];

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var1 = suspend();
            var2 = this.rmi_conn.getTypeMap();
         } finally {
            resume(var1);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

      return var2;
   }

   public void setTypeMap(Map var1) throws SQLException {
      Transaction var2 = null;
      String var3 = "setTypeMap";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);

         try {
            var2 = suspend();
            this.rmi_conn.setTypeMap(var1);
         } finally {
            resume(var2);
         }

         super.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var3, var4, var10);
      }

   }

   private static Transaction suspend() {
      return TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();
   }

   private static void resume(Transaction var0) {
      TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var0);
   }

   private void closeAndClearAllStatements() {
      synchronized(this.stmts) {
         Iterator var2 = this.stmts.iterator();

         while(var2.hasNext()) {
            SerialStatement var3 = (SerialStatement)var2.next();

            try {
               var3.close(false);
               var2.remove();
            } catch (SQLException var6) {
            }
         }

      }
   }

   public void closeAndClearAllLobs() {
      if (this.lobsets != null && !this.lobsets.isEmpty()) {
         synchronized(this.lobsets) {
            for(Iterator var2 = this.lobsets.iterator(); var2.hasNext(); var2.remove()) {
               Object var3 = var2.next();
               if (var3 instanceof SerialArray) {
                  ((SerialArray)var3).internalClose();
               } else if (var3 instanceof SerialOracleBlob) {
                  ((SerialOracleBlob)var3).internalClose();
               } else if (var3 instanceof SerialOracleClob) {
                  ((SerialOracleClob)var3).internalClose();
               } else if (var3 instanceof SerialSQLXML) {
                  ((SerialSQLXML)var3).internalClose();
               }
            }

         }
      }
   }

   public Statement createStatement(int var1, int var2, int var3) throws SQLException {
      Statement var4 = null;
      String var5 = "createStatement";
      Object[] var6 = new Object[]{new Integer(var1), new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         var4 = this.rmi_conn.createStatement(var1, var2, var3);
         if (var4 != null) {
            var4 = SerialStatement.makeSerialStatement(var4, this);
            this.stmts.add(var4);
         }

         super.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public CallableStatement prepareCall(String var1, int var2, int var3, int var4) throws SQLException {
      CallableStatement var5 = null;
      String var6 = "prepareCall";
      Object[] var7 = new Object[]{var1, new Integer(var2), new Integer(var3), new Integer(var4)};

      try {
         this.preInvocationHandler(var6, var7);
         var5 = this.rmi_conn.prepareCall(var1, var2, var3, var4);
         if (var5 != null) {
            var5 = SerialCallableStatement.makeSerialCallableStatement(var5, this);
            this.stmts.add(var5);
         }

         super.postInvocationHandler(var6, var7, var5);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

      return var5;
   }

   public PreparedStatement prepareStatement(String var1, int var2) throws SQLException {
      PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rmi_conn.prepareStatement(var1, var2);
         if (var3 != null) {
            var3 = SerialPreparedStatement.makeSerialPreparedStatement(var3, this);
            this.stmts.add(var3);
         }

         super.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public PreparedStatement prepareStatement(String var1, int[] var2) throws SQLException {
      PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rmi_conn.prepareStatement(var1, var2);
         if (var3 != null) {
            var3 = SerialPreparedStatement.makeSerialPreparedStatement(var3, this);
            this.stmts.add(var3);
         }

         super.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public PreparedStatement prepareStatement(String var1, int var2, int var3, int var4) throws SQLException {
      PreparedStatement var5 = null;
      String var6 = "prepareStatement";
      Object[] var7 = new Object[]{var1, new Integer(var2), new Integer(var3), new Integer(var4)};

      try {
         this.preInvocationHandler(var6, var7);
         var5 = this.rmi_conn.prepareStatement(var1, var2, var3, var4);
         if (var5 != null) {
            var5 = SerialPreparedStatement.makeSerialPreparedStatement(var5, this);
            this.stmts.add(var5);
         }

         super.postInvocationHandler(var6, var7, var5);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

      return var5;
   }

   public PreparedStatement prepareStatement(String var1, String[] var2) throws SQLException {
      PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rmi_conn.prepareStatement(var1, var2);
         if (var3 != null) {
            var3 = SerialPreparedStatement.makeSerialPreparedStatement(var3, this);
            this.stmts.add(var3);
         }

         super.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Array createArrayOf(String var1, Object[] var2) throws SQLException {
      Array var3 = null;
      String var4 = "createArrayOf";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rmi_conn.createArrayOf(var1, var2);
         if (var3 != null) {
            var3 = SerialArray.makeSerialArrayFromStub(var3);
            this.addToLobSet(var3);
         }

         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Blob createBlob() throws SQLException {
      Blob var1 = null;
      String var2 = "createBlob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rmi_conn.createBlob();
         if (var1 != null) {
            var1 = SerialOracleBlob.makeSerialOracleBlob(var1);
            this.addToLobSet(var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public Clob createClob() throws SQLException {
      Clob var1 = null;
      String var2 = "createClob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rmi_conn.createClob();
         if (var1 != null) {
            var1 = SerialOracleClob.makeSerialOracleClob(var1);
            this.addToLobSet(var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public NClob createNClob() throws SQLException {
      NClob var1 = null;
      String var2 = "createNClob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rmi_conn.createNClob();
         if (var1 != null) {
            var1 = SerialOracleNClob.makeSerialOracleNClob(var1);
            this.addToLobSet(var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public SQLXML createSQLXML() throws SQLException {
      SQLXML var1 = null;
      String var2 = "createSQLXML";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rmi_conn.createSQLXML();
         if (var1 != null) {
            var1 = SerialSQLXML.makeSerialSQLXMLFromStub(var1);
            this.addToLobSet(var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public Struct createStruct(String var1, Object[] var2) throws SQLException {
      Struct var3 = null;
      String var4 = "createStruct";
      Object[] var5 = new Object[0];

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rmi_conn.createStruct(var1, var2);
         if (var3 != null) {
            var3 = SerialStruct.makeSerialStructFromStub(var3);
         }

         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public boolean isValid() throws SQLException {
      return this.isValid(15);
   }

   public boolean isValid(int var1) throws SQLException {
      boolean var2 = false;
      String var3 = "isValid";
      Object[] var4 = new Object[]{new Integer(var1)};
      boolean var5 = false;

      try {
         try {
            this.preInvocationHandler(var3, var4);
            var5 = true;
         } catch (SQLException var8) {
         }

         if (var1 < 0) {
            throw new SQLException("timeout must not be less than 0");
         }

         if (var5 && !this.connectionClosed) {
            try {
               var2 = this.rmi_conn.isValid(var1);
            } catch (Throwable var7) {
            }
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var3, var4, var9);
      }

      return var2;
   }
}
