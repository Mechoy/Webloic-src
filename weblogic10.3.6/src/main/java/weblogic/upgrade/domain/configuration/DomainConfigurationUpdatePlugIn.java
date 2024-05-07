package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import weblogic.upgrade.PluginActionDelegatePlugIn;
import weblogic.upgrade.UpgradeHelper;

public class DomainConfigurationUpdatePlugIn extends PluginActionDelegatePlugIn {
   private static String[] CONFIGURATORS = new String[]{"weblogic.upgrade.channels.ChannelConfigProcessor", "weblogic.upgrade.jta.JTAMigratableTargetConfigProcessor", "weblogic.ldap.EmbeddedLDAPPersistentCompletionProcessor", "weblogic.security.internal.SecurityPersistentCompletionProcessor", "weblogic.management.mbeans.custom.MigratableTargetConfigProcessor", "weblogic.webservice.saf.WSConfigUpdater", "weblogic.jms.module.JMSConfigurationProcessor", "weblogic.jms.backend.BEConfigUpdater", "weblogic.jms.bridge.internal.BridgeConfigUpdater", "weblogic.jdbc.module.JDBCConfigurationProcessor", "weblogic.xml.registry.XMLConfigUpdater", "weblogic.management.deploy.internal.ApplicationMigrationProcessor", "weblogic.servlet.logging.LogMigrationProcessor", "weblogic.logging.LoggingConfigurationProcessor", "weblogic.management.snmp.SNMPCompatibilityProcessor", "weblogic.management.provider.internal.ManagementConfigProcessor", "weblogic.upgrade.domain.configuration.SetBackwardsCompatibilityFlagsDelegate", "weblogic.upgrade.PluginActionDelegate"};

   public DomainConfigurationUpdatePlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1, CONFIGURATORS);
   }

   public void execute() throws PlugInException {
      this.log(UpgradeHelper.i18n("DomainConfigurationUpdatePlugin.execute.start_in_mem_config_upgrade"));
      super.execute();
      this.log(UpgradeHelper.i18n("DomainConfigurationUpdatePlugin.execute.completedin_mem_config_upgrade"));
      this.log(UpgradeHelper.i18n("DomainConfigurationUpdatePlugin.execute.config_written_out_later"));
   }
}
