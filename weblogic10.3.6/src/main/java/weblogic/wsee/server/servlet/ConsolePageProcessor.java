package weblogic.wsee.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.wsee.util.ToStringWriter;

public class ConsolePageProcessor implements Processor {
   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      if ("GET".equalsIgnoreCase(var1.getMethod()) && "admin".equalsIgnoreCase(var1.getQueryString())) {
         var2.setContentType("text/html");
         PrintWriter var4 = var2.getWriter();
         var4.write("<html><body>");
         var4.write("<h1>Admin page</h1>");
         var4.write("</body></html>");
         var4.close();
         return true;
      } else {
         return false;
      }
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
