package weblogic.wsee.reliability2;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WseeWsrmRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.monitoring.WseeWsrmRuntimeMBeanImpl;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;

public class ReliabilityService {
   private static RuntimeAccess _runtimeAccess;

   public static void startup() throws WsrmException {
      SourceSequenceManager.getInstance();
      DestinationSequenceManager.getInstance();

      try {
         ServerRuntimeMBean var0 = _runtimeAccess.getServerRuntime();
         if (var0 != null && var0.getWseeWsrmRuntime() == null) {
            WseeWsrmRuntimeMBeanImpl var1 = new WseeWsrmRuntimeMBeanImpl(var0.getName(), var0);
            var1.register();
            var0.setWseeWsrmRuntime(var1);
         }

      } catch (Exception var2) {
         throw new WsrmException(var2.toString(), var2);
      }
   }

   public static void shutdown() {
      ServerRuntimeMBean var0 = _runtimeAccess.getServerRuntime();
      WseeWsrmRuntimeMBeanImpl var1 = (WseeWsrmRuntimeMBeanImpl)var0.getWseeWsrmRuntime();
      if (var1 != null) {
         var0.setWseeWsrmRuntime((WseeWsrmRuntimeMBean)null);

         try {
            var1.unregister();
         } catch (Exception var3) {
            WseeCoreLogger.logUnexpectedException(var3.toString(), var3);
         }
      }

   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
   }
}
