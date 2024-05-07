package weblogic.deployment;

import java.lang.reflect.InvocationHandler;

public interface InterceptingInvocationHandler extends InvocationHandler {
   void setInterceptor(InvocationHandlerInterceptor var1);
}
