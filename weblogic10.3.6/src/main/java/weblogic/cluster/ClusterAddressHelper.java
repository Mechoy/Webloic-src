package weblogic.cluster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import weblogic.management.configuration.ClusterMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.utils.UnsyncStringBuffer;
import weblogic.utils.net.InetAddressHelper;

public class ClusterAddressHelper implements ClusterMembersChangeListener {
   private static boolean debug = false;
   private static final ClusterAddressHelper singleton = new ClusterAddressHelper();
   private boolean isInitialized = false;
   private boolean needsDynamicConstruction = false;
   private boolean needToAppendPort = false;
   private int membersInList = 3;
   private List members = Collections.synchronizedList(new ArrayList());
   private String clusterAddressConfigured;

   public static ClusterAddressHelper getInstance() {
      return singleton;
   }

   public static String getClusterAddressURL(ServerChannel var0) {
      return var0.getProtocolPrefix() + "://" + getInstance().getClusterAddress(var0);
   }

   private ClusterAddressHelper() {
   }

   final void initialize(ClusterMBean var1) {
      this.clusterAddressConfigured = var1.getClusterAddress();
      this.membersInList = var1.getNumberOfServersInClusterAddress();
      if (this.clusterAddressConfigured != null && this.clusterAddressConfigured.length() != 0) {
         this.verifyClusterAddress(this.clusterAddressConfigured);
      } else {
         this.needsDynamicConstruction = true;
      }

      if (this.needsDynamicConstruction) {
         this.debugSay(" adding " + this + " as ClusterMembersChangeListener.");
         ClusterService.getClusterService().addClusterMembersListener(this);
         this.members.add(LocalServerIdentity.getIdentity());
      }

      this.isInitialized = true;
      this.debugSay(" Initialized ClusterAddressHelper with clusterAddressConfigured to= " + this.clusterAddressConfigured + " : membersInList= " + this.membersInList);
   }

   private final String getClusterAddress(ServerChannel var1) {
      if (!this.isInitialized) {
         return var1.getPublicAddress() + ':' + var1.getPublicPort();
      } else if (this.needsDynamicConstruction) {
         return this.constructClusterAddress(var1);
      } else {
         return !this.needToAppendPort ? this.clusterAddressConfigured : this.clusterAddressConfigured + ':' + var1.getPublicPort();
      }
   }

   public final void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      ClusterMemberInfo var2 = var1.getClusterMemberInfo();
      int var3 = var1.getAction();
      ServerIdentity var4 = var2.identity();
      if (var3 == 1) {
         this.debugSay(" Removing a member to the list : " + var4);
         this.members.remove(var4);
         this.dumpList(" +++ clusterMembersChanged() : ", this.members);
      } else if (var3 == 0) {
         this.debugSay(" Adding a member to the list : " + var4);
         this.members.add(var4);
         this.dumpList(" +++ clusterMembersChanged() : ", this.members);
      }

   }

   private final String constructClusterAddress(ServerChannel var1) {
      List var2 = this.selectServerListRandomly();
      List var3 = this.getHostPortInfo(var2, var1);
      HostPortInfo[] var4 = new HostPortInfo[var3.size()];
      var4 = (HostPortInfo[])((HostPortInfo[])var3.toArray(var4));
      boolean var5 = this.hasMixedPorts(var4);
      UnsyncStringBuffer var6 = new UnsyncStringBuffer();

      for(int var7 = 0; var7 < var4.length; ++var7) {
         if (var7 != 0) {
            var6.append(",");
         }

         if (var5) {
            var6.append(var4[var7].getHostPort());
         } else {
            var6.append(var4[var7].getHost());
         }
      }

      if (!var5) {
         var6.append(":").append(var1.getPublicPort()).toString();
      }

      this.debugSay(" +++ constructClusterAddress() : " + var6.toString());
      return var6.toString();
   }

   private boolean needToAppendPort() {
      return this.needToAppendPort;
   }

   private boolean hasMixedPorts(HostPortInfo[] var1) {
      if (var1.length == 1) {
         this.needToAppendPort = true;
         return false;
      } else {
         int var2 = var1[0].getPort();

         for(int var3 = 1; var3 < var1.length; ++var3) {
            if (var2 != var1[var3].getPort()) {
               this.needToAppendPort = false;
               return true;
            }
         }

         this.needToAppendPort = true;
         return false;
      }
   }

   private List getHostPortInfo(List var1, ServerChannel var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         ServerIdentity var7 = (ServerIdentity)var4.next();
         String var5;
         int var6;
         if (var7.isLocal()) {
            this.debugSay(" +++ got local server channel : " + var2);
            var5 = var2.getPublicAddress();
            var6 = var2.getPublicPort();
         } else {
            ServerChannel var8 = ServerChannelManager.findServerChannel(var7, var2.getProtocol(), var2.getChannelName());
            this.debugSay(" +++ got server channel : " + var8);
            if (var8 == null) {
               var8 = ServerChannelManager.findServerChannel(var7, var2.getProtocol());
               this.debugSay(" +++ got server channel[1] : " + var8);
               if (var8 == null) {
                  var8 = ServerChannelManager.findServerChannel(var7);
                  this.debugSay(" +++ got server channel[2] : " + var8);
                  if (var8 == null) {
                     throw new AssertionError("ServerChannel for id : " + var7 + " is null");
                  }
               }
            }

            var5 = var8.getPublicAddress();
            var6 = var8.getPublicPort();
         }

         this.debugSay(" +++ eachHost, eachPort : " + var5 + "," + var6 + " : for id --- " + var7);
         var3.add(this.getHostPortInfo(var5, var6, var6 != -1));
      }

      this.dumpList(" +++ getHostPortInfo() : ", (List)var3);
      return var3;
   }

   private List selectServerListRandomly() {
      this.debugSay(" Selecting Server List Randomly...");
      ServerIdentity[] var1 = new ServerIdentity[this.members.size()];
      var1 = (ServerIdentity[])((ServerIdentity[])this.members.toArray(var1));
      Random var2 = new Random(System.currentTimeMillis());
      int var3 = var2.nextInt(var1.length);
      int var4 = var1.length <= this.membersInList ? var1.length : this.membersInList;
      ArrayList var5 = new ArrayList();
      int var6 = 0;

      for(int var7 = var3; var6 < var4; ++var6) {
         if (var7 >= var1.length) {
            var7 = 0;
         }

         var5.add(var1[var7]);
         ++var7;
      }

      this.dumpList(" +++ selectServerListRandomly() :", (List)var5);
      return var5;
   }

   private void verifyClusterAddress(String var1) {
      if (var1 != null && var1.length() != 0) {
         byte var2 = 0;
         String[] var3 = StringUtils.splitCompletely(var1, ",", false);
         if (var3.length > 1) {
            try {
               List var4 = this.constructHostPortInfos(var1);
               boolean var5 = this.allHasPorts(var4);
               boolean var6 = this.allHasNoPorts(var4);
               if (!var5 && !var6) {
                  this.needsDynamicConstruction = true;
                  return;
               }

               if (var5) {
                  this.needToAppendPort = false;
                  return;
               }

               if (var6) {
                  this.needToAppendPort = true;
                  return;
               }
            } catch (NumberFormatException var7) {
               ClusterLogger.logInvalidConfiguredClusterAddress(var1);
               this.needsDynamicConstruction = true;
            }
         } else {
            try {
               InetAddress.getByName(var1);
               this.needToAppendPort = true;
            } catch (UnknownHostException var8) {
               if (var3.length > 1) {
                  ClusterLogger.logCannotResolveClusterAddressWarning(var1 + ": Unknown host: " + var3[var2]);
               } else {
                  ClusterLogger.logCannotResolveClusterAddressWarning(var1);
               }

               this.needsDynamicConstruction = true;
            }
         }
      }

   }

   List constructHostPortInfos(String var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;
      String[] var4 = StringUtils.splitCompletely(var1, ",", false);
      if (var4.length <= 1) {
         throw new AssertionError("ClusterAddress has dns name when it isalready verified as non dns name");
      } else {
         while(var3 < var4.length) {
            HostPortInfo var5 = this.constructHostPortInfo(var4[var3]);
            var2.add(var5);
            ++var3;
         }

         return var2;
      }
   }

   private HostPortInfo constructHostPortInfo(String var1) {
      this.debugSay(" nodeAddressPort is " + var1);
      byte var2 = 58;
      int var3 = -1;
      int var4 = -1;
      String var5 = var1;
      boolean var6 = false;
      int var7 = var1.indexOf(91);
      int var8 = var1.indexOf(93);
      if (var7 > -1 && var8 > 0) {
         var4 = var1.indexOf(var2, var8);
         var6 = true;
      } else if (var7 == -1 && var8 == -1) {
         if (InetAddressHelper.isIPV6Address(var1)) {
            var4 = var1.lastIndexOf(var2);
            var6 = true;
         } else {
            var4 = var1.indexOf(var2);
         }
      }

      this.debugSay(" portIndex is " + var4);
      if (var4 > -1) {
         var5 = var1.substring(0, var4);
         if (var4 + 1 != var1.length()) {
            String var10 = var1.substring(var4 + 1);

            try {
               var3 = Integer.valueOf(var10);
            } catch (NumberFormatException var12) {
               NumberFormatException var9 = var12;
               if (var6) {
                  var9 = new NumberFormatException("IPV6 addresses have to be specified in [a:b:c:d:e:f:g]:port format");
               }

               throw var9;
            }
         }
      }

      return this.getHostPortInfo(var5, var3, var3 > 0);
   }

   private HostPortInfo getHostPortInfo(final String var1, final int var2, final boolean var3) {
      return new HostPortInfo() {
         public String getHost() {
            return var1;
         }

         public int getPort() {
            return var2;
         }

         public boolean hasPort() {
            return var3;
         }

         public String getHostPort() {
            return var1 + ":" + var2;
         }

         public String toString() {
            return "HostPortInfo(" + this.getHost() + "," + this.getPort() + "," + this.hasPort() + ")";
         }
      };
   }

   private boolean allHasPorts(List var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(((HostPortInfo)var2.next()).hasPort());

      return false;
   }

   private boolean allHasNoPorts(List var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(!((HostPortInfo)var2.next()).hasPort());

      return false;
   }

   private void debugSay(String var1) {
      if (debug) {
         Debug.say(" --- " + var1);
      }

   }

   private void dumpList(String var1, List var2) {
      if (debug) {
         StringBuffer var3 = new StringBuffer();
         if (var1 != null) {
            var3.append(var1);
         }

         Iterator var4 = var2.iterator();
         var3.append("List [");

         while(var4.hasNext()) {
            var3.append(var4.next().toString()).append(",");
         }

         String var5 = var3.toString().substring(0, var3.toString().lastIndexOf(44));
         var5 = var5 + "]";
         this.debugSay(var5);
      }
   }

   private void dumpList(String var1, Object[] var2) {
      if (debug) {
         StringBuffer var3 = new StringBuffer();
         if (var1 != null) {
            var3.append(var1);
         }

         var3.append(" [");

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var4 != 0) {
               var3.append(",");
            }

            var3.append(var2[var4].toString());
         }

         var3.append("]");
         this.debugSay(var3.toString());
      }
   }

   private interface HostPortInfo {
      String getHost();

      int getPort();

      boolean hasPort();

      String getHostPort();
   }
}
