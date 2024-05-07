package weblogic.jms.backend;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public final class BEConfigCompatibility implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      JMSServerMBean[] var2 = var1.getJMSServers();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            JMSServerMBean var4 = var2[var3];
            var4.useDelegates(var1);
         }
      }

   }
}
