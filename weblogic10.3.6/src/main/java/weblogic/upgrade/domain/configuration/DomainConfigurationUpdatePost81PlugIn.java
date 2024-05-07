package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import weblogic.version;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainConfigurationUpdatePost81PlugIn extends AbstractPlugIn {
   DomainMBean domainBean;

   public DomainConfigurationUpdatePost81PlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      if (this.domainBean == null) {
         throw new PlugInException(this.getName(), "The domain bean tree was not found");
      }
   }

   public void execute() throws PlugInException {
      UpgradeHelper.log(this, UpgradeHelper.i18n("DomainConfigurationUpdatePost81Plugin.execute.start_in_mem_config_upgrade"));
      String var1 = version.getReleaseBuildVersion();
      this.domainBean.setConfigurationVersion(var1);
      this.domainBean.setDomainVersion(var1);
      UpgradeHelper.log(this, UpgradeHelper.i18n("DomainConfigurationUpdatePost81Plugin.execute.completedin_mem_config_upgrade"));
      UpgradeHelper.log(this, UpgradeHelper.i18n("DomainConfigurationUpdatePost81Plugin.execute.config_written_out_later"));
   }
}
