package weblogic.diagnostics.snmp.server;

import java.util.HashSet;
import java.util.Set;
import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;

class ServerStateListener extends JMXMonitorListener {
   private static final String STATE_ATTRIBUTE = "State";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static Set shutDownStates = new HashSet();
   private String serverName;
   private boolean serverStarted;
   private boolean serverShutdown;

   private static void ensureShutdownStatesInitialized() {
      if (shutDownStates.isEmpty()) {
         shutDownStates.add("SHUTTING_DOWN");
         shutDownStates.add("FORCE_SHUTTING_DOWN");
         shutDownStates.add("SHUTDOWN");
         shutDownStates.add("UNKNOWN");
      }

   }

   public ServerStateListener(JMXMonitorLifecycle var1, String var2, SNMPAgent var3) {
      super(var1, var3);
      this.serverName = var2;
      ensureShutdownStatesInitialized();
   }

   public boolean isNotificationEnabled(Notification var1) {
      if (var1 instanceof AttributeChangeNotification) {
         AttributeChangeNotification var2 = (AttributeChangeNotification)var1;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Got AttributeChangeNotification from " + var2.getSource());
         }

         String var3 = var2.getAttributeName();
         if (var3.equals("State")) {
            return true;
         }
      }

      return false;
   }

   public void handleNotification(Notification var1, Object var2) {
      if (var1 instanceof AttributeChangeNotification) {
         AttributeChangeNotification var3 = (AttributeChangeNotification)var1;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Got AttributeChangeNotification from " + var3.getSource());
         }

         Object var4 = var3.getNewValue();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Server state = " + var4 + " for " + this.serverName);
         }

         if (var4.equals("RUNNING")) {
            try {
               this.monitorLifecycle.serverStarted(this.serverName);
               this.sendServerStartNotification();
            } catch (Exception var7) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Exception sending server start trap", var7);
               }
            }
         } else if (this.isShutdownState(var4)) {
            try {
               this.monitorLifecycle.serverStopped(this.serverName);
               this.serverServerShutdownNotification();
            } catch (Throwable var6) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Exception sending server start trap", var6);
               }
            }
         }
      }

   }

   void updateMonitorTrapCount() {
   }

   private boolean isShutdownState(Object var1) {
      return shutDownStates.contains(var1);
   }

   private void sendServerStartNotification() throws SNMPAgentToolkitException {
      if (!this.serverStarted) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Sending serverStart trap for " + this.serverName);
         }

         SNMPRuntimeStats var1 = this.getSNMPRuntimeStats();
         if (var1 != null) {
            var1.incrementServerStartTrapCount();
         }

         ServerStateTrapUtil.sendServerLifecycleNotification(this.snmpAgent, this.serverName, "wlsServerStart");
         this.serverStarted = true;
         this.serverShutdown = false;
      }

   }

   private void serverServerShutdownNotification() throws SNMPAgentToolkitException {
      if (!this.serverShutdown) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Sending serverStop trap for " + this.serverName);
         }

         SNMPRuntimeStats var1 = this.getSNMPRuntimeStats();
         if (var1 != null) {
            var1.incrementServerStopTrapCount();
         }

         ServerStateTrapUtil.sendServerLifecycleNotification(this.snmpAgent, this.serverName, "wlsServerShutDown");
         this.serverShutdown = true;
         this.serverStarted = false;
      }

   }
}
