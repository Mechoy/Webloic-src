package weblogic.servlet.jsp;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;

public class HttpJspPageImpl extends HttpServlet implements HttpJspPage {
   private boolean jspInitCalled = false;

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      if (!this.jspInitCalled) {
         this.jspInit();
         this.jspInitCalled = true;
      }

   }

   public void init() throws ServletException {
      super.init();
      if (!this.jspInitCalled) {
         this.jspInit();
         this.jspInitCalled = true;
      }

   }

   public void destroy() {
      super.destroy();
      this.jspDestroy();
   }

   public void jspInit() {
   }

   public void jspDestroy() {
   }

   public void _jspService(HttpServletRequest var1, HttpServletResponse var2) throws IOException, ServletException {
      super.service(var1, var2);
   }
}
