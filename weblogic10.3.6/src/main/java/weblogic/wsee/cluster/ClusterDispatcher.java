package weblogic.wsee.cluster;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.rpc.JAXRPCException;
import weblogic.jndi.Environment;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.util.Verbose;

public class ClusterDispatcher implements ClusterDispatcherRemote {
   private static final boolean verbose = Verbose.isVerbose(ClusterDispatcher.class);
   public static final String CLUSTER_DISPATCHER_JNDI_NAME = "weblogic.wsee.cluster.ClusterDispatcher";
   private static ClusterDispatcher dispatcher;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Map services = new HashMap();

   public static synchronized ClusterDispatcher getInstance() {
      if (dispatcher == null) {
         dispatcher = new ClusterDispatcher();
      }

      return dispatcher;
   }

   private ClusterDispatcher() {
      Environment var1 = new Environment();
      var1.setCreateIntermediateContexts(true);
      var1.setReplicateBindings(false);

      try {
         Context var2 = var1.getInitialContext();
         PrivilegedActionUtilities.bindAsSU(var2, "weblogic.wsee.cluster.ClusterDispatcher", this, kernelId);
         if (verbose) {
            Verbose.say("Initialized ClusterDispather and bound it to non-replicated JNDI context at: weblogic.wsee.cluster.ClusterDispatcher");
         }

      } catch (NamingException var3) {
         if (verbose) {
            Verbose.logException(var3);
         }

         throw new JAXRPCException(var3);
      }
   }

   public synchronized void registerClusterService(ClusterService var1) {
      if (this.services.get(var1.getTargetURI()) == null) {
         if (verbose) {
            Verbose.say("Registering ClusterService at target URI " + var1.getTargetURI() + " as: " + var1);
         }

         this.services.put(var1.getTargetURI(), var1);
      }

   }

   public Serializable dispatch(String var1, Serializable var2) throws RemoteException {
      ClusterService var3 = this.getClusterService(var1);
      if (verbose) {
         Verbose.say("Dispatching using ClusterService for targetURI " + var1 + " and service (" + var3 + "), object: " + var2);
      }

      if (var3 == null) {
         throw new JAXRPCException("Cluster service for " + var1 + " not available");
      } else {
         try {
            return var3.dispatch(var2);
         } catch (ClusterServiceException var5) {
            throw new RemoteException(var5.toString());
         }
      }
   }

   private synchronized ClusterService getClusterService(String var1) {
      return (ClusterService)this.services.get(var1);
   }
}
