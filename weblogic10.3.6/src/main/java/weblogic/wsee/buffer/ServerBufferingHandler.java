package weblogic.wsee.buffer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.jws.container.Duration;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.Verbose;

public class ServerBufferingHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(ServerBufferingHandler.class);
   public static final String BUFFER_ONEWAY_CONFIRM_OVERRIDE = "weblogic.wsee.handler.wlw81BufferCompatFlat";
   public static final String HANDLER_NAME = "SERVER_BUFFER_HANDLER";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ServerTransport var3 = (ServerTransport)var2.getDispatcher().getConnection().getTransport();
      if (var3.isReliable()) {
         if (verbose) {
            Verbose.log((Object)"Transport is reliable; skipping buffering");
         }

         return true;
      } else {
         MessageBuffer var4 = getMessageBufferConfig(var2);
         if (var4 == null) {
            if (verbose) {
               Verbose.log((Object)"Method is not buffered");
            }

            return true;
         } else if (var2.getProperty("weblogic.wsee.oneway.confirmed") == null && !var2.containsProperty("weblogic.wsee.handler.wlw81BufferCompatFlat")) {
            throw new JAXRPCException("Buffering only works with one way messages or asynchronous request/response messages.");
         } else {
            if (verbose) {
               Verbose.log((Object)"Buffering message");
            }

            int var5 = var4.retryCount();
            String var6 = var4.retryDelay();
            Duration var7 = new Duration(var6);
            long var8 = var7.convertToSeconds(new Date());
            if (verbose) {
               Verbose.log((Object)("Buffer retry count " + var5));
               Verbose.log((Object)("Buffer retry delay " + var8));
            }

            Serializable var10 = this.getBufferMessageBody(var1);
            BufferManager.instance().bufferMessage(var3.getServiceURI(), var10, var5, var8);
            var1.setProperty("weblogic.wsee.queued.invoke", "true");
            return false;
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ServerTransport var3 = (ServerTransport)var2.getDispatcher().getConnection().getTransport();
      if (!var3.isReliable()) {
      }

      return true;
   }

   static MessageBuffer getMessageBufferConfig(WlMessageContext var0) {
      Class var1 = var0.getDispatcher().getWsPort().getEndpoint().getJwsClass();

      assert var1 != null;

      Method var2 = findMethod(var1, var0.getDispatcher().getWsMethod().getMethodName());

      assert var2 != null;

      MessageBuffer var3 = (MessageBuffer)var2.getAnnotation(MessageBuffer.class);
      if (var3 == null) {
         var3 = (MessageBuffer)var1.getAnnotation(MessageBuffer.class);
      }

      return var3;
   }

   static Method findMethod(Class var0, String var1) {
      Method[] var2 = var0.getMethods();
      Method[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (var6.getName().equals(var1)) {
            return var6;
         }
      }

      return null;
   }

   private Serializable getBufferMessageBody(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         throw new JAXRPCException("Unsupported MessageContext; only SOAP messages are supported");
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         SOAPInvokeState var3 = new SOAPInvokeState(var2, false);
         var3.setSubject(ServerSecurityHelper.getCurrentSubject());
         return var3;
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
