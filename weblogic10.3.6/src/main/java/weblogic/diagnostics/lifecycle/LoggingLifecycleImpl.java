package weblogic.diagnostics.lifecycle;

import java.security.AccessController;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.instrumentation.gathering.DataGatheringManager;
import weblogic.logging.DomainLogBroadcasterClient;
import weblogic.logging.LogEditObserver;
import weblogic.management.ManagementException;
import weblogic.management.configuration.LogMBean;
import weblogic.management.logging.DomainLogHandlerException;
import weblogic.management.logging.DomainLogHandlerImpl;
import weblogic.management.logging.LogBroadcaster;
import weblogic.management.logging.LogRuntime;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class LoggingLifecycleImpl implements DiagnosticComponentLifecycle {
   private static LoggingLifecycleImpl singleton = new LoggingLifecycleImpl();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int status = 4;
   private DomainLogBroadcasterClient client = null;

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return this.status;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      try {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         var1.addAccessCallbackClass(LogEditObserver.class.getName());
         if (var1.isAdminServer()) {
            DomainLogHandlerImpl.getInstance();
            DomainRuntimeMBean var2 = ManagementService.getDomainAccess(kernelId).getDomainRuntime();
            LogMBean var3 = var1.getDomain().getLog();
            LogRuntime var4 = new LogRuntime(var3, var2);
            var2.setLogRuntime(var4);
         }
      } catch (DomainLogHandlerException var6) {
         DiagnosticsLogger.logErrorCreatingDomainLogHandler(var6);
      } catch (ManagementException var7) {
         throw new DiagnosticComponentLifecycleException(var7.toString());
      }

      try {
         LogBroadcaster.getLogBroadcaster();
         this.client = DomainLogBroadcasterClient.getInstance();
         this.initDomainLogHandler(false);
         ConnectMonitorFactory.getConnectMonitor().addConnectListener(new ConnectListener() {
            public void onConnect(ConnectEvent var1) {
               if (var1.getServerName().equals(ManagementService.getRuntimeAccess(LoggingLifecycleImpl.kernelId).getAdminServerName())) {
                  if (LoggingLifecycleImpl.this.client != null) {
                     LoggingLifecycleImpl.this.initDomainLogHandler(true);
                  }
               }
            }
         });
      } catch (ManagementException var5) {
         throw new DiagnosticComponentLifecycleException(var5.toString());
      }

      DataGatheringManager.initializeLogging();
      this.status = 1;
   }

   private void initDomainLogHandler(boolean var1) {
      this.client.initDomainLogHandler(var1);
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
      DomainLogBroadcasterClient var1 = DomainLogBroadcasterClient.getInstance();
      var1.flush();
      var1.close();
   }
}
