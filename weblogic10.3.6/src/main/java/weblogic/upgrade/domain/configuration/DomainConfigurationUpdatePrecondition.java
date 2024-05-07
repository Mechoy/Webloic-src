package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInGroupPreCondition;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainConfigurationUpdatePrecondition implements PlugInGroupPreCondition {
   public boolean evaluate(PlugInContext var1) {
      Boolean var2 = (Boolean)var1.get(DomainPlugInConstants.HAS_CONFIGURATION_KEY);
      return var2 != null && var2;
   }
}
