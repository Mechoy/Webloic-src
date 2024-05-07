package weblogic.jdbc.rmi.internal;

import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class DatabaseMetaDataImpl extends RMISkelWrapperImpl {
   private java.sql.DatabaseMetaData t2_dbmd = null;
   private RmiDriverSettings rmiSettings = null;

   public void init(java.sql.DatabaseMetaData var1, RmiDriverSettings var2) {
      this.t2_dbmd = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof java.sql.ResultSet) {
               java.sql.ResultSet var4 = (java.sql.ResultSet)var3;
               ResultSetImpl var5 = (ResultSetImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ResultSetImpl", var4, true);
               var5.init(var4, this.rmiSettings);
               var3 = (java.sql.ResultSet)var5;
            }
         } catch (Exception var6) {
            JDBCLogger.logStackTrace(var6);
            throw var6;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }
}
