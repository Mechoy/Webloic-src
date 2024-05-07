package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialStatement extends RMIStubWrapperImpl implements RmiStatement, Serializable {
   private static final long serialVersionUID = -1944649289013384795L;
   private Statement rmi_stmt = null;
   private transient SerialConnection parent_conn = null;
   protected transient Set<SerialResultSet> rsets = Collections.synchronizedSet(new HashSet());
   private transient boolean isClosed = false;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof ResultSet) {
               var3 = SerialResultSet.makeSerialResultSet((ResultSet)var3, this);
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
            throw var5;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void preInvocationHandler(String var1, Object[] var2) throws Exception {
      super.preInvocationHandler(var1, var2);
      if (!var1.equals("isClosed")) {
         this.checkClosed();
      }

   }

   public void init(Statement var1, SerialConnection var2) {
      this.rmi_stmt = var1;
      this.parent_conn = var2;
      this.isClosed = false;
   }

   public static Statement makeSerialStatement(Statement var0, SerialConnection var1) {
      if (var0 == null) {
         return null;
      } else {
         SerialStatement var2 = (SerialStatement)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialStatement", var0, false);
         var2.init(var0, var1);
         return (Statement)var2;
      }
   }

   void addResultSet(SerialResultSet var1) {
      this.rsets.add(var1);
   }

   void removeResultSet(SerialResultSet var1) {
      this.rsets.remove(var1);
   }

   public int getRmiFetchSize() throws SQLException {
      try {
         return ((RmiStatement)this.rmi_stmt).getRmiFetchSize();
      } catch (Exception var2) {
         if (var2 instanceof SQLException) {
            throw (SQLException)var2;
         } else {
            throw new SQLException(var2.toString());
         }
      }
   }

   public void setRmiFetchSize(int var1) throws SQLException {
      try {
         ((RmiStatement)this.rmi_stmt).setRmiFetchSize(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public void close() throws SQLException {
      this.close(true);
   }

   void close(boolean var1) throws SQLException {
      if (!this.isClosed) {
         try {
            this.closeAndClearAllResultSets();
            if (var1) {
               this.parent_conn.removeStatement(this);
            }

            this.rmi_stmt.close();
         } catch (Exception var7) {
            if (var7 instanceof SQLException) {
               throw (SQLException)var7;
            }

            throw new SQLException(var7.toString());
         } finally {
            this.isClosed = true;
         }

      }
   }

   private void checkClosed() throws SQLException {
      if (this.isClosed) {
         throw new SQLException("Statement is closed.");
      }
   }

   public Connection getConnection() throws SQLException {
      String var1 = "getConnection";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, this.parent_conn);
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

      return (Connection)this.parent_conn;
   }

   private void closeAndClearAllResultSets() {
      synchronized(this.rsets) {
         Iterator var2 = this.rsets.iterator();

         while(var2.hasNext()) {
            SerialResultSet var3 = (SerialResultSet)var2.next();

            try {
               var3.close(false);
               var2.remove();
            } catch (SQLException var6) {
            }
         }

      }
   }

   public boolean isClosed() throws SQLException {
      String var1 = "isClosed";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         super.postInvocationHandler(var1, var2, new Boolean(this.isClosed));
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

      return this.isClosed;
   }
}
