package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.kernel.KernelStatus;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.cluster.ReplicaAwareInfo;
import weblogic.rmi.cluster.ReplicaHandler;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.cluster.RetryHandler;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.utils.Debug;

public class NamingNodeReplicaHandler implements ReplicaHandler, Externalizable {
   private static final int MAX_RETRIES = 3;
   private static final long serialVersionUID = -1480987318128214931L;
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugFailOver");
   private String name;
   private transient Environment env;

   public NamingNodeReplicaHandler(ReplicaAwareInfo var1, RemoteReference var2) {
      this();
      this.name = var1.getJNDIName();
   }

   public void resetReplicaList(ReplicaList var1) {
   }

   public void resetRefreshedCount() {
   }

   public String toString() {
      return "NamingNodeReplicaHandler (for " + this.name + ")";
   }

   public RemoteReference loadBalance(RemoteReference var1, Method var2, Object[] var3) {
      return var1;
   }

   public RemoteReference failOver(RemoteReference var1, RuntimeMethodDescriptor var2, Method var3, Object[] var4, RemoteException var5, RetryHandler var6) throws RemoteException {
      Throwable var7 = var5.getCause();
      if (var1.getHostID().isLocal()) {
         throw var5;
      } else if (!(var7 instanceof AdminModeAccessException) && !RemoteHelper.isRecoverableFailure(var5)) {
         throw var5;
      } else if (var6.getRetryCount() >= 3) {
         throw var5;
      } else {
         String var8 = this.name.length() == 0 ? "<InitialContext>" : '"' + this.name.toString() + '"';
         if (NamingDebugLogger.isDebugEnabled() && logger.isDebugEnabled()) {
            NamingDebugLogger.debug(var8 + " attempting failover due to: " + var5);
         }

         Context var9 = null;

         RemoteReference var13;
         try {
            var9 = this.env.getContext(this.name, var1.getHostID());
            RemoteWrapper var10 = (RemoteWrapper)var9;
            RemoteReference var11 = ((StubInfoIntf)var10.getRemoteDelegate()).getStubInfo().getRemoteRef();
            Debug.assertion(var11 instanceof ClusterableRemoteRef);
            RemoteReference var12 = ((ClusterableRemoteRef)var11).getCurrentReplica();
            if (NamingDebugLogger.isDebugEnabled() && logger.isDebugEnabled()) {
               NamingDebugLogger.debug(var8 + " failing over to " + var12.getHostID());
            }

            var13 = var12;
         } catch (NamingException var22) {
            if (NamingDebugLogger.isDebugEnabled() && logger.isDebugEnabled()) {
               NamingDebugLogger.debug(var8 + " unable to failover due to " + var22);
            }

            throw var5;
         } finally {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (NamingException var21) {
               }
            }

         }

         return var13;
      }
   }

   public ReplicaList getReplicaList() {
      return null;
   }

   public NamingNodeReplicaHandler() {
      this.env = ThreadEnvironment.get();
      if (this.env == null) {
         if (KernelStatus.isServer()) {
            this.env = new Environment();
         } else {
            if (NamingDebugLogger.isDebugEnabled() && logger.isDebugEnabled()) {
               NamingDebugLogger.debug("Environment not found on the thread");
            }

            this.env = new Environment();
         }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.name);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.name = var1.readUTF();
   }
}
