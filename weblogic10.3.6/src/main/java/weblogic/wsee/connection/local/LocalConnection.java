package weblogic.wsee.connection.local;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.soap.SoapClientConnection;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.local.LocalDelegateServerTransport;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class LocalConnection implements Connection {
   public static final String INVOKE_REQUEST = "weblogic.wsee.local.invoke.request";
   public static final String INVOKE_RESPONSE = "weblogic.wsee.local.invoke.response";
   public static final String INVOKE_THROWABLE = "weblogic.wsee.local.invoke.throwable";
   public static final String PRIOR_CONTEXT = "weblogic.wsee.local.transport.prior.context";
   private static final boolean verbose = Verbose.isVerbose(LocalConnection.class);
   private Transport transport;
   private LoopbackConnection loopback;
   private SoapMessageContext loopbackCtx;
   private WlMessageContext incomingContext;

   public void setIncomingContext(MessageContext var1) {
      this.incomingContext = (WlMessageContext)var1;
   }

   public void send(MessageContext var1) throws IOException {
      String var2 = SoapClientConnection.getDestinationAddress(var1);
      if (verbose) {
         Verbose.log((Object)("address: " + var2));
      }

      if (var2 == null) {
         throw new ConnectionException("Unable to find endpoint address");
      } else {
         WsPort var3 = null;
         if (var2.startsWith("local://")) {
            var3 = this.getPort(var2.substring("local://".length() - 1));
         } else {
            URI var4 = this.getUri(var2);
            var3 = this.getPort(var4.getPath());
         }

         this.sendMessage(var1, var3);
      }
   }

   private void sendMessage(MessageContext var1, WsPort var2) throws ConnectionException {
      SOAPMessage var3 = null;
      SoapMessageContext var4 = null;
      if (var1 instanceof SoapMessageContext) {
         var4 = new SoapMessageContext(AsyncUtil.isSoap12(var1));
         Map var5 = (Map)var1.getProperty("weblogic.wsee.local.invoke.request");
         this.populateContext(var5, var4);
         var3 = ((SoapMessageContext)var1).getMessage();
         var4.setProperty("weblogic.wsee.local.transport.prior.context", var1);
         if (verbose) {
            Verbose.log((Object)("message: " + var3));
         }

         this.loopback = new LoopbackConnection(var3);
         if (this.incomingContext != null) {
            this.loopback.setTransport(new LocalDelegateServerTransport((ServerTransport)((ServerTransport)this.incomingContext.getDispatcher().getConnection().getTransport())));
         } else {
            this.loopback.setTransport(this.transport);
         }

         this.loopbackCtx = var4;
         WsSkel var8 = (WsSkel)var2.getEndpoint();

         try {
            var8.invoke(this.loopback, var2, var4);
         } catch (WsException var7) {
            throw new ConnectionException("Failed to send message to end component", var7);
         }
      } else {
         throw new ConnectionException("Not a SOAP message context");
      }
   }

   private void populateContext(Map var1, SOAPMessageContext var2) throws ConnectionException {
      if (var1 != null) {
         Iterator var3 = var1.keySet().iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (!(var4 instanceof String)) {
               throw new ConnectionException("Local invoke data object should be keyed by a String");
            }

            var2.setProperty((String)var4, var1.get(var4));
         }
      }

   }

   private WsPort getPort(String var1) throws ConnectionException {
      String var2 = WsRegistry.getURL(var1);
      String var3 = WsRegistry.getVersion(var1);
      WsPort var4 = WsRegistry.instance().lookup(var2, var3);
      if (var4 == null) {
         throw new ConnectionException("Unable to find port at address: " + var1);
      } else {
         return var4;
      }
   }

   private URI getUri(String var1) throws ConnectionException {
      URI var2 = null;

      try {
         var2 = new URI(var1);
         return var2;
      } catch (URISyntaxException var4) {
         throw new ConnectionException("Unable to find parse address: " + var1, var4);
      }
   }

   public void receive(MessageContext var1) throws IOException {
      SOAPMessage var2 = this.loopback.getMessage();
      if (var1 instanceof SOAPMessageContext) {
         SOAPMessageContext var3 = (SOAPMessageContext)var1;
         var3.setMessage(var2);
         Map var4 = (Map)this.loopbackCtx.getProperty("weblogic.wsee.local.invoke.response");
         this.populateContext(var4, var3);
         var3.setProperty("weblogic.wsee.local.invoke.throwable", this.loopbackCtx.getProperty("weblogic.wsee.local.invoke.throwable"));
      }

   }

   public void setTransport(Transport var1) {
      this.transport = var1;
   }

   public Transport getTransport() {
      return this.transport;
   }
}
