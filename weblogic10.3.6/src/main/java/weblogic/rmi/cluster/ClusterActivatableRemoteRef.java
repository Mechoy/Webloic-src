package weblogic.rmi.cluster;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.activation.ActivatableRemoteRef;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.RMIRuntime;

public final class ClusterActivatableRemoteRef extends ActivatableRemoteRef implements InteropWriteReplaceable {
   private static final long serialVersionUID = -9116119681422760510L;
   private String jndiName;
   private ReplicaHandler replicaHandler;
   private Environment environment;

   public ClusterActivatableRemoteRef() {
   }

   public ClusterActivatableRemoteRef(int var1, HostID var2, Object var3, String var4) {
      super(var1, var2, var3);
      this.jndiName = var4;
   }

   public Object invoke(Remote var1, RuntimeMethodDescriptor var2, Object[] var3, Method var4) throws Throwable {
      if (this.environment != null) {
         ThreadEnvironment.push(this.environment);
      }

      try {
         RetryHandler var5 = new RetryHandler();
         int var6 = 0;

         while(true) {
            try {
               var5.setRetryCount(var6);
               Object var7 = super.invoke((Remote)null, var2, var3, var4);
               return var7;
            } catch (RemoteException var12) {
               RemoteReference var8 = this.replicaHandler.failOver((RemoteReference)null, var2, var4, var3, var12, var5);
               this.oid = var8.getObjectID();
               this.hostID = var8.getHostID();
               ++var6;
            }
         }
      } finally {
         if (this.environment != null) {
            ThreadEnvironment.pop();
         }

      }
   }

   public int hashCode() {
      return super.hashCode() ^ this.jndiName.hashCode();
   }

   public String toString() {
      return super.toString() + ", jndiName: '" + this.jndiName + "'";
   }

   public Object interopWriteReplace(PeerInfo var1) throws RemoteException {
      if (var1.getMajor() == 6 && var1.getMinor() == 1) {
         ClusterActivatableServerRef var2 = (ClusterActivatableServerRef)OIDManager.getInstance().getServerReference(this.getObjectID());
         Activator var3 = var2.getActivator();
         Activatable var4 = var3.activate(this.getActivationID());
         EntityServerRef var5 = null;

         try {
            var5 = (EntityServerRef)ServerHelper.getServerReference((Remote)var4);
         } catch (NoSuchObjectException var7) {
         }

         if (var5 == null) {
            var5 = new EntityServerRef(var4);
         }

         var5.exportObject();
         return var5.getRemoteRef();
      } else {
         return this;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeObject(this.jndiName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.jndiName = (String)var1.readObject();
      HostID var2 = this.getHostID();
      this.environment = new Environment();
      if (!var2.isLocal()) {
         this.environment.setProviderUrl(RMIRuntime.findOrCreateEndPoint(var2).getClusterURL(var1));
      }

      this.replicaHandler = new EntityBeanReplicaHandler(this.getActivationID(), this.jndiName, this.environment);
   }
}
