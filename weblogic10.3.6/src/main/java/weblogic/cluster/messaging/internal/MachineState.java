package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.NumericValueHashMap;

public final class MachineState implements Runnable {
   private static final DebugCategory debugMachineState;
   private static final boolean DEBUG;
   public static final boolean USE_NM_CONNECTION_TIMEOUT = true;
   static final boolean IGNORE_NM_CONNECTION_TIMEOUT = false;
   private static final int TIMEOUT_MULTIPLIER = 15;
   private static final AuthenticatedSubject kernelId;
   private static NumericValueHashMap machineRoundTripTimes;
   private final ArrayList configuredServerNamesInMachine = new ArrayList();
   private final HashSet configuredServerNamesInCluster = new HashSet();
   private final NodeManagerRuntime nodeManagerRuntime;
   private final SRMResultImpl result;
   private final MachineMBean machine;
   private boolean machineUnavailable;
   private boolean useConnectionTimeout;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   MachineState(MachineMBean var1, SRMResultImpl var2, boolean var3) {
      this.nodeManagerRuntime = NodeManagerRuntime.getInstance(var1);
      this.result = var2;
      this.machine = var1;
      this.useConnectionTimeout = var3;
      RuntimeAccess var4 = ManagementService.getRuntimeAccess(kernelId);
      ClusterMBean var5 = var4.getServer().getCluster();
      if (!$assertionsDisabled && var5 == null) {
         throw new AssertionError();
      } else {
         ServerMBean[] var6 = var5.getServers();
         if (!$assertionsDisabled && var6 == null) {
            throw new AssertionError();
         } else {
            for(int var7 = 0; var7 < var6.length; ++var7) {
               this.configuredServerNamesInCluster.add(var6[var7].getName());
               MachineMBean var8 = var6[var7].getMachine();
               if (var8.getName().equals(var1.getName())) {
                  this.configuredServerNamesInMachine.add(var6[var7].getName());
               }
            }

         }
      }
   }

   public static MachineState getMachineState(MachineMBean var0, boolean var1) {
      MachineState var2 = new MachineState(var0, new SRMResultImpl(), var1);
      var2.run();
      return var2;
   }

   public void run() {
      try {
         long var1 = System.currentTimeMillis();
         if (DEBUG) {
            debug("Invoking NodeManager.getStates() with timeout of " + this.getTimeout(this.machine) + "ms");
         }

         String var3 = this.nodeManagerRuntime.getStates(true, this.getTimeout(this.machine));
         recordExecutionTime(var1, System.currentTimeMillis(), this.machine);
         if (DEBUG) {
            debug("consolidated states for machine " + this.machine.getName() + " returned by NM " + var3);
         }

         if (var3 != null && var3.length() != 0) {
            String[] var4 = StringUtils.split(var3, '=');

            ArrayList var5;
            String var7;
            for(var5 = new ArrayList(); var4 != null && var4.length >= 2 && var4[1].trim().length() != 0; var4 = StringUtils.split(var4[1], '=')) {
               String var6 = var4[0].trim();
               var4 = StringUtils.split(var4[1], ' ');
               if (var4 == null || var4.length < 2) {
                  break;
               }

               var7 = var4[0].trim();
               if (this.configuredServerNamesInCluster.contains(var6) && var7.length() > 0) {
                  if (DEBUG) {
                     debug("Server state for " + var6 + " is " + var7);
                  }

                  this.result.setServerState(var6, this.machine.getName(), var7);
                  if (this.configuredServerNamesInMachine.contains(var6)) {
                     var5.add(var6);
                  }
               }
            }

            if (var5.size() < this.configuredServerNamesInMachine.size()) {
               Iterator var13 = this.configuredServerNamesInMachine.iterator();

               while(var13.hasNext()) {
                  var7 = (String)var13.next();
                  if (!var5.contains(var7)) {
                     if (DEBUG) {
                        debug("Server state for " + var7 + " is " + " set to UNKNOWN as the NM.getStates() did not return a result");
                     }

                     this.result.setServerState(var7, this.machine.getName(), "UNKNOWN");
                  }
               }
            }

            return;
         }

         this.nullifyStates();
      } catch (IOException var11) {
         this.nullifyStates();
         return;
      } finally {
         this.result.doneReporting(this.machine.getName());
      }

   }

   private static void recordExecutionTime(long var0, long var2, MachineMBean var4) {
      int var5 = (int)machineRoundTripTimes.get(var4.getName());
      int var6 = (int)(var2 - var0);
      if (var6 > var5) {
         machineRoundTripTimes.put(var4.getName(), (long)var6);
      }

   }

   private int getTimeout(MachineMBean var1) {
      if (!this.useConnectionTimeout) {
         return 0;
      } else {
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
         ClusterMBean var3 = var2.getServer().getCluster();
         int var4 = var3.getDatabaseLessLeasingBasis().getNodeManagerTimeoutMillis();
         if (var4 == 0) {
            return 0;
         } else {
            int var5 = (int)(15L * machineRoundTripTimes.get(var1.getName()));
            return Math.min(var4, var5);
         }
      }
   }

   public String getServerState(String var1) {
      return this.result.getServerState(var1);
   }

   public List getServersInState(String var1) {
      return this.result.getServersInState(var1);
   }

   public boolean isMachineUnavailable() {
      return this.machineUnavailable;
   }

   public List getServerNames() {
      return this.configuredServerNamesInMachine;
   }

   private static void debug(String var0) {
      DebugLogger.debug("[MachineState] " + var0);
   }

   void nullifyStates() {
      for(int var1 = 0; var1 < this.configuredServerNamesInMachine.size(); ++var1) {
         this.result.setServerState((String)this.configuredServerNamesInMachine.get(var1), this.machine.getName(), (String)null);
      }

      this.machineUnavailable = true;
   }

   public String getMachineName() {
      return this.machine.getName();
   }

   public String toString() {
      return "MachineState for " + this.machine.getName() + " is " + this.result + " with machineUnavailable=" + this.machineUnavailable;
   }

   private static boolean debugEnabled() {
      return debugMachineState.isEnabled() || DebugLogger.isDebugEnabled();
   }

   static {
      $assertionsDisabled = !MachineState.class.desiredAssertionStatus();
      debugMachineState = Debug.getCategory("weblogic.cluster.leasing.MachineState");
      DEBUG = debugEnabled();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      machineRoundTripTimes = new NumericValueHashMap();
   }
}
