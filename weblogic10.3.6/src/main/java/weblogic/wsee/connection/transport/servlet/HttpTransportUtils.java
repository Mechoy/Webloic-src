package weblogic.wsee.connection.transport.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.message.WlMessageContext;

public class HttpTransportUtils {
   public static final HttpServletRequest getHttpServletRequest(MessageContext var0) {
      if (var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME") != null && var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME").equals("true")) {
         HttpServletRequest var3 = (HttpServletRequest)var0.getProperty("javax.xml.ws.servlet.request");
         return var3;
      } else {
         WlMessageContext var1 = WlMessageContext.narrow(var0);
         if (var1.getDispatcher() == null) {
            return null;
         } else {
            Transport var2 = var1.getDispatcher().getConnection().getTransport();
            return var2 instanceof HttpServerTransport ? ((HttpServerTransport)var2).getRequest() : null;
         }
      }
   }

   public static final HttpServletResponse getHttpServletResponse(MessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      Transport var2 = var1.getDispatcher().getConnection().getTransport();
      return var2 instanceof HttpServerTransport ? ((HttpServerTransport)var2).getResponse() : null;
   }
}
