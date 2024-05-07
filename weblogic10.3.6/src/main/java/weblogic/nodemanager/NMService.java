package weblogic.nodemanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.AccessController;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.OverloadProtectionMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.nodemanager.common.StateInfo;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.nodemanager.server.DomainDir;
import weblogic.nodemanager.server.ServerDir;
import weblogic.nodemanager.util.ConcurrentFile;
import weblogic.nodemanager.util.ProcessControl;
import weblogic.nodemanager.util.ProcessControlFactory;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.Debug;

public class NMService extends AbstractServerService implements PropertyChangeListener {
   private boolean started;
   private String startupMode;
   private String srvrURL;
   private StateInfo stateInfo;
   private ConcurrentFile stateFile;
   private ConcurrentFile pidFile;
   private ConcurrentFile urlFile;
   public static final String SERVICE_ENABLED_PROP = "weblogic.nodemanager.ServiceEnabled";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static NMService instance;

   public NMService() {
      Class var1 = NMService.class;
      synchronized(NMService.class) {
         Debug.assertion(instance == null);
         instance = this;
      }
   }

   public static NMService getInstance() {
      return instance;
   }

   public synchronized void start() throws ServiceFailureException {
      if (!this.started) {
         if (Boolean.getBoolean("weblogic.nodemanager.ServiceEnabled")) {
            this.stateInfo = new StateInfo();
            ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
            this.startupMode = var1.getStartupMode();
            ServerDir var2 = this.getServerDir();
            this.pidFile = var2.getPidFile();
            this.urlFile = var2.getURLFile();
            this.stateFile = var2.getStateFile();
            this.writeProcessId();
            ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            var3.addPropertyChangeListener(this);
            var1.getServerStart().addPropertyChangeListener(this);
            if (var1.getAutoKillIfFailed()) {
               OverloadProtectionMBean var4 = var1.getOverloadProtection();
               var4.setFailureAction("force-shutdown");
               var4.setPanicAction("system-exit");
            }

            this.started = true;
         }
      }
   }

   private boolean writeProcessId() {
      ProcessControl var1 = null;

      try {
         var1 = ProcessControlFactory.getProcessControl();
      } catch (UnsatisfiedLinkError var4) {
      }

      if (var1 == null) {
         NodeManagerLogger.logNativePidSupportUnavailable();
         return false;
      } else {
         try {
            this.pidFile.writeLine(var1.getProcessId());
            return true;
         } catch (IOException var3) {
            NodeManagerLogger.logErrorWritingPidFile(this.pidFile.getPath(), var3);
            return false;
         }
      }
   }

   private boolean writeServerURL() {
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      this.srvrURL = var1.getURL("http");
      if (this.srvrURL == null) {
         this.srvrURL = var1.getURL("https");
      }

      try {
         this.urlFile.writeLine(this.srvrURL);
         return true;
      } catch (IOException var3) {
         NodeManagerLogger.logErrorWritingURLFile(this.urlFile.getPath(), var3);
         return false;
      }
   }

   private ServerDir getServerDir() {
      DomainDir var1 = new DomainDir(BootStrap.getRootDirectory());
      String var2 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      return var1.getServerDir(var2);
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public synchronized void halt() throws ServiceFailureException {
      if (this.started) {
         ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         T3Srvr var2 = T3Srvr.getT3Srvr();
         String var3 = var2.getState();

         assert var3.equals("SHUTTING_DOWN");

         assert var3.equals("FORCE_SHUTTING_DOWN");

         this.stateInfo.setState(var3);
         this.stateInfo.setStarted(var2.isStarted());
         this.stateInfo.setFailed(var1.isShuttingDownDueToFailure());
         this.finishHalting();
      }
   }

   private synchronized void finishHalting() throws ServiceFailureException {
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      var1.removePropertyChangeListener(this);
      ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
      var2.getServerStart().removePropertyChangeListener(this);
      this.writeStateInfo();
      this.pidFile.delete();
      this.started = false;
   }

   public void hardShutdown() throws ServiceFailureException {
      if (this.started) {
         this.stateInfo.setState("FORCE_SHUTTING_DOWN");
         this.stateInfo.setStarted(true);
         this.stateInfo.setFailed(false);
         this.finishHalting();
      }
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      if (this.started) {
         if ("State".equals(var1.getPropertyName())) {
            String var2 = (String)var1.getNewValue();
            ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            this.stateInfo.setState(var2);
            if (var3.isStartupAbortedInAdminState()) {
               this.stateInfo.setStarted(true);
               this.writeServerURL();
               var2 = var2 + "_ON_ABORTED_STARTUP";
               this.stateInfo.setState(var2);
            } else if (var2.equals(this.startupMode)) {
               this.stateInfo.setStarted(true);
               this.writeServerURL();
            } else if ("FORCE_SHUTTING_DOWN".equals(var2) && var3.isShuttingDownDueToFailure()) {
               this.stateInfo.setFailed(true);
            }

            this.writeStateInfo();
         } else {
            ServerMBean var6 = ManagementService.getRuntimeAccess(kernelId).getServer();
            NodeManagerRuntime var7 = NodeManagerRuntime.getInstance(var6);

            try {
               var7.updateServerProps(var6);
            } catch (IOException var5) {
               NodeManagerLogger.logErrorUpdatingServerProps(var6.getName(), var5);
            }
         }

      }
   }

   private void writeStateInfo() {
      try {
         this.stateInfo.save(this.stateFile);
      } catch (IOException var2) {
         NodeManagerLogger.logStateChangeNotificationFailureMsg(var2);
         Runtime.getRuntime().halt(1);
      }

   }
}
