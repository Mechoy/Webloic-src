package weblogic.servlet.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/** @deprecated */
public abstract class AuthFilter extends HttpServlet {
   /** @deprecated */
   public static final String TARGET_URL = "weblogic.formauth.targeturl";

   /** @deprecated */
   public final void service(ServletRequest var1, ServletResponse var2) {
      Integer var3 = (Integer)var1.getAttribute("weblogic.auth.result");
      var1.removeAttribute("weblogic.auth.result");
      boolean var5 = true;
      int var4;
      if (var3 == null) {
         var4 = 1;
      } else {
         var4 = var3;
      }

      switch (var4) {
         case -1:
            this.doPreAuth(var1, var2);
            break;
         case 0:
            var5 = this.doSuccessAuth(var1, var2);
            break;
         case 1:
         case 2:
            this.doFailAuth(var1, var2);
            break;
         default:
            this.doFailAuth(var1, var2);
      }

      if (!var5) {
         var1.setAttribute("weblogic.auth.result", new Integer(1));
      }

   }

   /** @deprecated */
   public void doPreAuth(ServletRequest var1, ServletResponse var2) {
   }

   /** @deprecated */
   public boolean doSuccessAuth(ServletRequest var1, ServletResponse var2) {
      return true;
   }

   /** @deprecated */
   public void doFailAuth(ServletRequest var1, ServletResponse var2) {
   }
}
