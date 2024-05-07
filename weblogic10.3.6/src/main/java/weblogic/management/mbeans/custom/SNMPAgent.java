package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SNMPAttributeChangeMBean;
import weblogic.management.configuration.SNMPCounterMonitorMBean;
import weblogic.management.configuration.SNMPGaugeMonitorMBean;
import weblogic.management.configuration.SNMPLogFilterMBean;
import weblogic.management.configuration.SNMPProxyMBean;
import weblogic.management.configuration.SNMPStringMonitorMBean;
import weblogic.management.configuration.SNMPTrapDestinationMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class SNMPAgent extends ConfigurationMBeanCustomizer {
   public SNMPAgent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public SNMPAttributeChangeMBean createSNMPAttributeChange(String var1, SNMPAttributeChangeMBean var2) {
      try {
         SNMPAttributeChangeMBean var3 = (SNMPAttributeChangeMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPAttributeChange", var2);
         var3.setEnabledServers(var2.getEnabledServers());
         return var3;
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      } catch (InvalidAttributeValueException var5) {
         throw new Error(var5);
      } catch (ManagementException var6) {
         throw new Error(var6);
      }
   }

   public SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1, SNMPCounterMonitorMBean var2) {
      try {
         SNMPCounterMonitorMBean var3 = (SNMPCounterMonitorMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPCounterMonitor", var2);
         var3.setEnabledServers(var2.getEnabledServers());
         return var3;
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      } catch (InvalidAttributeValueException var5) {
         throw new Error(var5);
      } catch (ManagementException var6) {
         throw new Error(var6);
      }
   }

   public SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1, SNMPGaugeMonitorMBean var2) {
      try {
         SNMPGaugeMonitorMBean var3 = (SNMPGaugeMonitorMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPGaugeMonitor", var2);
         var3.setEnabledServers(var2.getEnabledServers());
         return var3;
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      } catch (InvalidAttributeValueException var5) {
         throw new Error(var5);
      } catch (ManagementException var6) {
         throw new Error(var6);
      }
   }

   public SNMPStringMonitorMBean createSNMPStringMonitor(String var1, SNMPStringMonitorMBean var2) {
      try {
         SNMPStringMonitorMBean var3 = (SNMPStringMonitorMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPStringMonitor", var2);
         var3.setEnabledServers(var2.getEnabledServers());
         return var3;
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      } catch (InvalidAttributeValueException var5) {
         throw new Error(var5);
      } catch (ManagementException var6) {
         throw new Error(var6);
      }
   }

   public SNMPLogFilterMBean createSNMPLogFilter(String var1, SNMPLogFilterMBean var2) {
      try {
         SNMPLogFilterMBean var3 = (SNMPLogFilterMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPLogFilter", var2);
         var3.setEnabledServers(var2.getEnabledServers());
         return var3;
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      } catch (InvalidAttributeValueException var5) {
         throw new Error(var5);
      } catch (ManagementException var6) {
         throw new Error(var6);
      }
   }

   public SNMPProxyMBean createSNMPProxy(String var1, SNMPProxyMBean var2) {
      try {
         return (SNMPProxyMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPProxy", var2);
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      }
   }

   public SNMPTrapDestinationMBean createSNMPTrapDestination(String var1, SNMPTrapDestinationMBean var2) {
      try {
         return (SNMPTrapDestinationMBean)this.getMbean().createChildCopyIncludingObsolete("SNMPTrapDestination", var2);
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      }
   }
}
