package weblogic.common;

import weblogic.common.internal.LogMsg;
import weblogic.utils.StackTraceUtils;

final class LogServicesImpl implements LogServicesDef {
   T3Client t3;

   public LogServicesImpl(T3Client var1) {
      this.t3 = var1;
   }

   /** @deprecated */
   public synchronized void log(String var1, Throwable var2) throws T3Exception {
      this.error(var1, var2);
   }

   /** @deprecated */
   public synchronized void log(String var1) throws T3Exception {
      this.info(var1);
   }

   public synchronized void info(String var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)0, var1));
   }

   public synchronized void info(String var1, Throwable var2) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)0, var1, StackTraceUtils.throwable2StackTrace(var2)));
   }

   public synchronized void error(String var1, Throwable var2) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)1, var1, StackTraceUtils.throwable2StackTrace(var2)));
   }

   public synchronized void error(Throwable var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)1, (String)null, StackTraceUtils.throwable2StackTrace(var1)));
   }

   public synchronized void error(String var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)1, var1));
   }

   public synchronized void error(String var1, String var2) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)1, var1, var2));
   }

   public synchronized void warning(String var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)2, var1));
   }

   public synchronized void security(String var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)3, var1));
   }

   public synchronized void debug(String var1) throws T3Exception {
      this.t3.sendOneWay("weblogic.common.internal.LogProxyLazy", new LogMsg((byte)4, var1));
   }
}
