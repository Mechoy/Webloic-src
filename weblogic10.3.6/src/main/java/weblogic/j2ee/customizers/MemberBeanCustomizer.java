package weblogic.j2ee.customizers;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.AnnotationInstanceBean;
import weblogic.j2ee.descriptor.wl.MemberBean;

public class MemberBeanCustomizer implements weblogic.j2ee.descriptor.wl.customizers.MemberBeanCustomizer {
   private MemberBean _customized;
   private AnnotationInstanceBean _belongingAnnotation;
   private String _shortDescription;

   public MemberBeanCustomizer(MemberBean var1) {
      this._customized = var1;
   }

   public void _postCreate() {
      if (this._customized instanceof DescriptorBean) {
         DescriptorBean var1 = ((DescriptorBean)this._customized).getParentBean();
         if (var1 instanceof AnnotationInstanceBean) {
            this._belongingAnnotation = (AnnotationInstanceBean)var1;
         }
      }

   }

   public String getOverrideValue() {
      String var1 = null;
      if (this._customized.getRequiresEncryption()) {
         var1 = this._customized.getSecuredOverrideValue();
      } else {
         var1 = this._customized.getCleartextOverrideValue();
      }

      return var1;
   }

   public void setOverrideValue(String var1) {
      if (var1 != null) {
         if (this._customized.getRequiresEncryption()) {
            this._customized.setSecuredOverrideValue(var1);
         } else {
            this._customized.setCleartextOverrideValue(var1);
         }

      }
   }

   public String getShortDescription() {
      if (this._shortDescription == null && this._belongingAnnotation != null) {
         String var1 = this._belongingAnnotation.getAnnotationClassName();
         String var2 = var1 + "." + this._customized.getMemberName();
         this._shortDescription = AnnotationLocalizer.getShortDescription(var1, var2);
      }

      return this._shortDescription;
   }
}
