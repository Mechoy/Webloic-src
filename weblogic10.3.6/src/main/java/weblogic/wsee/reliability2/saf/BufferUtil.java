package weblogic.wsee.reliability2.saf;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.application.ApplicationAccess;
import weblogic.application.internal.ApplicationContextImpl;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.spi.BufferingProvider;
import weblogic.wsee.buffer2.spi.BufferingProviderManager;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class BufferUtil {
   private static final Logger LOGGER = Logger.getLogger(BufferUtil.class.getName());
   private static final AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Object mdbPoolCreationLock = new Object();

   public static BufferingFeature setupBufferingFeature(ServerTubeAssemblerContext var0, WSEndpoint var1, TubelineSpliceFactory.DispatchFactory var2, boolean var3, WsrmConfig.Destination var4) throws BufferingException {
      if (var4.isNonBufferedDestination()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setupBufferingFeature:  RM Destination is non-buffered.  Skip BufferingSetup.");
         }

         return null;
      } else {
         BufferingProviderManager.App_Server_Platform var5 = BufferingProviderManager.App_Server_Platform.WLS;
         BufferingProvider var6 = BufferingProviderManager.getBufferingProvider(var5);
         BufferingManager var7 = var6.getBufferingManager();
         BufferingFeature var8 = var7.newBufferingFeature(BufferingFeature.BufferingFeatureUsers.WSRM, var0, var3, var1, var2);
         setupBufferingFeature(var8, var1, var4);
         return var8;
      }
   }

   public static BufferingFeature setupBufferingFeature(ClientTubeAssemblerContext var0, WLSEndpoint var1, TubelineSpliceFactory.DispatchFactory var2, boolean var3, WsrmConfig.Destination var4) throws BufferingException {
      if (var4.isNonBufferedDestination()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setupBufferingFeature:  RM Destination is non-buffered.  Skip BufferingSetup.");
         }

         return null;
      } else {
         BufferingProviderManager.App_Server_Platform var5 = BufferingProviderManager.App_Server_Platform.WLS;
         BufferingProvider var6 = BufferingProviderManager.getBufferingProvider(var5);
         BufferingManager var7 = var6.getBufferingManager();
         BufferingFeature var8 = var7.newBufferingFeature(BufferingFeature.BufferingFeatureUsers.WSRM, var0, var3, var1, var2);
         setupBufferingFeature(var8, var1.getWSEndpoint(), var4);
         return var8;
      }
   }

   private static void setupBufferingFeature(BufferingFeature var0, WSEndpoint var1, WsrmConfig.Destination var2) throws BufferingException {
      BufferingProviderManager.App_Server_Platform var3 = BufferingProviderManager.App_Server_Platform.WLS;
      WsrmSAFEndpointManager var4 = (WsrmSAFEndpointManager)SAFManagerImpl.getManager().getEndpointManager(3);

      assert var4 != null;

      EnvironmentFactory var5 = JAXRPCEnvironmentFeature.getFactory(var1);
      WsDeploymentContext var6 = var5.getDeploymentContext();
      ServerUtil.QueueInfo var7 = new ServerUtil.QueueInfo(var0.getQueueJndiName(), (String)null);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.log(Level.FINE, "setupBufferingFeature created new BufferingFeature with Endpoint serviceName='" + var1.getServiceName() + "', portName='" + var1.getPortName() + "', " + " Queue JNDI Name= " + var0.getQueueJndiName() + "'" + "  processing contextPath='" + var6.getContextPath() + "', to BufferingFeature Id='" + var0.getBufferingFeatureId() + "'");
      }

      List var8 = getTargetURIs(var1);
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "setupBufferingFeature processing targetURI='" + var10 + "'");
         }

         BufferingFeature.addTargetURI(var10, var0);
         WsrmSAFEndpoint var11 = new WsrmSAFEndpoint(var10);
         var4.addEndpoint(var10, var11);
      }

      boolean var18 = BufferingFeature.isQueueRegistered(var0.getQueueJndiName());
      if (var3 == BufferingProviderManager.App_Server_Platform.WLS && !var18) {
         synchronized(mdbPoolCreationLock) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("BufferingFeature: Set up dynamic MDB to queue: " + var0.getQueueJndiName());
            }

            DeployMDBAction var17 = new DeployMDBAction(var6, "", var7, var0.getBufferDispatch().getMDBClassName(), var0);

            try {
               SecurityServiceManager.runAs(_kernelId, _kernelId, var17);
            } catch (PrivilegedActionException var15) {
               Throwable var13 = var15.getCause();
               if (var13 instanceof RuntimeException) {
                  throw (RuntimeException)var13;
               }

               throw new RuntimeException(var13.toString(), var13);
            }
         }
      }

   }

   public static void setupBuffering(WSEndpoint var0, TubelineSpliceFactory.DispatchFactory var1, boolean var2, WsrmConfig.Destination var3) {
      if (!var3.isNonBufferedDestination()) {
         WsrmSAFEndpointManager var4 = (WsrmSAFEndpointManager)SAFManagerImpl.getManager().getEndpointManager(3);

         assert var4 != null;

         EnvironmentFactory var5 = JAXRPCEnvironmentFeature.getFactory(var0);
         WsDeploymentContext var6 = var5.getDeploymentContext();
         ServerUtil.QueueInfo var7 = new ServerUtil.QueueInfo(var3.getBufferingConfig().getRequestQueue().getJndiName(), (String)null);
         StringBuffer var8 = new StringBuffer();
         BufferMdbListener var9 = new BufferMdbListener(var0, var1, var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "created new BufferMdbListener with Endpoint serviceName='" + var0.getServiceName() + "', portName='" + var0.getPortName() + "'");
         }

         boolean var10 = true;

         for(int var11 = 0; var11 < var6.getServiceURIs().length; ++var11) {
            String var12 = var6.getContextPath();
            String var13 = var6.getServiceURIs()[var11];
            String var14 = AsyncUtil.calculateServiceTargetURI(var12, var13);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "processing contextPath='" + var12 + "', serviceUri='" + var13 + "', calculated targetUri='" + var14 + "'");
            }

            BufferManager var15 = BufferManager.instance();
            synchronized(var15) {
               if (var15.getMessageListener(var14) != null) {
                  var10 = false;
                  continue;
               }

               var15.addMessageListener(var14, var9);
            }

            var6.addBufferTargetURI(var14);
            var15.setTargetQueue(var14, var7);
            WsrmSAFEndpoint var16 = new WsrmSAFEndpoint(var14);
            var4.addEndpoint(var14, var16);
            if (var8.length() == 0) {
               var8.append(AsyncUtil.getAsyncSelector(var14));
            } else {
               var8.append(" OR (");
               var8.append(AsyncUtil.getAsyncSelector(var14));
               var8.append(")");
            }
         }

         if (var10) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Set up dynamic MDB to queue: " + var7.getQueueName());
            }

            DeployMDBAction var20 = new DeployMDBAction(var6, var8.toString(), var7, "weblogic.wsee.server.jms.MdbWS");

            try {
               SecurityServiceManager.runAs(_kernelId, _kernelId, var20);
            } catch (PrivilegedActionException var18) {
               Throwable var21 = var18.getCause();
               if (var21 instanceof RuntimeException) {
                  throw (RuntimeException)var21;
               }

               throw new RuntimeException(var21.toString(), var21);
            }
         }

      }
   }

   public static List<String> getTargetURIs(WSEndpoint var0) {
      ArrayList var1 = new ArrayList();
      EnvironmentFactory var2 = JAXRPCEnvironmentFeature.getFactory(var0);
      WsDeploymentContext var3 = var2.getDeploymentContext();
      String var4 = var3.getContextPath();

      for(int var5 = 0; var5 < var3.getServiceURIs().length; ++var5) {
         String var6 = var3.getServiceURIs()[var5];
         var1.add(AsyncUtil.calculateServiceTargetURI(var4, var6));
      }

      return var1;
   }

   private static class DeployMDBAction implements PrivilegedExceptionAction {
      WsDeploymentContext _deployContext;
      String _messageSelector;
      ServerUtil.QueueInfo _queueInfo;
      String _mdbClassName;
      BufferingFeature _bufferingFeature;

      private DeployMDBAction(WsDeploymentContext var1, String var2, ServerUtil.QueueInfo var3, String var4) {
         this._deployContext = var1;
         this._messageSelector = var2;
         this._queueInfo = var3;
         this._mdbClassName = var4;
         this._bufferingFeature = null;
      }

      private DeployMDBAction(WsDeploymentContext var1, String var2, ServerUtil.QueueInfo var3, String var4, BufferingFeature var5) {
         this._deployContext = var1;
         this._messageSelector = var2;
         this._queueInfo = var3;
         this._mdbClassName = var4;
         this._bufferingFeature = var5;
      }

      public Object run() throws Exception {
         ApplicationContextImpl var1 = (ApplicationContextImpl)ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
         AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(BufferUtil._kernelId);
         var1.setDeploymentInitiator(var2);
         AsyncUtil.setupDynamicMDB(this._deployContext, this._messageSelector, this._queueInfo.getQueueName(), this._queueInfo.getMdbRunAsPrincipalName(), this._mdbClassName, 180, this._bufferingFeature != null, "weblogic.wsee.jaxws.mdb.DispatchPolicy");
         return null;
      }

      // $FF: synthetic method
      DeployMDBAction(WsDeploymentContext var1, String var2, ServerUtil.QueueInfo var3, String var4, BufferingFeature var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      // $FF: synthetic method
      DeployMDBAction(WsDeploymentContext var1, String var2, ServerUtil.QueueInfo var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
