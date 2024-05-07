package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;

interface DelegateHarvesterControl {
   boolean isActive();

   boolean isAttributeNameTrackingEnabled();

   void setAttributeNameTrackingEnabled(boolean var1);

   ActivationPolicy getActivationPolicy();

   Harvester getDelegate();

   boolean isDefaultDelegate();

   String getName();

   void activate();

   void deactivate();

   String getNamespace();

   void setAttributeValidationEnabled(boolean var1);

   public static enum ActivationPolicy {
      IMMEDIATE,
      ON_METADATA_REQUEST,
      ON_REGISTRATION,
      EXPLICIT;
   }
}
