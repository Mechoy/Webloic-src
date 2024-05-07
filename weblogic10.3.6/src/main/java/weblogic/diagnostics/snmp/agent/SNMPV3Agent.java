package weblogic.diagnostics.snmp.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import weblogic.diagnostics.snmp.agent.monfox.MonfoxV3Toolkit;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class SNMPV3Agent extends SNMPAgent implements SNMPConstants {
   private WorkManager snmpWorkManager;
   private String engineId;
   private int engineBoots;
   private boolean communityBasedAccessEnabled = true;
   private int securityLevel;
   private int authProtocol;
   private int privProtocol;
   private long localizedKeyCacheInvalidationInterval;
   private long snmpEngineBoots;
   private String snmpDataFileName;
   private String tcpListenAddress;
   private int tcpListenPort;
   private SNMPV3AgentToolkit snmpV3AgentToolkit;

   public SNMPV3Agent() {
      super(new MonfoxV3Toolkit());
      this.snmpV3AgentToolkit = (SNMPV3AgentToolkit)super.snmpAgentToolkit;
   }

   public void initialize() throws SNMPAgentToolkitException {
      this.snmpWorkManager = WorkManagerFactory.getInstance().findOrCreate("SnmpWorkManager", 2, -1);
      this.snmpV3AgentToolkit.initializeSNMPAgentToolkit(this.mibBasePath, this.mibModules);
      this.snmpV3AgentToolkit.setMaxPortRetryCount(this.maxPortRetryCount);
      this.snmpV3AgentToolkit.startSNMPAgent(this.engineId, this.tcpListenAddress, this.tcpListenPort, this.udpListenAddress, this.udpListenPort, this.engineBoots);
      this.snmpV3AgentToolkit.createSNMPMibTables(this.mibModules);
      this.snmpV3AgentToolkit.setSNMPCommunity(this.community, this.rootOidNode);
      this.snmpV3AgentToolkit.setCommunityBasedAccessEnabled(this.communityBasedAccessEnabled);
      this.snmpV3AgentToolkit.setSecurityParams(this.securityLevel, this.authProtocol, this.privProtocol, this.localizedKeyCacheInvalidationInterval);
      this.initializeTrapDestinations();
      this.snmpEngineBoots = this.getCurrentEngineBootsValue();
      this.updateSNMPEngineBoots();
      if (this.automaticTrapsEnabled) {
         this.sendColdStartTrap();
      }

      this.snmpAgentInitialized = true;
   }

   public int getEngineBoots() {
      return this.engineBoots;
   }

   public void setEngineBoots(int var1) {
      this.engineBoots = var1;
   }

   public String getEngineId() {
      return this.engineId;
   }

   public void setEngineId(String var1) {
      this.engineId = var1;
   }

   public boolean isCommunityBasedAccessEnabled() {
      return this.communityBasedAccessEnabled;
   }

   public void setCommunityBasedAccessEnabled(boolean var1) {
      this.communityBasedAccessEnabled = var1;
   }

   public int getAuthProtocol() {
      return this.authProtocol;
   }

   public void setAuthProtocol(int var1) {
      this.authProtocol = var1;
   }

   public int getPrivProtocol() {
      return this.privProtocol;
   }

   public void setPrivProtocol(int var1) {
      this.privProtocol = var1;
   }

   public int getSecurityLevel() {
      return this.securityLevel;
   }

   public void setSecurityLevel(int var1) {
      this.securityLevel = var1;
   }

   private void updateSNMPEngineBoots() {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Updating engine boots");
      }

      ++this.snmpEngineBoots;
      String var1 = this.getSNMPDataFileName();
      File var2 = new File(var1);
      File var3 = var2.getParentFile();
      if (var3 != null) {
         var3.mkdirs();
      }

      SNMPData var4 = new SNMPData();
      var4.setEngineBoots(this.snmpEngineBoots);
      FileOutputStream var5 = null;

      try {
         var5 = new FileOutputStream(var2);
         ObjectOutputStream var6 = new ObjectOutputStream(var5);
         var6.writeObject(var4);
         var5.flush();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Updated engine boots to " + this.snmpEngineBoots);
         }
      } catch (Throwable var15) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Error updating engine boots ", var15);
         }
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var14) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Error closing file ", var14);
               }
            }
         }

      }

   }

   private long getCurrentEngineBootsValue() {
      long var1 = 0L;
      String var3 = this.getSNMPDataFileName();
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("SNMP data file = " + var3);
      }

      File var4 = new File(var3);
      FileInputStream var5 = null;
      if (var4.exists()) {
         long var7;
         try {
            var5 = new FileInputStream(var4);
            ObjectInputStream var6 = new ObjectInputStream(var5);
            SNMPData var20 = (SNMPData)var6.readObject();
            var1 = var20.getEngineBoots();
            return var1;
         } catch (Throwable var18) {
            var7 = 0L;
         } finally {
            try {
               if (var5 != null) {
                  var5.close();
               }
            } catch (IOException var17) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Error closing data file", var17);
               }
            }

         }

         return var7;
      } else {
         return var1;
      }
   }

   public String getSNMPDataFileName() {
      return this.snmpDataFileName;
   }

   public void setSNMPDataFileName(String var1) {
      this.snmpDataFileName = var1;
   }

   public long getLocalizedKeyCacheInvalidationInterval() {
      return this.localizedKeyCacheInvalidationInterval;
   }

   public void setLocalizedKeyCacheInvalidationInterval(long var1) {
      this.localizedKeyCacheInvalidationInterval = var1;
   }

   public WorkManager getSnmpWorkManagerInstance() {
      return this.snmpWorkManager;
   }

   public String getTcpListenAddress() {
      return this.tcpListenAddress;
   }

   public void setTcpListenAddress(String var1) {
      this.tcpListenAddress = var1;
   }

   public int getTcpListenPort() {
      return this.tcpListenPort;
   }

   public void setTcpListenPort(int var1) {
      this.tcpListenPort = var1;
   }

   private static class SNMPData implements Serializable {
      private static final long serialVersionUID = 4541537220945911770L;
      private long engineBoots;

      private SNMPData() {
         this.engineBoots = 0L;
      }

      public long getEngineBoots() {
         return this.engineBoots;
      }

      public void setEngineBoots(long var1) {
         this.engineBoots = var1;
      }

      // $FF: synthetic method
      SNMPData(Object var1) {
         this();
      }
   }
}
