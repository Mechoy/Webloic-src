package weblogic.servlet.internal.session;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;

public class GracefulShutdownHelper {
   private static int maxPendingSessionTimeout;
   private static final long PENDING_SESSION_INTERVAL = 30000L;
   private static final String PRODUCTION_TO_ADMIN_LOCK_PREFIX = "weblogic.webapp.ProductionToAdminLock";

   public static void waitForPendingSessions() {
      Map var0 = getActiveSessions();
      if (var0.size() > 0) {
         String[] var1 = getPendingSessionsMessage(var0);
         int var2 = getPendingSessionTimeout(maxPendingSessionTimeout);
         HTTPLogger.logSessionListDuringSuspend(var1[0], var1[1]);
         HTTPLogger.logInitialSessionsDuringSuspend(var2);
         startSessionChecker(30000L);
      }

      HTTPLogger.logPrepareToSuspendComplete();
   }

   private static Map getActiveSessions() {
      HashMap var0 = new HashMap();
      Iterator var1 = WebService.getHttpServers().iterator();

      while(true) {
         WebAppServletContext[] var3;
         do {
            if (!var1.hasNext()) {
               return var0;
            }

            HttpServer var2 = (HttpServer)var1.next();
            var3 = var2.getServletContextManager().getAllContexts();
         } while(var3 == null);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            WebAppServletContext var5 = var3[var4];
            maxPendingSessionTimeout = updateActiveSessionTable(var5, var0, maxPendingSessionTimeout, false);
         }
      }
   }

   private static int getPendingSessionTimeout(int var0) {
      int var1 = var0;
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (ManagementService.getRuntimeAccess(var2).getServer().getGracefulShutdownTimeout() > 0) {
         var1 = Math.min(ManagementService.getRuntimeAccess(var2).getServer().getGracefulShutdownTimeout(), var0);
      }

      return var1;
   }

   private static int updateActiveSessionTable(WebAppServletContext var0, Map var1, int var2, boolean var3) {
      if (var0.isInternalApp()) {
         return var2;
      } else {
         SessionContext var4 = var0.getSessionContext();
         int var5 = var3 ? var4.getCurrOpenSessionsCount() : var4.getNonPersistedSessionCount();
         AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         if (hasWSEEServlets(var0) && var0.getVersionId() != null && var3) {
            String var7 = var0.getApplicationName();
            ApplicationRuntimeMBean var8 = ManagementService.getRuntimeAccess(var6).getServerRuntime().lookupApplicationRuntime(var7);
            if (var8 != null) {
               ComponentRuntimeMBean[] var9 = var8.getComponentRuntimes();
               ComponentRuntimeMBean[] var10 = var9;
               int var11 = var9.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  ComponentRuntimeMBean var13 = var10[var12];
                  WseeV2RuntimeMBean[] var14 = null;
                  if (var13 instanceof WebAppComponentRuntimeMBean) {
                     var14 = ((WebAppComponentRuntimeMBean)var13).getWseeV2Runtimes();
                  } else if (var13 instanceof EJBComponentRuntimeMBean) {
                     var14 = ((EJBComponentRuntimeMBean)var13).getWseeV2Runtimes();
                  }

                  if (var14 != null) {
                     for(int var15 = 0; var15 < var14.length; ++var15) {
                        var5 = (int)((long)var5 + var14[var15].getConversationInstanceCount());
                     }
                  }
               }
            }
         }

         if (var5 > 0) {
            var1.put(var0.getContextPath(), new Long((long)var5));
            return Math.max(var2, var4.getConfigMgr().getSessionTimeoutSecs());
         } else {
            return var2;
         }
      }
   }

   private static String[] getPendingSessionsMessage(Map var0) {
      StringBuffer var1 = new StringBuffer();
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         var1.append(var4.getKey());
         var2.append(var4.getValue());
         if (var3.hasNext()) {
            var1.append(", ");
            var2.append(", ");
         }
      }

      return new String[]{var1.toString(), var2.toString()};
   }

   private static void startSessionChecker(long var0) {
      if (var0 > 0L) {
         SessionChecker var2 = new SessionChecker(var0);
         var2.start();
      }
   }

   public static void waitForPendingSessions(String var0, WebAppModule var1, boolean var2) {
      if (!var2) {
         HashMap var3 = new HashMap();
         int var4 = getActiveSessions(var1, var3);
         String var5 = ApplicationVersionUtils.getDisplayName(var0);
         if (var3.size() > 0) {
            String[] var6 = getPendingSessionsMessage(var3);
            int var7 = getPendingSessionTimeout(var4);
            HTTPLogger.logSessionListDuringGracefulProductionToAdmin(var5, var1.getName(), var6[0], var6[1]);
            HTTPLogger.logInitialSessionsDuringGracefulProductionToAdmin(var5, var1.getName(), var7);
            startSessionChecker(30000L, var0, var1);
         } else {
            HTTPLogger.logGracefulProductionToAdminComplete(var5, var1.getName());
         }

      }
   }

   private static int getActiveSessions(WebAppModule var0, Map var1) {
      int var2 = 0;
      Iterator var3 = var0.getAllContexts();

      while(var3.hasNext()) {
         WebAppServletContext var4 = (WebAppServletContext)var3.next();
         if (var4 != null) {
            var2 = updateActiveSessionTable(var4, var1, var2, true);
         }
      }

      return var2;
   }

   private static void startSessionChecker(long var0, String var2, WebAppModule var3) {
      if (var0 > 0L) {
         SessionChecker var4 = new SessionChecker(var0, var2, var3);
         var4.start();
      }
   }

   public static void notifyGracefulProductionToAdmin(String var0, WebAppModule var1) {
      Object var2 = getProductionToAdminLock(var0, var1);
      synchronized(var2) {
         var2.notify();
      }
   }

   private static boolean hasWSEEServlets(WebAppServletContext var0) {
      ServletRuntimeMBean[] var1 = var0.getServletRuntimeMBeans();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ServletRuntimeMBean var3 = var1[var2];
         if (var3.getServletClassName().equals("weblogic.wsee.server.servlet.WebappWSServlet") || var3.getServletClassName().equals("weblogic.wsee.server.servlet.EjbWSServlet")) {
            return true;
         }
      }

      return false;
   }

   private static Object getProductionToAdminLock(String var0, WebAppModule var1) {
      return var1 == null ? "weblogic.webapp.ProductionToAdminLock" : ("weblogic.webapp.ProductionToAdminLock." + var0 + "." + var1.getName()).intern();
   }

   private static final class SessionChecker {
      private final long period;
      private long duration;
      private long nextLogInterval;
      private String appId;
      private WebAppModule webAppModule;

      SessionChecker(long var1) {
         this.period = var1;
      }

      SessionChecker(long var1, String var3, WebAppModule var4) {
         this.period = var1;
         this.appId = var3;
         this.webAppModule = var4;
      }

      private final void start() {
         while(true) {
            Map var1;
            if ((var1 = this.getActiveSessions()).size() > 0 && (this.webAppModule == null || !this.webAppModule.isSuspended())) {
               if (this.shouldLog()) {
                  String[] var2 = GracefulShutdownHelper.getPendingSessionsMessage(var1);
                  this.logSessionsDuringSuspend(this.duration, var2[0]);
               }

               this.duration += this.period / 1000L;
               Object var8 = GracefulShutdownHelper.getProductionToAdminLock(this.appId, this.webAppModule);
               synchronized(var8) {
                  try {
                     var8.wait(this.period);
                     continue;
                  } catch (InterruptedException var6) {
                  }
               }
            }

            this.logPrepareToSuspendComplete();
            return;
         }
      }

      private Map getActiveSessions() {
         if (this.webAppModule == null) {
            return GracefulShutdownHelper.getActiveSessions();
         } else {
            HashMap var1 = new HashMap();
            GracefulShutdownHelper.getActiveSessions(this.webAppModule, var1);
            return var1;
         }
      }

      private boolean shouldLog() {
         if (this.nextLogInterval > 0L && this.duration < this.nextLogInterval) {
            return false;
         } else {
            this.nextLogInterval = this.duration * 2L;
            return true;
         }
      }

      private void logSessionsDuringSuspend(long var1, String var3) {
         int var4 = (int)(var1 / 60L);
         if (this.webAppModule == null) {
            HTTPLogger.logSessionsDuringSuspend(var4, var3);
         } else {
            HTTPLogger.logSessionsDuringGracefulProductionToAdmin(ApplicationVersionUtils.getDisplayName(this.appId), this.webAppModule.getName(), var4, var3);
         }

      }

      private void logPrepareToSuspendComplete() {
         if (this.webAppModule == null) {
            HTTPLogger.logPrepareToSuspendComplete();
         } else if (this.webAppModule.isSuspended()) {
            HTTPLogger.logGracefulProductionToAdminInterrupted(ApplicationVersionUtils.getDisplayName(this.appId), this.webAppModule.getName());
         } else {
            HTTPLogger.logGracefulProductionToAdminComplete(ApplicationVersionUtils.getDisplayName(this.appId), this.webAppModule.getName());
         }

      }
   }
}
