package weblogic.wsee.async;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.util.Verbose;

public abstract class AbstractAsyncResponseBean implements ServiceLifecycle {
   private static final boolean verbose = Verbose.isVerbose(AbstractAsyncResponseBean.class);
   protected ServletEndpointContext _context;

   public void init(Object var1) {
      this._context = (ServletEndpointContext)var1;
   }

   public void destroy() {
   }

   @WebMethod
   @Oneway
   public void onAsyncDelivery() {
      WlMessageContext var1 = (WlMessageContext)this._context.getMessageContext();
      String var2 = "Unknown";
      String var3 = "";
      String var4 = (String)var1.getProperty("weblogic.wsee.addressing.RelatesTo");
      if (verbose) {
         MsgHeaders var5 = var1.getHeaders();
         MessageIdHeader var6 = (MessageIdHeader)var5.getHeader(MessageIdHeader.TYPE);
         var2 = var6 != null ? var6.getMessageId() : "Unknown";
         SequenceHeader var7 = (SequenceHeader)var5.getHeader(SequenceHeader.TYPE);
         if (var7 != null) {
            var3 = " on reliable sequence " + var7.getSequenceId() + " and seq num " + var7.getMessageNumber();
         } else {
            String var8 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqNumber");
            String var9 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
            if (var9 != null) {
               var3 = " on reliable sequence " + var9 + " and seq num " + var8;
            }
         }

         Verbose.log((Object)("In AbstractAsyncResponseBean.onAsyncDelivery for async response message " + var2 + " related to request msg " + var4 + var3));
      }

      try {
         Object var13;
         if ((var13 = var1.getProperty("weblogic.wsee.async.fault")) == null) {
            Object var14 = var1.getProperty("weblogic.wsee.async.result");
            this.handleResult(var4, var1, var14);
            return;
         }

         this.handleFault(var4, var1, var13);
      } finally {
         if (verbose) {
            Verbose.log((Object)("Leaving AbstractAsyncResponseBean.onAsyncDelivery for async response message " + var2 + " related to request msg " + var4 + var3));
         }

      }

   }

   protected abstract void handleResult(String var1, WlMessageContext var2, Object var3) throws JAXRPCException;

   protected abstract void handleFault(String var1, WlMessageContext var2, Object var3) throws JAXRPCException;
}
