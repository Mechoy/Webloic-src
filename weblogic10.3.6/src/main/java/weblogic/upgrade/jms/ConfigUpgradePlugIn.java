package weblogic.upgrade.jms;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import weblogic.jms.backend.BEConfigUpdater;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class ConfigUpgradePlugIn extends AbstractPlugIn {
   private DomainMBean domainBean;

   public ConfigUpgradePlugIn(PlugInDefinition var1) throws PlugInException {
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
      this.updateStatus("Beginning upgrade of the domain bean tree for the domain \"" + this.domainBean.getName() + "\"...");

      try {
         UpgradeHelper.setupWLSClientLogger(this);
         BEConfigUpdater var1 = new BEConfigUpdater();
         var1.updateConfiguration(this.domainBean);
      } catch (ManagementException var5) {
         this.updateStatus("Got an exception: " + var5);
         throw new PlugInException(this.getName(), "Error updating configuration: " + var5, var5);
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

      this.updateStatus("Domain bean tree upgrade complete.");
   }

   private void updateStatus(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1 + ""));
   }
}
