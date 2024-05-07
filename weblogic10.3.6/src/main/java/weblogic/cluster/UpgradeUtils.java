package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.work.WorkManagerFactory;

public final class UpgradeUtils implements ClusterMembersChangeListener {
   private static UpgradeUtils THE_ONE;
   private Map peerInfoMap;
   private PeerInfo clusterVersion = this.getClusterVersion();
   private PeerInfoComparator peerInfoComparator;

   private UpgradeUtils() {
      if (ClusterDebugLogger.isDebugEnabled()) {
         ClusterDebugLogger.debug("[UPGRADE] startup peer info=" + this.clusterVersion);
      }

      this.peerInfoComparator = new PeerInfoComparator();
      this.peerInfoMap = new HashMap();
      this.peerInfoMap.put("_local_", VersionInfoFactory.getPeerInfo());
      if (KernelStatus.isServer()) {
         MemberManager.theOne().addClusterMembersListener(this);
      }

   }

   public static synchronized UpgradeUtils getInstance() {
      if (THE_ONE != null) {
         return THE_ONE;
      } else {
         THE_ONE = new UpgradeUtils();
         return THE_ONE;
      }
   }

   private synchronized PeerInfo getClusterVersion() {
      if (this.clusterVersion == null) {
         this.clusterVersion = VersionInfoFactory.getPeerInfo();
      }

      if (ClusterDebugLogger.isDebugEnabled()) {
         ClusterDebugLogger.debug("[UPGRADE] cluster version string=" + this.clusterVersion);
      }

      return this.clusterVersion;
   }

   public synchronized PeerInfo getServerVersion(String var1) {
      return (PeerInfo)this.peerInfoMap.get(var1);
   }

   public synchronized PeerInfo getNewestServerVersion() {
      return (PeerInfo)Collections.max(this.peerInfoMap.values(), this.peerInfoComparator);
   }

   public synchronized void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      if (var1.getAction() == 0) {
         if (this.peerInfoMap.get(var1.getClusterMemberInfo().serverName()) == null) {
            if (ClusterDebugLogger.isDebugEnabled()) {
               ClusterDebugLogger.debug("[UPGRADE] cluster member added=" + var1.getClusterMemberInfo().peerInfo() + ", serverName=" + var1.getClusterMemberInfo().serverName());
            }

            this.peerInfoMap.put(var1.getClusterMemberInfo().serverName(), var1.getClusterMemberInfo().peerInfo());
         }
      } else if (var1.getAction() == 1 && this.peerInfoMap.remove(var1.getClusterMemberInfo().serverName()) != null && ClusterDebugLogger.isDebugEnabled()) {
         ClusterDebugLogger.debug("[UPGRADE] cluster member removed=" + var1.getClusterMemberInfo().peerInfo() + ", serverName=" + var1.getClusterMemberInfo().serverName());
      }

      this.clusterVersion = (PeerInfo)Collections.min(this.peerInfoMap.values(), this.peerInfoComparator);
      if (ClusterDebugLogger.isDebugEnabled()) {
         ClusterDebugLogger.debug("[UPGRADE] new cluster version=" + this.clusterVersion);
      }

   }

   synchronized Replacer getInteropReplacer() {
      return RemoteObjectReplacer.getReplacer(this.clusterVersion);
   }

   public void test() {
      PeerInfo var1 = this.clusterVersion;
      this.clusterVersion = VersionInfoFactory.getPeerInfo("9.0.2.0");
      this.rewriteServiceOffersAtNewVersion(var1, this.clusterVersion);
   }

   static boolean needsRewrite(Object var0, PeerInfo var1, PeerInfo var2) throws IOException {
      if (!(var0 instanceof Externalizable) && !(var0 instanceof InteropWriteReplaceable)) {
         return false;
      } else {
         UnsyncByteArrayOutputStream var3 = null;
         UnsyncByteArrayOutputStream var4 = null;
         PeerInfoableObjectOutput var5 = null;

         boolean var6;
         try {
            var3 = new UnsyncByteArrayOutputStream();
            var5 = new PeerInfoableObjectOutput(var3, var1);
            var5.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
            var5.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
            var5.writeObjectWL(var0);
            var5.flush();
            var5.close();
            var4 = new UnsyncByteArrayOutputStream();
            var5 = new PeerInfoableObjectOutput(var4, var2);
            var5.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
            var5.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
            var5.writeObjectWL(var0);
            var5.flush();
            var6 = !Arrays.equals(var3.toRawBytes(), var4.toRawBytes());
         } finally {
            if (var3 != null) {
               var3.close();
            }

            if (var4 != null) {
               var4.close();
            }

            if (var5 != null) {
               var5.close();
            }

         }

         return var6;
      }
   }

   private void rewriteServiceOffersAtNewVersion(final PeerInfo var1, final PeerInfo var2) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            ServiceAdvertiser.theOne().rewriteServicesAtNewVersion(var1, var2);
         }
      });
   }

   synchronized WLObjectOutputStream getOutputStream(UnsyncByteArrayOutputStream var1, ServerChannel var2) throws IOException {
      return this.getOutputStream(var1, var2, this.clusterVersion);
   }

   synchronized WLObjectOutputStream getOutputStream(UnsyncByteArrayOutputStream var1, ServerChannel var2, PeerInfo var3) throws IOException {
      PeerInfoableObjectOutput var4 = new PeerInfoableObjectOutput(var1, var3);
      var4.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
      if (var2 != null) {
         var4.setServerChannel(var2);
      }

      return var4;
   }

   public boolean acceptVersion(String var1) {
      PeerInfo var2 = VersionInfoFactory.getPeerInfo(var1);
      if (ClusterDebugLogger.isDebugEnabled()) {
         ClusterDebugLogger.debug("[UPGRADE] comparing [" + VersionInfoFactory.getPeerInfo() + "] with remote version [" + var2 + "] with result=" + this.peerInfoComparator.compare(VersionInfoFactory.getPeerInfo(), var2));
      }

      return this.peerInfoComparator.compare(VersionInfoFactory.getPeerInfo(), var2) >= 0;
   }

   public String getLocalServerVersion() {
      return ClusterHelper.STRINGFIED_PEERINFO;
   }

   private static String stringfyPeerInfo(PeerInfo var0) {
      return var0.getMajor() + "," + var0.getMinor() + "," + var0.getServicePack();
   }

   private static class PeerInfoComparator implements Comparator {
      private PeerInfoComparator() {
      }

      public int compare(Object var1, Object var2) {
         PeerInfo var3 = (PeerInfo)var1;
         PeerInfo var4 = (PeerInfo)var2;
         int var5 = var3.getMajor() - var4.getMajor();
         if (var5 != 0) {
            return var5;
         } else {
            var5 = var3.getMinor() - var4.getMinor();
            if (var5 != 0) {
               return var5;
            } else {
               var5 = var3.getServicePack() - var4.getServicePack();
               return var5 != 0 ? var5 : 0;
            }
         }
      }

      // $FF: synthetic method
      PeerInfoComparator(Object var1) {
         this();
      }
   }

   static final class PeerInfoableObjectOutput extends WLObjectOutputStream implements PeerInfoable {
      private PeerInfo peerInfo;

      PeerInfoableObjectOutput(UnsyncByteArrayOutputStream var1, PeerInfo var2) throws IOException {
         super((OutputStream)var1);
         this.peerInfo = var2;
      }

      public PeerInfo getPeerInfo() {
         return this.peerInfo;
      }

      public String getClusterVersion() {
         return UpgradeUtils.stringfyPeerInfo(this.peerInfo);
      }
   }
}
