package weblogic.iiop;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class ProxyDesc implements Serializable {
   private static final long serialVersionUID = 8369403353195736832L;
   private Class[] interfaces;
   private InvocationHandler handler;

   private ProxyDesc() {
   }

   public ProxyDesc(Proxy var1) {
      try {
         this.interfaces = var1.getClass().getInterfaces();
         Field var2 = Proxy.class.getDeclaredField("h");
         var2.setAccessible(true);
         this.handler = (InvocationHandler)var2.get(var1);
      } catch (IllegalAccessException var3) {
         throw (SecurityException)(new SecurityException(var3.getMessage())).initCause(var3);
      } catch (NoSuchFieldException var4) {
         throw (SecurityException)(new SecurityException(var4.getMessage())).initCause(var4);
      }
   }

   protected Object readResolve() throws ObjectStreamException, ClassNotFoundException {
      try {
         return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), this.interfaces, this.handler);
      } catch (IllegalArgumentException var2) {
         throw new ClassNotFoundException((String)null, var2);
      }
   }
}
