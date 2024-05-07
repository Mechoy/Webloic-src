package weblogic.wsee.callback;

import java.util.Date;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.jws.container.Duration;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class CallbackClientBufferingHandler extends CallbackHandler {
   private static final boolean verbose = Verbose.isVerbose(CallbackClientBufferingHandler.class);
   public static final String CALLBACK_CLIENT_BUFFERING_DATA = "weblogic.wsee.callback.client.buffering";
   public static final String CALLBACK_CLIENT_BUFFERING_METHODNAME = "weblogic.wsee.callback.client.methodname";
   public static final String CALLBACK_CLIENT_BUFFERING_ARGS = "weblogic.wsee.callback.client.args";
   public static final String BINDING_TYPE = "weblogic.wsee.binding.type";

   public boolean handleRequest(MessageContext var1) {
      assert var1 != null;

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         WlMessageContext var3 = WlMessageContext.narrow(var1);
         CallbackClientBufferingData var4 = (CallbackClientBufferingData)var1.getProperty("weblogic.wsee.callback.client.buffering");
         if (var4 == null) {
            return true;
         } else {
            if (verbose) {
               Verbose.log((Object)"[CallbackClientBufferingHandler.handleRequest()] called");
            }

            if (var3.getDispatcher().getOperation().getType() != 1 && var3.getDispatcher().getOperation().getType() != 3 && !var1.containsProperty("weblogic.wsee.async.invoke")) {
               throw new JAXRPCException("Buffering only works with one way messages or asynchronous request/response messages.");
            } else {
               var1.setProperty("weblogic.wsee.binding.type", var3.getDispatcher().getWsdlPort().getBinding().getBindingType());
               int var5 = var4.getRetryCount();
               String var6 = var4.getRetryDelay();
               Duration var7 = new Duration(var6);
               long var8 = var7.convertToSeconds(new Date());
               if (verbose) {
                  Verbose.log((Object)("CallbackClientBufferingHandler: buffer to " + var4.getTargetURI() + " retryCount = " + var5 + ", retryDelay = " + var8 + " seconds."));
               }

               var2.removeProperty("weblogic.wsee.enclosing.container");
               String var10 = (String)var2.getProperty("weblogic.wsee.enclosing.jws.serviceuri");
               BufferManager.instance().bufferMessageWithServiceURI(var4.getTargetURI(), var10, new SOAPInvokeState(var2, false), var5, var8);
               return false;
            }
         }
      }
   }
}
