package weblogic.diagnostics.snmp.server;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.MBeanServerSubAgentX;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPV3AgentToolkit;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.diagnostics.snmp.mib.WLSMibMetadata;
import weblogic.diagnostics.snmp.mib.WLSMibTableColumnsMetadata;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.mbeanservers.internal.utils.typing.MBeanCategorizer;
import weblogic.management.mbeanservers.internal.utils.typing.MBeanCategorizerPlugins;
import weblogic.management.mbeanservers.internal.utils.typing.MBeanTypeUtil;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class MBeanRegHandler implements MBeanTypeUtil.RegHandler {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final String LOCATION_KEY = "Location";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String domainName;
   private boolean adminServer;
   private String serverName;
   private SNMPAgentMBean snmpAgentConfig;
   private MBeanServerConnection mbeanServerConnection;
   private MBeanTypeUtil mbeanTypeUtil = null;
   private SNMPAgent snmpAgent;
   private MBeanServerSubAgentX customMBeansSubAgent;
   private List<WLSMibMetadata> wlsMibMetadataList = null;
   private List jmxMonitorLifecycleList;
   private Map objectNameTypes = new HashMap();

   public MBeanRegHandler(String var1, boolean var2, String var3, SNMPAgentMBean var4, MBeanServerConnection var5, SNMPAgent var6, MBeanServerSubAgentX var7, List<WLSMibMetadata> var8, List var9) {
      this.domainName = var1;
      this.adminServer = var2;
      this.serverName = var3;
      this.snmpAgentConfig = var4;
      this.snmpAgent = var6;
      this.customMBeansSubAgent = var7;
      this.mbeanServerConnection = var5;
      this.wlsMibMetadataList = var8;
      this.jmxMonitorLifecycleList = var9;
   }

   public void newInstance(String var1, String var2, String var3) throws MalformedObjectNameException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Invoked newInstance(" + var1 + ", " + var2 + ", " + var3 + ")");
      }

      if (!this.objectNameTypes.containsKey(var2)) {
         if (this.isAgentUnavailable()) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Agent not available for MBean registration");
            }

         } else {
            final ObjectName var4 = new ObjectName(var2);
            boolean var5 = var3.equals("WLS-MBean");
            if (!var5) {
               try {
                  if (this.snmpAgentConfig.isSNMPAccessForUserMBeansEnabled() && this.customMBeansSubAgent != null) {
                     this.customMBeansSubAgent.addSNMPTableRowForMBeanInstance(this.mbeanServerConnection, var1, var4);
                     this.objectNameTypes.put(var2, var1);
                     return;
                  }
               } catch (Throwable var11) {
                  SNMPLogger.logErrorAddingRowForMBeanInstance(var1, var2, var11);
               }
            } else {
               String var7;
               if (this.adminServer) {
                  boolean var6 = var1.startsWith("weblogic.management.runtime");
                  if (!var6) {
                     var7 = var4.getKeyProperty("Location");
                     if (var7 == null || !var7.equals(this.serverName)) {
                        if (DEBUG.isDebugEnabled()) {
                           DEBUG.debug("Rejecting " + var2);
                        }

                        return;
                     }
                  }
               }

               try {
                  SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        MBeanRegHandler.this.registerMonitorListeners(var4);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var13) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Exception registering monitors", var13);
                  }
               }

               WLSMibMetadata var14 = this.findWLSMibMetadata(var1);
               if (var14 == null) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Type not defined in existing metadatas " + var1);
                  }

                  return;
               }

               var7 = var14.getSNMPTableName(var1);
               WLSMibTableColumnsMetadata var8 = var14.getColumnsMetadataForSNMPTable(var7);
               SNMPV3AgentToolkit var9 = (SNMPV3AgentToolkit)this.snmpAgent.getSNMPAgentToolkit();

               try {
                  Map var10 = var8.getColumnAttributeMap();
                  var9.addSNMPTableRowForMBeanInstance(var7, this.mbeanServerConnection, var4, var10);
                  this.objectNameTypes.put(var2, var1);
               } catch (Throwable var12) {
                  SNMPLogger.logErrorAddingRowForMBeanInstance(var1, var2, var12);
                  return;
               }
            }

         }
      }
   }

   private boolean isAgentUnavailable() {
      SNMPAgentDeploymentHandler var1 = SNMPAgentDeploymentHandler.getInstance();
      return this.snmpAgent == null || !this.snmpAgent.isSNMPAgentInitialized() || var1.isAgentStopping() || var1.isAgentStopped();
   }

   void initializeMBeanServerRegistration() throws Exception {
      MBeanCategorizer.Impl var1 = new MBeanCategorizer.Impl(new MBeanCategorizer.Plugin[]{new MBeanCategorizerPlugins.WLSPlugin(), new MBeanCategorizerPlugins.NonWLSPlugin()});
      this.mbeanTypeUtil = new MBeanTypeUtil(this.mbeanServerConnection, var1);
      this.mbeanTypeUtil.addRegistrationHandler(this);
   }

   public void instanceDeleted(String var1) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Invoked instanceDeleted(" + var1 + ")");
      }

      if (this.isAgentUnavailable()) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Agent not available for MBean unregistration");
         }

      } else {
         String var2 = (String)this.objectNameTypes.get(var1);
         if (var2 == null) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Type name not available for " + var1);
            }

         } else {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Type name for " + var1 + " = " + var2);
            }

            WLSMibMetadata var3 = this.findWLSMibMetadata(var2);
            if (var3 == null) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Type not defined in existing metadatas " + var2);
               }

               try {
                  if (this.customMBeansSubAgent != null) {
                     this.customMBeansSubAgent.deleteSNMPTableRowForMBeanInstance(var2, new ObjectName(var1));
                  }
               } catch (Throwable var8) {
                  SNMPLogger.logErrorDeletingRowForMBeanInstance(var1, var8);
               }

            } else {
               final ObjectName var4 = null;

               try {
                  var4 = new ObjectName(var1);
               } catch (Exception var10) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Bad ObjectName in callback ", var10);
                     return;
                  }
               }

               SNMPV3AgentToolkit var5 = (SNMPV3AgentToolkit)this.snmpAgent.getSNMPAgentToolkit();

               try {
                  String var6 = var3.getSNMPTableName(var2);
                  var5.deleteSNMPTableRowForMBeanInstance(var6, var4);
                  this.objectNameTypes.remove(var1);
                  SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        MBeanRegHandler.this.deregisterMonitorListeners(var4);
                        return null;
                     }
                  });
               } catch (Throwable var9) {
                  SNMPLogger.logErrorDeletingRowForMBeanInstance(var1, var9);
               }

            }
         }
      }
   }

   private boolean containsLocationKey(ObjectName var1) {
      Hashtable var2 = var1.getKeyPropertyList();
      return var2.containsKey("Location");
   }

   private void registerMonitorListeners(ObjectName var1) {
      Iterator var2 = this.jmxMonitorLifecycleList.iterator();

      while(var2.hasNext()) {
         JMXMonitorLifecycle var3 = (JMXMonitorLifecycle)var2.next();
         var3.registerMonitorListeners(var1);
      }

   }

   private void deregisterMonitorListeners(ObjectName var1) {
      Iterator var2 = this.jmxMonitorLifecycleList.iterator();

      while(var2.hasNext()) {
         JMXMonitorLifecycle var3 = (JMXMonitorLifecycle)var2.next();
         var3.deregisterMonitorListeners(var1);
      }

   }

   void deregister() {
      try {
         this.mbeanTypeUtil.removeRegistrationHandler(this);
      } catch (Throwable var2) {
      }

   }

   private WLSMibMetadata findWLSMibMetadata(String var1) {
      Iterator var2 = this.wlsMibMetadataList.iterator();

      WLSMibMetadata var3;
      String var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLSMibMetadata)var2.next();
         var4 = var3.getSNMPTableName(var1);
      } while(var4 == null);

      return var3;
   }
}
