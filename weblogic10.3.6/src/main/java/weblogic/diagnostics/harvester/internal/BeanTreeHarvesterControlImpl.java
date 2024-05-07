package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;

class BeanTreeHarvesterControlImpl extends BaseHarvesterControlImpl {
   private BeanTreeHarvesterImpl beanTreeHarvester;

   public BeanTreeHarvesterControlImpl(String var1, String var2) {
      this(var1, var2, DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST);
   }

   public BeanTreeHarvesterControlImpl(String var1, String var2, DelegateHarvesterControl.ActivationPolicy var3, boolean var4) {
      super(var1, var2, var3, true, var4);
   }

   public BeanTreeHarvesterControlImpl(String var1, String var2, DelegateHarvesterControl.ActivationPolicy var3) {
      super(var1, var2, var3, true, false);
   }

   public void deactivate() {
      synchronized(this) {
         if (this.beanTreeHarvester != null && this.isActive()) {
            this.beanTreeHarvester.deallocate();
            this.beanTreeHarvester = null;
            this.setDelegate((Harvester)null);
         }

         this.setActive(false);
      }
   }

   public void activate() {
      synchronized(this) {
         if (!this.isActive()) {
            this.beanTreeHarvester = BeanTreeHarvesterImpl.getInstance();
            this.beanTreeHarvester.setAttributeNameTrackingEnabled(this.isAttributeNameTrackingEnabled());
            this.beanTreeHarvester.setAttributeValidationEnabled(this.isAttributeValidationEnabled());
            this.beanTreeHarvester.setRemoveAttributesWithProblems(true);
            this.setDelegate(this.beanTreeHarvester);
            this.setActive(true);
         }

      }
   }
}
