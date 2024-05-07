package weblogic.j2ee.dd.xml.validator.lifecyclecallback;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import javax.interceptor.InvocationContext;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidatorHelper;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.utils.ErrorCollectionException;

class InterceptorValidator extends EJBValidator {
   protected Class getClass(DescriptorBean var1, ClassLoader var2) throws ClassNotFoundException {
      String var3 = ((LifecycleCallbackBean)var1).getLifecycleCallbackClass();
      if (var3 == null) {
         InterceptorBean var4 = (InterceptorBean)var1.getParentBean();
         var3 = var4.getInterceptorClass();
      }

      return AnnotationValidatorHelper.getClass(var3, var2);
   }

   protected Method guessMethodFromSignature(List<Method> var1) {
      Iterator var2 = var1.iterator();

      Method var3;
      Class[] var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Method)var2.next();
         var4 = var3.getParameterTypes();
      } while(var3.getReturnType() != Void.TYPE || var4.length != 1 || !InvocationContext.class.isAssignableFrom(var4[0]));

      return var3;
   }

   protected void checkParameters(Method var1, ErrorCollectionException var2) {
      Class[] var3 = var1.getParameterTypes();
      if (var3.length != 1 || !InvocationContext.class.isAssignableFrom(var3[0])) {
         var2.add(this.error(var1, "it should take an InvocationContext object as parameter in the case of EJB interceptor"));
      }
   }
}
