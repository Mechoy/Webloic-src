package weblogic.kodo.event;

import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.lib.conf.AbstractProductDerivation;
import org.apache.openjpa.lib.conf.Configuration;

public class ClusterFeatureDerivation extends AbstractProductDerivation {
   private static final String FEATURE = "wls-cluster";
   private static final Class CLASS = ClusterRemoteCommitProvider.class;

   public int getType() {
      return 1000;
   }

   public boolean beforeConfigurationLoad(Configuration var1) {
      if (var1 instanceof OpenJPAConfigurationImpl) {
         OpenJPAConfigurationImpl var2 = (OpenJPAConfigurationImpl)var1;
         var2.remoteProviderPlugin.setAlias("wls-cluster", CLASS.getName());
         var2.remoteProviderPlugin.setDefault("wls-cluster");
         return true;
      } else {
         return false;
      }
   }
}
