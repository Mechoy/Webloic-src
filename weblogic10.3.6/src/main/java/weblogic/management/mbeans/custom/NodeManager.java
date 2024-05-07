package weblogic.management.mbeans.custom;

import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.VMMAdapterValidator;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class NodeManager extends ConfigurationMBeanCustomizer {
   public NodeManager(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   protected NodeManagerMBean getMBean() {
      return (NodeManagerMBean)this.getMbean();
   }

   public void setAdapter(String var1) {
      if (var1 != null) {
         NodeManagerMBean var2 = this.getMBean();
         String[] var3 = var1.split("_");
         var2.setAdapterName(var3[0]);
         var2.setAdapterVersion(var3[1]);
      }

   }

   public String getAdapter() {
      NodeManagerMBean var1 = this.getMBean();
      return var1.getAdapterName() + "_" + var1.getAdapterVersion();
   }

   public String[] getInstalledVMMAdapters() {
      return (String[])((String[])VMMAdapterValidator.getAvailableAdapters().toArray(new String[0]));
   }
}
