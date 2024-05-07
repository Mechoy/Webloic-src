package weblogic.wsee.buffer;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class BufferHelper {
   private static final boolean verbose = Verbose.isVerbose(BufferHelper.class);

   public static boolean isRuntimeRetryExceptionEnabled(MessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      MessageBuffer var2 = ServerBufferingHandler.getMessageBufferConfig(var1);
      if (var2 == null) {
         if (verbose) {
            Verbose.log((Object)"Method is not buffered");
         }

         throw new JAXRPCException("Method is not buffered. So the RetryException can NOT be thrown!");
      } else if (var1.getProperty("weblogic.wsee.oneway.confirmed") == null && !var1.containsProperty("weblogic.wsee.handler.wlw81BufferCompatFlat")) {
         throw new JAXRPCException("Buffering only works with one way messages or asynchronous request/response messages. So the RetryException can NOT be thrown!");
      } else {
         if (verbose) {
            Verbose.log((Object)"The method can throw a RetryException");
         }

         return true;
      }
   }
}
