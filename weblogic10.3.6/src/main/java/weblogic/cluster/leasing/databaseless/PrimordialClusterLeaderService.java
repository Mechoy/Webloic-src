package weblogic.cluster.leasing.databaseless;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterService;
import weblogic.cluster.InboundService;
import weblogic.cluster.messaging.internal.ClusterMessageFactory;
import weblogic.cluster.messaging.internal.ClusterMessageSender;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.MachineState;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class PrimordialClusterLeaderService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = true;
   private static PrimordialClusterLeaderService THE_ONE;
   private boolean stopped;
   private boolean canQuery = false;
   private List machineList;
   private ServerInformation leader = null;

   public PrimordialClusterLeaderService() {
      synchronized(PrimordialClusterLeaderService.class) {
         if (THE_ONE != null) {
            throw new AssertionError("PrimordialClusterLeaderService cannot be initialized more than once !");
         } else {
            THE_ONE = this;
         }
      }
   }

   public static PrimordialClusterLeaderService getInstance() {
      return THE_ONE;
   }

   public synchronized void start() throws ServiceFailureException {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null && "consensus".equals(var1.getMigrationBasis()) && (isAutoServiceMigrationEnabled(var1) || isAutoMigratableCluster(var1))) {
         this.canQuery = true;
         this.machineList = this.createMachineList(var1);
         InboundService.startListening();
         long var2 = (long)ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis().getMemberDiscoveryTimeout();
         long var4 = System.currentTimeMillis() + var2;

         while(true) {
            try {
               this.debug("waiting for " + var2 + " seconds to sync ... ");
               this.wait(var2 * 1000L);
            } catch (InterruptedException var9) {
            }

            long var6 = System.currentTimeMillis();
            ServerInformation var8 = this.getLeaderInformation();
            if (var8 != null || var6 >= var4) {
               this.debug("done waiting to sync with cluster leader");
               return;
            }

            var2 = var4 - var6;
         }
      } else {
         this.debug("Not starting the PrimordialClusterLeaderService");
      }
   }

   public synchronized void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      if (var1.getAction() == 0) {
         this.notify();
      }

   }

   ServerInformation getLeaderInformation() {
      return this.getLeaderInformationInternal(false);
   }

   public void stop() {
      this.stopped = true;
   }

   ServerInformation getLeaderInformationInternal(boolean var1) {
      if (this.canQuery && (!this.stopped || var1)) {
         if (this.leader != null) {
            try {
               ServerInformation var2 = this.queryForLeader(this.leader);
               if (var2 != null) {
                  this.leader = var2;
                  return var2;
               }
            } catch (RemoteException var13) {
            }
         }

         Iterator var15 = ClusterService.getClusterService().getAllRemoteMembers().iterator();
         TreeSet var3 = new TreeSet();

         while(var15.hasNext()) {
            ClusterMemberInfo var4 = (ClusterMemberInfo)var15.next();
            ServerInformationImpl var5 = new ServerInformationImpl(var4);
            this.debug("discovered " + var5);
            var3.add(var5);
         }

         if (var3.size() > 0) {
            try {
               return this.queryForLeader((ServerInformation)var3.first());
            } catch (RemoteException var14) {
            }
         }

         if (var1) {
            Iterator var16 = this.machineList.iterator();

            while(var16.hasNext()) {
               MachineMBean var17 = (MachineMBean)var16.next();
               MachineState var6 = MachineState.getMachineState(var17, true);
               List var7 = var6.getServersInState("RUNNING");
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  ServerInformationImpl var10 = new ServerInformationImpl(var9);

                  try {
                     ServerInformation var11 = this.queryForLeader(var10);
                     if (var11 != null) {
                        this.leader = var11;
                        return var11;
                     }
                  } catch (RemoteException var12) {
                  }
               }
            }
         }

         return var3.size() > 0 ? (ServerInformation)var3.first() : null;
      } else {
         return null;
      }
   }

   private ServerInformation queryForLeader(ServerInformation var1) throws RemoteException {
      ClusterMessageSender var2 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
      LeaderQueryMessage var3 = LeaderQueryMessage.create(ClusterMember.getInstance().getLocalServerInformation());
      this.debug("requesting cluster leader " + var3 + " from " + var1);
      LeaderQueryResponse var4 = (LeaderQueryResponse)var2.send(var3, (ServerInformation)var1);
      this.debug("received response to the cluster leader query  " + var4);
      return var4.getLeaderInformation();
   }

   private List createMachineList(ClusterMBean var1) {
      ServerMBean[] var2 = var1.getServers();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         ServerMBean var5 = var2[var4];
         MachineMBean var6 = var5.getMachine();
         if (!var3.contains(var6)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   private static boolean isAutoServiceMigrationEnabled(ClusterMBean var0) {
      MigratableTargetMBean[] var1 = var0.getMigratableTargets();
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!"manual".equals(var1[var2].getMigrationPolicy())) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean isAutoMigratableCluster(ClusterMBean var0) {
      if (var0 == null) {
         return false;
      } else {
         ServerMBean[] var1 = var0.getServers();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].isAutoMigrationEnabled()) {
               return true;
            }
         }

         return false;
      }
   }

   private void debug(String var1) {
      DebugLogger.debug("[PrimordialClusterLeaderService] " + var1);
   }
}
