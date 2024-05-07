package weblogic.jdbc.rmi;

import java.sql.SQLException;
import weblogic.common.resourcepool.ResourceUnusableException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.jdbc.common.internal.ConnectionEnv;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.extensions.PoolUnavailableSQLException;
import weblogic.jdbc.wrapper.JDBCWrapperImpl;
import weblogic.utils.StackTraceUtils;

public class RMIWrapperImpl extends JDBCWrapperImpl {
   static final long serialVersionUID = -6070228019593000541L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.jdbc.rmi.RMIWrapperImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public void preInvocationHandler(String var1, Object[] var2) throws Exception {
      if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
         String var3 = var1 + "(";
         if (var2 != null && var2.length > 0) {
            for(int var4 = 0; var4 < var2.length - 1; ++var4) {
               var3 = var3 + var2[var4] + ", ";
            }

            var3 = var3 + var2[var2.length - 1] + ")";
         } else {
            var3 = var3 + ")";
         }

         this.trace(var3);
      }

   }

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
         String var4 = var1 + " returns";
         if (var3 != null) {
            var4 = var4 + " " + var3;
         }

         this.trace(var4);
      }

      return var3;
   }

   public void trace(String var1) {
      JdbcDebug.JDBCRMI.debug("[" + this + "] " + var1);
   }

   public Object invocationExceptionHandler(String var1, Object[] var2, Throwable var3) throws SQLException {
      if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
         String var4 = var1 + "(";
         if (var2 != null && var2.length > 0) {
            for(int var5 = 0; var5 < var2.length - 1; ++var5) {
               var4 = var4 + var2[var5] + ", ";
            }

            var4 = var4 + var2[var2.length - 1] + ") throws ";
         } else {
            var4 = var4 + "unknown) throws: ";
         }

         var4 = var4 + StackTraceUtils.throwable2StackTrace(var3);
         this.trace(var4);
      }

      if (var3 instanceof ResourceUnusableException) {
         throw new PoolUnavailableSQLException(var3.getMessage());
      } else if (var3 instanceof SQLException) {
         throw (SQLException)var3;
      } else if (var3 instanceof SecurityException) {
         throw (SecurityException)var3;
      } else {
         SQLException var6 = new SQLException(var1 + ", Exception = " + var3.getMessage());
         var6.initCause(var3);
         throw var6;
      }
   }

   public ConnectionEnv getConnectionEnv() {
      return null;
   }

   public <T> T unwrap(Class<T> var1) throws SQLException {
      boolean var9 = false;

      Object var10000;
      DynamicJoinPoint var10001;
      DelegatingMonitor var10002;
      Object var4;
      try {
         var9 = true;
         if (!var1.isInstance(this)) {
            throw new SQLException(this + " is not an instance of " + var1);
         }

         var10000 = var1.cast(this);
         var9 = false;
      } finally {
         if (var9) {
            var4 = null;
            if (_WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium.isEnabledAndNotDyeFiltered()) {
               var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
               var10002 = _WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium;
               InstrumentationSupport.process(var10001, var10002, var10002.getActions());
            }

         }
      }

      var4 = var10000;
      if (_WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium.isEnabledAndNotDyeFiltered()) {
         var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
         var10002 = _WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium;
         InstrumentationSupport.process(var10001, var10002, var10002.getActions());
      }

      return var10000;
   }

   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return var1.isInstance(this);
   }

   static {
      _WLDF$INST_FLD_JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "RMIWrapperImpl.java", "weblogic.jdbc.rmi.RMIWrapperImpl", "unwrap", "(Ljava/lang/Class;)Ljava/lang/Object;", 112, InstrumentationSupport.makeMap(new String[]{"JDBC_Diagnostic_Connection_Get_Vendor_Connection_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JDBCConnectionRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null)}), (boolean)0);
   }
}
