package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInGroupPreCondition;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class NodeManagerCredentialsPrecondition implements PlugInGroupPreCondition {
   public boolean evaluate(PlugInContext var1) {
      boolean var2 = false;
      DomainMBean var3 = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      MachineMBean[] var4 = var3.getMachines();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            NodeManagerMBean var6 = var4[var5].getNodeManager();
            if (var6 != null && var6.getListenAddress() != null) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }
}
