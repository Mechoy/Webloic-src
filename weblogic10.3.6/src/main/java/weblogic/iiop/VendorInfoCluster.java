package weblogic.iiop;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import org.omg.CORBA.MARSHAL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.rjvm.JVMID;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.cluster.Version;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.spi.HostID;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class VendorInfoCluster extends ServiceContext implements ReplicaList, Externalizable {
   private static final long serialVersionUID = -5343036220579753659L;
   private ArrayList replicas;
   private ReplicaList replicaList;
   private Version version;
   private HashMap hostToReplicaMap;
   private IIOPRemoteRef localRef;
   private ClusterComponent clustInfo;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");

   public VendorInfoCluster() {
      super(1111834883);
      this.replicaList = this;
   }

   VendorInfoCluster(Object var1) {
      this();
      if (var1 instanceof Version) {
         this.version = (Version)var1;
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("VendorInfoCluster(Version: " + var1 + ")");
         }
      } else if (var1 instanceof VendorInfoCluster) {
         this.reset((ReplicaList)var1);
         this.replicas = new ArrayList(1);
         this.version = new Version(0L);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("VendorInfoCluster(VendorInfoCluster: " + var1 + ")");
         }
      } else if (var1 instanceof ReplicaList) {
         this.replicaList = (ReplicaList)var1;
         this.version = (Version)((ReplicaList)var1).version();
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("VendorInfoCluster(ReplicaList: " + var1 + ")");
         }
      }

   }

   public VendorInfoCluster(Version var1) {
      this((Object)var1);
   }

   public VendorInfoCluster(IIOPRemoteRef var1) {
      this();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("VendorInfoCluster(IIOPRemoteRef: " + var1 + ")");
      }

      this.replicas = new ArrayList(1);
      this.version = new Version(0L);
      this.add(var1);
   }

   public final ArrayList getReplicaList() {
      return this.replicas;
   }

   public void setClusterInfo(ClusterComponent var1) {
      this.clustInfo = new ClusterComponent(var1);
   }

   void setVersion(Version var1) {
      this.version = var1;
   }

   public ClusterComponent getClusterInfo() {
      if (this.clustInfo != null) {
         ArrayList var1 = new ArrayList(this.replicas.size());
         Iterator var2 = this.replicas.iterator();

         while(var2.hasNext()) {
            IIOPRemoteRef var3 = (IIOPRemoteRef)var2.next();
            var1.add(var3.getIOR());
         }

         this.clustInfo.setIORs(var1);
      }

      return this.clustInfo;
   }

   protected VendorInfoCluster(IIOPInputStream var1) {
      super(1111834883);
      this.readEncapsulatedContext(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.version = new Version(var1.read_longlong());
      int var2 = var1.read_long();
      if (var2 > 0) {
         this.replicas = new ArrayList(var2);

         for(int var3 = 0; var3 < var2; ++var3) {
            this.replicas.add(new IIOPRemoteRef(new IOR(var1)));
         }
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("readEncapsulation(version " + this.version.getVersion() + " " + var2 + " IORs)");
      }

   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_longlong(this.version.getVersion());
      int var2 = this.replicaList == null ? 0 : this.replicaList.size();
      IOR[] var3 = new IOR[var2];
      int var4 = 0;
      IIOPReplacer var5 = IIOPReplacer.getIIOPReplacer();

      int var6;
      for(var6 = 0; var6 < var2; ++var6) {
         try {
            RemoteReference var7 = this.replicaList.get(var6);
            Object var8 = var5.replaceObject(var7);
            if (var8 instanceof IOR) {
               var3[var6] = (IOR)var8;
            } else {
               var7 = this.replicaList.findReplicaHostedBy(JVMID.localID());
               ServerReference var9 = OIDManager.getInstance().getServerReference(var7.getObjectID());
               StubInfo var10 = (StubInfo)var9.getStubReference();
               String var11 = Utils.getRepositoryID(var10);
               var3[var6] = var5.getIOR((RemoteReference)var8, var11, var9.getApplicationName(), (ClusterComponent)null, var9.getDescriptor(), (Object)null);
            }

            ++var4;
         } catch (MARSHAL var12) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugMarshalError("Failed to export ClusterComponent member", var12);
            }
         } catch (IOException var13) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugMarshalError("Failed to export ClusterComponent member", var13);
            }
         }
      }

      var1.write_long(var4);

      for(var6 = 0; var6 < var4; ++var6) {
         var3[var6].write(var1);
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("writeEncapsulation(version " + this.version.getVersion() + ", " + var4 + " of " + var2 + " IORs)");
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      IIOPOutputStream var2 = new IIOPOutputStream();
      long var3 = var2.startEncapsulation();
      this.writeEncapsulation(var2);
      var2.endEncapsulation(var3);
      byte[] var5 = var2.getBuffer();
      var2.close();
      var1.writeInt(var5.length);
      var1.write(var5);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      byte[] var3 = new byte[var2];
      var1.read(var3);
      IIOPInputStream var4 = new IIOPInputStream(var3);
      long var5 = var4.startEncapsulation();
      this.readEncapsulation(var4);
      var4.endEncapsulation(var5);
      var4.close();
   }

   public String toString() {
      return "VendorInfoCluster: version: " + Long.toHexString(this.version.getVersion()) + " " + (this.replicas == null ? 0 : this.replicas.size()) + " IORs";
   }

   public int size() {
      return this.replicas == null ? 0 : this.replicas.size();
   }

   public Object version() {
      return this.version;
   }

   public void add(RemoteReference var1) {
      if (var1.getHostID().equals(JVMID.localID())) {
         this.localRef = (IIOPRemoteRef)var1;
      }

      synchronized(this) {
         this.replicas.add(var1);
         if (var1.getHostID() instanceof JVMID) {
            this.version.addServer(var1.getHostID());
         }

      }
   }

   public RemoteReference get(int var1) {
      Debug.assertion(this.replicas != null && this.replicas.size() > 0);
      return (RemoteReference)this.replicas.get(var1);
   }

   public RemoteReference getPrimary() {
      return this.get(0);
   }

   public void clear() {
      this.replicas.clear();
      this.version = new Version(0L);
      this.clearHostToReplicaMap();
   }

   public void remove(RemoteReference var1) {
      Debug.assertion(var1 != null);
      synchronized(this) {
         this.replicas.remove(var1);
         if (this.hostToReplicaMap != null) {
            HostID var3 = var1.getHostID();
            this.hostToReplicaMap.remove(var3);
         }

         if (var1.getHostID() instanceof JVMID) {
            this.version.removeServer(var1.getHostID());
         }

      }
   }

   public RemoteReference removeOne(HostID var1) {
      synchronized(this) {
         ListIterator var3 = this.replicas.listIterator();

         RemoteReference var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (RemoteReference)var3.next();
         } while(!var4.getHostID().equals(var1));

         var3.remove();
         if (this.hostToReplicaMap != null) {
            this.hostToReplicaMap.remove(var1);
         }

         this.version.removeServer(var4.getHostID());
         return var4;
      }
   }

   protected Iterator iterator() {
      return this.replicas.iterator();
   }

   public Object[] toArray() {
      return this.replicas.toArray();
   }

   public RemoteReference findReplicaHostedBy(HostID var1) {
      RemoteReference var2 = null;
      synchronized(this) {
         if (this.hostToReplicaMap != null) {
            var2 = (RemoteReference)this.hostToReplicaMap.get(var1);
         }

         if (var2 == null) {
            for(Iterator var4 = this.replicas.iterator(); var4.hasNext(); var2 = null) {
               var2 = (RemoteReference)var4.next();
               if (var1.equals(var2.getHostID())) {
                  if (this.hostToReplicaMap == null) {
                     this.hostToReplicaMap = new HashMap(5);
                  }

                  this.hostToReplicaMap.put(var1, var2);
                  break;
               }
            }
         }
      }

      if (var2 != null) {
         Debug.assertion(var2 == null || var2.getHostID().equals(var1), "host ID of new replica (" + var2.getHostID() + ") " + "must equal " + var1);
      }

      return var2;
   }

   public void reset(ReplicaList var1) {
      if (var1 instanceof VendorInfoCluster) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("reset(" + var1 + ")");
         }

         VendorInfoCluster var2 = (VendorInfoCluster)var1;
         if (var2.replicas != null && var2.size() != 0) {
            synchronized(this) {
               Collections.shuffle(var2.replicas);
               this.replicas = var2.replicas;
               this.version = var2.version;
               this.clearHostToReplicaMap();
            }
         } else {
            throw new AssertionError("reset() called with null ReplicaList");
         }
      } else {
         throw new AssertionError("reset() called with foreign ReplicaList");
      }
   }

   public void resetWithoutShuffle(ReplicaList var1) {
      if (var1 instanceof VendorInfoCluster) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("resetWithoutShuffle(" + var1 + ")");
         }

         VendorInfoCluster var2 = (VendorInfoCluster)var1;
         if (var2.replicas != null && var2.size() != 0) {
            synchronized(this) {
               this.replicas = var2.replicas;
               this.version = var2.version;
               this.clearHostToReplicaMap();
            }
         } else {
            throw new AssertionError("reset() called with null ReplicaList");
         }
      } else {
         throw new AssertionError("reset() called with foreign ReplicaList");
      }
   }

   protected final void clearHostToReplicaMap() {
      if (this.hostToReplicaMap != null) {
         this.hostToReplicaMap.clear();
      }

   }

   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   public ReplicaList getListWithRefHostedBy(HostID var1) {
      return new VendorInfoCluster(this.localRef);
   }

   protected static void p(String var0) {
      System.err.println("<VendorInfoCluster> " + var0);
   }
}
