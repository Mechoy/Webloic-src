package weblogic.management.deploy.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.common.Debug;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public class RetirementManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int RETIREMENT_SCHEDULED = 1;
   private static final int RETIREMENT_STARTED = 2;
   private static final int RETIREMENT_CANCELLED = 3;
   private static final String RETIRE_TASK_ID_PREFIX = "weblogic.retire.";
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();
   private static final Hashtable retireInfoMap = new Hashtable();
   private static HashMap retireOnRestartApps = null;
   private static int curTaskId = 0;
   private static Timer retireTimer = null;
   private static final int MAX_RETIRE_WAIT_TIME_SECS = 20;

   public static void retire(AppDeploymentMBean var0, DeploymentData var1) throws ManagementException {
      if (var0 != null) {
         String var2 = var0.getApplicationIdentifier();
         if (retireInfoMap.get(var2) == null) {
            int var3 = getRetireTimeoutSecs(var1);
            boolean var4 = getIgnoreSessions(var1);
            int var5 = getRMIGracePeriodSecs(var1);
            long var6 = System.currentTimeMillis();
            if (var3 > 0) {
               var6 += (long)(var3 * 1000);
            }

            appRTStateMgr.setRetireTimeoutSeconds(var2, var3);
            appRTStateMgr.setRetireTimeMillis(var2, var6);
            if (var3 == -1) {
               DeployerRuntimeLogger.logRetireGracefully(ApplicationVersionUtils.getDisplayName(var2));
               stopForGracefulRetire(var2, var4, var5);
            } else {
               scheduleRetire(var2, var3, var6, var4);
            }

         }
      }
   }

   public static void retireAppsOnStartup() {
      ArrayList var0 = getToBeRetiredApps();
      if (var0 != null && var0.size() != 0) {
         ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         if (var1 != null) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Registering " + var0.size() + " app(s) for retirement");
            }

            HashMap var2 = new HashMap(var0.size());

            for(int var3 = 0; var3 < var0.size(); ++var3) {
               var2.put(((AppDeploymentMBean)var0.get(var3)).getApplicationIdentifier(), new Integer(1));
            }

            retireOnRestartApps = var2;
            var1.addPropertyChangeListener(new RetireOnRestartListener(var0));
         } else if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Cannot register apps for retirement");
         }

      }
   }

   private static ArrayList getToBeRetiredApps() {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
      if (!var0.isAdminServer()) {
         return null;
      } else {
         AppDeploymentMBean[] var1 = AppDeploymentHelper.getAppsAndLibs(var0.getDomain());
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            AppDeploymentMBean var4 = var1[var3];
            if (var4 != null && var4.getVersionIdentifier() != null && !(var4 instanceof LibraryMBean) && appRTStateMgr.getCurrentState(var4) != null && !appRTStateMgr.isActiveVersion(var4) && !appRTStateMgr.isRetiredVersion(var4) && !appRTStateMgr.isFailedVersion(var4)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   private static void retireOnRestart(ArrayList var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("RetireOnRestart " + var0.size() + " app(s)");
      }

      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         retireOnRestart((AppDeploymentMBean)var1.next());
         var1.remove();
      }

      synchronized(retireInfoMap) {
         retireOnRestartApps = null;
      }
   }

   private static void retireOnRestart(AppDeploymentMBean var0) {
      if (var0 != null) {
         String var1 = var0.getApplicationIdentifier();
         int var2 = appRTStateMgr.getRetireTimeoutSeconds(var1);
         long var3 = appRTStateMgr.getRetireTimeMillis(var1);
         long var5 = System.currentTimeMillis();
         if (var3 > 0L) {
            if (var3 <= var5) {
               if (var2 == -1) {
                  DeployerRuntimeLogger.logRetireGracefully(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0));
                  retireNow(var1, true, true);
               } else {
                  DeployerRuntimeLogger.logRetireNow(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0));
                  retireNow(var1, false, true);
               }
            } else {
               scheduleRetire(var1, (int)((var3 - var5) / 1000L), var3, true);
            }

         }
      }
   }

   private static void stopForGracefulRetire(String var0, boolean var1, int var2) {
      String var3 = getRetireTaskId(var0);
      RetireInfo var4 = new RetireInfo(2, var0, var3, true, var1, var2, System.currentTimeMillis());
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("RM.putRetireInfo for stop, app=" + var4.appId + ", taskId=" + var4.taskId);
      }

      retireInfoMap.put(var0, var4);
      stopForGracefulRetire(var4);
   }

   private static void retireNow(String var0, boolean var1, boolean var2) {
      String var3 = getRetireTaskId(var0);
      RetireInfo var4 = new RetireInfo(2, var0, var3, var1, var2, -1, System.currentTimeMillis());
      synchronized(retireInfoMap) {
         if (retireOnRestartApps != null && retireOnRestartApps.containsKey(var0) && (Integer)retireOnRestartApps.remove(var0) == 3) {
            return;
         }

         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("RM.putRetireInfo for retireNow, app=" + var4.appId + ", taskId=" + var4.taskId);
         }

         retireInfoMap.put(var0, var4);
      }

      retire(var4);
   }

   private static void scheduleRetire(String var0, int var1, long var2, boolean var4) {
      DeployerRuntimeLogger.logRetireTimeout(ApplicationVersionUtils.getDisplayName(var0), var1);
      String var5 = getRetireTaskId(var0);
      RetireInfo var6 = new RetireInfo(1, var0, var5, false, var4, -1, var2);
      synchronized(retireInfoMap) {
         if (retireOnRestartApps != null && retireOnRestartApps.containsKey(var0) && (Integer)retireOnRestartApps.remove(var0) == 3) {
            return;
         }

         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("RM.putRetireInfo for scheduleRetire, app=" + var6.appId + ", taskId=" + var6.taskId);
         }

         retireInfoMap.put(var0, var6);
      }

      startTimerIfNeeded();
   }

   public static boolean cancelIfNeeded(String var0, String var1) throws ManagementException {
      String var2 = ApplicationVersionUtils.getApplicationId(var0, var1);
      RetireInfo var3;
      synchronized(retireInfoMap) {
         if (retireOnRestartApps != null && retireOnRestartApps.containsKey(var2)) {
            retireOnRestartApps.put(var2, new Integer(3));
            return true;
         }

         var3 = (RetireInfo)retireInfoMap.get(var2);
      }

      if (var3 == null) {
         return true;
      } else {
         boolean var4 = false;
         synchronized(var3) {
            if (var3.state == 1) {
               var3.state = 3;
               var4 = true;
            } else if (var3.state == 2) {
               return false;
            }
         }

         if (var4) {
            retireInfoMap.remove(var2);
            DeployerRuntimeLogger.logRetirementCancelled(ApplicationVersionUtils.getDisplayName(var0, var1), (var3.retireTimeMillis - System.currentTimeMillis()) / 1000L);
         }

         return true;
      }
   }

   public static boolean isRetirementInProgress(String var0, String var1) {
      String var2 = ApplicationVersionUtils.getApplicationId(var0, var1);
      synchronized(retireInfoMap) {
         return retireInfoMap.get(var2) != null || retireOnRestartApps != null && retireOnRestartApps.containsKey(var2);
      }
   }

   public static void waitForRetirementCompleteIfNeeded(String var0, String var1) {
      String var2 = ApplicationVersionUtils.getApplicationId(var0, var1);
      RetireInfo var3 = (RetireInfo)retireInfoMap.get(var2);
      if (var3 != null) {
         synchronized(var3) {
            try {
               if (!var3.notificationDone()) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("RM.waitForRetirementComplete for, app=" + var3.appId + ", taskId=" + var3.taskId);
                  }

                  var3.wait(20000L);
               }
            } catch (InterruptedException var7) {
            }
         }
      }

   }

   private static void retireExpiredApps() {
      HashMap var0;
      synchronized(retireInfoMap) {
         var0 = new HashMap(retireInfoMap);
      }

      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.values().iterator();

      while(var2.hasNext()) {
         RetireInfo var3 = (RetireInfo)var2.next();
         long var4 = System.currentTimeMillis();
         synchronized(var3) {
            if (var3.state != 1 || var3.retireTimeMillis > var4) {
               continue;
            }

            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Retirement timer expired for app=" + var3.appId);
            }

            var3.state = 2;
         }

         var1.add(var3);
      }

      initiateRetire(var1);
   }

   private static void initiateRetire(final ArrayList var0) {
      WorkManagerFactory.getInstance().getDefault().schedule(new Runnable() {
         public void run() {
            Iterator var1 = var0.iterator();

            while(var1.hasNext()) {
               RetirementManager.retire((RetireInfo)var1.next());
            }

         }
      });
   }

   private static void retire(RetireInfo var0) {
      AppDeploymentMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupAppDeployment(var0.appId);
      if (var1 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("RM.retire skip undeployed app=" + var0.appId);
         }

         retireTaskDone(var0);
         synchronized(var0) {
            var0.notifyAll();
            var0.setNotificationDone(true);
         }
      } else {
         try {
            DeployerRuntimeImpl var2 = (DeployerRuntimeImpl)DeploymentServerService.getDeployerRuntime();
            if (var2 == null) {
               return;
            }

            DeploymentTaskRuntimeMBean var3 = var2.retire(var0.appId, getDeployData(var0), var0.taskId, false);
            if (var3 != null) {
               var3.addPropertyChangeListener(new RetireTaskListener(var0));
            }

            var3.start();
         } catch (ManagementException var5) {
         }

      }
   }

   private static void retireTaskDone(RetireInfo var0) {
      if (var0 != null && var0.appId != null && var0.taskId != null) {
         RetireInfo var1 = (RetireInfo)retireInfoMap.get(var0.appId);
         if (var1 != null && var0.taskId.equals(var1.taskId)) {
            retireInfoMap.remove(var1.appId);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("RM.retireTaskDone for app=" + var1.appId + ", taskId=" + var1.taskId);
            }
         }

      }
   }

   private static void stopForGracefulRetire(RetireInfo var0) {
      AppDeploymentMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupAppDeployment(var0.appId);
      if (var1 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("RM.stopForGracefulRetire skip app=" + var0.appId);
         }

      } else {
         try {
            DeployerRuntimeImpl var2 = (DeployerRuntimeImpl)DeploymentServerService.getDeployerRuntime();
            if (var2 == null) {
               return;
            }

            DeploymentTaskRuntimeMBean var3 = var2.stopForGracefulRetire(var0.appId, getDeployData(var0), var0.taskId, false);
            if (var3 != null) {
               var3.addPropertyChangeListener(new GracefulStopTaskListener(var0));
            }

            var3.start();
         } catch (ManagementException var4) {
         }

      }
   }

   public static synchronized String getRetireTaskId(String var0) {
      return "weblogic.retire." + var0 + "." + curTaskId++;
   }

   public static boolean isRetireTaskId(String var0) {
      return var0 != null && var0.startsWith("weblogic.retire.");
   }

   private static int getRetireTimeoutSecs(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null ? var0.getDeploymentOptions().getRetireTime() : -1;
   }

   private static boolean getIgnoreSessions(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null ? var0.getDeploymentOptions().isGracefulIgnoreSessions() : false;
   }

   private static int getRMIGracePeriodSecs(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null ? var0.getDeploymentOptions().getRMIGracePeriodSecs() : -1;
   }

   private static DeploymentData getDeployData(RetireInfo var0) {
      DeploymentData var1 = new DeploymentData();
      DeploymentOptions var2 = new DeploymentOptions();
      var2.setRetireGracefully(var0.graceful);
      if (var0.graceful) {
         var1.setTimeOut(Integer.MAX_VALUE);
         var2.setTimeout(Long.MAX_VALUE);
      }

      var2.setGracefulProductionToAdmin(var0.graceful);
      var2.setGracefulIgnoreSessions(var0.ignoreSessions);
      var2.setRMIGracePeriodSecs(var0.rmiGracePeriodSecs);
      var1.setDeploymentOptions(var2);
      return var1;
   }

   private static synchronized void startTimerIfNeeded() {
      if (retireTimer == null) {
         retireTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new TimerListener() {
            public void timerExpired(Timer var1) {
               RetirementManager.retireExpiredApps();
            }
         }, 5000L, 5000L);
      }

   }

   private static class RetireOnRestartListener implements PropertyChangeListener {
      private ArrayList apps;

      private RetireOnRestartListener(ArrayList var1) {
         this.apps = var1;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equalsIgnoreCase("State")) {
            Object var2 = var1.getNewValue();
            if (var2 instanceof String && ((String)var2).equalsIgnoreCase("RUNNING")) {
               TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new TimerListener() {
                  public void timerExpired(Timer var1) {
                     RetirementManager.retireOnRestart(RetireOnRestartListener.this.apps);
                  }
               }, (long)(ManagementService.getRuntimeAccess(RetirementManager.kernelId).getServer().getAdminReconnectIntervalSeconds() * 1000));
            }
         }

      }

      // $FF: synthetic method
      RetireOnRestartListener(ArrayList var1, Object var2) {
         this(var1);
      }
   }

   private static class RetireTaskListener implements PropertyChangeListener {
      private RetireInfo info;

      private RetireTaskListener(RetireInfo var1) {
         this.info = var1;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equalsIgnoreCase("State")) {
            Object var2 = var1.getNewValue();
            if (var2 instanceof Integer) {
               int var3 = (Integer)var2;
               if (var3 == 2 || var3 == 3 || var3 == 4) {
                  RetirementManager.retireTaskDone(this.info);
                  synchronized(this.info) {
                     if (Debug.isDeploymentDebugEnabled()) {
                        Debug.deploymentDebug("RetireTaskListener.propertyChange for, app=" + this.info.appId + ", taskId=" + this.info.taskId);
                     }

                     this.info.notifyAll();
                     this.info.setNotificationDone(true);
                  }

                  try {
                     ((RuntimeMBeanDelegate)var1.getSource()).removePropertyChangeListener(this);
                  } catch (Throwable var6) {
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      RetireTaskListener(RetireInfo var1, Object var2) {
         this(var1);
      }
   }

   private static class GracefulStopTaskListener implements PropertyChangeListener {
      private RetireInfo info;

      private GracefulStopTaskListener(RetireInfo var1) {
         this.info = var1;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equalsIgnoreCase("State")) {
            Object var2 = var1.getNewValue();
            if (var2 instanceof Integer) {
               int var3 = (Integer)var2;
               if (var3 == 2 || var3 == 3 || var3 == 4) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("RM.stopForGracefulRetire done for app=" + this.info.appId + ", taskId=" + this.info.taskId);
                  }

                  RetirementManager.retireTaskDone(this.info);
                  this.info.taskId = RetirementManager.getRetireTaskId(this.info.appId);
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("RM.putRetireInfo for retire, app=" + this.info.appId + ", taskId=" + this.info.taskId);
                  }

                  RetirementManager.retireInfoMap.put(this.info.appId, this.info);
                  RetirementManager.retire(this.info);

                  try {
                     ((RuntimeMBeanDelegate)var1.getSource()).removePropertyChangeListener(this);
                  } catch (Throwable var5) {
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      GracefulStopTaskListener(RetireInfo var1, Object var2) {
         this(var1);
      }
   }

   private static class RetireInfo {
      private int state;
      private String appId;
      private String taskId;
      private boolean graceful;
      private boolean ignoreSessions;
      private int rmiGracePeriodSecs;
      private long retireTimeMillis;
      private boolean notificationDone;

      RetireInfo(int var1, String var2, String var3, boolean var4, boolean var5, int var6, long var7) {
         this.state = var1;
         this.appId = var2;
         this.taskId = var3;
         this.graceful = var4;
         this.ignoreSessions = var5;
         this.rmiGracePeriodSecs = var6;
         this.retireTimeMillis = var7;
         this.notificationDone = false;
      }

      boolean notificationDone() {
         return this.notificationDone;
      }

      void setNotificationDone(boolean var1) {
         this.notificationDone = var1;
      }

      public String toString() {
         return "RetireInfo[appId=" + this.appId + ",taskId=" + this.taskId + ",state=" + this.state + "]";
      }
   }
}
