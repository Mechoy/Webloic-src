package weblogic.wsee.server.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.servlet.HttpServerTransport;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsSkel;

public class SoapProcessor implements Processor {
   private static final boolean verbose = Verbose.isVerbose(SoapProcessor.class);

   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      if ("POST".equalsIgnoreCase(var1.getMethod())) {
         this.handlePost(var3, var1, var2);
         return true;
      } else {
         return false;
      }
   }

   private void handlePost(BaseWSServlet var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      assert var1.getPort() != null;

      WsPort var4 = var1.getPort();
      String var5 = var4.getWsdlPort().getBinding().getBindingType();
      HttpServerTransport var6 = new HttpServerTransport(var2, var3);
      WsSkel var7 = (WsSkel)var4.getEndpoint();

      try {
         Connection var8 = ConnectionFactory.instance().createServerConnection(var6, var5);
         var7.invoke(var8, var4);
      } catch (ConnectionException var9) {
         this.sendError(var3, var9, "Failed to create connection");
      } catch (Throwable var10) {
         this.sendError(var3, var10, "Unknown error");
      }

   }

   private void sendError(HttpServletResponse var1, Throwable var2, String var3) throws IOException {
      if (verbose) {
         Verbose.log("SOAP request failed", var2);
      }

      var1.sendError(500, var3 + var2);
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
