package weblogic.deployment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class InterceptingInvocationHandlerImpl implements InvocationHandler {
   private final Object delegate;
   private final InvocationHandlerInterceptor iceptor;

   InterceptingInvocationHandlerImpl(Object var1, InvocationHandlerInterceptor var2) {
      this.delegate = var1;
      this.iceptor = var2;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      this.iceptor.preInvoke(var2, var3);
      Object var4 = null;

      try {
         var4 = var2.invoke(this.delegate, var3);
      } catch (InvocationTargetException var6) {
         throw var6.getCause();
      }

      return this.iceptor.postInvoke(var2, var3, var4);
   }
}
