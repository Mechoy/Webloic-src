package weblogic.logging;

import com.bea.logging.MsgIdPrefixConverter;
import java.security.AccessController;
import java.util.logging.Logger;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.diagnostics.image.ImageManager;
import weblogic.kernel.KernelLogManager;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.logging.LogRuntime;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ServerLoggingInitializer {
   private static final boolean DEBUG = false;

   public static void initializeServerLogging() throws ManagementException {
      RuntimeAccess var0 = getRuntimeAccess();
      ServerMBean var1 = var0.getServer();
      if (var1 != null) {
         LogBufferHandlerInitializer.initServerLogBufferHandler(var1.getLog());
         JDKLoggerFactory var2 = JDKLoggerFactory.getJDKLoggerFactory(var1.getLog());
         Logger var3 = var2.createAndInitializeServerLogger(var1);
         KernelLogManager.setLogger(var3);
         var1.getLog().addPropertyChangeListener(StdoutSeverityListener.getStdoutSeverityListener(var1));
         if (var1.getLog().isRedirectStdoutToServerLogEnabled()) {
            System.setOut(new LoggingPrintStream(new LoggingOutputStream("Stdout", WLLevel.NOTICE, true)));
         }

         if (var1.getLog().isRedirectStderrToServerLogEnabled()) {
            System.setErr(new LoggingPrintStream(new LoggingOutputStream("StdErr", WLLevel.NOTICE, true)));
         }

         ImageManager var4 = ImageManager.getInstance();
         var4.registerImageSource("Logging", LoggingImageSource.getInstance());
         ServerRuntimeMBean var5 = var0.getServerRuntime();
         LogRuntime var6 = new LogRuntime(var1.getLog(), var5);
         var5.setLogRuntime(var6);
         LogEntryInitializer.setServerInitialized(true);
         boolean var7 = var0.getDomain().isMsgIdPrefixCompatibilityEnabled();
         initializeMsgIdPrefixConverter(var7);
         var0.getDomain().addBeanUpdateListener(new BeanUpdateListener() {
            public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
               DomainMBean var2 = (DomainMBean)var1.getProposedBean();
               ServerLoggingInitializer.initializeMsgIdPrefixConverter(var2.isMsgIdPrefixCompatibilityEnabled());
            }

            public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
            }

            public void rollbackUpdate(BeanUpdateEvent var1) {
            }
         });
      }
   }

   private static void initializeMsgIdPrefixConverter(boolean var0) {
      MsgIdPrefixConverter.setCompatibilityModeEnabled(var0);
   }

   private static RuntimeAccess getRuntimeAccess() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return ManagementService.getRuntimeAccess(var0);
   }
}
