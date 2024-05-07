package weblogic.wsee.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlService;

public class ServiceInfoProcessor implements Processor {
   private static final boolean verbose = Verbose.isVerbose(ServiceInfoProcessor.class);

   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      if ("GET".equalsIgnoreCase(var1.getMethod()) && "INFO".equalsIgnoreCase(var1.getQueryString())) {
         this.writeSystemInfo(var1, var2, var3);
         return true;
      } else {
         return false;
      }
   }

   private void writeSystemInfo(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      PrintWriter var4 = var2.getWriter();
      this.writeHeader(var4);
      this.writeWsService(var4, var3.getPort());
      this.writeWsdl(var4, var3.getPort());
      this.writeFooter(var4);
   }

   private void writeWsdl(PrintWriter var1, WsPort var2) {
      this.writeHeading("Web Service Description", var1);
      WsService var3 = var2.getEndpoint().getService();
      WsdlService var4 = var3.getWsdlService();
      WsdlDefinitions var5 = var4.getDefinitions();
      this.writePre(this.clean(var5.toString()), var1);
   }

   private void writeWsService(PrintWriter var1, WsPort var2) {
      this.writeHeading("Web Service Info", var1);
      WsService var3 = var2.getEndpoint().getService();
      this.writePre(this.clean(var3.toString()), var1);
   }

   private void writePre(String var1, PrintWriter var2) {
      var2.println("<table border='1' background='#FFFFEE'>");
      var2.println("<tr background='#FFFFEE'><td background='#FFFFEE'>");
      var2.println("<pre>");
      var2.println(var1);
      var2.println("</pre>");
      var2.println("</td></tr>");
      var2.println("<table>");
   }

   private void writeHeading(String var1, PrintWriter var2) {
      var2.println("<h2>");
      var2.println(var1);
      var2.println("</h2>");
   }

   private void writeFooter(PrintWriter var1) {
      var1.println("</body></html>");
   }

   private void writeHeader(PrintWriter var1) {
      var1.println("<html><body>");
      var1.println("<h1>Web Service System Info</h1>");
   }

   private String clean(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, "<>", true);
      StringBuffer var3 = new StringBuffer();

      while(var2.hasMoreTokens()) {
         String var4 = var2.nextToken();
         if ("<".equals(var4)) {
            var3.append("&lt;");
         } else if (">".equals(var4)) {
            var3.append("&gt;");
         } else {
            var3.append(var4);
         }
      }

      return var3.toString();
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
