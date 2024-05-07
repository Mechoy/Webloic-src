package weblogic.j2ee.customizers;

import weblogic.j2ee.descriptor.wl.AnnotationInstanceBean;

public class AnnotationInstanceBeanCustomizer implements weblogic.j2ee.descriptor.wl.customizers.AnnotationInstanceBeanCustomizer {
   private AnnotationInstanceBean _customized;
   private String _shortDescription;

   public AnnotationInstanceBeanCustomizer(AnnotationInstanceBean var1) {
      assert var1 != null : "The AnnotationInstanceBean to be customized cannot be null";

      this._customized = var1;
   }

   public String getShortDescription() {
      if (this._shortDescription == null) {
         String var1 = this._customized.getAnnotationClassName();
         String var2 = this._customized.getAnnotationClassName();
         this._shortDescription = AnnotationLocalizer.getShortDescription(var1, var2);
      }

      return this._shortDescription;
   }
}
