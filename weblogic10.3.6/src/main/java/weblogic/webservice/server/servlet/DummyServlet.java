package weblogic.webservice.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @deprecated */
public class DummyServlet extends HttpServlet {
   private static final String WEBSERVICES_XML_FILE_NAME = "web-services.xml";
   private static final String WEBSERVICES_XML_PATH = "/WEB-INF/web-services.xml";
   private static final String errorMsg = "\n\n!!!\n\nThis web app tries to deploy web services.\nYou need to download webservice.jar file for that.\n\n!!!\n\n";

   public void init() throws ServletException {
      ServletContext var1 = this.getServletConfig().getServletContext();
      InputStream var2 = var1.getResourceAsStream("/WEB-INF/web-services.xml");
      if (var2 != null) {
         throw new ServletException("\n\n!!!\n\nThis web app tries to deploy web services.\nYou need to download webservice.jar file for that.\n\n!!!\n\n");
      }
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      PrintWriter var3 = var2.getWriter();
      var3.println("<html><body><h1>Error:</h1><p>");
      var3.println("\n\n!!!\n\nThis web app tries to deploy web services.\nYou need to download webservice.jar file for that.\n\n!!!\n\n");
      var3.println("</body></html>");
   }
}
