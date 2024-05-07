package weblogic.wsee.runtime;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;
import weblogic.wsee.WseeCoreLogger;

public final class WseeService extends ActivatedService {
   private static RuntimeAccess _runtimeAccess;
   private static boolean _initialized;

   public void stopService() throws ServiceFailureException {
      WseeCoreLogger.logWseeServiceStopping();
      WebServicesRuntime.getInstance().shutdown();
   }

   public void haltService() throws ServiceFailureException {
      WseeCoreLogger.logWseeServiceHalting();
      WebServicesRuntime.getInstance().shutdown();
   }

   public boolean startService() throws ServiceFailureException {
      WseeCoreLogger.logWseeServiceStarting();
      if (_initialized) {
         return true;
      } else {
         ServerStateChangeListener var1 = new ServerStateChangeListener();
         _runtimeAccess.getServerRuntime().addPropertyChangeListener(var1);
         return true;
      }
   }

   private static void handleServerUp() {
      WebServicesRuntime.getInstance().startup();
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
      _initialized = false;
   }

   private class ServerStateChangeListener implements PropertyChangeListener {
      boolean _serverUp;

      public ServerStateChangeListener() {
         this.interpretState(WseeService._runtimeAccess.getServerRuntime().getState());
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if ("State".equals(var1.getPropertyName())) {
            this.interpretState((String)var1.getNewValue());
         }

      }

      private void interpretState(String var1) {
         if (!"RESUMING".equals(var1) && !"RUNNING".equals(var1)) {
            if (!"ADMIN".equals(var1)) {
               this.setServerDown();
            }
         } else {
            this.setServerUp();
         }

      }

      private void setServerDown() {
         this._serverUp = false;
      }

      private void setServerUp() {
         if (!this._serverUp) {
            this._serverUp = true;
            WseeService.handleServerUp();
         }

         this._serverUp = true;
      }

      public boolean isServerUp() {
         return this._serverUp;
      }
   }
}
