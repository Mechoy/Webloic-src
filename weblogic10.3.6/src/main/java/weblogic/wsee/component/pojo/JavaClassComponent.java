package weblogic.wsee.component.pojo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServiceLifecycle;
import weblogic.jws.ForceDotNetCompatibleBinding;
import weblogic.wsee.component.BaseComponent;
import weblogic.wsee.component.Component;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.server.ServletEndpointContextImpl;

public class JavaClassComponent extends BaseComponent implements Component {
   private static final boolean verbose = Verbose.isVerbose(JavaClassComponent.class);
   private ServletEndpointContextImpl endpointCtx;
   private Object target;
   private Class targetClass;
   private Map operations = new HashMap();

   public JavaClassComponent(Class var1, ServletContext var2) throws ComponentException {
      this.endpointCtx = new ServletEndpointContextImpl(var2, var1);
      this.targetClass = var1;
      this.target = this.createTarget();
   }

   public Object createTarget() throws ComponentException {
      Object var1 = null;

      try {
         var1 = this.targetClass.newInstance();
         this.callInit(var1);
         return var1;
      } catch (IllegalAccessException var3) {
         throw new ComponentException("Failed to create a new instance", var3);
      } catch (InstantiationException var4) {
         throw new ComponentException("Failed to create a new instance", var4);
      }
   }

   private void callInit(Object var1) throws ComponentException {
      if (var1 instanceof ServiceLifecycle) {
         ServiceLifecycle var2 = (ServiceLifecycle)var1;

         try {
            var2.init(this.endpointCtx);
         } catch (ServiceException var4) {
            throw new ComponentException("failed to call init method on the target " + var4);
         }
      }

   }

   public void destroy() {
      if (ServiceLifecycle.class.isInstance(this.target)) {
         ServiceLifecycle var1 = (ServiceLifecycle)this.target;
         var1.destroy();
      }

   }

   public void preinvoke(String var1, MessageContext var2) throws ComponentException {
      super.preinvoke(var1, var2);
      this.forceDotNetCompatibleBinding(var2);
   }

   private void forceDotNetCompatibleBinding(MessageContext var1) {
      ForceDotNetCompatibleBinding var2 = (ForceDotNetCompatibleBinding)this.targetClass.getAnnotation(ForceDotNetCompatibleBinding.class);
      if (var2 != null) {
         var1.setProperty("weblogic.wsee.dotnet.compatible.binding", var2.value());
      }

   }

   public Object invoke(String var1, Object[] var2, MessageContext var3) throws ComponentException {
      this.endpointCtx.setMessageContext(var3);
      Method var4 = (Method)this.operations.get(var1);
      if (var4 == null) {
         throw new ComponentException("Unable to find a method with name '" + var1 + "' in target :" + this.target);
      } else {
         Object var5 = null;

         try {
            var5 = var4.invoke(this.getTarget(var3), var2);
         } catch (IllegalAccessException var13) {
            throw new ComponentException("Failed to invoke method", var13);
         } catch (IllegalArgumentException var14) {
            throw new ComponentException("Failed to invoke method", var14);
         } catch (InvocationTargetException var15) {
            throw new ComponentException("Failed to invoke method", var15);
         } finally {
            this.endpointCtx.unSetMessageContext();
         }

         return var5;
      }
   }

   private Object getTarget(MessageContext var1) {
      Container var2 = ContainerFactory.getContainer(var1);
      return var2 != null ? var2.getTargetJWS() : this.target;
   }

   public void registerOperation(String var1, String var2, Class[] var3) throws ComponentException {
      assert var1 != null;

      assert var2 != null;

      if (verbose) {
         Verbose.log((Object)("Register method:" + var2 + " operation: " + var1));
      }

      Method[] var4 = this.targetClass.getMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (var2.equals(var7.getName())) {
            if (var3 == null) {
               this.operations.put(var1, var7);
               return;
            }

            Class[] var8 = var7.getParameterTypes();
            if (isAssignable(var3, var8)) {
               this.operations.put(var1, var7);
               return;
            }

            if (verbose) {
               Verbose.log((Object)("Found method: " + var7 + ", but arguments " + "does not match: " + Verbose.expand(var3)));
            }
         }
      }

      throw new ComponentException("Unable to find method '" + var2 + "' " + "operation '" + var1 + "' in target class: " + this.targetClass);
   }

   public Class getTargetClass() {
      return this.targetClass;
   }

   public Object getTarget() {
      return this.target;
   }

   public String toString() {
      return this.targetClass.getName() + " (POJO)";
   }
}
