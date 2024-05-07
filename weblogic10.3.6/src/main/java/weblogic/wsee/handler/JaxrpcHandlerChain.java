package weblogic.wsee.handler;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class JaxrpcHandlerChain {
   private HandlerListImpl handlers;
   private boolean serverSide;
   private boolean hasRuntimeException = false;
   private int index;

   public JaxrpcHandlerChain(HandlerListImpl var1) {
      this.handlers = var1;
   }

   public static JaxrpcHandlerChain cloneChain(JaxrpcHandlerChain var0) {
      JaxrpcHandlerChain var1 = new JaxrpcHandlerChain(var0.handlers);
      var1.setServerSide(var0.isServerSide());
      return var1;
   }

   public static JaxrpcHandlerChain deepCloneChain(JaxrpcHandlerChain var0) {
      HandlerListImpl var1 = new HandlerListImpl(var0.handlers);
      JaxrpcHandlerChain var2 = new JaxrpcHandlerChain(var1);
      var2.setServerSide(var0.isServerSide());
      return var2;
   }

   public void init() throws HandlerException {
      this.handlers.init();
   }

   public void setServerSide(boolean var1) {
      this.serverSide = var1;
   }

   public boolean isServerSide() {
      return this.serverSide;
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);

      for(this.index = 0; this.index < this.handlers.size(); ++this.index) {
         try {
            if (!this.handlers.get(this.index).handleRequest(var2)) {
               return false;
            }
         } catch (ClassCastException var4) {
            Verbose.log((Object)("Exception: " + var4.getMessage()));
            return false;
         } catch (SOAPFaultException var5) {
            var2.setFault(var5);
            return false;
         } catch (Throwable var6) {
            var2.setFault(var6);
            this.hasRuntimeException = true;
            return false;
         }
      }

      --this.index;
      return true;
   }

   public boolean handleResponse(MessageContext var1) {
      if (this.hasRuntimeException) {
         return true;
      } else {
         for(WlMessageContext var2 = WlMessageContext.narrow(var1); this.index >= 0; --this.index) {
            if (var2.hasFault()) {
               try {
                  if (!this.handlers.get(this.index).handleFault(var2)) {
                     return false;
                  }
               } catch (Throwable var4) {
                  this.hasRuntimeException = true;
                  return false;
               }
            } else {
               try {
                  if (!this.handlers.get(this.index).handleResponse(var1)) {
                     return false;
                  }
               } catch (Throwable var5) {
                  this.hasRuntimeException = true;
                  var2.setFault(var5);
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean hasRuntimeException() {
      return this.hasRuntimeException;
   }

   public void destroy() {
      this.handlers.destroy();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("handlers", this.handlers);
      var1.end();
   }
}
