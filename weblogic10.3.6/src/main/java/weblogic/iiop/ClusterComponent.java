package weblogic.iiop;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.common.internal.PeerInfo;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.cluster.Version;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

public final class ClusterComponent extends TaggedComponent {
   private ArrayList replicas;
   private boolean idempotent;
   private boolean stickToFirstServer;
   private String algorithm;
   private String jndiName;
   private Version version;
   private static final String NULL_STRING = "";
   private static String defaultLoadAlgorithm = null;

   ClusterComponent(boolean var1, boolean var2, String var3, String var4, ArrayList var5, Version var6) {
      super(1111834883);
      this.idempotent = var1;
      this.algorithm = var3;
      this.replicas = var5;
      this.jndiName = var4;
      this.stickToFirstServer = var2;
      this.version = var6;
   }

   ClusterComponent(ClusterComponent var1) {
      super(1111834883);
      this.idempotent = var1.idempotent;
      this.algorithm = var1.algorithm;
      this.jndiName = var1.jndiName;
      this.stickToFirstServer = var1.stickToFirstServer;
      this.version = var1.version;
   }

   ClusterComponent(IIOPInputStream var1) {
      super(1111834883);
      this.read(var1);
   }

   public final ReplicaList getReplicaList() {
      Iterator var1 = this.replicas.iterator();
      VendorInfoCluster var2 = new VendorInfoCluster(new IIOPRemoteRef((IOR)var1.next()));

      while(var1.hasNext()) {
         var2.add(new IIOPRemoteRef((IOR)var1.next()));
      }

      var2.setClusterInfo(this);
      if (this.version != null) {
         var2.setVersion(this.version);
      }

      return var2;
   }

   public final ArrayList getIORs() {
      return this.replicas;
   }

   public final Version getVersion() {
      return this.version;
   }

   final void setIORs(ArrayList var1) {
      this.replicas = var1;
   }

   public IOR findLocalIOR() {
      Debug.assertion(this.replicas != null);
      int var1 = this.replicas.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         IOR var3 = (IOR)this.replicas.get(var2);
         if (var3.isLocal()) {
            return var3;
         }
      }

      return null;
   }

   public final boolean getIdempotent() {
      return this.idempotent;
   }

   public final boolean getStickToFirstServer() {
      return this.stickToFirstServer;
   }

   public final String getClusterAlgorithm() {
      return this.algorithm;
   }

   public final String getJndiName() {
      return this.jndiName;
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.algorithm = var1.read_string();
      this.idempotent = var1.read_boolean();
      this.stickToFirstServer = var1.read_boolean();
      this.jndiName = var1.read_string();
      if (this.jndiName.equals("")) {
         this.jndiName = null;
      }

      int var4 = var1.read_long();
      this.replicas = new ArrayList(var4);

      for(int var5 = 0; var5 < var4; ++var5) {
         IOR var6 = new IOR(var1);
         this.replicas.add(var6);
      }

      if (var1.bytesLeft(var2) >= 8) {
         this.version = new Version(var1.read_longlong());
      }

      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      if (this.algorithm.equals("default")) {
         var1.write_string(getDefaultLoadAlgorithm());
      } else {
         var1.write_string(this.algorithm);
      }

      var1.write_boolean(this.idempotent);
      var1.write_boolean(this.stickToFirstServer);
      if (this.jndiName != null && var1.getPeerInfo().compareTo(PeerInfo.VERSION_81) >= 0) {
         var1.write_string(this.jndiName);
      } else {
         var1.write_string("");
      }

      int var4 = this.replicas.size();
      var1.write_long(var4);

      for(int var5 = 0; var5 < var4; ++var5) {
         IOR var6 = (IOR)this.replicas.get(var5);
         var6.write(var1);
      }

      if (this.version != null) {
         var1.write_longlong(this.version.getVersion());
      }

      var1.endEncapsulation(var2);
   }

   private static String getDefaultLoadAlgorithm() {
      if (defaultLoadAlgorithm == null) {
         Class var0 = ClusterComponent.class;
         synchronized(ClusterComponent.class) {
            if (Kernel.isServer()) {
               AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               ClusterMBean var2 = ManagementService.getRuntimeAccess(var1).getServer().getCluster();
               if (var2 != null) {
                  defaultLoadAlgorithm = var2.getDefaultLoadAlgorithm();
               } else {
                  defaultLoadAlgorithm = "round-robin";
               }
            } else {
               defaultLoadAlgorithm = "round-robin";
            }
         }
      }

      return defaultLoadAlgorithm;
   }
}
