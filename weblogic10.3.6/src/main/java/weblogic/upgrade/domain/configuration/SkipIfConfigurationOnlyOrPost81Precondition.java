package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInContext;
import java.util.Arrays;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class SkipIfConfigurationOnlyOrPost81Precondition extends SkipIfConfigurationPost81Precondition {
   public boolean evaluate(PlugInContext var1) {
      String[] var2 = (String[])((String[])var1.get(DomainPlugInConstants.OPTIONAL_GROUPS_KEY));
      return super.evaluate(var1) && !Arrays.asList(var2).contains("CONFIGURATION_ONLY_SELECTED_VALUE");
   }
}
