package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInGroupPreCondition;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class RunIfConfigurationPost81Precondition implements PlugInGroupPreCondition {
   public boolean evaluate(PlugInContext var1) {
      String var2 = (String)var1.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
      return !var2.startsWith("6.") && !var2.startsWith("7.") && !var2.startsWith("8.");
   }
}
