package weblogic.rmi.cluster;

import java.io.Externalizable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.utils.AssertionError;

public final class PrimarySecondaryRemoteObject extends ReplicaAwareRemoteObject implements Externalizable {
   private static final long serialVersionUID = 8733059777893264840L;
   private transient Remote secondary;

   public PrimarySecondaryRemoteObject(Remote var1, Remote var2) throws RemoteException {
      super(var1);

      try {
         ClusterableRemoteRef var3 = this.getRef();
         var3.initialize(this.getInfo());
         this.changeSecondary(var2);
      } catch (RemoteException var4) {
         throw new AssertionError("impossible exception", var4);
      }
   }

   public void changeSecondary(Remote var1) {
      ClusterableRemoteRef var2;
      try {
         var2 = this.getRef();
      } catch (RemoteException var8) {
         throw new AssertionError("impossible exception", var8);
      }

      RemoteReference var3 = var2.getPrimaryRef();
      RemoteReference var4 = null;
      if (var1 instanceof StubInfoIntf) {
         this.secondary = var1;
         ClusterableRemoteRef var5 = (ClusterableRemoteRef)((StubInfoIntf)var1).getStubInfo().getRemoteRef();
         var4 = var5.getPrimaryRef();
      } else if (var1 != null) {
         throw new AssertionError("if not null secondary must always be a Stub");
      }

      ReplicaList var9;
      try {
         var9 = (ReplicaList)var2.getReplicaList().clone();
      } catch (CloneNotSupportedException var7) {
         throw new AssertionError("couldn't clone replica list");
      }

      var9.clear();
      var9.add(var3);
      if (var4 != null) {
         var9.add(var4);
      }

      var2.resetReplicaList(var9);
   }

   public Remote getSecondary() {
      return this.secondary;
   }

   public Remote getPrimary() {
      return this.getPrimaryRemote();
   }

   public PrimarySecondaryRemoteObject() {
   }
}
