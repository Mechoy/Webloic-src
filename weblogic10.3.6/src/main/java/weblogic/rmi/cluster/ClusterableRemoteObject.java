package weblogic.rmi.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import org.omg.PortableServer.Servant;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.OpaqueReference;
import weblogic.jndi.internal.NamingNode;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rmi.extensions.server.DescriptorHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.CBVWrapper;
import weblogic.rmi.internal.RemoteType;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.utils.AssertionError;

public class ClusterableRemoteObject extends ReplicaAwareRemoteObject implements Aggregatable, OpaqueReference, Externalizable {
   private static final long serialVersionUID = 588136583649318138L;
   private RemoteType remoteType = null;
   protected transient ClusterableRemoteRef clusterableRef;

   static boolean isClusterable(Object var0) {
      Object var1 = null;
      if (var0 instanceof CBVWrapper) {
         var1 = ((CBVWrapper)var0).getDelegate();
      } else if (var0 instanceof Remote) {
         var1 = var0;
      } else if (var0 instanceof RemoteWrapper) {
         var1 = ((RemoteWrapper)var0).getRemoteDelegate();
      }

      if (var1 == null) {
         return false;
      } else {
         try {
            if (var1 instanceof StubInfoIntf) {
               StubInfo var5 = ((StubInfoIntf)var1).getStubInfo();
               RemoteReference var3 = var5.getRemoteRef();
               return var3 instanceof ClusterableRemoteRef;
            } else {
               RuntimeDescriptor var2 = DescriptorHelper.getDescriptor(var1.getClass());
               return var2.isClusterable();
            }
         } catch (RemoteException var4) {
            return false;
         }
      }
   }

   static boolean isIDLObject(Object var0) {
      return var0 instanceof org.omg.CORBA.Object || var0 instanceof Servant;
   }

   public ClusterableRemoteObject(Remote var1) throws RemoteException {
      super(var1);
      this.initialize();
   }

   public ClusterableRemoteObject(RemoteWrapper var1) throws RemoteException {
      super(var1);
      this.initialize();
   }

   private void initialize() {
      try {
         if (this.remoteType == null) {
            this.remoteType = ServerHelper.getDescriptor(this.getPrimaryRemote()).getRemoteType();
         }

         this.clusterableRef = this.getRef();
      } catch (RemoteException var2) {
         throw new AssertionError("Attempt to create RemoteObject using unexported Remote", var2);
      }
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      Object var3 = this.getPrimaryRepresentative();
      if (var3 == null) {
         throw new NamingException("unable to find primary representative");
      } else {
         return var3;
      }
   }

   public String toString() {
      return super.toString() + "\t" + this.remoteType.toString();
   }

   protected boolean isSameType(ClusterableRemoteObject var1) {
      RemoteType var2 = var1.remoteType;
      return this.remoteType != null && var2 != null ? this.remoteType.isAssignableFrom(var2) : false;
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (var3 == null) {
         try {
            this.initializeRef(var1.getNameInNamespace(var2));
         } catch (RemoteException var8) {
            throw new AssertionError("impossible exception", var8);
         }
      } else {
         if (!(var3 instanceof ClusterableRemoteObject)) {
            throw new NameAlreadyBoundException();
         }

         ClusterableRemoteObject var4 = (ClusterableRemoteObject)var3;
         if (!this.isSameType(var4)) {
            throw new NameAlreadyBoundException("Failed to bind remote object (" + var4 + ") to replica aware stub at " + var2 + " (" + this + ") because it " + "does not implement the same remote interfaces");
         }

         ClusterableRemoteRef var5 = var4.clusterableRef;
         if (!this.clusterableRef.getHostID().equals(var5.getHostID())) {
            try {
               var4.initializeRef(var1.getNameInNamespace(var2));
               this.clusterableRef.add(var4.clusterableRef);
            } catch (RemoteException var7) {
               throw new AssertionError("impossible exception", var7);
            }
         } else {
            if (this.clusterableRef.getObjectID() != var5.getObjectID()) {
               throw new NameAlreadyBoundException("Failed to bind remote object (" + var4 + ") to replica aware stub at " + var2 + "(" + this + ")");
            }

            this.clusterableRef.getReplicaList().add(var5.getReplicaList().getPrimary());
         }
      }

   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      boolean var4 = false;
      if (var3 instanceof ClusterableRemoteObject) {
         ClusterableRemoteObject var5 = (ClusterableRemoteObject)var3;
         if (this.isSameType(var5)) {
            try {
               var5.initializeRef(var1.getNameInNamespace(var2));
               this.clusterableRef.replace(var5.clusterableRef);
               var4 = true;
            } catch (RemoteException var7) {
               throw new AssertionError("impossible exception", var7);
            }
         }
      }

      if (!var4) {
         throw new NameAlreadyBoundException("Can't rebind anything but a replica-aware stub to a name that is currently bound to a replica-aware stub");
      }
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (var3 == null) {
         this.clusterableRef.removeOne(LocalServerIdentity.getIdentity());
         this.setPrimaryRepresentativeToNull();
      } else if (var3 instanceof ClusterableRemoteObject) {
         try {
            ClusterableRemoteObject var4 = (ClusterableRemoteObject)var3;
            if (this.isSameType(var4)) {
               this.clusterableRef.remove(var4.getRef());
            }
         } catch (RemoteException var6) {
            ConfigurationException var5 = new ConfigurationException("failed to unbind due to unexpected exception");
            var5.setRootCause(var6);
            throw var5;
         }
      }

      return this.clusterableRef.getReplicaCount() == 0;
   }

   protected void initializeRef(String var1) throws ConfigurationException {
      if (!this.clusterableRef.isInitialized()) {
         try {
            ReplicaAwareInfo var2 = this.getInfo();
            var2.setJNDIName(var1);
            this.clusterableRef.initialize(var2);
         } catch (NoSuchObjectException var4) {
            ConfigurationException var3 = new ConfigurationException("failed to rebind due to unexpected exception");
            var3.setRootCause(var4);
            throw var3;
         }
      }

   }

   public ClusterableRemoteObject() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.remoteType);
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.remoteType = (RemoteType)var1.readObject();
      super.readExternal(var1);
      this.initialize();
   }
}
