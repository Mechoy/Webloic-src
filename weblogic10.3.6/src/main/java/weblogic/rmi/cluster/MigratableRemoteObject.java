package weblogic.rmi.cluster;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;
import javax.naming.NamingException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.internal.NamingNode;
import weblogic.rmi.extensions.server.DescriptorHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.rmi.internal.CBVWrapper;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.spi.HostID;
import weblogic.utils.Debug;

public final class MigratableRemoteObject extends ClusterableRemoteObject {
   private static final long serialVersionUID = -8639826242081467352L;
   private static final boolean DEBUG = true;

   public static boolean isEOS(Object var0) {
      Remote var1 = null;
      if (var0 instanceof CBVWrapper) {
         var1 = ((CBVWrapper)var0).getDelegate();
      } else if (var0 instanceof Remote) {
         var1 = (Remote)var0;
      } else if (var0 instanceof RemoteWrapper) {
         var1 = ((RemoteWrapper)var0).getRemoteDelegate();
      }

      if (var1 == null) {
         return false;
      } else {
         try {
            boolean var2 = false;
            if (var1 instanceof StubInfoIntf) {
               StubInfo var3 = ((StubInfoIntf)var1).getStubInfo();
               RemoteReference var4 = var3.getRemoteRef();
               if (var4 instanceof ClusterableRemoteRef) {
                  ClusterableRemoteRef var5 = (ClusterableRemoteRef)var4;
                  var2 = var5.getReplicaHandler() instanceof MigratableReplicaHandler;
               }
            } else {
               RuntimeDescriptor var7 = DescriptorHelper.getDescriptor(var1.getClass());
               String var8 = var7.getReplicaHandlerClassName();
               var2 = var8 != null && var8.equals(MigratableReplicaHandler.class.getName());
            }

            return var2;
         } catch (RemoteException var6) {
            return false;
         }
      }
   }

   private void resetList(String var1) {
      HostID[] var2 = MigrationManager.singleton().getMigratableHostList(var1);
      ReplicaList var3 = this.clusterableRef.getReplicaList();
      if (var2 != null) {
         Debug.say(var1 + " OLD LIST " + var3);
         Debug.say(var1 + " MIG MAN LIST " + Arrays.asList((Object[])var2));
         if (var2 != null && var2.length != 0) {
            BasicReplicaList var4 = null;

            int var5;
            for(var5 = 0; var5 < var2.length; ++var5) {
               if (var2[var5] != null) {
                  RemoteReference var6 = var3.findReplicaHostedBy(var2[var5]);
                  if (var6 != null) {
                     if (var4 == null) {
                        var4 = new BasicReplicaList(var6);
                     } else {
                        var4.add(var6);
                     }
                  }
               }
            }

            for(var5 = 0; var5 < var3.size(); ++var5) {
               if (var4 == null) {
                  var4 = new BasicReplicaList(var3.get(var5));
               }

               if (var4.findReplicaHostedBy(var3.get(var5).getHostID()) == null) {
                  var4.add(var3.get(var5));
               }
            }

            if (var4 != null && var4.size() != 0) {
               Debug.say(var1 + " NEW LIST " + var4);
               this.clusterableRef.getReplicaList().resetWithoutShuffle(var4);
               this.clusterableRef.setCurRef(var4.get(0));
            }
         } else {
            Debug.say("WARNING For service " + var1 + " the migration man. returned a list of 0");
         }
      }
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      super.onBind(var1, var2, var3);
      this.resetList(var2);
   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      super.onRebind(var1, var2, var3);
      this.resetList(var2);
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      boolean var4 = super.onUnbind(var1, var2, var3);
      this.resetList(var2);
      return var4;
   }

   public MigratableRemoteObject(Remote var1) throws RemoteException {
      super(var1);
   }

   public MigratableRemoteObject(RemoteWrapper var1) throws RemoteException {
      super(var1);
   }

   public MigratableRemoteObject() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }
}
