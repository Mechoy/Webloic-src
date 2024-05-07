package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;
import com.bea.adaptive.harvester.jmx.MBeanHarvesterManager;
import weblogic.diagnostics.harvester.HarvesterRuntimeException;

class JMXHarvesterControlImpl extends BaseHarvesterControlImpl {
   private static MBeanHarvesterManager harvesterLauncher;
   private JMXHarvesterConfig config;

   public JMXHarvesterControlImpl(JMXHarvesterConfig var1, boolean var2) {
      super(var1.getHarvesterName(), var1.getNamespace(), DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST, true, var2);
      this.config = var1;
   }

   public JMXHarvesterControlImpl(JMXHarvesterConfig var1, DelegateHarvesterControl.ActivationPolicy var2) {
      super(var1.getHarvesterName(), var1.getNamespace(), var2, true, false);
      this.config = var1;
   }

   private void initHarvesterManager() {
      if (harvesterLauncher == null) {
         harvesterLauncher = new MBeanHarvesterManager();
         harvesterLauncher.prepareHarvesterManager();
         harvesterLauncher.activateHarvesterManager();
      }

   }

   public void activate() {
      synchronized(this) {
         if (!this.isActive()) {
            this.initHarvesterManager();

            try {
               Harvester var2 = harvesterLauncher.allocateHarvester(this.config.getHarvesterName(), this.config.getNamespace(), this.config.getMbeanServer(), this.config.getCategorizer(), (Throwable[])null);
               var2.setAttributeNameTrackingEnabled(this.isAttributeNameTrackingEnabled());
               var2.setAttributeValidationEnabled(this.isAttributeValidationEnabled());
               var2.setRemoveAttributesWithProblems(true);
               this.setDelegate(var2);
               this.setActive(true);
            } catch (Exception var4) {
               throw new HarvesterRuntimeException(var4);
            }
         }

      }
   }
}
