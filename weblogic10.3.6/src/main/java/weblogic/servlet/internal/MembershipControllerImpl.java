package weblogic.servlet.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.cluster.RemoteClusterMemberManager;
import weblogic.cluster.RemoteClusterMembersChangeListener;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerIdentity;
import weblogic.utils.encoders.BASE64Encoder;

public class MembershipControllerImpl implements ClusterMembersChangeListener, RemoteClusterMembersChangeListener, MembershipController {
   private final Map localClusterChannelMap;
   private final Map localClusterMap;
   private final Map clusterMap;
   private final BASE64Encoder base64;
   private MessageDigest sha;
   private String currentHash;

   public static MembershipController getInstance() {
      return MembershipControllerImpl.SingletonMaker.singleton;
   }

   private MembershipControllerImpl() {
      this.localClusterChannelMap = new ConcurrentHashMap();
      this.localClusterMap = new ConcurrentHashMap();
      this.clusterMap = new ConcurrentHashMap();
      this.base64 = new BASE64Encoder();

      try {
         this.sha = MessageDigest.getInstance("SHA");
      } catch (NoSuchAlgorithmException var4) {
         try {
            this.sha = MessageDigest.getInstance("MD5");
         } catch (NoSuchAlgorithmException var3) {
            throw new Error("JVM does not support SHA or MD5");
         }
      }

   }

   void startService() {
      ClusterServices var1 = ClusterService.getServices();
      var1.addClusterMembersListener(this);
      ServerIdentity var2 = LocalServerIdentity.getIdentity();
      this.localClusterMap.put(new Integer(var2.hashCode()), var2);
      this.clusterMap.put(new Integer(var2.hashCode()), var2);
      Iterator var3 = var1.getRemoteMembers().iterator();

      while(var3.hasNext()) {
         ClusterMemberInfo var4 = (ClusterMemberInfo)var3.next();
         ServerIdentity var5 = var4.identity();
         this.localClusterMap.put(new Integer(var5.hashCode()), var5);
         this.clusterMap.put(new Integer(var5.hashCode()), var5);
      }

      this.getHash();
      if (ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getCluster().getClusterType().equals("man")) {
         RemoteClusterMemberManager.getInstance().addRemoteClusterMemberListener(this);
      }

   }

   void stopService() {
      ClusterService.getServices().removeClusterMembersListener(this);
      if (ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getCluster().getClusterType().equals("man")) {
         RemoteClusterMemberManager.getInstance().addRemoteClusterMemberListener(this);
      }

   }

   private Map fabricateList(String var1) {
      ConcurrentHashMap var2 = new ConcurrentHashMap();
      ClusterServices var3 = ClusterService.getServices();
      if (var3 == null) {
         throw new AssertionError("ClusterService not initialized");
      } else {
         Iterator var4 = this.localClusterMap.values().iterator();

         while(var4.hasNext()) {
            ServerIdentity var5 = (ServerIdentity)var4.next();
            String var6 = ServerHelper.createServerEntry(var1, var5, "!");
            if (var6 != null) {
               var2.put(new Integer(var5.hashCode()), var6);
            }
         }

         return var2;
      }
   }

   private String calcHash() {
      this.sha.reset();
      ArrayList var1 = new ArrayList(this.localClusterMap.keySet());
      Collections.sort(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.sha.update(((Integer)var2.next()).toString().getBytes());
      }

      String var3 = this.base64.encodeBuffer(this.sha.digest());
      return var3.substring(0, var3.length() - 1);
   }

   public synchronized void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      this.addClusterMember(var1.getClusterMemberInfo(), var1.getAction(), true);
   }

   private void addClusterMember(ClusterMemberInfo var1, int var2, boolean var3) {
      ServerIdentity var4 = var1.identity();
      Integer var5 = new Integer(var4.hashCode());
      Iterator var6;
      switch (var2) {
         case 0:
         case 2:
            if (var3) {
               if (!this.localClusterChannelMap.isEmpty()) {
                  var6 = this.localClusterChannelMap.keySet().iterator();

                  while(var6.hasNext()) {
                     String var10 = (String)var6.next();
                     String var8 = ServerHelper.createServerEntry(var10, var4, "!");
                     if (var8 != null) {
                        Map var9 = (Map)this.localClusterChannelMap.get(var10);
                        var9.put(var5, var8);
                     }
                  }
               }

               this.localClusterMap.put(var5, var4);
            }

            this.clusterMap.put(var5, var4);
            this.currentHash = this.calcHash();
            break;
         case 1:
            if (var3) {
               this.localClusterMap.remove(var5);
               if (!this.localClusterChannelMap.isEmpty()) {
                  var6 = this.localClusterChannelMap.values().iterator();

                  while(var6.hasNext()) {
                     Map var7 = (Map)var6.next();
                     var7.remove(var5);
                  }
               }
            }

            this.clusterMap.remove(var5);
            this.currentHash = this.calcHash();
      }

   }

   public String getHash() {
      if (this.currentHash == null) {
         synchronized(this) {
            if (this.currentHash == null) {
               this.currentHash = this.calcHash();
            }
         }
      }

      return this.currentHash;
   }

   public String[] getClusterList(ServerChannel var1) {
      String var2 = var1.getChannelName();
      Map var3 = (Map)this.localClusterChannelMap.get(var2);
      if (var3 == null) {
         synchronized(this) {
            var3 = (Map)this.localClusterChannelMap.get(var2);
            if (var3 == null) {
               var3 = this.fabricateList(var2);
               this.localClusterChannelMap.put(var2, var3);
            }
         }
      }

      String[] var4 = null;
      synchronized(var3) {
         int var6 = var3.size();
         if (var6 < 1) {
            return null;
         } else {
            var4 = new String[var6];
            var3.values().toArray(var4);
            return var4;
         }
      }
   }

   public Map getClusterMembers() {
      return this.clusterMap;
   }

   public void remoteClusterMembersChanged(ArrayList var1) {
      int var2 = var1.size();
      if (var2 == 0) {
         this.clusterMap.clear();
         this.clusterMap.putAll(this.localClusterMap);
      } else {
         for(int var3 = 0; var3 < var2; ++var3) {
            ClusterMemberInfo var4 = (ClusterMemberInfo)var1.get(var3);
            this.addClusterMember(var4, 2, false);
         }
      }

   }

   // $FF: synthetic method
   MembershipControllerImpl(Object var1) {
      this();
   }

   private static final class SingletonMaker {
      private static final MembershipControllerImpl singleton = new MembershipControllerImpl();
   }
}
