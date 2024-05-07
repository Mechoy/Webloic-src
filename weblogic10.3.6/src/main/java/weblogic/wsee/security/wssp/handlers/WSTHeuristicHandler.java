package weblogic.wsee.security.wssp.handlers;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.dispatch.server.ServerDispatcher;

public class WSTHeuristicHandler extends GenericHandler implements WLHandler {
   private WSTHeuristicProcessor processor;

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleClosure(MessageContext var1) {
      return true;
   }

   public boolean handleRequest(MessageContext var1) {
      if (this.processor == null) {
         this.processor = new WSTHeuristicProcessor();
      }

      this.processor.setReserve(new Reserve((SoapMessageContext)var1));
      this.processor.processRequest(var1);
      return super.handleRequest(var1);
   }

   public boolean handleResponse(MessageContext var1) {
      if (this.processor == null) {
         return true;
      } else if (this.processor.processResponse(var1)) {
         WlMessageContext var2 = WlMessageContext.narrow(var1);

         try {
            ServerDispatcher var3 = (ServerDispatcher)var2.getDispatcher();
            HandlerIterator var4 = var3.getHandlerChain();
            var3.setContext(this.processor.getReserve().getSoapMessageContext());
            var3.dispatch();
            var4.setIndex(0);
            return false;
         } catch (WsException var5) {
            throw new WebServiceException("Failed to dispatch for heuristic approach", var5);
         }
      } else {
         return super.handleResponse(var1);
      }
   }

   public boolean handleFault(MessageContext var1) {
      if (this.processor.processResponse(var1)) {
         WlMessageContext var2 = WlMessageContext.narrow(var1);

         try {
            ServerDispatcher var3 = (ServerDispatcher)var2.getDispatcher();
            HandlerIterator var4 = var3.getHandlerChain();
            var3.setContext(this.processor.getReserve().getSoapMessageContext());
            var3.dispatch();
            var4.setIndex(0);
            return false;
         } catch (WsException var5) {
            throw new WebServiceException("Failed to dispatch for heuristic approach", var5);
         }
      } else {
         return super.handleFault(var1);
      }
   }

   private class Reserve extends WSTHeuristicProcessor.Reserve {
      private SoapMessageContext originalContext;

      public Reserve(SoapMessageContext var2) {
         this.originalContext = var2;
      }

      public SoapMessageContext getSoapMessageContext() {
         return this.originalContext;
      }
   }
}
