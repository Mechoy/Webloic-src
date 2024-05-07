package weblogic.rmi.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.internal.AppClassLoaderManagerImpl;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.jndi.internal.NameAlreadyUnboundException;
import weblogic.rmi.extensions.server.DescriptorHelper;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.CBVWrapper;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.utils.AssertionError;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.GenericClassLoader;

public class ReplicaAwareRemoteObject implements Externalizable {
   private static final long serialVersionUID = -3476281508384826108L;
   private Object primaryRepresentative;
   private String primaryRemoteName;
   private String appName;
   private static final boolean DEBUG = false;
   private transient Remote primaryRemote;
   private transient ReplicaAwareInfo info;
   private transient ClusterableRemoteRef remoteRef;
   private transient StubInfoIntf stub;
   private transient boolean isRemoteWrapper;

   protected ReplicaAwareRemoteObject(Remote var1) throws RemoteException {
      if (var1 instanceof CBVWrapper) {
         this.primaryRepresentative = var1;
         this.primaryRemote = ((CBVWrapper)var1).getDelegate();
      } else {
         this.primaryRepresentative = var1;
         this.primaryRemote = var1;
      }

      this.initialize();
      if (!ServerHelper.isCollocated(this.primaryRemote)) {
         throw new AssertionError(var1.toString() + " is not hosted locally");
      }
   }

   protected ReplicaAwareRemoteObject(RemoteWrapper var1) throws RemoteException {
      this.isRemoteWrapper = true;
      this.primaryRepresentative = var1;
      this.primaryRemote = var1.getRemoteDelegate();
      this.initialize();
      if (!ServerHelper.isCollocated(this.primaryRemote)) {
         throw new AssertionError(var1.toString() + " is not hosted locally");
      }
   }

   private void initialize() throws RemoteException {
      this.stub = (StubInfoIntf)ServerHelper.exportObject(this.primaryRemote);
      this.remoteRef = (ClusterableRemoteRef)this.stub.getStubInfo().getRemoteRef();
      this.primaryRemoteName = this.primaryRemote.getClass().getName();
      this.appName = this.stub.getStubInfo().getApplicationName();
   }

   public Object getPrimaryRepresentative() {
      if (this.primaryRepresentative == null) {
         try {
            Class var1 = null;
            if (this.appName != null) {
               AppClassLoaderManager var2 = AppClassLoaderManagerImpl.getAppClassLoaderManager();
               GenericClassLoader var3 = var2.findLoader(new Annotation(this.appName));
               if (var3 != null) {
                  var1 = var3.loadClass(this.primaryRemoteName);
               } else {
                  var1 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader().loadClass(this.primaryRemoteName);
               }
            } else {
               var1 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader().loadClass(this.primaryRemoteName);
            }

            RuntimeDescriptor var6 = DescriptorHelper.getDescriptor(var1);
            return new StubInfo(this.getRef(), var6.getClientRuntimeDescriptor(this.appName), var6.getStubClassName());
         } catch (ClassNotFoundException var4) {
            return null;
         } catch (RemoteException var5) {
            throw new AssertionError("Unexpected exception ", var5);
         }
      } else {
         return this.primaryRepresentative;
      }
   }

   protected void setPrimaryRepresentativeToNull() {
      this.primaryRepresentative = null;
   }

   public Remote getPrimaryRemote() {
      return this.primaryRemote;
   }

   public ReplicaAwareInfo getInfo() throws NoSuchObjectException {
      if (this.info == null) {
         ClusterableServerRef var1 = (ClusterableServerRef)ServerHelper.getServerReference(this.primaryRemote);
         this.info = var1.getInfo();
      }

      return this.info;
   }

   public ClusterableRemoteRef getRef() throws RemoteException {
      if (this.remoteRef == null) {
         if (!ServerHelper.isCollocated(this.primaryRemote)) {
            throw new AssertionError(this.primaryRemote + ": is not local");
         }

         ClusterableServerRef var1 = (ClusterableServerRef)ServerHelper.getServerReference(this.primaryRemote);
         this.remoteRef = var1.getReplicaAwareRemoteRef();
      }

      return this.remoteRef;
   }

   public ReplicaAwareRemoteObject() {
   }

   public String toString() {
      return this.remoteRef.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (this.isRemoteWrapper) {
         if (this.primaryRepresentative == null) {
            throw new NameAlreadyUnboundException("Object already partially unbound in cluster.");
         }

         var1.writeBoolean(this.isRemoteWrapper);
         var1.writeObject(this.primaryRepresentative);
      } else if (var1 instanceof WLObjectOutput) {
         WLObjectOutput var2 = (WLObjectOutput)var1;
         var2.writeBoolean(this.isRemoteWrapper);
         var2.writeString(this.primaryRemoteName);
         var2.writeString(this.appName);
         var2.writeObject(this.getRef());
      } else {
         var1.writeBoolean(this.isRemoteWrapper);
         var1.writeObject(this.primaryRemoteName);
         var1.writeObject(this.appName);
         var1.writeObject(this.getRef());
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.isRemoteWrapper = var1.readBoolean();
      if (this.isRemoteWrapper) {
         this.primaryRepresentative = var1.readObject();
         Remote var2 = ((RemoteWrapper)this.primaryRepresentative).getRemoteDelegate();
         if (var2 instanceof StubInfoIntf) {
            this.primaryRemoteName = var2.getClass().getName();
            StubInfoIntf var3 = (StubInfoIntf)var2;
            this.remoteRef = (ClusterableRemoteRef)var3.getStubInfo().getRemoteRef();
         } else {
            this.primaryRemote = var2;
            this.initialize();
         }
      } else if (var1 instanceof WLObjectInput) {
         WLObjectInput var4 = (WLObjectInput)var1;
         this.primaryRemoteName = var4.readString();
         this.appName = var4.readString();
         this.remoteRef = (ClusterableRemoteRef)var4.readObject();
      } else {
         this.primaryRemoteName = (String)var1.readObject();
         this.appName = (String)var1.readObject();
         this.remoteRef = (ClusterableRemoteRef)var1.readObject();
      }

   }

   static void p(String var0) {
      System.out.println("<ReplicaAwareRemoteObject>: " + var0);
   }
}
