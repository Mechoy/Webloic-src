package weblogic.application.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationContext;
import weblogic.application.utils.StateChange;
import weblogic.application.utils.StateChangeException;
import weblogic.application.utils.StateMachineDriver;
import weblogic.deploy.common.Debug;
import weblogic.deploy.container.DeploymentContext;
import weblogic.j2ee.J2EELogger;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.server.AbstractServerService;
import weblogic.server.RunningStateListener;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.internal.OnDemandListener;
import weblogic.servlet.internal.WebService;
import weblogic.t3.srvr.ServerServicesManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public final class BackgroundDeploymentService extends AbstractServerService implements OnDemandListener, TimerListener, RunningStateListener {
   private static BackgroundDeploymentService THE_ONE = null;
   private static final List<BackgroundApplication> backgroundApps = new ArrayList();
   private static final List<BackgroundApplication> backgroundOnDemandApps = new ArrayList();
   private volatile boolean stillRunning;
   private volatile boolean backgroundActionScheduled = false;
   private BackgroundDeployAction backgroundAction = null;
   private static final long BACKGROUND_DELAY = 3000L;
   private static final StateChange prepareStateChange = new StateChange() {
      public void next(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().prepare(var2.ctx);
      }

      public void previous(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().unprepare(var2.ctx);
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   };
   private static final StateChange activateStateChange = new StateChange() {
      public void next(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().activate(var2.ctx);
      }

      public void previous(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().deactivate(var2.ctx);
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   };
   private static final StateChange adminStateChange = new StateChange() {
      public void next(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().adminToProduction(var2.ctx);
      }

      public void previous(Object var1) throws Exception {
         BackgroundApplication var2 = (BackgroundApplication)var1;
         var2.deployment.getDelegate().forceProductionToAdmin(var2.ctx);
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringAdminModeErrro(var1.getCause());
      }
   };

   public BackgroundDeploymentService() {
      assert THE_ONE == null;

      THE_ONE = this;
   }

   public static BackgroundDeploymentService getInstance() {
      return THE_ONE;
   }

   static void addBackgroundDeployment(BackgroundDeployment var0, DeploymentContext var1) {
      ApplicationContext var2 = var0.getApplicationContext();
      AppDeploymentMBean var3 = var2 != null ? var2.getAppDeploymentMBean() : null;
      if (var3 != null && var3.getOnDemandContextPaths() != null && var3.getOnDemandContextPaths().length > 0) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Add background deployment: " + var3.getName());
         }

         backgroundOnDemandApps.add(new BackgroundApplication(var0, var1));
      } else {
         backgroundApps.add(new BackgroundApplication(var0, var1));
      }

   }

   private synchronized void waitForCompletion() {
      while(this.stillRunning) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

   }

   private synchronized void complete() {
      this.stillRunning = false;
      this.notifyAll();
   }

   public void start() throws ServiceFailureException {
      AppDeploymentMBean var4;
      if (!backgroundOnDemandApps.isEmpty()) {
         for(Iterator var1 = backgroundOnDemandApps.iterator(); var1.hasNext(); WebService.defaultHttpServer().getOnDemandManager().registerOnDemandContextPaths(var4.getOnDemandContextPaths(), this, var4.getName(), var4.isOnDemandDisplayRefresh())) {
            BackgroundApplication var2 = (BackgroundApplication)var1.next();
            ApplicationContext var3 = var2.deployment.getApplicationContext();
            var4 = var3.getAppDeploymentMBean();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Registering on demand context paths for : " + var4.getName());
            }
         }
      }

      if (!backgroundApps.isEmpty()) {
         this.stillRunning = true;
         BackgroundApplication[] var5 = (BackgroundApplication[])((BackgroundApplication[])backgroundApps.toArray(new BackgroundApplication[backgroundApps.size()]));
         this.backgroundAction = new BackgroundDeployAction(var5);
         TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 3000L);
         ServerServicesManager.addRunningStateListener(this);
      }
   }

   public void OnDemandURIAccessed(String var1, String var2, boolean var3) throws DeploymentException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("OnDemand URI accessed : " + var1 + " for app: " + var2 + " load async: " + var3);
      }

      BackgroundApplication var4 = null;
      Iterator var5 = backgroundOnDemandApps.iterator();
      boolean var6 = false;

      while(var5.hasNext() && !var6) {
         var4 = (BackgroundApplication)var5.next();
         ApplicationContext var7 = var4.deployment.getApplicationContext();
         AppDeploymentMBean var8 = var7.getAppDeploymentMBean();
         if (var2.equals(var8.getName())) {
            if (var4.deployment.getCompletedDeployment()) {
               return;
            }

            if (var4.failureException != null) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Background deployment error: ", var4.failureException);
               }

               if (var4.failureException instanceof DeploymentException) {
                  throw (DeploymentException)var4.failureException;
               }

               throw new DeploymentException(var4.failureException);
            }

            var6 = true;
         }
      }

      if (!var6) {
         throw new IllegalArgumentException("appName " + var2 + " is not found");
      } else {
         this.waitForCompletion();
         if (!var3 || !var4.deployment.getStartedDeployment()) {
            OnDemandBackgroundDeployAction var9 = new OnDemandBackgroundDeployAction(var4);
            if (var3) {
               WorkManagerFactory.getInstance().getSystem().schedule(var9);
            } else {
               var9.run();
               if (var4.failureException != null) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Background deployment error: ", var4.failureException);
                  }

                  if (var4.failureException instanceof DeploymentException) {
                     throw (DeploymentException)var4.failureException;
                  }

                  throw new DeploymentException(var4.failureException);
               }
            }

         }
      }
   }

   public void timerExpired(Timer var1) {
      this.scheduleBackgroundDeployAction();
   }

   public void onRunning() {
      this.scheduleBackgroundDeployAction();
   }

   private synchronized void scheduleBackgroundDeployAction() {
      if (!this.backgroundActionScheduled) {
         this.backgroundActionScheduled = true;
         WorkManagerFactory.getInstance().getSystem().schedule(this.backgroundAction);
         this.backgroundAction = null;
      }
   }

   private static class OnDemandBackgroundDeployAction extends BackgroundDeployAction {
      private final BackgroundApplication app;

      OnDemandBackgroundDeployAction(BackgroundApplication var1) {
         super(new BackgroundApplication[]{var1});
         this.app = var1;
      }

      public void run() {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Performing on demand background deploy action: ");
         }

         Class var1 = OnDemandBackgroundDeployAction.class;
         synchronized(OnDemandBackgroundDeployAction.class) {
            this.app.deployment.setStartedDeployment(true);
            if (!this.app.deployment.getCompletedDeployment() && this.app.failureException == null) {
               super.run();
            }
         }
      }
   }

   private static class BackgroundDeployAction implements Runnable {
      private final BackgroundApplication[] backgroundApps;
      private StateMachineDriver driver = new StateMachineDriver();

      BackgroundDeployAction(BackgroundApplication[] var1) {
         this.backgroundApps = var1;
      }

      public void run() {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Starting background deploy action");
         }

         try {
            this.driver.nextState(BackgroundDeploymentService.prepareStateChange, this.backgroundApps);

            try {
               this.driver.nextState(BackgroundDeploymentService.activateStateChange, this.backgroundApps);

               try {
                  this.driver.nextState(BackgroundDeploymentService.adminStateChange, this.backgroundApps);

                  for(int var12 = 0; var12 < this.backgroundApps.length; ++var12) {
                     this.backgroundApps[var12].deployment.setCompletedDeployment(true);
                  }
               } catch (Exception var8) {
                  this.driver.previousState(BackgroundDeploymentService.activateStateChange, this.backgroundApps);
                  throw var8;
               }
            } catch (Exception var9) {
               this.driver.previousState(BackgroundDeploymentService.prepareStateChange, this.backgroundApps);
               throw var9;
            }
         } catch (Exception var10) {
            Exception var1 = var10;
            J2EELogger.logErrorDeployingApplication("Internal Application", var10.getMessage(), var10);

            for(int var2 = 0; var2 < this.backgroundApps.length; ++var2) {
               this.backgroundApps[var2].failureException = var1;
            }
         } finally {
            BackgroundDeploymentService.getInstance().complete();
         }

      }
   }

   private static class BackgroundApplication {
      private final BackgroundDeployment deployment;
      private final DeploymentContext ctx;
      private Exception failureException;

      BackgroundApplication(BackgroundDeployment var1, DeploymentContext var2) {
         this.deployment = var1;
         this.ctx = var2;
      }
   }

   public static class WaitForBackgroundCompletion extends AbstractServerService {
      public void stop() throws ServiceFailureException {
         BackgroundDeploymentService.getInstance().waitForCompletion();
      }

      public void halt() throws ServiceFailureException {
         BackgroundDeploymentService.getInstance().waitForCompletion();
      }
   }
}
