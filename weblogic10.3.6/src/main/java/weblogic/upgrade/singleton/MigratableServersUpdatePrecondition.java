package weblogic.upgrade.singleton;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInGroupPreCondition;
import java.util.Arrays;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.upgrade.domain.configuration.SkipIfConfigurationPost81Precondition;

public class MigratableServersUpdatePrecondition extends SkipIfConfigurationPost81Precondition implements PlugInGroupPreCondition, DomainPlugInConstants {
   public boolean evaluate(PlugInContext var1) {
      DomainMBean var2 = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      String[] var3 = (String[])((String[])var1.get(OPTIONAL_GROUPS_KEY));
      boolean var4 = Arrays.asList(var3).contains("MIGRATABLE_SERVERS_UPGRADE_SELECTED_VALUE");
      boolean var5 = MigratableServersJDBCConfigPlugIn.doesDomainHaveMigratableServers(var2);
      boolean var6 = super.evaluate(var1);
      return var4 && var5 && var6;
   }
}
