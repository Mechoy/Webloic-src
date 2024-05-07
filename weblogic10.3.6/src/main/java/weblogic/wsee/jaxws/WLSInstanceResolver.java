package weblogic.wsee.jaxws;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import com.sun.xml.ws.server.AbstractInstanceResolver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.WebServiceException;

public abstract class WLSInstanceResolver<T> extends AbstractInstanceResolver<T> {
   protected static final String WEB_SERVICE_CONTEXT_BINDING = "comp/WebServiceContext";
   @NotNull
   private T singleton;

   @NotNull
   public T resolve(Packet var1) {
      return this.getSingleton();
   }

   public T getSingleton() {
      if (this.singleton == null) {
         try {
            this.singleton = this.getFactory().create();
         } catch (Throwable var2) {
            throw new WebServiceException(var2);
         }
      }

      return this.singleton;
   }

   protected abstract Factory<T> getFactory();

   protected boolean isJsr250Needed() {
      return true;
   }

   public void start(WSWebServiceContext var1, WSEndpoint var2) {
      try {
         this.getFactory().publishContext(var1);
      } catch (Throwable var4) {
         throw new WebServiceException(var4);
      }

      Object var3 = this.getSingleton();
      getResourceInjector(var2).inject(var1, var3);
      if (this.isJsr250Needed()) {
         invokeMethod(this.findAnnotatedMethod(var3.getClass(), PostConstruct.class), var3, new Object[0]);
      }

   }

   public void dispose() {
      Object var1 = this.getSingleton();
      invokeMethod(this.findAnnotatedMethod(var1.getClass(), PreDestroy.class), var1, new Object[0]);
   }

   @NotNull
   public Invoker createInvoker() {
      return new WLSInvoker();
   }

   public interface Factory<T> {
      T create();

      void publishContext(WSWebServiceContext var1);
   }

   protected class WLSInvoker extends Invoker {
      public final Object invoke(Packet var1, Method var2, Object... var3) throws InvocationTargetException, IllegalAccessException {
         this.reportInvokeBegin(var1);
         Object var4 = this.invoke(WLSInstanceResolver.this.resolve(var1), var2, var3);
         this.reportInvokeEnd(var1);
         return var4;
      }

      public void start(WSWebServiceContext var1, WSEndpoint var2) {
         WLSInstanceResolver.this.start(var1, var2);
      }

      public void dispose() {
         WLSInstanceResolver.this.dispose();
      }

      Object invoke(Object var1, Method var2, Object... var3) throws IllegalAccessException, InvocationTargetException {
         return var2.invoke(var1, var3);
      }

      private void reportInvokeBegin(Packet var1) {
         MonitoringPipe.MonitoringPropertySet var2 = (MonitoringPipe.MonitoringPropertySet)var1.getSatellite(MonitoringPipe.MonitoringPropertySet.class);
         if (var2 != null) {
            var2.setExecutionBegin(System.nanoTime());
         }

      }

      private void reportInvokeEnd(Packet var1) {
         MonitoringPipe.MonitoringPropertySet var2 = (MonitoringPipe.MonitoringPropertySet)var1.getSatellite(MonitoringPipe.MonitoringPropertySet.class);
         if (var2 != null) {
            var2.setExecutionEnd(System.nanoTime());
         }

      }
   }
}
