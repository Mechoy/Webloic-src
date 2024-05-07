package weblogic.cluster.singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import weblogic.cluster.AnnouncementManager;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.cluster.UpgradeUtils;
import weblogic.cluster.migration.ScriptExecutor;
import weblogic.common.internal.PeerInfo;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;

public abstract class AbstractServiceLocationSelector implements ServiceLocationSelector {
   protected static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();

   protected List getAcceptableServers(List var1) {
      ArrayList var2 = new ArrayList();
      UpgradeUtils var3 = UpgradeUtils.getInstance();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         ServerMBean var5 = (ServerMBean)var4.next();
         if (var3.getServerVersion(var5.getName()) == null) {
            var2.add(var5);
         } else if (var3.getServerVersion(var5.getName()).compareTo(PeerInfo.VERSION_920) > -1) {
            var2.add(var5);
         }
      }

      return var2;
   }

   protected boolean isServerRunning(ServerMBean var1) {
      boolean var2 = false;
      String var3 = var1.getName();
      ClusterMemberInfo var4 = ClusterService.getClusterService().getLocalMember();
      if (var4 != null && var4.serverName().equals(var3) && !AnnouncementManager.theOne().isBlocked()) {
         return true;
      } else {
         Collection var5 = ClusterService.getClusterService().getRemoteMembers();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            ClusterMemberInfo var7 = (ClusterMemberInfo)var6.next();
            if (var7.serverName().equals(var3)) {
               ClusterMBean var8 = var1.getCluster();
               if ("consensus".equalsIgnoreCase(var8.getMigrationBasis())) {
                  String var9 = AbstractConsensusService.getInstance().getServerState(var3);
                  if (var9 == null || var9.equals("RESUMING") || var9.equals("RUNNING")) {
                     var2 = true;
                  }
               } else {
                  var2 = true;
               }
               break;
            }
         }

         return var2;
      }
   }

   protected boolean executePostScript(MigratableTargetMBean var1, ServerMBean var2, ServerMBean var3) {
      boolean var4 = true;
      if (var1.getPostScript() != null && var1.isPostScriptFailureFatal() && var2 != null) {
         boolean var5 = false;
         boolean var6 = false;
         if (var3 != null) {
            var6 = true;
            var5 = this.executePostScript(var1, var3);
            if (!var5 && DEBUG) {
               p("Failed to run the postscript on " + var3 + " for " + var1.getName());
            }
         }

         if (!var5 && var1.isNonLocalPostAllowed()) {
            var6 = true;
            var5 = this.executePostScript(var1, var2);
            if (!var5 && DEBUG) {
               p("Failed to run the postscript on " + var2 + " for " + var1.getName());
            }
         }

         if (var6 && !var5) {
            var4 = false;
         }
      }

      return var4;
   }

   protected boolean executePostScript(MigratableTargetMBean var1, ServerMBean var2) {
      if (DEBUG) {
         p("Going to execute the post script " + var1.getPostScript() + " for " + var1.getName() + " on " + var2);
      }

      return ScriptExecutor.runNMScript(var1.getPostScript(), var1, var2);
   }

   protected static void p(Object var0) {
      SingletonServicesDebugLogger.debug("ServiceLocationSelector: " + var0);
   }

   public void migrationSuccessful(ServerMBean var1, boolean var2) {
   }

   public abstract ServerMBean chooseServer();

   public abstract void setLastHost(ServerMBean var1);

   public abstract void setServerList(List var1);
}
