package weblogic.wsee.jaxws.client.async;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.client.Stub;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;

public class HandlerInvokerTube extends AbstractFilterTubeImpl implements ServiceCreationInterceptor {
   private HandlerInvoker invoker;
   private AsyncClientHandlerFeature achf;
   private WSBinding binding;
   private static final Logger LOGGER = Logger.getLogger(HandlerInvokerTube.class.getName());

   public HandlerInvokerTube(Tube var1, AsyncClientHandlerFeature var2, WSBinding var3) {
      super(var1);
      this.achf = var2;
      this.binding = var3;
   }

   protected HandlerInvokerTube(HandlerInvokerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.achf = var1.achf;
      this.binding = var1.binding;
      this.invoker = var1.invoker;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new HandlerInvokerTube(this, var1);
   }

   public void postCreateDispatch(WSBindingProvider var1) {
      this.init(var1);
   }

   public void postCreateProxy(WSBindingProvider var1, Class<?> var2) {
      this.init(var1);
   }

   private void init(WSBindingProvider var1) {
      Stub var2 = Proxy.isProxyClass(var1.getClass()) ? (Stub)Proxy.getInvocationHandler(var1) : (Stub)var1;
      this.invoker = HandlerInvokerFactory.getInvoker(this.achf, this.binding, var2);
   }

   public NextAction processException(Throwable var1) {
      if (this.invoker == null) {
         return this.doThrow(var1);
      } else {
         Packet var2 = this.invoker.handleException(var1);
         return var2 == null ? this.doThrow(var1) : this.doReturnWith(var2);
      }
   }

   public NextAction processRequest(Packet var1) {
      if (this.invoker == null) {
         return super.processRequest(var1);
      } else {
         if (LOGGER.isLoggable(Level.FINE) && var1.supports("com.sun.xml.ws.api.addressing.messageId")) {
            String var2 = (String)var1.get("com.sun.xml.ws.api.addressing.messageId");
            String var3 = AsyncTransportProvider.dumpPersistentContextContextProps(var1.persistentContext);
            LOGGER.fine("In HandlerInvokerTube, dispatching request path for msg id " + var2 + " persistentContext: " + var3);
         }

         return super.processRequest(var1);
      }
   }

   public NextAction processResponse(Packet var1) {
      if (this.invoker == null) {
         return super.processResponse(var1);
      } else {
         if (var1.supports("com.sun.xml.ws.api.addressing.messageId")) {
            String var2 = (String)var1.get("com.sun.xml.ws.api.addressing.messageId");
            String var3 = AsyncTransportProvider.dumpPersistentContextContextProps(var1.persistentContext);
            LOGGER.fine("In HandlerInvokerTube, dispatching response path for msg id " + var2 + " persistentContext: " + var3);
         }

         return this.doReturnWith(var1.getMessage() != null ? this.invoker.handleResponse(var1) : var1);
      }
   }
}
