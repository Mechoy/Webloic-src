package weblogic.deployment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.persistence.EntityManagerFactory;
import weblogic.diagnostics.debug.DebugLogger;

public final class EntityManagerFactoryProxyImpl implements InterceptingInvocationHandler {
   private final EntityManagerFactory emf;
   private final Method closeMethod;
   private final Method equalsMethod;
   private final String unitName;
   private String appName;
   private String moduleName;
   private InvocationHandlerInterceptor iceptor;
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugKodoWeblogic");

   public EntityManagerFactoryProxyImpl(EntityManagerFactory var1, String var2) {
      this.emf = var1;
      this.unitName = var2;

      try {
         this.closeMethod = EntityManagerFactory.class.getMethod("close", (Class[])null);
         this.equalsMethod = Object.class.getMethod("equals", Object.class);
      } catch (NoSuchMethodException var4) {
         throw new AssertionError("Couldn't get expected method: " + var4);
      }
   }

   public void setInterceptor(InvocationHandlerInterceptor var1) {
      this.iceptor = var1;
   }

   EntityManagerFactory getEntityManagerFactory() {
      return this.emf;
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public String getAppName() {
      return this.appName;
   }

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getUnitName() {
      return this.unitName;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (this.iceptor != null) {
         this.iceptor.preInvoke(var2, var3);
      }

      if (var2.equals(this.closeMethod)) {
         return null;
      } else {
         EntityManagerFactoryProxyImpl var4;
         if (var2.equals(this.equalsMethod) && var3.length == 1 && var3[0] instanceof Proxy) {
            try {
               var4 = (EntityManagerFactoryProxyImpl)Proxy.getInvocationHandler(var3[0]);
               var3 = new Object[]{var4.getEntityManagerFactory()};
            } catch (ClassCastException var7) {
            }
         }

         var4 = null;

         Object var8;
         try {
            var8 = var2.invoke(this.emf, var3);
         } catch (InvocationTargetException var6) {
            throw var6.getCause();
         }

         return this.iceptor != null ? this.iceptor.postInvoke(var2, var3, var8) : var8;
      }
   }
}
