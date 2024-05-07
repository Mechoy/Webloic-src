package weblogic.connector.common;

import java.security.AccessController;
import weblogic.application.ApplicationFactoryManager;
import weblogic.connector.deploy.ConnectorDeploymentFactory;
import weblogic.connector.deploy.ConnectorModuleFactory;
import weblogic.connector.deploy.TransportableJNDIHandler;
import weblogic.connector.exception.RAException;
import weblogic.connector.monitoring.ServiceRuntimeMBeanImpl;
import weblogic.diagnostics.image.ImageManager;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.ErrorCollectionException;

public final class ConnectorService extends ActivatedService {
   private static ServiceRuntimeMBeanImpl sMBean;
   private static boolean alreadyInitialized = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean imageSourceRegistered = false;

   public void stopService() throws ServiceFailureException {
      Debug.service("stopService() called on ConnectorService ");

      try {
         RACollectionManager.stop();
         this.haltService();
      } catch (RAException var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public void haltService() throws ServiceFailureException {
      Debug.service("haltService() called on ConnectorService ");
      ErrorCollectionException var1 = null;

      try {
         RACollectionManager.halt();
      } catch (RAException var5) {
         Debug.logConnectorServiceShutdownError(var5.toString());
         if (var1 == null) {
            var1 = new ErrorCollectionException();
         }

         var1.addError(var5);
      }

      try {
         sMBean.unregister();
      } catch (ManagementException var4) {
         Debug.logConnectorServiceShutdownError(var4.toString());
         if (var1 == null) {
            var1 = new ErrorCollectionException();
         }

         var1.addError(var4);
      }

      if (imageSourceRegistered) {
         try {
            Debug.service("Unregistering the Connector diagnostic image source");
            ImageManager var2 = ImageManager.getInstance();
            var2.unregisterImageSource("CONNECTOR");
            imageSourceRegistered = false;
         } catch (Throwable var3) {
            Debug.logDiagImageUnregisterFailure(var3);
            if (var1 == null) {
               var1 = new ErrorCollectionException();
            }

            var1.addError(var3);
         }
      }

      if (var1 != null) {
         throw new ServiceFailureException(var1);
      }
   }

   public boolean startService() throws ServiceFailureException {
      Debug.service("startService() called on ConnectorService ");
      Debug.logConnectorServiceInitializing();
      if (alreadyInitialized) {
         return true;
      } else {
         try {
            Debug.service("Creating ServiceRuntimeMBeanImpl");
            sMBean = new ServiceRuntimeMBeanImpl();
            ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setConnectorServiceRuntime(sMBean);
         } catch (ManagementException var6) {
            Debug.logConnectorServiceInitError(var6.toString());
            throw new ServiceFailureException(var6);
         }

         ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
         ConnectorDeploymentFactory var2 = new ConnectorDeploymentFactory();
         var1.addDeploymentFactory(var2);
         var1.addModuleFactory(new ConnectorModuleFactory());
         alreadyInitialized = true;
         Debug.logConnectorServiceInitialized();

         try {
            Debug.service("Initializing the Connector diagnostic image source");
            ImageManager var3 = ImageManager.getInstance();
            ConnectorDiagnosticImageSource var4 = new ConnectorDiagnosticImageSource();
            var3.registerImageSource("CONNECTOR", var4);
            imageSourceRegistered = true;
         } catch (Throwable var5) {
            Debug.logDiagImageRegisterFailure(var5);
         }

         Debug.service("Registering the connector TransportableJNDIHandler as a TransportableFactory");
         WLNamingManager.addTransportableFactory(new TransportableJNDIHandler());
         return true;
      }
   }

   public static ServiceRuntimeMBeanImpl getConnectorServiceRuntimeMBean() {
      return sMBean;
   }
}
