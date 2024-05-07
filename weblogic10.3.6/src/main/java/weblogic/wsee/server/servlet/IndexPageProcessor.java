package weblogic.wsee.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import weblogic.servlet.security.Utils;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.ws.WsPort;

public class IndexPageProcessor implements Processor {
   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      if ("GET".equals(var1.getMethod()) && var1.getQueryString() == null) {
         this.doIndexPage(var1, var2, var3.getPort());
         return true;
      } else {
         return false;
      }
   }

   private void doIndexPage(HttpServletRequest var1, HttpServletResponse var2, WsPort var3) throws IOException {
      PrintWriter var4 = var2.getWriter();
      var4.write("<html><body>");
      QName var5 = var3.getEndpoint().getService().getWsdlService().getName();
      String var6 = var5 == null ? "?" : Utils.encodeXSS(var5.toString());
      var4.write("<h1>Welcome to the " + var6 + " home page");
      StringBuffer var7 = new StringBuffer();
      var7.append(Utils.encodeXSS(var1.getScheme()));
      var7.append("://");
      var7.append(Utils.encodeXSS(var1.getServerName()));
      var7.append(":");
      var7.append(var1.getServerPort());
      var7.append(Utils.encodeXSS(var1.getRequestURI()));
      var7.append("?WSDL");
      String var8 = var7.toString();
      var4.write("<h3><a href='/wls_utc?wsdlUrl=" + URLEncoder.encode(var8) + "'>Test page</a></h3>");
      var4.write("<h3><a href='" + var8 + "' >WSDL page</a></h3>");
      var4.write("</body></html>");
      var4.close();
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
