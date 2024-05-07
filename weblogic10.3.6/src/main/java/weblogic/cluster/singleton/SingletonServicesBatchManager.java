package weblogic.cluster.singleton;

import java.rmi.RemoteException;
import java.security.AccessController;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.StackTraceUtils;

public class SingletonServicesBatchManager implements ServerService, TimerListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static SingletonServicesBatchManager singleton;
   private boolean started = false;
   private static final long SINGLETON_MONITOR_DISCOVERY_PERIOD_MILLIS = 10000L;

   public String getName() {
      return "Singleton Services Batch Manager";
   }

   public String getVersion() {
      return "1.0";
   }

   synchronized boolean hasStarted() {
      return this.started;
   }

   static SingletonServicesBatchManager theOne() {
      return singleton;
   }

   public synchronized void startService(String var1) throws IllegalArgumentException {
      try {
         this.registerWithSingletonMonitor(var1);
      } catch (RemoteException var3) {
         throw new AssertionError("Unable to register singleton service '" + var1 + "'\n" + StackTraceUtils.throwable2StackTrace(var3));
      } catch (LeasingException var4) {
         throw new AssertionError("Unable to register singleton service '" + var1 + "'\n" + StackTraceUtils.throwable2StackTrace(var4));
      }
   }

   public synchronized void start() throws ServiceFailureException {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      if (var1.getCluster() != null) {
         singleton = new SingletonServicesBatchManager();
         singleton.started = true;
         TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 0L, 10000L);
      }
   }

   private void registerWithSingletonMonitor(String var1) throws RemoteException, LeasingException {
      SingletonMonitorRemote var2 = MigratableServerService.theOne().getSingletonMasterRemote();
      if (var2 != null) {
         var2.register(var1);
      } else {
         throw new LeasingException("Could not contact Singleton Monitor.");
      }
   }

   public void timerExpired(Timer var1) {
      try {
         SingletonMonitorRemote var2 = MigratableServerService.theOne().getSingletonMasterRemote();
         if (var2 == null) {
            return;
         }
      } catch (LeasingException var8) {
         return;
      }

      Object[] var9 = SingletonServicesManager.getInstance().getInternalSingletonServices();

      for(int var3 = 0; var3 < var9.length; ++var3) {
         String var4 = (String)var9[var3];

         try {
            this.registerWithSingletonMonitor(var4);
         } catch (RemoteException var6) {
            throw new AssertionError("Unable to register singleton service '" + var4 + "'\n" + StackTraceUtils.throwable2StackTrace(var6));
         } catch (LeasingException var7) {
            throw new AssertionError("Unable to register singleton service '" + var4 + "'\n" + StackTraceUtils.throwable2StackTrace(var7));
         }
      }

      var1.cancel();
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }
}
