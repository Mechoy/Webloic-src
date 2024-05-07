package weblogic.j2ee.dd.xml.validator.lifecyclecallback;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidator;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;

public class LifecycleCallbackValidatorFactory {
   public static AnnotationValidator getValidator(DescriptorBean var0) {
      if (!(var0 instanceof LifecycleCallbackBean)) {
         return null;
      } else {
         DescriptorBean var1 = var0.getParentBean();
         if (var1 instanceof ApplicationClientBean) {
            return LifecycleCallbackValidatorFactory.Holder.CLIENT_VALIDATOR;
         } else if (var1 instanceof InterceptorBean) {
            return LifecycleCallbackValidatorFactory.Holder.INTERCEPTOR_VALIDATOR;
         } else if (!(var1 instanceof SessionBeanBean) && !(var1 instanceof MessageDrivenBeanBean) && !(var1 instanceof EntityBeanBean)) {
            return var1 instanceof J2eeEnvironmentBean ? LifecycleCallbackValidatorFactory.Holder.J2EE_VALIDATOR : null;
         } else {
            return LifecycleCallbackValidatorFactory.Holder.EJB_VALIDATOR;
         }
      }
   }

   static class Holder {
      private static AnnotationValidator CLIENT_VALIDATOR = new ClientValidator();
      private static AnnotationValidator J2EE_VALIDATOR = new J2EEValidator();
      private static AnnotationValidator EJB_VALIDATOR = new EJBValidator();
      private static AnnotationValidator INTERCEPTOR_VALIDATOR = new InterceptorValidator();
   }
}
