package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;

abstract class BaseHarvesterControlImpl implements DelegateHarvesterControl {
   private boolean active = false;
   private Harvester delegate;
   private DelegateHarvesterControl.ActivationPolicy activationPolicy;
   private String namespace;
   private String name;
   private boolean attributeNameTrackingEnabled;
   private boolean defaultDelegate;
   private boolean attributeTrackingEnabled;
   private boolean attributeValidationEnabled;

   public BaseHarvesterControlImpl(String var1, String var2, DelegateHarvesterControl.ActivationPolicy var3, boolean var4, boolean var5) {
      this.activationPolicy = DelegateHarvesterControl.ActivationPolicy.ON_METADATA_REQUEST;
      this.attributeValidationEnabled = false;
      this.namespace = var2;
      this.name = var1;
      this.activationPolicy = var3;
      this.defaultDelegate = var5;
      this.attributeNameTrackingEnabled = var4;
   }

   public boolean isDefaultDelegate() {
      return this.defaultDelegate;
   }

   public Harvester getDelegate() {
      return this.delegate;
   }

   public String getName() {
      return this.name;
   }

   public String getNamespace() {
      return this.namespace;
   }

   protected void setDelegate(Harvester var1) {
      this.delegate = var1;
   }

   protected void setActive(boolean var1) {
      this.active = var1;
   }

   public void activate() {
      this.active = true;
   }

   public void deactivate() {
      this.active = false;
   }

   public boolean isActive() {
      return this.active;
   }

   public DelegateHarvesterControl.ActivationPolicy getActivationPolicy() {
      return this.activationPolicy;
   }

   public void setActivationPolicy(DelegateHarvesterControl.ActivationPolicy var1) {
      this.activationPolicy = var1;
   }

   public boolean isAttributeNameTrackingEnabled() {
      return this.attributeNameTrackingEnabled;
   }

   public void setAttributeNameTrackingEnabled(boolean var1) {
      this.attributeNameTrackingEnabled = var1;
      if (this.delegate != null) {
         this.delegate.setAttributeNameTrackingEnabled(this.attributeNameTrackingEnabled);
      }

   }

   public void setAttributeValidationEnabled(boolean var1) {
      this.attributeValidationEnabled = var1;
   }

   public boolean isAttributeValidationEnabled() {
      return this.attributeValidationEnabled;
   }
}
