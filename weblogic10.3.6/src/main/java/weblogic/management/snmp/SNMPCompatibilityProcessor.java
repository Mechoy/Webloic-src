package weblogic.management.snmp;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.configuration.SNMPAttributeChangeMBean;
import weblogic.management.configuration.SNMPCounterMonitorMBean;
import weblogic.management.configuration.SNMPGaugeMonitorMBean;
import weblogic.management.configuration.SNMPLogFilterMBean;
import weblogic.management.configuration.SNMPProxyMBean;
import weblogic.management.configuration.SNMPStringMonitorMBean;
import weblogic.management.configuration.SNMPTrapDestinationMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class SNMPCompatibilityProcessor implements ConfigurationProcessor {
   private static final boolean DEBUG = false;

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      SNMPAgentMBean var2 = var1.getSNMPAgent();
      SNMPProxyMBean[] var3 = var1.getSNMPProxies();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         upgradeSNMPProxyConfiguration(var2, var3[var4]);
      }

      SNMPTrapDestinationMBean[] var11 = var1.getSNMPTrapDestinations();

      for(int var5 = 0; var5 < var11.length; ++var5) {
         upgradeSNMPTrapDestinationConfiguration(var2, var11[var5]);
      }

      SNMPCounterMonitorMBean[] var12 = var1.getSNMPCounterMonitors();

      for(int var6 = 0; var6 < var12.length; ++var6) {
         upgradeSNMPCounterMonitorConfiguration(var2, var12[var6]);
      }

      SNMPGaugeMonitorMBean[] var13 = var1.getSNMPGaugeMonitors();

      for(int var7 = 0; var7 < var13.length; ++var7) {
         upgradeSNMPGaugeMonitorConfiguration(var2, var13[var7]);
      }

      SNMPStringMonitorMBean[] var14 = var1.getSNMPStringMonitors();

      for(int var8 = 0; var8 < var14.length; ++var8) {
         upgradeSNMPStringMonitorConfiguration(var2, var14[var8]);
      }

      SNMPAttributeChangeMBean[] var15 = var1.getSNMPAttributeChanges();

      for(int var9 = 0; var9 < var15.length; ++var9) {
         upgradeSNMPAttributeChangeConfiguration(var2, var15[var9]);
      }

      SNMPLogFilterMBean[] var16 = var1.getSNMPLogFilters();

      for(int var10 = 0; var10 < var16.length; ++var10) {
         upgradeSNMPLogFilterConfiguration(var2, var16[var10]);
      }

   }

   private static void upgradeSNMPProxyConfiguration(SNMPAgentMBean var0, SNMPProxyMBean var1) {
      var0.createSNMPProxy(var1.getName(), var1);
   }

   private static void upgradeSNMPTrapDestinationConfiguration(SNMPAgentMBean var0, SNMPTrapDestinationMBean var1) {
      var0.createSNMPTrapDestination(var1.getName(), var1);
   }

   private static void upgradeSNMPCounterMonitorConfiguration(SNMPAgentMBean var0, SNMPCounterMonitorMBean var1) {
      var0.createSNMPCounterMonitor(var1.getName(), var1);
   }

   private static void upgradeSNMPGaugeMonitorConfiguration(SNMPAgentMBean var0, SNMPGaugeMonitorMBean var1) {
      var0.createSNMPGaugeMonitor(var1.getName(), var1);
   }

   private static void upgradeSNMPStringMonitorConfiguration(SNMPAgentMBean var0, SNMPStringMonitorMBean var1) {
      var0.createSNMPStringMonitor(var1.getName(), var1);
   }

   private static void upgradeSNMPLogFilterConfiguration(SNMPAgentMBean var0, SNMPLogFilterMBean var1) {
      var0.createSNMPLogFilter(var1.getName(), var1);
   }

   private static void upgradeSNMPAttributeChangeConfiguration(SNMPAgentMBean var0, SNMPAttributeChangeMBean var1) {
      var0.createSNMPAttributeChange(var1.getName(), var1);
   }
}
