package weblogic.wsee.jaxrpc;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Map;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;

public class JndiServiceImpl implements InvocationHandler, Serializable {
   private String implJndiName;
   private Map portNameMap;
   private transient ServiceImpl impl = null;

   JndiServiceImpl(String var1, Map var2) {
      this.implJndiName = var1;
      this.portNameMap = var2;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (this.impl == null) {
         InitialContext var4 = new InitialContext();
         this.impl = (ServiceImpl)var4.lookup("java:comp/env/" + this.implJndiName);

         assert this.impl != null : "Can't find ServiceImpl " + this.implJndiName;
      }

      if (var2.getDeclaringClass().isAssignableFrom(ServiceImpl.class)) {
         return this.callServiceMethod(var2, var3);
      } else {
         String var6 = var2.getName();
         this.checkGetPortMethod(var6, var2);
         var6 = var6.substring(3);
         String var5 = (String)this.portNameMap.get(var6);
         if (var5 == null) {
            var5 = var6;
         }

         return this.invokePortMethod(var5, var2);
      }
   }

   private Object invokePortMethod(String var1, Method var2) throws Throwable {
      try {
         Method var3 = ServiceImpl.class.getMethod("getPort", QName.class, Class.class);
         return var3.invoke(this.impl, new QName(var1), var2.getReturnType());
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new JAXRPCException("Failed to call method " + var2.getName() + var5, var5);
      }
   }

   private void checkGetPortMethod(String var1, Method var2) {
      if (!var1.startsWith("get")) {
         this.throwMethodError(var2.getName());
      }

      if (var2.getParameterTypes().length != 0) {
         this.throwMethodError(var2.getName());
      }

      if (!Remote.class.isAssignableFrom(var2.getReturnType())) {
         this.throwMethodError(var2.getName());
      }

   }

   private Object callServiceMethod(Method var1, Object[] var2) throws Throwable {
      try {
         return var1.invoke(this.impl, var2);
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new JAXRPCException("Failed to call method " + var1.getName() + var5, var5);
      }
   }

   private void throwMethodError(String var1) {
      throw new JAXRPCException("There is no suitable implementation for method " + var1);
   }
}
