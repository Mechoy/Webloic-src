package weblogic.deploy.service.datatransferhandlers;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.targetserver.datamanagement.ModuleRedeployDataTransferRequestImpl;
import weblogic.deploy.service.AppDataTransferRequest;
import weblogic.deploy.service.ConfigDataTransferRequest;
import weblogic.deploy.service.DataTransferHandler;
import weblogic.deploy.service.DataTransferRequest;
import weblogic.deploy.service.MultiDataStream;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DataHandlerManager {
   public static final String HTTP_FILE_BASED_HANDLER = "HTTP";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private DataTransferHandler handler = null;

   public static DataHandlerManager getInstance() {
      return DataHandlerManager.Maker.singleton;
   }

   public DataTransferHandler getHttpDataTransferHandler() {
      synchronized(this) {
         if (this.handler == null) {
            this.handler = (DataTransferHandler)(ManagementService.getPropertyService(kernelId).isAdminServer() ? new LocalDataTransferHandler("HTTP") : new RemoteDataTransferHandler("HTTP"));
         }
      }

      return this.handler;
   }

   public static DataTransferHandler getHandler(String var0) throws IOException {
      if (var0 == null) {
         var0 = "HTTP";
      }

      DataTransferHandler var1 = DeploymentService.getDeploymentService().getDataTransferHandler(var0);
      if (var1 == null) {
         throw new IOException(DeploymentServiceLogger.logNoDataHandlerRegisteredLoggable(var0).getMessage());
      } else {
         return var1;
      }
   }

   protected static void validateRequestType(DataTransferRequest var0) throws IOException {
      if (!(var0 instanceof AppDataTransferRequest) && !(var0 instanceof ConfigDataTransferRequest)) {
         throw new IOException("Invalid request type : " + var0.getClass().getName());
      }
   }

   private static MultiDataStream createDeploymentStream(BasicDeploymentMBean var0, AppDataTransferRequest var1) throws IOException {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      if (!var2.isAdminServer()) {
         throw new AssertionError("Cannot be invoked on managed server.");
      } else {
         List var3;
         if (var1 instanceof ModuleRedeployDataTransferRequestImpl) {
            var3 = var1.getFilePaths();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("createMultiStream for " + var0.getName() + " moduleIds= " + var3);
            }

            if (!(var0 instanceof AppDeploymentMBean)) {
               throw new AssertionError("ModuleRedeploy cannot be applied for SystemResources");
            } else {
               return SourceCache.getSourceCache(var0).getAppDataLocationsForModuleIds(var3, (AppDeploymentMBean)var0);
            }
         } else {
            var3 = var1.getFilePaths();
            boolean var4 = var1.isPlanUpdate();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("createMultiStream for " + var0.getName() + " urisOrFiles= " + var3 + ", isPlanUpdate=" + var4);
            }

            return SourceCache.getSourceCache(var0).getDataLocations(var3, var4, var0);
         }
      }
   }

   private static MultiDataStream createConfigDataStream(ConfigDataTransferRequest var0) throws IOException {
      List var1 = var0.getFilePaths();
      if (var1 != null && !var1.isEmpty()) {
         File var2 = new File(DomainDir.getRootDir());
         MultiDataStream var3 = DataStreamFactory.createMultiDataStream();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            File var6 = new File(var2, var5);
            var3.addFileDataStream(var5, var6, false);
         }

         return var3;
      } else {
         throw new IOException("Got request with empty file paths");
      }
   }

   private class RemoteDataTransferHandler implements DataTransferHandler {
      private DataTransferHandler delegate;

      private RemoteDataTransferHandler(String var2) {
         if ("HTTP".equals(var2)) {
            this.delegate = new HttpDataTransferHandler();
         } else {
            throw new IllegalArgumentException("Unsupported handler type " + var2);
         }
      }

      public final String getType() {
         return this.delegate.getType();
      }

      public MultiDataStream getDataAsStream(DataTransferRequest var1) throws IOException {
         return this.delegate.getDataAsStream(var1);
      }

      // $FF: synthetic method
      RemoteDataTransferHandler(String var2, Object var3) {
         this(var2);
      }
   }

   private class LocalDataTransferHandler extends RemoteDataTransferHandler {
      private LocalDataTransferHandler(String var2) {
         super(var2, null);
      }

      public MultiDataStream getDataAsStream(DataTransferRequest var1) throws IOException {
         DataHandlerManager.validateRequestType(var1);
         if (var1 instanceof AppDataTransferRequest) {
            AppDataTransferRequest var2 = (AppDataTransferRequest)var1;
            String var3 = ApplicationVersionUtils.getApplicationId(var2.getAppName(), var2.getAppVersionIdentifier());
            long var4 = var2.getRequestId();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(" LocalHandler.getDataAsStream() : handling application data request for application : " + var2.getAppName() + " : request id : " + var4);
            }

            BasicDeploymentMBean var6 = AppDeploymentHelper.lookupBasicDeployment(var3, var4);
            if (var6 == null) {
               throw new AssertionError("Could not find DeploymentMBean for '" + var3 + "' for deployment request '" + var4 + "'");
            } else {
               return DataHandlerManager.createDeploymentStream(var6, var2);
            }
         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(" LocalHandler.getDataAsStream() : handling config data request : " + var1.getFilePaths() + " : request id : " + var1.getRequestId());
            }

            return DataHandlerManager.createConfigDataStream((ConfigDataTransferRequest)var1);
         }
      }

      // $FF: synthetic method
      LocalDataTransferHandler(String var2, Object var3) {
         this(var2);
      }
   }

   static class Maker {
      private static final DataHandlerManager singleton = new DataHandlerManager();
   }
}
