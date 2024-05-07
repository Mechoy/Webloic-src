package weblogic.servlet.proxy;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import weblogic.servlet.FutureResponseServlet;
import weblogic.servlet.FutureServletResponse;
import weblogic.work.WorkManagerFactory;

public class GenericServlet extends FutureResponseServlet {
   public void service(HttpServletRequest var1, FutureServletResponse var2) {
      try {
         Cookie[] var3 = var1.getCookies();
         SocketConnResource var4 = new SocketConnResource("localhost", 7001);
         if (var3 != null && var3.length > 0) {
            WorkManagerFactory.getInstance().getSystem().schedule(new ProxyRequest(var4, var1, var2, var3));
         } else {
            WorkManagerFactory.getInstance().getSystem().schedule(new ProxyRequest(var4, var1, var2));
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void service(HttpServletRequestWrapper var1, HttpServletResponseWrapper var2) {
      this.service((HttpServletRequest)var1.getRequest(), (FutureServletResponse)var2.getResponse());
   }
}
