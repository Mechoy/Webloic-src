package weblogic.wsee.ws.dispatch.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.transaction.Transaction;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.transaction.TransactionHelper;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.http.HTTPClientTransport;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.message.UnknownMsgHeader;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.util.EndpointAddressUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.ws.dispatch.DispatcherImpl;

public class ConnectionHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(ConnectionHandler.class);
   public static final String OUTPUT_HEADERS = "weblogic.wsee.OutputHeaders";
   public static final String INPUT_HEADERS = "weblogic.wsee.InputHeaders";
   private static ConnectionFactory factory = ConnectionFactory.instance();
   public static final String WLW81_COMPAT_TX_VOID_RETURN = "weblogic.wsee.ws.dispatch.WLW81compatTxVoidReturn";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      this.writeOutputHeaders(var2);
      this.checkPossibleBlockingCondition(var2);
      Dispatcher var3 = var2.getDispatcher();

      try {
         ((DispatcherImpl)var3).setConnection(factory.createClientConnection(EndpointAddressUtil.getProtocolFromEndpointAddress((MessageContext)var2), var3.getWsdlPort().getBinding().getBindingType()));
      } catch (ConnectionException var7) {
         throw new InvocationException("Failed to create a connection", var7);
      }

      if (verbose) {
         Verbose.log((Object)var1);
      }

      try {
         var3.getConnection().send(var1);
      } catch (IOException var6) {
         throw new InvocationException("Failed to send message using connection:" + var3.getConnection(), var6);
      }

      if (var3.getOperation().getType() == 1 || var3.getOperation().getType() == 3 || var1.getProperty("weblogic.wsee.async.invoke") != null) {
         try {
            if ("true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.complex")) && var3.getConnection().getTransport() instanceof HTTPClientTransport) {
               int var4 = ((HTTPClientTransport)var3.getConnection().getTransport()).getResponseCode();
               if (var4 == 500) {
                  var1.setProperty("weblogic.wsee.addressing.client.hasexception", "true");
                  return true;
               }
            }

            var3.getConnection().getTransport().confirmOneway();
         } catch (IOException var5) {
            throw new InvocationException("Oneway failed", var5);
         }
      }

      return true;
   }

   private void checkPossibleBlockingCondition(WlMessageContext var1) {
      Dispatcher var2 = var1.getDispatcher();
      if ("jms".equalsIgnoreCase(var2.getWsdlPort().getTransport()) && (var2.getOperation().getType() == 0 || var2.getOperation().getType() == 2) && !var1.containsProperty("weblogic.wsee.async.invoke") && !var1.containsProperty("weblogic.wsee.ws.dispatch.WLW81compatTxVoidReturn") && this.isInTransaction()) {
         System.err.println("Potential blocking operation " + var2.getOperation().getName() + ": a synchronous request/response invocation within a transaction " + "using the JMS transport can cause deadlocks.  Please refer to WebLogic documentation for details.");
      }

   }

   private boolean isInTransaction() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      return var1 != null;
   }

   private void writeOutputHeaders(WlMessageContext var1) {
      List var2 = (List)var1.getProperty("weblogic.wsee.OutputHeaders");
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Element var4 = (Element)var3.next();
            var1.getHeaders().addHeader(new UnknownMsgHeader(var4));
         }

         ControlAPIUtil.unsetOutputHeaders((MessageContext)var1);
      }

   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      if (var1.getProperty("weblogic.wsee.async.invoke") != null) {
         return true;
      } else {
         try {
            var3.getConnection().receive(var1);
         } catch (IOException var6) {
            throw new InvocationException("Failed to receive message " + var6, var6);
         }

         if (verbose) {
            Verbose.log((Object)var1);
         }

         this.setInputHeaders(var1);
         if ("true".equals((String)var2.getProperty("weblogic.wsee.addressing.client.hasexception")) && !var2.hasFault()) {
            try {
               var3.getConnection().getTransport().confirmOneway();
            } catch (IOException var5) {
               throw new InvocationException("Oneway failed", var5);
            }
         }

         return true;
      }
   }

   private void setInputHeaders(MessageContext var1) {
      ((Map)var1.getProperty("weblogic.wsee.invoke_properties")).put("weblogic.wsee.InputHeaders", ControlAPIUtil.getInputHeaders(var1));
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
