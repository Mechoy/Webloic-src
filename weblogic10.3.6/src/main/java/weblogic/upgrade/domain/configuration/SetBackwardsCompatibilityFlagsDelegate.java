package weblogic.upgrade.domain.configuration;

import java.util.Arrays;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.WebAppContainerMBean;
import weblogic.upgrade.PluginActionDelegate;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class SetBackwardsCompatibilityFlagsDelegate extends PluginActionDelegate {
   public void execute() throws Exception {
      if (!this.isSkipExecute()) {
         DomainMBean var1 = this.getDomainMBean();
         WebAppContainerMBean var2 = var1.getWebAppContainer();
         this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_FilterDispatchedRequestsEnabled"));
         var2.setFilterDispatchedRequestsEnabled(true);
         this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_RtexprvalueJspParamName"));
         var2.setRtexprvalueJspParamName(true);
         this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_JSPCompilerBackwardsCompatible="));
         var2.setJSPCompilerBackwardsCompatible(true);
         this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_ReloginEnabled"));
         var2.setReloginEnabled(true);
         this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_AllowAllRoles"));
         var2.setAllowAllRoles(true);
         SecurityConfigurationMBean var3 = var1.getSecurityConfiguration();
         if (var3 != null && !var3.isSet("WebAppFilesCaseInsensitive")) {
            this.log(UpgradeHelper.i18n("SetBackwardsCompatibilityFlagsDelegate.set_WebAppFilesCaseInsensitive"));
            var3.setWebAppFilesCaseInsensitive("os");
         }

      }
   }

   private boolean isSkipExecute() {
      String[] var1 = (String[])((String[])this.getPlugInContext().get(DomainPlugInConstants.OPTIONAL_GROUPS_KEY));
      return Arrays.asList(var1).contains("SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE");
   }
}
