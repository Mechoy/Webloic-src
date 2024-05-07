package weblogic.j2ee.customizers;

import weblogic.j2ee.descriptor.wl.AnnotatedClassBean;

public class AnnotatedClassBeanCustomizer implements weblogic.j2ee.descriptor.wl.customizers.AnnotatedClassBeanCustomizer {
   private AnnotatedClassBean _customized;
   private String _shortDescription;

   public AnnotatedClassBeanCustomizer(AnnotatedClassBean var1) {
      this._customized = var1;
   }

   public String getShortDescription() {
      if (this._shortDescription == null) {
         String var1 = this._customized.getAnnotatedClassName();
         String var2 = this._customized.getAnnotatedClassName();
         this._shortDescription = AnnotationLocalizer.getShortDescription(var1, var2);
      }

      return this._shortDescription;
   }
}
