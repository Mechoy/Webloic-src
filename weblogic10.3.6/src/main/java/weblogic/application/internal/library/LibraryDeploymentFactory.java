package weblogic.application.internal.library;

import java.io.File;
import java.security.AccessController;
import java.util.jar.Attributes;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryDeploymentException;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.PathUtils;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class LibraryDeploymentFactory implements DeploymentFactory {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverName;
   private static final ApplicationFactoryManager afm;

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws LibraryDeploymentException {
      if (!(var1 instanceof LibraryMBean)) {
         return null;
      } else {
         LibraryMBean var3 = (LibraryMBean)var1;
         LibraryData var4 = this.getLibData(var3, var2);
         String var5 = PathUtils.generateTempPath(serverName, var4.getName(), var4.getSpecificationVersion() + var4.getImplementationVersion());
         File var6 = PathUtils.getAppTempDir(var5, var3.isInternalApp());
         LibraryDefinition var7 = null;

         try {
            var7 = LibraryLoggingUtils.getLibraryDefinition(var4, var6, afm.getLibraryFactories());
         } catch (LoggableLibraryProcessingException var9) {
            throw new LibraryDeploymentException(var9.getLoggable().getMessage());
         }

         return this.createDeployment(var7, var3);
      }
   }

   private Deployment createDeployment(LibraryDefinition var1, LibraryMBean var2) {
      return new LibraryDeployment(var1, var2);
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) {
      return null;
   }

   private LibraryData getLibData(LibraryMBean var1, File var2) throws LibraryDeploymentException {
      LibraryData var3 = null;

      try {
         var3 = LibraryLoggingUtils.initLibraryData(var1, var2);
      } catch (LoggableLibraryProcessingException var9) {
         throw new LibraryDeploymentException(var9.getLoggable().getMessage());
      }

      LibraryData var4 = null;
      Attributes var5 = var3.getAttributes();

      try {
         var4 = LibraryLoggingUtils.initLibraryData(var2, var5);
      } catch (LoggableLibraryProcessingException var8) {
         throw new LibraryDeploymentException(var8.getLoggable().getMessage());
      }

      var4 = var4.importData(var3);

      try {
         LibraryLoggingUtils.handleLibraryInfoMismatch(var4, var3);
         return var3;
      } catch (LoggableLibraryProcessingException var7) {
         throw new LibraryDeploymentException(var7.getLoggable().getMessage());
      }
   }

   static {
      serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      afm = ApplicationFactoryManager.getApplicationFactoryManager();
   }
}
