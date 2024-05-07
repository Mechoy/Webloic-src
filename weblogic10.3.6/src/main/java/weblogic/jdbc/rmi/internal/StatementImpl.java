package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.jdbc.wrapper.JDBCWrapperImpl;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class StatementImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private java.sql.Statement t2_stmt = null;
   RmiDriverSettings rmiSettings = null;
   private java.sql.ResultSet curr_remote_rs = null;
   private weblogic.jdbc.wrapper.ResultSet curr_rs = null;
   transient Object interop = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof java.sql.ResultSet) {
               if (this.curr_rs != null && !this.curr_rs.internalIsClosed() && this.curr_remote_rs != null) {
                  try {
                     if (((JDBCWrapperImpl)this.curr_remote_rs).getVendorObj() == ((JDBCWrapperImpl)var3).getVendorObj()) {
                        super.postInvocationHandler(var1, var2, this.curr_remote_rs);
                        return this.curr_remote_rs;
                     }
                  } catch (Exception var5) {
                  }
               }

               if (var3 instanceof weblogic.jdbc.wrapper.ResultSet) {
                  this.curr_rs = (weblogic.jdbc.wrapper.ResultSet)var3;
               } else {
                  this.curr_rs = null;
               }

               this.curr_remote_rs = ResultSetImpl.makeResultSetImpl((java.sql.ResultSet)var3, this.rmiSettings);
               var3 = this.curr_remote_rs;
            } else if (var3 instanceof java.sql.Blob) {
               var3 = OracleTBlobImpl.makeOracleTBlobImpl((java.sql.Blob)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Clob) {
               var3 = OracleTClobImpl.makeOracleTClobImpl((java.sql.Clob)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Struct) {
               var3 = StructImpl.makeStructImpl((java.sql.Struct)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Ref) {
               var3 = RefImpl.makeRefImpl((java.sql.Ref)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Array) {
               var3 = ArrayImpl.makeArrayImpl((java.sql.Array)var3, this.rmiSettings);
            }
         } catch (Exception var6) {
            JDBCLogger.logStackTrace(var6);
            throw var6;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public StatementImpl() {
      this.rmiSettings = new RmiDriverSettings();
   }

   public StatementImpl(java.sql.Statement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(java.sql.Statement var1, RmiDriverSettings var2) {
      this.t2_stmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public java.sql.Statement getImplDelegate() {
      return this.t2_stmt;
   }

   public static java.sql.Statement makeStatementImpl(java.sql.Statement var0, RmiDriverSettings var1) {
      if (var0 == null) {
         return null;
      } else {
         StatementImpl var2 = (StatementImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.StatementImpl", var0, true);
         var2.init(var0, var1);
         return (java.sql.Statement)var2;
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new StatementStub((Statement)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public int getRmiFetchSize() throws SQLException {
      return this.rmiSettings.getRowCacheSize();
   }

   public void setRmiFetchSize(int var1) throws SQLException {
      this.rmiSettings.setRowCacheSize(var1);
   }

   public void close() throws SQLException {
      this.t2_stmt.close();

      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

   }
}
