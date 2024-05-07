package weblogic.io.common.internal;

import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.common.T3MiscLogger;
import weblogic.jndi.Environment;
import weblogic.logging.LogOutputStream;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.FileT3MBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.AssertionError;

public final class FileService extends AbstractServerService implements DeploymentHandler {
   private static FileService singleton = null;
   private Context context;
   private LogOutputStream log;

   public FileService() {
      if (singleton != null) {
         throw new AssertionError("File server module singleton already set");
      } else {
         singleton = this;
         this.log = new LogOutputStream("FileSystem");
      }
   }

   public void start() throws ServiceFailureException {
      DeploymentHandlerHome.addDeploymentHandler(this);
      this.context = this.getContext();
   }

   protected boolean isFileSystemMounted(String var1) {
      Context var2 = this.getContext();
      if (var2 == null) {
         return false;
      } else {
         try {
            var2.lookup(var1);
            return true;
         } catch (NamingException var4) {
            return false;
         }
      }
   }

   private void mountFileSystem(FileT3MBean var1) {
      Context var2 = this.getContext();
      if (var2 != null) {
         String var3 = var1.getName();
         String var4 = var1.getPath();

         try {
            var2.bind(var3, new T3FileSystemProxyImpl(var3, var4));
         } catch (NamingException var6) {
            T3MiscLogger.logMount(var1.getName(), var6);
         }

      }
   }

   private void unmountFileSystem(FileT3MBean var1) {
      Context var2 = this.getContext();
      if (var2 != null) {
         try {
            var2.unbind(var1.getName());
         } catch (NamingException var4) {
            T3MiscLogger.logUnmount(var1.getName(), var4);
         }

      }
   }

   private Context getContext() {
      if (this.context == null) {
         try {
            Environment var1 = new Environment();
            var1.setCreateIntermediateContexts(true);
            var1.setReplicateBindings(false);
            this.context = var1.getInitialContext().createSubcontext("weblogic.fileSystem");
            return this.context;
         } catch (NamingException var2) {
            T3MiscLogger.logGetRoot(var2);
         }
      }

      return this.context;
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof FileT3MBean) {
         try {
            this.mountFileSystem((FileT3MBean)var1);
         } catch (Exception var4) {
            throw new DeploymentException("error creating connection pool", var4);
         }
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) {
      if (var1 instanceof FileT3MBean) {
         this.unmountFileSystem((FileT3MBean)var1);
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
   }

   static FileService getFileService() {
      return singleton;
   }
}
