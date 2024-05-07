package weblogic.servlet.internal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import weblogic.management.DeploymentException;

public final class RequestEventsFilter implements Filter {
   private EventsManager eventsManager;

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws ServletException, IOException {
      this.eventsManager.notifyRequestLifetimeEvent(var1, true);

      try {
         var3.doFilter(new EventsRequestWrapper((HttpServletRequest)var1), var2);
      } finally {
         this.eventsManager.notifyRequestLifetimeEvent(var1, false);
      }

   }

   public void init(FilterConfig var1) throws ServletException {
      ServletContext var2 = var1.getServletContext();
      if (!(var2 instanceof WebAppServletContext)) {
         throw new ServletException(new DeploymentException("Incorrect ServletContext runtime type in FilterConfig. Expecting 'WebAppServletContext', but got '" + var2.getClass().getName() + "'."));
      } else {
         this.eventsManager = ((WebAppServletContext)var2).getEventsManager();
      }
   }

   public void destroy() {
      this.eventsManager = null;
   }

   private class EventsRequestWrapper extends HttpServletRequestWrapper {
      private EventsRequestWrapper(HttpServletRequest var2) {
         super(var2);
      }

      public void setAttribute(String var1, Object var2) {
         Object var3 = this.getAttribute(var1);
         super.setAttribute(var1, var2);
         RequestEventsFilter.this.eventsManager.notifyRequestAttributeEvent(this, var1, var3, var2);
      }

      public void removeAttribute(String var1) {
         Object var2 = this.getAttribute(var1);
         super.removeAttribute(var1);
         RequestEventsFilter.this.eventsManager.notifyRequestAttributeEvent(this, var1, var2, (Object)null);
      }

      // $FF: synthetic method
      EventsRequestWrapper(HttpServletRequest var2, Object var3) {
         this(var2);
      }
   }
}
