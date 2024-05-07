package weblogic.cluster.messaging.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.collections.ConcurrentHashMap;

public class SRMResultImpl implements SRMResult {
   private static final AuthenticatedSubject kernelId;
   private static final DebugCategory debugSRMResult;
   private static final boolean DEBUG;
   private final HashMap serverStates;
   private final String leaderName;
   private int reachabilityCount;
   private final int expectedCount;
   private int machinesExpectedToReportStates;
   private final HashSet machinesReportingState = new HashSet();
   private ConcurrentHashMap serverMachineNameMap = new ConcurrentHashMap();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   SRMResultImpl() {
      this.leaderName = null;
      this.serverStates = new HashMap();
      this.reachabilityCount = 0;
      this.expectedCount = Integer.MAX_VALUE;
   }

   public SRMResultImpl(ServerInformation var1, int var2) {
      this.leaderName = var1 != null ? var1.getServerName() : null;
      this.serverStates = new HashMap();
      this.reachabilityCount = 0;
      this.expectedCount = var2;
   }

   public boolean hasReachabilityMajority() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      if ("UNKNOWN".equals(this.serverStates.get(var1))) {
         if (DEBUG) {
            debug("local server state is UNKNOWN ! Local NodeManager is not reporting correct state?!");
         }

         return false;
      } else {
         if (DEBUG) {
            debug("hasReachabilityMajority called. reachabilityCount=" + this.reachabilityCount + ", server states=" + this.serverStates.size());
         }

         if (2 * this.reachabilityCount > this.serverStates.size()) {
            if (DEBUG) {
               debug("hasReachabilityMajority returns true as majority is reachable");
            }

            return true;
         } else if (2 * this.reachabilityCount == this.serverStates.size() && this.leaderName != null && this.serverStates.get(this.leaderName) != null) {
            if (DEBUG) {
               debug("hasReachabilityMajority returns true as half the servers are reachable along with the leader");
            }

            return true;
         } else {
            if (DEBUG) {
               debug("hasReachabilityMajority returns FALSE !");
            }

            return false;
         }
      }
   }

   private static void debug(String var0) {
      DebugLogger.debug("[SRMResult] " + var0);
   }

   public String getServerState(String var1) {
      return (String)this.serverStates.get(var1);
   }

   public String getCurrentMachine(String var1) {
      return (String)this.serverMachineNameMap.get(var1);
   }

   public synchronized void blockTillCompletion() {
      if (!$assertionsDisabled && this.expectedCount == Integer.MAX_VALUE) {
         throw new AssertionError();
      } else {
         while(this.serverStates.size() < this.expectedCount || this.machinesReportingState.size() < this.machinesExpectedToReportStates) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }
   }

   public synchronized void doneReporting(String var1) {
      this.machinesReportingState.add(var1);
      if (this.serverStates.size() == this.expectedCount && this.machinesReportingState.size() == this.machinesExpectedToReportStates) {
         Iterator var2 = this.serverStates.values().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var3 != null) {
               ++this.reachabilityCount;
            }
         }

         this.notify();
      }

   }

   public synchronized void setServerState(String var1, String var2, String var3) {
      String var4 = (String)this.serverStates.get(var1);
      if (acceptState(var4, var3)) {
         this.serverStates.put(var1, var3);
         this.serverMachineNameMap.put(var1, var2);
         if (!$assertionsDisabled && this.serverStates.size() > this.expectedCount) {
            throw new AssertionError();
         }
      }
   }

   private static boolean acceptState(String var0, String var1) {
      if (var0 != null && !"UNKNOWN".equals(var0)) {
         return var1 != null && !"UNKNOWN".equals(var1) && !"FAILED_NOT_RESTARTABLE".equals(var1);
      } else {
         return true;
      }
   }

   public String toString() {
      return "SRMResult [" + this.serverStates + "]";
   }

   private static boolean debugEnabled() {
      return debugSRMResult.isEnabled() || DebugLogger.isDebugEnabled();
   }

   public void setMachinesExpectedToReportStates(int var1) {
      this.machinesExpectedToReportStates = var1;
   }

   public List getServersInState(String var1) {
      ArrayList var2 = new ArrayList();
      if (this.serverStates == null) {
         return var2;
      } else {
         Iterator var3 = this.serverStates.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getValue();
            if (var5 != null && var5.equals(var1)) {
               var2.add(var4.getKey());
            }
         }

         return var2;
      }
   }

   static {
      $assertionsDisabled = !SRMResultImpl.class.desiredAssertionStatus();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      debugSRMResult = Debug.getCategory("weblogic.cluster.leasing.SRMResult");
      DEBUG = debugEnabled();
   }
}
