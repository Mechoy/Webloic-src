package weblogic.wsee.context;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.JAXRPCException;

public class WebServiceHttpSessionImpl implements WebServiceSession {
   private HttpServletRequest request;

   public WebServiceHttpSessionImpl(HttpServletRequest var1) {
      this.request = var1;
   }

   public Object getUnderlyingSession() throws JAXRPCException {
      return this.request.getSession();
   }

   public Object getAttribute(String var1) {
      return this.request.getSession().getAttribute(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.request.getSession().setAttribute(var1, var2);
   }

   public Iterator getAttributeNames() {
      Enumeration var1 = this.request.getSession().getAttributeNames();
      ArrayList var2 = new ArrayList();

      while(var1.hasMoreElements()) {
         var2.add(var1.nextElement());
      }

      return var2.iterator();
   }

   public void removeAttribute(String var1) {
      this.request.getSession().removeAttribute(var1);
   }

   public void invalidate() {
      this.request.getSession().invalidate();
   }
}
