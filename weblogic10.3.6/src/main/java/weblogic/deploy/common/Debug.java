package weblogic.deploy.common;

import weblogic.diagnostics.debug.DebugLogger;

public class Debug {
   private static boolean ADD_THREAD_NAME = false;
   public static final DebugLogger serviceLogger = DebugLogger.getDebugLogger("DebugDeploymentServiceInternal");
   public static final DebugLogger serviceStatusLogger = DebugLogger.getDebugLogger("DebugDeploymentServiceStatusUpdates");
   public static final DebugLogger serviceTransportLogger = DebugLogger.getDebugLogger("DebugDeploymentServiceTransport");
   public static final DebugLogger serviceHttpLogger = DebugLogger.getDebugLogger("DebugDeploymentServiceTransportHttp");
   public static final DebugLogger deploymentLogger = DebugLogger.getDebugLogger("DebugDeployment");

   public static final void serviceDebug(String var0) {
      serviceLogger.debug(addThreadToMessage(var0));
   }

   public static final boolean isServiceDebugEnabled() {
      return serviceLogger.isDebugEnabled();
   }

   public static final void serviceStatusDebug(String var0) {
      serviceStatusLogger.debug(addThreadToMessage(var0));
   }

   public static final boolean isServiceStatusDebugEnabled() {
      return serviceStatusLogger.isDebugEnabled();
   }

   public static final void serviceTransportDebug(String var0) {
      serviceTransportLogger.debug(addThreadToMessage(var0));
   }

   public static final boolean isServiceTransportDebugEnabled() {
      return serviceTransportLogger.isDebugEnabled();
   }

   public static final void serviceHttpDebug(String var0) {
      serviceHttpLogger.debug(addThreadToMessage(var0));
   }

   public static final boolean isServiceHttpDebugEnabled() {
      return serviceHttpLogger.isDebugEnabled();
   }

   public static final void deploymentDebug(String var0) {
      deploymentLogger.debug(addThreadToMessage(var0));
   }

   public static final void deploymentDebug(String var0, Throwable var1) {
      deploymentLogger.debug(addThreadToMessage(var0), var1);
   }

   public static final boolean isDeploymentDebugEnabled() {
      return deploymentLogger.isDebugEnabled();
   }

   private static final String addThreadToMessage(String var0) {
      return ADD_THREAD_NAME ? "<" + Thread.currentThread().getName() + "> " + var0 : var0;
   }
}
