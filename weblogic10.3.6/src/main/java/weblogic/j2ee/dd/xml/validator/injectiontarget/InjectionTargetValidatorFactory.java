package weblogic.j2ee.dd.xml.validator.injectiontarget;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidator;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;

public class InjectionTargetValidatorFactory {
   public static AnnotationValidator getValidator(DescriptorBean var0) {
      if (!(var0 instanceof InjectionTargetBean)) {
         return null;
      } else {
         DescriptorBean var1 = var0.getDescriptor().getRootBean();
         return var1 instanceof ApplicationClientBean ? InjectionTargetValidatorFactory.Holder.CLIENT_VALIDATOR : InjectionTargetValidatorFactory.Holder.J2EE_VALIDATOR;
      }
   }

   static class Holder {
      private static AnnotationValidator CLIENT_VALIDATOR = new ClientValidator();
      private static AnnotationValidator J2EE_VALIDATOR = new J2EEValidator();
   }
}
