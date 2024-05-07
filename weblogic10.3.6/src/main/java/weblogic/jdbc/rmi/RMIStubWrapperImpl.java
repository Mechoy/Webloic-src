package weblogic.jdbc.rmi;

import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.jdbc.common.internal.JDBCHelper;
import weblogic.jdbc.common.internal.JdbcDebug;

public class RMIStubWrapperImpl extends RMIWrapperImpl {
   public Object invocationExceptionHandler(String var1, Object[] var2, Throwable var3) throws SQLException {
      if (!(this.vendorObj instanceof Remote)) {
         throw new AssertionError("object not Remote : " + this.vendorObj);
      } else {
         try {
            JDBCHelper.getHelper().removeRMIContext(this.vendorObj);
         } catch (Exception var5) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("error removing RMI context", var5);
            }

            throw new SQLException(var5);
         }

         return super.invocationExceptionHandler(var1, var2, var3);
      }
   }

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (!(this.vendorObj instanceof Remote)) {
         throw new AssertionError("object not Remote : " + this.vendorObj);
      } else {
         try {
            JDBCHelper.getHelper().removeRMIContext(this.vendorObj);
         } catch (Exception var5) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("error removing RMI context", var5);
            }

            throw var5;
         }

         return super.postInvocationHandler(var1, var2, var3);
      }
   }

   public void preInvocationHandler(String var1, Object[] var2) throws Exception {
      super.preInvocationHandler(var1, var2);
      if (!(this.vendorObj instanceof Remote)) {
         throw new AssertionError("object not Remote : " + this.vendorObj);
      } else {
         try {
            JDBCHelper.getHelper().addRMIContext(this.vendorObj);
         } catch (Exception var4) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("error adding RMI context", var4);
            }

            throw var4;
         }
      }
   }
}
