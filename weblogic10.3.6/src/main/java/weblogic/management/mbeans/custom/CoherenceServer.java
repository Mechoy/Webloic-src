package weblogic.management.mbeans.custom;

import weblogic.management.configuration.CoherenceServerMBean;
import weblogic.management.configuration.ManagedExternalServerStartMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class CoherenceServer extends ConfigurationMBeanCustomizer {
   public CoherenceServer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private CoherenceServerMBean getServer() {
      return (CoherenceServerMBean)this.getMbean();
   }

   public ManagedExternalServerStartMBean getManagedExternalServerStart() {
      return this.getServer().getCoherenceServerStart();
   }
}
