package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.server.Unreferenced;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Properties;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.ConnectionLeakProfile;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ProfileStorage;
import weblogic.jdbc.rmi.RMIWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rjvm.RJVM;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.server.UnicastRemoteObject;
import weblogic.rmi.spi.EndPoint;
import weblogic.utils.StackTraceUtils;

public class ConnectionImpl extends RMISkelWrapperImpl implements Unreferenced, PeerGoneListener, InteropWriteReplaceable {
   private java.sql.Connection t2_conn = null;
   private RmiDriverSettings rmiSettings = null;
   private boolean isPeerGoneListener = false;
   public static final String CHUNK_SIZE = "weblogic.jdbc.stream_chunk_size";
   public static final String VERBOSE = "weblogic.jdbc.verbose";
   public static final String CACHE_ROWS = "weblogic.jdbc.rmi.cacheRows";
   private Throwable stackTraceSource = null;
   private boolean isClosed = false;
   private String poolName = null;
   private static final boolean DEBUGROUTING = false;
   private transient boolean createdInThisVM = true;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof java.sql.CallableStatement) {
               var3 = CallableStatementImpl.makeCallableStatementImpl((java.sql.CallableStatement)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.PreparedStatement) {
               var3 = PreparedStatementImpl.makePreparedStatementImpl((java.sql.PreparedStatement)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Statement) {
               var3 = StatementImpl.makeStatementImpl((java.sql.Statement)var3, this.rmiSettings);
            } else if (var3 instanceof Savepoint) {
               var3 = SavepointImpl.makeSavepointImpl((Savepoint)var3);
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
            throw var5;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(java.sql.Connection var1, Properties var2, RmiDriverSettings var3, String var4) throws SQLException {
      this.t2_conn = var1;
      this.rmiSettings = new RmiDriverSettings(var3);
      if (var4 != null) {
         this.poolName = var4;
      }

      if (var2 != null) {
         this.setup(var2);
      }

      if (this.rmiSettings.isVerbose()) {
         JdbcDebug.JDBCRMIInternal.debug("rmi settings: " + this.rmiSettings);
      }

   }

   public void finalize() {
      if (!this.isClosed && !this.createdInThisVM) {
         if (this.poolName != null) {
            ConnectionLeakProfile var1 = new ConnectionLeakProfile(this.poolName, StackTraceUtils.throwable2StackTrace(this.stackTraceSource));
            ProfileStorage.storeLeakedConnTrace(var1);
         }

         try {
            if (!this.t2_conn.getAutoCommit()) {
               this.t2_conn.rollback();
            }
         } catch (Exception var2) {
         }

         JdbcDebug.JDBCRMIInternal.debug("Detected Connection Leak!!!!! : " + StackTraceUtils.throwable2StackTrace(this.stackTraceSource));
      }

   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      return this;
   }

   public void peerGone(PeerGoneEvent var1) {
      if (this.rmiSettings.isVerbose()) {
         JdbcDebug.JDBCRMIInternal.debug("Client JVM died: " + var1);
      }

      try {
         if (!this.isClosed && !this.createdInThisVM) {
            if (this.poolName != null) {
               ConnectionLeakProfile var2 = new ConnectionLeakProfile(this.poolName, StackTraceUtils.throwable2StackTrace(this.stackTraceSource));
               ProfileStorage.storeLeakedConnTrace(var2);
            }

            String var6 = StackTraceUtils.throwable2StackTrace(this.stackTraceSource);

            try {
               String var3 = ":";
               var6 = var6.substring(var6.indexOf(var3) + var3.length());
            } catch (Exception var4) {
            }

            JDBCLogger.logConnectionLeakWarning(var6);
            if (!this.t2_conn.getAutoCommit()) {
               this.t2_conn.rollback();
            }
         }

         this.close();
      } catch (SQLException var5) {
      }

      this.t2_conn = null;
   }

   public void addPeerGoneListener() {
      if (!this.isPeerGoneListener) {
         this.isPeerGoneListener = true;
         EndPoint var1 = ServerHelper.getClientEndPointInternal();
         if (var1 != null && var1 instanceof RJVM) {
            ((RJVM)var1).addPeerGoneListener(this);
         }
      }

   }

   private void setup(Properties var1) throws SQLException {
      String var2 = (String)var1.get("weblogic.jdbc.stream_chunk_size");
      String var4;
      if (var2 != null) {
         try {
            this.rmiSettings.setChunkSize(Integer.parseInt(var2));
         } catch (Exception var7) {
            var4 = "The Property weblogic.jdbc.stream_chunk_size must be a number";
            throw new SQLException(var4);
         }
      }

      var2 = (String)var1.get("weblogic.jdbc.rmi.cacheRows");
      if (var2 != null) {
         try {
            this.rmiSettings.setRowCacheSize(Integer.parseInt(var2));
         } catch (Exception var6) {
            var4 = "The Property weblogic.jdbc.rmi.cacheRows must be a number";
            throw new SQLException(var4);
         }
      }

      var2 = (String)var1.get("weblogic.jdbc.verbose");
      if (var2 != null) {
         try {
            this.rmiSettings.setVerbose(Boolean.valueOf(var2));
         } catch (Exception var5) {
            var4 = "The Property weblogic.jdbc.verbose must be a true or false.";
            throw new SQLException(var4);
         }
      }

   }

   public void unreferenced() {
      try {
         if (!this.isClosed && !this.createdInThisVM) {
            if (this.poolName != null) {
               ConnectionLeakProfile var1 = new ConnectionLeakProfile(this.poolName, StackTraceUtils.throwable2StackTrace(this.stackTraceSource));
               ProfileStorage.storeLeakedConnTrace(var1);
            }

            String var11 = StackTraceUtils.throwable2StackTrace(this.stackTraceSource);

            try {
               String var2 = ":";
               var11 = var11.substring(var11.indexOf(var2) + var2.length());
            } catch (Exception var8) {
            }

            JDBCLogger.logConnectionLeakWarning(var11);
         }

         this.close();
      } catch (SQLException var9) {
         JDBCLogger.logStackTrace(var9);
      } finally {
         this.t2_conn = null;
      }

   }

   public java.sql.Statement createStatement() throws SQLException {
      java.sql.Statement var1 = null;
      String var2 = "createStatement";
      Object[] var3 = new Object[0];

      try {
         try {
            this.preInvocationHandler(var2, var3);
            var1 = StatementImpl.makeStatementImpl(this.t2_conn.createStatement(), this.rmiSettings);
            this.postInvocationHandler(var2, var3, var1);
         } catch (Exception var9) {
            this.invocationExceptionHandler(var2, var3, var9);
         }

         return var1;
      } finally {
         ;
      }
   }

   public java.sql.Statement createStatement(int var1, int var2) throws SQLException {
      java.sql.Statement var3 = null;
      String var4 = "createStatement";
      Object[] var5 = new Object[]{new Integer(var1), new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = StatementImpl.makeStatementImpl(this.t2_conn.createStatement(var1, var2), this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public java.sql.PreparedStatement prepareStatement(String var1) throws SQLException {
      java.sql.PreparedStatement var2 = null;
      String var3 = "prepareStatement";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1), this.rmiSettings);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public java.sql.PreparedStatement prepareStatement(String var1, int var2, int var3) throws SQLException {
      java.sql.PreparedStatement var4 = null;
      String var5 = "prepareStatement";
      Object[] var6 = new Object[]{var1, new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         var4 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1, var2, var3), this.rmiSettings);
         this.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public java.sql.CallableStatement prepareCall(String var1) throws SQLException {
      java.sql.CallableStatement var2 = null;
      String var3 = "prepareCall";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = CallableStatementImpl.makeCallableStatementImpl(this.t2_conn.prepareCall(var1), this.rmiSettings);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public java.sql.CallableStatement prepareCall(String var1, int var2, int var3) throws SQLException {
      java.sql.CallableStatement var4 = null;
      String var5 = "prepareCall";
      Object[] var6 = new Object[]{var1, new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         var4 = CallableStatementImpl.makeCallableStatementImpl(this.t2_conn.prepareCall(var1, var2, var3), this.rmiSettings);
         this.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public void rollback(Savepoint var1) throws SQLException {
      this.t2_conn.rollback((Savepoint)((RMIWrapperImpl)var1).getVendorObj());
   }

   public void releaseSavepoint(Savepoint var1) throws SQLException {
      this.t2_conn.releaseSavepoint((Savepoint)((RMIWrapperImpl)var1).getVendorObj());
      ((SavepointImpl)var1).close();
   }

   public void close() throws SQLException {
      String var1 = "close";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         if (this.t2_conn != null) {
            this.t2_conn.close();
            this.t2_conn = null;
            if (this.isPeerGoneListener) {
               this.isPeerGoneListener = false;
               EndPoint var3 = ServerHelper.getClientEndPointInternal();
               if (var3 != null && var3 instanceof RJVM) {
                  ((RJVM)var3).removePeerGoneListener(this);
               }

               try {
                  UnicastRemoteObject.unexportObject(this, true);
               } catch (Exception var5) {
               }
            }
         }

         this.postInvocationHandler(var1, var2, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var1, var2, var6);
      }

   }

   public boolean isClosed() throws SQLException {
      boolean var1 = false;
      String var2 = "isClosed";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         if (this.t2_conn == null) {
            var1 = true;
         } else {
            var1 = this.t2_conn.isClosed();
         }

         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public java.sql.DatabaseMetaData getMetaData() throws SQLException {
      DatabaseMetaDataImpl var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         java.sql.DatabaseMetaData var4 = this.t2_conn.getMetaData();
         var1 = (DatabaseMetaDataImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.DatabaseMetaDataImpl", var4, true);
         var1.init(var4, this.rmiSettings);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.DatabaseMetaData)var1;
   }

   public java.sql.Statement createStatement(int var1, int var2, int var3) throws SQLException {
      java.sql.Statement var4 = null;
      String var5 = "createStatement";
      Object[] var6 = new Object[]{new Integer(var1), new Integer(var2), new Integer(var3)};

      try {
         this.preInvocationHandler(var5, var6);
         var4 = StatementImpl.makeStatementImpl(this.t2_conn.createStatement(var1, var2, var3), this.rmiSettings);
         this.postInvocationHandler(var5, var6, var4);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

      return var4;
   }

   public java.sql.CallableStatement prepareCall(String var1, int var2, int var3, int var4) throws SQLException {
      java.sql.CallableStatement var5 = null;
      String var6 = "prepareCall";
      Object[] var7 = new Object[]{var1, new Integer(var2), new Integer(var3), new Integer(var4)};

      try {
         this.preInvocationHandler(var6, var7);
         var5 = CallableStatementImpl.makeCallableStatementImpl(this.t2_conn.prepareCall(var1, var2, var3, var4), this.rmiSettings);
         this.postInvocationHandler(var6, var7, var5);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

      return var5;
   }

   public java.sql.PreparedStatement prepareStatement(String var1, int var2) throws SQLException {
      java.sql.PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1, var2), this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public java.sql.PreparedStatement prepareStatement(String var1, int[] var2) throws SQLException {
      java.sql.PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1, var2), this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public java.sql.PreparedStatement prepareStatement(String var1, int var2, int var3, int var4) throws SQLException {
      java.sql.PreparedStatement var5 = null;
      String var6 = "prepareStatement";
      Object[] var7 = new Object[]{var1, new Integer(var2), new Integer(var3), new Integer(var4)};

      try {
         this.preInvocationHandler(var6, var7);
         var5 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1, var2, var3, var4), this.rmiSettings);
         this.postInvocationHandler(var6, var7, var5);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

      return var5;
   }

   public java.sql.PreparedStatement prepareStatement(String var1, String[] var2) throws SQLException {
      java.sql.PreparedStatement var3 = null;
      String var4 = "prepareStatement";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = PreparedStatementImpl.makePreparedStatementImpl(this.t2_conn.prepareStatement(var1, var2), this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public java.sql.Array createArrayOf(String var1, Object[] var2) throws SQLException {
      ArrayImpl var3 = null;
      String var4 = "createArrayOf";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         java.sql.Array var6 = this.t2_conn.createArrayOf(var1, var2);
         var3 = (ArrayImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ArrayImpl", var6, true);
         var3.init(var6, this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return (java.sql.Array)var3;
   }

   public java.sql.Blob createBlob() throws SQLException {
      OracleTBlobImpl var1 = null;
      String var2 = "createBlob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         java.sql.Blob var4 = this.t2_conn.createBlob();
         var1 = (OracleTBlobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTBlobImpl", var4, true);
         var1.init(var4, this.rmiSettings);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.Blob)var1;
   }

   public java.sql.Clob createClob() throws SQLException {
      OracleTClobImpl var1 = null;
      String var2 = "createClob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         java.sql.Clob var4 = this.t2_conn.createClob();
         var1 = (OracleTClobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTClobImpl", var4, true);
         var1.init(var4, this.rmiSettings);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.Clob)var1;
   }

   public NClob createNClob() throws SQLException {
      OracleTNClobImpl var1 = null;
      String var2 = "createNClob";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         NClob var4 = this.t2_conn.createNClob();
         var1 = (OracleTNClobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTNClobImpl", var4, true);
         var1.init(var4, this.rmiSettings);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (NClob)var1;
   }

   public java.sql.SQLXML createSQLXML() throws SQLException {
      SQLXMLImpl var1 = null;
      String var2 = "createSQLXML";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         java.sql.SQLXML var4 = this.t2_conn.createSQLXML();
         var1 = (SQLXMLImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.SQLXMLImpl", var4, true);
         var1.init(var4, this.rmiSettings);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.SQLXML)var1;
   }

   public java.sql.Struct createStruct(String var1, Object[] var2) throws SQLException {
      StructImpl var3 = null;
      String var4 = "createStruct";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         java.sql.Struct var6 = this.t2_conn.createStruct(var1, var2);
         var3 = (StructImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.StructImpl", var6, true);
         var3.init(var6, this.rmiSettings);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return (java.sql.Struct)var3;
   }

   public boolean isValid(int var1) throws SQLException {
      boolean var2 = false;
      String var3 = "isValid";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.t2_conn.isValid(var1);
         this.postInvocationHandler(var3, var4, new Boolean(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }
}
