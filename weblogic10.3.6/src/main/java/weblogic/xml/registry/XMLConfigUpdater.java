package weblogic.xml.registry;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class XMLConfigUpdater implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      ServerMBean[] var2 = var1.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getXMLRegistry() == null) {
         }

         if (var2[var3].getXMLEntityCache() == null) {
         }
      }

   }
}
