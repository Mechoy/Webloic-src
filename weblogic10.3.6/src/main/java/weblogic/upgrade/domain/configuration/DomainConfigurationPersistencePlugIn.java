package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ClientAccess;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.upgrade.PluginActionDelegatePlugIn;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainConfigurationPersistencePlugIn extends PluginActionDelegatePlugIn {
   private static String[] RESTRUCTURERS = new String[]{"weblogic.upgrade.domain.directoryrestructure.MoveConfigurationFilesPlugin", "weblogic.upgrade.PluginActionDelegate"};

   public DomainConfigurationPersistencePlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1, RESTRUCTURERS);
   }

   public void execute() throws PlugInException {
      try {
         if (this.doWriteOutConfigFiles()) {
            DomainMBean var1 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
            File var2 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
            File var3 = new File(var2, "config");
            UpgradeHelper.log(this, UpgradeHelper.i18n("DomainConfigurationPersistencePlugIn.execute.writing_config_files", (Object)var3.getPath()));
            ClientAccess var4 = ManagementServiceClient.getClientAccess();
            var4.saveDomainDirectory(var1, var2.getPath());
            UpgradeHelper.log(this, UpgradeHelper.i18n("DomainConfigurationPersistencePlugIn.execute.done_writing_config_files", (Object)var3.getPath()));
            UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainConfigurationPersistencePlugIn.execute.check_java_compiler_values"));
            UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainConfigurationPersistencePlugIn.execute.check_server_start_values"));
         }
      } catch (Exception var5) {
         throw new PlugInException(this.getName(), "Execute Exception ... " + var5.toString(), var5);
      }

      super.execute();
   }

   private boolean doWriteOutConfigFiles() {
      Boolean var1 = (Boolean)this._context.get(DomainPlugInConstants.HAS_CONFIGURATION_KEY);
      return var1 != null && var1;
   }
}
