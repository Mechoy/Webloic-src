package weblogic.wsee.ws.dispatch.client;

import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.codec.CodecFactory;
import weblogic.wsee.connection.ResponseListener;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.DispatcherImpl;
import weblogic.wsee.wsdl.WsdlBindingOperation;

public final class ClientDispatcher extends DispatcherImpl implements ResponseListener {
   public static final String OPERATION_NAME_PROPERTY = "weblogic.wsee.ws.dispatch.client.OperationName";
   private static final boolean verbose = Verbose.isVerbose(ClientDispatcher.class);
   private Map properties;

   public ClientDispatcher(WsMethod var1, WsPort var2, WsdlBindingOperation var3, Map var4, Map var5, Map var6) throws CodecException {
      if (verbose) {
         Verbose.logArgs("method", var1.getMethodName(), "wsPort", var2.getWsdlPort().getName(), "operation", var3.getName(), "input", var4);
      }

      this.properties = var6;
      this.setWsMethod(var1);
      this.setWsPort(var2);
      this.setBindingOperation(var3);
      this.setInParams(var4);
      this.setOutParams(var5);
      this.setMessageContext();
      updateEffectivePolicy(var2, var1);
   }

   public void dispatch() throws Throwable {
      WlMessageContext var1 = this.getContext();
      if (this.getWsMethod() != null) {
         var1.setProperty("weblogic.wsee.ws.dispatch.client.OperationName", this.getWsMethod().getOperationName().getLocalPart());
      }

      Object var2 = (Map)this.properties.get("weblogic.wsee.invoke_properties");
      if (var2 == null) {
         var2 = new ConcurrentHashMap();
         this.properties.put("weblogic.wsee.invoke_properties", var2);
      }

      if (verbose) {
         Verbose.log((Object)"Copy invocation properties");
      }

      this.fillProperties(var1, this.properties);
      synchronized(var2) {
         this.fillProperties(var1, (Map)var2);
      }

      this.setHandlerChain(new HandlerIterator(this.getWsPort().getInternalHandlerList()));
      if (verbose) {
         Verbose.say("Handler chain:");
         HandlerIterator var3 = this.getHandlerChain();

         for(int var4 = 0; var4 < var3.getHandlers().size(); ++var4) {
            Handler var5 = var3.getHandlers().get(var4);
            Verbose.say("\tHandler[" + var4 + "] = " + var5);
         }
      }

      if (verbose) {
         Verbose.log((Object)"Invoking handler chain");
      }

      synchronized(this) {
         this.getHandlerChain().handleRequest(var1);
      }

      switch (this.getOperation().getType()) {
         case 0:
         case 2:
            if (var1.getProperty("weblogic.wsee.async.invoke") == null) {
               if (var1.getProperty("weblogic.wsee.ws.dispatch.WLW81compatTxVoidReturn") != null) {
                  this.getHandlerChain().handleClosure(var1);
               } else {
                  this.handleResponse();
               }
            }
            break;
         case 1:
            if ("true".equals(var1.getProperty("weblogic.wsee.addressing.client.hasexception"))) {
               this.handleResponse();
            }
         case 3:
            this.getHandlerChain().handleClosure(var1);
      }

      if (verbose) {
         Verbose.log((Object)"Dispatch done");
      }

      if (var1.hasFault() && var1.getFault() != null) {
         throw var1.getFault();
      }
   }

   private WlMessageContext setMessageContext() throws CodecException {
      CodecFactory var1 = CodecFactory.instance();
      this.setCodec(var1.getCodec(this.getWsdlPort().getBinding()));
      if (verbose) {
         Verbose.log((Object)"Creating context");
      }

      WlMessageContext var2 = WlMessageContext.narrow(this.getCodec().createContext());
      this.setContext(var2);
      var2.setDispatcher(this);
      return var2;
   }

   private void fillProperties(WlMessageContext var1, Map var2) {
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();

         assert var4.getKey() instanceof String;

         String var5 = (String)var4.getKey();
         if (!var1.containsProperty(var5)) {
            var1.setProperty(var5, var4.getValue());
         }
      }

      EndpointReference var6 = DispatcherImpl.ServiceContext.get();
      if (var6 != null && !var1.containsProperty("weblogic.wsee.addressing.From")) {
         var1.setProperty("weblogic.wsee.addressing.From", var6);
      }

      DispatcherImpl.ServiceContext.acceptPropertiesIntoClient(var1);
   }

   private void handleResponse() {
      if (verbose) {
         Verbose.say("handleResponse called");
      }

      this.getHandlerChain().handleResponse(this.getContext());
   }

   public void responseReady() {
   }

   public synchronized void handleAsyncResponse(SOAPMessage var1) throws Throwable {
      if (verbose) {
         Verbose.say("handleAsyncResponse for called: msg = " + var1);
      }

      WlMessageContext var2 = this.getContext();
      this.handleResponse();
      if (verbose) {
         Verbose.log((Object)"Dispatch done");
      }

      if (var2.hasFault() && var2.getFault() != null) {
         throw var2.getFault();
      }
   }

   public void handleAsyncResponse(SOAPMessage var1, MessageContext var2) throws Throwable {
      if (verbose) {
         Verbose.say("handleAsyncResponse for called: msg = " + var1 + " mc = " + var2);
      }

      WlMessageContext var3 = (WlMessageContext)var2;
      this.setContext(var3);
      var3.setDispatcher(this);
      Object var4 = (Map)this.properties.get("weblogic.wsee.invoke_properties");
      if (var4 == null) {
         var4 = new ConcurrentHashMap();
         this.properties.put("weblogic.wsee.invoke_properties", var4);
      }

      if (verbose) {
         Verbose.log((Object)"Copy invocation properties");
      }

      this.fillProperties(var3, this.properties);
      synchronized(var4) {
         this.fillProperties(var3, (Map)var4);
      }

      this.setHandlerChain(new HandlerIterator(this.getWsPort().getInternalHandlerList()));
      this.getHandlerChain().handleAsyncResponse(this.getContext());
      if (verbose) {
         Verbose.log((Object)"Dispatch done");
      }

      if (var3.hasFault() && var3.getFault() != null) {
         throw var3.getFault();
      }
   }

   private static void updateEffectivePolicy(WsPort var0, WsMethod var1) {
      if (var1 != null) {
         Iterator var2 = var0.getEndpoint().getMethods();

         while(var2.hasNext()) {
            WsMethod var3 = (WsMethod)var2.next();
            if (var3.getMethodName().equals(var1.getMethodName())) {
               var1.setCachedEffectiveInboundPolicy(var3.getCachedEffectiveInboundPolicy());
               var1.setCachedEffectiveOutboundPolicy(var3.getCachedEffectiveOutboundPolicy());
               break;
            }
         }
      }

   }
}
