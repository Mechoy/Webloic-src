package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SNMPAgentMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SNMPAgentMBeanImpl bean;

   protected SNMPAgentMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SNMPAgentMBeanImpl)var1;
   }

   public SNMPAgentMBeanBinder() {
      super(new SNMPAgentMBeanImpl());
      this.bean = (SNMPAgentMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AuthenticationProtocol")) {
                  try {
                     this.bean.setAuthenticationProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("CommunityPrefix")) {
                  try {
                     this.bean.setCommunityPrefix((String)var2);
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("DebugLevel")) {
                  this.handleDeprecatedProperty("DebugLevel", "10.0.0.0 Use the ServerDebugMBean.DebugSNMPToolkit attribute to configure the SNMP Toolkit debug");

                  try {
                     this.bean.setDebugLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("InformRetryInterval")) {
                  try {
                     this.bean.setInformRetryInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("LocalizedKeyCacheInvalidationInterval")) {
                  try {
                     this.bean.setLocalizedKeyCacheInvalidationInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("MasterAgentXPort")) {
                  try {
                     this.bean.setMasterAgentXPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("MaxInformRetryCount")) {
                  try {
                     this.bean.setMaxInformRetryCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("MibDataRefreshInterval")) {
                  this.handleDeprecatedProperty("MibDataRefreshInterval", "10.0.0.0 There is no replacement for this attribute.");

                  try {
                     this.bean.setMibDataRefreshInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("PrivacyProtocol")) {
                  try {
                     this.bean.setPrivacyProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("SNMPAttributeChange")) {
                  try {
                     this.bean.addSNMPAttributeChange((SNMPAttributeChangeMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                     this.bean.removeSNMPAttributeChange((SNMPAttributeChangeMBean)var21.getExistingBean());
                     this.bean.addSNMPAttributeChange((SNMPAttributeChangeMBean)((AbstractDescriptorBean)((SNMPAttributeChangeMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPCounterMonitor")) {
                  try {
                     this.bean.addSNMPCounterMonitor((SNMPCounterMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                     this.bean.removeSNMPCounterMonitor((SNMPCounterMonitorMBean)var20.getExistingBean());
                     this.bean.addSNMPCounterMonitor((SNMPCounterMonitorMBean)((AbstractDescriptorBean)((SNMPCounterMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPEngineId")) {
                  try {
                     this.bean.setSNMPEngineId((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("SNMPGaugeMonitor")) {
                  try {
                     this.bean.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     this.bean.removeSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var18.getExistingBean());
                     this.bean.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)((AbstractDescriptorBean)((SNMPGaugeMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPLogFilter")) {
                  try {
                     this.bean.addSNMPLogFilter((SNMPLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     this.bean.removeSNMPLogFilter((SNMPLogFilterMBean)var17.getExistingBean());
                     this.bean.addSNMPLogFilter((SNMPLogFilterMBean)((AbstractDescriptorBean)((SNMPLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPPort")) {
                  try {
                     this.bean.setSNMPPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("SNMPProxy")) {
                  try {
                     this.bean.addSNMPProxy((SNMPProxyMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     this.bean.removeSNMPProxy((SNMPProxyMBean)var15.getExistingBean());
                     this.bean.addSNMPProxy((SNMPProxyMBean)((AbstractDescriptorBean)((SNMPProxyMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPStringMonitor")) {
                  try {
                     this.bean.addSNMPStringMonitor((SNMPStringMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     this.bean.removeSNMPStringMonitor((SNMPStringMonitorMBean)var14.getExistingBean());
                     this.bean.addSNMPStringMonitor((SNMPStringMonitorMBean)((AbstractDescriptorBean)((SNMPStringMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPTrapDestination")) {
                  try {
                     this.bean.addSNMPTrapDestination((SNMPTrapDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     this.bean.removeSNMPTrapDestination((SNMPTrapDestinationMBean)var13.getExistingBean());
                     this.bean.addSNMPTrapDestination((SNMPTrapDestinationMBean)((AbstractDescriptorBean)((SNMPTrapDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("SNMPTrapVersion")) {
                  try {
                     this.bean.setSNMPTrapVersion(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ServerStatusCheckIntervalFactor")) {
                  this.handleDeprecatedProperty("ServerStatusCheckIntervalFactor", "10.0.0.0 There is no replacement for this attribute.");

                  try {
                     this.bean.setServerStatusCheckIntervalFactor(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("TargetedTrapDestinations")) {
                  this.handleDeprecatedProperty("TargetedTrapDestinations", "9.0.0.0 Use the getSNMPTrapDestinations() method instead.");
                  this.bean.setTargetedTrapDestinationsAsString((String)var2);
               } else if (var1.equals("UserDefinedMib")) {
                  try {
                     this.bean.setUserDefinedMib((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("CommunityBasedAccessEnabled")) {
                  try {
                     this.bean.setCommunityBasedAccessEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Enabled")) {
                  try {
                     this.bean.setEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("InformEnabled")) {
                  try {
                     this.bean.setInformEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SNMPAccessForUserMBeansEnabled")) {
                  try {
                     this.bean.setSNMPAccessForUserMBeansEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SendAutomaticTrapsEnabled")) {
                  try {
                     this.bean.setSendAutomaticTrapsEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var32) {
         System.out.println(var32 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var32;
      } catch (RuntimeException var33) {
         throw var33;
      } catch (Exception var34) {
         if (var34 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var34);
         } else if (var34 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var34.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var34);
         }
      }
   }
}
