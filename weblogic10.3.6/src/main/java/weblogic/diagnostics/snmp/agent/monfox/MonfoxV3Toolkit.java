package weblogic.diagnostics.snmp.agent.monfox;

import java.net.InetAddress;
import java.security.AccessController;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import monfox.toolkit.snmp.SnmpIpAddress;
import monfox.toolkit.snmp.SnmpValueException;
import monfox.toolkit.snmp.agent.SnmpAgent;
import monfox.toolkit.snmp.agent.ext.acm.AppAcm;
import monfox.toolkit.snmp.agent.ext.table.SnmpMibTableAdaptor;
import monfox.toolkit.snmp.engine.SnmpEngineID;
import monfox.toolkit.snmp.engine.SnmpTransportException;
import monfox.toolkit.snmp.engine.TransportProvider;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPSecurityManager;
import weblogic.diagnostics.snmp.agent.SNMPSubAgentX;
import weblogic.diagnostics.snmp.agent.SNMPTransportProvider;
import weblogic.diagnostics.snmp.agent.SNMPV3AgentToolkit;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MonfoxV3Toolkit extends MonfoxToolkit implements SNMPV3AgentToolkit {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private SnmpEngineID snmpEngineId;
   private WLSTcpTransportProvider tcpProvider;
   private SNMPSecurityManager snmpSecurityManager;

   public void initializeSNMPAgentToolkit(String var1, String var2) throws SNMPAgentToolkitException {
      try {
         super.initializeSNMPAgentToolkit(var1, var2);
         TransportProvider.addTransportProvider(2, WLSTcpTransportProvider.class);
      } catch (Exception var4) {
         throw new SNMPAgentToolkitException(var4);
      }
   }

   public void startSNMPAgent(String var1, String var2, int var3, String var4, int var5, int var6) throws SNMPAgentToolkitException {
      try {
         this.snmpEngineId = new SnmpEngineID(var1, true);
      } catch (SnmpValueException var13) {
         throw new SNMPAgentToolkitException(var13);
      }

      for(int var7 = var5; var7 <= var5 + this.maxPortRetryCount; ++var7) {
         if (var7 != 162) {
            try {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Trying UDP port " + var7);
               }

               this.snmpAgentListenAddress = var4;
               this.snmpAgentUDPPort = var7;
               SnmpAgent.V3Config var8 = new SnmpAgent.V3Config(this.snmpAgentUDPPort, this.snmpEngineId);
               InetAddress var9 = null;
               if (this.snmpAgentListenAddress != null && this.snmpAgentListenAddress.length() > 0) {
                  var9 = InetAddress.getByName(this.snmpAgentListenAddress);
                  var8.setInetAddress(var9);
               }

               var8.setUsmUserSecurityExtension(WLSSecurityExtension.getInstance());
               var8.setAccessControlModel(new AppAcm(WLSAccessController.getInstance()));
               var8.setAuditTrailLogger(WLSAuditTrailLogger.getInstance());
               this.snmpAgent = new SnmpAgent(this.snmpMib, var8);
               if (var9 == null) {
                  try {
                     RuntimeAccess var10 = ManagementService.getRuntimeAccess(KERNEL_ID);
                     ServerURL var11 = new ServerURL(URLManager.findAdministrationURL(var10.getServerName()));
                     var9 = InetAddress.getByName(var11.getHost());
                  } catch (Exception var14) {
                     if (DEBUG_LOGGER.isDebugEnabled()) {
                        DEBUG_LOGGER.debug("Error computing agent address", var14);
                     }
                  }
               }

               if (var9 != null) {
                  this.snmpAgent.setAgentAddress(new SnmpIpAddress(var9));
               }

               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Claimed UDP port " + var7);
               }

               SNMPLogger.logStartedSNMPagent(var5);
               break;
            } catch (SnmpTransportException var15) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("UDP Port seems to be taken" + var7);
               }

               this.snmpAgent = null;
               if (var7 == var5 + this.maxPortRetryCount) {
                  throw new SNMPAgentToolkitException(var15);
               }
            } catch (Throwable var16) {
               throw new SNMPAgentToolkitException(var16);
            }
         }
      }

      try {
         this.snmpAgent.getEngine().setEngineBoots(var6);
         this.snmpAgent.setRequestExecManager(new MonfoxExecManager());
         this.snmpAgent.isCommunityAtContextFormSupported(true);
         this.targetManager = new TargetManager(this.snmpAgent.getTarget());
         this.notificationManager = new NotificationManager(this.snmpMetadata, this.snmpAgent.getNotifier());
         this.proxyManager = new ProxyManager(this.snmpAgent);
         this.tcpProvider = (WLSTcpTransportProvider)TransportProvider.newInstance(2, (InetAddress)null, 0);
         this.snmpAgent.getEngine().addTransportProvider(this.tcpProvider);
         this.snmpSecurityManager = new WLSSnmpSecurityManager(this.snmpAgent);
      } catch (Throwable var12) {
         throw new SNMPAgentToolkitException(var12);
      }
   }

   public void stopSNMPAgent() throws SNMPAgentToolkitException {
      if (this.tcpProvider != null) {
         this.snmpAgent.getEngine().removeTransportProvider(this.tcpProvider);
      }

      super.stopSNMPAgent();
   }

   public void setSecurityParams(int var1, int var2, int var3, long var4) {
      WLSSecurityExtension var6 = WLSSecurityExtension.getInstance();
      var6.setSecurityLevel(var1);
      var6.setAuthProtocol(var2);
      var6.setPrivProtocol(var3);
      var6.setLocalizedKeyCacheInvalidationInterval(var4);
      AppAcm var7 = (AppAcm)this.snmpAgent.getAccessControlModel();
      WLSAccessController var8 = (WLSAccessController)var7.getAccessController();
      var8.setSnmpEngineId(this.snmpEngineId);
   }

   public SNMPTransportProvider getTransportProvider(int var1) {
      switch (var1) {
         case 1:
            return this.tcpProvider;
         default:
            return null;
      }
   }

   public SNMPSecurityManager getSNMPSecurityManager() {
      return this.snmpSecurityManager;
   }

   public void addSNMPTableRowForMBeanInstance(String var1, MBeanServerConnection var2, ObjectName var3, Map var4) throws SNMPAgentToolkitException {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Adding table row " + var1 + " for " + var3);
      }

      try {
         SnmpMibTableAdaptor var5 = (SnmpMibTableAdaptor)this.snmpTables.get(var1);
         if (var5 != null) {
            MBeanInstanceTableRow var6 = new MBeanInstanceTableRow(var2, var3, var4);
            var5.addRow(var6);
         } else if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Did not find SnmpMibTableAdaptor for " + var1 + " and MBean " + var3);
         }

      } catch (Exception var7) {
         throw new SNMPAgentToolkitException(var7);
      }
   }

   public void deleteSNMPTableRowForMBeanInstance(String var1, ObjectName var2) throws SNMPAgentToolkitException {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Deleting row " + var1 + " for objectName " + var2);
      }

      try {
         SnmpMibTableAdaptor var3 = (SnmpMibTableAdaptor)this.snmpTables.get(var1);
         if (var3 != null) {
            String var4 = MBeanInstanceTableRow.computeIndex(var2.toString());
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Deleting row for index " + var4);
            }

            var3.removeRow(new String[]{var4});
         }

      } catch (Exception var5) {
         throw new SNMPAgentToolkitException(var5);
      }
   }

   public void setCommunityBasedAccessEnabled(boolean var1) throws SNMPAgentToolkitException {
      AppAcm var2 = (AppAcm)this.snmpAgent.getAccessControlModel();
      WLSAccessController var3 = (WLSAccessController)var2.getAccessController();
      var3.setCommunityBasedAccessEnabled(var1);
   }

   public void setSNMPCommunity(String var1, String var2) throws SNMPAgentToolkitException {
      AppAcm var3 = (AppAcm)this.snmpAgent.getAccessControlModel();
      WLSAccessController var4 = (WLSAccessController)var3.getAccessController();
      var4.setCommunity(var1);
   }

   public SNMPSubAgentX createSNMPSubAgentX(String var1, String var2) throws SNMPAgentToolkitException {
      MBeanServerSubAgentXImpl var3 = new MBeanServerSubAgentXImpl(this.masterAgentXHost, this.masterAgentXPort, var1, var2);
      this.subAgents.put(var1, var3);
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Created subagent " + var1);
      }

      return var3;
   }
}
