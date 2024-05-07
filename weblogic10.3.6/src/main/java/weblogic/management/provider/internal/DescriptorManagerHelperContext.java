package weblogic.management.provider.internal;

import java.util.List;

public final class DescriptorManagerHelperContext {
   private List errors;
   private boolean transform;
   private boolean editable;
   private boolean validate;
   private boolean transformed;
   private boolean eProductionModeEnabled;
   private boolean rProductionModeEnabled;

   public void setErrors(List var1) {
      this.errors = var1;
   }

   public List getErrors() {
      return this.errors;
   }

   public boolean isTransform() {
      return this.transform;
   }

   public void setTransform(boolean var1) {
      this.transform = var1;
   }

   public boolean isValidate() {
      return this.validate;
   }

   public void setValidate(boolean var1) {
      this.validate = var1;
   }

   public boolean isEditable() {
      return this.editable;
   }

   public void setEditable(boolean var1) {
      this.editable = var1;
   }

   public boolean isTransformed() {
      return this.transformed;
   }

   public void setTransformed(boolean var1) {
      this.transformed = var1;
   }

   public boolean isEProductionModeEnabled() {
      return this.eProductionModeEnabled;
   }

   public void setEProductionModeEnabled(boolean var1) {
      this.eProductionModeEnabled = var1;
   }

   public boolean isRProductionModeEnabled() {
      return this.rProductionModeEnabled;
   }

   public void setRProductionModeEnabled(boolean var1) {
      this.rProductionModeEnabled = var1;
   }
}
