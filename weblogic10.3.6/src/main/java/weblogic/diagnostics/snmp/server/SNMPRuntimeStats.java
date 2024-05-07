package weblogic.diagnostics.snmp.server;

import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPSecurityManager;
import weblogic.diagnostics.snmp.agent.SNMPSubAgentX;
import weblogic.diagnostics.snmp.agent.SNMPV3AgentToolkit;

class SNMPRuntimeStats {
   private boolean running;
   private long attributeChangeTrapCount;
   private long monitorTrapCount;
   private long counterMonitorTrapCount;
   private long gaugeMonitorTrapCount;
   private long stringMonitorTrapCount;
   private long logMessageTrapCount;
   private long serverStartTrapCount;
   private long serverStopTrapCount;
   private int failedAuthenticationCount;
   private int failedAuthorizationCount;
   private int failedEncryptionCount;
   private String snmpAgentName;
   private SNMPV3AgentToolkit snmpAgentToolkit;

   SNMPRuntimeStats() {
   }

   SNMPRuntimeStats(SNMPV3AgentToolkit var1) {
      this.snmpAgentToolkit = var1;
   }

   public long getAttributeChangeTrapCount() {
      return this.attributeChangeTrapCount;
   }

   public long getCounterMonitorTrapCount() {
      return this.counterMonitorTrapCount;
   }

   public long getGaugeMonitorTrapCount() {
      return this.gaugeMonitorTrapCount;
   }

   public long getLogMessageTrapCount() {
      return this.logMessageTrapCount;
   }

   public long getMonitorTrapCount() {
      return this.monitorTrapCount;
   }

   public boolean isRunning() {
      return this.running;
   }

   public long getServerStartTrapCount() {
      return this.serverStartTrapCount;
   }

   public long getServerStopTrapCount() {
      return this.serverStopTrapCount;
   }

   public long getStringMonitorTrapCount() {
      return this.stringMonitorTrapCount;
   }

   void setRunning(boolean var1) {
      this.running = var1;
   }

   synchronized void incrementAttributeChangeTrapCount() {
      ++this.attributeChangeTrapCount;
      ++this.monitorTrapCount;
   }

   synchronized void incrementCounterMonitorTrapCount() {
      ++this.counterMonitorTrapCount;
      ++this.monitorTrapCount;
   }

   synchronized void incrementGaugeMonitorTrapCount() {
      ++this.gaugeMonitorTrapCount;
      ++this.monitorTrapCount;
   }

   synchronized void incrementStringMonitorTrapCount() {
      ++this.stringMonitorTrapCount;
      ++this.monitorTrapCount;
   }

   synchronized void incrementLogMessageTrapCount() {
      ++this.logMessageTrapCount;
      ++this.monitorTrapCount;
   }

   synchronized void incrementServerStartTrapCount() {
      ++this.serverStartTrapCount;
   }

   synchronized void incrementServerStopTrapCount() {
      ++this.serverStopTrapCount;
   }

   String getSNMPAgentListenAddress() {
      return this.snmpAgentToolkit == null ? "" : this.snmpAgentToolkit.getSNMPAgentListenAddress();
   }

   int getSNMPAgentUDPPort() {
      return this.snmpAgentToolkit == null ? -1 : this.snmpAgentToolkit.getSNMPAgentUDPPort();
   }

   String getMasterAgentXListenAddress() {
      return this.snmpAgentToolkit == null ? "" : this.snmpAgentToolkit.getMasterAgentXListenAddress();
   }

   int getMasterAgentXPort() {
      return this.snmpAgentToolkit == null ? -1 : this.snmpAgentToolkit.getMasterAgentXPort();
   }

   String getCustomMBeansSubAgentMIB() throws SNMPAgentToolkitException {
      if (this.snmpAgentToolkit == null) {
         return "";
      } else {
         SNMPSubAgentX var1 = this.snmpAgentToolkit.findSNMPSubAgentX("1.2.3.4.5.6");
         return var1 != null ? var1.outputMIBModule() : "";
      }
   }

   public int getFailedAuthenticationCount() {
      if (this.snmpAgentToolkit == null) {
         return this.failedAuthenticationCount;
      } else {
         SNMPSecurityManager var1 = this.snmpAgentToolkit.getSNMPSecurityManager();
         int var2 = var1 != null ? var1.getFailedAuthenticationCount() : 0;
         if (var2 > this.failedAuthenticationCount) {
            this.failedAuthenticationCount = var2;
         }

         return this.failedAuthenticationCount;
      }
   }

   public int getFailedAuthorizationCount() {
      if (this.snmpAgentToolkit == null) {
         return this.failedAuthorizationCount;
      } else {
         SNMPSecurityManager var1 = this.snmpAgentToolkit.getSNMPSecurityManager();
         int var2 = var1 != null ? var1.getFailedAuthorizationCount() : 0;
         if (var2 > this.failedAuthorizationCount) {
            this.failedAuthorizationCount = var2;
         }

         return this.failedAuthorizationCount;
      }
   }

   public int getFailedEncryptionCount() {
      if (this.snmpAgentToolkit == null) {
         return this.failedEncryptionCount;
      } else {
         SNMPSecurityManager var1 = this.snmpAgentToolkit.getSNMPSecurityManager();
         int var2 = var1 != null ? var1.getFailedEncryptionCount() : 0;
         if (var2 > this.failedEncryptionCount) {
            this.failedEncryptionCount = var2;
         }

         return this.failedEncryptionCount;
      }
   }

   public void invalidateLocalizedKeyCache(String var1) {
      if (this.snmpAgentToolkit != null) {
         SNMPSecurityManager var2 = this.snmpAgentToolkit.getSNMPSecurityManager();
         if (var2 != null) {
            var2.invalidateLocalizedKeyCache(var1);
         }

      }
   }

   public SNMPV3AgentToolkit getSNMPAgentToolkit() {
      return this.snmpAgentToolkit;
   }

   public void setSNMPAgentToolkit(SNMPV3AgentToolkit var1) {
      this.snmpAgentToolkit = var1;
   }

   public String getSNMPAgentName() {
      return this.snmpAgentName;
   }

   public void setSNMPAgentName(String var1) {
      this.snmpAgentName = var1;
   }
}
