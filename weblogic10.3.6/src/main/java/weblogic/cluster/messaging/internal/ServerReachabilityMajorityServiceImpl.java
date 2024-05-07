package weblogic.cluster.messaging.internal;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkManagerFactory;

public class ServerReachabilityMajorityServiceImpl implements ServerReachabilityMajorityService {
   private static final DebugCategory debugSRM;
   private static final boolean DEBUG;
   private SRMResult lastResult;
   private static final AuthenticatedSubject kernelId;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public SRMResult performSRMCheck(ServerInformation var1, String var2, String var3, boolean var4) {
      ClusterMBean var5 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupCluster(var2);
      if (!$assertionsDisabled && var5 == null) {
         throw new AssertionError();
      } else {
         ServerMBean[] var6 = var5.getServers();
         if (!$assertionsDisabled && var6 == null) {
            throw new AssertionError();
         } else {
            if (DEBUG) {
               debug("There are " + var6.length + " in the cluster");
            }

            SRMResultImpl var7 = new SRMResultImpl(var1, var6.length);
            HashMap var8 = new HashMap();

            for(int var9 = 0; var9 < var6.length; ++var9) {
               MachineMBean var10 = var6[var9].getMachine();
               MachineState var11 = (MachineState)var8.get(var10.getName());
               if (var11 == null) {
                  if (DEBUG) {
                     debug("created new MachineState for " + var10.getName());
                  }

                  var11 = new MachineState(var10, var7, var4);
                  var8.put(var10.getName(), var11);
               }
            }

            var7.setMachinesExpectedToReportStates(var8.size());
            if (DEBUG) {
               debug("machine state map has " + var8.size() + " entries");
            }

            MachineState var12;
            for(Iterator var13 = var8.values().iterator(); var13.hasNext(); WorkManagerFactory.getInstance().getSystem().schedule(var12)) {
               var12 = (MachineState)var13.next();
               if (var12.getMachineName().equals(var3)) {
                  var12.nullifyStates();
               }
            }

            var7.blockTillCompletion();
            this.lastResult = var7;
            return var7;
         }
      }
   }

   public SRMResult getLastSRMResult() {
      return this.lastResult;
   }

   public SRMResult performSRMCheck(ServerInformation var1, String var2) {
      return this.performSRMCheck(var1, var2, (String)null, true);
   }

   public SRMResult performSRMCheck(ServerInformation var1, String var2, String var3) {
      return this.performSRMCheck(var1, var2, var3, true);
   }

   private static void debug(String var0) {
      DebugLogger.debug("[SRMService] " + var0);
   }

   public static ServerReachabilityMajorityService getInstance() {
      return ServerReachabilityMajorityServiceImpl.Factory.THE_ONE;
   }

   private static boolean debugEnabled() {
      return debugSRM.isEnabled() || DebugLogger.isDebugEnabled();
   }

   static {
      $assertionsDisabled = !ServerReachabilityMajorityServiceImpl.class.desiredAssertionStatus();
      debugSRM = Debug.getCategory("weblogic.cluster.leasing.SRMService");
      DEBUG = debugEnabled();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static final class Factory {
      static final ServerReachabilityMajorityServiceImpl THE_ONE = new ServerReachabilityMajorityServiceImpl();
   }
}
