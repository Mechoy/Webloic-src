package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.cluster.ServerInfoManager;
import weblogic.server.channels.ChannelService;

final class MemberAttributes implements ClusterMemberInfo, Externalizable {
   private static final long serialVersionUID = -2297055762635480163L;
   private ServerIdentity jvmid;
   private String hostAddress;
   private String machineName;
   private String version;
   private long joinTime;
   private int loadWeight;
   private String replicationGroup;
   private String preferredSecondaryGroup;
   private String clusterName;
   private boolean isMigratableServer;
   private String replicationChannel;
   private PeerInfo info;

   public MemberAttributes() {
   }

   public MemberAttributes(String var1, String var2, String var3, long var4, int var6, String var7, String var8, String var9, boolean var10, String var11, PeerInfo var12) {
      this.jvmid = LocalServerIdentity.getIdentity();
      this.hostAddress = var1 == null ? "" : var1;
      this.machineName = var2;
      this.version = var3;
      this.joinTime = var4;
      this.loadWeight = var6;
      this.replicationGroup = var7 == null ? "" : var7;
      this.preferredSecondaryGroup = var8 == null ? "" : var8;
      this.clusterName = var9;
      this.isMigratableServer = var10;
      this.replicationChannel = var11 == null ? "" : var11;
      this.info = PeerInfo.getPeerInfoForWire();
   }

   public String toString() {
      return this.jvmid.getServerName() + " jvmid:" + this.jvmid;
   }

   public int hashCode() {
      return this.jvmid.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof MemberAttributes)) {
         return false;
      } else {
         MemberAttributes var2 = (MemberAttributes)var1;
         return this.jvmid.equals(var2.jvmid) && this.serverName().equals(var2.serverName());
      }
   }

   public ServerIdentity identity() {
      return this.jvmid;
   }

   public String serverName() {
      return this.jvmid.getServerName();
   }

   public String hostAddress() {
      return this.hostAddress;
   }

   public String machineName() {
      return this.machineName;
   }

   public String version() {
      return this.version;
   }

   public long joinTime() {
      return this.joinTime;
   }

   public int loadWeight() {
      return this.loadWeight;
   }

   public String replicationGroup() {
      return this.replicationGroup;
   }

   public String preferredSecondaryGroup() {
      return this.preferredSecondaryGroup;
   }

   public String domainName() {
      return this.jvmid.getDomainName();
   }

   public String clusterName() {
      return this.clusterName;
   }

   public boolean isMigratableServer() {
      return this.isMigratableServer;
   }

   public String replicationChannel() {
      return this.replicationChannel;
   }

   public PeerInfo peerInfo() {
      return this.info;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.info);
      var1.writeObject(this.jvmid);
      var1.writeUTF(this.hostAddress);
      var1.writeUTF(this.machineName);
      var1.writeUTF(this.version);
      var1.writeLong(this.joinTime);
      var1.writeObject(ServerInfoManager.theOne().writeLocalInfoUpdate());
      var1.writeInt(this.loadWeight);
      var1.writeUTF(this.replicationGroup);
      var1.writeUTF(this.preferredSecondaryGroup);
      var1.writeUTF(this.clusterName);
      var1.writeBoolean(this.isMigratableServer);
      var1.writeUTF(this.replicationChannel);
      ChannelService.exportServerChannels(this.jvmid, var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.info = (PeerInfo)var1.readObject();
      this.jvmid = (ServerIdentity)var1.readObject();
      this.hostAddress = var1.readUTF();
      this.machineName = var1.readUTF();
      this.version = var1.readUTF();
      this.joinTime = var1.readLong();
      ServerInfoManager.theOne().readUpdate(var1.readObject());
      this.loadWeight = var1.readInt();
      this.replicationGroup = var1.readUTF();
      this.preferredSecondaryGroup = var1.readUTF();
      this.clusterName = var1.readUTF();
      this.isMigratableServer = var1.readBoolean();
      this.replicationChannel = var1.readUTF();
      ChannelService.importServerChannels(this.jvmid, var1);
   }
}
