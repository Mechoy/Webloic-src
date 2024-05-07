package weblogic.management.mbeans.custom;

import java.util.HashSet;
import java.util.Set;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class SAFAgent extends ConfigurationMBeanCustomizer {
   public SAFAgent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public Set getServerNames() {
      TargetMBean[] var1 = ((SAFAgentMBean)this.getMbean()).getTargets();
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.addAll(var1[var3].getServerNames());
      }

      return var2;
   }
}
